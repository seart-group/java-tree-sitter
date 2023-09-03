package ch.usi.si.seart.treesitter.printer;

import ch.usi.si.seart.treesitter.TreeCursor;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.Objects;

@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
abstract class IterativeTreePrinter implements TreePrinter {

    TreeCursor cursor;

    protected IterativeTreePrinter(TreeCursor cursor) {
        this.cursor = Objects.requireNonNull(cursor, "Cursor must not be null!");
    }
}
