package ch.usi.si.seart.treesitter;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Specialization of {@link TreeCursor} that applies a positional offset to visited nodes.
 * Retrieving nodes by either {@link #getCurrentNode()} or {@link #getCurrentTreeCursorNode()}
 * applies the offset to a copy of the original node, meaning that the tree is not mutated
 * during traversal.
 *
 * <p>
 * So what exactly is the purpose of this cursor?
 * Imagine a scenario in which we need to print out the node positions
 * of a source code file that begins with {@code n} blank lines.
 * Rather than modifying the original file, we can just use this
 * cursor with a positional offset of {@code new Position(-n, 0)}.
 * Likewise, when printing node positions of an indented function,
 * the {@code n} spaces of indentation can be negated with a
 * positional offset of {@code new Position(0, -n)}.
 * Row and column offsets can be combined with various
 * positive and negative arrangements.
 *
 * @since 1.2.0
 * @author Ozren Dabić
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OffsetTreeCursor extends TreeCursor {

    private static final String UOE_MESSAGE_1 = "Byte positions not available after node position has changed!";
    private static final String UOE_MESSAGE_2 = "Byte position searches not supported after node position has changed!";
    private static final String UOE_MESSAGE_3 = "Querying not available after node position has changed!";
    private static final String UOE_MESSAGE_4 = "Byte positions not available!";

    TreeCursor cursor;
    Point offset;

    public OffsetTreeCursor(@NotNull Node node, @NotNull Point offset) {
        super();
        Objects.requireNonNull(node, "Node must not be null!");
        Objects.requireNonNull(offset, "Offset must not be null!");
        this.cursor = node.walk();
        this.offset = offset;
    }

    @Override
    public void close() {
        cursor.close();
    }

    @Override
    public String getCurrentFieldName() {
        return cursor.getCurrentFieldName();
    }

    @Override
    public boolean gotoFirstChild() {
        return cursor.gotoFirstChild();
    }

    @Override
    public boolean gotoFirstChild(int offset) {
        throw new UnsupportedOperationException(UOE_MESSAGE_2);
    }

    @Override
    public boolean gotoFirstChild(@NotNull Point point) {
        return cursor.gotoFirstChild(point.subtract(offset));
    }

    @Override
    public boolean gotoNextSibling() {
        return cursor.gotoNextSibling();
    }

    @Override
    public boolean gotoParent() {
        return cursor.gotoParent();
    }

    @Override
    public void preorderTraversal(@NotNull Consumer<Node> callback) {
        cursor.preorderTraversal(callback);
    }

    @Override
    public Node getCurrentNode() {
        return new OffsetNode(cursor.getCurrentNode());
    }

    @Override
    public String toString() {
        return String.format("OffsetTreeCursor(row: %d, column: %d)", offset.getRow(), offset.getColumn());
    }

    @AllArgsConstructor(access = AccessLevel.PACKAGE)
    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    private class OffsetNode extends Node {

        Node node;

        @Override
        public Node getChild(int child) {
            return new OffsetNode(node.getChild(child));
        }

        @Override
        public Node getChildByFieldName(@NotNull String name) {
            return new OffsetNode(node.getChildByFieldName(name));
        }

        @Override
        public int getChildCount() {
            return node.getChildCount();
        }

        @Override
        public List<Node> getChildren() {
            return node.getChildren().stream()
                    .map(OffsetNode::new)
                    .collect(Collectors.collectingAndThen(
                            Collectors.toList(),
                            Collections::unmodifiableList
                    ));
        }

        @Override
        public String getContent() {
            return node.getContent();
        }

        @Override
        public Node getDescendant(int startByte, int endByte) {
            throw new UnsupportedOperationException(UOE_MESSAGE_2);
        }

        @Override
        public Node getDescendant(@NotNull Point startPoint, @NotNull Point endPoint) {
            return super.getDescendant(
                    startPoint.subtract(offset),
                    endPoint.subtract(offset)
            );
        }

        @Override
        public int getEndByte() {
            throw new UnsupportedOperationException(UOE_MESSAGE_1);
        }

        @Override
        public Point getEndPoint() {
            return node.getEndPoint().add(offset);
        }

        @Override
        public String getFieldNameForChild(int child) {
            return node.getFieldNameForChild(child);
        }

        @Override
        public Node getFirstChildForByte(int offset) {
            throw new UnsupportedOperationException(UOE_MESSAGE_2);
        }

        @Override
        public Node getFirstNamedChildForByte(int offset) {
            throw new UnsupportedOperationException(UOE_MESSAGE_2);
        }

        @Override
        public Node getNamedDescendant(int startByte, int endByte) {
            throw new UnsupportedOperationException(UOE_MESSAGE_2);
        }

        @Override
        public Node getNamedDescendant(@NotNull Point startPoint, @NotNull Point endPoint) {
            return super.getNamedDescendant(
                    startPoint.subtract(offset),
                    endPoint.subtract(offset)
            );
        }

        @Override
        public Node getNextNamedSibling() {
            return new OffsetNode(node.getNextNamedSibling());
        }

        @Override
        public Node getNextSibling() {
            return new OffsetNode(node.getNextSibling());
        }

        @Override
        public Node getPrevNamedSibling() {
            return new OffsetNode(node.getPrevNamedSibling());
        }

        @Override
        public Node getPrevSibling() {
            return new OffsetNode(node.getPrevSibling());
        }

        @Override
        public Node getParent() {
            return new OffsetNode(node.getParent());
        }

        @Override
        public Range getRange() {
            return new PositionOnlyRange(this);
        }

        @Override
        public int getStartByte() {
            throw new UnsupportedOperationException(UOE_MESSAGE_1);
        }

        @Override
        public Point getStartPoint() {
            return node.getStartPoint().add(offset);
        }

        @Override
        public Symbol getSymbol() {
            return node.getSymbol();
        }

        @Override
        public String getType() {
            return node.getType();
        }

        @Override
        public boolean hasError() {
            return node.hasError();
        }

        @Override
        public boolean isExtra() {
            return node.isExtra();
        }

        @Override
        public boolean isMissing() {
            return node.isMissing();
        }

        @Override
        public boolean isNamed() {
            return node.isNamed();
        }

        @Override
        public boolean isNull() {
            return node.isNull();
        }

        @Override
        public TreeCursor walk() {
            return new OffsetTreeCursor(node, offset);
        }

        @Override
        public QueryCursor walk(@NotNull Query query) {
            throw new UnsupportedOperationException(UOE_MESSAGE_3);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            OffsetNode other = (OffsetNode) obj;
            return node.equals(other.node);
        }

        @Override
        public int hashCode() {
            return Objects.hash(node, offset);
        }

        @Override
        public String toString() {
            String original = node.toString();
            String data = original.substring(5, original.length() - 1);
            return String.format(
                    "OffsetNode(%s, row: %d, column: %d)",
                    data, offset.getRow(), offset.getColumn()
            );
        }
    }

    @Override
    public TreeCursorNode getCurrentTreeCursorNode() {
        return new OffsetTreeCursorNode(cursor.getCurrentTreeCursorNode(), offset);
    }

    /*
     * An offset copy of the current tree cursor node.
     * Retains all the information from the original node,
     * except for the byte positions.
     */
    private static class OffsetTreeCursorNode extends TreeCursorNode {

        OffsetTreeCursorNode(TreeCursorNode cursorNode, Point offset) {
            this(
                    cursorNode.getName(),
                    cursorNode.getType(),
                    cursorNode.getContent(),
                    cursorNode.getStartPoint().add(offset),
                    cursorNode.getEndPoint().add(offset),
                    cursorNode.isNamed()
            );
        }

        OffsetTreeCursorNode(
                String name, String type, String content, Point startPoint, Point endPoint, boolean isNamed
        ) {
            super(name, type, content, Integer.MIN_VALUE, Integer.MAX_VALUE, startPoint, endPoint, isNamed);
        }

        @Override
        public int getEndByte() {
            throw new UnsupportedOperationException(UOE_MESSAGE_1);
        }

        @Override
        public int getStartByte() {
            throw new UnsupportedOperationException(UOE_MESSAGE_1);
        }
    }

    /*
     * A range that retains only Point information.
     * Byte positions are not provided by this implementation.
     */
    private static class PositionOnlyRange extends Range {

        PositionOnlyRange(Node node) {
            super(
                    Integer.MIN_VALUE,
                    Integer.MAX_VALUE,
                    node.getStartPoint(),
                    node.getEndPoint()
            );
        }

        @Override
        public int getEndByte() {
            throw new UnsupportedOperationException(UOE_MESSAGE_4);
        }

        @Override
        public int getStartByte() {
            throw new UnsupportedOperationException(UOE_MESSAGE_4);
        }
    }
}
