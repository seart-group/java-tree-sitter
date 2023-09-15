package ch.usi.si.seart.treesitter;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

class TreeCursorTest extends TestBase {

    private static Parser parser;
    private static Tree tree;

    private TreeCursor cursor;

    @BeforeAll
    static void beforeAll() {
        parser = new Parser(Language.PYTHON);
        tree = parser.parse("def foo(bar, baz):\n  print(bar)\n  print(baz)");
    }

    @BeforeEach
    void setUp() {
        cursor = tree.getRootNode().walk();
    }

    @AfterEach
    void tearDown() {
        cursor.close();
    }

    @AfterAll
    static void afterAll() {
        tree.close();
        parser.close();
    }

    @Test
    void testWalk() {
        Assertions.assertEquals("module", cursor.getCurrentTreeCursorNode().getType());
        Assertions.assertEquals("module", cursor.getCurrentNode().getType());
        Assertions.assertTrue(cursor.gotoFirstChild());
        Assertions.assertEquals("function_definition", cursor.getCurrentNode().getType());
        Assertions.assertTrue(cursor.gotoFirstChild());

        Assertions.assertEquals("def", cursor.getCurrentNode().getType());
        Assertions.assertTrue(cursor.gotoNextSibling());
        Assertions.assertEquals("identifier", cursor.getCurrentNode().getType());
        Assertions.assertTrue(cursor.gotoNextSibling());
        Assertions.assertEquals("parameters", cursor.getCurrentNode().getType());
        Assertions.assertTrue(cursor.gotoNextSibling());
        Assertions.assertEquals(":", cursor.getCurrentNode().getType());
        Assertions.assertTrue(cursor.gotoNextSibling());
        Assertions.assertEquals("block", cursor.getCurrentNode().getType());
        Assertions.assertEquals("body", cursor.getCurrentFieldName());
        Assertions.assertFalse(cursor.gotoNextSibling());

        Assertions.assertTrue(cursor.gotoParent());
        Assertions.assertEquals("function_definition", cursor.getCurrentNode().getType());
        Assertions.assertTrue(cursor.gotoFirstChild());
    }

    @Test
    void testPreorderTraversal() {
        AtomicInteger count = new AtomicInteger();
        cursor.preorderTraversal(node -> {
            if (node.isNamed())
                count.incrementAndGet();
        });
        Assertions.assertEquals(17, count.get());
    }

    @SuppressWarnings({"resource", "DataFlowIssue"})
    private static class WalkExceptionProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
            return Stream.of(
                    Arguments.of(NullPointerException.class, (Executable) () -> new TreeCursor(null)),
                    Arguments.of(IllegalArgumentException.class, (Executable) () -> new Node().walk())
            );
        }
    }

    @ParameterizedTest(name = "[{index}] {0}")
    @ArgumentsSource(WalkExceptionProvider.class)
    void testWalkThrows(Class<Throwable> throwableType, Executable executable) {
        Assertions.assertThrows(throwableType, executable);
    }
}
