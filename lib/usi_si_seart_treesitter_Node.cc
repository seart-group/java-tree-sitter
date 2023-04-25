#include "usi_si_seart_treesitter.h"
#include "usi_si_seart_treesitter_Node.h"
#include <jni.h>
#include <string.h>
#include <tree_sitter/api.h>

JNIEXPORT jobject JNICALL Java_usi_si_seart_treesitter_Node_getChild(
  JNIEnv* env, jobject thisObject, jint index) {
  TSNode node = __unmarshalNode(env, thisObject);
  uint32_t childIndex = (uint32_t)index;
  if ((childIndex < 0) || (childIndex >= ts_node_child_count(node))) {
    jclass exceptionClass = _getClass("java/lang/IndexOutOfBoundsException");
    jmethodID exceptionConstructor = _getConstructor(exceptionClass, "(I)V");
    jobject exception = env->NewObject(exceptionClass, exceptionConstructor, index);
    env->Throw((jthrowable)exception);
    return NULL;
  }
  TSNode child = ts_node_child(node, childIndex);
  return __marshalNode(env, child);
}

JNIEXPORT jobject JNICALL Java_usi_si_seart_treesitter_Node_getChildByFieldName(
  JNIEnv* env, jobject thisObject, jstring name) {
  if (name == NULL) {
    jclass exceptionClass = _getClass("java/lang/NullPointerException");
    env->ThrowNew(exceptionClass, "Field name must not be null!");
    return NULL;
  }
  const char* childName;
  uint32_t length = env->GetStringLength(name);
  childName = env->GetStringUTFChars(name, NULL);
  TSNode node = __unmarshalNode(env, thisObject);
  TSNode child = ts_node_child_by_field_name(node, childName, length);
  if (ts_node_is_null(child)) return NULL;
  return __marshalNode(env, child);
}

JNIEXPORT jint JNICALL Java_usi_si_seart_treesitter_Node_getChildCount(
  JNIEnv* env, jobject thisObject) {
  TSNode node = __unmarshalNode(env, thisObject);
  return (jint)ts_node_child_count(node);
}

JNIEXPORT jobject JNICALL Java_usi_si_seart_treesitter_Node_getDescendantForByteRange(
  JNIEnv* env, jobject thisObject, jint start, jint end) {
  if (start > end) {
    jclass exceptionClass = _getClass("java/lang/IllegalArgumentException");
    env->ThrowNew(exceptionClass, "The starting byte of the range must not be greater than the ending byte!");
    return NULL;
  }
  TSNode node = __unmarshalNode(env, thisObject);
  TSNode descendant = ts_node_descendant_for_byte_range(
    // Not sure why I need to multiply by two, again probably because of utf-16
    node, (uint32_t)start * 2, (uint32_t)end * 2
  );
  return __marshalNode(env, descendant);
}

JNIEXPORT jint JNICALL Java_usi_si_seart_treesitter_Node_getEndByte(
  JNIEnv* env, jobject thisObject) {
  TSNode node = __unmarshalNode(env, thisObject);
  return (jint)ts_node_end_byte(node) / 2;
}

JNIEXPORT jobject JNICALL Java_usi_si_seart_treesitter_Node_getEndPoint(
  JNIEnv* env, jobject thisObject) {
  TSNode node = __unmarshalNode(env, thisObject);
  TSPoint point = ts_node_end_point(node);
  return __marshalPoint(env, point);
}

JNIEXPORT jstring JNICALL Java_usi_si_seart_treesitter_Node_getFieldNameForChild(
  JNIEnv* env, jobject thisObject, jint index) {
  TSNode node = __unmarshalNode(env, thisObject);
  uint32_t childIndex = (uint32_t)index;
  if ((childIndex < 0) || (childIndex >= ts_node_child_count(node))) {
    jclass exceptionClass = _getClass("java/lang/IndexOutOfBoundsException");
    jmethodID exceptionConstructor = _getConstructor(exceptionClass, "(I)V");
    jobject exception = env->NewObject(exceptionClass, exceptionConstructor, index);
    env->Throw((jthrowable)exception);
    return NULL;
  }
  const char* childName = ts_node_field_name_for_child(node, childIndex);
  jstring result = env->NewStringUTF(childName);
  return result;
}

JNIEXPORT jobject JNICALL Java_usi_si_seart_treesitter_Node_getFirstChildForByte(
  JNIEnv* env, jobject thisObject, jint offset) {
  TSNode node = __unmarshalNode(env, thisObject);
  uint32_t position = (uint32_t)offset * 2;
  uint32_t nodeStart = ts_node_start_byte(node);
  uint32_t nodeEnd = ts_node_end_byte(node);
  if ((position < nodeStart) || (position > nodeEnd)) {
    jclass exceptionClass = _getClass("java/lang/IndexOutOfBoundsException");
    jmethodID exceptionConstructor = _getConstructor(exceptionClass, "(I)V");
    jobject exception = env->NewObject(exceptionClass, exceptionConstructor, offset);
    env->Throw((jthrowable)exception);
    return NULL;
  }
  TSNode child = ts_node_first_child_for_byte(node, position);
  return __marshalNode(env, child);
}

