package ch.usi.si.seart.treesitter;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.lang.ref.Cleaner;

@FieldDefaults(makeFinal = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
abstract class External implements AutoCloseable {

    protected long pointer;
    private Cleaner.Cleanable cleanable;

    private static final Cleaner CLEANER = Cleaner.create();

    protected External(long pointer) {
        this.pointer = pointer;
        this.cleanable = CLEANER.register(this, new Action(this));
    }

    /**
     * Checks whether the memory address associated
     * with this external resource is {@code nullptr}.
     *
     * @return {@code true} if the memory address is 0,
     * otherwise {@code false}
     */
    public final boolean isNull() {
        return pointer == 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        External other = (External) obj;
        return pointer == other.pointer;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(pointer);
    }

    /**
     * Delete the external resource, freeing all the memory that it used.
     */
    @Override
    public void close() {
        boolean requiresCleaning = cleanable != null && !isNull();
        if (requiresCleaning) cleanable.clean();
    }

    protected abstract void delete();

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    private static final class Action implements Runnable {

        External external;

        @Override
        public void run() {
            external.delete();
        }
    }
}
