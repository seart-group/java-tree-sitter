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
     * Set the range of bytes positions in which the query will be executed.
     *
     * @param startByte The start byte of the query range
     * @param endByte The end byte of the query range
     * @throws ch.usi.si.seart.treesitter.exception.ByteOffsetOutOfBoundsException
     * if either argument is outside the queried node's byte range
     * @throws IllegalArgumentException if:
     * <ul>
     *     <li>{@code startByte} &lt; 0</li>
     *     <li>{@code endByte} &lt; 0</li>
     *     <li>{@code startByte} &gt; {@code endByte}</li>
     * </ul>
     * @since 1.9.0
     */
    public native void setRange(int startByte, int endByte);

    /**
     * Set the range of row-column coordinates in which the query will be executed.
     *
     * @param startPoint The start point of the query range
     * @param endPoint The end point of the query range
     * @throws NullPointerException if either argument is null
     * @throws IllegalArgumentException if any point coordinates are negative,
     * or if {@code startPoint} is a position that comes after {@code endPoint}
     * @throws ch.usi.si.seart.treesitter.exception.PointOutOfBoundsException
     * if any of the arguments is outside the queried node's position range
     * @since 1.9.0
     */
    public native void setRange(@NotNull Point startPoint, @NotNull Point endPoint);

    /**
     * Advance to the next match of the currently running query.
     *
     * @return A match if there is one, null otherwise
     * @throws IllegalStateException if the query was not executed beforehand
     * @see #execute()
     */
    public native QueryMatch nextMatch();

    /**
     * Returns an iterator over the query matches,
     * starting from the first {@link QueryMatch}.
     * Implicitly calls {@link #execute()}.
     *
     * @return an iterator over query cursor matches
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
