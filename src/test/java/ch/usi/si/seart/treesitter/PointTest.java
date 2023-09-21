package ch.usi.si.seart.treesitter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class PointTest {

    private static final Point _0_0_ = new Point(0, 0);
    private static final Point _1_1_ = new Point(1, 1);

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
    void testAdd() {
        Assertions.assertEquals(_1_1_, _0_0_.add(_1_1_));
        Assertions.assertEquals(_1_1_, _1_1_.add(_0_0_));
        Assertions.assertEquals(_0_0_, new Point(-1, -1).add(_1_1_));
        Assertions.assertEquals(new Point(2, 2), _1_1_.add(_1_1_));
    }

    @Test
    void testSubtract() {
        Assertions.assertEquals(_0_0_, _1_1_.subtract(_1_1_));
        Assertions.assertEquals(_1_1_, _1_1_.subtract(_0_0_));
        Assertions.assertEquals(new Point(-1, -1), _0_0_.subtract(_1_1_));
        Assertions.assertEquals(new Point(-2, -2), new Point(-1, -1).subtract(_1_1_));
    }

    @Test
    void testMultiply() {
        Assertions.assertEquals(_0_0_, _0_0_.multiply(2));
        Assertions.assertEquals(_0_0_, _1_1_.multiply(0));
        Assertions.assertEquals(_1_1_, _1_1_.multiply(1));
        Assertions.assertEquals(new Point(2, 2), _1_1_.multiply(2));
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