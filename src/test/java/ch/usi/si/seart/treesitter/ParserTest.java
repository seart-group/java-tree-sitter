package ch.usi.si.seart.treesitter;

import ch.usi.si.seart.treesitter.exception.parser.ParsingException;
import lombok.Cleanup;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.function.Executable;
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
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

class ParserTest extends BaseTest {

    @TempDir
    private static Path tmp;
    private static Path tmpFile;

    private static Parser parser;

    private static final String source = "print(\"hi\")\n";

    @BeforeAll
    static void beforeAll() throws IOException {
        tmpFile = Files.createFile(tmp.resolve("print.py"));
        Files.writeString(tmpFile, source);
        parser = Parser.getFor(Language.PYTHON);
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
        checkRange(root);
    }

    @Test
    void testParseFile() {
        @Cleanup Tree tree = parser.parse(tmpFile);
        Assertions.assertFalse(tree.isNull());
        Node root = tree.getRootNode();
        Assertions.assertEquals("module", root.getType());
        checkRange(root);
    }

    @Test
    void testSetLanguage() {
        @Cleanup Parser parser = Parser.builder().language(Language.PYTHON).build();
        parser.setLanguage(Language.JAVA);
        String source = "public class _ {}\n";
        @Cleanup Tree tree = parser.parse(source);
        Assertions.assertFalse(tree.isNull());
        Node root = tree.getRootNode();
        Assertions.assertEquals("program", root.getType());
        checkRange(root);
    }

    private void checkRange(Node node) {
        Range range = node.getRange();
        Point start = range.getStartPoint();
        Point end = range.getEndPoint();
        Assertions.assertEquals(_0_0_, start);
        Assertions.assertEquals(_1_0_, end);
    }

    @Test
    void testSetIncludedRanges() {
        List<Range> ranges;
        ranges = parser.getIncludedRanges();
        Assertions.assertTrue(ranges.isEmpty());
        Range range = new Range(0, 1, _0_0_, _1_0_);
        parser.setIncludedRanges(range);
        ranges = parser.getIncludedRanges();
        Assertions.assertFalse(ranges.isEmpty());
        Assertions.assertEquals(1, ranges.size());
        Range copy = ranges.stream().findFirst().orElseGet(Assertions::fail);
        Assertions.assertEquals(range, copy);
        parser.setIncludedRanges();
        ranges = parser.getIncludedRanges();
        Assertions.assertTrue(ranges.isEmpty());
        List<Range> list = List.of(
                new Range(0, 1, _0_0_, _1_0_),
                new Range(1, 2, _1_0_, _1_1_)
        );
        parser.setIncludedRanges(list);
        ranges = parser.getIncludedRanges();
        Assertions.assertFalse(ranges.isEmpty());
        Assertions.assertEquals(2, ranges.size());
        Assertions.assertEquals(list, ranges);
    }

    @Test
    @SuppressWarnings("DataFlowIssue")
    @SneakyThrows(URISyntaxException.class)
    void testSetTimeout() {
        @Cleanup Parser parser = Parser.getFor(Language.JAVA);
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
    }

    @Test
    void testSetTimeoutNanoseconds() {
        Assertions.assertEquals(0, parser.getTimeout());
        parser.setTimeout(Duration.ofNanos(500));
        Assertions.assertEquals(0, parser.getTimeout());
        parser.setTimeout(500, TimeUnit.NANOSECONDS);
        Assertions.assertEquals(0, parser.getTimeout());
    }

    @Test
    void testBuilder() {
        Duration duration = Duration.ofSeconds(3);
        Range range = new Range(0, 1, _0_0_, _1_0_);
        @Cleanup Parser parser = Parser.builder()
                .language(Language.JAVA)
                .timeout(duration)
                .range(range)
                .build();
        Assertions.assertFalse(parser.isNull());
        Assertions.assertEquals(duration.toMillis() * 1000, parser.getTimeout());
        List<Range> ranges = parser.getIncludedRanges();
        Assertions.assertFalse(ranges.isEmpty());
        Assertions.assertEquals(1, ranges.size());
        Range copy = ranges.stream().findFirst().orElseGet(Assertions::fail);
        Assertions.assertEquals(range, copy);
    }

    @Test
    void testToBuilder() {
        Parser.Builder builder = parser.toBuilder();
        Duration duration = Duration.ofSeconds(1);
        Range range = new Range(0, 2, _0_0_, _1_1_);
        @Cleanup Parser other = builder.language(Language.JAVA)
                .timeout(duration)
                .ranges(List.of(range))
                .build();
        Assertions.assertFalse(other.isNull());
        Assertions.assertNotEquals(parser, other);
        Assertions.assertNotEquals(parser.getTimeout(), other.getTimeout());
        Assertions.assertNotEquals(parser.getLanguage(), other.getLanguage());
        Assertions.assertNotEquals(parser.getIncludedRanges(), other.getIncludedRanges());
    }

