package ch.usi.si.seart.treesitter.version;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Utility used for obtaining the current version of the {@code tree-sitter} API.
 *
 * @author Ozren Dabić
 * @since 1.11.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TreeSitter {

    private static final String SHA = "98be227227af10cc7a269cb3ffb23686c0610b17";

    private static final String TAG = "v0.20.9";

    /**
     * Get the current version of {@code tree-sitter}.
     *
     * @return the semantic version string, along with a commit SHA
     */
    public static String getVersion() {
        return TAG + " (" + SHA + ")";
    }
}
