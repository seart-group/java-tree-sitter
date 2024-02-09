package ch.usi.si.seart.treesitter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class PointTest extends BaseTest {

    @Test
    void testIsOrigin() {
        Assertions.assertTrue(_0_0_.isOrigin());
    }

    @Test
    void testIsNotOrigin() {
        Assertions.assertFalse(_1_0_.isOrigin());
        Assertions.assertFalse(_0_1_.isOrigin());
        Assertions.assertFalse(_1_1_.isOrigin());
        Assertions.assertFalse(_2_2_.isOrigin());
    }

    @Test
    void testCompareTo() {
        List<Point> sorted = List.of(new Point(-1, -1), new Point(-1, 0), _0_0_, _0_1_, _1_0_, _1_1_);
        ArrayList<Point> unsorted = new ArrayList<>(sorted);
        Collections.shuffle(unsorted);
        Collections.sort(unsorted);
        Assertions.assertEquals(sorted, unsorted);
    }

    @Test
    void testCompareToThrows() {
        Assertions.assertThrows(NullPointerException.class, () -> _0_0_.compareTo(null));
    }

    @Test
    void testAdd() {
        Assertions.assertEquals(_1_1_, _0_0_.add(_1_1_));
        Assertions.assertEquals(_1_1_, _1_1_.add(_0_0_));
        Assertions.assertEquals(_0_0_, new Point(-1, -1).add(_1_1_));
        Assertions.assertEquals(_2_2_, _1_1_.add(_1_1_));
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
        Assertions.assertEquals(_2_2_, _1_1_.multiply(2));
    }

    @Test
    void testThrows() {
        Assertions.assertThrows(NullPointerException.class, () -> _0_0_.add(null));
        Assertions.assertThrows(NullPointerException.class, () -> _0_0_.subtract(null));
    }
}
