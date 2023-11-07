package ch.usi.si.seart.treesitter;

import ch.usi.si.seart.treesitter.exception.ByteOffsetOutOfBoundsException;
import ch.usi.si.seart.treesitter.exception.PointOutOfBoundsException;
import lombok.Cleanup;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

class TreeCursorTest extends TestBase {

    private static Parser parser;
    private static Tree tree;

    private TreeCursor cursor;

    @BeforeAll
    static void beforeAll() {
        parser = Parser.getFor(Language.PYTHON);
        tree = parser.parse("def foo(bar, baz):\n  print(bar)\n  print(baz)");
    }

    @BeforeEach
    void setUp() {
        cursor = tree.getRootNode().walk();
    }

    @AfterEach
    void tearDown() {
        cursor.close();
    }

    @AfterAll
    static void afterAll() {
        tree.close();
        parser.close();
    }

    @Test
    void testWalk() {
        Assertions.assertEquals("module", cursor.getCurrentTreeCursorNode().getType());
        Assertions.assertEquals("module", cursor.getCurrentNode().getType());
        Assertions.assertTrue(cursor.gotoFirstChild());
        Assertions.assertEquals("function_definition", cursor.getCurrentTreeCursorNode().getType());
        Assertions.assertEquals("function_definition", cursor.getCurrentNode().getType());
        Assertions.assertTrue(cursor.gotoFirstChild());

        Assertions.assertEquals("def", cursor.getCurrentNode().getType());
        Assertions.assertTrue(cursor.gotoNextSibling());
        Assertions.assertEquals("identifier", cursor.getCurrentNode().getType());
        Assertions.assertEquals("name", cursor.getCurrentFieldName());
        Assertions.assertTrue(cursor.gotoNextSibling());
        Assertions.assertEquals("parameters", cursor.getCurrentNode().getType());
        Assertions.assertEquals("parameters", cursor.getCurrentFieldName());
        Assertions.assertTrue(cursor.gotoNextSibling());
        Assertions.assertEquals(":", cursor.getCurrentNode().getType());
        Assertions.assertTrue(cursor.gotoNextSibling());
        Assertions.assertEquals("block", cursor.getCurrentNode().getType());
        Assertions.assertEquals("body", cursor.getCurrentFieldName());
        Assertions.assertFalse(cursor.gotoNextSibling());

        Assertions.assertTrue(cursor.gotoParent());
        Assertions.assertEquals("function_definition", cursor.getCurrentNode().getType());
        Assertions.assertTrue(cursor.gotoFirstChild());
    }

    @Test
    void testGotoFirstChildByteOffset() {
        cursor.gotoFirstChild(); // function_definition
        cursor.gotoFirstChild(4);
        Assertions.assertEquals("identifier", cursor.getCurrentNode().getType());
        cursor.gotoParent();
        cursor.gotoFirstChild(21);
        Assertions.assertEquals("block", cursor.getCurrentNode().getType());
        cursor.gotoFirstChild(34);
        Assertions.assertEquals("expression_statement", cursor.getCurrentNode().getType());
    }

    @Test
    void testGotoFirstChildByteOffsetThrows() {
        cursor.gotoFirstChild(); // function_definition
        cursor.gotoFirstChild(); // identifier
        cursor.gotoNextSibling(); // parameters
        Assertions.assertThrows(IllegalArgumentException.class, () -> cursor.gotoFirstChild(-1));
        Assertions.assertThrows(ByteOffsetOutOfBoundsException.class, () -> cursor.gotoFirstChild(0));
        Assertions.assertThrows(ByteOffsetOutOfBoundsException.class, () -> cursor.gotoFirstChild(20));
    }

    @Test
    void testGotoFirstChildPositionOffset() {
        cursor.gotoFirstChild(); // function_definition
        cursor.gotoFirstChild(new Point(0, 4));
        Assertions.assertEquals("identifier", cursor.getCurrentNode().getType());
        cursor.gotoParent();
        cursor.gotoFirstChild(new Point(1, 2));
        Assertions.assertEquals("block", cursor.getCurrentNode().getType());
        cursor.gotoFirstChild(new Point(2, 2));
        Assertions.assertEquals("expression_statement", cursor.getCurrentNode().getType());
    }

    @Test
    void testGotoFirstChildPositionOffsetThrows() {
        cursor.gotoFirstChild(); // function_definition
        cursor.gotoFirstChild(); // identifier
        cursor.gotoNextSibling(); // parameters
        Point negative = new Point(0, -1);
        Point illegal = new Point(1, 2);
        Assertions.assertThrows(NullPointerException.class, () -> cursor.gotoFirstChild(null));
        Assertions.assertThrows(PointOutOfBoundsException.class, () -> cursor.gotoFirstChild(negative));
        Assertions.assertThrows(PointOutOfBoundsException.class, () -> cursor.gotoFirstChild(_0_0_));
        Assertions.assertThrows(PointOutOfBoundsException.class, () -> cursor.gotoFirstChild(illegal));

    }

    @Test
    void testGotoNode() {
        Node root = tree.getRootNode();
        Assertions.assertFalse(cursor.gotoNode(root));
        Node identifier = root.getChild(0).getChildByFieldName("name");
        Assertions.assertTrue(cursor.gotoNode(identifier));
        Assertions.assertEquals(identifier, cursor.getCurrentNode());
        Assertions.assertFalse(cursor.gotoNode(identifier));
        Assertions.assertTrue(cursor.gotoNode(root));
        Assertions.assertEquals(root, cursor.getCurrentNode());
        Node clone = tree.clone().getRootNode();
        Assertions.assertThrows(NullPointerException.class, () -> cursor.gotoNode(null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> cursor.gotoNode(empty));
        Assertions.assertThrows(IllegalArgumentException.class, () -> cursor.gotoNode(clone));
    }

    @Test
    void testPreorderTraversal() {
        AtomicInteger count = new AtomicInteger();
        cursor.preorderTraversal(node -> {
            if (node.isNamed())
                count.incrementAndGet();
        });
        Assertions.assertEquals(17, count.get());
    }

    @Test
    void testClone() {
        @Cleanup TreeCursor copy = cursor.clone();
        Assertions.assertNotEquals(cursor, copy);
    }

    @Test
    void testWalkThrows() {
        Node empty = new Node();
        Assertions.assertThrows(IllegalStateException.class, empty::walk);
    }
}
