#include "ch_usi_si_seart_treesitter.h"
#include "ch_usi_si_seart_treesitter_Node.h"
#include <jni.h>
#include <string.h>
#include <tree_sitter/api.h>

JNIEXPORT jobject JNICALL Java_ch_usi_si_seart_treesitter_Node_getChild(
  JNIEnv* env, jobject thisObject, jint index, jboolean named) {
  uint32_t (*child_counter)(TSNode) = (bool)named
      ? ts_node_named_child_count
      : ts_node_child_count;
  TSNode (*child_getter)(TSNode, uint32_t) = (bool)named
      ? ts_node_named_child
      : ts_node_child;
  TSNode node = __unmarshalNode(env, thisObject);
  uint32_t childIndex = (uint32_t)index;
  if ((childIndex < 0) || (childIndex >= child_counter(node))) {
    __throwIOB(env, index);
    return NULL;
  }
  TSNode child = child_getter(node, childIndex);
  jobject childObject = __marshalNode(env, child);
  __copyTree(env, thisObject, childObject);
  return childObject;
}

JNIEXPORT jobject JNICALL Java_ch_usi_si_seart_treesitter_Node_getChildByFieldName(
  JNIEnv* env, jobject thisObject, jstring name) {
  if (name == NULL) {
    __throwNPE(env, "Field name must not be null!");
    return NULL;
  }
  uint32_t length = env->GetStringLength(name);
  const char* childName = env->GetStringUTFChars(name, NULL);
  TSNode node = __unmarshalNode(env, thisObject);
  TSNode child = ts_node_child_by_field_name(node, childName, length);
  env->ReleaseStringUTFChars(name, childName);
  if (ts_node_is_null(child)) return NULL;
  jobject childObject = __marshalNode(env, child);
  __copyTree(env, thisObject, childObject);
  return childObject;
}

JNIEXPORT jint JNICALL Java_ch_usi_si_seart_treesitter_Node_getChildCount(
  JNIEnv* env, jobject thisObject, jboolean named) {
  uint32_t (*child_counter)(TSNode) = (bool)named
      ? ts_node_named_child_count
      : ts_node_child_count;
  TSNode node = __unmarshalNode(env, thisObject);
  uint32_t count = ts_node_is_null(node) ? 0 : child_counter(node);
  return (jint)count;
}

JNIEXPORT jobjectArray JNICALL Java_ch_usi_si_seart_treesitter_Node_getChildren(
  JNIEnv* env, jclass thisClass, jobject nodeObject, jboolean named) {
  uint32_t (*child_counter)(TSNode) = (bool)named
      ? ts_node_named_child_count
      : ts_node_child_count;
  TSNode (*child_getter)(TSNode, uint32_t) = (bool)named
      ? ts_node_named_child
      : ts_node_child;
  TSNode node = __unmarshalNode(env, nodeObject);
  uint32_t count = ts_node_is_null(node) ? 0 : child_counter(node);
  jobjectArray children = env->NewObjectArray(count, _nodeClass, NULL);
  for (uint32_t i = 0; i < count; i++) {
    TSNode child = child_getter(node, i);
    jobject childObject = __marshalNode(env, child);
    __copyTree(env, nodeObject, childObject);
    env->SetObjectArrayElement(children, i, childObject);
  }
  return children;
}

JNIEXPORT jobject JNICALL Java_ch_usi_si_seart_treesitter_Node_getDescendant__IIZ(
  JNIEnv* env, jobject thisObject, jint start, jint end, jboolean named) {
  if (start < 0 || end < 0) {
    __throwIAE(env, "The start and end bytes must not be negative!");
    return NULL;
  }
  if (start > end) {
    __throwIAE(env, "The starting byte of the range must not be greater than the ending byte!");
    return NULL;
  }
  // Not sure why I need to multiply by two, again probably because of utf-16
  TSNode node = __unmarshalNode(env, thisObject);
  uint32_t nodeStart = ts_node_start_byte(node);
  uint32_t rangeStart = (uint32_t)start * 2;
  if (rangeStart < nodeStart) {
    __throwBOB(env, start);
    return NULL;
  }
  uint32_t nodeEnd = ts_node_end_byte(node);
  uint32_t rangeEnd = (uint32_t)end * 2;
  if (rangeEnd > nodeEnd) {
    __throwBOB(env, end);
    return NULL;
  }
  TSNode (*descendant_getter)(TSNode, uint32_t, uint32_t) = (bool)named
    ? ts_node_named_descendant_for_byte_range
    : ts_node_descendant_for_byte_range;
  TSNode descendant = descendant_getter(node, rangeStart, rangeEnd);
  jobject descendantObject = __marshalNode(env, descendant);
  __copyTree(env, thisObject, descendantObject);
  return descendantObject;
}

