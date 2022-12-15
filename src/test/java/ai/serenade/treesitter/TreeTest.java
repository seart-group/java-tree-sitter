package ai.serenade.treesitter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;

class TreeTest extends TestBase {

    @Test
    void testTreeEdit() throws UnsupportedEncodingException {
        try (Parser parser = new Parser()) {
            parser.setLanguage(Language.JAVA);

            Tree tree = parser.parseString("class Main {\n    // This is a line comment\n}");

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

            tree = parser.parseString(tree, "class Main {\n}");

            String newSExp = tree.getRootNode().getNodeString();
            Assertions.assertNotEquals(oldSExp, newSExp);
        }
    }

}
