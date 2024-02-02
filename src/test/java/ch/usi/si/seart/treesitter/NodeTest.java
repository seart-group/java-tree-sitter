package ch.usi.si.seart.treesitter;

import ch.usi.si.seart.treesitter.exception.ByteOffsetOutOfBoundsException;
import ch.usi.si.seart.treesitter.exception.PointOutOfBoundsException;
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
        parser = Parser.getFor(Language.PYTHON);
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

    public static Stream<Arguments> provideStartAndEndBytes() {
        Node function = root.getChild(0);
        Node identifier = function.getChild(1);
        return Stream.of(
                Arguments.of(IllegalArgumentException.class, -1, identifier.getEndByte()),
                Arguments.of(IllegalArgumentException.class, identifier.getStartByte(), identifier.getEndByte() * -1),
                Arguments.of(IllegalArgumentException.class, identifier.getEndByte(), identifier.getStartByte()),
                Arguments.of(ByteOffsetOutOfBoundsException.class, root.getStartByte(), identifier.getEndByte()),
                Arguments.of(ByteOffsetOutOfBoundsException.class, identifier.getStartByte(), root.getEndByte())
        );
    }

    public static Stream<Arguments> provideStartAndEndPoints() {
        Node function = root.getChild(0);
        Node identifier = function.getChild(1);
        return Stream.of(
                Arguments.of(NullPointerException.class, null, new Point(0, 0)),
                Arguments.of(NullPointerException.class, new Point(0, 0), null),
                Arguments.of(IllegalArgumentException.class, new Point(-1, -1), root.getEndPoint()),
                Arguments.of(IllegalArgumentException.class, identifier.getEndPoint(), identifier.getStartPoint()),
                Arguments.of(PointOutOfBoundsException.class, root.getStartPoint(), new Point(3, 1))
        );
    }

    @Test
    void testGetChildCount() {
        Assertions.assertEquals(1, root.getChildCount());
        Assertions.assertEquals(5, root.getChild(0).getChildCount());
        Assertions.assertEquals(0, empty.getChildCount());
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
        Assertions.assertEquals(empty.getChildren(), List.of());
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
    void testGetContent() {
        Node function = root.getChild(0);
        Node identifier = function.getChild(1);
        Assertions.assertEquals(source, root.getContent());
        Assertions.assertEquals(source, function.getContent());
        Assertions.assertEquals("foo", identifier.getContent());
        Assertions.assertNull(empty.getContent());
    }

    @Test
    void testGetDescendantForByteRange() {
        Node function = root.getChild(0);
        Node def = function.getChild(0);
        Node identifier = function.getChild(1);
        Node parameters = function.getChild(2);
        Node colon = function.getChild(3);
        Node body = function.getChild(4);
        Assertions.assertEquals(def, root.getDescendant(def.getStartByte(), def.getEndByte()));
        Assertions.assertEquals(identifier, root.getDescendant(identifier.getStartByte(), identifier.getEndByte()));
        Assertions.assertEquals(parameters, root.getDescendant(parameters.getStartByte(), parameters.getEndByte()));
        Assertions.assertEquals(colon, root.getDescendant(colon.getStartByte(), colon.getEndByte()));
        Assertions.assertEquals(body, root.getDescendant(body.getStartByte(), body.getEndByte()));
    }

    @ParameterizedTest(name = "[{index}] {0}")
    @MethodSource("provideStartAndEndBytes")
    void testGetDescendantForByteRangeThrows(Class<Throwable> throwableType, int startByte, int endByte) {
        Node function = root.getChild(0);
        Node identifier = function.getChild(1);
        Assertions.assertThrows(throwableType, () -> identifier.getDescendant(startByte, endByte));
    }

    @Test
    void testGetDescendantForPointRange() {
        Node function = root.getChild(0);
        Node def = function.getChild(0);
        Node identifier = function.getChild(1);
        Node parameters = function.getChild(2);
        Node colon = function.getChild(3);
        Node body = function.getChild(4);
        Assertions.assertEquals(def, root.getDescendant(def.getStartPoint(), def.getEndPoint()));
        Assertions.assertEquals(identifier, root.getDescendant(identifier.getStartPoint(), identifier.getEndPoint()));
        Assertions.assertEquals(parameters, root.getDescendant(parameters.getStartPoint(), parameters.getEndPoint()));
        Assertions.assertEquals(colon, root.getDescendant(colon.getStartPoint(), colon.getEndPoint()));
        Assertions.assertEquals(body, root.getDescendant(body.getStartPoint(), body.getEndPoint()));
    }

    @ParameterizedTest(name = "[{index}] {0}")
    @MethodSource("provideStartAndEndPoints")
    void testGetDescendantForPointRangeThrows(Class<Throwable> throwableType, Point startPoint, Point endPoint) {
        Assertions.assertThrows(throwableType, () -> root.getDescendant(startPoint, endPoint));
    }

    @Test
    void testGetDescendantCount() {
        Node function = root.getChild(0);
        Node identifier = function.getChild(1);
        Assertions.assertEquals(26, root.getDescendantCount());
        Assertions.assertEquals(25, function.getDescendantCount());
        Assertions.assertEquals(1, identifier.getDescendantCount());
        Assertions.assertEquals(0, empty.getDescendantCount());
    }

    @Test
    void testGetEndByte() {
        Assertions.assertEquals(44, root.getEndByte());
        Assertions.assertEquals(0, empty.getEndByte());
    }

    @Test
    void testGetEndPoint() {
        Point endPoint = root.getEndPoint();
        Assertions.assertEquals(2, endPoint.getRow());
        Assertions.assertEquals(12, endPoint.getColumn());
        Assertions.assertTrue(empty.getEndPoint().isOrigin());
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
        Assertions.assertThrows(ByteOffsetOutOfBoundsException.class, () -> root.getFirstChildForByte(index));
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
        Assertions.assertThrows(ByteOffsetOutOfBoundsException.class, () -> root.getFirstNamedChildForByte(index));
    }

    @Test
    void testGetLanguage() {
        for (Node child: tree)
            Assertions.assertEquals(tree.getLanguage(), child.getLanguage());
        Assertions.assertNull(empty.getLanguage());
    }

    @ParameterizedTest
    @MethodSource("provideOutOfBoundsIndexes")
    void testGetNamedChildThrows(int index) {
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> root.getNamedChild(index));
    }

    @Test
    void testGetNamedChildCount() {
        Assertions.assertEquals(1, root.getNamedChildCount());
        Assertions.assertEquals(3, root.getChild(0).getNamedChildCount());
        Assertions.assertEquals(0, empty.getNamedChildCount());
    }

    @Test
    void testGetNamedChildren() {
        Node function = root.getChild(0);
        int count = function.getNamedChildCount();
        List<Node> children = function.getNamedChildren();
        Assertions.assertEquals(count, children.size());
        for (int i = 0; i < count; i++) {
            Assertions.assertEquals(function.getNamedChild(i), children.get(i));
        }
        Assertions.assertEquals(empty.getNamedChildren(), List.of());
    }

    @Test
    void testGetNamedDescendantForByteRange() {
        Node function = root.getChild(0);
        Node identifier = function.getChild(1);
        int startByte = identifier.getStartByte();
        int endByte = identifier.getEndByte();
        Assertions.assertEquals(identifier, root.getNamedDescendant(startByte, endByte));
    }

    @ParameterizedTest(name = "[{index}] {0}")
    @MethodSource("provideStartAndEndBytes")
    void testGetNamedDescendantForByteRangeThrows(Class<Throwable> throwableType, int startByte, int endByte) {
        Node function = root.getChild(0);
        Node identifier = function.getChild(1);
        Assertions.assertThrows(throwableType, () -> identifier.getNamedDescendant(startByte, endByte));
    }

    @Test
    void testGetNamedDescendantForPointRange() {
        Node function = root.getChild(0);
        Node identifier = function.getChild(1);
        Point startPoint = identifier.getStartPoint();
        Point endPoint = identifier.getEndPoint();
        Assertions.assertEquals(identifier, root.getNamedDescendant(startPoint, endPoint));
    }

    @ParameterizedTest(name = "[{index}] {0}")
    @MethodSource("provideStartAndEndPoints")
    void testGetNamedDescendantForPointRangeThrows(Class<Throwable> throwableType, Point startPoint, Point endPoint) {
        Assertions.assertThrows(throwableType, () -> root.getNamedDescendant(startPoint, endPoint));
    }

    @Test
    void testGetNextParseState() {
        Assertions.assertEquals(0, root.getNextParseState());
        Assertions.assertEquals(-1, empty.getNextParseState());
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
    void testGetParent() {
        Assertions.assertNull(root.getParent());
        Assertions.assertEquals(root, root.getChild(0).getParent());
    }

    @Test
    void testGetParseState() {
        Assertions.assertEquals(0, root.getParseState());
        Assertions.assertEquals(-1, empty.getParseState());
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
        Assertions.assertEquals(0, empty.getStartByte());
    }

    @Test
    void testGetStartPoint() {
        Point startPoint = root.getStartPoint();
        Assertions.assertEquals(0, startPoint.getRow());
        Assertions.assertEquals(0, startPoint.getColumn());
        Assertions.assertTrue(empty.getStartPoint().isOrigin());
    }

    @Test
    void testGetSymbol() {
        Symbol symbol = root.getSymbol();
        Assertions.assertEquals("module", symbol.getName());
        Assertions.assertEquals(Symbol.Type.REGULAR, symbol.getType());
        Assertions.assertNull(empty.getSymbol());
    }

    @Test
    void testGetType() {
        Assertions.assertEquals("module", root.getType());
        Assertions.assertEquals("function_definition", root.getChild(0).getType());
        Assertions.assertNull(empty.getType());
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
    void testIsError() {
        @Cleanup Tree tree = parser.parse("def foo(bar baz):\n  pass");
        Node root = tree.getRootNode();
        Node function = root.getChild(0);
        Node parameters = function.getChildByFieldName("parameters");
        Assertions.assertFalse(root.isError());
        Assertions.assertFalse(function.isError());
        Assertions.assertFalse(parameters.getChild(0).isError());
        Assertions.assertFalse(parameters.getChild(1).isError());
        Assertions.assertTrue(parameters.getChild(2).isError());
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
        @Cleanup Parser parser = Parser.getFor(Language.JAVA);
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
        Assertions.assertTrue(empty.isNull());
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
        Iterator<Node> emptyIterator = empty.iterator();
        emptyIterator.next();
        Assertions.assertFalse(emptyIterator.hasNext());
        Assertions.assertThrows(NoSuchElementException.class, emptyIterator::next);
    }
}
