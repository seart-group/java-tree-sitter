package ch.usi.si.seart.treesitter.printer;

import ch.usi.si.seart.treesitter.Language;
import ch.usi.si.seart.treesitter.Parser;
import ch.usi.si.seart.treesitter.TestBase;
import ch.usi.si.seart.treesitter.Tree;
import ch.usi.si.seart.treesitter.TreeCursor;
import lombok.Cleanup;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;

public abstract class PrinterTestBase extends TestBase {

    @Test
    @SneakyThrows(UnsupportedEncodingException.class)
    void testPrint() {
        String source = getSource();
        @Cleanup Parser parser = getParser();
        @Cleanup Tree tree = parser.parseString(source);
        @Cleanup TreeCursor cursor = getCursor(tree);
        TreePrinter printer = getPrinter(cursor);
        String expected = getExpected();
        String actual = printer.print();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @SneakyThrows({UnsupportedEncodingException.class, IOException.class})
    void testExport() {
        String source = getSource();
        @Cleanup Parser parser = getParser();
        @Cleanup Tree tree = parser.parseString(source);
        @Cleanup TreeCursor cursor = getCursor(tree);
        TreePrinter printer = getPrinter(cursor);
        String expected = getExpected();
        File file = printer.export();
        Path path = file.toPath();
        String actual = Files.readString(path);
        file.deleteOnExit();
        Assertions.assertEquals(expected, actual);
    }

    protected Parser getParser() {
        return new Parser(Language.JAVA);
    }

    protected TreeCursor getCursor(Tree tree) {
        return tree.getRootNode().walk();
    }

    protected String getSource() {
        return "package ch.usi.si;\n" +
                "\n" +
                "public class Main {\n" +
                "    public static void main(String[] args) {\n" +
                "        //line comment\n" +
                "        System.out.println(\"Hello, World!\");\n" +
                "    }\n" +
                "}";
    }

    protected abstract String getExpected();

    protected abstract TreePrinter getPrinter(TreeCursor cursor);
}
