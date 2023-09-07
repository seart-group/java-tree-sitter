package ch.usi.si.seart.treesitter.printer;

import ch.usi.si.seart.treesitter.Point;
import ch.usi.si.seart.treesitter.TreeCursor;
import ch.usi.si.seart.treesitter.TreeCursorNode;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.Consumer;

/**
 * Printer used for generating Extensible Markup Language (XML)
 * representations of trees using an iterative approach.
 * Note that unlike the CLI counterpart, the resulting XML
 * document does not contain the actual source code contents.
 *
 * @since 1.2.0
 * @see <a href="https://github.com/tree-sitter/tree-sitter/blob/293f0d1ca30a63839810ad4b943c0f19f1cb4933/cli/src/parse.rs#L186-L239">Rust implementation</a>
 * @author Ozren Dabić
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class XMLPrinter extends IterativeTreePrinter {

    public static final String PROLOG = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

    @NonFinal
    boolean visitedChildren = false;
    Deque<String> tags = new ArrayDeque<>();

    public XMLPrinter(@NotNull TreeCursor cursor) {
        super(cursor);
    }

    @Override
    protected String getPreamble() {
        return PROLOG;
    }

    @Override
    protected String getFileExtension() {
        return ".xml";
    }

    protected void write(Consumer<String> appender) {
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
