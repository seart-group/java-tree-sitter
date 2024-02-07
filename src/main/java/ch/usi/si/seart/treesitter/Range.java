package ch.usi.si.seart.treesitter;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Generated;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

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
@AllArgsConstructor
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Range {

    int startByte;
    int endByte;
    Point startPoint;
    Point endPoint;

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
