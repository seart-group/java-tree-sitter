package ch.usi.si.seart.treesitter;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

/**
 * Represents the named capture of a {@link Query}.
 * Captures are used to extract information from
 * syntax trees when a query match occurs.
 * Each instance consists of an ordinal representing
 * its order of appearance within the query,
 * as well as the name used to refer to the
 * captured node in the results.
 *
 * @since 1.7.0
 * @author Ozren Dabić
 * @see Pattern
 * @see Query
 */
@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Capture {

    int index;

    String value;

    @Override
    public String toString() {
        return "@" + value;
    }
}
