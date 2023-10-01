package ch.usi.si.seart.treesitter.exception;

/**
 * Thrown to indicate that a specified byte offset is outside a node's byte range.
 *
 * @since 1.7.0
 * @author Ozren Dabić
 */
public class ByteOffsetOutOfBoundsException extends NodeRangeBoundaryException {

    public ByteOffsetOutOfBoundsException(int offset) {
        super("Byte offset outside node range: " + offset);
    }
}
