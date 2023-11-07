package ch.usi.si.seart.treesitter;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * A tree cursor is a stateful object that allows
 * you to walk a syntax tree with maximum efficiency.
 * It allows you to walk a syntax tree more efficiently
 * than is possible using the {@link Node} traversal functions.
 * It is always on a certain syntax node,
 * and can be moved imperatively to different nodes.
 *
 * @since 1.0.0
 * @author Tommy MacWilliam
 * @author Ozren Dabić
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TreeCursor extends External implements Cloneable {

    int context0;
    int context1;

    long id;

    Tree tree;

    /*
     * This is a workaround intended for OffsetTreeCursor.
     * Should not be used under any other circumstances!
     */
    protected TreeCursor() {
        super();
        this.context0 = 0;
        this.context1 = 0;
        this.id = 0L;
        this.tree = null;
    }

    @SuppressWarnings("unused")
    TreeCursor(long pointer, int context0, int context1, long id, @NotNull Tree tree) {
        super(pointer);
        this.context0 = context0;
        this.context1 = context1;
        this.id = id;
        this.tree = tree;
    }

    @Override
    protected native void delete();

    /**
     * Get the {@link Node} that the cursor is currently pointing to.
     *
     * @return the tree cursor's current node
     */
    public native Node getCurrentNode();

    /**
     * Get the field name of the {@link Node} that the cursor is currently pointing to.
     *
     * @return the field name of the tree cursor's current node,
     * {@code null} if the current node doesn't have a field
     */
    public native String getCurrentFieldName();

    /**
     * Get the {@link TreeCursorNode} representation of the
     * {@link Node} that the cursor is currently pointing to.
     *
     * @return the tree cursor's current node
     */
    public native TreeCursorNode getCurrentTreeCursorNode();

    /**
     * Move the cursor to the first child of its current node.
     *
     * @return true if the cursor successfully moved,
     * and false if there were no children
     */
    public native boolean gotoFirstChild();

    /**
     * Move the cursor to the first child of its current node
     * that extends beyond the given byte offset.
     *
     * @param offset the starting byte of the child
     * @return true if the cursor successfully moved,
     * and false if no such child was found
     * @throws IllegalArgumentException if {@code offset} is negative
     * @throws ch.usi.si.seart.treesitter.exception.ByteOffsetOutOfBoundsException
     * if {@code offset} is outside the current node's byte range
     * @since 1.7.0
     */
    public native boolean gotoFirstChild(int offset);

    /**
     * Move the cursor to the first child of its current node
     * that extends beyond the given row-column offset.
     *
     * @param point the starting row-column position of the child
     * @return true if the cursor successfully moved,
     * and false if no such child was found
     * @throws NullPointerException if {@code point} is null
     * @throws ch.usi.si.seart.treesitter.exception.PointOutOfBoundsException
     * if {@code point} is outside the current node's positional span
     * @since 1.7.0
     */
    public native boolean gotoFirstChild(@NotNull Point point);

    /**
     * Move the cursor to the next sibling of its current node.
     *
     * @return true if the cursor successfully moved,
     * and false if there was no next sibling node
     */
    public native boolean gotoNextSibling();

    /**
     * Move the cursor to the parent of its current node.
     *
     * @return true if the cursor successfully moved,
     * and false if there was no parent node
     * (the cursor was already on the root node)
     */
    public native boolean gotoParent();

    /**
     * Iteratively traverse over the parse tree,
     * applying a callback to the nodes before they are visited.
     *
     * @param callback The callback consumer which will execute upon visiting a node
     * @throws NullPointerException if {@code callback} is null
     */
    public void preorderTraversal(@NotNull Consumer<Node> callback) {
        Objects.requireNonNull(callback, "Callback must not be null!");
        for (;;) {
            callback.accept(getCurrentNode());
            if (gotoFirstChild() || gotoNextSibling()) continue;
            do {
                if (!gotoParent()) return;
            } while (!gotoNextSibling());
        }
    }

    /**
     * Clone this cursor, creating a separate, independent instance.
     *
     * @return a clone of this instance
     * @since 1.5.1
     */
    @Override
    public native TreeCursor clone();
}
