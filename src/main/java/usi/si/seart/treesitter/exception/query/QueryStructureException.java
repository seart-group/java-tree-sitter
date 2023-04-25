package usi.si.seart.treesitter.exception.query;

import lombok.experimental.StandardException;

@SuppressWarnings("unused")
@StandardException
public class QueryStructureException extends QueryException {

    public QueryStructureException(int offset) {
        super("Bad pattern structure at offset " + offset);
    }
}
