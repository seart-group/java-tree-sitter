package ch.usi.si.seart.treesitter.printer;

import java.io.File;
import java.io.IOException;

/**
 * Contract for classes that can generate string
 * representations of Abstract Syntax Trees (ASTs).
 * Implementations of this interface are responsible
 * for traversing trees and producing a specific representation.
 *
 * @since 1.2.0
 * @author Ozren Dabić
 */
public interface TreePrinter {

    /**
     * Generates a string representation of an Abstract Syntax Tree (AST).
     *
     * @return A string representation of the tree
     */
    String print();

    /**
     * Generates a file containing the string
     * representation of an Abstract Syntax Tree (AST).
     * This method should be preferred over {@link #print()}
     * when dealing with extremely wide, or deep trees.
     *
     * @return A file containing the string representation of the tree
     * @throws IOException if an I/O error occurs
     */
    File export() throws IOException;
}
