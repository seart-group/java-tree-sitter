package ch.usi.si.seart.treesitter;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

/**
 * Special type of node returned during tree traversals with {@link TreeCursor}.
 *
 * @since 1.0.0
 * @author Tommy MacWilliam
 * @author Ozren Dabić
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TreeCursorNode {

    String name;
    String type;
    String content;
    int startByte;
    int endByte;
    Point startPoint;
    Point endPoint;
    boolean isNamed;

    @SuppressWarnings("unused")
    TreeCursorNode(String name, Node node) {
        this(
                name,
                node.getType(),
                node.getContent(),
                node.getStartByte(),
                node.getEndByte(),
                node.getStartPoint(),
                node.getEndPoint(),
                node.isNamed()
        );
    }

    @Override
    @Generated
    public String toString() {
        String field = name != null ? name + ": " : "";
        return String.format("%s%s [%s] - [%s]", field, type, startPoint, endPoint);
    }
}
