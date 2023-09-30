#include "ch_usi_si_seart_treesitter.h"
#include "ch_usi_si_seart_treesitter_Capture.h"
#include <jni.h>
#include <tree_sitter/api.h>

JNIEXPORT void JNICALL Java_ch_usi_si_seart_treesitter_Capture_disable(
  JNIEnv* env, jobject thisObject) {
  jboolean enabled = env->GetBooleanField(thisObject, _captureEnabledField);
  if (!enabled) return;
  env->SetBooleanField(thisObject, _captureEnabledField, JNI_FALSE);
  jobject queryObject = env->GetObjectField(thisObject, _captureQueryField);
  if (queryObject == NULL) return;
  TSQuery* query = (TSQuery*)__getPointer(env, queryObject);
  if (query == NULL) return;
  jstring name = (jstring)env->GetObjectField(thisObject, _captureNameField);
  uint32_t length = env->GetStringLength(name);
  const char* characters = env->GetStringUTFChars(name, NULL);
  ts_query_disable_capture(query, characters, length);
}
