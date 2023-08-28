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

## Building dependency locally

To build the project for development purposes, all one has to do is run the following:

```shell
mvn clean package
```

This will generate both the header files in `lib`, as well as the shared library produced by `build.py`.
For it to work, you must have the following installed:

| Dependency | Version |
|:-----------|--------:|
| Java       |      11 |
| Maven      |    3.9+ |
| Python     |     3.9 |
| Docker     |     23+ |

## Adding dependency to project

To use in your own Maven project, include the following in your POM file:

```xml
<dependency>
  <groupId>ch.usi.si.seart</groupId>
  <artifactId>java-tree-sitter</artifactId>
  <version>1.0.2</version>
</dependency>
```

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
import ch.usi.si.seart.treesitter.*;

public class Example {
  static {
      LibraryLoader.load();
  }
}
```

Then you can create a `Parser` initialized to a `Language`, and use it to parse a string of source code:

```java
import ch.usi.si.seart.treesitter.*;

public class Example {
    
    // init omitted...

    public static void main(String[] args) {
        try (
            Parser parser = new Parser(Language.PYTHON);
            Tree tree = parser.parseString("def foo(bar, baz):\n  print(bar)\n  print(baz)")
        ) {
            Node root = tree.getRootNode();
            assert root.getChildCount() == 1;
            assert root.getType().equals("module");
            assert root.getStartByte() == 0;
            assert root.getEndByte() == 44;
            Node function = root.getChild(0);
            assert function.getType().equals("function_definition");
            assert function.getChildCount() == 5;
        } catch (Exception ex) {
            // ...
        }
    }
}
```

For debugging, it can be helpful to see a [symbolic expression](https://en.wikipedia.org/wiki/S-expression) representation of the tree structure:

```java
import ch.usi.si.seart.treesitter.*;

public class Example {

    // init omitted...

    public static void main(String[] args) {
        try (
            Parser parser = new Parser(Language.PYTHON);
            Tree tree = parser.parseString("print(\"hi\")")
        ) {
            String actual = tree.getRootNode().getNodeString();
            String expected = "(module (expression_statement (call function: (identifier) arguments: (argument_list (string)))))";
            assert expected.equals(actual);
        } catch (Exception ex) {
            // ...
        }
    }
}
```

We also provide a way to print the syntax tree, similar to the [online playground](https://tree-sitter.github.io/tree-sitter/playground):

```java
import ch.usi.si.seart.treesitter.*;

public class Example {

    // init omitted...

    public static void main(String[] args) {
        try (
            Parser parser = new Parser(Language.PYTHON);
            Tree tree = parser.parseString("print(\"hi\")")
        ) {
            SyntaxTreePrinter printer = new SyntaxTreePrinter(tree.getRootNode());
            String actual = printer.printSubtree();
            String expected =
                "module [0:0] - [0:11]\n" +
                "  expression_statement [0:0] - [0:11]\n" +
                "    call [0:0] - [0:11]\n" +
                "      function: identifier [0:0] - [0:5]\n" +
                "      arguments: argument_list [0:5] - [0:11]\n" +
                "        string [0:6] - [0:10]\n";
            assert expected.equals(actual);
        } catch (Exception ex) {
            // ...
        }
    }
}
```

Use `TreeCursor` instances to traverse trees, as it is more efficient than both manual traversal, and through `Node` iterators:

```java
import ch.usi.si.seart.treesitter.*;

public class Example {

    // init omitted...

    public static void main(String[] args) {
        String type;
        try (
            Parser parser = new Parser(Language.PYTHON);
            Tree tree = parser.parseString("def foo(bar, baz):\n  print(bar)\n  print(baz)");
            TreeCursor cursor = tree.getRootNode().walk()
        ) {
            type = cursor.getCurrentTreeCursorNode().getType();
            assert type.equals("module");
            cursor.gotoFirstChild();
            type = cursor.getCurrentTreeCursorNode().getType();
            assert type.equals("function_definition");
            cursor.gotoFirstChild();
            type = cursor.getCurrentTreeCursorNode().getType();
            assert type.equals("def");
            cursor.gotoNextSibling();
            cursor.gotoParent();
        } catch (Exception ex) {
            // ...
        }
    }
}
```

For more usage examples, take a look at the [tests](src/test/java/usi/si/seart/treesitter).
