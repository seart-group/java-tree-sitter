package ai.serenade.treesitter;

import java.io.UnsupportedEncodingException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class NodeTest extends TestBase {

  private static final String source = "def foo(bar, baz):\n  print(bar)\n  print(baz)";

  @Test
  void testGetChild() throws UnsupportedEncodingException {
    try (Parser parser = new Parser()) {
      parser.setLanguage(Language.PYTHON);
      try (Tree tree = parser.parseString(source)) {
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

  @Test
  void testGetChildByFieldName() throws UnsupportedEncodingException {
    try (Parser parser = new Parser()) {
      parser.setLanguage(Language.PYTHON);
      try (Tree tree = parser.parseString(source)) {
        Node root = tree.getRootNode();
        Node function = root.getChild(0);
        Node identifier = function.getChild(1);
        Assertions.assertEquals(identifier, function.getChildByFieldName("name"));
      }
    }
  }

  @Test
  void testGetDescendantForByteRange() throws UnsupportedEncodingException {
    try (Parser parser = new Parser()) {
      parser.setLanguage(Language.PYTHON);
      try (Tree tree = parser.parseString(source)) {
        Node root = tree.getRootNode();
        Node function = root.getChild(0);
        Node def = function.getChild(0);
        Node identifier = function.getChild(1);
        Node parameters = function.getChild(2);
        Node colon = function.getChild(3);
        Node body = function.getChild(4);
        Assertions.assertEquals(def, root.getDescendantForByteRange(def.getStartByte(), def.getEndByte()));
        Assertions.assertEquals(identifier, root.getDescendantForByteRange(identifier.getStartByte(), identifier.getEndByte()));
        Assertions.assertEquals(parameters, root.getDescendantForByteRange(parameters.getStartByte(), parameters.getEndByte()));
        Assertions.assertEquals(colon, root.getDescendantForByteRange(colon.getStartByte(), colon.getEndByte()));
        Assertions.assertEquals(body, root.getDescendantForByteRange(body.getStartByte(), body.getEndByte()));
      }
    }
  }

  @Test
  void testGetFieldNameForChild() throws UnsupportedEncodingException {
    try (Parser parser = new Parser()) {
      parser.setLanguage(Language.PYTHON);
      try (Tree tree = parser.parseString(source)) {
        Node root = tree.getRootNode();
        Node function = root.getChild(0);
        Assertions.assertNull(function.getFieldNameForChild(0));                  // `def`
        Assertions.assertEquals("name", function.getFieldNameForChild(1));        // "name"
        Assertions.assertEquals("parameters", function.getFieldNameForChild(2));  // "parameters"
        Assertions.assertNull(function.getFieldNameForChild(3));                  // `:`
        Assertions.assertEquals("body", function.getFieldNameForChild(4));        // "body"
      }
    }
  }

  @Test
  void testGetFirstChildForByte() throws UnsupportedEncodingException {
    try (Parser parser = new Parser()) {
      parser.setLanguage(Language.PYTHON);
      try (Tree tree = parser.parseString(source)) {
        Node root = tree.getRootNode();
        Node function = root.getChild(0);
        Node def = function.getChild(0);
        Node identifier = function.getChild(1);
        Node parameters = function.getChild(2);
        Node colon = function.getChild(3);
        Node body = function.getChild(4);
        Assertions.assertEquals(def, function.getFirstChildForByte(def.getStartByte()));
        Assertions.assertEquals(identifier, function.getFirstChildForByte(identifier.getStartByte()));
        Assertions.assertEquals(parameters, function.getFirstChildForByte(parameters.getStartByte()));
        Assertions.assertEquals(colon, function.getFirstChildForByte(colon.getStartByte()));
        Assertions.assertEquals(body, function.getFirstChildForByte(body.getStartByte()));
      }
    }
  }

  @Test
  void testGetFirstNamedChildForByte() throws UnsupportedEncodingException {
    try (Parser parser = new Parser()) {
      parser.setLanguage(Language.PYTHON);
      try (Tree tree = parser.parseString(source)) {
        Node root = tree.getRootNode();
        Node function = root.getChild(0);
        Node def = function.getChild(0);
        Node identifier = function.getChild(1);
        Node parameters = function.getChild(2);
        Node colon = function.getChild(3);
        Node body = function.getChild(4);
        Assertions.assertEquals(identifier, function.getFirstNamedChildForByte(def.getStartByte()));
        Assertions.assertEquals(identifier, function.getFirstNamedChildForByte(identifier.getStartByte()));
        Assertions.assertEquals(parameters, function.getFirstNamedChildForByte(parameters.getStartByte()));
        Assertions.assertEquals(body, function.getFirstNamedChildForByte(colon.getStartByte()));
        Assertions.assertEquals(body, function.getFirstNamedChildForByte(body.getStartByte()));
      }
    }
  }

  @Test
  void testGetParent() throws UnsupportedEncodingException {
    try (Parser parser = new Parser()) {
      parser.setLanguage(Language.PYTHON);
      try (Tree tree = parser.parseString(source)) {
        Node root = tree.getRootNode();
        Assertions.assertNull(root.getParent());
        Assertions.assertEquals(root, root.getChild(0).getParent());
      }
    }
  }

  @Test
  void testGetNextNamedSibling() throws UnsupportedEncodingException {
    try (Parser parser = new Parser()) {
      parser.setLanguage(Language.PYTHON);
      try (Tree tree = parser.parseString(source)) {
        Node root = tree.getRootNode();
        Node function = root.getChild(0);
        Node def = function.getChild(0);
        Node identifier = function.getChild(1);
        Assertions.assertNull(root.getNextNamedSibling());
        Assertions.assertEquals(identifier, def.getNextNamedSibling());
      }
    }
  }

  @Test
  void testGetNextSibling() throws UnsupportedEncodingException {
    try (Parser parser = new Parser()) {
      parser.setLanguage(Language.PYTHON);
      try (Tree tree = parser.parseString(source)) {
        Node root = tree.getRootNode();
        Node function = root.getChild(0);
        Node def = function.getChild(0);
        Node identifier = function.getChild(1);
        Assertions.assertNull(root.getNextSibling());
        Assertions.assertEquals(identifier, def.getNextSibling());
      }
    }
  }

  @Test
  void testGetPrevNamedSibling() throws UnsupportedEncodingException {
    try (Parser parser = new Parser()) {
      parser.setLanguage(Language.PYTHON);
      try (Tree tree = parser.parseString(source)) {
        Node root = tree.getRootNode();
        Node function = root.getChild(0);
        Node identifier = function.getChild(1);
        Node parameters = function.getChild(2);
        Assertions.assertNull(root.getPrevNamedSibling());
        Assertions.assertEquals(identifier, parameters.getPrevNamedSibling());
      }
    }
  }

  @Test
  void testGetPrevSibling() throws UnsupportedEncodingException {
    try (Parser parser = new Parser()) {
      parser.setLanguage(Language.PYTHON);
      try (Tree tree = parser.parseString(source)) {
        Node root = tree.getRootNode();
        Node function = root.getChild(0);
        Node def = function.getChild(0);
        Node identifier = function.getChild(1);
        Assertions.assertNull(root.getPrevSibling());
        Assertions.assertEquals(def, identifier.getPrevSibling());
      }
    }
  }

  @Test
  void testHasError() throws UnsupportedEncodingException {
    try (Parser parser = new Parser()) {
      parser.setLanguage(Language.PYTHON);
      try (Tree tree = parser.parseString("def foo(bar, baz):\n  print(bar.)")) {
        Node root = tree.getRootNode();
        Node function = root.getChild(0);
        Node def = function.getChild(0);
        Assertions.assertTrue(root.hasError());
        Assertions.assertTrue(function.hasError());
        Assertions.assertFalse(def.hasError());
      }
    }
  }

  @Test
  void testIsExtra() throws UnsupportedEncodingException {
    try (Parser parser = new Parser()) {
      parser.setLanguage(Language.PYTHON);
      try (Tree tree = parser.parseString("# this is just a comment")) {
        Node root = tree.getRootNode();
        Node comment = root.getChild(0);
        Assertions.assertFalse(root.isExtra());
        Assertions.assertTrue(comment.isExtra());
      }
    }
  }

  @Test
  void testIsMissing() throws UnsupportedEncodingException {
    try (Parser parser = new Parser()) {
      parser.setLanguage(Language.JAVA);
      try (Tree tree = parser.parseString("class C { public static final int i = 6 }")) {
        Node root = tree.getRootNode();
        Assertions.assertFalse(root.isMissing());
        Assertions.assertFalse(root.getChild(0).isMissing());
        Assertions.assertFalse(root.getChild(0).getChild(2).isMissing());
        Assertions.assertFalse(root.getChild(0).getChild(2).getChild(1).isMissing());
        Assertions.assertFalse(root.getChild(0).getChild(2).getChild(1).getChild(2).isMissing());
        Assertions.assertTrue(root.getChild(0).getChild(2).getChild(1).getChild(3).isMissing());
      }
    }
  }

  @Test
  void testIsNamed() throws UnsupportedEncodingException {
    try (Parser parser = new Parser()) {
      parser.setLanguage(Language.PYTHON);
      try (Tree tree = parser.parseString(source)) {
        Node root = tree.getRootNode();
        Node function = root.getChild(0);
        Node def = function.getChild(0);
        Node identifier = function.getChild(1);
        Assertions.assertFalse(def.isNamed());
        Assertions.assertTrue(identifier.isNamed());
      }
    }
  }
}
