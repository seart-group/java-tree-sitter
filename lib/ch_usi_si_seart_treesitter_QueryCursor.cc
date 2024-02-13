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

JNIEXPORT void JNICALL Java_ch_usi_si_seart_treesitter_QueryCursor_setRange__II(
  JNIEnv* env, jobject thisObject, jint start, jint end) {
  if (start < 0 || end < 0) {
    __throwIAE(env, "The start and end bytes must not be negative!");
    return;
  }
  if (start > end) {
    __throwIAE(env, "The starting byte of the range must not be greater than the ending byte!");
    return;
  }
  // Not sure why I need to multiply by two, again probably because of utf-16
  jobject nodeObject = env->GetObjectField(thisObject, _queryCursorNodeField);
  TSNode node = __unmarshalNode(env, nodeObject);
  uint32_t nodeStart = ts_node_start_byte(node);
  uint32_t rangeStart = (uint32_t)start * 2;
  if (rangeStart < nodeStart) {
    __throwBOB(env, start);
    return;
  }
  uint32_t nodeEnd = ts_node_end_byte(node);
  uint32_t rangeEnd = (uint32_t)end * 2;
  if (rangeEnd > nodeEnd) {
    __throwBOB(env, end);
    return;
  }
  TSQueryCursor* cursor = (TSQueryCursor*)__getPointer(env, thisObject);
  ts_query_cursor_set_byte_range(cursor, rangeStart, rangeEnd);
}

JNIEXPORT void JNICALL Java_ch_usi_si_seart_treesitter_QueryCursor_setRange__Lch_usi_si_seart_treesitter_Point_2Lch_usi_si_seart_treesitter_Point_2(
  JNIEnv* env, jobject thisObject, jobject startPointObject, jobject endPointObject) {
  if (startPointObject == NULL) {
    __throwNPE(env, "Start point must not be null!");
    return;
  }
  if (endPointObject == NULL) {
    __throwNPE(env, "End point must not be null!");
    return;
  }
  jobject nodeObject = env->GetObjectField(thisObject, _queryCursorNodeField);
  TSNode node = __unmarshalNode(env, nodeObject);
  TSPoint startPoint = __unmarshalPoint(env, startPointObject);
  TSPoint endPoint = __unmarshalPoint(env, endPointObject);
  if (endPoint.row < 0 || endPoint.column < 0) {
    __throwIAE(env, "End point can not have negative coordinates!");
    return;
  }
  if (startPoint.row < 0 || startPoint.column < 0) {
    __throwIAE(env, "Start point can not have negative coordinates!");
    return;
  }
  if (__comparePoints(startPoint, endPoint) == GT) {
    __throwIAE(env, "Start point can not be greater than end point!");
    return;
  }
  TSPoint lowerBound = ts_node_start_point(node);
  if (__comparePoints(lowerBound, startPoint) == GT) {
    __throwPOB(env, startPointObject);
    return;
  }
  TSPoint upperBound = ts_node_end_point(node);
  if (__comparePoints(endPoint, upperBound) == GT) {
    __throwPOB(env, endPointObject);
    return;
  }
  TSQueryCursor* cursor = (TSQueryCursor*)__getPointer(env, thisObject);
  ts_query_cursor_set_point_range(cursor, startPoint, endPoint);
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
  for (uint16_t i = 0; i < match.capture_count; i++) {
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
  return _newObject(
    _queryMatchClass,
    _queryMatchConstructor,
    (jint)match.id,
    patternObject,
    entries
  );
}
