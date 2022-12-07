package ai.serenade.treesitter;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Parsers are stateful objects that can be assigned a language and used to produce a
 * {@link ai.serenade.treesitter.Tree Tree} based on some source code.
 */
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
   * @param language The language used for parsing.
   */
  public void setLanguage(Language language) {
    TreeSitter.parserSetLanguage(pointer, language.getId());
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
   * Use the parser to parse some source code found in a file at the specified path.
   *
   * @param path The path of the file to be parsed.
   * @return A tree-sitter Tree matching the provided source.
   */
  public Tree parseFile(Path path) throws IOException {
    String source = Files.readString(path);
    return parseString(source);
  }

  /**
   * Use the parser to parse some source code found in a file represented by the reference.
   *
   * @param file The reference to the file which will be parsed.
   * @return A tree-sitter Tree matching the provided source.
   */
  public Tree parseFile(File file) throws IOException {
    Path path = file.toPath();
    return parseFile(path);
  }

  /**
   * Delete the parser, freeing all the memory that it used.
   */
  @Override
  public void close() {
    TreeSitter.parserDelete(pointer);
  }
}
