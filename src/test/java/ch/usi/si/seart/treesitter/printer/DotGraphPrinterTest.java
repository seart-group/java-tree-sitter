package ch.usi.si.seart.treesitter.printer;

import ch.usi.si.seart.treesitter.BaseTest;
import ch.usi.si.seart.treesitter.Language;
import ch.usi.si.seart.treesitter.Parser;
import ch.usi.si.seart.treesitter.Tree;
import lombok.Cleanup;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

class DotGraphPrinterTest extends BaseTest {

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
    void testPrint() {
        @Cleanup Parser parser = Parser.getFor(Language.JAVA);
        @Cleanup Tree tree = parser.parse(source);
        TreePrinter printer = new DotGraphPrinter(tree);
        String actual = printer.print();
        assertion(actual);
    }

    @Test
    @SneakyThrows(IOException.class)
    void testExport() {
        @Cleanup Parser parser = Parser.getFor(Language.JAVA);
        @Cleanup Tree tree = parser.parse(source);
        TreePrinter printer = new DotGraphPrinter(tree);
        File file = printer.export();
        Path path = file.toPath();
        String actual = Files.readString(path);
        Files.delete(file.toPath());
        assertion(actual);
    }

    @Test
    void testPrinterThrows() {
        Assertions.assertThrows(NullPointerException.class, () -> new DotGraphPrinter(null));
    }

    private void assertion(String result) {
        Assertions.assertEquals(700, result.lines().count());
        Assertions.assertTrue(result.startsWith("digraph tree {"));
        Assertions.assertTrue(result.endsWith("}\n"));
    }
}
