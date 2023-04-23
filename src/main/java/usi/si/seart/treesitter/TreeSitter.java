package usi.si.seart.treesitter;

import lombok.experimental.UtilityClass;
import usi.si.seart.treesitter.exception.query.QueryException;

@UtilityClass
class TreeSitter {

    static native void parserDelete(long parser);

    static native void queryDelete(long query);

    static native long queryNew(long language, String source) throws QueryException;

    static native String queryCaptureName(long pointer, int index);

    static native int queryStringCount(long query);

    static native int queryCaptureCount(long query);

    static native int queryPatternCount(long query);

    static native void queryCursorDelete(long query_cursor);

    static native long queryCursorNew();

    static native void queryCursorExec(long query_cursor, long query, Node node);

    static native QueryMatch queryCursorNextMatch(long query_cursor);

    static native long treeCursorNew(Node node);

    static native TreeCursorNode treeCursorCurrentTreeCursorNode(long cursor);

    static native String treeCursorCurrentFieldName(long cursor);

    static native Node treeCursorCurrentNode(long cursor);

    static native void treeCursorDelete(long cursor);

    static native boolean treeCursorGotoFirstChild(long cursor);

    static native boolean treeCursorGotoNextSibling(long cursor);

    static native boolean treeCursorGotoParent(long cursor);

    static native void treeDelete(long tree);

    static native void treeEdit(long tree, InputEdit edit);

    static native Node treeRootNode(long tree);
}
