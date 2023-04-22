package usi.si.seart.treesitter;

import lombok.experimental.UtilityClass;
import usi.si.seart.treesitter.exception.query.QueryException;

@UtilityClass
class TreeSitter {

    static native Node nodeChild(Node node, int child);

    static native Node nodeChildByFieldName(Node node, String name);

    static native int nodeChildCount(Node node);

    static native Node nodeDescendantForByteRange(Node node, int startByte, int endByte);

    static native int nodeEndByte(Node node);

    static native Point nodeEndPoint(Node node);

    static native boolean nodeEq(Node node, Node other);

    static native String nodeFieldNameForChild(Node node, int child);

    static native Node nodeFirstChildForByte(Node node, int offset);

    static native Node nodeFirstNamedChildForByte(Node node, int offset);

    static native boolean nodeHasError(Node node);

    static native boolean nodeIsExtra(Node node);

    static native boolean nodeIsMissing(Node node);

    static native boolean nodeIsNamed(Node node);

    static native boolean nodeIsNull(Node node);

    static native Node nodeParent(Node node);

    static native Node nodePrevNamedSibling(Node node);

    static native Node nodePrevSibling(Node node);

    static native Node nodeNextNamedSibling(Node node);

    static native Node nodeNextSibling(Node node);

    static native int nodeStartByte(Node node);

    static native Point nodeStartPoint(Node node);

    static native String nodeString(Node node);

    static native String nodeType(Node node);

    static native long parserNew();

    static native void parserDelete(long parser);

    static native boolean parserSetLanguage(long parser, long language);

    static native long parserParseBytes(long parser, byte[] source, int length);

    static native long parserIncrementalParseBytes(long parser, long old_tree, byte[] source, int length);

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
