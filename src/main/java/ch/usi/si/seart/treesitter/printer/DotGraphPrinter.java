package ch.usi.si.seart.treesitter.printer;

import ch.usi.si.seart.treesitter.Tree;
import lombok.AllArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Printer used for generating DOT graph representations of trees.
 * Unlike its sister classes, it does not rely on an iterative approach,
 * relying instead on the internal {@code tree-sitter} API.
 */
@AllArgsConstructor
public class DotGraphPrinter implements TreePrinter {

    Tree tree;

    /**
     * Generates a DOT graph of the tree.
     *
     * @return A DOT graph string of the tree.
     */
    @Override
    public String print() {
        try {
            File file = export();
            Path path = file.toPath();
            String contents = Files.readString(path);
            Files.delete(path);
            return contents;
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    /**
     * Generates a DOT graph of the tree,
     * writing it directly to a file.
     *
     * @return A file containing the DOT graph of the tree.
     */
    @Override
    public File export() throws IOException {
        File file = Files.createTempFile("ts-export-", ".dot").toFile();
        write(file);
        return file;
    }

    native void write(File file) throws IOException;
}
