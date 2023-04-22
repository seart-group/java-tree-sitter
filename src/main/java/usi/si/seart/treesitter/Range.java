package usi.si.seart.treesitter;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Range {

    int startByte;
    int endByte;
    Point startPoint;
    Point endPoint;

    public Range(Node node) {
        this(node.getStartByte(), node.getEndByte(), node.getStartPoint(), node.getEndPoint());
    }

    @Override
    public String toString() {
        return String.format("[%s] - [%s]", startPoint, endPoint);
    }
}
