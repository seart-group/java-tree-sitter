package ch.usi.si.seart.treesitter;

import lombok.Cleanup;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

class NodeTest extends TestBase {

    private static final String source = "def foo(bar, baz):\n  print(bar)\n  print(baz)";
    private static Parser parser;
    private static Tree tree;
    private static Node root;

    @BeforeAll
    static void beforeAll() {
        parser = new Parser(Language.PYTHON);
        tree = parser.parse(source);
        root = tree.getRootNode();
    }

    @AfterAll
    static void afterAll() {
        tree.close();
        parser.close();
    }

    public static Stream<Arguments> provideOutOfBoundsIndexes() {
        return Stream.of(
                Arguments.of(-1),
                Arguments.of(Integer.MAX_VALUE)
        );
    }

    @Test
    void testGetChildCount() {
        Assertions.assertEquals(1, root.getChildCount());
        Assertions.assertEquals(5, root.getChild(0).getChildCount());
        Assertions.assertEquals(0, new Node().getChildCount());
    }

    @ParameterizedTest
    @MethodSource("provideOutOfBoundsIndexes")
    void testGetChildThrows(int index) {
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> root.getChild(index));
    }

    @Test
    void testGetChildren() {
        Node function = root.getChild(0);
        int count = function.getChildCount();
        List<Node> children = function.getChildren();
        Assertions.assertEquals(count, children.size());
        for (int i = 0; i < count; i++) {
            Assertions.assertEquals(function.getChild(i), children.get(i));
        }
        Assertions.assertEquals(new Node().getChildren(), List.of());
    }

    @Test
    void testGetChildByFieldName() {
        Node function = root.getChild(0);
        Node identifier = function.getChild(1);
        Assertions.assertEquals(identifier, function.getChildByFieldName("name"));
    }

    @ParameterizedTest
    @NullSource
    void testGetChildByFieldNameThrows(String name) {
        Assertions.assertThrows(NullPointerException.class, () -> root.getChildByFieldName(name));
    }

    @Test
    void testGetDescendantForByteRange() {
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
        Assertions.assertThrows(IllegalArgumentException.class, () -> root.getDescendantForByteRange(2, 0));
    }

    @Test
    void testGetEndByte() {
        Assertions.assertEquals(44, root.getEndByte());
        Assertions.assertEquals(0, new Node().getEndByte());
    }

    @Test
    void testGetEndPoint() {
        Point endPoint = root.getEndPoint();
        Assertions.assertEquals(2, endPoint.getRow());
        Assertions.assertEquals(12, endPoint.getColumn());
        Assertions.assertTrue(new Node().getEndPoint().isOrigin());
    }

    @Test
    void testGetFieldNameForChild() {
        Node function = root.getChild(0);
        Assertions.assertNull(function.getFieldNameForChild(0));                  // `def`
        Assertions.assertEquals("name", function.getFieldNameForChild(1));        // "name"
        Assertions.assertEquals("parameters", function.getFieldNameForChild(2));  // "parameters"
        Assertions.assertNull(function.getFieldNameForChild(3));                  // `:`
        Assertions.assertEquals("body", function.getFieldNameForChild(4));        // "body"
    }

    @ParameterizedTest
    @MethodSource("provideOutOfBoundsIndexes")
    void testGetFieldNameForChildThrows(int index) {
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> root.getFieldNameForChild(index));
    }

    @Test
    void testGetFirstChildForByte() {
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

    @ParameterizedTest
    @MethodSource("provideOutOfBoundsIndexes")
    void testGetFirstChildForByteThrows(int index) {
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> root.getFirstChildForByte(index));
    }

    @Test
    void testGetFirstNamedChildForByte() {
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

    @ParameterizedTest
    @MethodSource("provideOutOfBoundsIndexes")
    void testGetFirstNamedChildForByteThrows(int index) {
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> root.getFirstNamedChildForByte(index));
    }

    @Test
    void testGetParent() {
        Assertions.assertNull(root.getParent());
        Assertions.assertEquals(root, root.getChild(0).getParent());
    }

    @Test
    void testGetNextNamedSibling() {
        Node function = root.getChild(0);
        Node def = function.getChild(0);
        Node identifier = function.getChild(1);
        Assertions.assertNull(root.getNextNamedSibling());
        Assertions.assertEquals(identifier, def.getNextNamedSibling());
    }

    @Test
    void testGetNextSibling() {
        Node function = root.getChild(0);
        Node def = function.getChild(0);
        Node identifier = function.getChild(1);
        Assertions.assertNull(root.getNextSibling());
        Assertions.assertEquals(identifier, def.getNextSibling());
    }

    @Test
    void testGetPrevNamedSibling() {
        Node function = root.getChild(0);
        Node identifier = function.getChild(1);
        Node parameters = function.getChild(2);
        Assertions.assertNull(root.getPrevNamedSibling());
        Assertions.assertEquals(identifier, parameters.getPrevNamedSibling());
    }

    @Test
    void testGetPrevSibling() {
        Node function = root.getChild(0);
        Node def = function.getChild(0);
        Node identifier = function.getChild(1);
        Assertions.assertNull(root.getPrevSibling());
        Assertions.assertEquals(def, identifier.getPrevSibling());
    }

    @Test
    void testGetStartByte() {
        Assertions.assertEquals(0, root.getStartByte());
        Assertions.assertEquals(0, new Node().getStartByte());
    }

    @Test
    void testGetStartPoint() {
        Point startPoint = root.getStartPoint();
        Assertions.assertEquals(0, startPoint.getRow());
        Assertions.assertEquals(0, startPoint.getColumn());
        Assertions.assertTrue(new Node().getStartPoint().isOrigin());
    }

    @Test
    void testGetType() {
        Assertions.assertEquals("module", root.getType());
        Assertions.assertEquals("function_definition", root.getChild(0).getType());
        Assertions.assertNull(new Node().getType());
    }

    @Test
    void testHasError() {
        @Cleanup Tree tree = parser.parse("def foo(bar, baz):\n  print(bar.)");
        Node root = tree.getRootNode();
        Node function = root.getChild(0);
        Node def = function.getChild(0);
        Assertions.assertTrue(root.hasError());
        Assertions.assertTrue(function.hasError());
        Assertions.assertFalse(def.hasError());
    }

    @Test
    void testIsExtra() {
        @Cleanup Tree tree = parser.parse("# this is just a comment");
        Node root = tree.getRootNode();
        Node comment = root.getChild(0);
        Assertions.assertFalse(root.isExtra());
        Assertions.assertTrue(comment.isExtra());
    }

    @Test
    void testIsMissing() {
        @Cleanup Parser parser = new Parser(Language.JAVA);
        @Cleanup Tree tree = parser.parse("class C { public static final int i = 6 }");
        Node root = tree.getRootNode();
        Assertions.assertFalse(root.isMissing());
        Assertions.assertFalse(root.getChild(0).isMissing());
        Assertions.assertFalse(root.getChild(0).getChild(2).isMissing());
        Assertions.assertFalse(root.getChild(0).getChild(2).getChild(1).isMissing());
        Assertions.assertFalse(root.getChild(0).getChild(2).getChild(1).getChild(2).isMissing());
        Assertions.assertTrue(root.getChild(0).getChild(2).getChild(1).getChild(3).isMissing());
    }

    @Test
    void testIsNamed() {
        Node function = root.getChild(0);
        Node def = function.getChild(0);
        Node identifier = function.getChild(1);
        Assertions.assertFalse(def.isNamed());
        Assertions.assertTrue(identifier.isNamed());
    }

    @Test
    void testIsNull() {
        Assertions.assertFalse(root.isNull());
        Assertions.assertTrue(new Node().isNull());
    }

    @Test
    void testIterator() {
        Node function = root.getChild(0);
        Iterator<Node> iterator = function.iterator();
        Assertions.assertTrue(iterator.hasNext());
        Assertions.assertEquals(function, iterator.next());
        for (int i = 0; i < function.getChildCount(); i++) {
            Assertions.assertTrue(iterator.hasNext());
            Assertions.assertEquals(function.getChild(i), iterator.next());
        }
        Assertions.assertTrue(iterator.hasNext());
        Iterator<Node> empty = new Node().iterator();
        empty.next();
        Assertions.assertFalse(empty.hasNext());
        Assertions.assertThrows(NoSuchElementException.class, empty::next);
    }
}
