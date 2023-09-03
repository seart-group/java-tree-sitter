package ch.usi.si.seart.treesitter;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

/**
 * Wrapper for a single node matched with a {@link Query} capture.
 * Each instance contains a reference to a matched node,
 * as well as the ordinal index within the match.
 *
 * @author Ozren Dabić
 */
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
