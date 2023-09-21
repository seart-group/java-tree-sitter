package ch.usi.si.seart.treesitter;

import lombok.Cleanup;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.Optional;
import java.util.Spliterator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
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
        parser = new Parser(language);
        tree = parser.parse(source);
        root = tree.getRootNode();
    }

    @BeforeEach
    void setUp() {
        query = new Query(language, pattern);
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
        for (QueryMatch match: cursor) {
            check(match);
            count++;
        }
        Assertions.assertEquals(3, count);
    }

    @Test
    void testExecuteWithWhile() {
        cursor.execute();
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
        Iterator<QueryMatch> iterator = cursor.iterator();
        iterator.forEachRemaining(match -> {
            check(match);
            count.incrementAndGet();
        });
        Assertions.assertEquals(3, count.get());
    }

    @Test
    void testExecuteWithStream() {
        AtomicInteger count = new AtomicInteger();
        Spliterator<QueryMatch> spliterator = cursor.spliterator();
        StreamSupport.stream(spliterator, false).forEach(match -> {
            check(match);
            count.incrementAndGet();
        });
        Assertions.assertEquals(3, count.get());
    }

    private static void check(QueryMatch match) {
        QueryCapture[] captures = match.getCaptures();
        Optional<QueryCapture> optional = Stream.of(captures).findFirst();
        Assertions.assertTrue(optional.isPresent());
        QueryCapture capture = optional.get();
        Node node = capture.getNode();
        Assertions.assertEquals(0, capture.getIndex());
        Assertions.assertEquals("block_comment", node.getType());
    }

    @Test
    void testExecuteNoResultQuery() {
        @Cleanup Query query = new Query(language, "(method_declaration) @method");
        @Cleanup QueryCursor cursor = root.walk(query);
        cursor.execute();
        Assertions.assertNull(cursor.nextMatch());
    }

    @Test
    void testMultipleExecuteCalls() {
        @Cleanup Query query = new Query(language, "(class_body) @class");
        @Cleanup QueryCursor cursor = root.walk(query);
        cursor.execute();
        cursor.execute();
        QueryMatch match = cursor.nextMatch();
        Assertions.assertNotNull(match);
        Assertions.assertNull(cursor.nextMatch());
    }
}
