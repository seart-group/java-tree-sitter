package usi.si.seart.treesitter;

import lombok.Getter;

import java.util.function.LongSupplier;

public enum Language {

    _INVALID_(),
    AGDA(Languages::agda),
    BASH(Languages::bash),
    C(Languages::c),
    CSHARP(Languages::cSharp),
    CPP(Languages::cpp),
    CSS(Languages::css),
    DART(Languages::dart),
    ELM(Languages::elm),
    EMBEDDED_TEMPLATE(Languages::embeddedTemplate),
    ENO(Languages::eno),
    GO(Languages::go),
    HASKELL(Languages::haskell),
    HTML(Languages::html),
    JAVA(Languages::java),
    JAVASCRIPT(Languages::javascript),
    JULIA(Languages::julia),
    KOTLIN(Languages::kotlin),
    LUA(Languages::lua),
    MARKDOWN(Languages::markdown),
    OCAML(Languages::ocaml),
    PHP(Languages::php),
    PYTHON(Languages::python),
    RUBY(Languages::ruby),
    RUST(Languages::rust),
    SCALA(Languages::scala),
    SCSS(Languages::scss),
    SWIFT(Languages::swift),
    TOML(Languages::toml),
    TSX(Languages::tsx),
    TYPESCRIPT(Languages::typescript),
    VUE(Languages::vue),
    YAML(Languages::yaml),
    WASM(Languages::wasm);

    @Getter
    private final long id;

    static final long INVALID = 0L;

    Language() {
        this.id = INVALID;
    }

    Language(LongSupplier supplier) {
        long id = INVALID;
        try {
            id = supplier.getAsLong();
        } catch (UnsatisfiedLinkError ignored) {
            // Triggered whenever a language is not included in the native library.
        } finally {
            this.id = id;
        }
    }


    @Override
    public String toString() {
        switch (this) {
            // Unmodified
            case C:
            case CSS:
            case HTML:
            case PHP:
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
