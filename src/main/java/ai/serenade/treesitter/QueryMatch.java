package ai.serenade.treesitter;

public class QueryMatch {

    private final int id;
    private final int pattern_index;
    private final QueryCapture[] captures;

    public QueryMatch(int id, int pattern_index, QueryCapture[] captures) {
        this.id = id;
        this.pattern_index = pattern_index;
        this.captures = captures;
    }

    public int getId() {
        return id;
    }

    public int getPatternIndex() {
        return pattern_index;
    }

    public QueryCapture[] getCaptures() {
        return captures;
    }
}
