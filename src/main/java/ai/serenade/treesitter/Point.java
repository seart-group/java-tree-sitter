package ai.serenade.treesitter;

public class Point {

    private final int row;
    private final int column;

    public Point(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    @Override
    public String toString() {
        return row + ":" + column;
    }
}
