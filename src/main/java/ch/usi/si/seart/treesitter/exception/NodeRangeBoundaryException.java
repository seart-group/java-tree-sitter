package ch.usi.si.seart.treesitter.exception;

import lombok.AccessLevel;
import lombok.experimental.StandardException;

/**
 * The base exception type for all exceptions involving invalid node positional offsets.
 *
 * @since 1.7.0
 * @author Ozren Dabić
 */
@StandardException(access = AccessLevel.PROTECTED)
abstract class NodeRangeBoundaryException extends TreeSitterException {
}
