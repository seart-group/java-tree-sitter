package usi.si.seart.treesitter;

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
}
