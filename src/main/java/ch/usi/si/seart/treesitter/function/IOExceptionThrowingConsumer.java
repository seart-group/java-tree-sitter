package ch.usi.si.seart.treesitter.function;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.function.Consumer;

/**
 * Represents an operation that accepts a single input argument and returns no result.
 * Can potentially throw an {@link IOException} while doing so.
 *
 * @see java.util.function.Consumer Consumer
 * @param <T> the type of the input to the operation
 */
@FunctionalInterface
public interface IOExceptionThrowingConsumer<T> {

    /**
     * Performs this operation on the given argument.
     *
     * @param t the input argument
     * @throws IOException if an I/O error occurs
     */
    void accept(T t) throws IOException;

    /**
     * Returns a wrapped {@code Consumer} that performs this operation.
     * If performing said operation results in an {@code IOException},
     * it is re-thrown as its unchecked counterpart: {@code UncheckedIOException}.
     *
     * @param consumer the operation to wrap
     * @param <T> the type of the input to the operation
     * @return and identical {@code Consumer} that throws
     * {@code UncheckedIOException} instead
     */
    static <T> Consumer<T> toUnchecked(IOExceptionThrowingConsumer<T> consumer) {
        return t -> {
            try {
                consumer.accept(t);
            } catch (IOException ex) {
                throw new UncheckedIOException(ex);
            }
        };
    }
}
