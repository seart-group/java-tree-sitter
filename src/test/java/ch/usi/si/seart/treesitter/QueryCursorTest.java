package ch.usi.si.seart.treesitter;

import ch.usi.si.seart.treesitter.exception.ByteOffsetOutOfBoundsException;
import ch.usi.si.seart.treesitter.exception.PointOutOfBoundsException;
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

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Spliterator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

class QueryCursorTest extends TestBase {

    private static final String source = "class Hello { /* Comment 1 */ /* Comment 2 */ /* Comment 3 */ }";
    private static final String pattern = "(block_comment) @comment";
    private static final Language language = Language.JAVA;
    private static Parser parser;
    private static Tree tree;
    private static Node root;
    private static Node body;

    private Query query;
    private QueryCursor cursor;

    @BeforeAll
    static void beforeAll() {
        parser = Parser.builder().language(language).build();
        tree = parser.parse(source);
        root = tree.getRootNode();
        body = root.getChild(0).getChildByFieldName("body");
    }

    @BeforeEach
    void setUp() {
        query = Query.getFor(language, pattern);
        cursor = root.walk(query);
    }

    @AfterEach
    void tearDown() {
        cursor.close();
        query.close();
    }

    @AfterAll
    static void afterAll() {
        tree.close();
        parser.close();
    }

    @Test
    void testExecuteWithFor() {
        int count = 0;
        Assertions.assertFalse(cursor.isExecuted());
        for (QueryMatch match: cursor) {
            check(match);
            count++;
        }
        Assertions.assertTrue(cursor.isExecuted());
        Assertions.assertEquals(3, count);
    }

    @Test
    void testExecuteWithWhile() {
        Assertions.assertFalse(cursor.isExecuted());
        cursor.execute();
        Assertions.assertTrue(cursor.isExecuted());
        int count = 0;
        QueryMatch match;
        while ((match = cursor.nextMatch()) != null) {
            check(match);
            count++;
        }
        Assertions.assertEquals(3, count);
    }

    @Test
    void testExecuteWithIterator() {
        AtomicInteger count = new AtomicInteger();
        Assertions.assertFalse(cursor.isExecuted());
        Iterator<QueryMatch> iterator = cursor.iterator();
        Assertions.assertTrue(cursor.isExecuted());
        iterator.forEachRemaining(match -> {
            check(match);
            count.incrementAndGet();
        });
        Assertions.assertEquals(3, count.get());
    }

    @Test
    void testExecuteWithStream() {
        AtomicInteger count = new AtomicInteger();
        Assertions.assertFalse(cursor.isExecuted());
        Spliterator<QueryMatch> spliterator = cursor.spliterator();
        Assertions.assertTrue(cursor.isExecuted());
        StreamSupport.stream(spliterator, false).forEach(match -> {
            check(match);
            count.incrementAndGet();
        });
        Assertions.assertEquals(3, count.get());
    }

    private static void check(QueryMatch match) {
        Map<Capture, Collection<Node>> captures = match.getCaptures();
        Set<Map.Entry<Capture, Collection<Node>>> entries = captures.entrySet();
        Map.Entry<Capture, Collection<Node>> entry = entries.stream().findFirst().orElseGet(Assertions::fail);
        Capture capture = entry.getKey();
        Node node = entry.getValue().stream().findFirst().orElseGet(Assertions::fail);
        Assertions.assertEquals(0, capture.getIndex());
        Assertions.assertEquals("comment", capture.getName());
        Assertions.assertEquals("block_comment", node.getType());
    }

    @Test
    void testExecuteNoResultQuery() {
        @Cleanup Query query = Query.getFor(language, "(method_declaration) @method");
        @Cleanup QueryCursor cursor = root.walk(query);
        Assertions.assertFalse(cursor.isExecuted());
        cursor.execute();
        Assertions.assertTrue(cursor.isExecuted());
        Assertions.assertNull(cursor.nextMatch());
    }

