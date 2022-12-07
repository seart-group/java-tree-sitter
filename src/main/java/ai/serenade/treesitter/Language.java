package ai.serenade.treesitter;

import java.util.function.LongSupplier;

public enum Language {
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

    private final long id;

    Language(LongSupplier supplier) {
        long id = 0L;
        try {
            id = supplier.getAsLong();
        } catch (UnsatisfiedLinkError ignored) {
            // Triggered whenever a language is not included in the native library.
        } finally {
            this.id = id;
        }
    }

    public long getId() {
        return id;
    }
}
