package ai.serenade.treesitter;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public class Parser implements AutoCloseable {
  private long pointer;

  Parser(long pointer) {
    this.pointer = pointer;
  }

  public Parser() {
    this(TreeSitter.parserNew());
  }

  /**
   * Set the language that the parser should use for parsing.
   *
   * @param language The identifier corresponding to the language.
   */
  public void setLanguage(long language) {
    TreeSitter.parserSetLanguage(pointer, language);
  }

  /**
   * Use the parser to parse some source code stored in one contiguous buffer.
   *
   * @param source The source code string to be parsed.
   * @return A tree-sitter Tree matching the provided source.
   */
  public Tree parseString(String source) throws UnsupportedEncodingException {
    byte[] bytes = source.getBytes(StandardCharsets.UTF_16LE);
    return new Tree(TreeSitter.parserParseBytes(pointer, bytes, bytes.length));
  }

  /**
   * Delete the parser, freeing all the memory that it used.
   */
  @Override
  public void close() {
    TreeSitter.parserDelete(pointer);
  }
}
