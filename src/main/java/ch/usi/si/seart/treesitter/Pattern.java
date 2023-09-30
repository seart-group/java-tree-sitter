package ch.usi.si.seart.treesitter;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a single symbolic expression (s-expression) pattern of a {@link Query}.
 * Said pattern is a structured representation of a syntax tree fragment,
 * which can be used to query {@link Node} subtrees. Each instance consists
 * of an ordinal representing its order of appearance within the query,
 * as well as the actual pattern value.
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
    public String toString() {
        return value;
    }
}
