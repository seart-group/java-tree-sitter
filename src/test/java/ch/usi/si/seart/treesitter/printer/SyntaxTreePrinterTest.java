package ch.usi.si.seart.treesitter.printer;

import ch.usi.si.seart.treesitter.TreeCursor;

public class SyntaxTreePrinterTest extends PrinterTestBase {

    @Override
    protected String getExpected() {
        return "program [0:0] - [7:1]\n" +
                "  package_declaration [0:0] - [0:18]\n" +
                "    scoped_identifier [0:8] - [0:17]\n" +
                "      scope: scoped_identifier [0:8] - [0:14]\n" +
                "        scope: identifier [0:8] - [0:10]\n" +
                "        name: identifier [0:11] - [0:14]\n" +
                "      name: identifier [0:15] - [0:17]\n" +
                "  class_declaration [2:0] - [7:1]\n" +
                "    modifiers [2:0] - [2:6]\n" +
                "    name: identifier [2:13] - [2:17]\n" +
                "    body: class_body [2:18] - [7:1]\n" +
                "      method_declaration [3:4] - [6:5]\n" +
                "        modifiers [3:4] - [3:17]\n" +
                "        type: void_type [3:18] - [3:22]\n" +
                "        name: identifier [3:23] - [3:27]\n" +
                "        parameters: formal_parameters [3:27] - [3:42]\n" +
                "          formal_parameter [3:28] - [3:41]\n" +
                "            type: array_type [3:28] - [3:36]\n" +
                "              element: type_identifier [3:28] - [3:34]\n" +
                "              dimensions: dimensions [3:34] - [3:36]\n" +
                "            name: identifier [3:37] - [3:41]\n" +
                "        body: block [3:43] - [6:5]\n" +
                "          line_comment [4:8] - [4:22]\n" +
                "          expression_statement [5:8] - [5:44]\n" +
                "            method_invocation [5:8] - [5:43]\n" +
                "              object: field_access [5:8] - [5:18]\n" +
                "                object: identifier [5:8] - [5:14]\n" +
                "                field: identifier [5:15] - [5:18]\n" +
                "              name: identifier [5:19] - [5:26]\n" +
                "              arguments: argument_list [5:26] - [5:43]\n" +
                "                string_literal [5:27] - [5:42]\n";
    }

    @Override
    protected TreePrinter getPrinter(TreeCursor cursor) {
        return new SyntaxTreePrinter(cursor);
    }
}