JNIEXPORT jobject JNICALL Java_ch_usi_si_seart_treesitter_Node_getDescendant__Lch_usi_si_seart_treesitter_Point_2Lch_usi_si_seart_treesitter_Point_2Z(
  JNIEnv* env, jobject thisObject, jobject startPointObject, jobject endPointObject, jboolean named) {
  if (startPointObject == NULL) {
    __throwNPE(env, "Start point must not be null!");
    return NULL;
  }
  if (endPointObject == NULL) {
    __throwNPE(env, "End point must not be null!");
    return NULL;
  }
  TSNode node = __unmarshalNode(env, thisObject);
  TSPoint startPoint = __unmarshalPoint(env, startPointObject);
  TSPoint endPoint = __unmarshalPoint(env, endPointObject);
  if (endPoint.row < 0 || endPoint.column < 0) {
    __throwIAE(env, "End point can not have negative coordinates!");
    return NULL;
  }
  if (startPoint.row < 0 || startPoint.column < 0) {
    __throwIAE(env, "Start point can not have negative coordinates!");
    return NULL;
  }
  if (__comparePoints(startPoint, endPoint) == GT) {
    __throwIAE(env, "Start point can not be greater than end point!");
    return NULL;
  }
  TSPoint lowerBound = ts_node_start_point(node);
  if (__comparePoints(lowerBound, startPoint) == GT) {
    __throwPOB(env, startPointObject);
    return NULL;
  }
  TSPoint upperBound = ts_node_end_point(node);
  if (__comparePoints(endPoint, upperBound) == GT) {
    __throwPOB(env, endPointObject);
    return NULL;
  }
  TSNode (*descendant_getter)(TSNode, TSPoint, TSPoint) = (bool)named
    ? ts_node_named_descendant_for_point_range
    : ts_node_descendant_for_point_range;
  TSNode descendant = descendant_getter(node, startPoint, endPoint);
  jobject descendantObject = __marshalNode(env, descendant);
  __copyTree(env, thisObject, descendantObject);
  return descendantObject;
}

JNIEXPORT jint JNICALL Java_ch_usi_si_seart_treesitter_Node_getDescendantCount(
  JNIEnv* env, jobject thisObject) {
  TSNode node = __unmarshalNode(env, thisObject);
  return ts_node_is_null(node) ? (jint)0 : (jint)ts_node_descendant_count(node);
}

JNIEXPORT jint JNICALL Java_ch_usi_si_seart_treesitter_Node_getEndByte(
  JNIEnv* env, jobject thisObject) {
  TSNode node = __unmarshalNode(env, thisObject);
  return ts_node_is_null(node) ? (jint)0 : (jint)ts_node_end_byte(node) / 2;
}

JNIEXPORT jobject JNICALL Java_ch_usi_si_seart_treesitter_Node_getEndPoint(
  JNIEnv* env, jobject thisObject) {
  TSNode node = __unmarshalNode(env, thisObject);
  if (ts_node_is_null(node)) return env->CallStaticObjectMethod(_pointClass, _pointOriginStaticMethod);
  TSPoint point = ts_node_end_point(node);
  return __marshalPoint(env, point);
}

JNIEXPORT jstring JNICALL Java_ch_usi_si_seart_treesitter_Node_getFieldNameForChild(
  JNIEnv* env, jobject thisObject, jint index) {
  TSNode node = __unmarshalNode(env, thisObject);
  uint32_t childIndex = (uint32_t)index;
  if ((childIndex < 0) || (childIndex >= ts_node_child_count(node))) {
    __throwIOB(env, index);
    return NULL;
  }
  const char* childName = ts_node_field_name_for_child(node, childIndex);
  jstring result = env->NewStringUTF(childName);
  return result;
}

JNIEXPORT jobject JNICALL Java_ch_usi_si_seart_treesitter_Node_getFirstChildForByte(
  JNIEnv* env, jobject thisObject, jint offset, jboolean named) {
  TSNode node = __unmarshalNode(env, thisObject);
  uint32_t position = (uint32_t)offset * 2;
  uint32_t nodeStart = ts_node_start_byte(node);
  if (position < nodeStart) {
    __throwBOB(env, offset);
    return NULL;
  }
  uint32_t nodeEnd = ts_node_end_byte(node);
  if (position > nodeEnd) {
    __throwBOB(env, offset);
    return NULL;
  }
  TSNode (*child_getter)(TSNode, uint32_t) = (bool)named
    ? ts_node_first_named_child_for_byte
    : ts_node_first_child_for_byte;
  TSNode child = child_getter(node, position);
  jobject childObject = __marshalNode(env, child);
  __copyTree(env, thisObject, childObject);
  return childObject;
}

