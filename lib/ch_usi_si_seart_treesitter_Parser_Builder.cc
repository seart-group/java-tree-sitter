#include "ch_usi_si_seart_treesitter.h"
#include "ch_usi_si_seart_treesitter_Parser_Builder.h"
#include <jni.h>
#include <tree_sitter/api.h>

JNIEXPORT jobject JNICALL Java_ch_usi_si_seart_treesitter_Parser_00024Builder_build(
  JNIEnv* env, jclass thisClass, jobject languageObject, jlong timeout, jobjectArray rangeObjectArray, jint length) {
  TSParser* parser = ts_parser_new();
  const TSLanguage* language = __unmarshalLanguage(env, languageObject);
  if (!ts_parser_set_language(parser, language)) {
    ts_parser_delete(parser);
    __throwILE(env, languageObject);
    return NULL;
  }
  if (timeout < 0) {
    ts_parser_delete(parser);
    __throwIAE(env, "Timeout can not be negative!");
    return NULL;
  } else if (timeout > 0) {
    ts_parser_set_timeout_micros(parser, (uint64_t)timeout);
  }
  TSRange ranges[length];
  for (int i = 0; i < length; i++) {
    jobject rangeObject = env->GetObjectArrayElement(rangeObjectArray, i);
    ranges[i] = __unmarshalRange(env, rangeObject);
  }
  if (!ts_parser_set_included_ranges(parser, ranges, length)) {
    ts_parser_delete(parser);
    __throwISE(env, NULL);
    return NULL;
  } else {
    return _newObject(
      _parserClass,
      _parserConstructor,
      (jlong)parser,
      languageObject
    );
  }
}
