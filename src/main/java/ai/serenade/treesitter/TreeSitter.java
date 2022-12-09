package ai.serenade.treesitter;

public class TreeSitter {

  private TreeSitter() {}

  public static native Node nodeChild(Node node, int child);

  public static native int nodeChildCount(Node node);

  public static native int nodeEndByte(Node node);

  public static native Point nodeEndPoint(Node node);

  public static native boolean nodeHasError(Node node);

  public static native boolean nodeIsExtra(Node node);

  public static native boolean nodeIsMissing(Node node);

  public static native boolean nodeIsNamed(Node node);

  public static native int nodeStartByte(Node node);

  public static native Point nodeStartPoint(Node node);

  public static native String nodeString(Node node);

  public static native String nodeType(Node node);

  public static native long parserNew();

  public static native void parserDelete(long parser);

  public static native void parserSetLanguage(long parser, long language);

  public static native long parserParseBytes(long parser, byte[] source, int length);

  public static native void queryDelete(long query);

  public static native long queryNew(long language, String source);

  public static native void queryCursorDelete(long query_cursor);

  public static native long queryCursorNew();

  public static native void queryCursorExec(long query_cursor, long query, Node node);

  public static native QueryMatch queryCursorNextMatch(long query_cursor);

  public static native long treeCursorNew(Node node);

  public static native TreeCursorNode treeCursorCurrentTreeCursorNode(long cursor);

  public static native String treeCursorCurrentFieldName(long cursor);

  public static native Node treeCursorCurrentNode(long cursor);

  public static native void treeCursorDelete(long cursor);

  public static native boolean treeCursorGotoFirstChild(long cursor);

  public static native boolean treeCursorGotoNextSibling(long cursor);

  public static native boolean treeCursorGotoParent(long cursor);

  public static native void treeDelete(long tree);

  public static native Node treeRootNode(long tree);
}
