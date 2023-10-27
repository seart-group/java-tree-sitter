package ch.usi.si.seart.treesitter;

import ch.usi.si.seart.treesitter.exception.parser.IncompatibleLanguageException;
import ch.usi.si.seart.treesitter.exception.parser.ParsingException;
import lombok.AccessLevel;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Parsers are stateful objects that can be assigned a language
 * and used to produce a {@link Tree Tree} based on some source code.
 * Instances of this class <strong>can not</strong> be created
 * without an initially set language.
 *
 * @since 1.0.0
 * @author Tommy MacWilliam
 * @author Ozren Dabić
 */
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Parser extends External {

    Language language;

    private static final Charset CHARSET = StandardCharsets.UTF_16LE;

    @SuppressWarnings("unused")
    Parser(long pointer, @NotNull Language language) {
        super(pointer);
        this.language = language;
    }

    /**
     * @deprecated Use {@link Parser#getFor(Language)} or {@link Parser#builder()} instead
     */
    @Deprecated
    public Parser(@NotNull Language language) {
        throw new UnsupportedOperationException(
                "This constructor should no longer be used"
        );
    }

    /**
     * Static factory for obtaining new Parser instances.
     *
     * @param language The language used for parsing
     * @return A new parser instance
     * @since 1.7.0
     */
    public static Parser getFor(@NotNull Language language) {
        return builder().language(language).build();
    }

    /**
     * Obtain a new builder for constructing a Parser instance.
     *
     * @return a new parser builder
     * @since 1.7.0
     */
    public static Builder builder() {
        return new Builder();
    }

    @FieldDefaults(level = AccessLevel.PRIVATE)
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Builder {

        Language language = null;

        /**
         * Sets the programming language intended for parsing.
         *
         * @param language The language used for parsing
         * @return this builder
         * @throws NullPointerException if the language is null
         * @throws UnsatisfiedLinkError if the language was not linked to native code
         * @throws ch.usi.si.seart.treesitter.error.ABIVersionError
         * if the language ABI version is incompatible with requirements
         */
        public Builder language(@NotNull Language language) {
            Language.validate(language);
            this.language = language;
            return this;
        }

        /**
         * Builds and returns a new Parser instance with the configured language.
         *
         * @return A new parser instance
         * @throws NullPointerException if the language was not previously set
         * @throws IncompatibleLanguageException if the language can not be set
         */
        public Parser build() {
            Objects.requireNonNull(language, "Language must not be null!");
            return build(language);
        }

        private static native Parser build(Language language);
    }

    /**
     * The latest ABI version that is supported by the current version of the library.
     * When languages are generated by the tree-sitter CLI,
     * they are assigned an ABI version number that
     * corresponds to the current CLI version.
     *
     * <p>
     * The tree-sitter library is generally backwards-compatible
     * with languages generated using older CLI versions,
     * but is not forwards-compatible.
     *
     * @return latest ABI version number
     * @since 1.6.0
     */
    public static native int getLanguageVersion();

    /**
     * The earliest ABI version that is supported
     * by the current version of the library.
     *
     * @return earliest supported ABI version number
     * @since 1.6.0
     */
    public static native int getMinimumCompatibleLanguageVersion();

    @Override
    protected native void delete();

    /**
     * Set the language that the parser should use for parsing.
     *
     * @param language The language used for parsing
     * @throws NullPointerException if the language is null
     * @throws UnsatisfiedLinkError if the language was not linked to native code
     * @throws ch.usi.si.seart.treesitter.error.ABIVersionError if the language ABI version is outdated
     * @throws IncompatibleLanguageException if the language can not be set
     */
    public void setLanguage(@NotNull Language language) {
        Language.validate(language);
        setLanguage(this, language);
    }

    private static native void setLanguage(Parser parser, Language language) throws IncompatibleLanguageException;

    /**
     * Get the duration in microseconds that parsing is allowed to take.
     *
     * @return the timeout duration set for parsing, 0 if it was not set
     * @since 1.1.0
     */
    public native long getTimeout();

    /**
     * Set the maximum duration that parsing should be allowed to take before halting.
     * If parsing takes longer than this, an exception is thrown.
     * Note that the supplied duration will be rounded down
     * to 0 if the duration is expressed in nanoseconds.
     *
     * @param duration the timeout duration
     * @throws NullPointerException if the duration is {@code null}
     * @since 1.1.0
     */
    public void setTimeout(@NotNull Duration duration) {
        Objects.requireNonNull(duration, "Duration must not be null!");
        long micros = duration.toMillis() * TimeUnit.MILLISECONDS.toMicros(1);
        setTimeout(micros);
    }

    /**
     * Set the maximum duration that parsing should be allowed to take before halting.
     * If parsing takes longer than this, an exception is thrown.
     * Note that the supplied duration will be rounded down
     * to 0 if the duration is expressed in nanoseconds.
     *
     * @param timeout the timeout duration amount
     * @param timeUnit the duration time unit
     * @throws NullPointerException if the time unit is {@code null}
     * @throws IllegalArgumentException if the timeout value is negative
     * @since 1.1.0
     */
    public void setTimeout(long timeout, @NotNull TimeUnit timeUnit) {
        if (timeout < 0)
            throw new IllegalArgumentException("Timeout can not be negative!");
        Objects.requireNonNull(timeUnit, "Time unit must not be null!");
        long micros = timeUnit.toMicros(timeout);
        setTimeout(micros);
    }

    /**
     * Set the maximum duration in microseconds that
     * parsing should be allowed to take before halting.
     * If parsing takes longer than this, an exception is thrown.
     *
     * @param timeout the timeout in microseconds
     * @throws IllegalArgumentException if the timeout value is negative
     * @since 1.1.0
     */
    public native void setTimeout(long timeout);

    /**
     * Use the parser to parse some source code and create a syntax tree.
     *
     * @param source The source code string to be parsed
     * @return A syntax tree matching the provided source
     * @throws ParsingException if a parsing failure occurs
     * @since 1.3.0
     */
    public Tree parse(@NotNull String source) throws ParsingException {
        byte[] bytes = source.getBytes(CHARSET);
        return parse(source, bytes, bytes.length, null);
    }

    /**
     * Use the parser to incrementally parse a changed source code string,
     * reusing unchanged parts of the tree to speed up the process.
     *
     * @param source The source code string to be parsed
     * @param oldTree The syntax tree before changes were made
     * @return A syntax tree matching the provided source
     * @throws ParsingException if a parsing failure occurs
     * @since 1.3.0
     */
    public Tree parse(@NotNull String source, @NotNull Tree oldTree) throws ParsingException {
        byte[] bytes = source.getBytes(CHARSET);
        return parse(source, bytes, bytes.length, oldTree);
    }

    /**
     * Use the parser to parse some source code found in a file at the specified path.
     *
     * @param path The path of the file to be parsed
     * @return A tree-sitter Tree matching the provided source
     * @throws ParsingException if a parsing failure occurs
     * @since 1.3.0
     */
    public Tree parse(@NotNull Path path) throws ParsingException {
        try {
            byte[] bytes = Files.readAllBytes(path);
            String source = new String(bytes);
            return parse(source);
        } catch (IOException ex) {
            throw new ParsingException(ex);
        }
    }

    /**
     * Use the parser to parse some source code found in a file at the specified path,
     * reusing unchanged parts of the tree to speed up the process.
     *
     * @param path The path of the file to be parsed
     * @param oldTree The syntax tree before changes were made
     * @return A tree-sitter Tree matching the provided source
     * @throws ParsingException if a parsing failure occurs
     * @since 1.3.0
     */
    public Tree parse(@NotNull Path path, @NotNull Tree oldTree) throws ParsingException {
        try {
            byte[] bytes = Files.readAllBytes(path);
            String source = new String(bytes);
            return parse(source, oldTree);
        } catch (IOException ex) {
            throw new ParsingException(ex);
        }
    }

    private native Tree parse(String source, byte[] bytes, int length, Tree oldTree);

    @Override
    @Generated
    public String toString() {
        return String.format("Parser(id: %d, language: %s)", pointer, language);
    }
}
