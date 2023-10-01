package ch.usi.si.seart.treesitter.exception;

import ch.usi.si.seart.treesitter.Point;

/**
 * Thrown to indicate that a specified point is outside a node's point range.
 *
 * @since 1.7.0
 * @author Ozren Dabić
 */
public class PointOutOfBoundsException extends NodeRangeBoundaryException {

    @SuppressWarnings("unused")
    public PointOutOfBoundsException(Point point) {
        super("Point outside node range: " + point);
    }
}