JNIEXPORT jint JNICALL Java_ch_usi_si_seart_treesitter_Node_getNextParseState(
  JNIEnv* env, jobject thisObject) {
  TSNode node = __unmarshalNode(env, thisObject);
  return ts_node_is_null(node) ? (jint)-1 : (jint)ts_node_next_parse_state(node);
}

JNIEXPORT jobject JNICALL Java_ch_usi_si_seart_treesitter_Node_getNextSibling(
  JNIEnv* env, jobject thisObject, jboolean named) {
  TSNode (*next_sibling_getter)(TSNode) = (bool)named
        ? ts_node_next_named_sibling
        : ts_node_next_sibling;
  TSNode node = __unmarshalNode(env, thisObject);
  TSNode sibling = next_sibling_getter(node);
  if (ts_node_is_null(sibling)) return NULL;
  jobject siblingObject = __marshalNode(env, sibling);
  __copyTree(env, thisObject, siblingObject);
  return siblingObject;
}

JNIEXPORT jobject JNICALL Java_ch_usi_si_seart_treesitter_Node_getPrevSibling(
  JNIEnv* env, jobject thisObject, jboolean named) {
  TSNode (*prev_sibling_getter)(TSNode) = (bool)named
       ? ts_node_prev_named_sibling
       : ts_node_prev_sibling;
  TSNode node = __unmarshalNode(env, thisObject);
  TSNode sibling = prev_sibling_getter(node);
  if (ts_node_is_null(sibling)) return NULL;
  jobject siblingObject = __marshalNode(env, sibling);
  __copyTree(env, thisObject, siblingObject);
  return siblingObject;
}

JNIEXPORT jobject JNICALL Java_ch_usi_si_seart_treesitter_Node_getParent(
  JNIEnv* env, jobject thisObject) {
  TSNode node = __unmarshalNode(env, thisObject);
  TSNode parent = ts_node_parent(node);
  if (ts_node_is_null(parent)) return NULL;
  jobject parentObject = __marshalNode(env, parent);
  __copyTree(env, thisObject, parentObject);
  return parentObject;
}

JNIEXPORT jint JNICALL Java_ch_usi_si_seart_treesitter_Node_getParseState(
  JNIEnv* env, jobject thisObject) {
  TSNode node = __unmarshalNode(env, thisObject);
  return ts_node_is_null(node) ? (jint)-1 : (jint)ts_node_parse_state(node);
}

JNIEXPORT jint JNICALL Java_ch_usi_si_seart_treesitter_Node_getStartByte(
  JNIEnv* env, jobject thisObject) {
  TSNode node = __unmarshalNode(env, thisObject);
  return ts_node_is_null(node) ? (jint)0 : (jint)ts_node_start_byte(node) / 2;
}

JNIEXPORT jobject JNICALL Java_ch_usi_si_seart_treesitter_Node_getStartPoint(
  JNIEnv* env, jobject thisObject) {
  TSNode node = __unmarshalNode(env, thisObject);
  if (ts_node_is_null(node)) return env->CallStaticObjectMethod(_pointClass, _pointOriginStaticMethod);
  TSPoint point = ts_node_start_point(node);
  return __marshalPoint(env, point);
}

JNIEXPORT jobject JNICALL Java_ch_usi_si_seart_treesitter_Node_getSymbol(
  JNIEnv* env, jobject thisObject, jboolean grammar) {
  jobject treeObject = env->GetObjectField(thisObject, _nodeTreeField);
  if (treeObject == NULL) return NULL;
  jobject languageObject = env->GetObjectField(treeObject, _treeLanguageField);
  if (languageObject == NULL) return NULL;
  const TSLanguage* language = __unmarshalLanguage(env, languageObject);
  TSNode node = __unmarshalNode(env, thisObject);
  if (ts_node_is_null(node)) return NULL;
  TSSymbol (*symbol_getter)(TSNode) = (bool)grammar
    ? ts_node_grammar_symbol
    : ts_node_symbol;
  TSSymbol symbol = symbol_getter(node);
  const char* name = ts_language_symbol_name(language, symbol);
  TSSymbolType type = ts_language_symbol_type(language, symbol);
  return _newObject(
    _symbolClass,
    _symbolConstructor,
    (jint)symbol,
    (jint)type,
    env->NewStringUTF(name)
  );
}

JNIEXPORT jstring JNICALL Java_ch_usi_si_seart_treesitter_Node_getType(
  JNIEnv* env, jobject thisObject, jboolean grammar) {
  TSNode node = __unmarshalNode(env, thisObject);
  if (ts_node_is_null(node)) return NULL;
  const char* (*type_getter)(TSNode) = (bool)grammar
    ? ts_node_grammar_type
    : ts_node_type;
  const char* type = type_getter(node);
  return env->NewStringUTF(type);
}

