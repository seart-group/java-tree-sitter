package ch.usi.si.seart.treesitter;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

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
public class Point implements Comparable<Point> {

    private static final Point ORIGIN = new Point(0, 0);

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
        return equals(ORIGIN);
    }

    /**
     * Compares this point with another point for positional order.
     * Points are compared by their row coordinates first,
     * and if those are equal they are then compared by their column coordinates.
     *
     * @param other the object to be compared
     * @return the row comparison result, or the column comparison result if the rows are equal
     * @since 1.5.1
     */
    @Override
    public int compareTo(@NotNull Point other) {
        int comparison = Integer.compare(this.row, other.row);
        return comparison != 0 ? comparison : Integer.compare(this.column, other.column);
    }
}
