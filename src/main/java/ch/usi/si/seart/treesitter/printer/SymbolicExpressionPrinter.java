package ch.usi.si.seart.treesitter.printer;

import ch.usi.si.seart.treesitter.TreeCursor;
import ch.usi.si.seart.treesitter.TreeCursorNode;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * Printer used for generating Symbolic Expression (S-expression)
 * representations of trees using an iterative approach.
 * Note that unlike the CLI counterpart, the resulting symbolic
 * expression does not contain the positional information of the
 * node.
 *
 * @since 1.4.0
 * @see <a href="https://github.com/tree-sitter/tree-sitter/blob/293f0d1ca30a63839810ad4b943c0f19f1cb4933/cli/src/parse.rs#L130-L184">Rust implementation</a>
 * @author Ozren Dabić
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SymbolicExpressionPrinter extends IterativeTreePrinter {

    protected SymbolicExpressionPrinter(@NotNull TreeCursor cursor) {
        super(cursor);
    }

    @Override
    protected String getFileExtension() {
        return ".scm";
    }

    protected void write(Consumer<String> appender) {
        boolean needsSpace = false;
        boolean visitedChildren = false;
        for (;;) {
            TreeCursorNode cursorNode = cursor.getCurrentTreeCursorNode();
            boolean isNamed = cursorNode.isNamed();
            String type = cursorNode.getType();
            String name = cursorNode.getName();
            if (visitedChildren) {
                if (isNamed) {
                    appender.accept(")");
                    needsSpace = true;
                }
                if (cursor.gotoNextSibling()) visitedChildren = false;
                else if (cursor.gotoParent()) visitedChildren = true;
                else return;
            } else {
                if (isNamed) {
                    if (needsSpace)
                        appender.accept(" ");
                    if (name != null) {
                        appender.accept(name);
                        appender.accept(": ");
                    }
                    appender.accept("(");
                    appender.accept(type);
                    needsSpace = true;
                }
                visitedChildren = !cursor.gotoFirstChild();
            }
        }
    }
}
