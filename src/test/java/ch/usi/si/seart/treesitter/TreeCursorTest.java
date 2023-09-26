package ch.usi.si.seart.treesitter;

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
        parser = Parser.builder().language(Language.PYTHON).build();;
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
