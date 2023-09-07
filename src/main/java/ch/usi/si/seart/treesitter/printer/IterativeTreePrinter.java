package ch.usi.si.seart.treesitter.printer;

import ch.usi.si.seart.treesitter.TreeCursor;
import ch.usi.si.seart.treesitter.function.IOExceptionThrowingConsumer;
import lombok.AccessLevel;
import lombok.Cleanup;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.nio.file.Files;
import java.util.Objects;
import java.util.function.Consumer;

@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
abstract class IterativeTreePrinter implements TreePrinter {

    TreeCursor cursor;

    protected IterativeTreePrinter(@NotNull TreeCursor cursor) {
        this.cursor = Objects.requireNonNull(cursor, "Cursor must not be null!");
    }

    /**
     * Generates a string representation of the Abstract Syntax Tree (AST),
     * starting from the node currently pointed to by the cursor.
     *
     * @return A string representation of the tree
     */
    public final String print() {
        StringBuilder stringBuilder = new StringBuilder(getPreamble());
        write(stringBuilder::append);
        return stringBuilder.toString();
    }

    /**
     * Generates a string representation of the Abstract Syntax Tree (AST),
     * starting from the node currently pointed to by the cursor,
     * while writing outputs directly to a file.
     *
     * @return A file containing the string of the tree
     * @throws IOException if an I/O error occurs
     */
    public final File export() throws IOException {
        File file = Files.createTempFile("ts-export-", getFileExtension()).toFile();
        @Cleanup Writer writer = new BufferedWriter(new FileWriter(file));
        writer.append(getPreamble());
        Consumer<String> appender = IOExceptionThrowingConsumer.toUnchecked(writer::append);
        try {
            write(appender);
        } catch (UncheckedIOException ex) {
            throw ex.getCause();
        }
        return file;
    }

    protected String getPreamble() {
        return "";
    }

    protected String getFileExtension() {
        return "";
    }

    protected abstract void write(Consumer<String> appender);
}
