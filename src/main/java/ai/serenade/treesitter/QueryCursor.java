package ai.serenade.treesitter;

/**
 * Cursor used for executing queries, carrying the state needed to process them.
 *
 * @apiNote The query cursor should not be shared between threads, but can be reused for many query executions.
 */
public class QueryCursor implements AutoCloseable {

    private final long pointer;

    QueryCursor(long pointer) {
        this.pointer = pointer;
    }

    public QueryCursor() {
        this(TreeSitter.queryCursorNew());
    }

    /**
     * Start running a given query on a given node.
     *
     * @param query The query to execute.
     * @param node The node which the query will be executed on.
     */
    public void execQuery(Query query, Node node) {
        TreeSitter.queryCursorExec(pointer, query.getPointer(), node);
    }

    /**
     * Advance to the next match of the currently running query.
     *
     * @return A match if there is one, null otherwise.
     */
    public QueryMatch nextMatch() {
        return TreeSitter.queryCursorNextMatch(pointer);
    }

    /**
     * Delete a query cursor, freeing all the memory that it used.
     */
    @Override
    public void close() {
        TreeSitter.queryCursorDelete(pointer);
    }
}
