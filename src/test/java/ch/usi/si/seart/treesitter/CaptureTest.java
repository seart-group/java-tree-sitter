package ch.usi.si.seart.treesitter;

import org.junit.jupiter.api.Assertions;

import java.util.List;

class CaptureTest extends DisableTest {

    @Override
    protected void assertions(Node node, Query query) {
        List<Capture> captures = query.getCaptures();
        Capture additional = captures.get(0);
        Assertions.assertTrue(additional.isEnabled());
        additional.disable();
        Assertions.assertFalse(additional.isEnabled());
        try (QueryCursor cursor = node.walk(query)) {
            for (QueryMatch match: cursor) {
                Assertions.assertEquals(1, match.getNodes().size());
                Assertions.assertEquals(0, match.getNodes(additional).size());
            }
        }
        Assertions.assertFalse(additional.isEnabled());
        additional.disable();
        Assertions.assertFalse(additional.isEnabled());
        Capture target = captures.get(1);
        Assertions.assertTrue(target.isEnabled());
        target.disable();
        Assertions.assertFalse(target.isEnabled());
        try (QueryCursor cursor = node.walk(query)) {
            for (QueryMatch match: cursor) {
                Assertions.assertEquals(0, match.getNodes().size());
            }
        }
        Assertions.assertFalse(target.isEnabled());
        target.disable();
        Assertions.assertFalse(target.isEnabled());
    }
}