    private static class SetterExceptionProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            Range[] array = new Range[]{ null };
            List<Range> list = Arrays.asList(array);
            Executable nullLanguage = () -> parser.setLanguage(null);
            Executable invalidLanguage = () -> parser.setLanguage(invalid);
            Executable nullDuration = () -> parser.setTimeout(null);
            Executable nullTimeUnit = () -> parser.setTimeout(100L, null);
            Executable negativeTimeUnit = () -> parser.setTimeout(-1L, TimeUnit.SECONDS);
            Executable negativeTimeout = () -> parser.setTimeout(-1L);
            Executable nullRangeList = () -> parser.setIncludedRanges((List<Range>) null);
            Executable rangeListWithNulls = () -> parser.setIncludedRanges(list);
            Executable rangeListUnordered = () -> parser.setIncludedRanges(
                    List.of(
                            new Range(1, 2, _1_0_, _1_1_),
                            new Range(0, 1, _0_0_, _1_0_)
                    )
            );
            Executable nullRangeArray = () -> parser.setIncludedRanges((Range[]) null);
            Executable rangeArrayWithNulls = () -> parser.setIncludedRanges(array);
            Executable rangeArrayUnordered = () -> parser.setIncludedRanges(
                    new Range(1, 2, _1_0_, _1_1_),
                    new Range(0, 1, _0_0_, _1_0_)
            );
            return Stream.of(
                    Arguments.of(NullPointerException.class, nullLanguage),
                    Arguments.of(UnsatisfiedLinkError.class, invalidLanguage),
                    Arguments.of(NullPointerException.class, nullDuration),
                    Arguments.of(NullPointerException.class, nullTimeUnit),
                    Arguments.of(IllegalArgumentException.class, negativeTimeUnit),
                    Arguments.of(IllegalArgumentException.class, negativeTimeout),
                    Arguments.of(NullPointerException.class, nullRangeList),
                    Arguments.of(NullPointerException.class, rangeListWithNulls),
                    Arguments.of(IllegalArgumentException.class, rangeListUnordered),
                    Arguments.of(NullPointerException.class, nullRangeArray),
                    Arguments.of(NullPointerException.class, rangeArrayWithNulls),
                    Arguments.of(IllegalArgumentException.class, rangeArrayUnordered)
            );
        }
    }

    @ParameterizedTest(name = "[{index}] {0}")
    @ArgumentsSource(SetterExceptionProvider.class)
    void testSetterThrows(Class<Throwable> type, Executable executable) {
        Assertions.assertThrows(type, executable);
    }

    private static class BuilderExceptionProvider implements ArgumentsProvider {

        @Override
        @SuppressWarnings("resource")
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            Executable nothing = () -> Parser.builder().build();
            Executable nullLanguage = () -> Parser.builder().language(null);
            Executable invalidLanguage = () -> Parser.builder().language(invalid);
            Executable nullDuration = () -> Parser.builder().timeout(null);
            Executable nullTimeUnit = () -> Parser.builder().timeout(100L, null);
            Executable negativeTimeUnit = () -> Parser.builder().timeout(-1L, TimeUnit.SECONDS);
            Executable negativeTimeout = () -> Parser.builder().timeout(-1L);
            Executable nullRangeList = () -> Parser.builder().ranges((List<Range>) null);
            Executable rangeListWithNulls = () -> Parser.builder().ranges(Arrays.asList(new Range[]{ null }));
            Executable nullRangeArray = () -> Parser.builder().ranges((Range[]) null);
            Executable rangeArrayWithNulls = () -> Parser.builder().ranges(new Range[]{ null });
            Executable nullRange = () -> Parser.builder().range(null);
            Executable unorderedRanges = () -> Parser.builder()
                    .language(Language.JAVA)
                    .ranges(
                            new Range(1, 2, _1_0_, _1_1_),
                            new Range(0, 1, _0_0_, _1_0_)
                    ).build();
            return Stream.of(
                    Arguments.of(NullPointerException.class, nothing),
                    Arguments.of(NullPointerException.class, nullLanguage),
                    Arguments.of(UnsatisfiedLinkError.class, invalidLanguage),
                    Arguments.of(NullPointerException.class, nullDuration),
                    Arguments.of(NullPointerException.class, nullTimeUnit),
                    Arguments.of(IllegalArgumentException.class, negativeTimeUnit),
                    Arguments.of(IllegalArgumentException.class, negativeTimeout),
                    Arguments.of(NullPointerException.class, nullRangeList),
                    Arguments.of(NullPointerException.class, rangeListWithNulls),
                    Arguments.of(NullPointerException.class, nullRangeArray),
                    Arguments.of(NullPointerException.class, rangeArrayWithNulls),
                    Arguments.of(NullPointerException.class, nullRange),
                    Arguments.of(IllegalArgumentException.class, unorderedRanges)
            );
        }
    }

    @ParameterizedTest(name = "[{index}] {0}")
    @ArgumentsSource(BuilderExceptionProvider.class)
    void testBuilderThrows(Class<Throwable> type, Executable executable) {
        Assertions.assertThrows(type, executable);
    }
}
