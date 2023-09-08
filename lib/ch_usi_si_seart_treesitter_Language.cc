#include "ch_usi_si_seart_treesitter_Language.h"
#include <jni.h>
#include <string.h>
#include <tree_sitter/api.h>

#ifdef TS_LANGUAGE_AGDA
extern "C" TSLanguage* tree_sitter_agda();
JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_agda(JNIEnv* env, jclass self) {
  return (jlong)tree_sitter_agda();
}
#else
JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_agda(JNIEnv* env, jclass self) {
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
}
#endif

#ifdef TS_LANGUAGE_BASH
extern "C" TSLanguage* tree_sitter_bash();
JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_bash(JNIEnv* env, jclass self) {
  return (jlong)tree_sitter_bash();
}
#else
JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_bash(JNIEnv* env, jclass self) {
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
}
#endif

#ifdef TS_LANGUAGE_C
extern "C" TSLanguage* tree_sitter_c();
JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_c(JNIEnv* env, jclass self) {
  return (jlong)tree_sitter_c();
}
#else
JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_c(JNIEnv* env, jclass self) {
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
}
#endif

#ifdef TS_LANGUAGE_C_SHARP
extern "C" TSLanguage* tree_sitter_c_sharp();
JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_cSharp(JNIEnv* env, jclass self) {
  return (jlong)tree_sitter_c_sharp();
}
#else
JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_cSharp(JNIEnv* env, jclass self) {
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
}
#endif

#ifdef TS_LANGUAGE_CPP
extern "C" TSLanguage* tree_sitter_cpp();
JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_cpp(JNIEnv* env, jclass self) {
  return (jlong)tree_sitter_cpp();
}
#else
JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_cpp(JNIEnv* env, jclass self) {
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
}
#endif

#ifdef TS_LANGUAGE_CSS
extern "C" TSLanguage* tree_sitter_css();
JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_css(JNIEnv* env, jclass self) {
  return (jlong)tree_sitter_css();
}
#else
JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_css(JNIEnv* env, jclass self) {
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
}
#endif

#ifdef TS_LANGUAGE_DART
extern "C" TSLanguage* tree_sitter_dart();
JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_dart(JNIEnv* env, jclass self) {
  return (jlong)tree_sitter_dart();
}
#else
JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_dart(JNIEnv* env, jclass self) {
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
}
#endif

#ifdef TS_LANGUAGE_ELM
extern "C" TSLanguage* tree_sitter_elm();
JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_elm(JNIEnv* env, jclass self) {
  return (jlong)tree_sitter_elm();
}
#else
JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_elm(JNIEnv* env, jclass self) {
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
}
#endif

#ifdef TS_LANGUAGE_EMBEDDED_TEMPLATE
extern "C" TSLanguage* tree_sitter_embedded_template();
JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_embeddedTemplate(JNIEnv* env, jclass self) {
  return (jlong)tree_sitter_embedded_template();
}
#else
JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_embeddedTemplate(JNIEnv* env, jclass self) {
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
}
#endif

#ifdef TS_LANGUAGE_ENO
extern "C" TSLanguage* tree_sitter_eno();
JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_eno(JNIEnv* env, jclass self) {
  return (jlong)tree_sitter_eno();
}
#else
JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_eno(JNIEnv* env, jclass self) {
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
}
#endif

#ifdef TS_LANGUAGE_GO
extern "C" TSLanguage* tree_sitter_go();
JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_go(JNIEnv* env, jclass self) {
  return (jlong)tree_sitter_go();
}
#else
JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_go(JNIEnv* env, jclass self) {
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
}
#endif

#ifdef TS_LANGUAGE_GRAPHQL
extern "C" TSLanguage* tree_sitter_graphql();
JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_graphQl(JNIEnv* env, jclass self) {
  return (jlong)tree_sitter_graphql();
}
#else
JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_graphQl(JNIEnv* env, jclass self) {
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
}
#endif

#ifdef TS_LANGUAGE_HASKELL
extern "C" TSLanguage* tree_sitter_haskell();
JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_haskell(JNIEnv* env, jclass self) {
  return (jlong)tree_sitter_haskell();
}
#else
JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_haskell(JNIEnv* env, jclass self) {
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
}
#endif

