package usi.si.seart.treesitter;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Cursor used for executing queries, carrying the state needed to process them.
 * <p>
 * The query cursor should not be shared between threads, but can be reused for many query executions.
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class QueryCursor extends External implements Iterable<QueryMatch> {

    Node node;
    Query query;

    @SuppressWarnings({"FieldMayBeFinal", "unused"})
    @NonFinal
    boolean executed = false;

    public QueryCursor(Node node, Query query) {
        super(createIfValid(node, query));
        this.node = node;
        this.query = query;
    }

    private static long createIfValid(Node node, Query query) {
        Objects.requireNonNull(node, "Node must not be null!");
        Objects.requireNonNull(query, "Query must not be null!");
        return malloc();
    }

    static native long malloc();

    /**
     * Delete the query cursor,
     * freeing all the memory that it used.
     */
    @Override
    public native void close();

    /**
     * Start running a given query on a given node.
     * Successive calls to this method are ignored.
     */
    public native void execute();

    /**
     * Advance to the next match of the currently running query.
     *
     * @return A match if there is one, null otherwise.
     * @throws IllegalStateException
     * if {@code queryExec()} was not called beforehand
     */
    public native QueryMatch nextMatch();

    /**
     * @return An iterator over the query cursor matches, starting from the first match.
     */
    @Override
    public Iterator<QueryMatch> iterator() {
        execute();
        return new Iterator<>() {

            private QueryMatch current = nextMatch();

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public QueryMatch next() {
                if (!hasNext()) throw new NoSuchElementException();
                QueryMatch match = current;
                current = nextMatch();
                return match;
            }
        };
    }

    @Override
    public String toString() {
        return String.format("QueryCursor(node: %s, query: %s)", node, query);
    }
}
