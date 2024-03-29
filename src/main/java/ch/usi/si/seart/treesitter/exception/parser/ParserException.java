package ch.usi.si.seart.treesitter.exception.parser;

import ch.usi.si.seart.treesitter.exception.TreeSitterException;
import lombok.AccessLevel;
import lombok.experimental.StandardException;

/**
 * The base exception type for all exceptions related to tree-sitter parsers.
 *
 * @since 1.6.0
 * @author Ozren Dabić
 */
@StandardException(access = AccessLevel.PROTECTED)
public abstract class ParserException extends TreeSitterException {
}
