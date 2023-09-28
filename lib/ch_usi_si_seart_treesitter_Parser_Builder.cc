#include "ch_usi_si_seart_treesitter.h"
#include "ch_usi_si_seart_treesitter_Parser_Builder.h"
#include <jni.h>
#include <tree_sitter/api.h>

JNIEXPORT jobject JNICALL Java_ch_usi_si_seart_treesitter_Parser_00024Builder_build(
  JNIEnv* env, jclass thisClass, jobject languageObject) {
  TSParser* parser = ts_parser_new();
  const TSLanguage* language = __unmarshalLanguage(env, languageObject);
  bool succeeded = ts_parser_set_language(parser, language);
  if (!succeeded) {
    ts_parser_delete(parser);
    __throwILE(env, languageObject);
    return NULL;
  }
  return env->NewObject(
    _parserClass,
    _parserConstructor,
    (jlong)parser,
    languageObject
  );
}
