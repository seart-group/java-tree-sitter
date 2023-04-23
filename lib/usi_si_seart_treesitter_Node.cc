#include "usi_si_seart_treesitter_Node.h"
#include <jni.h>
#include <string.h>
#include <tree_sitter/api.h>

#define _getClass(NAME) \
  { env->FindClass(NAME) }

#define _getField(CLASS, NAME, TYPE) \
  { env->GetFieldID(CLASS, NAME, TYPE) }

jobject __marshalNode(JNIEnv* env, TSNode node) {
  if (node.id == 0) return NULL;
  jclass nodeClass = _getClass("usi/si/seart/treesitter/Node");
  jfieldID nodeContext0Field = _getField(nodeClass, "context0", "I");
  jfieldID nodeContext1Field = _getField(nodeClass, "context1", "I");
  jfieldID nodeContext2Field = _getField(nodeClass, "context2", "I");
  jfieldID nodeContext3Field = _getField(nodeClass, "context3", "I");
  jfieldID nodeIdField = _getField(nodeClass, "id", "J");
  jfieldID nodeTreeField = _getField(nodeClass, "tree", "J");
  jobject nodeObject = env->AllocObject(nodeClass);
  env->SetIntField(nodeObject, nodeContext0Field, node.context[0]);
  env->SetIntField(nodeObject, nodeContext1Field, node.context[1]);
  env->SetIntField(nodeObject, nodeContext2Field, node.context[2]);
  env->SetIntField(nodeObject, nodeContext3Field, node.context[3]);
  env->SetLongField(nodeObject, nodeIdField, (jlong)node.id);
  env->SetLongField(nodeObject, nodeTreeField, (jlong)node.tree);
  return nodeObject;
}

TSNode __unmarshalNode(JNIEnv* env, jobject nodeObject) {
  jclass nodeClass = _getClass("usi/si/seart/treesitter/Node");
  jfieldID nodeContext0Field = _getField(nodeClass, "context0", "I");
  jfieldID nodeContext1Field = _getField(nodeClass, "context1", "I");
  jfieldID nodeContext2Field = _getField(nodeClass, "context2", "I");
  jfieldID nodeContext3Field = _getField(nodeClass, "context3", "I");
  jfieldID nodeIdField = _getField(nodeClass, "id", "J");
  jfieldID nodeTreeField = _getField(nodeClass, "tree", "J");
  return (TSNode){
      {
          (uint32_t)env->GetIntField(nodeObject, nodeContext0Field),
          (uint32_t)env->GetIntField(nodeObject, nodeContext1Field),
          (uint32_t)env->GetIntField(nodeObject, nodeContext2Field),
          (uint32_t)env->GetIntField(nodeObject, nodeContext3Field),
      },
      (const void*)env->GetLongField(nodeObject, nodeIdField),
      (const TSTree*)env->GetLongField(nodeObject, nodeTreeField)};
}

jobject __marshalPoint(JNIEnv* env, TSPoint point) {
  jclass pointClass = _getClass("usi/si/seart/treesitter/Point");
  jfieldID pointRowField = _getField(pointClass, "row", "I");
  jfieldID pointColumnField = _getField(pointClass, "column", "I");
  jobject pointObject = env->AllocObject(pointClass);
  env->SetIntField(pointObject, pointRowField, point.row);
  env->SetIntField(pointObject, pointColumnField, point.column / 2);
  // Not sure why I need to divide by two, probably because of utf-16
  return pointObject;
}

TSPoint __unmarshalPoint(JNIEnv* env, jobject pointObject) {
  jclass pointClass = _getClass("usi/si/seart/treesitter/Point");
  jfieldID pointRowField = _getField(pointClass, "row", "I");
  jfieldID pointColumnField = _getField(pointClass, "column", "I");
  return (TSPoint) {
    (uint32_t)env->GetIntField(pointObject, pointRowField),
    (uint32_t)env->GetIntField(pointObject, pointColumnField),
  };
}

