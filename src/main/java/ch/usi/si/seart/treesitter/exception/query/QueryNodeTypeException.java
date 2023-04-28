package ch.usi.si.seart.treesitter.exception.query;

import lombok.experimental.StandardException;

@SuppressWarnings("unused")
@StandardException
public class QueryNodeTypeException extends QueryException {

    public QueryNodeTypeException(int offset) {
        super("Bad node name at offset " + offset);
    }
}
