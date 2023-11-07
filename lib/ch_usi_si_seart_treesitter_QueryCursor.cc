#include "ch_usi_si_seart_treesitter.h"
#include "ch_usi_si_seart_treesitter_QueryCursor.h"
#include <jni.h>
#include <tree_sitter/api.h>

JNIEXPORT void JNICALL Java_ch_usi_si_seart_treesitter_QueryCursor_delete(
  JNIEnv* env, jobject thisObject) {
  TSQueryCursor* cursor = (TSQueryCursor*)__getPointer(env, thisObject);
  ts_query_cursor_delete(cursor);
  __clearPointer(env, thisObject);
}

JNIEXPORT void JNICALL Java_ch_usi_si_seart_treesitter_QueryCursor_execute(
  JNIEnv* env, jobject thisObject) {
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
    __throwISE(env, "Query was not executed on node!");
    return NULL;
  }
  TSQueryMatch match;
  TSQueryCursor* cursor = (TSQueryCursor*)__getPointer(env, thisObject);
  found = ts_query_cursor_next_match(cursor, &match);
  if (!found) return NULL;
  jobject nodeObject = env->GetObjectField(thisObject, _queryCursorNodeField);
  jobject queryObject = env->GetObjectField(thisObject, _queryCursorQueryField);
  jobject queryCapturesList = env->GetObjectField(queryObject, _queryCapturesField);
  jobject queryPatternsList = env->GetObjectField(queryObject, _queryPatternsField);
  jobject patternObject = env->CallObjectMethod(
    queryPatternsList, _listGet, match.pattern_index
  );
  jobjectArray entries = env->NewObjectArray(match.capture_count, _mapEntryClass, NULL);
  for (int i = 0; i < match.capture_count; i++) {
    TSQueryCapture capture = match.captures[i];
    jobject captureObject = env->CallObjectMethod(
      queryCapturesList, _listGet, capture.index
    );
    jobject matchedObject = __marshalNode(env, capture.node);
    __copyTree(env, nodeObject, matchedObject);
    jobject entryObject = env->CallStaticObjectMethod(
      _mapClass,
      _mapEntryStaticMethod,
      captureObject,
      matchedObject
    );
    env->SetObjectArrayElement(entries, i, entryObject);
  }
  return env->NewObject(
    _queryMatchClass,
    _queryMatchConstructor,
    (jint)match.id,
    patternObject,
    entries
  );
}
