package ch.usi.si.seart.treesitter.exception.query;

import lombok.experimental.StandardException;

/**
 * Thrown when a query string has incorrect captures.
 */
@SuppressWarnings("unused")
@StandardException
public class QueryCaptureException extends QueryException {

    public QueryCaptureException(int offset) {
        super("Bad capture at offset " + offset);
    }
}
