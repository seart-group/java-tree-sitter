package ch.usi.si.seart.treesitter;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

/**
 * Represents a symbol in an abstract syntax tree.
 * Symbols are used to identify nodes in the AST,
 * and each symbol has an associated ID, type, and name.
 *
 * @author Ozren Dabić
 * @since 1.6.0
 */
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

    /**
     * Enumeration representing the possible types of symbols.
     * This includes:
     * <ul>
     *     <li>Named nodes ({@link #REGULAR})</li>
     *     <li>Anonymous nodes ({@link #ANONYMOUS})</li>
     *     <li>Hidden nodes ({@link #AUXILIARY})</li>
     * </ul>
     *
     * @author Ozren Dabić
     * @since 1.6.0
     */
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
