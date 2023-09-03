package ch.usi.si.seart.treesitter.exception.query;

import lombok.experimental.StandardException;

/**
 * Thrown when a query has incorrect syntax.
 */
@SuppressWarnings("unused")
@StandardException
public class QuerySyntaxException extends QueryException {

    public QuerySyntaxException(int offset) {
        super("Bad syntax at offset " + offset);
    }
}
