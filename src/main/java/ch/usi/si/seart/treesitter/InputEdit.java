package ch.usi.si.seart.treesitter;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

/**
 * Represents an edit operation on a section of source code.
 * Contains information pertaining to the starting byte offset and position,
 * as well as the former and current end byte offsets and positions.
 */
@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InputEdit {

    int startByte;
    int oldEndByte;
    int newEndByte;
    Point startPoint;
    Point oldEndPoint;
    Point newEndPoint;
}
