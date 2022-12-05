package ai.serenade.treesitter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ParserTest extends TestBase {

  @TempDir
  static Path tmp;

  static Path tmpFile;

  static final String source = "print(\"hi\")\n";
  static final String nodeString =
          "(module (expression_statement (call function: (identifier) arguments: (argument_list (string)))))";

  @BeforeAll
  static void beforeAll() throws IOException {
    tmpFile = Files.createFile(tmp.resolve("print.py"));
    Files.writeString(tmpFile, source);
  }

  @Test
  void testParseString() throws UnsupportedEncodingException {
    try (Parser parser = new Parser()) {
      parser.setLanguage(Languages.python());
      try (Tree tree = parser.parseString(source)) {
        Assertions.assertEquals(nodeString, tree.getRootNode().getNodeString());
      }
    }
  }

  @Test
  void testParseFile() throws IOException {
    try (Parser parser = new Parser()) {
      parser.setLanguage(Languages.python());
      try (Tree tree = parser.parseFile(tmpFile)) {
        Assertions.assertEquals(nodeString, tree.getRootNode().getNodeString());
      }
    }
  }
}
