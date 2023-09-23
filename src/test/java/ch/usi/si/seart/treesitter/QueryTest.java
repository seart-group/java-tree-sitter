package ch.usi.si.seart.treesitter;

import ch.usi.si.seart.treesitter.exception.query.QueryCaptureException;
import ch.usi.si.seart.treesitter.exception.query.QueryFieldException;
import ch.usi.si.seart.treesitter.exception.query.QueryNodeTypeException;
import ch.usi.si.seart.treesitter.exception.query.QueryStructureException;
import ch.usi.si.seart.treesitter.exception.query.QuerySyntaxException;
import lombok.Cleanup;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.stream.Stream;

class QueryTest extends TestBase {

    static Query query;

    @BeforeAll
    static void beforeAll() {
        query = new Query(Language.JAVA, "(_) @capture");
    }

    @AfterAll
    static void afterAll() {
        query.close();
    }

    @Test
    void testQuery() {
        Assertions.assertNotNull(query, "Query is not null");
    }

    private static class QueryExceptionProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
            return Stream.of(
                    Arguments.of(NullPointerException.class, null, null),
                    Arguments.of(NullPointerException.class, Language.JAVA, null),
                    Arguments.of(NullPointerException.class, null, "(_)"),
                    Arguments.of(UnsatisfiedLinkError.class, Language._INVALID_, "(_)"),
                    Arguments.of(QueryCaptureException.class, Language.JAVA, "(#eq? @key @value)"),
                    Arguments.of(QueryFieldException.class, Language.JAVA, "(program unknown: (_))"),
                    Arguments.of(QueryNodeTypeException.class, Language.JAVA, "(null)"),
                    Arguments.of(QueryStructureException.class, Language.JAVA, "(program (program))"),
                    Arguments.of(QuerySyntaxException.class, Language.JAVA, "()")
            );
        }
    }

    @SuppressWarnings("resource")
    @ParameterizedTest(name = "[{index}] {0}")
    @ArgumentsSource(QueryExceptionProvider.class)
    void testQueryException(Class<Throwable> throwableType, Language language, String pattern) {
        Assertions.assertThrows(throwableType, () -> new Query(language, pattern));
    }

    @Test
    void testQueryCount() {
        Assertions.assertEquals(1, query.countCaptures());
        Assertions.assertEquals(1, query.countPatterns());
        Assertions.assertEquals(0, query.countStrings());
    }

    @Test
    void testQueryCaptureName() {
        QueryCapture capture = new QueryCapture(empty, 0);
        Assertions.assertEquals("capture", query.getCaptureName(capture));
    }

    @Test
    void testQueryHasCaptures() {
        Assertions.assertTrue(query.hasCaptures());
        @Cleanup Query query = new Query(Language.JAVA, "(_)");
        Assertions.assertFalse(query.hasCaptures());
    }
}
