package usi.si.seart.treesitter;

import lombok.experimental.UtilityClass;

@UtilityClass
class TreeSitter {

    static native void parserDelete(long parser);

    static native void queryDelete(long query);

    static native void queryCursorDelete(long query_cursor);

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
