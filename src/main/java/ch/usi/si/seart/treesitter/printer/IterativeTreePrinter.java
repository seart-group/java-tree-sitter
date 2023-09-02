package ch.usi.si.seart.treesitter.printer;

import ch.usi.si.seart.treesitter.TreeCursor;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
abstract class IterativeTreePrinter implements TreePrinter {

    TreeCursor cursor;
}
