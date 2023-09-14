#include "ch_usi_si_seart_treesitter.h"
#include "ch_usi_si_seart_treesitter_QueryCursor.h"
#include <jni.h>
#include <tree_sitter/api.h>

JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_QueryCursor_malloc(
  JNIEnv* env, jclass self) {
  return (jlong)ts_query_cursor_new();
}

JNIEXPORT void JNICALL Java_ch_usi_si_seart_treesitter_QueryCursor_close(
  JNIEnv* env, jobject thisObject) {
  jlong queryCursor = __getPointer(env, _queryCursorClass, thisObject);
  ts_query_cursor_delete((TSQueryCursor*)queryCursor);
}

JNIEXPORT void JNICALL Java_ch_usi_si_seart_treesitter_QueryCursor_execute(
  JNIEnv* env, jobject thisObject) {
  bool executed = (bool)env->GetBooleanField(thisObject, _queryCursorExecutedField);
  if (executed) return;
  jlong queryCursor = __getPointer(env, _queryCursorClass, thisObject);
  jobject nodeObject = env->GetObjectField(thisObject, _queryCursorNodeField);
  jobject queryObject = env->GetObjectField(thisObject, _queryCursorQueryField);
  jlong query = __getPointer(env, _queryClass, queryObject);
  TSNode node = __unmarshalNode(env, nodeObject);
  ts_query_cursor_exec((TSQueryCursor*)queryCursor, (TSQuery*)query, node);
  env->SetBooleanField(thisObject, _queryCursorExecutedField, JNI_TRUE);
}

JNIEXPORT jobject JNICALL Java_ch_usi_si_seart_treesitter_QueryCursor_nextMatch(
  JNIEnv* env, jobject thisObject) {
  bool executed = (bool)env->GetBooleanField(thisObject, _queryCursorExecutedField);
  bool found = false;
  TSQueryMatch queryMatch;
  if (!executed) {
    jclass exceptionClass = _getClass("java/lang/IllegalStateException");
    env->ThrowNew(exceptionClass, "Query was not executed on node!");
  } else {
    jlong queryCursor = __getPointer(env, _queryCursorClass, thisObject);
    found = ts_query_cursor_next_match((TSQueryCursor*)queryCursor, &queryMatch);
  }
  return (!found) ? NULL : __marshalQueryMatch(env, queryMatch);
}
