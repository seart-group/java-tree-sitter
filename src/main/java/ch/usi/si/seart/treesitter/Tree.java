package ch.usi.si.seart.treesitter;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Iterator;

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
