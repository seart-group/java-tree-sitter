package ch.usi.si.seart.treesitter.exception.query;

import lombok.experimental.StandardException;

/**
 * Thrown when the node structure in the query does not adhere to the grammar.
 *
 * @since 1.0.0
 * @author Ozren Dabić
 */
@SuppressWarnings("unused")
@StandardException
public class QueryStructureException extends QueryException {

    public QueryStructureException(int offset) {
        super("Bad pattern structure at offset " + offset);
    }
}
