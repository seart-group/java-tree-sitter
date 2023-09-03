package ch.usi.si.seart.treesitter;

import lombok.AccessLevel;
import lombok.Generated;
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

    /**
     * Delete the tree, freeing all the memory that it used.
     */
    @Override
    public native void close();

    /**
     * Edit the syntax tree to keep it in sync with source code that has been edited.
     *
     * @param edit Changes made to the source code in terms of <em>both</em> byte offsets and row/column coordinates
     */
    public native void edit(InputEdit edit);

    /**
     * @return The root node of the syntax tree
     */
    public native Node getRootNode();

    /**
     * @return An iterator over the entire syntax tree, starting from the root node
     */
    @Override
    public Iterator<Node> iterator() {
        return getRootNode().iterator();
    }

    @Override
    @Generated
    public String toString() {
        return String.format("Tree(id: %d, language: %s)", pointer, language);
    }
}
