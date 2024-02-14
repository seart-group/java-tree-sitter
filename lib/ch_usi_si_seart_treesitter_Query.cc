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

JNIEXPORT jint JNICALL Java_ch_usi_si_seart_treesitter_Query_getQuantifier(
  JNIEnv* env, jobject thisObject, jint patternIndex, jint captureIndex) {
  TSQuery* query = (TSQuery*)__getPointer(env, thisObject);
  return (jint)ts_query_capture_quantifier_for_id(
    query,
    (uint32_t)patternIndex,
    (uint32_t)captureIndex
  );
}
