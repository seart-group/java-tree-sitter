package usi.si.seart.treesitter;

import lombok.Cleanup;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;

class ParserTest extends TestBase {

    @TempDir
    private static Path tmp;
    private static Path tmpFile;

    private static Parser parser;

    private static final String source = "print(\"hi\")\n";
    private static final String nodeString =
            "(module (expression_statement (call function: (identifier) arguments: (argument_list (string)))))";

    @BeforeAll
    static void beforeAll() throws IOException {
        tmpFile = Files.createFile(tmp.resolve("print.py"));
        Files.writeString(tmpFile, source);
        parser = new Parser(Language.PYTHON);
    }

    @AfterAll
    static void afterAll() {
        parser.close();
    }

    @Test
    @SneakyThrows(UnsupportedEncodingException.class)
    void testParseString() {
        @Cleanup Tree tree = parser.parseString(source);
        Assertions.assertEquals(nodeString, tree.getRootNode().getNodeString());
    }

    @Test
    @SneakyThrows(IOException.class)
    void testParseFile() {
        @Cleanup Tree tree = parser.parseFile(tmpFile);
        Assertions.assertEquals(nodeString, tree.getRootNode().getNodeString());
    }

    @Test
    @SuppressWarnings("resource")
    void testParserThrows() {
        Assertions.assertThrows(NullPointerException.class, () -> new Parser(null));
        Assertions.assertThrows(UnsatisfiedLinkError.class, () -> new Parser(Language._INVALID_));
    }
}
