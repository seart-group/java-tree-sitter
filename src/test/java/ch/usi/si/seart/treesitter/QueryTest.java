package ch.usi.si.seart.treesitter;

import ch.usi.si.seart.treesitter.exception.query.QueryCaptureException;
import ch.usi.si.seart.treesitter.exception.query.QueryFieldException;
import ch.usi.si.seart.treesitter.exception.query.QueryNodeTypeException;
import ch.usi.si.seart.treesitter.exception.query.QueryStructureException;
import ch.usi.si.seart.treesitter.exception.query.QuerySyntaxException;
import lombok.Cleanup;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class QueryTest extends TestBase {

    static Query query;

    @BeforeAll
    static void beforeAll() {
        query = new Query(Language.JAVA, "(_) @capture");
    }

    @AfterAll
    static void afterAll() {
        query.close();
    }

    @Test
    @SuppressWarnings("resource")
    void testQuery() {
        Assertions.assertNotNull(query, "Query is not null");
        Assertions.assertThrows(NullPointerException.class, () -> new Query(null, null));
        Assertions.assertThrows(NullPointerException.class, () -> new Query(Language.JAVA, null));
        Assertions.assertThrows(NullPointerException.class, () -> new Query(null, "(_)"));
        Assertions.assertThrows(UnsatisfiedLinkError.class, () -> new Query(Language._INVALID_, "(_)"));
        Assertions.assertThrows(QueryCaptureException.class, () -> new Query(Language.JAVA, "(#eq? @key @value)"));
        Assertions.assertThrows(QueryFieldException.class, () -> new Query(Language.JAVA, "(program unknown: (_))"));
        Assertions.assertThrows(QueryNodeTypeException.class, () -> new Query(Language.JAVA, "(null)"));
        Assertions.assertThrows(QueryStructureException.class, () -> new Query(Language.JAVA, "(program (program))"));
        Assertions.assertThrows(QuerySyntaxException.class, () -> new Query(Language.JAVA, "()"));
    }

    @Test
    void testQueryCount() {
        Assertions.assertEquals(1, query.countCaptures());
        Assertions.assertEquals(1, query.countPatterns());
        Assertions.assertEquals(0, query.countStrings());
    }

    @Test
    void testQueryCaptureName() {
        QueryCapture capture = new QueryCapture(new Node(), 0);
        Assertions.assertEquals("capture", query.getCaptureName(capture));
    }

    @Test
    void testQueryHasCaptures() {
        Assertions.assertTrue(query.hasCaptures());
        @Cleanup Query query = new Query(Language.JAVA, "(_)");
        Assertions.assertFalse(query.hasCaptures());
    }
}
