package ch.usi.si.seart.treesitter;

import lombok.AccessLevel;
import lombok.Generated;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Represents the named capture of a {@link Query}. Captures are used
 * to extract information from syntax trees when a query match occurs.
 * Each instance can be uniquely identified by the {@link Query} it
 * belongs to, along with its ordinal position within the same query.
 *
 * @since 1.7.0
 * @author Ozren Dabić
 * @see Pattern
 * @see Query
 */
@Getter
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Capture {

    Query query;

    int index;

    String name;

    @NonFinal
    boolean enabled = true;

    @SuppressWarnings("unused")
    Capture(int index, @NotNull String name) {
        this(null, index, name);
    }

    /**
     * Disable this capture, preventing it from returning in matches.
     * This will eliminate any resource usage from the {@link Query}
     * associated with recording the capture.
     * <p>
     * <strong>
     * This can not be undone.
     * </strong>
     */
    public native void disable();

    /**
     * Get the capture quantifier for a given query {@link Pattern}.
     *
     * @param pattern the query pattern
     * @return the quantifier
     * @throws NullPointerException if pattern is {@code null}
     * @throws IllegalArgumentException if the pattern is not present in the query
     * @since 1.12.0
     */
    public Quantifier getQuantifier(Pattern pattern) {
        return query.getQuantifier(pattern, this);
    }

    /**
     * Get the capture quantifiers for all {@link Query} patterns.
     * The order of the quantifiers in the returned list corresponds
     * to the {@link Pattern} order in the query.
     *
     * @return the quantifiers
     * @since 1.12.0
     */
    public List<Quantifier> getQuantifiers() {
        return query.getPatterns().stream()
                .map(this::getQuantifier)
                .collect(Collectors.toUnmodifiableList());
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
