package usi.si.seart.treesitter;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.Iterator;

/**
 * A Tree represents the syntax tree of an entire source code file. It contains {@link Node Node}
 * instances that indicate the structure of the source code.
 */
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Tree extends External implements Iterable<Node> {

  Language language;

  Tree(long pointer, Language language) {
    super(pointer);
    this.language = language;
  }

  @Override
  protected void free(long pointer) {
    TreeSitter.treeDelete(pointer);
  }

  /**
   * Edit the syntax tree to keep it in sync with source code that has been edited.
   *
   * @param edit Changes made to the source code in terms of <em>both</em> byte offsets and row/column coordinates.
   */
  public void edit(InputEdit edit) {
      TreeSitter.treeEdit(pointer, edit);
  }

  /**
   * @return The root node of the syntax tree.
   */
  public Node getRootNode() {
    return TreeSitter.treeRootNode(pointer);
  }

  /**
   * @return An iterator over the entire syntax tree, starting from the root node.
   */
  @Override
  public Iterator<Node> iterator() {
    return getRootNode().iterator();
  }

  @Override
  public String toString() {
    return "Tree(id: "+pointer+", language: "+language+")";
  }
}
