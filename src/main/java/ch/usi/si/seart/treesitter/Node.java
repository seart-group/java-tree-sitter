package ch.usi.si.seart.treesitter;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * A single node within a syntax {@link Tree}.
 * It tracks its start and end positions in the source code,
 * as well as its relation to other nodes like its parent,
 * siblings and children.
 *
 * @since 1.0.0
 * @author Tommy MacWilliam
 * @author Ozren Dabić
 */
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Node implements Iterable<Node> {

    int context0;
    int context1;
    int context2;
    int context3;

    long id;

    Tree tree;

    static class Null extends Node {
    }

    /**
     * Get the child {@code Node} at the given index.
     *
     * @param child the zero-indexed child position
     * @return the child
     * @throws IndexOutOfBoundsException
     * if the index is a negative number or if it is
     * greater or equal to the total number of children
     */
    public Node getChild(int child) {
        return getChild(child, false);
    }

    private native Node getChild(int child, boolean named);

    /**
     * Get the child {@code Node} residing in a given named field.
     *
     * @param name the child field name
     * @return the node's child with the given field name
     * @throws NullPointerException if {@code name} is {@code null}
     */
    public native Node getChildByFieldName(@NotNull String name);

    /**
     * Get the number of children associated with this node.
     *
     * @return the count of this node's children
     */
    public int getChildCount() {
        return getChildCount(false);
    }

    private native int getChildCount(boolean named);

    /**
     * Get an ordered list of this node's children.
     *
     * @return this node's children
     */
    public List<Node> getChildren() {
        Node[] children = Node.getChildren(this, false);
        return List.of(children);
    }

    private static native Node[] getChildren(Node node, boolean named);

    /**
     * Get the source code content encapsulated by this node.
     *
     * @return the node's source code
     * @since 1.5.0
     */
    public String getContent() {
        return !isNull() ? tree.getSource(getStartByte(), getEndByte()) : null;
    }

    /**
     * Get the smallest node within this node that spans the given range of bytes.
     *
     * @param startByte the start byte of the range
     * @param endByte the end byte of the range
     * @return a descendant node
     * @throws ch.usi.si.seart.treesitter.exception.ByteOffsetOutOfBoundsException
     * if either argument is outside of this node's byte range
     * @throws IllegalArgumentException if:
     * <ul>
     *     <li>{@code startByte} &lt; 0</li>
     *     <li>{@code endByte} &lt; 0</li>
     *     <li>{@code startByte} &gt; {@code endByte}</li>
     * </ul>
     * @since 1.6.0
     */
    public Node getDescendant(int startByte, int endByte) {
        return getDescendant(startByte, endByte, false);
    }

    private native Node getDescendant(int startByte, int endByte, boolean named);

    /**
     * Get the smallest node within this node that spans the given range of points.
     *
     * @param startPoint the start point of the range
     * @param endPoint the end point of the range
     * @return a descendant node
     * @throws NullPointerException if either argument is {@code null}
     * @throws IllegalArgumentException if any point coordinates are negative,
     * or if {@code startPoint} is a position that comes after {@code endPoint}
     * @throws ch.usi.si.seart.treesitter.exception.PointOutOfBoundsException
     * if any of the arguments is outside of this node's position range
     * @since 1.6.0
     */
    public Node getDescendant(@NotNull Point startPoint, @NotNull Point endPoint) {
        return getDescendant(startPoint, endPoint, false);
    }

    private native Node getDescendant(Point startPoint, Point endPoint, boolean named);

    /**
     * Get the byte offset where this node ends.
     *
     * @return the node's end byte
     */
    public native int getEndByte();

    /**
     * Get the node's end position in terms of rows and columns.
     *
     * @return the node's end position
     */
    public native Point getEndPoint();

    /**
     * Get the field name of a child {@code Node} residing at a given index.
     *
     * @return the child field name,
     * {@code null} if child does not reside in a field
     * @param child the zero-indexed child position
     * @throws IndexOutOfBoundsException
     * if the index is a negative number or if it is
     * greater or equal to the total number of children
     */
    public native String getFieldNameForChild(int child);

    /**
     * Get the first child {@code Node} that extends beyond the given byte offset.
     *
     * @param offset the offset in bytes
     * @return the child
     * @throws ch.usi.si.seart.treesitter.exception.ByteOffsetOutOfBoundsException
     * if the byte offset is outside the node's byte range
     */
    public Node getFirstChildForByte(int offset) {
        return getFirstChildForByte(offset, false);
    }

    private native Node getFirstChildForByte(int offset, boolean named);

    /**
     * Get the first named child {@code Node} that extends beyond the given byte offset.
     *
     * @param offset the offset in bytes
     * @return the child
     * @throws ch.usi.si.seart.treesitter.exception.ByteOffsetOutOfBoundsException
     * if the byte offset is outside the node's byte range
     */
    public Node getFirstNamedChildForByte(int offset) {
        return getFirstChildForByte(offset, true);
    }

    /**
     * Get the {@link Language} that was used to parse this node’s syntax tree.
     *
     * @return the node's language
     * @since 1.9.0
     */
    public Language getLanguage() {
        return !isNull() ? tree.getLanguage() : null;
    }

    /**
     * Get the <em>named</em> child {@code Node} at the given index.
     *
     * @param child the zero-indexed child position
     * @return the named child
     * @throws IndexOutOfBoundsException
     * if the index is a negative number or if it is
     * greater or equal to the total number of named children
     * @since 1.9.0
     */
    public Node getNamedChild(int child) {
        return getChild(child, true);
    }

    /**
     * Get the number of <em>named</em> children associated with this node.
     *
     * @return the count of this node's named children
     * @since 1.9.0
     */
    public int getNamedChildCount() {
        return getChildCount(true);
    }

    /**
     * Get an ordered list of this node's <em>named</em> children.
     *
     * @return this node's named children
     * @since 1.9.0
     */
    public List<Node> getNamedChildren() {
        Node[] children = Node.getChildren(this, true);
        return List.of(children);
    }

    /**
     * Get the smallest named node within this node that spans the given range of bytes.
     *
     * @param startByte the start byte of the range
     * @param endByte the end byte of the range
     * @return a named descendant node
     * @throws ch.usi.si.seart.treesitter.exception.ByteOffsetOutOfBoundsException
     * if either argument is outside of this node's byte range
     * @throws IllegalArgumentException if:
     * <ul>
     *     <li>{@code startByte} &lt; 0</li>
     *     <li>{@code endByte} &lt; 0</li>
     *     <li>{@code startByte} &gt; {@code endByte}</li>
     * </ul>
     * @since 1.6.0
     */
    public Node getNamedDescendant(int startByte, int endByte) {
        return getDescendant(startByte, endByte, true);
    }

    /**
     * Get the smallest named node within this node that spans the given range of points.
     *
     * @param startPoint the start point of the range
     * @param endPoint the end point of the range
     * @return a named descendant node
     * @throws NullPointerException if either argument is {@code null}
     * @throws IllegalArgumentException if any point coordinates are negative,
     * or if {@code startPoint} is a position that comes after {@code endPoint}
     * @throws ch.usi.si.seart.treesitter.exception.PointOutOfBoundsException
     * if any of the arguments is outside of this node's position range
     * @since 1.6.0
     */
    public Node getNamedDescendant(@NotNull Point startPoint, @NotNull Point endPoint) {
        return getDescendant(startPoint, endPoint, true);
    }

    /**
     * Get the next <em>named</em> sibling {@code Node}.
     *
     * @return the next named sibling, {@code null} if there is none
     */
    public Node getNextNamedSibling() {
        return getNextSibling(true);
    }

    /**
     * Get the next sibling {@code Node}.
     *
     * @return the next sibling, {@code null} if there is none
     */
    public Node getNextSibling() {
        return getNextSibling(false);
    }

    private native Node getNextSibling(boolean named);

    /**
     * Get the previous <em>named</em> sibling {@code Node}.
     *
     * @return the previous named sibling, {@code null} if there is none
     */
    public Node getPrevNamedSibling() {
        return getPrevSibling(true);
    }

    /**
     * Get the previous sibling {@code Node}.
     *
     * @return the previous sibling, {@code null} if there is none
     */
    public Node getPrevSibling() {
        return getPrevSibling(false);
    }

    public native Node getPrevSibling(boolean named);

    /**
     * Get the parent {@code Node}.
     *
     * @return the parent, {@code null} if there is none
     */
    public native Node getParent();

    /**
     * Get the node's {@link Range}, indicating its byte and row-column position span.
     *
     * @return the node's range
     */
    public Range getRange() {
        return new Range(this);
    }

    /**
     * Get the byte offset where this node starts.
     *
     * @return the node's start byte
     */
    public native int getStartByte();

    /**
     * Get the node's start position in terms of rows and columns.
     *
     * @return the node's start position
     */
    public native Point getStartPoint();

    /**
     * Get the syntax tree {@link Symbol} associated with this node.
     *
     * @return the node's symbol
     * @since 1.6.0
     */
    public native Symbol getSymbol();

    /**
     * Get the node's type as a string.
     *
     * @return the node's type
     */
    public native String getType();

    /**
     * Check if this node represents a syntax error
     * or contains any syntax errors anywhere within it.
     * Syntax errors represent parts of the code that
     * could not be incorporated into a valid syntax tree.
     *
     * @return {@code true} if the node is an {@code ERROR},
     * or contains one such child in its subtree,
     * {@code false} otherwise
     */
    public native boolean hasError();

    /**
     * Check if the node is an <em>extra</em>.
     * Extra nodes represent things like comments,
     * which are not required by the grammar,
     * but can appear anywhere.
     *
     * @return {@code true} if the node is an extra,
     * {@code false} otherwise
     */
    public native boolean isExtra();

    /**
     * Check if the node is <em>missing</em>.
     * These nodes are inserted by the parser
     * in order to recover from certain kinds
     * of syntax errors.
     *
     * @return {@code true} if the node is {@code MISSING},
     * {@code false} otherwise
     */
    public native boolean isMissing();

    /**
     * Check if the node is <em>named</em>.
     * Named nodes correspond to named rules in the grammar,
     * whereas anonymous nodes correspond to string
     * literals in the grammar.
     *
     * @return {@code true} if the node is named,
     * {@code false} otherwise
     */
    public native boolean isNamed();

    /**
     * Check if the node is <em>null</em> node.
     *
     * @return {@code true} if {@code id == 0},
     * {@code false} otherwise
     */
    public native boolean isNull();

    /**
     * Create a new {@link TreeCursor} starting from this node.
     *
     * @return a tree cursor
     */
    public native TreeCursor walk();

    /**
     * Create a new {@link QueryCursor} starting from this node.
     *
     * @param query the query to run against this node's subtree
     * @return a query cursor
     * @since 1.5.0
     */
    public native QueryCursor walk(@NotNull Query query);

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Node other = (Node) obj;
        return equals(this, other);
    }

    private static native boolean equals(@NotNull Node node, @NotNull Node other);

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }

    @Override
    @Generated
    public String toString() {
        return String.format("Node(id: %d, tree: %s)", id, tree);
    }

    /**
     * Create an iterator over the current node's subtree.
     * The subtree is traversed in a depth-first manner.
     * First iterator element is always the current node.
     *
     * @return the subtree iterator
     */
    @Override
    public @NotNull Iterator<Node> iterator() {
        return new Iterator<>() {

            private final Deque<Node> stack = new ArrayDeque<>(Collections.singletonList(Node.this));

            @Override
            public boolean hasNext() {
                return !stack.isEmpty();
            }

            @Override
            public Node next() {
                if (!hasNext()) throw new NoSuchElementException();
                Node node = stack.pop();
                stack.addAll(node.getChildren());
                return node;
            }
        };
    }
}
