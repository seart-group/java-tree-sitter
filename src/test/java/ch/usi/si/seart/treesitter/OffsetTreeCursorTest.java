package ch.usi.si.seart.treesitter;

import ch.usi.si.seart.treesitter.printer.PrinterTestBase;
import ch.usi.si.seart.treesitter.printer.SyntaxTreePrinter;
import ch.usi.si.seart.treesitter.printer.TreePrinter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class OffsetTreeCursorTest extends PrinterTestBase {

    @Test
    @SuppressWarnings("resource")
    void testCursorThrows() {
        Assertions.assertThrows(
                NullPointerException.class,
                () -> new OffsetTreeCursor(new Node(1, 1, 1, 1, 1, null), null)
        );
    }

    @Disabled
    @Override
    protected void testPrinterThrows() {
    }

    @Override
    protected TreeCursor getCursor(Tree tree) {
        Node root = tree.getRootNode();
        Node method = root.getChild(1).getChildByFieldName("body").getChild(1);
        return new OffsetTreeCursor(method, new Point(-1, -2));
    }

    @Override
    protected String getExpected() {
        return "method_declaration [2:2] - [5:3]\n" +
                "  modifiers [2:2] - [2:15]\n" +
                "  type: void_type [2:16] - [2:20]\n" +
                "  name: identifier [2:21] - [2:25]\n" +
                "  parameters: formal_parameters [2:25] - [2:40]\n" +
                "    formal_parameter [2:26] - [2:39]\n" +
                "      type: array_type [2:26] - [2:34]\n" +
                "        element: type_identifier [2:26] - [2:32]\n" +
                "        dimensions: dimensions [2:32] - [2:34]\n" +
                "      name: identifier [2:35] - [2:39]\n" +
                "  body: block [2:41] - [5:3]\n" +
                "    line_comment [3:6] - [3:20]\n" +
                "    expression_statement [4:6] - [4:42]\n" +
                "      method_invocation [4:6] - [4:41]\n" +
                "        object: field_access [4:6] - [4:16]\n" +
                "          object: identifier [4:6] - [4:12]\n" +
                "          field: identifier [4:13] - [4:16]\n" +
                "        name: identifier [4:17] - [4:24]\n" +
                "        arguments: argument_list [4:24] - [4:41]\n" +
                "          string_literal [4:25] - [4:40]\n";
    }

    @Override
    protected TreePrinter getPrinter(TreeCursor cursor) {
        return new SyntaxTreePrinter(cursor);
    }
}
