package ch.usi.si.seart.treesitter;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;

/**
 * A specialised symbolic expression (s-expression) that can
 * appear anywhere within a {@link Pattern}, instances of this
 * class represent predicates within a pattern. They consist of
 * arbitrary metadata and conditions which dictate the matching
 * behavior of a {@link Query}.
 * <p>
 * Note that the actual matching behavior is currently not
 * implemented in this binding. In spite of this, one can
 * still use this class to apply matching logic manually.
 *
 * @since 1.12.0
 * @author Ozren Dabić
 * @see Pattern
 * @see Query
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Predicate {

    Pattern pattern;
    List<Step> steps;

    @SuppressWarnings("unused")
    Predicate(Pattern pattern, Step[] steps) {
        this(pattern, List.of(steps));
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < steps.size() - 1; i++) {
            Step step = steps.get(i);
            String value = step.getValue();
            if (i == 0) {
                builder.append(value);
                continue;
            }
            builder.append(" ");
            switch (step.getType()) {
                case CAPTURE:
                    builder.append("@").append(value);
                    break;
                case STRING:
                    builder.append('"').append(value).append('"');
                    break;
                default:
            }
        }

        return "(#" + builder + ")";
    }

    /**
     * Represents a single step in a {@link Predicate}.
     * Each step is characterized by a {@link Type Type},
     * and an optional value.
     *
     * @since 1.12.0
     * @author Ozren Dabić
     */
    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    public static class Step {

        Type type;
        String value;

        public Step(int type, String value) {
            this(Type.get(type), value);
        }

        /**
         * Represents the type of {@link Step Step}.
         *
         * @since 1.12.0
         * @author Ozren Dabić
         */
        public enum Type {

            /**
             * Steps with this type are <em>sentinels</em> that
             * represent the end of an individual predicate.
             * Only one such step is allowed per predicate.
             */
            DONE,

            /**
             * Steps with this type represent names of captures.
             */
            CAPTURE,

            /**
             * Steps with this type represent literal strings.
             */
            STRING;

            private static final Type[] VALUES = values();

            private static Type get(int ordinal) {
                return VALUES[ordinal];
            }
        }
    }
}
