package ch.usi.si.seart.treesitter;

import lombok.Cleanup;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
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
        int numMatches = 0;
        for (QueryMatch ignored: cursor) numMatches++;
        Assertions.assertEquals(3, numMatches, "Must find three matches!");
    }

    @Test
    void testExecWithWhile() {
        @Cleanup Query query = new Query(language, "(block_comment) @comment");
        @Cleanup QueryCursor cursor = new QueryCursor(root, query);
        cursor.execute();
        int numMatches = 0;
        while (cursor.nextMatch() != null) numMatches++;
        Assertions.assertEquals(3, numMatches, "Must find three matches!");
    }

    @Test
    void testExecWithIterator() {
        @Cleanup Query query = new Query(language, "(block_comment) @comment");
        @Cleanup QueryCursor cursor = new QueryCursor(root, query);
        AtomicInteger numMatches = new AtomicInteger();
        Iterator<QueryMatch> iterator = cursor.iterator();
        iterator.forEachRemaining(ignored -> numMatches.incrementAndGet());
        Assertions.assertEquals(3, numMatches.get(), "Must find three matches!");
    }

    @Test
    void testExecWithStream() {
        @Cleanup Query query = new Query(language, "(block_comment) @comment");
        @Cleanup QueryCursor cursor = new QueryCursor(root, query);
        Spliterator<QueryMatch> spliterator = cursor.spliterator();
        Stream<QueryMatch> stream = StreamSupport.stream(spliterator, false);
        Assertions.assertEquals(3, stream.count(), "Must find three matches!");
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
        @Cleanup Query query = new Query(language, "(class_body) @test");
        @Cleanup QueryCursor cursor = new QueryCursor(root, query);
        cursor.execute();
        cursor.execute();
        QueryMatch match = cursor.nextMatch();
        Assertions.assertNotNull(match);
        Assertions.assertNull(cursor.nextMatch());
    }
}
