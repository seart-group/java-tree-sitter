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

/**
 * Represents a single symbolic expression (s-expression) pattern of a {@link Query}.
 * Said pattern is a structured representation of a syntax tree fragment, which can
 * be used to query subtrees. Each instance can be uniquely identified by the query
 * it belongs to, along with its ordinal position within the same query.
 *
 * @since 1.7.0
 * @author Ozren Dabić
 * @see Capture
 * @see Query
 */
@Getter
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Pattern {

    Query query;

    int index;

    int startOffset;

    boolean rooted;
    boolean nonLocal;

    String value;

    List<Predicate> predicates;

    @NonFinal
    boolean enabled = true;

    @SuppressWarnings("unused")
    Pattern(
            int index,
            int startOffset,
            boolean rooted,
            boolean nonLocal,
            @NotNull String value,
            @NotNull Predicate[] predicates
    ) {
        this(null, index, startOffset, rooted, nonLocal, value.stripTrailing(), List.of(predicates));
    }

    /**
     * Disable this pattern, preventing it from further matching.
     * This will eliminate any resource usage from the {@link Query}
     * associated with the pattern.
     * <p>
     * <strong>
     * This can not be undone.
     * </strong>
     */
    public native void disable();

    @Generated
    public int getEndOffset() {
        return startOffset + value.length();
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
