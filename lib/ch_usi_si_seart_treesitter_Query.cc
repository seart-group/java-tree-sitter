#include "ch_usi_si_seart_treesitter.h"
#include "ch_usi_si_seart_treesitter_Query.h"
#include <jni.h>
#include <string.h>
#include <tree_sitter/api.h>

JNIEXPORT void JNICALL Java_ch_usi_si_seart_treesitter_Query_delete(
  JNIEnv* env, jobject thisObject) {
  TSQuery* query = (TSQuery*)__getPointer(env, thisObject);
  ts_query_delete(query);
  __clearPointer(env, thisObject);
}

JNIEXPORT jint JNICALL Java_ch_usi_si_seart_treesitter_Query_countStrings(
  JNIEnv* env, jobject thisObject) {
  TSQuery* query = (TSQuery*)__getPointer(env, thisObject);
  return (jint)ts_query_string_count(query);
}

JNIEXPORT jint JNICALL Java_ch_usi_si_seart_treesitter_Query_countCaptures(
  JNIEnv* env, jobject thisObject) {
  TSQuery* query = (TSQuery*)__getPointer(env, thisObject);
  return (jint)ts_query_capture_count(query);
}

JNIEXPORT jint JNICALL Java_ch_usi_si_seart_treesitter_Query_countPatterns(
  JNIEnv* env, jobject thisObject) {
  TSQuery* query = (TSQuery*)__getPointer(env, thisObject);
  return (jint)ts_query_pattern_count(query);
}