#ifdef TS_LANGUAGE_HTML
extern "C" TSLanguage* tree_sitter_html();
JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_html(JNIEnv* env, jclass self) {
  return (jlong)tree_sitter_html();
}
#else
JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_html(JNIEnv* env, jclass self) {
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
}
#endif

#ifdef TS_LANGUAGE_JAVA
extern "C" TSLanguage* tree_sitter_java();
JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_java(JNIEnv* env, jclass self) {
  return (jlong)tree_sitter_java();
}
#else
JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_java(JNIEnv* env, jclass self) {
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
}
#endif

#ifdef TS_LANGUAGE_JAVASCRIPT
extern "C" TSLanguage* tree_sitter_javascript();
JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_javascript(JNIEnv* env, jclass self) {
  return (jlong)tree_sitter_javascript();
}
#else
JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_javascript(JNIEnv* env, jclass self) {
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
}
#endif

#ifdef TS_LANGUAGE_JSON
extern "C" TSLanguage* tree_sitter_json();
JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_json(JNIEnv* env, jclass self) {
  return (jlong)tree_sitter_json();
}
#else
JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_json(JNIEnv* env, jclass self) {
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
}
#endif

#ifdef TS_LANGUAGE_JULIA
extern "C" TSLanguage* tree_sitter_julia();
JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_julia(JNIEnv* env, jclass self) {
  return (jlong)tree_sitter_julia();
}
#else
JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_julia(JNIEnv* env, jclass self) {
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
}
#endif

#ifdef TS_LANGUAGE_KOTLIN
extern "C" TSLanguage* tree_sitter_kotlin();
JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_kotlin(JNIEnv* env, jclass self) {
  return (jlong)tree_sitter_kotlin();
}
#else
JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_kotlin(JNIEnv* env, jclass self) {
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
}
#endif

#ifdef TS_LANGUAGE_LUA
extern "C" TSLanguage* tree_sitter_lua();
JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_lua(JNIEnv* env, jclass self) {
  return (jlong)tree_sitter_lua();
}
#else
JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_lua(JNIEnv* env, jclass self) {
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
}
#endif

#ifdef TS_LANGUAGE_MARKDOWN
extern "C" TSLanguage* tree_sitter_markdown();
JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_markdown(JNIEnv* env, jclass self) {
  return (jlong)tree_sitter_markdown();
}
#else
JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_markdown(JNIEnv* env, jclass self) {
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
}
#endif

#ifdef TS_LANGUAGE_NIX
extern "C" TSLanguage* tree_sitter_nix();
JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_nix(JNIEnv* env, jclass self) {
  return (jlong)tree_sitter_nix();
}
#else
JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_nix(JNIEnv* env, jclass self) {
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
}
#endif

#ifdef TS_LANGUAGE_OBJECTIVE_C
extern "C" TSLanguage* tree_sitter_objc();
JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_objectiveC(JNIEnv* env, jclass self) {
  return (jlong)tree_sitter_objc();
}
#else
JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_objectiveC(JNIEnv* env, jclass self) {
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
}
#endif

#ifdef TS_LANGUAGE_OCAML
extern "C" TSLanguage* tree_sitter_ocaml();
JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_ocaml(JNIEnv* env, jclass self) {
  return (jlong)tree_sitter_ocaml();
}
#else
JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_ocaml(JNIEnv* env, jclass self) {
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
}
#endif

#ifdef TS_LANGUAGE_PHP
extern "C" TSLanguage* tree_sitter_php();
JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_php(JNIEnv* env, jclass self) {
  return (jlong)tree_sitter_php();
}
#else
JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_php(JNIEnv* env, jclass self) {
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
}
#endif

#ifdef TS_LANGUAGE_PYTHON
extern "C" TSLanguage* tree_sitter_python();
JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_python(JNIEnv* env, jclass self) {
  return (jlong)tree_sitter_python();
}
#else
JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_python(JNIEnv* env, jclass self) {
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
}
#endif

#ifdef TS_LANGUAGE_R
extern "C" TSLanguage* tree_sitter_r();
JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_r(JNIEnv* env, jclass self) {
  return (jlong)tree_sitter_r();
}
#else
JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_r(JNIEnv* env, jclass self) {
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
}
#endif

