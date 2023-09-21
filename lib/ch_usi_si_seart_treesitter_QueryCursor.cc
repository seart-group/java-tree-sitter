#include "ch_usi_si_seart_treesitter.h"
#include "ch_usi_si_seart_treesitter_QueryCursor.h"
#include <jni.h>
#include <tree_sitter/api.h>

JNIEXPORT void JNICALL Java_ch_usi_si_seart_treesitter_QueryCursor_close(
  JNIEnv* env, jobject thisObject) {
  TSQueryCursor* cursor = (TSQueryCursor*)__getPointer(env, thisObject);
  ts_query_cursor_delete(cursor);
}

JNIEXPORT void JNICALL Java_ch_usi_si_seart_treesitter_QueryCursor_execute(
  JNIEnv* env, jobject thisObject) {
  bool executed = (bool)env->GetBooleanField(thisObject, _queryCursorExecutedField);
  if (executed) return;
  jobject nodeObject = env->GetObjectField(thisObject, _queryCursorNodeField);
  jobject queryObject = env->GetObjectField(thisObject, _queryCursorQueryField);
  TSQueryCursor* cursor = (TSQueryCursor*)__getPointer(env, thisObject);
  TSQuery* query = (TSQuery*)__getPointer(env, queryObject);
  TSNode node = __unmarshalNode(env, nodeObject);
  ts_query_cursor_exec(cursor, query, node);
  env->SetBooleanField(thisObject, _queryCursorExecutedField, JNI_TRUE);
}

JNIEXPORT jobject JNICALL Java_ch_usi_si_seart_treesitter_QueryCursor_nextMatch(
  JNIEnv* env, jobject thisObject) {
  bool executed = (bool)env->GetBooleanField(thisObject, _queryCursorExecutedField);
  bool found = false;
  if (!executed) {
    env->ThrowNew(_illegalStateExceptionClass, "Query was not executed on node!");
    return NULL;
  }
  TSQueryMatch match;
  TSQueryCursor* cursor = (TSQueryCursor*)__getPointer(env, thisObject);
  found = ts_query_cursor_next_match(cursor, &match);
  if (!found) return NULL;
  jobject nodeObject = env->GetObjectField(thisObject, _queryCursorNodeField);
  jobjectArray captures = (*env).NewObjectArray(
    match.capture_count,
    _queryCaptureClass,
    NULL
  );
  for (int i = 0; i < match.capture_count; i++) {
    TSQueryCapture capture = match.captures[i];
    jobject matchedObject = __marshalNode(env, capture.node);
    __copyTree(env, nodeObject, matchedObject);
    jobject captureObject = env->NewObject(
      _queryCaptureClass,
      _queryCaptureConstructor,
      matchedObject,
      capture.index
    );
    env->SetObjectArrayElement(captures, i, captureObject);
  }
  return env->NewObject(
    _queryMatchClass,
    _queryMatchConstructor,
    match.id,
    match.pattern_index,
    captures
  );
}
