package ch.usi.si.seart.treesitter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

class PointTest {

    @Test
    void testIsOrigin() {
        Assertions.assertTrue(new Point(0, 0).isOrigin());
    }

    @ParameterizedTest
    @MethodSource("provideNonOriginPoints")
    void testIsNotOrigin(Point point) {
        Assertions.assertFalse(point.isOrigin());
    }

    @Test
    void testCompareTo() {
        List<Point> sorted = List.of(
                new Point(0, 0), new Point(0, 1),
                new Point(1, 0), new Point(1, 1)
        );
        ArrayList<Point> unsorted = new ArrayList<>(sorted);
        Collections.shuffle(unsorted);
        Collections.sort(unsorted);
        Assertions.assertEquals(sorted, unsorted);
    }

    public static Stream<Arguments> provideNonOriginPoints() {
        return Stream.of(
                Arguments.of(new Point(1, 0)),
                Arguments.of(new Point(0, 1)),
                Arguments.of(new Point(1, 1)),
                Arguments.of(new Point(-1, 0)),
                Arguments.of(new Point(0, -1)),
                Arguments.of(new Point(-1, -1))
        );
    }
}