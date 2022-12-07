package ai.serenade.treesitter;

import java.io.UnsupportedEncodingException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TreeCursorTest extends TestBase {

  @Test
  void testWalk() throws UnsupportedEncodingException {
    try (Parser parser = new Parser()) {
      parser.setLanguage(Language.PYTHON);
      try (Tree tree = parser.parseString("def foo(bar, baz):\n  print(bar)\n  print(baz)")) {
        try (TreeCursor cursor = tree.getRootNode().walk()) {
          Assertions.assertEquals("module", cursor.getCurrentTreeCursorNode().getType());
          Assertions.assertEquals("module", cursor.getCurrentNode().getType());
          Assertions.assertEquals(true, cursor.gotoFirstChild());
          Assertions.assertEquals("function_definition", cursor.getCurrentNode().getType());
          Assertions.assertEquals(true, cursor.gotoFirstChild());

          Assertions.assertEquals("def", cursor.getCurrentNode().getType());
          Assertions.assertEquals(true, cursor.gotoNextSibling());
          Assertions.assertEquals("identifier", cursor.getCurrentNode().getType());
          Assertions.assertEquals(true, cursor.gotoNextSibling());
          Assertions.assertEquals("parameters", cursor.getCurrentNode().getType());
          Assertions.assertEquals(true, cursor.gotoNextSibling());
          Assertions.assertEquals(":", cursor.getCurrentNode().getType());
          Assertions.assertEquals(true, cursor.gotoNextSibling());
          Assertions.assertEquals("block", cursor.getCurrentNode().getType());
          Assertions.assertEquals("body", cursor.getCurrentFieldName());
          Assertions.assertEquals(false, cursor.gotoNextSibling());

          Assertions.assertEquals(true, cursor.gotoParent());
          Assertions.assertEquals("function_definition", cursor.getCurrentNode().getType());
          Assertions.assertEquals(true, cursor.gotoFirstChild());
        }
      }
    }
  }
}
