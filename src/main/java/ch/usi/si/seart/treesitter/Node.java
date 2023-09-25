package ch.usi.si.seart.treesitter;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * A Node represents a single node in the syntax tree.
 * It tracks its start and end positions in the source code,
 * as well as its relation to other nodes like its parent,
 * siblings and children.
 *
 * @since 1.0.0
 * @author Tommy MacWilliam
 * @author Ozren Dabić
 */
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Node implements Iterable<Node> {

    int context0;
    int context1;
    int context2;
    int context3;
    long id;

    Tree tree;

    Node() {
        this(0, 0, 0, 0, 0L, null);
    }

    /**
     * Get the node's child at the given index,
     * where zero represents the first child.
     *
     * @param child The zero-indexed child position
     * @return The Node's child at the given index
     * @throws IndexOutOfBoundsException
     * if the index is a negative number or if it is
     * greater or equal to the total number of children
     */
    public native Node getChild(int child);

    /**
     * @param name The child field name
     * @return The node's child with the given field name
     * @throws NullPointerException if the field name is null
     */
    public native Node getChildByFieldName(@NotNull String name);

    /**
     * @return The node's number of children
     */
    public native int getChildCount();

    /**
     * @return A list of the node's children
     */
    public List<Node> getChildren() {
        return IntStream.range(0, getChildCount())
                .mapToObj(this::getChild)
                .collect(Collectors.toList());
    }

    /**
     * @return The source code content encapsulated by this node
     * @since 1.5.0
     */
    public String getContent() {
        return !isNull() ? tree.getSource(getStartByte(), getEndByte()) : null;
    }

    /**
     * @deprecated Use {@link #getDescendant(int, int)} instead
     * @return The smallest node within this node that spans the given range of bytes
     */
    @Deprecated(since = "1.6.0", forRemoval = true)
    public Node getDescendantForByteRange(int startByte, int endByte) {
        return getDescendant(startByte, endByte);
    }

    /**
     * Get the smallest node within this node that spans the given range of bytes.
     *
     * @param startByte The start byte of the range
     * @param endByte The end byte of the range
     * @return A descendant node
     * @throws IndexOutOfBoundsException if either argument is outside of this node's byte range
     * @throws IllegalArgumentException if:
     * <ul>
     *     <li>{@code startByte} &lt; 0</li>
     *     <li>{@code endByte} &lt; 0</li>
     *     <li>{@code startByte} &gt; {@code endByte}</li>
     * </ul>
     * @since 1.6.0
     */
    public native Node getDescendant(int startByte, int endByte);

    /**
     * Get the smallest node within this node that spans the given range of points.
     *
     * @param startPoint The start point of the range
     * @param endPoint The end point of the range
     * @return A descendant node
     * @throws NullPointerException if either argument is null
     * @throws IllegalArgumentException if:
     * <ul>
     *     <li>any of the arguments is outside of this node's position range</li>
     *     <li>{@code startPoint} is a position that comes after {@code endPoint}</li>
     * </ul>
     * @since 1.6.0
     */
    public native Node getDescendant(@NotNull Point startPoint, @NotNull Point endPoint);

    /**
     * @return The node's end byte
     */
    public native int getEndByte();

    /**
     * @return The node's end position in terms of rows and columns
     */
    public native Point getEndPoint();

    /**
     * @return
     * The field name for node's child at the given index,
     * with zero representing the first child,
     * {@code null} if no field is found
     * @param child The zero-indexed child position
     * @throws IndexOutOfBoundsException
     * if the index is a negative number or if it is
     * greater or equal to the total number of children
     */
    public native String getFieldNameForChild(int child);

    /**
     * @param offset The offset in bytes
     * @return The node's first child that extends beyond the given byte offset
     * @throws IndexOutOfBoundsException if the byte offset is outside the node's byte range
     */
    public native Node getFirstChildForByte(int offset);

    /**
     * @param offset The offset in bytes
     * @return The node's first named child that extends beyond the given byte offset
     * @throws IndexOutOfBoundsException if the byte offset is outside the node's byte range
     */
    public native Node getFirstNamedChildForByte(int offset);

    /**
     * Get the smallest named node within this node that spans the given range of bytes.
     *
     * @param startByte The start byte of the range
     * @param endByte The end byte of the range
     * @return A named descendant node
     * @throws IndexOutOfBoundsException if either argument is outside of this node's byte range
     * @throws IllegalArgumentException if:
     * <ul>
     *     <li>{@code startByte} &lt; 0</li>
     *     <li>{@code endByte} &lt; 0</li>
     *     <li>{@code startByte} &gt; {@code endByte}</li>
     * </ul>
     * @since 1.6.0
     */
    public native Node getNamedDescendant(int startByte, int endByte);

    /**
     * Get the smallest named node within this node that spans the given range of points.
     *
     * @param startPoint The start point of the range
     * @param endPoint The end point of the range
     * @return A named descendant node
     * @throws NullPointerException if either argument is null
     * @throws IllegalArgumentException if:
     * <ul>
     *     <li>any of the arguments is outside of this node's position range</li>
     *     <li>{@code startPoint} is a position that comes after {@code endPoint}</li>
     * </ul>
     * @since 1.6.0
     */
    public native Node getNamedDescendant(@NotNull Point startPoint, @NotNull Point endPoint);

    /**
     * @return An S-expression representing the node as a string
     * @deprecated <strong>This operation is potentially unsafe for large trees</strong>
     * @see ch.usi.si.seart.treesitter.printer.SymbolicExpressionPrinter SymbolicExpressionPrinter
     */
    @Deprecated(since = "1.4.0", forRemoval = true)
    public native String getNodeString();

    /**
     * @return The node's next <em>named</em> sibling
     */
    public native Node getNextNamedSibling();

    /**
     * @return The node's next sibling
     */
    public native Node getNextSibling();

    /**
     * @return The node's previous <em>named</em> sibling
     */
    public native Node getPrevNamedSibling();

    /**
     * @return The node's previous sibling
     */
    public native Node getPrevSibling();

    /**
     * @return The node's immediate parent
     */
    public native Node getParent();

    /**
     * @return The node's range, indicating its byte and file position span
     */
    public Range getRange() {
        return new Range(this);
    }

    /**
     * @return The node's start byte
     */
    public native int getStartByte();

    /**
     * @return The node's start position in terms of rows and columns
     */
    public native Point getStartPoint();

    /**
     * @return The node's symbol
     * @since 1.6.0
     */
    public native Symbol getSymbol();

    /**
     * @return The node's type as a string
     */
    public native String getType();

    /**
     * @return true if the node is a syntax error or contains any syntax errors, false otherwise
     */
    public native boolean hasError();

    /**
     * Check if the node is <em>extra</em>.
     * Extra nodes represent things like comments,
     * which are not required by the grammar,
     * but can appear anywhere.
     *
     * @return true if the node is an extra, false otherwise
     */
    public native boolean isExtra();

    /**
     * Check if the node is <em>missing</em>.
     * Missing nodes are inserted by the parser
     * in order to recover from certain kinds
     * of syntax errors.
     *
     * @return true if the node is missing, false otherwise
     */
    public native boolean isMissing();

    /**
     * Check if the node is <em>named</em>.
     * Named nodes correspond to named rules in the grammar,
     * whereas anonymous nodes correspond to string
     * literals in the grammar.
     *
     * @return true if the node is named, false otherwise
     */
    public native boolean isNamed();

    /**
     * Check if the node is <em>null</em> node.
     *
     * @return true if {@code id == 0}, false otherwise
     */
    public native boolean isNull();

    /**
     * @return A new tree cursor starting from the given node
     */
    public native TreeCursor walk();

    /**
     * @param query the query to run against this node's subtree
     * @return A new query cursor starting from the given node
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
        return String.format("Node(id: %d, tree: %d)", id, tree.pointer);
    }

    /**
     * @return An iterator over the node subtree, starting from the current node
     */
    @Override
    public @NotNull Iterator<Node> iterator() {
        return new Iterator<>() {

            private final Deque<Node> stack = new ArrayDeque<>(List.of(Node.this));

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
