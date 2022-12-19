package ai.serenade.treesitter;

/**
 * Utility used for pretty-printing parse trees.
 */
public class TreePrinter {

    private int indentLevel = 0;
    private final Tree tree;

    public TreePrinter(Tree tree) {
        this.tree = tree;
    }

    /**
     * @return A string representation of the parse tree. Consists only of named nodes.
     */
    public String printParseTree() {
        StringBuilder stringBuilder = new StringBuilder();
        Node root = this.tree.getRootNode();
        try (TreeCursor cursor = root.walk()) {
            for (;;) {
                TreeCursorNode treeCursorNode = cursor.getCurrentTreeCursorNode();
                if (treeCursorNode.isNamed()) {
                    String treeNode = printTreeNode(treeCursorNode);
                    stringBuilder.append(treeNode);
                }
                if (cursor.gotoFirstChild()) {
                    indentLevel++;
                    continue;
                }
                if (cursor.gotoNextSibling()) {
                    continue;
                }
                do {
                    if (!cursor.gotoParent()) {
                        return stringBuilder.toString();
                    } else {
                        indentLevel--;
                    }
                } while (!cursor.gotoNextSibling());
            }
        }
    }

    private String printTreeNode(TreeCursorNode treeCursorNode) {
        String name = treeCursorNode.getName();
        String type = treeCursorNode.getType();
        Point startPoint = treeCursorNode.getStartPoint();
        Point endPoint = treeCursorNode.getEndPoint();
        return String.format("%s%s%s [%s] - [%s]%n",
                "  ".repeat(this.indentLevel),
                (name != null) ? name + ": " : "",
                type, startPoint, endPoint
        );
    }
}
