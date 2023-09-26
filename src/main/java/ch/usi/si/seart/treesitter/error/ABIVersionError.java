package ch.usi.si.seart.treesitter.error;

import lombok.experimental.StandardException;

/**
 * An error raised whenever there is an ABI version mismatch.
 * <p>
 * These usually occur as a result of a language being generated
 * with an incompatible version of the Tree-sitter CLI.
 *
 * @since 1.0.0
 * @author Ozren Dabić
 */
@StandardException
public class ABIVersionError extends LinkageError {

    private static final String TEMPLATE = "Incompatible language version: %d";

    public ABIVersionError(int version) {
        super(String.format(TEMPLATE, version));
    }
}
