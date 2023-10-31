package ch.usi.si.seart.treesitter;

import ch.usi.si.seart.treesitter.exception.query.QueryCaptureException;
import ch.usi.si.seart.treesitter.exception.query.QueryFieldException;
import ch.usi.si.seart.treesitter.exception.query.QueryNodeTypeException;
import ch.usi.si.seart.treesitter.exception.query.QueryStructureException;
import ch.usi.si.seart.treesitter.exception.query.QuerySyntaxException;
import lombok.Cleanup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

class QueryTest extends TestBase {

    private static final Language language = Language.JAVA;

    private static class QueryProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {

            String pattern1 = "(_)";
            String pattern2 = "(_) @capture";
            String pattern3 = "\"return\" @capture";
            String pattern4 = "\"private\" @capture.first";
            String pattern5 = "\"public\" @capture.second";

            return Stream.of(
                    Arguments.of("", Query.builder().language(language).build(), 0, 0, 0),
                    Arguments.of(pattern1, Query.getFor(language, pattern1), 1, 0, 0),
                    Arguments.of(pattern2, Query.getFor(language, pattern2), 1, 1, 0),
                    Arguments.of(pattern3, Query.getFor(language, pattern3), 1, 1, 0),
                    Arguments.of(pattern4, Query.getFor(language, pattern4, pattern5), 2, 2, 0)
            );
        }
    }

    @ParameterizedTest(name = "[{index}] {0}")
    @ArgumentsSource(QueryProvider.class)
    void testQuery(String ignored, Query query, int patterns, int captures, int strings) {
        Assertions.assertNotNull(query);
        Assertions.assertFalse(query.isNull());
        Assertions.assertEquals(patterns, query.getPatterns().size());
        Assertions.assertEquals(captures, query.getCaptures().size());
        Assertions.assertEquals(strings, query.getStrings().size());
        Assertions.assertEquals(captures > 0, query.hasCaptures());
        query.close();
        Assertions.assertTrue(query.isNull());
    }

    private static class QueryExceptionProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
            return Stream.of(
                    Arguments.of(UnsatisfiedLinkError.class, Language._INVALID_, "(_)"),
                    Arguments.of(QueryCaptureException.class, Language.JAVA, "(#eq? @key @value)"),
                    Arguments.of(QueryFieldException.class, Language.JAVA, "(program unknown: (_))"),
                    Arguments.of(QueryNodeTypeException.class, Language.JAVA, "(null)"),
                    Arguments.of(QueryStructureException.class, Language.JAVA, "(program (program))"),
                    Arguments.of(QuerySyntaxException.class, Language.JAVA, "()")
            );
        }
    }

    @ParameterizedTest(name = "[{index}] {0}")
    @ArgumentsSource(QueryExceptionProvider.class)
    void testQueryException(Class<Throwable> throwableType, Language language, String pattern) {
        Assertions.assertThrows(throwableType, () -> Query.getFor(language, pattern));
    }

    private static class QuerySupplierExceptionProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
            Query.Builder builder = Query.builder();
            Map<String, Supplier<Query>> map = Map.of(
                    "Nothing specified", builder::build,
                    "No language specified", () -> builder.pattern("(_)").build(),
                    "Single null pattern string", () -> builder.pattern(null).build(),
                    "String array as null", () -> builder.patterns((String[]) null).build(),
                    "String list as null", () -> builder.patterns((List<String>) null).build(),
                    "String array contains null", () -> builder.patterns("(_)", null).build(),
                    "String list contains null", () -> builder.patterns(Arrays.asList("(_)", null)).build()
            );
            return map.entrySet().stream().map(entry -> Arguments.of(entry.getKey(), entry.getValue()));
        }
    }

    @ParameterizedTest(name = "[{index}] {0}")
    @ArgumentsSource(QuerySupplierExceptionProvider.class)
    void testQueryException(String ignored, Supplier<Query> supplier) {
        Assertions.assertThrows(NullPointerException.class, supplier::get);
    }
}
