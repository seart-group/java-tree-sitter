package ch.usi.si.seart.treesitter;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TreeCursor extends External implements Cloneable {

    int context0;
    int context1;

    long id;

    Tree tree;

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
     * Get the depth of the cursor's current {@link Node} relative
     * to the original {@link Node} that the cursor was constructed from.
     *
     * @return the relative depth of the tree cursor's current node
     * @since 1.11.0
     */
    public native int getCurrentDepth();

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
     * Move the cursor to the last child of its current node.
     * <p>
     * Note that this method may be slower than {@link #gotoFirstChild()}
     * because it needs to iterate through all the children to
     * compute the child's position.
     *
     * @return true if the cursor successfully moved,
     * and false if there were no children
     * @since 1.12.0
     */
    public native boolean gotoLastChild();

    /**
     * Move the cursor to the next sibling of its current node.
     *
     * @return true if the cursor successfully moved,
     * and false if there was no next sibling node
     */
    public native boolean gotoNextSibling();

    /**
     * Move the cursor to the previous sibling of its current node.
     * <p>
     * Note that this method may be slower than {@link #gotoNextSibling()}
     * due to how node positions are stored. In the worst case, this
     * method may need to iterate through all the previous nodes
     * up to the destination.
     *
     * @return true if the cursor successfully moved,
     * and false if there was no previous sibling node
     * @since 1.12.0
     */
    public native boolean gotoPrevSibling();

    /**
     * Move the cursor to the parent of its current node.
     *
     * @return true if the cursor successfully moved,
     * and false if there was no parent node
     * (the cursor was already on the root node)
     */
    public native boolean gotoParent();

    /**
     * Move the cursor to an arbitrary node.
     *
     * @param node target node to move the cursor to.
     * @return true if the cursor successfully moved,
     * and false if it was already at the specified node
     * @throws NullPointerException if {@code node} is null
     * @throws IllegalArgumentException if {@code node} is not present in the tree
     * @since 1.9.0
     */
    public native boolean gotoNode(@NotNull Node node);

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
     * Reset the cursor to the same state as another cursor.
     *
     * @param other the cursor to copy state from
     * @return true if the cursor successfully moved,
     * and false if it was already located at the same node
     * @throws NullPointerException if {@code other} is null
     * @throws IllegalArgumentException if the other cursor is not from the same tree
     * @since 1.11.0
     */
    public native boolean reset(@NotNull TreeCursor other);

    /**
     * Clone this cursor, creating a separate, independent instance.
     *
     * @return a clone of this instance
     * @since 1.5.1
     */
    @Override
    public native TreeCursor clone();

    static class Stub extends TreeCursor {

        @Override
        public int getCurrentDepth() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Node getCurrentNode() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getCurrentFieldName() {
            throw new UnsupportedOperationException();
        }

        @Override
        public TreeCursorNode getCurrentTreeCursorNode() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean gotoFirstChild() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean gotoFirstChild(int offset) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean gotoFirstChild(@NotNull Point point) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean gotoLastChild() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean gotoNextSibling() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean gotoPrevSibling() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean gotoParent() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean gotoNode(@NotNull Node node) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void preorderTraversal(@NotNull Consumer<Node> callback) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean reset(@NotNull TreeCursor other) {
            throw new UnsupportedOperationException();
        }

        @Override
        public TreeCursor clone() {
            throw new UnsupportedOperationException();
        }
    }
}
