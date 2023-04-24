#include "usi_si_seart_treesitter.h"
#include "usi_si_seart_treesitter_QueryCursor.h"
#include <jni.h>
#include <tree_sitter/api.h>

JNIEXPORT jlong JNICALL Java_usi_si_seart_treesitter_QueryCursor_malloc(
  JNIEnv* env, jclass self) {
  return (jlong)ts_query_cursor_new();
}

JNIEXPORT void JNICALL Java_usi_si_seart_treesitter_QueryCursor_close(
  JNIEnv* env, jobject thisObject) {
  jclass queryCursorClass = _getClass("usi/si/seart/treesitter/QueryCursor");
  jlong queryCursor = __getPointer(env, queryCursorClass, thisObject);
  ts_query_cursor_delete((TSQueryCursor*)queryCursor);
}

JNIEXPORT void JNICALL Java_usi_si_seart_treesitter_QueryCursor_execute(
  JNIEnv* env, jobject thisObject) {
  jclass queryCursorClass = _getClass("usi/si/seart/treesitter/QueryCursor");
  jfieldID executedField = _getField(queryCursorClass, "executed", "Z");
  bool executed = (bool)env->GetBooleanField(thisObject, executedField);
  if (executed) return;
  jlong queryCursor = __getPointer(env, queryCursorClass, thisObject);
  jfieldID nodeField = _getField(queryCursorClass, "node", "Lusi/si/seart/treesitter/Node;");
  jobject node = env->GetObjectField(thisObject, nodeField);
  jfieldID queryField = _getField(queryCursorClass, "query", "Lusi/si/seart/treesitter/Query;");
  jclass queryClass = _getClass("usi/si/seart/treesitter/Query");
  jlong query = __getPointer(env, queryClass, env->GetObjectField(thisObject, queryField));
  ts_query_cursor_exec((TSQueryCursor*) queryCursor, (TSQuery*) query, __unmarshalNode(env,node));
  env->SetBooleanField(thisObject, executedField, JNI_TRUE);
}

JNIEXPORT jobject JNICALL Java_usi_si_seart_treesitter_QueryCursor_nextMatch(
  JNIEnv* env, jobject thisObject) {
  jclass queryCursorClass = _getClass("usi/si/seart/treesitter/QueryCursor");
  jfieldID executedField = _getField(queryCursorClass, "executed", "Z");
  bool executed = (bool)env->GetBooleanField(thisObject, executedField);
  bool found = false;
  TSQueryMatch queryMatch;
  if (!executed) {
    jclass exceptionClass = _getClass("java/lang/IllegalStateException");
    env->ThrowNew(exceptionClass, "Query was not executed on node!");
  } else {
    jlong queryCursor = __getPointer(env, queryCursorClass, thisObject);
    found = ts_query_cursor_next_match((TSQueryCursor*)queryCursor, &queryMatch);
  }
  return (!found) ? NULL : __marshalQueryMatch(env, queryMatch);
}
