package ch.usi.si.seart.treesitter.printer;

import ch.usi.si.seart.treesitter.TreeCursor;
import ch.usi.si.seart.treesitter.TreeCursorNode;
import org.jetbrains.annotations.NotNull;

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
 * @since 1.2.0
 * @see <a href="https://tree-sitter.github.io/tree-sitter/playground">Syntax Tree Playground</a>
 * @author Ozren Dabić
 */
public class SyntaxTreePrinter extends IterativeTreePrinter {

    public SyntaxTreePrinter(@NotNull TreeCursor cursor) {
        super(cursor);
    }

    @Override
    protected String getFileExtension() {
        return ".txt";
    }

    protected void write(Consumer<String> appender) {
        for (;;) {
            TreeCursorNode cursorNode = cursor.getCurrentTreeCursorNode();
            if (cursorNode.isNamed()) {
                int depth = cursor.getCurrentDepth();
                String indent = "  ".repeat(depth);
                appender.accept(indent);
                appender.accept(cursorNode.toString());
                appender.accept("\n");
            }
            if (cursor.gotoFirstChild() || cursor.gotoNextSibling()) continue;
            do {
                if (!cursor.gotoParent()) return;
            } while (!cursor.gotoNextSibling());
        }
    }
}
