package ai.serenade.treesitter;

import java.io.UnsupportedEncodingException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ParserTest extends TestBase {

  @Test
  void testParse() throws UnsupportedEncodingException {
    try (Parser parser = new Parser()) {
      parser.setLanguage(Languages.python());
      try (Tree tree = parser.parseString("print(\"hi\")")) {
        Assertions.assertEquals(
          "(module (expression_statement (call function: (identifier) arguments: (argument_list (string)))))",
          tree.getRootNode().getNodeString()
        );
      }
    }
  }
}
