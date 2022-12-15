package ai.serenade.treesitter;

public class InputEdit {

    private final int startByte;
    private final int oldEndByte;
    private final int newEndByte;

    private final Point startPoint;
    private final Point oldEndPoint;
    private final Point newEndPoint;


    public InputEdit(
            int startByte, int oldEndByte, int newEndByte, Point startPoint, Point oldEndPoint, Point newEndPoint
    ) {
        this.startByte = startByte;
        this.oldEndByte = oldEndByte;
        this.newEndByte = newEndByte;
        this.startPoint = startPoint;
        this.oldEndPoint = oldEndPoint;
        this.newEndPoint = newEndPoint;
    }

    public int getStartByte() {
        return startByte;
    }

    public int getOldEndByte() {
        return oldEndByte;
    }

    public int getNewEndByte() {
        return newEndByte;
    }

    public Point getStartPoint() {
        return startPoint;
    }

    public Point getOldEndPoint() {
        return oldEndPoint;
    }

    public Point getNewEndPoint() {
        return newEndPoint;
    }
}
