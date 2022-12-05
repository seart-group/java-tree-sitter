package ai.serenade.treesitter;

import java.io.UnsupportedEncodingException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class NodeTest extends TestBase {

  @Test
  void testGetChildren() throws UnsupportedEncodingException {
    try (Parser parser = new Parser()) {
      parser.setLanguage(Languages.python());
      try (Tree tree = parser.parseString("def foo(bar, baz):\n  print(bar)\n  print(baz)")) {
        Node root = tree.getRootNode();
        Assertions.assertEquals(1, root.getChildCount());
        Assertions.assertEquals("module", root.getType());
        Assertions.assertEquals(0, root.getStartByte());
        Assertions.assertEquals(44, root.getEndByte());

        Node function = root.getChild(0);
        Assertions.assertEquals("function_definition", function.getType());
        Assertions.assertEquals(5, function.getChildCount());
      }
    }
  }
}
