package ch.usi.si.seart.treesitter;

import ch.usi.si.seart.treesitter.exception.query.QueryException;
import lombok.AccessLevel;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

/**
 * A query consists of one or more patterns, where each pattern is a symbolic expression (S-expression)
 * that matches a certain set of nodes in an abstract syntax tree. Each query is associated with a particular language,
 * and can only be run on syntax nodes parsed with that language. The expression to match a given node consists of
 * a pair of parentheses containing two things: the node's type, and optionally, a series of other S-expressions
 * that match the node's children. Query instances are immutable and can be safely shared among threads.
 *
 * @since 1.0.0
 * @author Ozren Dabić
 * @see <a href="https://tree-sitter.github.io/tree-sitter/using-parsers#query-syntax">Query Syntax</a>
 */
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Query extends External {

    Language language;
    String pattern;
    List<String> captures;

    @SuppressWarnings("unused")
    Query(long pointer, @NotNull Language language, @NotNull String pattern, String[] captures) {
        super(pointer);
        this.language = language;
        this.pattern = pattern;
        this.captures = List.of(captures);
    }

    /**
     * @deprecated Use {@link Query#getFor(Language, String)} or {@link Query#builder()} instead
     */
    @Deprecated(since = "1.7.0", forRemoval = true)
    public Query(@NotNull Language language, @NotNull String pattern) {
        throw new UnsupportedOperationException(
                "This constructor should no longer be used"
        );
    }

    /**
     * Static factory for obtaining new Query instances.
     *
     * @param language The language for querying
     * @param pattern The query pattern
     * @return A new query instance
     * @since 1.7.0
     */
    public static Query getFor(@NotNull Language language, @NotNull String pattern) {
        return builder().language(language).pattern(pattern).build();
    }

    /**
     * Obtain a new builder for constructing a Query instance.
     *
     * @return a new query builder
     * @since 1.7.0
     */
    public static Builder builder() {
        return new Builder();
    }

    @FieldDefaults(level = AccessLevel.PRIVATE)
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Builder {

        Language language = null;
        String pattern = null;

        /**
         * Sets the programming language associated with the query.
         *
         * @param language The language associated with the query
         * @return this builder
         * @throws NullPointerException if the language is null
         * @throws UnsatisfiedLinkError if the language was not linked to native code
         * @throws ch.usi.si.seart.treesitter.error.ABIVersionError
         * if the language ABI version is incompatible with requirements
         */
        public Builder language(@NotNull Language language) {
            Language.validate(language);
            this.language = language;
            return this;
        }

        /**
         * Sets the query pattern that will be used to match nodes.
         *
         * @param pattern The symbolic expression string of the query pattern.
         * @return this builder
         * @throws NullPointerException if the pattern is null
         */
        public Builder pattern(@NotNull String pattern) {
            Objects.requireNonNull(pattern, "Pattern must not be null!");
            this.pattern = pattern;
            return this;
        }

        /**
         * Builds and returns a new Query instance
         * with the configured language and pattern.
         *
         * @return A new query instance
         * @throws QueryException if query construction fails
         */
        public Query build() {
            Objects.requireNonNull(language, "Language must not be null!");
            Objects.requireNonNull(pattern, "Pattern must not be null!");
            return build(language, pattern);
        }

        private static native Query build(Language language, String pattern) throws QueryException;
    }

    @Override
    protected native void delete();

    /**
     * @return The number of string literals in this query
     */
    public native int countStrings();

    /**
     * @return The number of captures in this query
     */
    public native int countCaptures();

    /**
     * @return The number of patterns in this query
     */
    public native int countPatterns();

    /**
     * @param capture The query capture
     * @return The name of the provided query captures
     */
    public String getCaptureName(@NotNull QueryCapture capture) {
        return captures.get(capture.getIndex());
    }

    /**
     * @return true if the query has captures, false otherwise
     */
    public boolean hasCaptures() {
        return !captures.isEmpty();
    }

    @Override
    @Generated
    public String toString() {
        return String.format(
                "Query(language: %s, pattern: '%s', captures: [%s])",
                language, pattern, String.join(", ", captures)
        );
    }
}
