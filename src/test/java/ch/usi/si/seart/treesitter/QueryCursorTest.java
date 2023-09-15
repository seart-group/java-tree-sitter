package ch.usi.si.seart.treesitter;

import lombok.Cleanup;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.Optional;
import java.util.Spliterator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

class QueryCursorTest extends TestBase {

    private static final String source = "class Hello { /* Comment 1 */ /* Comment 2 */ /* Comment 3 */ }";
    private static final Language language = Language.JAVA;
    private static Parser parser;
    private static Tree tree;
    private static Node root;

    @BeforeAll
    static void beforeAll() {
        parser = new Parser(language);
        tree = parser.parse(source);
        root = tree.getRootNode();
    }

    @AfterAll
    static void afterAll() {
        tree.close();
        parser.close();
    }

    @Test
    void testExecWithFor() {
        @Cleanup Query query = new Query(language, "(block_comment) @comment");
        @Cleanup QueryCursor cursor = new QueryCursor(root, query);
        int count = 0;
        for (QueryMatch match: cursor) {
            check(match);
            count++;
        }
        Assertions.assertEquals(3, count, "Must find three matches!");
    }

    @Test
    void testExecWithWhile() {
        @Cleanup Query query = new Query(language, "(block_comment) @comment");
        @Cleanup QueryCursor cursor = new QueryCursor(root, query);
        cursor.execute();
        int count = 0;
        QueryMatch match;
        while ((match = cursor.nextMatch()) != null) {
            check(match);
            count++;
        }
        Assertions.assertEquals(3, count, "Must find three matches!");
    }

    @Test
    void testExecWithIterator() {
        @Cleanup Query query = new Query(language, "(block_comment) @comment");
        @Cleanup QueryCursor cursor = new QueryCursor(root, query);
        AtomicInteger count = new AtomicInteger();
        Iterator<QueryMatch> iterator = cursor.iterator();
        iterator.forEachRemaining(match -> {
            check(match);
            count.incrementAndGet();
        });
        Assertions.assertEquals(3, count.get(), "Must find three matches!");
    }

    @Test
    void testExecWithStream() {
        @Cleanup Query query = new Query(language, "(block_comment) @comment");
        @Cleanup QueryCursor cursor = new QueryCursor(root, query);
        AtomicInteger count = new AtomicInteger();
        Spliterator<QueryMatch> spliterator = cursor.spliterator();
        StreamSupport.stream(spliterator, false).forEach(match -> {
            check(match);
            count.incrementAndGet();
        });
        Assertions.assertEquals(3, count.get(), "Must find three matches!");
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
    void testExecNoResultQuery() {
        @Cleanup Query query = new Query(language, "(method_declaration) @method");
        @Cleanup QueryCursor cursor = new QueryCursor(root, query);
        cursor.execute();
        Assertions.assertNull(cursor.nextMatch(), "Must find no matches!");
    }

    @Test
    void testMultipleExecCalls() {
        @Cleanup Query query = new Query(language, "(class_body) @class");
        @Cleanup QueryCursor cursor = new QueryCursor(root, query);
        cursor.execute();
        cursor.execute();
        QueryMatch match = cursor.nextMatch();
        Assertions.assertNotNull(match);
        Assertions.assertNull(cursor.nextMatch());
    }
}