#ifdef TS_LANGUAGE_RUBY
extern "C" TSLanguage* tree_sitter_ruby();
JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_ruby(JNIEnv* env, jclass self) {
  return (jlong)tree_sitter_ruby();
}
#else
JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_ruby(JNIEnv* env, jclass self) {
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
}
#endif

#ifdef TS_LANGUAGE_RUST
extern "C" TSLanguage* tree_sitter_rust();
JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_rust(JNIEnv* env, jclass self) {
  return (jlong)tree_sitter_rust();
}
#else
JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_rust(JNIEnv* env, jclass self) {
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
}
#endif

#ifdef TS_LANGUAGE_SCALA
extern "C" TSLanguage* tree_sitter_scala();
JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_scala(JNIEnv* env, jclass self) {
  return (jlong)tree_sitter_scala();
}
#else
JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_scala(JNIEnv* env, jclass self) {
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
}
#endif

#ifdef TS_LANGUAGE_SCHEME
extern "C" TSLanguage* tree_sitter_scheme();
JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_scheme(JNIEnv* env, jclass self) {
  return (jlong)tree_sitter_scheme();
}
#else
JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_scheme(JNIEnv* env, jclass self) {
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
}
#endif

#ifdef TS_LANGUAGE_SCSS
extern "C" TSLanguage* tree_sitter_scss();
JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_scss(JNIEnv* env, jclass self) {
  return (jlong)tree_sitter_scss();
}
#else
JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_scss(JNIEnv* env, jclass self) {
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
}
#endif

#ifdef TS_LANGUAGE_SVELTE
extern "C" TSLanguage* tree_sitter_svelte();
JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_svelte(JNIEnv* env, jclass self) {
  return (jlong)tree_sitter_svelte();
}
#else
JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_svelte(JNIEnv* env, jclass self) {
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
}
#endif

#ifdef TS_LANGUAGE_SWIFT
extern "C" TSLanguage* tree_sitter_swift();
JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_swift(JNIEnv* env, jclass self) {
  return (jlong)tree_sitter_swift();
}
#else
JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_swift(JNIEnv* env, jclass self) {
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
}
#endif

#ifdef TS_LANGUAGE_TOML
extern "C" TSLanguage* tree_sitter_toml();
JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_toml(JNIEnv* env, jclass self) {
  return (jlong)tree_sitter_toml();
}
#else
JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_toml(JNIEnv* env, jclass self) {
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
}
#endif

#ifdef TS_LANGUAGE_TSX
extern "C" TSLanguage* tree_sitter_tsx();
JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_tsx(JNIEnv* env, jclass self) {
  return (jlong)tree_sitter_tsx();
}
#else
JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_tsx(JNIEnv* env, jclass self) {
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
}
#endif

#ifdef TS_LANGUAGE_TYPESCRIPT
extern "C" TSLanguage* tree_sitter_typescript();
JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_typescript(JNIEnv* env, jclass self) {
  return (jlong)tree_sitter_typescript();
}
#else
JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_typescript(JNIEnv* env, jclass self) {
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
}
#endif

#ifdef TS_LANGUAGE_VUE
extern "C" TSLanguage* tree_sitter_vue();
JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_vue(JNIEnv* env, jclass self) {
  return (jlong)tree_sitter_vue();
}
#else
JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_vue(JNIEnv* env, jclass self) {
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
}
#endif

#ifdef TS_LANGUAGE_YAML
extern "C" TSLanguage* tree_sitter_yaml();
JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_yaml(JNIEnv* env, jclass self) {
  return (jlong)tree_sitter_yaml();
}
#else
JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_yaml(JNIEnv* env, jclass self) {
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
}
#endif

#ifdef TS_LANGUAGE_WASM
extern "C" TSLanguage* tree_sitter_wasm();
JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_wasm(JNIEnv* env, jclass self) {
  return (jlong)tree_sitter_wasm();
}
#else
JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Language_wasm(JNIEnv* env, jclass self) {
  return (jlong)ch_usi_si_seart_treesitter_Language_INVALID;
}
#endif
