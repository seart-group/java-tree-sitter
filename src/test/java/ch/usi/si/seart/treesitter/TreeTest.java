package ch.usi.si.seart.treesitter;

import lombok.Cleanup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TreeTest extends TestBase {

    @Test
    void testTreeGetSource() {
        String source = "class Main {\n    // This is a line comment\n}\n";
        @Cleanup Parser parser = new Parser(Language.JAVA);
        Tree tree = parser.parse(source);
        Assertions.assertEquals(source, tree.getSource());
    }

    @Test
    void testTreeGetSourceStartEnd() {
        String source = "class Main {\n    // This is a line comment\n}\n";
        @Cleanup Parser parser = new Parser(Language.JAVA);
        Tree tree = parser.parse(source);
        Node root = tree.getRootNode();
        Node name = root.getChild(0).getChildByFieldName("name");
        Node body = root.getChild(0).getChildByFieldName("body");
        Node leftCurly = body.getChild(0);
        Node comment = body.getChild(1);
        Node rightCurly = body.getChild(2);
        Assertions.assertEquals(source.substring(0, 45), tree.getSource(root.getStartByte(), root.getEndByte()));
        Assertions.assertEquals(source.substring(6, 10), tree.getSource(name.getStartByte(), name.getEndByte()));
        Assertions.assertEquals(source.substring(11, 12), tree.getSource(leftCurly.getStartByte(), leftCurly.getEndByte()));
        Assertions.assertEquals(source.substring(17, 42), tree.getSource(comment.getStartByte(), comment.getEndByte()));
        Assertions.assertEquals(source.substring(43, 44), tree.getSource(rightCurly.getStartByte(), rightCurly.getEndByte()));
    }

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
        @Cleanup Tree tree = new Tree(0L, Language.JAVA, "");
        Assertions.assertThrows(NullPointerException.class, () -> tree.edit(null));
    }
}
