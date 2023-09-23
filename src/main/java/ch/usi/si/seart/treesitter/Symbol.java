package ch.usi.si.seart.treesitter;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Symbol {

    int id;
    Type type;
    String name;

    @SuppressWarnings("unused")
    Symbol(int id, int ordinal, String name) {
        this(id, Type.get(ordinal), name);
    }

    @Override
    @Generated
    public String toString() {
        return String.format("Symbol(id: %d, type: %s, name: '%s')", id, type, name);
    }

    public enum Type {

        REGULAR,
        ANONYMOUS,
        AUXILIARY;

        private static final Type[] VALUES = values();

        private static Type get(int ordinal) {
            return VALUES[ordinal];
        }
    }
}
