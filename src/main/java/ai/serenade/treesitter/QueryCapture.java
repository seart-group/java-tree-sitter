package ai.serenade.treesitter;

public class QueryCapture {

    private final Node node;
    private final int index;

    public QueryCapture(Node node, int index) {
        this.node = node;
        this.index = index;
    }

    public Node getNode() {
        return node;
    }

    public int getIndex() {
        return index;
    }
}
