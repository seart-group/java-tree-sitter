package ch.usi.si.seart.treesitter;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Generated;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

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
@AllArgsConstructor
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Point implements Comparable<Point> {

    @Getter(lazy = true)
    @Accessors(fluent = true, makeFinal = true)
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
        return equals(ORIGIN());
    }

    /**
     * Compares this point with another point for positional order.
     * Points are compared by their row coordinates first,
     * and if those are equal they are then compared by their column coordinates.
     *
     * @param other the object to be compared
     * @return the row comparison result, or the column comparison result if the rows are equal
     * @throws NullPointerException if the other point is null
     * @since 1.5.1
     */
    @Override
    public native int compareTo(@NotNull Point other);

    /**
     * Adds another point to this point,
     * resulting in a new point with coordinates
     * equal to the sum of the coordinates
     * of this point and the other point.
     *
     * @param other The point to be added to this point
     * @return A new point representing the sum of this point and the other point
     * @throws NullPointerException if {@code other} is null
     * @since 1.5.1
     */
    public Point add(@NotNull Point other) {
        Objects.requireNonNull(other, "Other point must not be null!");
        if (isOrigin()) return other;
        if (other.isOrigin()) return this;
        if (equals(other)) return multiply(2);
        return new Point(row + other.row, column + other.column);
    }

    /**
     * Subtracts another point from this point,
     * resulting in a new point with coordinates
     * equal to the difference between the coordinates
     * of this point and the other point.
     *
     * @param other The point to be subtracted from this point
     * @return A new point representing the difference between this point and the other point
     * @throws NullPointerException if {@code other} is null
     * @since 1.5.1
     */
    public Point subtract(@NotNull Point other) {
        Objects.requireNonNull(other, "Other point must not be null!");
        if (isOrigin()) return other.multiply(-1);
        if (other.isOrigin()) return this;
        if (equals(other)) return ORIGIN();
        return new Point(row - other.row, column - other.column);
    }

    /**
     * Multiplies the coordinates of this point by a scalar value,
     * resulting in a new point with scaled coordinates.
     *
     * @param value The scalar value by which to multiply the coordinates of this point
     * @return A new point representing the scaled coordinates
     * @since 1.5.1
     */
    public Point multiply(int value) {
        if (value == 0) return ORIGIN();
        if (value == 1) return this;
        return new Point(row * value, column * value);
    }
}