JNIEXPORT jboolean JNICALL Java_ch_usi_si_seart_treesitter_Node_hasChanges(
  JNIEnv* env, jobject thisObject) {
  TSNode node = __unmarshalNode(env, thisObject);
  return ts_node_has_changes(node) ? JNI_TRUE : JNI_FALSE;
}

JNIEXPORT jboolean JNICALL Java_ch_usi_si_seart_treesitter_Node_hasError(
  JNIEnv* env, jobject thisObject) {
  TSNode node = __unmarshalNode(env, thisObject);
  return ts_node_has_error(node) ? JNI_TRUE : JNI_FALSE;
}

JNIEXPORT jboolean JNICALL Java_ch_usi_si_seart_treesitter_Node_isError(
  JNIEnv* env, jobject thisObject) {
  TSNode node = __unmarshalNode(env, thisObject);
  return ts_node_is_error(node) ? JNI_TRUE : JNI_FALSE;
}

JNIEXPORT jboolean JNICALL Java_ch_usi_si_seart_treesitter_Node_isExtra(
  JNIEnv* env, jobject thisObject) {
  TSNode node = __unmarshalNode(env, thisObject);
  return ts_node_is_extra(node) ? JNI_TRUE : JNI_FALSE;
}

JNIEXPORT jboolean JNICALL Java_ch_usi_si_seart_treesitter_Node_isMissing(
  JNIEnv* env, jobject thisObject) {
  TSNode node = __unmarshalNode(env, thisObject);
  return ts_node_is_missing(node) ? JNI_TRUE : JNI_FALSE;
}

JNIEXPORT jboolean JNICALL Java_ch_usi_si_seart_treesitter_Node_isNamed(
  JNIEnv* env, jobject thisObject) {
  TSNode node = __unmarshalNode(env, thisObject);
  return ts_node_is_named(node) ? JNI_TRUE : JNI_FALSE;
}

JNIEXPORT jboolean JNICALL Java_ch_usi_si_seart_treesitter_Node_isNull(
  JNIEnv* env, jobject thisObject) {
  TSNode node = __unmarshalNode(env, thisObject);
  return ts_node_is_null(node) ? JNI_TRUE : JNI_FALSE;
}

JNIEXPORT jboolean JNICALL Java_ch_usi_si_seart_treesitter_Node_equals(
  JNIEnv* env, jclass self, jobject node, jobject other) {
  TSNode node_1 = __unmarshalNode(env, node);
  TSNode node_2 = __unmarshalNode(env, other);
  return ts_node_eq(node_1, node_2) ? JNI_TRUE : JNI_FALSE;
}

JNIEXPORT jobject JNICALL Java_ch_usi_si_seart_treesitter_Node_walk__(
  JNIEnv* env, jobject thisObject) {
  jobject treeObject = env->GetObjectField(thisObject, _nodeTreeField);
  if (treeObject == NULL) {
    __throwISE(env, "Cannot construct a TreeCursor instance without a Tree!");
    return NULL;
  }
  TSNode node = __unmarshalNode(env, thisObject);
  if (ts_node_is_null(node)) {
    __throwISE(env, "Cannot construct a TreeCursor instance from a `null` Node!");
    return NULL;
  }
  TSTreeCursor cursor = ts_tree_cursor_new(node);
  return _newObject(
    _treeCursorClass,
    _treeCursorConstructor,
    new TSTreeCursor(cursor),
    cursor.context[0],
    cursor.context[1],
    cursor.id,
    treeObject
  );
}

JNIEXPORT jobject JNICALL Java_ch_usi_si_seart_treesitter_Node_walk__Lch_usi_si_seart_treesitter_Query_2(
  JNIEnv* env, jobject thisObject, jobject queryObject) {
  if (queryObject == NULL) {
    __throwNPE(env, "Query must not be null!");
    return NULL;
  }
  jobject treeObject = env->GetObjectField(thisObject, _nodeTreeField);
  if (treeObject == NULL) {
    __throwISE(env, "Cannot construct a QueryCursor instance without a Tree!");
    return NULL;
  }
  TSNode node = __unmarshalNode(env, thisObject);
  if (ts_node_is_null(node)) {
    __throwISE(env, "Cannot construct a QueryCursor instance from a `null` Node!");
    return NULL;
  }
  return _newObject(
    _queryCursorClass,
    _queryCursorConstructor,
    ts_query_cursor_new(),
    thisObject,
    queryObject
  );
}
