package ai.serenade.treesitter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;

class QueryTest extends TestBase {

    @Test
    void testQuery() throws UnsupportedEncodingException {
        try (Query query = new Query(Language.JAVA, "(class_declaration)")) {
            Assertions.assertNotEquals(0, query.getPointer(), "Pointer is not null");
        }
    }

    @Test
    void testInvalidQuery() throws UnsupportedEncodingException {
        try (Query query = new Query(Language.JAVA, "(class_declaration")) {
            Assertions.assertEquals(0, query.getPointer(), "Pointer is null");
        }
    }
}
