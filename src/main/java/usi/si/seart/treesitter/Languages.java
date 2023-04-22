package usi.si.seart.treesitter;

import lombok.experimental.UtilityClass;

import java.util.Objects;

@UtilityClass
class Languages {
    static native long agda();
    static native long bash();
    static native long c();
    static native long cSharp();
    static native long cpp();
    static native long css();
    static native long dart();
    static native long elm();
    static native long embeddedTemplate();
    static native long eno();
    static native long go();
    static native long haskell();
    static native long html();
    static native long java();
    static native long javascript();
    static native long julia();
    static native long kotlin();
    static native long lua();
    static native long markdown();
    static native long ocaml();
    static native long php();
    static native long python();
    static native long ruby();
    static native long rust();
    static native long scala();
    static native long scss();
    static native long swift();
    static native long toml();
    static native long tsx();
    static native long typescript();
    static native long vue();
    static native long yaml();
    static native long wasm();

    static void validate(Language language) {
        Objects.requireNonNull(language, "Language must not be null!");
        long id = language.getId();
        if (id == Language.INVALID) throw new UnsatisfiedLinkError(
                "Language binding has not been defined for: " + language
        );
    }
}
