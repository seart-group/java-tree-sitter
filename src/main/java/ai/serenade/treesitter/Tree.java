package ai.serenade.treesitter;

/**
 * A Tree represents the syntax tree of an entire source code file. It contains {@link ai.serenade.treesitter.Node}
 * instances that indicate the structure of the source code.
 */
public class Tree implements AutoCloseable {
  private long pointer;

  Tree(long pointer) {
    this.pointer = pointer;
  }

  /**
   * Delete the syntax tree, freeing all the memory that it used.
   */
  @Override
  public void close() {
    TreeSitter.treeDelete(pointer);
  }

  /**
   * @return The root node of the syntax tree.
   */
  public Node getRootNode() {
    return TreeSitter.treeRootNode(pointer);
  }
}
