#include "ch_usi_si_seart_treesitter.h"
#include "ch_usi_si_seart_treesitter_Pattern.h"
#include <jni.h>
#include <tree_sitter/api.h>

JNIEXPORT void JNICALL Java_ch_usi_si_seart_treesitter_Pattern_disable(
  JNIEnv* env, jobject thisObject) {
  jboolean enabled = env->GetBooleanField(thisObject, _patternEnabledField);
  if (!enabled) return;
  env->SetBooleanField(thisObject, _patternEnabledField, JNI_FALSE);
  jobject queryObject = env->GetObjectField(thisObject, _patternQueryField);
  if (queryObject == NULL) return;
  TSQuery* query = (TSQuery*)__getPointer(env, queryObject);
  if (query == NULL) return;
  jint index = env->GetIntField(thisObject, _patternIndexField);
  ts_query_disable_pattern(query, (uint32_t)index);
}
