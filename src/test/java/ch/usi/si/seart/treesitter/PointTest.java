package ch.usi.si.seart.treesitter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PointTest {

    @Test
    void testIsOrigin() {
        Assertions.assertTrue(new Point(0, 0).isOrigin());
        Assertions.assertFalse(new Point(1, 0).isOrigin());
        Assertions.assertFalse(new Point(0, 1).isOrigin());
        Assertions.assertFalse(new Point(1, 1).isOrigin());
        Assertions.assertFalse(new Point(-1, 0).isOrigin());
        Assertions.assertFalse(new Point(0, -1).isOrigin());
        Assertions.assertFalse(new Point(-1, -1).isOrigin());
    }
}