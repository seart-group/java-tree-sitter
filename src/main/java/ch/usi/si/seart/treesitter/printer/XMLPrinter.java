package ch.usi.si.seart.treesitter.printer;

import ch.usi.si.seart.treesitter.Point;
import ch.usi.si.seart.treesitter.TreeCursor;
import ch.usi.si.seart.treesitter.TreeCursorNode;
import ch.usi.si.seart.treesitter.function.IOExceptionThrowingConsumer;
import lombok.AccessLevel;
import lombok.Cleanup;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.nio.file.Files;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.Consumer;

/**
 * Printer used for generating Extensible Markup Language (XML)
 * representations of trees using an iterative approach.
 * Note that unlike the CLI counterpart, the resulting XML
 * document does not contain the actual source code contents.
 *
 * @see <a href="https://github.com/tree-sitter/tree-sitter/blob/293f0d1ca30a63839810ad4b943c0f19f1cb4933/cli/src/parse.rs#L186-L239">Rust implementation</a>
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class XMLPrinter extends IterativeTreePrinter {

    @NonFinal
    boolean visitedChildren = false;
    Deque<String> tags = new ArrayDeque<>();

    public XMLPrinter(TreeCursor cursor) {
        super(cursor);
    }

    /**
     * Generates an XML representation of the tree,
     * starting from the node currently pointed to by the cursor.
     *
     * @return An XML string of the tree.
     */
    @Override
    public String print() {
        StringBuilder stringBuilder = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        write(stringBuilder::append);
        return stringBuilder.toString();
    }

    /**
     * Generates an XML representation of the tree,
     * writing it directly to a file.
     *
     * @return A file containing the XML string of the tree.
     * @throws IOException if an I/O error occurs
     */
    @Override
    public File export() throws IOException {
        File file = Files.createTempFile("ts-export-", ".xml").toFile();
        @Cleanup Writer writer = new BufferedWriter(new FileWriter(file));
        writer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        Consumer<String> appender = IOExceptionThrowingConsumer.toUnchecked(writer::append);
        try {
            write(appender);
        } catch (UncheckedIOException ex) {
            throw ex.getCause();
        }
        return file;
    }

    private void write(Consumer<String> appender) {
        for (;;) {
            TreeCursorNode cursorNode = cursor.getCurrentTreeCursorNode();
            boolean isNamed = cursorNode.isNamed();
            String type = cursorNode.getType();
            String name = cursorNode.getName();
            Point start = cursorNode.getStartPoint();
            Point end = cursorNode.getEndPoint();
            if (visitedChildren) {
                if (isNamed) {
                    appender.accept("</");
                    appender.accept(tags.pop());
                    appender.accept(">");
                }
                if (cursor.gotoNextSibling()) visitedChildren = false;
                else if (cursor.gotoParent()) visitedChildren = true;
                else return;
            } else {
                if (isNamed) {
                    appender.accept("<");
                    appender.accept(type);
                    appender.accept(" ");
                    if (name != null) {
                        appender.accept("name=\"");
                        appender.accept(name);
                        appender.accept("\" ");
                    }
                    appender.accept("start=\"");
                    appender.accept(start.toString());
                    appender.accept("\" ");
                    appender.accept("end=\"");
                    appender.accept(end.toString());
                    appender.accept("\">");
                    tags.push(type);
                }
                visitedChildren = !cursor.gotoFirstChild();
            }
        }
    }
}
