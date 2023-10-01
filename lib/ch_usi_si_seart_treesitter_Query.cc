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
