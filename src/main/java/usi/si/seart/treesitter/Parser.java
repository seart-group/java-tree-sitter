package usi.si.seart.treesitter;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import usi.si.seart.treesitter.error.ABIVersionError;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Parsers are stateful objects that can be assigned a language
 * and used to produce a {@link Tree Tree} based on some source code.
 * Instances of this class <strong>can not</strong> be created
 * without an initially set language.
 */
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Parser extends External {

  Language language;

  /**
   * @param language The language used for parsing.
   * @throws NullPointerException
   * if the specified language is null
   * @throws UnsatisfiedLinkError
   * if the specified language has not
   * been linked to the system library
   */
  public Parser(Language language) {
    super(createIfValid(language));
    this.language = language;
  }

  /*
   * Constructor precondition for creating a parser.
   * In essence, we should never allocate memory to
   * these structures if the language:
   * - Has not been specified (i.e. is null)
   * - Has not been linked to the system library
   */
  private static long createIfValid(Language language) {
    Languages.validate(language);
    long pointer = TreeSitter.parserNew();
    setLanguage(pointer, language);
    return pointer;
  }

  /**
   * Set the language that the parser should use for parsing.
   *
   * @param language The language used for parsing.
   * @throws NullPointerException
   * if the specified language is null
   * @throws UnsatisfiedLinkError
   * if the specified language has not
   * been linked to the system library
   */
  public void setLanguage(Language language) {
    Languages.validate(language);
    setLanguage(pointer, language);
  }

  private static void setLanguage(long pointer, Language language) {
    boolean success = TreeSitter.parserSetLanguage(pointer, language.getId());
    if (!success) throw new ABIVersionError("Language could not be assigned to parser!");
  }

  /**
   * Use the parser to parse some source code and create a syntax tree.
   *
   * @param source The source code string to be parsed.
   * @return A syntax tree matching the provided source.
   */
  public Tree parseString(String source) throws UnsupportedEncodingException {
    byte[] bytes = source.getBytes(StandardCharsets.UTF_16LE);
    long treePointer = TreeSitter.parserParseBytes(pointer, bytes, bytes.length);
    return new Tree(treePointer, language);
  }

  /**
   * Use the parser to incrementally parse a changed source code string,
   * reusing unchanged parts of the tree to speed up the process.
   *
   * @param oldTree The syntax tree before changes were made.
   * @param source The source code string to be parsed.
   * @return A syntax tree matching the provided source.
   */
  public Tree parseString(Tree oldTree, String source) throws UnsupportedEncodingException {
    byte[] bytes = source.getBytes(StandardCharsets.UTF_16LE);
    long treePointer = TreeSitter.parserIncrementalParseBytes(pointer, oldTree.getPointer(), bytes, bytes.length);
    return new Tree(treePointer, language);
  }

  /**
   * Use the parser to parse some source code found in a file at the specified path.
   *
   * @param path The path of the file to be parsed.
   * @return A tree-sitter Tree matching the provided source.
   * @throws IOException If an I/O error occurs reading from
   * the file or a malformed or unmappable byte sequence is read.
   * @throws OutOfMemoryError If the file is extremely large,
   * for example larger than 2GB.
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
   * @see Parser#parseFile(Path)
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

  @Override
  public String toString() {
    return "Parser(id: "+pointer+", language: "+language+")";
  }
}
