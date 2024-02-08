package ch.usi.si.seart.treesitter;

import ch.usi.si.seart.treesitter.exception.parser.IncompatibleLanguageException;
import ch.usi.si.seart.treesitter.exception.parser.ParsingException;
import lombok.AccessLevel;
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
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Parsers are stateful objects used to produce a {@link Tree} from some source code,
 * based on the rules outlined by the used {@link Language}.
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

    private static final String NULL_LANGUAGE = "Language must not be null!";
    private static final String NULL_DURATION = "Duration must not be null!";
    private static final String NULL_TIME_UNIT = "Time unit must not be null!";
    private static final String NEGATIVE_TIMEOUT = "Timeout must not be negative!";

    @SuppressWarnings("unused")
    Parser(long pointer, @NotNull Language language) {
        super(pointer);
        this.language = language;
    }

    /**
     * Static factory for obtaining new parser instances.
     *
     * @param language The language used for parsing
     * @return A new parser instance
     * @since 1.7.0
     */
    public static Parser getFor(@NotNull Language language) {
        return builder().language(language).build();
    }

    /**
     * Obtain a new {@link Builder Builder} for constructing a parser instance.
     *
     * @return a new parser builder
     * @since 1.7.0
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Obtain a new {@link Builder Builder} initialized with the current parser settings.
     *
     * @return a new parser builder
     * @since 1.8.0
     */
    public Builder toBuilder() {
        return builder().language(getLanguage()).timeout(getTimeout());
    }

    /**
     * Facilitates the construction of {@link Parser} instances.
     * It allows for the step-by-step creation of these objects
     * by providing methods for setting individual attributes.
     * Input validations are performed at each build step.
     *
     * @since 1.7.0
     */
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Builder {

        Language language = null;

        long timeout = 0L;

        /**
         * Sets the programming language intended for parsing.
         *
         * @param language the language used for parsing
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
         * Set the maximum duration that parsing should be allowed to take.
         * If parsing time exceeds this value, an exception is thrown.
         * The duration is rounded down to zero if it does not exceed one microsecond.
         * Timeouts are considered disabled when the value is zero.
         *
         * @param duration the timeout duration
         * @return this builder
         * @throws NullPointerException if the duration is {@code null}
         * @since 1.8.0
         */
        public Builder timeout(@NotNull Duration duration) {
            Objects.requireNonNull(duration, NULL_DURATION);
            if (duration.isZero()) return this;
            long micros = duration.toMillis() * TimeUnit.MILLISECONDS.toMicros(1);
            return timeout(micros);
        }

        /**
         * Set the maximum duration that parsing should be allowed to take.
         * If parsing time exceeds this value, an exception is thrown.
         * The duration is rounded down to zero if it does not exceed one microsecond.
         * Timeouts are considered disabled when the value is zero.
         *
         * @param timeout the timeout duration amount
         * @param timeUnit the duration time unit
         * @return this builder
         * @throws NullPointerException if the time unit is {@code null}
         * @throws IllegalArgumentException if the timeout value is negative
         * @since 1.8.0
         */
        public Builder timeout(long timeout, @NotNull TimeUnit timeUnit) {
            if (timeout == 0) return this;
            if (timeout < 0) throw new IllegalArgumentException(NEGATIVE_TIMEOUT);
            Objects.requireNonNull(timeUnit, NULL_TIME_UNIT);
            long micros = timeUnit.toMicros(timeout);
            return timeout(micros);
        }

        /**
         * Set the maximum duration in microseconds that parsing should be allowed to take.
         * If parsing time exceeds this value, an exception is thrown.
         * Timeouts are considered disabled when the value is zero.
         *
         * @param timeout the timeout in microseconds
         * @return this builder
         * @throws IllegalArgumentException if the timeout value is negative
         * @since 1.8.0
         */
        public Builder timeout(long timeout) {
            if (timeout == 0) return this;
            if (timeout < 0) throw new IllegalArgumentException(NEGATIVE_TIMEOUT);
            this.timeout = timeout;
            return this;
        }

        /**
         * Builds and returns a new Parser instance with the configured language.
         *
         * @return a new parser instance
         * @throws NullPointerException if the language was not previously set
         * @throws IncompatibleLanguageException if the language can not be set
         */
        public Parser build() {
            Objects.requireNonNull(language, NULL_LANGUAGE);
            return build(language, timeout);
        }

        private static native Parser build(Language language, long timeout);
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
     * @param language the language used for parsing
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
     * Get an ordered, immutable {@link Range} sequence that corresponds
     * to segments of source code included by the parser during parsing.
     * Returns an empty list when the included ranges were not set, i.e.
     * when the parser is configured to include the entire source code.
     *
     * @return the included text ranges
     * @since 1.12.0
     */
    public native List<Range> getIncludedRanges();

    /**
     * Specify a list of {@link Range} text segments that the parser should
     * include when parsing source code. This function allows you to parse
     * only a <em>portion</em> of a document, while yielding a syntax tree
     * whose ranges match up with the document as a whole. Although ranges
     * can be disjoint, they must be ordered from earliest to latest in the
     * source, and they must not overlap. In other words, the following must
     * hold for each {@code Range} at index {@code i}:
     * <pre>{@code
     * ranges[i].end_byte <= ranges[i + 1].start_byte
     * }</pre>
     * Passing an empty list will clear any previously set ranges,
     * causing the parser to include the entire source code.
     *
     * @param ranges the included text ranges
     * @throws NullPointerException
     * if the list is {@code null}
     * or contains {@code null} values
     * @since 1.12.0
     */
    public void setIncludedRanges(@NotNull List<@NotNull Range> ranges) {
        Objects.requireNonNull(ranges, "Ranges must not be null!");
        setIncludedRanges(ranges.toArray(Range[]::new));
    }

    /**
     * Specify an arbitrary number of {@link Range} text segments that the
     * parser should include when parsing source code. This function allows
     * you to parse only a <em>portion</em> of a document, while yielding a
     * syntax tree whose ranges match up with the document as a whole.
     * Although ranges can be disjoint, they must be ordered from earliest
     * to latest in the source, and they must not overlap. In other words,
     * the following must hold for each {@code Range} at index {@code i}:
     * <pre>{@code
     * ranges[i].end_byte <= ranges[i + 1].start_byte
     * }</pre>
     * Passing no arguments will clear any previously set ranges,
     * causing the parser to include the entire source code.
     *
     * @param ranges the included text ranges
     * @throws NullPointerException if any value is {@code null}
     * @since 1.12.0
     */
    public void setIncludedRanges(@NotNull Range... ranges) {
        for (Range range : ranges)
            Objects.requireNonNull(range, "Range must not be null!");
        setIncludedRanges(ranges, ranges.length);
    }

    private native void setIncludedRanges(Range[] ranges, int length);

    /**
     * Get the duration in microseconds that parsing is allowed to take.
     *
     * @return the timeout duration set for parsing, 0 if it was not set
     * @since 1.1.0
     */
    public native long getTimeout();

    /**
     * Set the maximum duration that parsing should be allowed to take.
     * If parsing time exceeds this value, an exception is thrown.
     * The duration is rounded down to zero if it does not exceed one microsecond.
     * Timeouts are considered disabled when the value is zero.
     *
     * @param duration the timeout duration
     * @throws NullPointerException if the duration is {@code null}
     * @since 1.1.0
     */
    public void setTimeout(@NotNull Duration duration) {
        Objects.requireNonNull(duration, NULL_DURATION);
        long micros = duration.toMillis() * TimeUnit.MILLISECONDS.toMicros(1);
        setTimeout(micros);
    }

    /**
     * Set the maximum duration that parsing should be allowed to take.
     * If parsing time exceeds this value, an exception is thrown.
     * The duration is rounded down to zero if it does not exceed one microsecond.
     * Timeouts are considered disabled when the value is zero.
     *
     * @param timeout the timeout duration amount
     * @param timeUnit the duration time unit
     * @throws NullPointerException if the time unit is {@code null}
     * @throws IllegalArgumentException if the timeout value is negative
     * @since 1.1.0
     */
    public void setTimeout(long timeout, @NotNull TimeUnit timeUnit) {
        if (timeout < 0) throw new IllegalArgumentException(NEGATIVE_TIMEOUT);
        Objects.requireNonNull(timeUnit, NULL_TIME_UNIT);
        long micros = timeUnit.toMicros(timeout);
        setTimeout(micros);
    }

    /**
     * Set the maximum duration in microseconds that parsing should be allowed to take.
     * If parsing time exceeds this value, an exception is thrown.
     * Timeouts are considered disabled when the value is zero.
     *
     * @param timeout the timeout in microseconds
     * @throws IllegalArgumentException if the timeout value is negative
     * @since 1.1.0
     */
    public native void setTimeout(long timeout);

    /**
     * Use the parser to parse some source code and create a syntax {@link Tree}.
     *
     * @param source the source code string to be parsed
     * @return a syntax tree matching the provided source
     * @throws ParsingException if a parsing failure occurs
     * @since 1.3.0
     */
    public Tree parse(@NotNull String source) throws ParsingException {
        byte[] bytes = source.getBytes(CHARSET);
        return parse(source, bytes, bytes.length, null);
    }

    /**
     * Use the parser to incrementally reparse a changed source code string,
     * re-using unchanged parts of the {@link Tree} to expedite the process.
     *
     * @param source the source code string to be parsed
     * @param oldTree the syntax tree before changes were made
     * @return a syntax tree matching the provided source
     * @throws ParsingException if a parsing failure occurs
     * @since 1.3.0
     */
    public Tree parse(@NotNull String source, @NotNull Tree oldTree) throws ParsingException {
        byte[] bytes = source.getBytes(CHARSET);
        return parse(source, bytes, bytes.length, oldTree);
    }

    /**
     * Use the parser to parse source code from a file.
     *
     * @param path the path of the file to be parsed
     * @return a syntax tree matching the provided source
     * @throws ParsingException if a parsing failure occurs
     * @since 1.3.0
     */
    public Tree parse(@NotNull Path path) throws ParsingException {
        try {
            String source = Files.readString(path);
            return parse(source);
        } catch (IOException ex) {
            throw new ParsingException(ex);
        }
    }

    /**
     * Use the parser to reparse source code from a file,
     * re-using unchanged parts of the {@link Tree} to expedite the process.
     *
     * @param path the path of the file to be parsed
     * @param oldTree the syntax tree before changes were made
     * @return a syntax tree matching the provided source
     * @throws ParsingException if a parsing failure occurs
     * @since 1.3.0
     */
    public Tree parse(@NotNull Path path, @NotNull Tree oldTree) throws ParsingException {
        try {
            String source = Files.readString(path);
            return parse(source, oldTree);
        } catch (IOException ex) {
            throw new ParsingException(ex);
        }
    }

    private native Tree parse(String source, byte[] bytes, int length, Tree oldTree);
}
