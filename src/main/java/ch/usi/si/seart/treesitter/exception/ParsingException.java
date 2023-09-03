package ch.usi.si.seart.treesitter.exception;

import lombok.experimental.StandardException;

/**
 * Thrown when there is an error during parsing.
 *
 * @since 1.1.0
 * @author Ozren Dabić
 */
@StandardException
public class ParsingException extends TreeSitterException {
}
