package ch.usi.si.seart.treesitter;

import lombok.Cleanup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

class QuantifierTest extends BaseTest {

    private static final Language language = Language.JAVA;

    private static final Map<Quantifier, String> symbols = Map.of(
            Quantifier.ONE, "",
            Quantifier.ONE_OR_MORE, "+",
            Quantifier.ZERO_OR_ONE, "?",
            Quantifier.ZERO_OR_MORE, "*"
    );

    private static final class QuantifierArgumentsProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return symbols.entrySet().stream()
                    .map(entry -> {
                        Quantifier quantifier = entry.getKey();
                        String symbol = entry.getValue();
                        String pattern = "((_)" + symbol + " @capture)";
                        Query query = Query.getFor(language, pattern);
                        List<Capture> captures = query.getCaptures();
                        Capture capture = captures.stream().findFirst().orElseThrow();
                        return Arguments.of(quantifier, capture, query);
                    });
        }
    }

    @ParameterizedTest(name = "[{index}] {0}")
    @ArgumentsSource(QuantifierArgumentsProvider.class)
    void testGetQuantifiers(Quantifier expected, Capture capture, Query query) {
        List<Quantifier> quantifiers = capture.getQuantifiers();
        Assertions.assertNotNull(quantifiers);
        Assertions.assertFalse(quantifiers.isEmpty());
        Assertions.assertEquals(1, quantifiers.size());
        Quantifier actual = quantifiers.stream()
                .findFirst()
                .orElseGet(Assertions::fail);
        Assertions.assertEquals(expected, actual);
        query.close();
    }

    @Test
    void testGetQuantifier() {
        @Cleanup Query query = Query.builder()
                .language(language)
                .pattern("((_) @capture)")
                .pattern("(identifier)")
                .build();
        List<Capture> captures = query.getCaptures();
        List<Pattern> patterns = query.getPatterns();
        Capture capture = captures.stream().findFirst().orElseThrow();
        Pattern pattern = patterns.stream().skip(1).findFirst().orElseThrow();
        Quantifier quantifier = capture.getQuantifier(pattern);
        Assertions.assertEquals(Quantifier.ZERO, quantifier);
    }

    @Test
    void testGetQuantifierThrows() {
        @Cleanup Query original = Query.getFor(language, "((_) @capture)");
        @Cleanup Query copy = Query.getFor(language, "((_) @capture)");
        List<Capture> captures = original.getCaptures();
        Capture capture = captures.stream().findFirst().orElseThrow();
        List<Pattern> patterns = copy.getPatterns();
        Pattern pattern = patterns.stream().findFirst().orElseThrow();
        Assertions.assertThrows(NullPointerException.class, () -> capture.getQuantifier(null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> capture.getQuantifier(pattern));
    }
}
