package ch.usi.si.seart.treesitter;

import lombok.Cleanup;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.List;
import java.util.stream.Stream;

class TreeTest extends BaseTest {

    private static final String source = "class Main {\n    // This is a line comment\n}\n";
    private static Parser parser;
    private Tree tree;
    private Node root;
    private Range range;

    @BeforeAll
    static void beforeAll() {
        parser = Parser.getFor(Language.JAVA);
    }

    @BeforeEach
    void setUp() {
        tree = parser.parse(source);
        root = tree.getRootNode();
        range = root.getRange();
    }

    @AfterEach
    void tearDown() {
        tree.close();
    }

    @AfterAll
    static void afterAll() {
        parser.close();
    }

    @Test
    void testGetSource() {
        Assertions.assertEquals(source, tree.getSource());
    }

    private static class ByteRangeContentProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
            Tree tree = parser.parse(source);
            Node root = tree.getRootNode();
            Node name = root.getChild(0).getChildByFieldName("name");
            Node body = root.getChild(0).getChildByFieldName("body");
            Node leftCurly = body.getChild(0);
            Node comment = body.getChild(1);
            Node rightCurly = body.getChild(2);
            return Stream.of(
                    Arguments.of(0, 45, root),
                    Arguments.of(6, 10, name),
                    Arguments.of(11, 12, leftCurly),
                    Arguments.of(17, 42, comment),
                    Arguments.of(43, 44, rightCurly)
            );
        }
    }

    @ParameterizedTest(name = "[{index}] {0} - {1}")
    @ArgumentsSource(ByteRangeContentProvider.class)
    void testGetSourceStartEnd(int beginIndex, int endIndex, Node node) {
        String expected = source.substring(beginIndex, endIndex);
        String actual = tree.getSource(node.getStartByte(), node.getEndByte());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testEdit() {
        Assertions.assertEquals(new Point(0, 0), range.getStartPoint());
        Assertions.assertEquals(new Point(3, 0), range.getEndPoint());
        Assertions.assertFalse(root.hasChanges());
        Node body = root.getChild(0).getChildByFieldName("body");
        int newEndByte = 13;
        Point newEndPoint = new Point(1, 1);
        InputEdit inputEdit = new InputEdit(
                body.getStartByte(),
                body.getEndByte(),
                newEndByte,
                body.getStartPoint(),
                body.getEndPoint(),
                newEndPoint
        );
        tree.edit(inputEdit);
        Assertions.assertTrue(root.hasChanges());
        Tree modified = parser.parse("class Main {\n}\n", tree);
        List<Range> ranges = tree.getChangedRanges(modified);
        Assertions.assertNotNull(ranges);
        Assertions.assertFalse(ranges.isEmpty());
        Assertions.assertEquals(1, ranges.size());
        root = modified.getRootNode();
        range = root.getRange();
        Assertions.assertEquals("program", root.getType());
        Assertions.assertEquals(new Point(0, 0), range.getStartPoint());
        Assertions.assertEquals(new Point(2, 0), range.getEndPoint());
        Assertions.assertFalse(root.hasChanges());
    }

    @Test
    void testClone() {
        @Cleanup Tree copy = tree.clone();
        Assertions.assertNotEquals(tree, copy);
    }

    @Test
    void testConstructorThrows() {
        @Cleanup Tree tree = new Tree(0L, Language.JAVA, "");
        Assertions.assertThrows(NullPointerException.class, () -> tree.edit(null));
    }
}
