package usi.si.seart.treesitter.error;

import lombok.experimental.StandardException;

/**
 * An error raised whenever there is an ABI version mismatch.
 * <p>
 * These usually occur as a result of a language being generated
 * with an incompatible version of the Tree-sitter CLI.
 */
@StandardException
public class ABIVersionError extends Error {
}
