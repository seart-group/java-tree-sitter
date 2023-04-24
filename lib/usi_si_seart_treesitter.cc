#include "usi_si_seart_treesitter.h"
#include <jni.h>

jlong __getPointer(JNIEnv* env, jclass objectClass, jobject objectInstance) {
  jfieldID pointerField = _getField(objectClass, "pointer", "J");
  return env->GetLongField(objectInstance, pointerField);
}

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
