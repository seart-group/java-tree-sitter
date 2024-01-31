package ch.usi.si.seart.treesitter.version;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Contains version information pertaining to this binding.
 *
 * @author Ozren Dabić
 * @since 1.11.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Library {

    public static final String VERSION = "v${project.version}";
}
