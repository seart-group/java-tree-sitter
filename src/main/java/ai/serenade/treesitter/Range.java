package ai.serenade.treesitter;

public class Range {

    private final int startByte;
    private final int endByte;
    private final Point startPoint;
    private final Point endPoint;

    Range(int startByte, int endByte, Point startPoint, Point endPoint) {
        this.startByte = startByte;
        this.endByte = endByte;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
    }

    public Range(Node node) {
        this(node.getStartByte(), node.getEndByte(), node.getStartPoint(), node.getEndPoint());
    }

    public int getStartByte() {
        return startByte;
    }

    public int getEndByte() {
        return endByte;
    }

    public Point getStartPoint() {
        return startPoint;
    }

    public Point getEndPoint() {
        return endPoint;
    }
}
