package ch.usi.si.seart.treesitter;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter(value = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TreeCursorNode {

    String type;
    String name;
    int startByte;
    int endByte;
    Point startPoint;
    Point endPoint;
    boolean isNamed;

    @Override
    public String toString() {
        String field = (name != null) ? name + ": " : "";
        return String.format("%s%s [%s] - [%s]", field, type, startPoint, endPoint);
    }
}
