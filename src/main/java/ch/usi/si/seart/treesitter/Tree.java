package ch.usi.si.seart.treesitter;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * A Tree represents the syntax tree of an entire source code file.
 * It contains {@link Node} instances that indicate the structure of the source code.
 *
 * @since 1.0.0
 * @author Tommy MacWilliam
 * @author Ozren Dabić
 */
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Tree extends External implements Iterable<Node>, Cloneable {

    private static final Charset CHARSET = StandardCharsets.UTF_16LE;

    Language language;
    String source;

    Tree(long pointer, @NotNull Language language, @NotNull String source) {
        super(pointer);
        this.language = language;
        this.source = source;
    }

    @Override
    protected native void delete();

    /**
     * Edit the syntax tree to keep it in sync with source code that has been edited.
     *
     * @param edit changes made to the source code in terms of
     * <strong>both</strong> byte offsets and row/column coordinates
     */
    public native void edit(@NotNull InputEdit edit);

    /**
     * Compare this old edited syntax tree to a new syntax tree representing the same document,
     * returning a sequence of {@link Range} instances, their coordinates corresponding to changes
     * made to the syntactic structure.
     * <p>
     * For this to work correctly, this syntax tree must have been edited such that its
     * ranges match up to the new tree. Generally, you'll want to call this method right
     * after calling one of the {@link Parser} methods.
     *
     * @param other the tree to compare with
     * @return a list of ranges that have been changed
     * @throws NullPointerException if {@code other} is null
     * @since 1.12.0
     * @see Parser#parse(String, Tree)
     * @see Parser#parse(java.nio.file.Path, Tree) Parser.parse(Path, Tree)
     */
    public native List<Range> getChangedRanges(@NotNull Tree other);

    /**
     * Get the topmost {@link Node} of the syntax tree.
     *
     * @return the root node of the syntax tree
     */
    public native Node getRootNode();

    /**
     * Returns an iterator over the entire syntax tree, starting from the root.
     *
     * @return an iterator over syntax tree nodes
     * @see Node#iterator()
     */
    @Override
    public @NotNull Iterator<Node> iterator() {
        return getRootNode().iterator();
    }

    /**
     * Clone this tree, creating a separate, independent instance.
     *
     * @return a clone of this instance
     * @since 1.6.0
     */
    @Override
    public native Tree clone();

    String getSource(int startByte, int endByte) {
        byte[] bytes = source.getBytes(CHARSET);
        byte[] copy = Arrays.copyOfRange(bytes, startByte * 2, endByte * 2);
        return new String(copy, CHARSET);
    }
}
