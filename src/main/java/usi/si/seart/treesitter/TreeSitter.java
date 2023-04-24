package usi.si.seart.treesitter;

import lombok.experimental.UtilityClass;

@UtilityClass
class TreeSitter {

    static native void parserDelete(long parser);

    static native void queryDelete(long query);

    static native void queryCursorDelete(long query_cursor);

    static native void treeCursorDelete(long cursor);

    static native void treeDelete(long tree);
}
