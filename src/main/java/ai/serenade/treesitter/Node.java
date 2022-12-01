package ai.serenade.treesitter;

public class Node {
  private int context0;
  private int context1;
  private int context2;
  private int context3;
  private long id;
  private long tree;

  public Node() {}

  /**
   * Get the node's child at the given index, where zero represents the first child.
   *
   * @param child The zero-indexed child
   * @return The Node's child at the given index
   */
  public Node getChild(int child) {
    return TreeSitter.nodeChild(this, child);
  }

  /**
   * @return The node's number of children.
   */
  public int getChildCount() {
    return TreeSitter.nodeChildCount(this);
  }

  /**
   * @return The node's end byte.
   */
  public int getEndByte() {
    return TreeSitter.nodeEndByte(this);
  }

  /**
   * This string is allocated with malloc and the caller is responsible for freeing it using free.
   *
   * @return An S-expression representing the node as a string.
   */
  public String getNodeString() {
    return TreeSitter.nodeString(this);
  }

  /**
   * @return The node's start byte.
   */
  public int getStartByte() {
    return TreeSitter.nodeStartByte(this);
  }

  /**
   * @return The node's type as a null-terminated string
   */
  public String getType() {
    return TreeSitter.nodeType(this);
  }

  /**
   * A tree cursor allows you to walk a syntax tree more efficiently than is possible using the TSNode functions.
   * It is a mutable object that is always on a certain syntax node, and can be moved imperatively to different nodes.
   *
   * @return A new tree cursor starting from the given node.
   */
  public TreeCursor walk() {
    return new TreeCursor(TreeSitter.treeCursorNew(this));
  }
}
