package usi.si.seart.treesitter;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * A query consists of one or more patterns, where each pattern is an S-expression that matches a certain set of nodes
 * in a syntax tree. The query is associated with a particular language, and can only be run on syntax nodes parsed with
 * that language. The expression to match a given node consists of a pair of parentheses containing two things: the
 * node's type, and optionally, a series of other S-expressions that match the node's children. For example, this
 * pattern would match any {@code binary_expression} node whose children are both {@code number_literal} nodes:
 *
 * <pre>
 *     (binary_expression (number_literal) (number_literal))
 * </pre>
 *
 * Children can also be omitted. For example, this would match any {@code binary_expression} where at least one of
 * child is a {@code string_literal} node:
 *
 * <pre>
 *     (binary_expression (string_literal))
 * </pre>
 *
 * The underlying query value is immutable and can be safely shared between threads.
 */
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Query extends External {

    Language language;
    String pattern;
    List<String> captures;

    public Query(Language language, String pattern) {
        super(createIfValid(language, pattern));
        this.language = language;
        this.pattern = pattern;
        int capturesCount = TreeSitter.queryCaptureCount(pointer);
        List<String> captures = new ArrayList<>(capturesCount);
        for (int idx = 0; idx < capturesCount; idx++) {
            String capture = TreeSitter.queryCaptureName(pointer, idx);
            captures.add(capture);
        }
        this.captures = Collections.unmodifiableList(captures);
    }

    private static long createIfValid(Language language, String pattern) {
        Languages.validate(language);
        Objects.requireNonNull(pattern, "Pattern must not be null!");
        return TreeSitter.queryNew(language.getId(), pattern);
    }

    /**
     * @return The number of string literals in this query.
     */
    public int countStrings() {
        return TreeSitter.queryStringCount(pointer);
    }

    /**
     * @return The number of captures in this query.
     */
    public int countCaptures() {
        return TreeSitter.queryCaptureCount(pointer);
    }

    /**
     * @return The number of patterns in this query.
     */
    public int countPatterns() {
        return TreeSitter.queryPatternCount(pointer);
    }

    /**
     * @param capture The query capture.
     * @return The name of the provided query captures.
     */
    public String getCaptureName(QueryCapture capture) {
        return captures.get(capture.getIndex());
    }

    /**
     * @return true if the query has captures, false otherwise.
     */
    public boolean hasCaptures() {
        return !captures.isEmpty();
    }

    @Override
    protected void free(long pointer) {
        TreeSitter.queryDelete(pointer);
    }

    @Override
    public String toString() {
        return String.format(
                "Query(language: %s, pattern: '%s', captures: [%s])",
                language, pattern, String.join(", ", captures)
        );
    }
}
