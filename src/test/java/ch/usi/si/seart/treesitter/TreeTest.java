package ch.usi.si.seart.treesitter;

import lombok.Cleanup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TreeTest extends TestBase {

    @Test
    void testTreeEdit() {
        @Cleanup Parser parser = new Parser(Language.JAVA);

        Tree tree = parser.parse("class Main {\n    // This is a line comment\n}");

        Node root = tree.getRootNode();
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

        String oldSExp = tree.getRootNode().getNodeString();

        tree.edit(inputEdit);

        tree = parser.parse("class Main {\n}", tree);

        String newSExp = tree.getRootNode().getNodeString();
        Assertions.assertNotEquals(oldSExp, newSExp);
    }

    @Test
    void testTreeThrows() {
        @Cleanup Tree tree = new Tree(0L, Language.JAVA);
        Assertions.assertThrows(NullPointerException.class, () -> tree.edit(null));
    }
}
