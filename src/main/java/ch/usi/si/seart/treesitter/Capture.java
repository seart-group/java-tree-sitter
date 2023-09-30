package ch.usi.si.seart.treesitter;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Represents the named capture of a {@link Query}.
 * Captures are used to extract information from syntax trees when a query match occurs.
 * Each instance can be uniquely identified by the query it belongs to,
 * along with its ordinal position within the same query.
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

    Query query;

    int index;

    String name;

    @SuppressWarnings("unused")
    Capture(int index, @NotNull String name) {
        this(null, index, name);
    }

    @Override
    @Generated
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Capture capture = (Capture) o;
        return Objects.equals(query, capture.query) && index == capture.index;
    }

    @Override
    @Generated
    public int hashCode() {
        return Objects.hash(query, index);
    }

    @Override
    @Generated
    public String toString() {
        return "@" + name;
    }
}
