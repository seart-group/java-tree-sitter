package ch.usi.si.seart.treesitter;

import lombok.Cleanup;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;

class SyntaxTreePrinterTest extends TestBase {

    @Test
    @SneakyThrows(UnsupportedEncodingException.class)
    void testPrintSubtree() {
        String expected;
        String actual;
        SyntaxTreePrinter printer;
        @Cleanup Parser parser = new Parser(Language.JAVA);
        @Cleanup Tree tree = parser.parseString(
                "package ch.usi.si;\n" +
                "\n" +
                "public class Main {\n" +
                "    public static void main(String[] args) {\n" +
                "        //line comment\n" +
                "        System.out.println(\"Hello, World!\");\n" +
                "    }\n" +
                "}"
        );
        Node root = tree.getRootNode();
        printer = new SyntaxTreePrinter(root);
        actual = printer.printSubtree();
        expected =
                "program [0:0] - [7:1]\n" +
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
        Assertions.assertEquals(expected, actual);

        Node method = root.getChild(1).getChildByFieldName("body").getChild(1);
        printer = new SyntaxTreePrinter(method);
        actual = printer.printSubtree();
        expected =
                "method_declaration [3:4] - [6:5]\n" +
                "  modifiers [3:4] - [3:17]\n" +
                "  type: void_type [3:18] - [3:22]\n" +
                "  name: identifier [3:23] - [3:27]\n" +
                "  parameters: formal_parameters [3:27] - [3:42]\n" +
                "    formal_parameter [3:28] - [3:41]\n" +
                "      type: array_type [3:28] - [3:36]\n" +
                "        element: type_identifier [3:28] - [3:34]\n" +
                "        dimensions: dimensions [3:34] - [3:36]\n" +
                "      name: identifier [3:37] - [3:41]\n" +
                "  body: block [3:43] - [6:5]\n" +
                "    line_comment [4:8] - [4:22]\n" +
                "    expression_statement [5:8] - [5:44]\n" +
                "      method_invocation [5:8] - [5:43]\n" +
                "        object: field_access [5:8] - [5:18]\n" +
                "          object: identifier [5:8] - [5:14]\n" +
                "          field: identifier [5:15] - [5:18]\n" +
                "        name: identifier [5:19] - [5:26]\n" +
                "        arguments: argument_list [5:26] - [5:43]\n" +
                "          string_literal [5:27] - [5:42]\n";
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @SneakyThrows(UnsupportedEncodingException.class)
    void testPrintSubtreeWithOffset() {
        SyntaxTreePrinter printer;
        @Cleanup Parser parser = new Parser(Language.JAVA);
        @Cleanup Tree tree = parser.parseString(
                "package ch.usi.si;\n" +
                "\n" +
                "public class Main {\n" +
                "    public static void main(String[] args) {\n" +
                "        //line comment\n" +
                "        System.out.println(\"Hello, World!\");\n" +
                "    }\n" +
                "}"
        );
        Node root = tree.getRootNode();
        Node method = root.getChild(1).getChildByFieldName("body").getChild(1);
        printer = new SyntaxTreePrinter(method, new Point(-1, -2));
        String actual = printer.printSubtree();
        String expected =
                "method_declaration [2:2] - [5:3]\n" +
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
        Assertions.assertEquals(expected, actual);
    }
}
