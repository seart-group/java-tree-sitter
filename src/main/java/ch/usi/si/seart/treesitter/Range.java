package ch.usi.si.seart.treesitter;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Represents the portions of source code taken up by a node within a file or snippet.
 * Each range consists of a:
 * <ul>
 *     <li>start byte offset</li>
 *     <li>end byte offset</li>
 *     <li>start point</li>
 *     <li>end point</li>
 * </ul>
 *
 * @since 1.0.0
 * @author Ozren Dabić
 */
@Getter
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Range {

    int startByte;
    int endByte;
    Point startPoint;
    Point endPoint;

    /**
     * Obtain a new {@link Builder} instance for constructing a range.
     *
     * @return a new range builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Obtain a new {@link Builder} initialized with the current range's attributes.
     *
     * @return a new range builder
     * @since 1.12.0
     */
    public Builder toBuilder() {
        return builder().startByte(startByte)
                        .endByte(endByte)
                        .startPoint(startPoint)
                        .endPoint(endPoint);
    }

    /**
     * Facilitates the construction of {@link Range} instances.
     * It allows for the step-by-step creation of these objects
     * by providing methods for setting individual attributes.
     * Input validations are performed at each build step.
     *
     * @since 1.12.0
     */
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Builder {

        int startByte = 0;
        int endByte = Integer.MAX_VALUE;
        Point startPoint = Point.ORIGIN();
        Point endPoint = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);

        /**
         * Sets the start byte offset for this range.
         *
         * @param value byte value offset
         * @return this builder
         * @throws IllegalArgumentException if the value is negative
         */
        public Builder startByte(int value) {
            if (value < 0) throw new IllegalArgumentException("Start byte cannot be negative");
            startByte = value;
            return this;
        }

        /**
         * Sets the end byte offset for this range.
         *
         * @param value byte value offset
         * @return this builder
         * @throws IllegalArgumentException if the value is negative
         */
        public Builder endByte(int value) {
            if (value < 0) throw new IllegalArgumentException("End byte cannot be negative");
            endByte = value;
            return this;
        }

        /**
         * Sets the start point for this range.
         *
         * @param point the point
         * @return this builder
         * @throws NullPointerException if the point is {@code null}
         * @throws IllegalArgumentException if the point has negative coordinates
         */
        public Builder startPoint(@NotNull Point point) {
            Objects.requireNonNull(point, "Start point cannot be null");
            if (point.getRow() < 0 || point.getColumn() < 0)
                throw new IllegalArgumentException("Start point cannot have negative coordinates");
            startPoint = point;
            return this;
        }

        /**
         * Sets the end point for this range.
         *
         * @param point the point
         * @return this builder
         * @throws NullPointerException if the point is {@code null}
         * @throws IllegalArgumentException if the point has negative coordinates
         */
        public Builder endPoint(@NotNull Point point) {
            Objects.requireNonNull(point, "End point cannot be null");
            if (point.getRow() < 0 || point.getColumn() < 0)
                throw new IllegalArgumentException("End point cannot have negative coordinates");
            endPoint = point;
            return this;
        }

        /**
         * Builds a new range instance with the
         * attributes specified in this builder.
         *
         * @return a new range instance
         * @throws IllegalArgumentException
         * if the start byte is greater than the end byte,
         * or if the start point is greater than the end point
         * @see Point#compareTo(Point)
         */
        public Range build() {
            if (Integer.compareUnsigned(startByte, endByte) > 0)
                throw new IllegalArgumentException("Start byte cannot be greater than end byte");
            if (startPoint.compareTo(endPoint) > 0)
                throw new IllegalArgumentException("Start point cannot be greater than end point");
            return new Range(startByte, endByte, startPoint, endPoint);
        }
    }

    Range(@NotNull Node node) {
        this(node.getStartByte(), node.getEndByte(), node.getStartPoint(), node.getEndPoint());
    }

    /**
     * Returns a string representation of this range in the format:
     * <pre>{@code
     *      [startPoint] - [endPoint]
     * }</pre>
     *
     * @return A string representation of this range
     */
    @Override
    @Generated
    public String toString() {
        return String.format("[%s] - [%s]", startPoint, endPoint);
    }
}
