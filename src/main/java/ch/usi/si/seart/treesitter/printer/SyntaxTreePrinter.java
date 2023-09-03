package ch.usi.si.seart.treesitter.printer;

import ch.usi.si.seart.treesitter.TreeCursor;
import ch.usi.si.seart.treesitter.TreeCursorNode;
import ch.usi.si.seart.treesitter.function.IOExceptionThrowingConsumer;
import lombok.AccessLevel;
import lombok.Cleanup;
import lombok.experimental.FieldDefaults;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.nio.file.Files;
import java.util.function.Consumer;

/**
 * Printer used for generating human-readable
 * representations of trees using an iterative approach.
 * Each node of the subtree is represented as follows:
 * <pre>{@code
 *      optional_field_name: node_name [start_column:start_row] - [end_column:end_row]
 * }</pre>
 * While indentation is used to represent the tree structure.
 *
 * @see <a href="https://tree-sitter.github.io/tree-sitter/playground">Syntax Tree Playground</a>
 * @author Ozren Dabić
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SyntaxTreePrinter extends IterativeTreePrinter {

    int depth = 0;

    public SyntaxTreePrinter(TreeCursor cursor) {
        super(cursor);
    }

    /**
     * Generates a human-readable representation of the tree,
     * starting from the node currently pointed to by the cursor.
     *
     * @return A human-readable print-out of the tree
     */
    @Override
    public String print() {
        StringBuilder stringBuilder = new StringBuilder();
        write(stringBuilder::append);
        return stringBuilder.toString();
    }

    /**
     * @return A file containing human-readable print-out of the tree
     * @throws IOException if an I/O error occurs
     */
    @Override
    public File export() throws IOException {
        File file = Files.createTempFile("ts-export-", ".txt").toFile();
        @Cleanup Writer writer = new BufferedWriter(new FileWriter(file));
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
            if (cursorNode.isNamed()) {
                String indent = "  ".repeat(depth);
                appender.accept(indent);
                appender.accept(cursorNode.toString());
                appender.accept("\n");
            }
            if (cursor.gotoFirstChild()) {
                depth++;
                continue;
            } else if (cursor.gotoNextSibling()) {
                continue;
            }
            do {
                if (cursor.gotoParent()) {
                    depth--;
                } else {
                    return;
                }
            } while (!cursor.gotoNextSibling());
        }
    }
}