JNIEXPORT jobject JNICALL Java_usi_si_seart_treesitter_Node_getChild(
    JNIEnv* env, jclass self, jobject node, jint child) {
  return __marshalNode(env, ts_node_child(__unmarshalNode(env, node), (uint32_t)child));
}

JNIEXPORT jobject JNICALL Java_usi_si_seart_treesitter_Node_getChildByFieldName(
    JNIEnv* env, jclass self, jobject node, jstring name) {
  const char* c_name;
  uint32_t name_length = env->GetStringLength(name);
  c_name = env->GetStringUTFChars(name, NULL);
  TSNode child = ts_node_child_by_field_name(__unmarshalNode(env, node), c_name, name_length);
  if (ts_node_is_null(child)) return NULL;
  return __marshalNode(env, child);
}

JNIEXPORT jint JNICALL Java_usi_si_seart_treesitter_Node_getChildCount(
    JNIEnv* env, jclass self, jobject node) {
  return (jint)ts_node_child_count(__unmarshalNode(env, node));
}

JNIEXPORT jobject JNICALL Java_usi_si_seart_treesitter_Node_getDescendantForByteRange(
    JNIEnv* env, jclass self, jobject node, jint start, jint end) {
  TSNode descendant = ts_node_descendant_for_byte_range(
    __unmarshalNode(env, node), (uint32_t)start * 2, (uint32_t)end * 2
  // Not sure why I need to multiply by two, again probably because of utf-16
  );
  return __marshalNode(env, descendant);
}

JNIEXPORT jint JNICALL Java_usi_si_seart_treesitter_Node_getEndByte(
    JNIEnv* env, jclass self, jobject node) {
  return (jint)ts_node_end_byte(__unmarshalNode(env, node)) / 2;
}

JNIEXPORT jobject JNICALL Java_usi_si_seart_treesitter_Node_getEndPoint(
    JNIEnv* env, jclass self, jobject node) {
  return __marshalPoint(env, ts_node_end_point(__unmarshalNode(env, node)));
}

JNIEXPORT jstring JNICALL Java_usi_si_seart_treesitter_Node_getFieldNameForChild(
    JNIEnv* env, jclass self, jobject node, jint child) {
  const char* nameForChild = ts_node_field_name_for_child(__unmarshalNode(env, node), (uint32_t)child);
  jstring result = env->NewStringUTF(nameForChild);
  return result;
}

JNIEXPORT jobject JNICALL Java_usi_si_seart_treesitter_Node_getFirstChildForByte(
    JNIEnv* env, jclass self, jobject node, jint offset) {
  TSNode child = ts_node_first_child_for_byte(__unmarshalNode(env, node), (uint32_t)offset * 2);
  return __marshalNode(env, child);
}

JNIEXPORT jobject JNICALL Java_usi_si_seart_treesitter_Node_getFirstNamedChildForByte(
    JNIEnv* env, jclass self, jobject node, jint offset) {
  TSNode child = ts_node_first_named_child_for_byte(__unmarshalNode(env, node), (uint32_t)offset * 2);
  return __marshalNode(env, child);
}

JNIEXPORT jstring JNICALL Java_usi_si_seart_treesitter_Node_getNodeString(
    JNIEnv* env, jclass self, jobject node) {
  char* nodeString = ts_node_string(__unmarshalNode(env, node));
  jstring result = env->NewStringUTF(nodeString);
  free(nodeString);
  return result;
}

JNIEXPORT jobject JNICALL Java_usi_si_seart_treesitter_Node_getNextNamedSibling(
    JNIEnv* env, jclass self, jobject node) {
  TSNode sibling = ts_node_next_named_sibling(__unmarshalNode(env, node));
  if (ts_node_is_null(sibling)) return NULL;
  return __marshalNode(env, sibling);
}

