package ch.usi.si.seart.treesitter;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Enum that consists of all the officially recognized programming languages.
 *
 * <p>
 * In terms of ABI, these include only languages with a version of either 13 or 14.
 * The version of the underlying parser we use requires as a bare minimum the former.
 * For this reason, languages that have not been maintained for a significant
 * amount of time will not be supported by the library.
 *
 * @since 1.0.0
 * @author Ozren Dabić
 * @see <a href="https://tree-sitter.github.io/tree-sitter/#parsers">tree-sitter language list</a>
 */
@Getter(value = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum Language {

    /**
     * Represents an invalid language.
     * Used primarily for testing.
     */
    @TestOnly
    _INVALID_(),

    /**
     * Ada programming language.
     *
     * @see <a href="https://github.com/briot/tree-sitter-ada">tree-sitter-ada</a>
     */
    ADA(ada()),

    /**
     * Bash: Bourne Again SHell.
     *
     * @see <a href="https://github.com/tree-sitter/tree-sitter-bash">tree-sitter-bash</a>
     */
    BASH(bash()),

    /**
     * C programming language.
     *
     * @see <a href="https://github.com/tree-sitter/tree-sitter-c">tree-sitter-c</a>
     */
    C(c()),

    /**
     * Clojure programming language.
     *
     * @see <a href="https://github.com/sogaiu/tree-sitter-clojure">tree-sitter-clojure</a>
     */
    CLOJURE(clojure()),

    /**
     * The CMake language.
     *
     * @see <a href="https://github.com/uyha/tree-sitter-cmake">tree-sitter-cmake</a>
     */
    CMAKE(cMake()),

    /**
     * Common Lisp programming language.
     *
     * @see <a href="https://github.com/theHamsta/tree-sitter-commonlisp">tree-sitter-commonlisp</a>
     */
    COMMON_LISP(commonLisp()),

    /**
     * C# programming language.
     *
     * @see <a href="https://github.com/tree-sitter/tree-sitter-c-sharp">tree-sitter-c-sharp</a>
     */
    CSHARP(cSharp()),

    /**
     * C++ programming language.
     *
     * @see <a href="https://github.com/tree-sitter/tree-sitter-cpp">tree-sitter-cpp</a>
     */
    CPP(cpp()),

    /**
     * CSS: Cascading Style Sheets.
     *
     * @see <a href="https://github.com/tree-sitter/tree-sitter-css">tree-sitter-css</a>
     */
    CSS(css()),

    /**
     * Dart programming language.
     *
     * @see <a href="https://github.com/UserNobody14/tree-sitter-dart">tree-sitter-dart</a>
     */
    DART(dart()),

    /**
     * Docker image specifications.
     *
     * @see <a href="https://github.com/camdencheek/tree-sitter-dockerfile">tree-sitter-dockerfile</a>
     */
    DOCKERFILE(dockerfile()),

    /**
     * DOT graph description language.
     *
     * @see <a href="https://github.com/rydesun/tree-sitter-dot">tree-sitter-dot</a>
     */
    DOT(dot()),

    /**
     * Elixir programming language.
     *
     * @see <a href="https://github.com/elixir-lang/tree-sitter-elixir">tree-sitter-elixir</a>
     */
    ELIXIR(elixir()),

    /**
     * Elm programming language.
     *
     * @see <a href="https://github.com/elm-tooling/tree-sitter-elm">tree-sitter-elm</a>
     */
    ELM(elm()),

    /**
     * Embedded HTML templates: EJS &amp; ERS.
     *
     * @see <a href="https://github.com/tree-sitter/tree-sitter-embedded-template">tree-sitter-embedded-template</a>
     */
    EMBEDDED_TEMPLATE(embeddedTemplate()),

    /**
     * Erlang programming language.
     *
     * @see <a href="https://github.com/WhatsApp/tree-sitter-erlang">tree-sitter-erlang</a>
     */
    ERLANG(erlang()),

    /**
     * Fortran programming language.
     *
     * @see <a href="https://github.com/stadelmanma/tree-sitter-fortran">tree-sitter-fortran</a>
     */
    FORTRAN(fortran()),

    /**
     * Go programming language.
     *
     * @see <a href="https://github.com/tree-sitter/tree-sitter-go">tree-sitter-go</a>
     */
    GO(go()),

    /**
     * GraphQL: Graph Query Language.
     *
     * @see <a href="https://github.com/bkegley/tree-sitter-graphql">tree-sitter-graphql</a>
     */
    GRAPHQL(graphQl()),

    /**
     * Haskell programming language.
     *
     * @see <a href="https://github.com/tree-sitter/tree-sitter-haskell">tree-sitter-haskell</a>
     */
    HASKELL(haskell()),

    /**
     * HTML: HyperText Markup Language.
     *
     * @see <a href="https://github.com/tree-sitter/tree-sitter-html">tree-sitter-html</a>
     */
    HTML(html()),

    /**
     * Java programming language.
     *
     * @see <a href="https://github.com/tree-sitter/tree-sitter-java">tree-sitter-java</a>
     */
    JAVA(java()),

    /**
     * JavaScript programming language.
     *
     * @see <a href="https://github.com/tree-sitter/tree-sitter-javascript">tree-sitter-javascript</a>
     */
    JAVASCRIPT(javascript()),

    /**
     * JSON: JavaScript Object Notation.
     *
     * @see <a href="https://github.com/tree-sitter/tree-sitter-json">tree-sitter-json</a>
     */
    JSON(json()),

    /**
     * Julia programming language.
     *
     * @see <a href="https://github.com/tree-sitter/tree-sitter-julia">tree-sitter-julia</a>
     */
    JULIA(julia()),

    /**
     * Kotlin programming language.
     *
     * @see <a href="https://github.com/fwcd/tree-sitter-kotlin">tree-sitter-kotlin</a>
     */
    KOTLIN(kotlin()),

    /**
     * LaTeX markup language for document typesetting.
     *
     * @see <a href="https://github.com/latex-lsp/tree-sitter-latex">tree-sitter-latex</a>
     */
    LATEX(latex()),

    /**
     * Lua programming language.
     *
     * @see <a href="https://github.com/Azganoth/tree-sitter-lua">tree-sitter-lua</a>
     */
    LUA(lua()),

    /**
     * Markdown markup language for creating formatted text.
     *
     * @see <a href="https://github.com/MDeiml/tree-sitter-markdown">tree-sitter-markdown</a>
     */
    MARKDOWN(markdown()),

    /**
     * Nix programming language.
     *
     * @see <a href="https://github.com/nix-community/tree-sitter-nix">tree-sitter-nix</a>
     */
    NIX(nix()),

    /**
     * Objective-C programming language.
     *
     * @see <a href="https://github.com/jiyee/tree-sitter-objc">tree-sitter-objc</a>
     */
    OBJECTIVE_C(objectiveC()),

    /**
     * OCaml programming language.
     *
     * @see <a href="https://github.com/tree-sitter/tree-sitter-ocaml">tree-sitter-ocaml</a>
     */
    OCAML(ocaml()),

    /**
     * Pascal programming language.
     *
     * @see <a href="https://github.com/Isopod/tree-sitter-pascal">tree-sitter-pascal</a>
     */
    PASCAL(pascal()),

    /**
     * PHP: Hypertext Preprocessor.
     *
     * @see <a href="https://github.com/tree-sitter/tree-sitter-php">tree-sitter-php</a>
     */
    PHP(php()),

    /**
     * Python programming language.
     *
     * @see <a href="https://github.com/tree-sitter/tree-sitter-python">tree-sitter-python</a>
     */
    PYTHON(python()),

    /**
     * R programming language.
     *
     * @see <a href="https://github.com/r-lib/tree-sitter-r">tree-sitter-r</a>
     */
    R(r()),

    /**
     * Racket programming language.
     *
     * @see <a href="https://github.com/6cdh/tree-sitter-racket">tree-sitter-racket</a>
     */
    RACKET(racket()),

    /**
     * Ruby programming language.
     *
     * @see <a href="https://github.com/tree-sitter/tree-sitter-ruby">tree-sitter-ruby</a>
     */
    RUBY(ruby()),

    /**
     * Rust programming language.
     *
     * @see <a href="https://github.com/tree-sitter/tree-sitter-rust">tree-sitter-rust</a>
     */
    RUST(rust()),

    /**
     * Scala programming language.
     *
     * @see <a href="https://github.com/tree-sitter/tree-sitter-scala">tree-sitter-scala</a>
     */
    SCALA(scala()),

    /**
     * Scheme programming language.
     *
     * @see <a href="https://github.com/6cdh/tree-sitter-scheme">tree-sitter-scheme</a>
     */
    SCHEME(scheme()),

    /**
     * SCSS: Sassy CSS.
     *
     * @see <a href="https://github.com/serenadeai/tree-sitter-scss">tree-sitter-scss</a>
     */
    SCSS(scss()),

    /**
     * Svelte front-end component framework.
     *
     * @see <a href="https://github.com/Himujjal/tree-sitter-svelte">tree-sitter-svelte</a>
     */
    SVELTE(svelte()),

    /**
     * Swift programming language.
     *
     * @see <a href="https://github.com/alex-pinkus/tree-sitter-swift">tree-sitter-swift</a>
     */
    SWIFT(swift()),

    /**
     * TOML: Tom's Obvious Minimal Language.
     *
     * @see <a href="https://github.com/tree-sitter/tree-sitter-toml">tree-sitter-toml</a>
     */
    TOML(toml()),

    /**
     * JSX-enhanced TypeScript.
     *
     * @see <a href="https://github.com/tree-sitter/tree-sitter-typescript">tree-sitter-typescript</a>
     */
    TSX(tsx()),

    /**
     * TypeScript programming language.
     *
     * @see <a href="https://github.com/tree-sitter/tree-sitter-typescript">tree-sitter-typescript</a>
     */
    TYPESCRIPT(typescript()),

    /**
     * YAML: YAML Ain't Markup Language.
     *
     * @see <a href="https://github.com/ikatyang/tree-sitter-yaml">tree-sitter-yaml</a>
     */
    YAML(yaml());

    private static native long ada();
    private static native long bash();
    private static native long c();
    private static native long clojure();
    private static native long commonLisp();
    private static native long cMake();
    private static native long cSharp();
    private static native long cpp();
    private static native long css();
    private static native long dart();
    private static native long dockerfile();
    private static native long dot();
    private static native long elixir();
    private static native long elm();
    private static native long embeddedTemplate();
    private static native long erlang();
    private static native long fortran();
    private static native long go();
    private static native long graphQl();
    private static native long haskell();
    private static native long html();
    private static native long java();
    private static native long javascript();
    private static native long json();
    private static native long julia();
    private static native long kotlin();
    private static native long latex();
    private static native long lua();
    private static native long markdown();
    private static native long nix();
    private static native long objectiveC();
    private static native long ocaml();
    private static native long pascal();
    private static native long php();
    private static native long python();
    private static native long ruby();
    private static native long r();
    private static native long racket();
    private static native long rust();
    private static native long scala();
    private static native long scheme();
    private static native long scss();
    private static native long svelte();
    private static native long swift();
    private static native long toml();
    private static native long tsx();
    private static native long typescript();
    private static native long yaml();

    public static void validate(@NotNull Language language) {
        Objects.requireNonNull(language, "Language must not be null!");
        long id = language.getId();
        if (id == Language.INVALID) throw new UnsatisfiedLinkError(
                "Language binding has not been defined for: " + language
        );
    }

    long id;

    private static final long INVALID = 0L;

    Language() {
        this(INVALID);
    }

    @Override
    public String toString() {
        switch (this) {
            /*
             * Uppercase
             */
            case C:
            case CSS:
            case DOT:
            case HTML:
            case JSON:
            case PHP:
            case R:
            case SCSS:
            case TOML:
            case TSX:
            case YAML:
                return name();
            /*
             * Capital Case
             */
            case ADA:
            case BASH:
            case CLOJURE:
            case DART:
            case DOCKERFILE:
            case ELIXIR:
            case ELM:
            case ERLANG:
            case FORTRAN:
            case GO:
            case HASKELL:
            case JAVA:
            case JULIA:
            case KOTLIN:
            case LUA:
            case MARKDOWN:
            case NIX:
            case PASCAL:
            case PYTHON:
            case RACKET:
            case RUBY:
            case RUST:
            case SCALA:
            case SCHEME:
            case SVELTE:
            case SWIFT:
                return capitalize(name());
            /*
             * Space-Delimited Capital Case
             */
            case COMMON_LISP:
            case EMBEDDED_TEMPLATE:
                String[] parts = name().split("_");
                return Stream.of(parts)
                        .map(Language::capitalize)
                        .collect(Collectors.joining(" "));
            /*
             * Special Cases
             */
            case CMAKE: return "CMake";
            case CSHARP: return "C#";
            case CPP: return "C++";
            case GRAPHQL: return "GraphQL";
            case JAVASCRIPT: return "JavaScript";
            case LATEX: return "LaTeX";
            case OBJECTIVE_C: return "Objective-C";
            case OCAML: return "OCaml";
            case TYPESCRIPT: return "TypeScript";
            /*
             * Fallback
             */
            default:
                return "???";
        }
    }

    private static String capitalize(String name) {
        return name.charAt(0) + name.substring(1).toLowerCase();
    }
}
