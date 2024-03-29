package ch.usi.si.seart.treesitter;

import ch.usi.si.seart.treesitter.exception.parser.IncompatibleLanguageException;
import ch.usi.si.seart.treesitter.exception.parser.ParsingException;
import ch.usi.si.seart.treesitter.version.TreeSitter;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
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

    private static final String NULL_RANGE = "Range must not be null!";
    private static final String NULL_RANGES = "Ranges must not be null!";
    private static final String NULL_LANGUAGE = "Language must not be null!";
    private static final String NULL_DURATION = "Duration must not be null!";
    private static final String NULL_TIME_UNIT = "Time unit must not be null!";
    private static final String NEGATIVE_TIMEOUT = "Timeout must not be negative!";
    private static final String NEGATIVE_DURATION = "Duration must not be negative!";
    private static final String OVERLAPPING_RANGES = "Ranges must not overlap!";

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
        return builder()
                .language(getLanguage())
                .timeout(getTimeout())
                .ranges(getIncludedRanges());
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

        List<Range> ranges = new ArrayList<>();

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
            if (duration.isNegative()) throw new IllegalArgumentException(NEGATIVE_DURATION);
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
            if (timeout < 0) throw new IllegalArgumentException(NEGATIVE_TIMEOUT);
            this.timeout = timeout;
            return this;
        }

        /**
         * Sets the collection of ranges that the parser will include when parsing.
         * Although ranges can be disjoint, they must be ordered from earliest to
         * latest in the source, and they must not overlap. In other words,
         * the following must hold for each {@code Range} at index {@code i}:
         * <pre>{@code
         * ranges[i].end_byte <= ranges[i + 1].start_byte
         * }</pre>
         * This method <em>will overwrite</em> all the currently specified ranges.
         *
         * @param ranges the included text ranges
         * @return this builder
         * @throws NullPointerException if the ranges list is {@code null},
         * or contains {@code null} elements
         * @since 1.12.0
         */
        public Builder ranges(@NotNull List<@NotNull Range> ranges) {
            Objects.requireNonNull(ranges, NULL_RANGES);
            this.ranges = new ArrayList<>(List.copyOf(ranges));
            return this;
        }

        /**
         * Specify additional ranges that the parser should include when parsing.
         * The added ranges can be disjoint, both with respect to the already specified
         * ranges, and among themselves. At the same time, the union of the already
         * specified ranges and the added ranges must satisfy the range overlap invariant.
         * In other words, the following must hold for each {@code Range} at index {@code i}:
         * <pre>{@code
         * ranges[i].end_byte <= ranges[i + 1].start_byte
         * }</pre>
         *
         * @param ranges the included text ranges
         * @return this builder
         * @throws NullPointerException if the range sequence is {@code null},
         * or contains {@code null} elements
         * @since 1.12.0
         */
        public Builder ranges(@NotNull Range... ranges) {
            Objects.requireNonNull(ranges, NULL_RANGES);
            for (Range range: ranges) range(range);
            return this;
        }

        /**
         * Specify an additional range that the parser should include when parsing.
         * The added range can be disjoint with respect to the already specified ranges.
         * At the same time, the union of the already specified ranges and the added range
         * must satisfy the range overlap invariant. In other words, the following must
         * hold for each {@code Range} at index {@code i}:
         * <pre>{@code
         * ranges[i].end_byte <= ranges[i + 1].start_byte
         * }</pre>
         *
         * @param range the included text range
         * @return this builder
         * @throws NullPointerException if the range is {@code null}
         * @since 1.12.0
         */
        public Builder range(@NotNull Range range) {
            Objects.requireNonNull(range, NULL_RANGE);
            ranges.add(range);
            return this;
        }

        /**
         * Removes all currently specified ranges.
         *
         * @return this builder
         * @since 1.12.0
         */
        public Builder range() {
            ranges.clear();
            return this;
        }

        /**
         * Builds and returns a new Parser instance with the configured language.
         *
         * @return a new parser instance
         * @throws NullPointerException if the language was not previously set
         * @throws IllegalArgumentException if the range overlap invariant is violated
         * @throws IncompatibleLanguageException if the language can not be set
         */
        public Parser build() {
            Objects.requireNonNull(language, NULL_LANGUAGE);
            Range[] array = validated(ranges.toArray(Range[]::new));
            return build(language, timeout, array, array.length);
        }

        private static native Parser build(Language language, long timeout, Range[] ranges, int length);
    }

    /**
     * @deprecated Use {@link TreeSitter#getCurrentABIVersion()}
     */
    @Deprecated(since = "1.12.0", forRemoval = true)
    public static int getLanguageVersion() {
        return TreeSitter.getCurrentABIVersion();
    }

    /**
     * @deprecated Use {@link TreeSitter#getMinimumABIVersion()}
     */
    @Deprecated(since = "1.12.0", forRemoval = true)
    public static int getMinimumCompatibleLanguageVersion() {
        return TreeSitter.getMinimumABIVersion();
    }

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
     * Get the {@link Logger} instance used by the parser
     * for writing debugging information during parsing.
     *
     * @return the logger used by the parser
     * @since 1.12.0
     */
    public native Logger getLogger();

    /**
     * Set the {@link Logger} that a parser should use for
     * writing debugging information during parsing.
     * To disable logging, pass {@code null} as an argument.
     * <p>
     * By default, the parser will use the {@code DEBUG} level
     * with a dedicated {@link org.slf4j.Marker Marker} for
     * either the {@code PARSE} or {@code LEX} events.
     *
     * @param logger the logger used by the parser
     * @since 1.12.0
     */
    public native void setLogger(Logger logger);

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
     * @throws IllegalArgumentException
     * if the range overlap invariant is violated
     * @since 1.12.0
     */
    public void setIncludedRanges(@NotNull List<@NotNull Range> ranges) {
        Objects.requireNonNull(ranges, NULL_RANGES);
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
     * @throws IllegalArgumentException
     * if the range overlap invariant is violated
     * @since 1.12.0
     */
    public void setIncludedRanges(@NotNull Range... ranges) {
        Objects.requireNonNull(ranges, NULL_RANGES);
        setIncludedRanges(validated(ranges), ranges.length);
    }

    private static Range[] validated(Range[] ranges) {
        switch (ranges.length) {
            case 0: break;
            case 1: Objects.requireNonNull(ranges[0], NULL_RANGE);
                    break;
            default:
                Range[] left = Arrays.copyOfRange(ranges, 0, ranges.length - 1);
                Range[] right = Arrays.copyOfRange(ranges, 1, ranges.length);
                for (int i = 0; i < ranges.length - 1; i++) {
                    Objects.requireNonNull(left[i], NULL_RANGE);
                    Objects.requireNonNull(right[i], NULL_RANGE);
                    if (left[i].getEndByte() > right[i].getStartByte())
                        throw new IllegalArgumentException(OVERLAPPING_RANGES);
                }
        }
        return ranges;
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
