package ch.usi.si.seart.treesitter;

import java.util.List;
import java.util.Objects;
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
 */
public class OffsetTreeCursor extends TreeCursor {

    Point offset;

    public OffsetTreeCursor(Node node, Point offset) {
        super(node);
        Objects.requireNonNull(offset, "Offset must not be null!");
        this.offset = offset;
    }

    @Override
    public Node getCurrentNode() {
        return new OffsetNode(super.getCurrentNode());
    }

    private class OffsetNode extends Node {

        private final Node node;

        OffsetNode(Node node) {
            this.node = node;
        }

        @Override
        public Node getChild(int child) {
            return new OffsetNode(super.getChild(child));
        }

        @Override
        public Node getChildByFieldName(String name) {
            return new OffsetNode(super.getChildByFieldName(name));
        }

        @Override
        public List<Node> getChildren() {
            return super.getChildren().stream()
                    .map(OffsetNode::new)
                    .collect(Collectors.toList());
        }

        @Override
        public Node getDescendantForByteRange(int startByte, int endByte) {
            throw new UnsupportedOperationException(
                    "Byte position searches not supported after node position has changed!"
            );
        }

        @Override
        public int getEndByte() {
            throw new UnsupportedOperationException(
                    "Byte positions not available after node position has changed!"
            );
        }

        @Override
        public Point getEndPoint() {
            Point point = node.getEndPoint();
            return new Point(
                    point.getRow() + offset.getRow(),
                    point.getColumn() + offset.getColumn()
            );
        }

        @Override
        public Node getFirstChildForByte(int offset) {
            throw new UnsupportedOperationException(
                    "Byte position searches not supported after node position has changed!"
            );
        }

        @Override
        public Node getFirstNamedChildForByte(int offset) {
            throw new UnsupportedOperationException(
                    "Byte position searches not supported after node position has changed!"
            );
        }

        @Override
        public Node getNextNamedSibling() {
            return new OffsetNode(super.getNextNamedSibling());
        }

        @Override
        public Node getNextSibling() {
            return new OffsetNode(super.getNextSibling());
        }

        @Override
        public Node getPrevNamedSibling() {
            return new OffsetNode(super.getPrevNamedSibling());
        }

        @Override
        public Node getPrevSibling() {
            return new OffsetNode(super.getPrevSibling());
        }

        @Override
        public Node getParent() {
            return new OffsetNode(super.getParent());
        }

        @Override
        public Range getRange() {
            return new PositionOnlyRange(this);
        }

        @Override
        public int getStartByte() {
            throw new UnsupportedOperationException(
                    "Byte positions not available after node position has changed!"
            );
        }

        @Override
        public Point getStartPoint() {
            Point point = node.getStartPoint();
            return new Point(
                    point.getRow() + offset.getRow(),
                    point.getColumn() + offset.getColumn()
            );
        }

        @Override
        public TreeCursor walk() {
            return new OffsetTreeCursor(node, offset);
        }
    }

    @Override
    public TreeCursorNode getCurrentTreeCursorNode() {
        return new OffsetTreeCursorNode(super.getCurrentTreeCursorNode(), offset);
    }

    /*
     * An offset copy of the current tree cursor node.
     * Retains all the information from the original node,
     * except for the byte positions.
     */
    private static class OffsetTreeCursorNode extends TreeCursorNode {

        OffsetTreeCursorNode(TreeCursorNode cursorNode, Point offset) {
            this(
                    cursorNode.getType(),
                    cursorNode.getName(),
                    new Point(
                            cursorNode.getStartPoint().getRow() + offset.getRow(),
                            cursorNode.getStartPoint().getColumn() + offset.getColumn()
                    ),
                    new Point(
                            cursorNode.getEndPoint().getRow() + offset.getRow(),
                            cursorNode.getEndPoint().getColumn() + offset.getColumn()
                    ),
                    cursorNode.isNamed()
            );
        }

        OffsetTreeCursorNode(
                String type, String name, Point startPoint, Point endPoint, boolean isNamed
        ) {
            super(type, name, Integer.MIN_VALUE, Integer.MAX_VALUE, startPoint, endPoint, isNamed);
        }

        @Override
        public int getEndByte() {
            throw new UnsupportedOperationException(
                    "Byte positions not available after node position has changed!"
            );
        }

        @Override
        public int getStartByte() {
            throw new UnsupportedOperationException(
                    "Byte positions not available after node position has changed!"
            );
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
            throw new UnsupportedOperationException("Byte positions not available!");
        }

        @Override
        public int getStartByte() {
            throw new UnsupportedOperationException("Byte positions not available!");
        }
    }
}
