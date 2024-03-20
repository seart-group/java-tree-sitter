#include "ch_usi_si_seart_treesitter.h"
#include "ch_usi_si_seart_treesitter_Language.h"
#include <jni.h>
#include <string.h>
#include <tree_sitter/api.h>

JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_ada(
  JNIEnv* env, jclass self) {
#ifdef TS_LANGUAGE_ADA
  return (jlong)tree_sitter_ada();
#else
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
#endif
}

JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_arduino(
  JNIEnv* env, jclass self) {
#ifdef TS_LANGUAGE_ARDUINO
  return (jlong)tree_sitter_arduino();
#else
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
#endif
}

JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_bash(
  JNIEnv* env, jclass self) {
#ifdef TS_LANGUAGE_BASH
  return (jlong)tree_sitter_bash();
#else
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
#endif
}

JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_c(
  JNIEnv* env, jclass self) {
#ifdef TS_LANGUAGE_C
  return (jlong)tree_sitter_c();
#else
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
#endif
}

JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_cSharp(
  JNIEnv* env, jclass self) {
#ifdef TS_LANGUAGE_C_SHARP
  return (jlong)tree_sitter_c_sharp();
#else
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
#endif
}

JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_clojure(
  JNIEnv* env, jclass self) {
#ifdef TS_LANGUAGE_CLOJURE
  return (jlong)tree_sitter_clojure();
#else
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
#endif
}

JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_cmake(
  JNIEnv* env, jclass self) {
#ifdef TS_LANGUAGE_CMAKE
  return (jlong)tree_sitter_cmake();
#else
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
#endif
}

JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_cobol(
  JNIEnv* env, jclass self) {
#ifdef TS_LANGUAGE_COBOL
  return (jlong)tree_sitter_COBOL();
#else
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
#endif
}

JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_commonLisp(
  JNIEnv* env, jclass self) {
#ifdef TS_LANGUAGE_COMMON_LISP
  return (jlong)tree_sitter_commonlisp();
#else
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
#endif
}

JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_cpp(
  JNIEnv* env, jclass self) {
#ifdef TS_LANGUAGE_CPP
  return (jlong)tree_sitter_cpp();
#else
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
#endif
}

JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_css(
  JNIEnv* env, jclass self) {
#ifdef TS_LANGUAGE_CSS
  return (jlong)tree_sitter_css();
#else
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
#endif
}

JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_csv(
  JNIEnv* env, jclass self) {
#ifdef TS_LANGUAGE_CSV
  return (jlong)tree_sitter_csv();
#else
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
#endif
}

JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_dart(
  JNIEnv* env, jclass self) {
#ifdef TS_LANGUAGE_DART
  return (jlong)tree_sitter_dart();
#else
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
#endif
}

JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_dot(
  JNIEnv* env, jclass self) {
#ifdef TS_LANGUAGE_DOT
  return (jlong)tree_sitter_dot();
#else
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
#endif
}

JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_dtd(
  JNIEnv* env, jclass self) {
#ifdef TS_LANGUAGE_DTD
  return (jlong)tree_sitter_dtd();
#else
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
#endif
}

JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_elixir(
  JNIEnv* env, jclass self) {
#ifdef TS_LANGUAGE_ELIXIR
  return (jlong)tree_sitter_elixir();
#else
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
#endif
}

JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_elm(
  JNIEnv* env, jclass self) {
#ifdef TS_LANGUAGE_ELM
  return (jlong)tree_sitter_elm();
#else
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
#endif
}

JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_embeddedTemplate(
  JNIEnv* env, jclass self) {
#ifdef TS_LANGUAGE_EMBEDDED_TEMPLATE
  return (jlong)tree_sitter_embedded_template();
#else
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
#endif
}

JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_erlang(
  JNIEnv* env, jclass self) {
#ifdef TS_LANGUAGE_ERLANG
  return (jlong)tree_sitter_erlang();
#else
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
#endif
}

JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_fortran(
  JNIEnv* env, jclass self) {
#ifdef TS_LANGUAGE_FORTRAN
  return (jlong)tree_sitter_fortran();
#else
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
#endif
}

JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_gitattributes(
  JNIEnv* env, jclass self) {
#ifdef TS_LANGUAGE_GITATTRIBUTES
  return (jlong)tree_sitter_gitattributes();
#else
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
#endif
}

JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_gitignore(
  JNIEnv* env, jclass self) {
#ifdef TS_LANGUAGE_GITIGNORE
  return (jlong)tree_sitter_gitignore();
#else
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
#endif
}

JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_go(
  JNIEnv* env, jclass self) {
#ifdef TS_LANGUAGE_GO
  return (jlong)tree_sitter_go();
#else
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
#endif
}

JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_graphql(
  JNIEnv* env, jclass self) {
#ifdef TS_LANGUAGE_GRAPHQL
  return (jlong)tree_sitter_graphql();
#else
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
#endif
}

JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_haskell(
  JNIEnv* env, jclass self) {
#ifdef TS_LANGUAGE_HASKELL
  return (jlong)tree_sitter_haskell();
#else
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
#endif
}

JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_hcl(
  JNIEnv* env, jclass self) {
#ifdef TS_LANGUAGE_HCL
  return (jlong)tree_sitter_hcl();
#else
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
#endif
}

JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_html(
  JNIEnv* env, jclass self) {
#ifdef TS_LANGUAGE_HTML
  return (jlong)tree_sitter_html();
#else
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
#endif
}

JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_java(
  JNIEnv* env, jclass self) {
#ifdef TS_LANGUAGE_JAVA
  return (jlong)tree_sitter_java();
#else
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
#endif
}

JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_javascript(
  JNIEnv* env, jclass self) {
#ifdef TS_LANGUAGE_JAVASCRIPT
  return (jlong)tree_sitter_javascript();
#else
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
#endif
}

JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_json(
  JNIEnv* env, jclass self) {
#ifdef TS_LANGUAGE_JSON
  return (jlong)tree_sitter_json();
#else
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
#endif
}

JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_julia(
  JNIEnv* env, jclass self) {
#ifdef TS_LANGUAGE_JULIA
  return (jlong)tree_sitter_julia();
#else
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
#endif
}

JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_kotlin(
  JNIEnv* env, jclass self) {
#ifdef TS_LANGUAGE_KOTLIN
  return (jlong)tree_sitter_kotlin();
#else
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
#endif
}

JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_latex(
  JNIEnv* env, jclass self) {
#ifdef TS_LANGUAGE_LATEX
  return (jlong)tree_sitter_latex();
#else
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
#endif
}

JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_lua(
  JNIEnv* env, jclass self) {
#ifdef TS_LANGUAGE_LUA
  return (jlong)tree_sitter_lua();
#else
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
#endif
}

JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_markdown(
  JNIEnv* env, jclass self) {
#ifdef TS_LANGUAGE_MARKDOWN
  return (jlong)tree_sitter_markdown();
#else
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
#endif
}

JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_nix(
  JNIEnv* env, jclass self) {
#ifdef TS_LANGUAGE_NIX
  return (jlong)tree_sitter_nix();
#else
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
#endif
}

JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_objectiveC(
  JNIEnv* env, jclass self) {
#ifdef TS_LANGUAGE_OBJECTIVE_C
  return (jlong)tree_sitter_objc();
#else
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
#endif
}

JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_ocaml(
  JNIEnv* env, jclass self) {
#ifdef TS_LANGUAGE_OCAML
  return (jlong)tree_sitter_ocaml();
#else
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
#endif
}

JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_pascal(
  JNIEnv* env, jclass self) {
#ifdef TS_LANGUAGE_PASCAL
  return (jlong)tree_sitter_pascal();
#else
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
#endif
}

JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_php(
  JNIEnv* env, jclass self) {
#ifdef TS_LANGUAGE_PHP
  return (jlong)tree_sitter_php();
#else
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
#endif
}

JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_psv(
  JNIEnv* env, jclass self) {
#ifdef TS_LANGUAGE_PSV
  return (jlong)tree_sitter_psv();
#else
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
#endif
}

JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_python(
  JNIEnv* env, jclass self) {
#ifdef TS_LANGUAGE_PYTHON
  return (jlong)tree_sitter_python();
#else
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
#endif
}

JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_r(
  JNIEnv* env, jclass self) {
#ifdef TS_LANGUAGE_R
  return (jlong)tree_sitter_r();
#else
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
#endif
}

JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_racket(
  JNIEnv* env, jclass self) {
#ifdef TS_LANGUAGE_RACKET
  return (jlong)tree_sitter_racket();
#else
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
#endif
}

JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_ruby(
  JNIEnv* env, jclass self) {
#ifdef TS_LANGUAGE_RUBY
  return (jlong)tree_sitter_ruby();
#else
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
#endif
}

JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_rust(
  JNIEnv* env, jclass self) {
#ifdef TS_LANGUAGE_RUST
  return (jlong)tree_sitter_rust();
#else
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
#endif
}

JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_scala(
  JNIEnv* env, jclass self) {
#ifdef TS_LANGUAGE_SCALA
  return (jlong)tree_sitter_scala();
#else
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
#endif
}

JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_scheme(
  JNIEnv* env, jclass self) {
#ifdef TS_LANGUAGE_SCHEME
  return (jlong)tree_sitter_scheme();
#else
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
#endif
}

JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_scss(
  JNIEnv* env, jclass self) {
#ifdef TS_LANGUAGE_SCSS
  return (jlong)tree_sitter_scss();
#else
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
#endif
}

JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_svelte(
  JNIEnv* env, jclass self) {
#ifdef TS_LANGUAGE_SVELTE
  return (jlong)tree_sitter_svelte();
#else
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
#endif
}

JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_swift(
  JNIEnv* env, jclass self) {
#ifdef TS_LANGUAGE_SWIFT
  return (jlong)tree_sitter_swift();
#else
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
#endif
}

JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_thrift(
  JNIEnv* env, jclass self) {
#ifdef TS_LANGUAGE_THRIFT
  return (jlong)tree_sitter_thrift();
#else
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
#endif
}

JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_toml(
  JNIEnv* env, jclass self) {
#ifdef TS_LANGUAGE_TOML
  return (jlong)tree_sitter_toml();
#else
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
#endif
}

JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_tsv(
  JNIEnv* env, jclass self) {
#ifdef TS_LANGUAGE_TSV
  return (jlong)tree_sitter_tsv();
#else
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
#endif
}

JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_tsx(
  JNIEnv* env, jclass self) {
#ifdef TS_LANGUAGE_TSX
  return (jlong)tree_sitter_tsx();
#else
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
#endif
}

JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_twig(
  JNIEnv* env, jclass self) {
#ifdef TS_LANGUAGE_TWIG
  return (jlong)tree_sitter_twig();
#else
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
#endif
}

JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_typescript(
  JNIEnv* env, jclass self) {
#ifdef TS_LANGUAGE_TYPESCRIPT
  return (jlong)tree_sitter_typescript();
#else
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
#endif
}

JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_verilog(
  JNIEnv* env, jclass self) {
#ifdef TS_LANGUAGE_VERILOG
  return (jlong)tree_sitter_verilog();
#else
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
#endif
}

JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_xml(
  JNIEnv* env, jclass self) {
#ifdef TS_LANGUAGE_XML
  return (jlong)tree_sitter_xml();
#else
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
#endif
}

JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_yaml(
  JNIEnv* env, jclass self) {
#ifdef TS_LANGUAGE_YAML
  return (jlong)tree_sitter_yaml();
#else
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
#endif
}

JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_zig(
  JNIEnv* env, jclass self) {
#ifdef TS_LANGUAGE_ZIG
  return (jlong)tree_sitter_zig();
#else
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
#endif
}

JNIEXPORT jint JNICALL Java_ch_usi_si_seart_treesitter_Language_version(
  JNIEnv* env, jclass self, jlong id) {
  TSLanguage* language = (TSLanguage*)id;
  return (jint)ts_language_version(language);
}

JNIEXPORT jint JNICALL Java_ch_usi_si_seart_treesitter_Language_symbols(
  JNIEnv* env, jclass self, jlong id) {
  TSLanguage* language = (TSLanguage*)id;
  return (jint)ts_language_symbol_count(language);
}

JNIEXPORT jobject JNICALL Java_ch_usi_si_seart_treesitter_Language_symbol(
  JNIEnv* env, jclass self, jlong languageId, jint symbolId) {
  TSSymbol symbol = (TSSymbol)symbolId;
  TSLanguage* language = (TSLanguage*)languageId;
  const char* name = ts_language_symbol_name(language, symbol);
  TSSymbolType type = ts_language_symbol_type(language, symbol);
  return _newObject(
    _symbolClass,
    _symbolConstructor,
    (jint)symbol,
    (jint)type,
    env->NewStringUTF(name)
  );
}

JNIEXPORT jint JNICALL Java_ch_usi_si_seart_treesitter_Language_fields(
  JNIEnv* env, jclass self, jlong id) {
  TSLanguage* language = (TSLanguage*)id;
  return (jint)ts_language_field_count(language);
}

JNIEXPORT jstring JNICALL Java_ch_usi_si_seart_treesitter_Language_field(
  JNIEnv* env, jclass self, jlong languageId, jint fieldId) {
  TSLanguage* language = (TSLanguage*)languageId;
  TSFieldId field = (TSFieldId)fieldId;
  const char* name = ts_language_field_name_for_id(language, field);
  return env->NewStringUTF(name);
}

JNIEXPORT jint JNICALL Java_ch_usi_si_seart_treesitter_Language_states(
  JNIEnv* env, jclass self, jlong id) {
  TSLanguage* language = (TSLanguage*)id;
  return (jint)ts_language_state_count(language);
}

JNIEXPORT jobject JNICALL Java_ch_usi_si_seart_treesitter_Language_iterator(
  JNIEnv* env, jobject thisObject, jint state) {
  const TSLanguage* language = __unmarshalLanguage(env, thisObject);
  if (state < 0 || state >= ts_language_state_count(language)) {
    __throwIAE(env, "Invalid parse state!");
    return NULL;
  }
  TSLookaheadIterator* iterator = ts_lookahead_iterator_new(language, (TSStateId)state);
  if (iterator == NULL) {
    __throwISE(env, "Unable to create lookahead iterator!");
    return NULL;
  }
  jboolean hasNext = ts_lookahead_iterator_next(iterator) ? JNI_TRUE : JNI_FALSE;
  return _newObject(
    _lookaheadIteratorClass,
    _lookaheadIteratorConstructor,
    (jlong)iterator,
    hasNext,
    thisObject
  );
}

JNIEXPORT jint JNICALL Java_ch_usi_si_seart_treesitter_Language_nextState(
  JNIEnv* env, jclass self, jlong id, jint state, jint symbol) {
  return (id != ch_usi_si_seart_treesitter_Language_INVALID)
    ? ts_language_next_state((TSLanguage*)id, (TSStateId)state, (TSSymbol)symbol)
    : -1;
}
