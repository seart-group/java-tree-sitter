package usi.si.seart.treesitter;

import java.util.function.Consumer;

/**
 *  A tree cursor is a stateful object that allows you to walk a syntax tree with maximum efficiency.
 */
public class TreeCursor extends External {

  private int context0;
  private int context1;
  private long id;
  private long tree;

  TreeCursor(long pointer) {
    super(pointer);
  }

  @Override
  protected void free(long pointer) {
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

  /**
   * Iteratively traverse over the parse tree, applying a callback to the nodes before they are visited.
   *
   * @param callback The callback consumer which will execute upon visiting a node.
   */
  public void preorderTraversal(Consumer<Node> callback) {
    for (;;) {
      callback.accept(this.getCurrentNode());
      if (this.gotoFirstChild() || this.gotoNextSibling())
        continue;
      do {
        if (!this.gotoParent())
          return;
      } while (!this.gotoNextSibling());
    }
  }

  @Override
  public String toString() {
    return "TreeCursor(id: "+id+", tree: "+tree+")";
  }
}