JNIEXPORT jobject JNICALL Java_usi_si_seart_treesitter_Node_getFirstNamedChildForByte(
  JNIEnv* env, jobject thisObject, jint offset) {
  TSNode node = __unmarshalNode(env, thisObject);
  uint32_t position = (uint32_t)offset * 2;
  uint32_t nodeStart = ts_node_start_byte(node);
  uint32_t nodeEnd = ts_node_end_byte(node);
  if ((position < nodeStart) || (position > nodeEnd)) {
    jclass exceptionClass = _getClass("java/lang/IndexOutOfBoundsException");
    jmethodID exceptionConstructor = _getConstructor(exceptionClass, "(I)V");
    jobject exception = env->NewObject(exceptionClass, exceptionConstructor, offset);
    env->Throw((jthrowable)exception);
    return NULL;
  }
  TSNode child = ts_node_first_named_child_for_byte(node, position);
  return __marshalNode(env, child);
}

JNIEXPORT jstring JNICALL Java_usi_si_seart_treesitter_Node_getNodeString(
  JNIEnv* env, jobject thisObject) {
  TSNode node = __unmarshalNode(env, thisObject);
  char* nodeString = ts_node_string(node);
  jstring result = env->NewStringUTF(nodeString);
  free(nodeString);
  return result;
}

JNIEXPORT jobject JNICALL Java_usi_si_seart_treesitter_Node_getNextNamedSibling(
  JNIEnv* env, jobject thisObject) {
  TSNode node = __unmarshalNode(env, thisObject);
  TSNode sibling = ts_node_next_named_sibling(node);
  if (ts_node_is_null(sibling)) return NULL;
  return __marshalNode(env, sibling);
}

JNIEXPORT jobject JNICALL Java_usi_si_seart_treesitter_Node_getNextSibling(
  JNIEnv* env, jobject thisObject) {
  TSNode node = __unmarshalNode(env, thisObject);
  TSNode sibling = ts_node_next_sibling(node);
  if (ts_node_is_null(sibling)) return NULL;
  return __marshalNode(env, sibling);
}

JNIEXPORT jobject JNICALL Java_usi_si_seart_treesitter_Node_getPrevNamedSibling(
  JNIEnv* env, jobject thisObject) {
  TSNode node = __unmarshalNode(env, thisObject);
  TSNode sibling = ts_node_prev_named_sibling(node);
  if (ts_node_is_null(sibling)) return NULL;
  return __marshalNode(env, sibling);
}

JNIEXPORT jobject JNICALL Java_usi_si_seart_treesitter_Node_getPrevSibling(
  JNIEnv* env, jobject thisObject) {
  TSNode node = __unmarshalNode(env, thisObject);
  TSNode sibling = ts_node_prev_sibling(node);
  if (ts_node_is_null(sibling)) return NULL;
  return __marshalNode(env, sibling);
}

JNIEXPORT jobject JNICALL Java_usi_si_seart_treesitter_Node_getParent(
  JNIEnv* env, jobject thisObject) {
  TSNode node = __unmarshalNode(env, thisObject);
  TSNode parent = ts_node_parent(node);
  if (ts_node_is_null(parent)) return NULL;
  return __marshalNode(env, parent);
}

JNIEXPORT jint JNICALL Java_usi_si_seart_treesitter_Node_getStartByte(
  JNIEnv* env, jobject thisObject) {
  TSNode node = __unmarshalNode(env, thisObject);
  return (jint)ts_node_start_byte(node) / 2;
}

JNIEXPORT jobject JNICALL Java_usi_si_seart_treesitter_Node_getStartPoint(
  JNIEnv* env, jobject thisObject) {
  TSNode node = __unmarshalNode(env, thisObject);
  TSPoint point = ts_node_start_point(node);
  return __marshalPoint(env, point);
}

JNIEXPORT jstring JNICALL Java_usi_si_seart_treesitter_Node_getType(
  JNIEnv* env, jobject thisObject) {
  TSNode node = __unmarshalNode(env, thisObject);
  const char* type = ts_node_type(node);
  jstring result = env->NewStringUTF(type);
  return result;
}

JNIEXPORT jboolean JNICALL Java_usi_si_seart_treesitter_Node_hasError(
  JNIEnv* env, jobject thisObject) {
  TSNode node = __unmarshalNode(env, thisObject);
  return ts_node_has_error(node) ? JNI_TRUE : JNI_FALSE;
}

JNIEXPORT jboolean JNICALL Java_usi_si_seart_treesitter_Node_isExtra(
  JNIEnv* env, jobject thisObject) {
  TSNode node = __unmarshalNode(env, thisObject);
  return ts_node_is_extra(node) ? JNI_TRUE : JNI_FALSE;
}

JNIEXPORT jboolean JNICALL Java_usi_si_seart_treesitter_Node_isMissing(
  JNIEnv* env, jobject thisObject) {
  TSNode node = __unmarshalNode(env, thisObject);
  return ts_node_is_missing(node) ? JNI_TRUE : JNI_FALSE;
}

JNIEXPORT jboolean JNICALL Java_usi_si_seart_treesitter_Node_isNamed(
  JNIEnv* env, jobject thisObject) {
  TSNode node = __unmarshalNode(env, thisObject);
  return ts_node_is_named(node) ? JNI_TRUE : JNI_FALSE;
}

JNIEXPORT jboolean JNICALL Java_usi_si_seart_treesitter_Node_isNull(
  JNIEnv* env, jobject thisObject) {
  TSNode node = __unmarshalNode(env, thisObject);
  return ts_node_is_null(node) ? JNI_TRUE : JNI_FALSE;
}

JNIEXPORT jboolean JNICALL Java_usi_si_seart_treesitter_Node_equals(
  JNIEnv* env, jclass self, jobject node, jobject other) {
  TSNode node_1 = __unmarshalNode(env, node);
  TSNode node_2 = __unmarshalNode(env, other);
  return ts_node_eq(node_1, node_2) ? JNI_TRUE : JNI_FALSE;
}
