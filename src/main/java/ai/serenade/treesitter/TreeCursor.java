package ai.serenade.treesitter;

public class TreeCursor implements AutoCloseable {
  private long pointer;
  private int context0;
  private int context1;
  private long id;
  private long tree;

  TreeCursor(long pointer) {
    this.pointer = pointer;
  }

  /**
   * Delete the tree cursor, freeing all the memory that it used.
   */
  @Override
  public void close() {
    TreeSitter.treeCursorDelete(pointer);
  }

  /**
   * @return The tree cursor's current node.
   */
  public Node getCurrentNode() {
    return TreeSitter.treeCursorCurrentNode(pointer);
  }

  /**
   * @return The field name of the tree cursor's current node.
   * Will return null if the current node doesn't have a field.
   */
  public String getCurrentFieldName() {
    return TreeSitter.treeCursorCurrentFieldName(pointer);
  }

  /**
   * @return The tree cursor's current node.
   */
  public TreeCursorNode getCurrentTreeCursorNode() {
    return TreeSitter.treeCursorCurrentTreeCursorNode(pointer);
  }

  /**
   * Move the cursor to the first child of its current node.
   *
   * @return true if the cursor successfully moved, and false if there were no children.
   */
  public boolean gotoFirstChild() {
    return TreeSitter.treeCursorGotoFirstChild(pointer);
  }

  /**
   * Move the cursor to the next sibling of its current node.
   *
   * @return true if the cursor successfully moved, and false if there was no next sibling node.
   */
  public boolean gotoNextSibling() {
    return TreeSitter.treeCursorGotoNextSibling(pointer);
  }

  /**
   * Move the cursor to the parent of its current node.
   *
   * @return true if the cursor successfully moved, and false if there was no parent node
   * (the cursor was already on the root node).
   */
  public boolean gotoParent() {
    return TreeSitter.treeCursorGotoParent(pointer);
  }
}
