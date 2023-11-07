package ch.usi.si.seart.treesitter;

import lombok.AccessLevel;
import lombok.Generated;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Cursor used for executing queries, carrying the state needed to process them.
 * <p>
 * The query cursor should not be shared between threads, but can be reused for many query executions.
 *
 * @since 1.0.0
 * @author Ozren Dabić
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class QueryCursor extends External implements Iterable<QueryMatch> {

    Node node;
    Query query;

    @Getter
    @NonFinal
    boolean executed = false;

    @SuppressWarnings("unused")
    QueryCursor(long pointer, @NotNull Node node, @NotNull Query query) {
        super(pointer);
        this.node = node;
        this.query = query;
    }

    @Override
    protected native void delete();

    /**
     * Start running a query against a node.
     */
    public native void execute();

    /**
     * Advance to the next match of the currently running query.
     *
     * @return A match if there is one, null otherwise
     * @throws IllegalStateException if the query was not executed beforehand
     * @see #execute()
     */
    public native QueryMatch nextMatch();

    /**
     * @return An iterator over the query cursor matches, starting from the first match
     */
    @Override
    public @NotNull Iterator<QueryMatch> iterator() {
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
    @Generated
    public String toString() {
        return String.format("QueryCursor(node: %s, query: %s)", node, query);
    }
}
