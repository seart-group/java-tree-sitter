package ai.serenade.treesitter;

public class TreeSitterException extends RuntimeException {

    public TreeSitterException(String message) {
        super(message);
    }

    public TreeSitterException(Throwable cause) {
        super(cause);
    }

    public TreeSitterException(String message, Throwable cause) {
        super(message, cause);
    }
}
