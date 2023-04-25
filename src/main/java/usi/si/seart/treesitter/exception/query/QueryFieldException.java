package usi.si.seart.treesitter.exception.query;

import lombok.experimental.StandardException;

@SuppressWarnings("unused")
@StandardException
public class QueryFieldException extends QueryException {

    public QueryFieldException(int offset) {
        super("Bad field name at offset " + offset);
    }
}
