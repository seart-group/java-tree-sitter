package ch.usi.si.seart.treesitter;

import lombok.Cleanup;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Spliterator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.StreamSupport;

class QueryCursorTest extends TestBase {

    private static final String source = "class Hello { /* Comment 1 */ /* Comment 2 */ /* Comment 3 */ }";
    private static final String pattern = "(block_comment) @comment";
    private static final Language language = Language.JAVA;
    private static Parser parser;
    private static Tree tree;
    private static Node root;

    private Query query;
    private QueryCursor cursor;

    @BeforeAll
    static void beforeAll() {
        parser = Parser.builder().language(language).build();
        tree = parser.parse(source);
        root = tree.getRootNode();
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
