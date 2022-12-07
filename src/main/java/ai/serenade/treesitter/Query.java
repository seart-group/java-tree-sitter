package ai.serenade.treesitter;

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
 * @apiNote The underlying query value is immutable and can be safely shared between threads.
 */
public class Query implements AutoCloseable {

    private final long pointer;

    Query(long pointer) {
        this.pointer = pointer;
    }

    public Query(Language language, String source) {
        this(TreeSitter.queryNew(language.getId(), source));
    }

    long getPointer() {
        return pointer;
    }

    /**
     * Delete a query, freeing all the memory that it used.
     */
    @Override
    public void close() {
        TreeSitter.queryDelete(pointer);
    }
}
