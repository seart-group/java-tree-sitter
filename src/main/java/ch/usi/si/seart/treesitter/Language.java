package ch.usi.si.seart.treesitter;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.Objects;

/**
 * Enum that consists of all the officially recognized programming language mappings.
 *
 * @since 1.0.0
 * @author Ozren Dabić
 */
@Getter(value = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum Language {

    /**
     * Represents an invalid language.
     * Used primarily for testing.
     */
    _INVALID_(),

    /**
     * Agda language mapping.
     *
     * @see <a href="https://github.com/tree-sitter/tree-sitter-agda">tree-sitter-agda</a>
     */
    AGDA(agda()),

    /**
     * Bash language mapping.
     *
     * @see <a href="https://github.com/tree-sitter/tree-sitter-bash">tree-sitter-bash</a>
     */
    BASH(bash()),

    /**
     * C language mapping.
     *
     * @see <a href="https://github.com/tree-sitter/tree-sitter-c">tree-sitter-c</a>
     */
    C(c()),

    /**
     * C# language mapping.
     *
     * @see <a href="https://github.com/tree-sitter/tree-sitter-c-sharp">tree-sitter-c-sharp</a>
     */
    CSHARP(cSharp()),

    /**
     * C++ language mapping.
     *
     * @see <a href="https://github.com/tree-sitter/tree-sitter-cpp">tree-sitter-cpp</a>
     */
    CPP(cpp()),

    /**
     * CSS language mapping.
     *
     * @see <a href="https://github.com/tree-sitter/tree-sitter-css">tree-sitter-css</a>
     */
    CSS(css()),

    /**
     * Dart language mapping.
     *
     * @see <a href="https://github.com/UserNobody14/tree-sitter-dart">tree-sitter-dart</a>
     */
    DART(dart()),

    /**
     * Elm language mapping.
     *
     * @see <a href="https://github.com/elm-tooling/tree-sitter-elm">tree-sitter-elm</a>
     */
    ELM(elm()),

    /**
     * Embedded template (EJS &amp; ERS) language mapping.
     *
     * @see <a href="https://github.com/tree-sitter/tree-sitter-embedded-template">tree-sitter-embedded-template</a>
     */
    EMBEDDED_TEMPLATE(embeddedTemplate()),

    /**
     * Eno language mapping.
     *
     * @see <a href="https://github.com/eno-lang/tree-sitter-eno">tree-sitter-eno</a>
     */
    ENO(eno()),

    /**
     * Go language mapping.
     *
     * @see <a href="https://github.com/tree-sitter/tree-sitter-go">tree-sitter-go</a>
     */
    GO(go()),

    /**
     * Haskell language mapping.
     *
     * @see <a href="https://github.com/tree-sitter/tree-sitter-haskell">tree-sitter-haskell</a>
     */
    HASKELL(haskell()),

    /**
     * HTML language mapping.
     *
     * @see <a href="https://github.com/tree-sitter/tree-sitter-html">tree-sitter-html</a>
     */
    HTML(html()),

    /**
     * Java language mapping.
     *
     * @see <a href="https://github.com/tree-sitter/tree-sitter-java">tree-sitter-java</a>
     */
    JAVA(java()),

    /**
     * JavaScript language mapping.
     *
     * @see <a href="https://github.com/tree-sitter/tree-sitter-javascript">tree-sitter-javascript</a>
     */
    JAVASCRIPT(javascript()),

    /**
     * Julia language mapping.
     *
     * @see <a href="https://github.com/tree-sitter/tree-sitter-julia">tree-sitter-julia</a>
     */
    JULIA(julia()),

    /**
     * Kotlin language mapping.
     *
     * @see <a href="https://github.com/fwcd/tree-sitter-kotlin">tree-sitter-kotlin</a>
     */
    KOTLIN(kotlin()),

    /**
     * Lua language mapping.
     *
     * @see <a href="https://github.com/Azganoth/tree-sitter-lua">tree-sitter-lua</a>
     */
    LUA(lua()),

    /**
     * Markdown language mapping.
     *
     * @see <a href="https://github.com/MDeiml/tree-sitter-markdown">tree-sitter-markdown</a>
     */
    MARKDOWN(markdown()),

    /**
     * OCaml language mapping.
     *
     * @see <a href="https://github.com/tree-sitter/tree-sitter-ocaml">tree-sitter-ocaml</a>
     */
    OCAML(ocaml()),

    /**
     * PHP language mapping.
     *
     * @see <a href="https://github.com/tree-sitter/tree-sitter-php">tree-sitter-php</a>
     */
    PHP(php()),

    /**
     * Python language mapping.
     *
     * @see <a href="https://github.com/tree-sitter/tree-sitter-python">tree-sitter-python</a>
     */
    PYTHON(python()),

    /**
     * R language mapping.
     *
     * @see <a href="https://github.com/r-lib/tree-sitter-r">tree-sitter-r</a>
     */
    R(r()),

    /**
     * Ruby language mapping.
     *
     * @see <a href="https://github.com/tree-sitter/tree-sitter-ruby">tree-sitter-ruby</a>
     */
    RUBY(ruby()),

    /**
     * Rust language mapping.
     *
     * @see <a href="https://github.com/tree-sitter/tree-sitter-rust">tree-sitter-rust</a>
     */
    RUST(rust()),

    /**
     * Scala language mapping.
     *
     * @see <a href="https://github.com/tree-sitter/tree-sitter-scala">tree-sitter-scala</a>
     */
    SCALA(scala()),

    /**
     * SCSS language mapping.
     *
     * @see <a href="https://github.com/serenadeai/tree-sitter-scss">tree-sitter-scss</a>
     */
    SCSS(scss()),

    /**
     * Svelte language mapping.
     *
     * @see <a href="https://github.com/Himujjal/tree-sitter-svelte">tree-sitter-svelte</a>
     */
    SVELTE(svelte()),

    /**
     * Swift language mapping.
     *
     * @see <a href="https://github.com/alex-pinkus/tree-sitter-swift">tree-sitter-swift</a>
     */
    SWIFT(swift()),

    /**
     * TOML language mapping.
     *
     * @see <a href="https://github.com/tree-sitter/tree-sitter-toml">tree-sitter-toml</a>
     */
    TOML(toml()),

    /**
     * JSX-enhanced TypeScript language mapping.
     */
    TSX(tsx()),

    /**
     * TypeScript language mapping.
     *
     * @see <a href="https://github.com/tree-sitter/tree-sitter-typescript">tree-sitter-typescript</a>
     */
    TYPESCRIPT(typescript()),

    /**
     * Vue language mapping.
     *
     * @see <a href="https://github.com/ikatyang/tree-sitter-vue">tree-sitter-vue</a>
     */
    VUE(vue()),

    /**
     * YAML language mapping.
     *
     * @see <a href="https://github.com/ikatyang/tree-sitter-yaml">tree-sitter-yaml</a>
     */
    YAML(yaml()),

    /**
     * WebAssembly language mapping.
     *
     * @see <a href="https://github.com/wasm-lsp/tree-sitter-wasm">tree-sitter-wasm</a>
     */
    WASM(wasm());

    private static native long agda();
    private static native long bash();
    private static native long c();
    private static native long cSharp();
    private static native long cpp();
    private static native long css();
    private static native long dart();
    private static native long elm();
    private static native long embeddedTemplate();
    private static native long eno();
    private static native long go();
    private static native long haskell();
    private static native long html();
    private static native long java();
    private static native long javascript();
    private static native long julia();
    private static native long kotlin();
    private static native long lua();
    private static native long markdown();
    private static native long ocaml();
    private static native long php();
    private static native long python();
    private static native long ruby();
    private static native long r();
    private static native long rust();
    private static native long scala();
    private static native long scss();
    private static native long svelte();
    private static native long swift();
    private static native long toml();
    private static native long tsx();
    private static native long typescript();
    private static native long vue();
    private static native long yaml();
    private static native long wasm();

    public static void validate(Language language) {
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
            // Unmodified
            case C:
            case CSS:
            case HTML:
            case PHP:
            case R:
            case SCSS:
            case TOML:
            case TSX:
            case YAML:
                return name();

            // Capital Case
            case AGDA:
            case BASH:
            case DART:
            case ELM:
            case ENO:
            case GO:
            case HASKELL:
            case JAVA:
            case JULIA:
            case KOTLIN:
            case LUA:
            case MARKDOWN:
            case PYTHON:
            case RUBY:
            case RUST:
            case SCALA:
            case SVELTE:
            case SWIFT:
            case VUE:
                return name().charAt(0) + name().substring(1).toLowerCase();

            // Special Cases
            case CSHARP: return "C#";
            case CPP: return "C++";
            case EMBEDDED_TEMPLATE: return "Embedded Template";
            case JAVASCRIPT: return "JavaScript";
            case OCAML: return "OCaml";
            case TYPESCRIPT: return "TypeScript";
            case WASM: return "WebAssembly";

            // Default / Undefined
            default:
                return "???";
        }
    }
}
