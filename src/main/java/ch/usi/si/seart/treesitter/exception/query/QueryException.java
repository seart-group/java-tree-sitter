package ch.usi.si.seart.treesitter.exception.query;

import ch.usi.si.seart.treesitter.exception.TreeSitterException;
import lombok.experimental.StandardException;

/**
 * The base exception type for all exceptions related to tree-sitter queries.
 *
 * @since 1.0.0
 * @author Ozren Dabić
 */
@StandardException
public abstract class QueryException extends TreeSitterException {
}
