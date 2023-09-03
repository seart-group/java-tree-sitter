package ch.usi.si.seart.treesitter.exception.query;

import lombok.experimental.StandardException;

/**
 * Thrown when the targeted node does not have a field with the specified name.
 *
 * @since 1.0.0
 * @author Ozren Dabić
 */
@SuppressWarnings("unused")
@StandardException
public class QueryFieldException extends QueryException {

    public QueryFieldException(int offset) {
        super("Bad field name at offset " + offset);
    }
}
