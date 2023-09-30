package ch.usi.si.seart.treesitter;

import lombok.Cleanup;
import org.junit.jupiter.api.Assertions;

class PatternTest extends DisableTest {

    @Override
    protected void assertions(Node node, Query query) {
        Pattern pattern = query.getPatterns().stream()
                .findFirst()
                .orElseGet(Assertions::fail);
        Assertions.assertTrue(pattern.isEnabled());
        pattern.disable();
        Assertions.assertFalse(pattern.isEnabled());
        @Cleanup QueryCursor cursor = node.walk(query);
        for (QueryMatch ignored: cursor) Assertions.fail();
        Assertions.assertFalse(pattern.isEnabled());
        pattern.disable();
        Assertions.assertFalse(pattern.isEnabled());
    }
}
