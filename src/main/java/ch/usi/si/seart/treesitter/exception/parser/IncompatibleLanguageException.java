package ch.usi.si.seart.treesitter.exception.parser;

import ch.usi.si.seart.treesitter.Language;
import lombok.experimental.StandardException;

/**
 * Thrown when attempts to set the parser language result in failure.
 *
 * @since 1.6.0
 * @author Ozren Dabić
 */
@StandardException
public class IncompatibleLanguageException extends ParserException {

    private static final String template = "Could not assign language to parser: %s";

    public IncompatibleLanguageException(Language language) {
        super(String.format(template, language));
    }
}
