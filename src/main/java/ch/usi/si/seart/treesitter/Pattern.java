package ch.usi.si.seart.treesitter;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Represents a single symbolic expression (s-expression) pattern of a {@link Query}.
 * Said pattern is a structured representation of a syntax tree fragment, which can be used to query subtrees.
 * Each instance can be uniquely identified by the query it belongs to,
 * along with its ordinal position within the same query.
 *
 * @since 1.7.0
 * @author Ozren Dabić
 * @see Capture
 * @see Query
 */
@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Pattern {

    Query query;

    int index;

    boolean rooted;
    boolean nonLocal;

    String value;

    @SuppressWarnings("unused")
    Pattern(int index, boolean rooted, boolean nonLocal, @NotNull String value) {
        this(null, index, rooted, nonLocal, value);
    }

    @Override
    @Generated
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pattern pattern = (Pattern) o;
        return Objects.equals(query, pattern.query) && index == pattern.index;
    }

    @Override
    @Generated
    public int hashCode() {
        return Objects.hash(query, index);
    }

    @Override
    @Generated
    public String toString() {
        return value;
    }
}
