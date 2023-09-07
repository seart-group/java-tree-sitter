package ch.usi.si.seart.treesitter;

import lombok.Cleanup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TreeTest extends TestBase {

    @Test
    void testTreeEdit() {
        @Cleanup Parser parser = new Parser(Language.JAVA);
        Tree tree = parser.parse("class Main {\n    // This is a line comment\n}\n");
        Node root = tree.getRootNode();
        Assertions.assertEquals("program", root.getType());
        Range range = root.getRange();
        Point start = range.getStartPoint();
        Point end = range.getEndPoint();
        Assertions.assertEquals(new Point(0, 0), start);
        Assertions.assertEquals(new Point(3, 0), end);
        Node body = root.getChild(0).getChildByFieldName("body");
        int newEndByte = 13;
        Point newEndPoint = new Point(1, 1);
        InputEdit inputEdit = new InputEdit(
                body.getStartByte(),
                body.getEndByte(),
                newEndByte,
                body.getStartPoint(),
                body.getEndPoint(),
                newEndPoint
        );
        tree.edit(inputEdit);
        tree = parser.parse("class Main {\n}\n", tree);
        root = tree.getRootNode();
        Assertions.assertEquals("program", root.getType());
        range = root.getRange();
        start = range.getStartPoint();
        end = range.getEndPoint();
        Assertions.assertEquals(new Point(0, 0), start);
        Assertions.assertEquals(new Point(2, 0), end);
    }

    @Test
    void testTreeThrows() {
        @Cleanup Tree tree = new Tree(0L, Language.JAVA);
        Assertions.assertThrows(NullPointerException.class, () -> tree.edit(null));
    }
}
