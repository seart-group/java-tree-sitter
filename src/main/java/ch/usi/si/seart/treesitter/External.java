package ch.usi.si.seart.treesitter;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

@FieldDefaults(makeFinal = true)
abstract class External implements AutoCloseable {

    private static final CleanerThread CLEANER_THREAD = new CleanerThread(5000);

    protected long pointer;

    private CleanerThread.Cleaner cleaner;

    protected External() {
        this.pointer = 0L;
        this.cleaner = null;
    }

    protected External(long pointer) {
        this.pointer = pointer;
        this.cleaner = CLEANER_THREAD.register(this, new CleaningAction(this));
    }

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
        cleaner.clean();
    }

    protected abstract void delete();

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    private static final class CleaningAction implements CleanerThread.Action {

        External external;

        @Override
        public void run(boolean leak) {
            external.delete();
        }
    }
}
