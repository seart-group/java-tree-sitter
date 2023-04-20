package usi.si.seart.treesitter.exception.query;

import lombok.experimental.StandardException;
import usi.si.seart.treesitter.exception.TreeSitterException;

/**
 * The base exception type for all exceptions related to tree-sitter queries.
 */
@StandardException
public abstract class QueryException extends TreeSitterException {
}
