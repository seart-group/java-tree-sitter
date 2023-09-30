package ch.usi.si.seart.treesitter;

import lombok.Cleanup;
import org.junit.jupiter.api.Assertions;

import java.util.List;

class MultiCaptureTest extends AbstractQueryTest {

    @Override
    protected void assertions(Node node, Query query) {
        List<Capture> captures = query.getCaptures();
        Capture additional = captures.get(0);
        Capture target = captures.get(1);
        @Cleanup QueryCursor cursor = node.walk(query);
        for (QueryMatch match: cursor) {
            Assertions.assertEquals(3, match.getNodes().size());
            Assertions.assertEquals(2, match.getNodes(additional.getName()).size());
            Assertions.assertEquals(1, match.getNodes(target.getName()).size());
            Assertions.assertEquals(2, match.getNodes(additional).size());
            Assertions.assertEquals(1, match.getNodes(target).size());
        }
    }
}
