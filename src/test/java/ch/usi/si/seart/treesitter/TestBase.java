package ch.usi.si.seart.treesitter;

public abstract class TestBase {

    protected final Node empty = new Node();
    protected final Node treeless = new Node(1, 1, 1, 1, 1L, null);
    protected final Point _0_0_ = new Point(0, 0);
    protected final Point _0_1_ = new Point(0, 1);
    protected final Point _1_0_ = new Point(1, 0);
    protected final Point _1_1_ = new Point(1, 1);
    protected final Point _2_2_ = new Point(2, 2);

    static {
        LibraryLoader.load();
    }
}
