# java-tree-sitter

Java bindings for [tree-sitter](https://tree-sitter.github.io/tree-sitter/).
Originally developed by [serenadeai](https://github.com/serenadeai).

This fork was originally created to simplify integration into [DL4SE](https://github.com/seart-group/DL4SE).
Along the way, the project evolved to include useful features from [other forks](https://github.com/jakobkhansen/java-tree-sitter),
while introducing support for those that were completely absent from the original project.
Highlights include:

- Incremental abstract syntax tree edits
- APIs for querying parsed abstract syntax trees
- Support for both macOS and Linux out of the box
- A wide range of languages [supported](.gitmodules) out of the box
- Streamlined native library construction, packaging and runtime loading
- Safer interop with native code to minimize risks of segmentation faults
- Direct mapping between the abstract syntax tree nodes and the source code contents
- Multiple export formats: DOT, XML, [symbolic expression](https://en.wikipedia.org/wiki/S-expression) and human-readable syntax trees
- Various other quality-of-life improvements

Our end-goal is to offer all features that are available in the official tree-sitter bindings,
features that one might expect from [py-tree-sitter](https://github.com/tree-sitter/py-tree-sitter) or
[node-tree-sitter](https://github.com/tree-sitter/node-tree-sitter).

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
  <version>1.5.1</version>
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
            Tree tree = parser.parse("def foo(bar, baz):\n  print(bar)\n  print(baz)")
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

Use `TreeCursor` instances to traverse trees, as it is more efficient than both manual traversal, and through `Node` iterators:

```java
import ch.usi.si.seart.treesitter.*;

public class Example {

    // init omitted...

    public static void main(String[] args) {
        String type;
        try (
            Parser parser = new Parser(Language.PYTHON);
            Tree tree = parser.parse("def foo(bar, baz):\n  print(bar)\n  print(baz)");
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

We also provide a way to print the syntax tree, similar to the [online playground](https://tree-sitter.github.io/tree-sitter/playground):

```java
import ch.usi.si.seart.treesitter.*;
import ch.usi.si.seart.treesitter.printer.*;

public class Example {

    // init omitted...

    public static void main(String[] args) {
        try (
            Parser parser = new Parser(Language.PYTHON);
            Tree tree = parser.parse("print(\"hi\")");
            TreeCursor cursor = tree.getRootNode().walk()
        ) {
            SyntaxTreePrinter printer = new SyntaxTreePrinter(cursor);
            String actual = printer.print();
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

For more usage examples, take a look at the [tests](src/test/java/usi/si/seart/treesitter).