JNIEXPORT jobject JNICALL Java_usi_si_seart_treesitter_Node_getNextSibling(
    JNIEnv* env, jclass self, jobject node) {
  TSNode sibling = ts_node_next_sibling(__unmarshalNode(env, node));
  if (ts_node_is_null(sibling)) return NULL;
  return __marshalNode(env, sibling);
}

JNIEXPORT jobject JNICALL Java_usi_si_seart_treesitter_Node_getPrevNamedSibling(
    JNIEnv* env, jclass self, jobject node) {
  TSNode sibling = ts_node_prev_named_sibling(__unmarshalNode(env, node));
  if (ts_node_is_null(sibling)) return NULL;
  return __marshalNode(env, sibling);
}

JNIEXPORT jobject JNICALL Java_usi_si_seart_treesitter_Node_getPrevSibling(
    JNIEnv* env, jclass self, jobject node) {
  TSNode sibling = ts_node_prev_sibling(__unmarshalNode(env, node));
  if (ts_node_is_null(sibling)) return NULL;
  return __marshalNode(env, sibling);
}

JNIEXPORT jobject JNICALL Java_usi_si_seart_treesitter_Node_getParent(
    JNIEnv* env, jclass self, jobject node) {
  TSNode parent = ts_node_parent(__unmarshalNode(env, node));
  if (ts_node_is_null(parent)) return NULL;
  return __marshalNode(env, parent);
}

JNIEXPORT jint JNICALL Java_usi_si_seart_treesitter_Node_getStartByte(
    JNIEnv* env, jclass self, jobject node) {
  return (jint)ts_node_start_byte(__unmarshalNode(env, node)) / 2;
}

JNIEXPORT jobject JNICALL Java_usi_si_seart_treesitter_Node_getStartPoint(
    JNIEnv* env, jclass self, jobject node) {
  return __marshalPoint(env, ts_node_start_point(__unmarshalNode(env, node)));
}

JNIEXPORT jstring JNICALL Java_usi_si_seart_treesitter_Node_getType(
    JNIEnv* env, jclass self, jobject node) {
  const char* type = ts_node_type(__unmarshalNode(env, node));
  jstring result = env->NewStringUTF(type);
  return result;
}

JNIEXPORT jboolean JNICALL Java_usi_si_seart_treesitter_Node_hasError(
    JNIEnv* env, jclass self, jobject node) {
  return ts_node_has_error(__unmarshalNode(env, node)) ? JNI_TRUE : JNI_FALSE;
}

JNIEXPORT jboolean JNICALL Java_usi_si_seart_treesitter_Node_isExtra(
    JNIEnv* env, jclass self, jobject node) {
  return ts_node_is_extra(__unmarshalNode(env, node)) ? JNI_TRUE : JNI_FALSE;
}

JNIEXPORT jboolean JNICALL Java_usi_si_seart_treesitter_Node_isMissing(
    JNIEnv* env, jclass self, jobject node) {
  return ts_node_is_missing(__unmarshalNode(env, node)) ? JNI_TRUE : JNI_FALSE;
}

JNIEXPORT jboolean JNICALL Java_usi_si_seart_treesitter_Node_isNamed(
    JNIEnv* env, jclass self, jobject node) {
  return ts_node_is_named(__unmarshalNode(env, node)) ? JNI_TRUE : JNI_FALSE;
}

JNIEXPORT jboolean JNICALL Java_usi_si_seart_treesitter_Node_isNull(
    JNIEnv* env, jclass self, jobject node) {
  return ts_node_is_null(__unmarshalNode(env, node)) ? JNI_TRUE : JNI_FALSE;
}

JNIEXPORT jboolean JNICALL Java_usi_si_seart_treesitter_Node_equals(
    JNIEnv* env, jclass self, jobject node, jobject other) {
  TSNode node_1 = __unmarshalNode(env, node);
  TSNode node_2 = __unmarshalNode(env, other);
  return ts_node_eq(node_1, node_2) ? JNI_TRUE : JNI_FALSE;
}