    @Test
    void testExecuteByteRange() {
        List<Node> comments = body.getChildren().stream()
                .filter(Node::isNamed)
                .collect(Collectors.toUnmodifiableList());
        @Cleanup QueryCursor cursor = root.walk(query);
        Assertions.assertFalse(cursor.isExecuted());
        for (Node comment: comments) {
            int lowerByte = comment.getStartByte();
            int upperByte = comment.getEndByte();
            cursor.execute(lowerByte, upperByte);
            Assertions.assertTrue(cursor.isExecuted());
            Assertions.assertNotNull(cursor.nextMatch());
            Assertions.assertNull(cursor.nextMatch());
        }
    }

    private static class ByteRangeExceptionProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
            return Stream.of(
                    Arguments.of(IllegalArgumentException.class, -1, body.getEndByte()),
                    Arguments.of(IllegalArgumentException.class, body.getStartByte(), body.getEndByte() * -1),
                    Arguments.of(IllegalArgumentException.class, body.getEndByte(), body.getStartByte()),
                    Arguments.of(ByteOffsetOutOfBoundsException.class, root.getStartByte(), body.getEndByte()),
                    Arguments.of(ByteOffsetOutOfBoundsException.class, body.getStartByte(), root.getEndByte() + 1)
            );
        }
    }

    @ParameterizedTest(name = "[{index}] {0}")
    @ArgumentsSource(ByteRangeExceptionProvider.class)
    void testExecuteByteRangeThrows(Class<Throwable> throwableType, int startByte, int endByte) {
        @Cleanup QueryCursor cursor = body.walk(query);
        Assertions.assertThrows(throwableType, () -> cursor.execute(startByte, endByte));
    }

    @Test
    void testExecutePointRange() {
        List<Node> comments = body.getChildren().stream()
                .filter(Node::isNamed)
                .collect(Collectors.toUnmodifiableList());
        @Cleanup QueryCursor cursor = root.walk(query);
        Assertions.assertFalse(cursor.isExecuted());
        for (Node comment: comments) {
            Point lowerPoint = comment.getStartPoint();
            Point upperPoint = comment.getEndPoint();
            cursor.execute(lowerPoint, upperPoint);
            Assertions.assertTrue(cursor.isExecuted());
            Assertions.assertNotNull(cursor.nextMatch());
            Assertions.assertNull(cursor.nextMatch());
        }
    }

    private static class PointRangeExceptionProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
            return Stream.of(
                    Arguments.of(NullPointerException.class, null, new Point(0, 0)),
                    Arguments.of(NullPointerException.class, new Point(0, 0), null),
                    Arguments.of(IllegalArgumentException.class, new Point(-1, -1), body.getEndPoint()),
                    Arguments.of(IllegalArgumentException.class, body.getEndPoint(), body.getStartPoint()),
                    Arguments.of(PointOutOfBoundsException.class, root.getStartPoint(), body.getEndPoint()),
                    Arguments.of(PointOutOfBoundsException.class, body.getStartPoint(), new Point(1, 0))
            );
        }
    }

    @ParameterizedTest(name = "[{index}] {0}")
    @ArgumentsSource(PointRangeExceptionProvider.class)
    void testExecutePointRangeThrows(Class<Throwable> throwableType, Point startPoint, Point endPoint) {
        @Cleanup QueryCursor cursor = body.walk(query);
        Assertions.assertThrows(throwableType, () -> cursor.execute(startPoint, endPoint));
    }

    @Test
    void testExecuteReuse() {
        @Cleanup Query query = Query.getFor(language, "(class_body) @class");
        @Cleanup QueryCursor cursor = root.walk(query);
        Assertions.assertFalse(cursor.isExecuted());
        cursor.execute();
        Assertions.assertTrue(cursor.isExecuted());
        Assertions.assertNotNull(cursor.nextMatch());
        Assertions.assertNull(cursor.nextMatch());
        cursor.execute();
        Assertions.assertTrue(cursor.isExecuted());
        Assertions.assertNotNull(cursor.nextMatch());
        Assertions.assertNull(cursor.nextMatch());
    }

    @Test
    @SuppressWarnings("resource")
    void testConstructorThrows() {
        Assertions.assertThrows(NullPointerException.class, () -> root.walk(null));
        Assertions.assertThrows(IllegalStateException.class, () -> empty.walk(query));
        Assertions.assertThrows(IllegalStateException.class, () -> treeless.walk(query));
    }
}
