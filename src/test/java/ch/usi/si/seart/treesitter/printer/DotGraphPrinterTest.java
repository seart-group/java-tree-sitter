package ch.usi.si.seart.treesitter.printer;

import ch.usi.si.seart.treesitter.Language;
import ch.usi.si.seart.treesitter.Parser;
import ch.usi.si.seart.treesitter.TestBase;
import ch.usi.si.seart.treesitter.Tree;
import lombok.Cleanup;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DotGraphPrinterTest extends TestBase {

    private static final String source =
            "package ch.usi.si;\n" +
            "\n" +
            "public class Main {\n" +
            "    public static void main(String[] args) {\n" +
            "        //line comment\n" +
            "        System.out.println(\"Hello, World!\");\n" +
            "    }\n" +
            "}";

    @Test
    @SneakyThrows(UnsupportedEncodingException.class)
    void testPrint() {
        @Cleanup Parser parser = new Parser(Language.JAVA);
        @Cleanup Tree tree = parser.parseString(source);
        TreePrinter printer = new DotGraphPrinter(tree);
        String actual = printer.print();
        assertion(actual);
    }

    @Test
    @SneakyThrows({UnsupportedEncodingException.class, IOException.class})
    void testExport() {
        @Cleanup Parser parser = new Parser(Language.JAVA);
        @Cleanup Tree tree = parser.parseString(source);
        TreePrinter printer = new DotGraphPrinter(tree);
        File file = printer.export();
        Path path = file.toPath();
        String actual = Files.readString(path);
        Files.delete(file.toPath());
        assertion(actual);
    }

    private void assertion(String result) {
        Assertions.assertEquals(594, result.lines().count());
        Assertions.assertTrue(result.startsWith("digraph tree {"));
        Assertions.assertTrue(result.endsWith("}\n"));
    }
}
