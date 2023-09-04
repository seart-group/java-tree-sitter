package ch.usi.si.seart.treesitter;

import lombok.Cleanup;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;

class TreeCursorTest extends TestBase {

  @Test
  @SneakyThrows(UnsupportedEncodingException.class)
  void testWalk() {
    @Cleanup Parser parser = new Parser(Language.PYTHON);
    @Cleanup Tree tree = parser.parseString("def foo(bar, baz):\n  print(bar)\n  print(baz)");
    @Cleanup TreeCursor cursor = tree.getRootNode().walk();
    Assertions.assertEquals("module", cursor.getCurrentTreeCursorNode().getType());
    Assertions.assertEquals("module", cursor.getCurrentNode().getType());
    Assertions.assertTrue(cursor.gotoFirstChild());
    Assertions.assertEquals("function_definition", cursor.getCurrentNode().getType());
    Assertions.assertTrue(cursor.gotoFirstChild());

    Assertions.assertEquals("def", cursor.getCurrentNode().getType());
    Assertions.assertTrue(cursor.gotoNextSibling());
    Assertions.assertEquals("identifier", cursor.getCurrentNode().getType());
    Assertions.assertTrue(cursor.gotoNextSibling());
    Assertions.assertEquals("parameters", cursor.getCurrentNode().getType());
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
  @SneakyThrows(UnsupportedEncodingException.class)
  void testPreorderTraversal() {
    @Cleanup Parser parser = new Parser(Language.PYTHON);
    @Cleanup Tree tree = parser.parseString("def foo(bar, baz):\n  print(bar)\n  print(baz)");
    @Cleanup TreeCursor cursor = tree.getRootNode().walk();
    AtomicInteger count = new AtomicInteger();
    cursor.preorderTraversal(node -> {
      if (node.isNamed())
        count.incrementAndGet();
    });
    Assertions.assertEquals(17, count.get());
  }

  @Test
  @SuppressWarnings("resource")
  void testWalkException() {
    Node nullNode = new Node();
    Assertions.assertThrows(NullPointerException.class, () -> new TreeCursor(null));
    Assertions.assertThrows(IllegalArgumentException.class, nullNode::walk);
  }
}
