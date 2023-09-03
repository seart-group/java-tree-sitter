package ch.usi.si.seart.treesitter;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

/**
 * Represents a two-dimensional point with row and column coordinates.
 * Points are an  alternative to byte ranges, and as such are used to
 * represent more human-friendly positions of tree nodes within source code.
 * Although node positions within files should never be negative,
 * instances of this class can be created with negative row and column
 * values for other purposes, such as denoting repositioning offsets.
 *
 * @since 1.0.0
 * @author Ozren Dabić
 */
@Getter
@Setter(value = AccessLevel.PACKAGE)
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class Point {

    int row;
    int column;

    /**
     * Returns a string representation of this point in the format:
     * <pre>{@code
     *      row:column
     * }</pre>
     *
     * @return A string representation of this point
     */
    @Override
    @Generated
    public String toString() {
        return row + ":" + column;
    }

    /**
     * Checks if this point represents the origin,
     * which is when both the row and the column are zero.
     * In byte range terms, this point also corresponds to zero.
     *
     * @return true if this is an origin point, false otherwise
     */
    public boolean isOrigin() {
        return row == 0 && column == 0;
    }
}
