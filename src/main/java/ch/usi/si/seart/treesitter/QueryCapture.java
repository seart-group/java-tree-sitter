package ch.usi.si.seart.treesitter;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class QueryCapture {

    Node node;
    int index;

    @Override
    @Generated
    public String toString() {
        return String.format("QueryCapture(index: %d, node: %s)", index, node);
    }
}
