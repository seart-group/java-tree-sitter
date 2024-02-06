package ch.usi.si.seart.treesitter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

class LookaheadIteratorTest extends BaseTest {

    private static final Language language = Language.JAVA;

    @Test
    void testIterator() {
        try (LookaheadIterator iterator = language.iterator(0)) {
            Spliterator<Symbol> spliterator = Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED);
            List<Symbol> symbols = StreamSupport.stream(spliterator, false).collect(Collectors.toUnmodifiableList());
            Assertions.assertFalse(symbols.isEmpty());
            Assertions.assertTrue(language.getSymbols().containsAll(symbols));
            Assertions.assertThrows(NoSuchElementException.class, iterator::next);
        } catch (NoSuchElementException ignored) {
            Assertions.fail();
        }
    }

    @Test
    @SuppressWarnings("resource")
    void testIteratorThrows() {
        int states = language.getTotalStates();
        Assertions.assertThrows(IllegalArgumentException.class, () -> language.iterator(Integer.MIN_VALUE));
        Assertions.assertThrows(IllegalArgumentException.class, () -> language.iterator(-1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> language.iterator(states));
        Assertions.assertThrows(IllegalArgumentException.class, () -> language.iterator(Integer.MAX_VALUE));
    }
}
