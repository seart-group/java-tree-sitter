# java-tree-sitter

Java bindings for [tree-sitter](https://tree-sitter.github.io/tree-sitter/).
Originally developed by [serenadeai](https://github.com/serenadeai).

This fork was created with the purpose of more convenient integration into [DL4SE](https://github.com/seart-group/DL4SE), while including features introduced in [other forks](https://github.com/jakobkhansen/java-tree-sitter) and miscellaneous features absent from the original.

## Local development

Recursively clone the project with submodules:

```shell
git clone https://github.com/seart-group/java-tree-sitter.git --recursive
```

Or clone first and update the submodules then:

```shell   
git clone https://github.com/seart-group/java-tree-sitter.git
git submodule update --init --recursive  
# or:  git submodule init && git submodule update
```

## Building the dependency

To build the project locally, all one has to do is run the following:

```shell
mvn clean package
```

This will generate both the header files in `lib`, as well as the shared library produced by `build.py`.

## Adding a grammar

To add a new grammar, first create a submodule in the repository root:

```shell
git submodule add https://github.com/{{owner}}/tree-sitter-{{language}}.git tree-sitter-{{language}}
```

Once finished, check out the release whose version is **less than or equal** to the current `tree-sitter` release:
```shell
(cd tree-sitter-{{language}} && git checkout {{version}})
```

## Example usage

First, load the shared object somewhere in your application:

```java
public class App {
  static {
      LibraryLoader.load();
  }
}
```

Then you can create a `Parser` initialized to a `Language`, and use it to parse a string of source code:

```java
try (
        Parser parser = new Parser(Language.PYTHON);
        Tree tree = parser.parseString("def foo(bar, baz):\n  print(bar)\n  print(baz)")
) {
    Node root = tree.getRootNode();
    assertEquals(1, root.getChildCount());
    assertEquals("module", root.getType());
    assertEquals(0, root.getStartByte());
    assertEquals(44, root.getEndByte());
    Node function = root.getChild(0);
    assertEquals("function_definition", function.getType());
    assertEquals(5, function.getChildCount());
} catch (Exception ex) {
    // ...
}
```

For debugging, it can be helpful to see an S-Expression of the tree structure:

```java
try (
        Parser parser = new Parser(Language.PYTHON);
        Tree tree = parser.parseString("print(\"hi\")")
) {
    assertEquals(
        "(module (expression_statement (call function: (identifier) arguments: (argument_list (string)))))",
        tree.getRootNode().getNodeString()
    );
} catch (Exception ex) {
    // ...
}
```

For tree traversals use the `walk` method, as it is more efficient than the above getters:

```java
try (
        Parser parser = new Parser(Language.PYTHON);
        Tree tree = parser.parseString("def foo(bar, baz):\n  print(bar)\n  print(baz)");
        TreeCursor cursor = tree.getRootNode().walk()
) {
    assertEquals("module", cursor.getCurrentTreeCursorNode().getType());
    cursor.gotoFirstChild();
    assertEquals("function_definition", cursor.getCurrentTreeCursorNode().getType());
    cursor.gotoFirstChild();
    assertEquals("def", cursor.getCurrentTreeCursorNode().getType());
    cursor.gotoNextSibling();
    cursor.gotoParent();
} catch (Exception ex) {
    // ...
}
```

For more examples, take a look at the [tests](src/test/java/usi/si/seart/treesitter).
