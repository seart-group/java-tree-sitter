package ch.usi.si.seart.treesitter;

import ch.usi.si.seart.treesitter.exception.ParsingException;
import lombok.Cleanup;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

class ParserTest extends TestBase {

    @TempDir
    private static Path tmp;
    private static Path tmpFile;

    private static Parser parser;

    private static final String source = "print(\"hi\")\n";

    @BeforeAll
    static void beforeAll() throws IOException {
        tmpFile = Files.createFile(tmp.resolve("print.py"));
        Files.writeString(tmpFile, source);
        parser = new Parser(Language.PYTHON);
    }

    @AfterAll
    static void afterAll() {
        parser.close();
    }

    @Test
    void testParseString() {
        @Cleanup Tree tree = parser.parse(source);
        Assertions.assertFalse(tree.isNull());
        Node root = tree.getRootNode();
        Assertions.assertEquals("module", root.getType());
        Range range = root.getRange();
        Point start = range.getStartPoint();
        Point end = range.getEndPoint();
        Assertions.assertEquals(new Point(0, 0), start);
        Assertions.assertEquals(new Point(1, 0), end);
    }

    @Test
    void testParseFile() {
        @Cleanup Tree tree = parser.parse(tmpFile);
        Assertions.assertFalse(tree.isNull());
        Node root = tree.getRootNode();
        Assertions.assertEquals("module", root.getType());
        Range range = root.getRange();
        Point start = range.getStartPoint();
        Point end = range.getEndPoint();
        Assertions.assertEquals(new Point(0, 0), start);
        Assertions.assertEquals(new Point(1, 0), end);
    }

    @Test
    void testSetLanguage() {
        @Cleanup Parser parser = new Parser(Language.PYTHON);
        parser.setLanguage(Language.JAVA);
        String source = "public class _ {}\n";
        @Cleanup Tree tree = parser.parse(source);
        Assertions.assertFalse(tree.isNull());
        Node root = tree.getRootNode();
        Assertions.assertEquals("program", root.getType());
        Range range = root.getRange();
        Point start = range.getStartPoint();
        Point end = range.getEndPoint();
        Assertions.assertEquals(new Point(0, 0), start);
        Assertions.assertEquals(new Point(1, 0), end);
    }

    @Test
    @SuppressWarnings("DataFlowIssue")
    @SneakyThrows(URISyntaxException.class)
    void testSetTimeout() {
        @Cleanup Parser parser = new Parser(Language.JAVA);
        Assertions.assertEquals(0, parser.getTimeout());
        parser.setTimeout(10);
        Assertions.assertEquals(10, parser.getTimeout());
        Path path = Path.of(getClass().getClassLoader().getResource("deep_string_concat").toURI());
        Assertions.assertThrows(ParsingException.class, () -> parser.parse(path));
        TimeUnit unit = TimeUnit.SECONDS;
        parser.setTimeout(1, unit);
        Assertions.assertEquals(unit.toMicros(1), parser.getTimeout());
        Assertions.assertFalse(parser.parse(path).isNull());
        Duration duration = Duration.ofSeconds(1);
        parser.setTimeout(duration);
        Assertions.assertEquals(duration.toMillis() * 1000, parser.getTimeout());
        Assertions.assertFalse(parser.parse(path).isNull());
        parser.setTimeout(Duration.ofNanos(500));
        Assertions.assertEquals(0, parser.getTimeout());
        parser.setTimeout(500, TimeUnit.NANOSECONDS);
        Assertions.assertEquals(0, parser.getTimeout());
    }

    private static class ConstructorExceptionProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
            return Stream.of(
                    Arguments.of(NullPointerException.class, null),
                    Arguments.of(UnsatisfiedLinkError.class, Language._INVALID_)
            );
        }
    }

    @SuppressWarnings("resource")
    @ParameterizedTest(name = "[{index}] {0}")
    @ArgumentsSource(ConstructorExceptionProvider.class)
    void testConstructorThrows(Class<Throwable> throwableType, Language language) {
        Assertions.assertThrows(throwableType, () -> new Parser(language));
    }

    private static class SetTimeoutExceptionProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
            return Stream.of(
                    Arguments.of(NullPointerException.class, null),
                    Arguments.of(IllegalArgumentException.class, -1L)
            );
        }
    }

    @ParameterizedTest(name = "[{index}] {0}")
    @ArgumentsSource(SetTimeoutExceptionProvider.class)
    void testSetTimeoutThrows(Class<Throwable> throwableType, Long timeout) {
        Assertions.assertThrows(throwableType, () -> parser.setTimeout(timeout));
    }

    private static class SetTimeoutWitTimeUnitExceptionProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
            return Stream.of(
                    Arguments.of(NullPointerException.class, 100L, null),
                    Arguments.of(IllegalArgumentException.class, -1L, TimeUnit.MICROSECONDS)
            );
        }
    }

    @ParameterizedTest(name = "[{index}] {0}")
    @ArgumentsSource(SetTimeoutWitTimeUnitExceptionProvider.class)
    void testSetTimeoutThrows(Class<Throwable> throwableType, Long timeout, TimeUnit timeUnit) {
        Assertions.assertThrows(throwableType, () -> parser.setTimeout(timeout, timeUnit));
    }
}
