package ch.usi.si.seart.treesitter;

import lombok.Cleanup;
import org.junit.jupiter.api.Test;

public abstract class AbstractQueryTest extends TestBase {

    protected Language getLanguage() {
        return Language.PYTHON;
    }

    protected String getSource() {
        return
                "@pure\n" +
                "@property\n" +
                "def foo(x):\n" +
                "    pass\n" +
                "\n" +
                "@pure\n" +
                "@property\n" +
                "def bar(x):\n" +
                "    pass\n";
    }

    protected String getPattern() {
        return "(_ (decorator)* @additional (function_definition) @target)";
    }

    protected abstract void assertions(Node node, Query query);

    @Test
    void test() {
        String source = getSource();
        Language language = getLanguage();
        @Cleanup Query query = Query.getFor(language, getPattern());
        @Cleanup Parser parser = Parser.getFor(language);
        @Cleanup Tree tree = parser.parse(source);
        Node root = tree.getRootNode();
        assertions(root, query);
    }
}
