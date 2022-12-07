package ai.serenade.treesitter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;

class QueryCursorTest extends TestBase {

    @Test
    void testExecSimpleQuery() throws UnsupportedEncodingException {
        try (Parser parser = new Parser()) {
            parser.setLanguage(Language.JAVA);
            try (Tree tree = parser.parseString("class Hello {}")) {
                try (Query query = new Query(Language.JAVA, "(class_body) @test")) {
                    try (QueryCursor cursor = new QueryCursor()) {
                        cursor.execQuery(query, tree.getRootNode());
                        int numMatches = 0;
                        while (cursor.nextMatch() != null) numMatches++;
                        Assertions.assertEquals(1, numMatches, "Finds one match");
                    }
                }
            }
        }
    }

    @Test
    void testExecNoResultQuery() throws UnsupportedEncodingException {
        try (Parser parser = new Parser()) {
            parser.setLanguage(Language.JAVA);
            try (Tree tree = parser.parseString("class Hello {}")) {
                try (Query query = new Query(Language.JAVA, "(method_declaration) @method")) {
                    try (QueryCursor cursor = new QueryCursor()) {
                        cursor.execQuery(query, tree.getRootNode());
                        Assertions.assertNull(cursor.nextMatch());
                    }
                }
            }

        }
    }
}
