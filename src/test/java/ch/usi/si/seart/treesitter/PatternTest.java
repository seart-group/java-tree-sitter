package ch.usi.si.seart.treesitter;

import lombok.Cleanup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

class PatternTest extends BaseTest {

    @ParameterizedTest(name = "[{index}] {0}")
    @ValueSource(strings = {
        "(line_comment)(block_comment)",
        "(line_comment) (block_comment)",
        "(line_comment ) (block_comment )",
        "(line_comment ) ( block_comment)",
        "( line_comment) (block_comment )",
        "( line_comment ) ( block_comment )",
        "(line_comment)     (block_comment)",
        "(line_comment   )(   block_comment)",
        "   (line_comment) (block_comment)   ",
    })
    void test(String string) {
        List<String> parts = List.of("(line_comment)", "(block_comment)");
        @Cleanup Query query = Query.builder()
                .language(Language.JAVA)
                .pattern(string)
                .build();
        Assertions.assertEquals(String.join(" ", parts), query.getPattern());
        List<Pattern> patterns = query.getPatterns();
        Assertions.assertFalse(patterns.isEmpty());
        Assertions.assertEquals(2, patterns.size());
        for (int i = 0; i < patterns.size(); i++) {
            Pattern pattern = patterns.get(i);
            Assertions.assertTrue(pattern.getStartOffset() >= 0);
            Assertions.assertTrue(pattern.getStartOffset() < pattern.getEndOffset());
            String actual = pattern.getValue();
            String expected = parts.get(i);
            Assertions.assertEquals(expected, actual);
        }
    }

    @Test
    void testDisable() {
        @Cleanup Parser parser = Parser.getFor(Language.PYTHON);
        @Cleanup Tree tree = parser.parse("pass");
        @Cleanup Query query = Query.builder()
                .language(Language.PYTHON)
                .pattern("(module) @capture")
                .build();
        Node root = tree.getRootNode();
        Pattern pattern = query.getPatterns().stream()
                .findFirst()
                .orElseGet(Assertions::fail);
        Assertions.assertTrue(pattern.isEnabled());
        pattern.disable();
        Assertions.assertFalse(pattern.isEnabled());
        @Cleanup QueryCursor cursor = root.walk(query);
        for (QueryMatch ignored: cursor) Assertions.fail();
        Assertions.assertFalse(pattern.isEnabled());
        pattern.disable();
        Assertions.assertFalse(pattern.isEnabled());
    }
}
