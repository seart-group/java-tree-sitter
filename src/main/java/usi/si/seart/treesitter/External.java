package usi.si.seart.treesitter;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter(value = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
abstract class External implements AutoCloseable {

    protected final long pointer;

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

    @Override
    public void close() {
        free(pointer);
    }

    /**
     * Delete the external resource, freeing all the memory that it used.
     */
    protected abstract void free(long pointer);
}