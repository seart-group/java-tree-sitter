package usi.si.seart.treesitter;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * A Node represents a single node in the syntax tree.
 * It tracks its start and end positions in the source code,
 * as well as its relation to other nodes like its parent,
 * siblings and children.
 */
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class Node implements Iterable<Node> {

    private int context0;
    private int context1;
    private int context2;
    private int context3;
    private long id;
    private long tree;

    /**
     * Get the node's child at the given index,
     * where zero represents the first child.
     *
     * @param child The zero-indexed child position
     * @return The Node's child at the given index
     * @throws IndexOutOfBoundsException
     * if the index is a negative number
     */
    public Node getChild(int child) {
        if (child < 0)
            throw new IndexOutOfBoundsException("Child index must not be negative!");
        return TreeSitter.nodeChild(this, child);
    }

    /**
     * @param name The child field name.
     * @return The node's child with the given field name.
     * @throws NullPointerException
     * if the field name is null
     */
    public Node getChildByFieldName(String name) {
        Objects.requireNonNull(name, "Field name must not be null!");
        return TreeSitter.nodeChildByFieldName(this, name);
    }

    /**
     * @return The node's number of children.
     */
    public int getChildCount() {
        return TreeSitter.nodeChildCount(this);
    }

    /**
     * @param startByte The starting byte of the range
     * @param endByte The ending byte of the range
     * @return The smallest node within this node that spans the given range of bytes
     * @throws IllegalArgumentException if {@code startByte} &gt; {@code endByte}
     */
    public Node getDescendantForByteRange(int startByte, int endByte) {
        if (startByte > endByte)
            throw new IllegalArgumentException(
                    "The starting byte of the range must not be greater than the ending byte!"
            );
        return TreeSitter.nodeDescendantForByteRange(this, startByte, endByte);
    }

    /**
     * @return The node's end byte.
     */
    public int getEndByte() {
        return TreeSitter.nodeEndByte(this);
    }

    /**
     * @return The node's end position in terms of rows and columns.
     */
    public Point getEndPoint() {
        return TreeSitter.nodeEndPoint(this);
    }

    /**
     * @return
     * The field name for node's child at the given index,
     * with zero representing the first child.
     * Returns NULL, if no field is found.
     * @param child The zero-indexed child position
     * @throws IndexOutOfBoundsException
     * if the index is a negative number
     */
    public String getFieldNameForChild(int child) {
        if (child < 0)
            throw new IndexOutOfBoundsException("Child index must not be negative!");
        return TreeSitter.nodeFieldNameForChild(this, child);
    }

    /**
     * @param offset The offset in bytes.
     * @return The node's first child that extends beyond the given byte offset.
     * @throws IndexOutOfBoundsException if the offset is a negative number
     */
    public Node getFirstChildForByte(int offset) {
        if (offset < 0)
            throw new IndexOutOfBoundsException("Byte offset must not be negative!");
        return TreeSitter.nodeFirstChildForByte(this, offset);
    }

    /**
     * @param offset The offset in bytes.
     * @return The node's first named child that extends beyond the given byte offset.
     * @throws IndexOutOfBoundsException if the offset is a negative number
     */
    public Node getFirstNamedChildForByte(int offset) {
        if (offset < 0)
            throw new IndexOutOfBoundsException("Byte offset must not be negative!");
        return TreeSitter.nodeFirstNamedChildForByte(this, offset);
    }

    /**
     * @return An S-expression representing the node as a string.
     */
    public String getNodeString() {
        return TreeSitter.nodeString(this);
    }

    /**
     * @return The node's next <em>named</em> sibling.
     */
    public Node getNextNamedSibling() {
        return TreeSitter.nodeNextNamedSibling(this);
    }

    /**
     * @return The node's next sibling.
     */
    public Node getNextSibling() {
        return TreeSitter.nodeNextSibling(this);
    }

    /**
     * @return The node's previous <em>named</em> sibling.
     */
    public Node getPrevNamedSibling() {
        return TreeSitter.nodePrevNamedSibling(this);
    }

    /**
     * @return The node's previous sibling.
     */
    public Node getPrevSibling() {
        return TreeSitter.nodePrevSibling(this);
    }

    /**
     * @return The node's immediate parent.
     */
    public Node getParent() {
        return TreeSitter.nodeParent(this);
    }

    /**
     * @return The node's range, indicating its byte and file position span.
     */
    public Range getRange() {
        return new Range(this);
    }

    /**
     * @return The node's start byte.
     */
    public int getStartByte() {
        return TreeSitter.nodeStartByte(this);
    }

    /**
     * @return The node's start position in terms of rows and columns.
     */
    public Point getStartPoint() {
        return TreeSitter.nodeStartPoint(this);
    }

    /**
     * @return The node's type as a string
     */
    public String getType() {
        return TreeSitter.nodeType(this);
    }

    /**
     * @return true if the node is a syntax error or contains any syntax errors, false otherwise.
     */
    public boolean hasError() {
        return TreeSitter.nodeHasError(this);
    }

    /**
     * Check if the node is <em>extra</em>.
     * Extra nodes represent things like comments,
     * which are not required the grammar,
     * but can appear anywhere.
     *
     * @return true if the node is an extra, false otherwise.
     */
    public boolean isExtra() {
        return TreeSitter.nodeIsExtra(this);
    }

    /**
     * Check if the node is <em>missing</em>.
     * Missing nodes are inserted by the parser
     * in order to recover from certain kinds
     * of syntax errors.
     *
     * @return true if the node is missing, false otherwise.
     */
    public boolean isMissing() {
        return TreeSitter.nodeIsMissing(this);
    }

    /**
     * Check if the node is <em>named</em>.
     * Named nodes correspond to named rules in the grammar,
     * whereas anonymous nodes correspond to string
     * literals in the grammar.
     *
     * @return true if the node is named, false otherwise.
     */
    public boolean isNamed() {
        return TreeSitter.nodeIsNamed(this);
    }

    /**
     * Check if the node is <em>null</em> node.
     *
     * @return true if {@code id == 0}, false otherwise
     */
    public boolean isNull() {
        return TreeSitter.nodeIsNull(this);
    }

    /**
     * A tree cursor allows you to walk a syntax tree more
     * efficiently than is possible using the instance functions.
     * It is a mutable object that is always on a certain syntax node,
     * and can be moved imperatively to different nodes.
     *
     * @return A new tree cursor starting from the given node.
     */
    public TreeCursor walk() {
        return new TreeCursor(TreeSitter.treeCursorNew(this));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Node other = (Node) obj;
        return TreeSitter.nodeEq(this, other);
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }

    @Override
    public String toString() {
        return "Node(id: "+id+", tree: "+tree+")";
    }

    /**
     * @return An iterator over the node subtree, starting from the current node.
     */
    @Override
    public Iterator<Node> iterator() {
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
                int children = node.getChildCount();
                for (int child = children - 1; child >= 0; child--) {
                    stack.push(node.getChild(child));
                }
                return node;
            }
        };
    }
}
