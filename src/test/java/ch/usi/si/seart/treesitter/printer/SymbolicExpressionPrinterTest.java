package ch.usi.si.seart.treesitter.printer;

import ch.usi.si.seart.treesitter.TreeCursor;

class SymbolicExpressionPrinterTest extends PrinterTestBase {

    @Override
    protected String getExpected() {
        return
                "(program " +
                    "(package_declaration " +
                        "(scoped_identifier " +
                            "scope: (scoped_identifier " +
                                "scope: (identifier) " +
                                "name: (identifier)" +
                            ") " +
                            "name: (identifier)" +
                        ")" +
                    ") " +
                    "(class_declaration " +
                        "(modifiers) " +
                        "name: (identifier) " +
                        "body: (class_body " +
                            "(method_declaration " +
                                "(modifiers) " +
                                "type: (void_type) " +
                                "name: (identifier) " +
                                "parameters: (formal_parameters " +
                                    "(formal_parameter " +
                                        "type: (array_type " +
                                            "element: (type_identifier) " +
                                            "dimensions: (dimensions)" +
                                        ") " +
                                        "name: (identifier)" +
                                    ")" +
                                ") " +
                                "body: (block " +
                                    "(line_comment) " +
                                    "(expression_statement " +
                                        "(method_invocation " +
                                            "object: (field_access " +
                                                "object: (identifier) " +
                                                "field: (identifier)" +
                                            ") " +
                                            "name: (identifier) " +
                                            "arguments: (argument_list " +
                                                "(string_literal " +
                                                    "(string_fragment)" +
                                                ")" +
                                            ")" +
                                        ")" +
                                    ")" +
                                ")" +
                            ")" +
                        ")" +
                    ")" +
                ")";
    }

    @Override
    protected TreePrinter getPrinter(TreeCursor cursor) {
        return new SymbolicExpressionPrinter(cursor);
    }
}
