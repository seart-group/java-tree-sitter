#include "ch_usi_si_seart_treesitter.h"
#include <jni.h>

static jint JNI_VERSION = JNI_VERSION_10;

jclass _nodeClass;
jmethodID _nodeConstructor;
jfieldID _nodeContext0Field;
jfieldID _nodeContext1Field;
jfieldID _nodeContext2Field;
jfieldID _nodeContext3Field;
jfieldID _nodeIdField;
jfieldID _nodeTreeField;

jclass _pointClass;
jmethodID _pointConstructor;
jfieldID _pointRowField;
jfieldID _pointColumnField;

jclass _queryCaptureClass;
jmethodID _queryCaptureConstructor;
jfieldID _queryCaptureNodeField;
jfieldID _queryCaptureIndexField;

jint JNI_OnLoad(JavaVM* vm, void* reserved) {
  JNIEnv* env;
  if (vm->GetEnv(reinterpret_cast<void**>(&env), JNI_VERSION) != JNI_OK) {
    return JNI_ERR;
  }

  _loadClass(_nodeClass, "ch/usi/si/seart/treesitter/Node");
  _loadConstructor(_nodeConstructor, _nodeClass, "(IIIIJJ)V");
  _loadField(_nodeContext0Field, _nodeClass, "context0", "I");
  _loadField(_nodeContext1Field, _nodeClass, "context1", "I");
  _loadField(_nodeContext2Field, _nodeClass, "context2", "I");
  _loadField(_nodeContext3Field, _nodeClass, "context3", "I");
  _loadField(_nodeIdField, _nodeClass, "id", "J");
  _loadField(_nodeTreeField, _nodeClass, "tree", "J");

  _loadClass(_pointClass, "ch/usi/si/seart/treesitter/Point");
  _loadConstructor(_pointConstructor, _pointClass, "(II)V");
  _loadField(_pointRowField, _pointClass, "row", "I");
  _loadField(_pointColumnField, _pointClass, "column", "I");

  _loadClass(_queryCaptureClass, "ch/usi/si/seart/treesitter/QueryCapture");
  _loadConstructor(_queryCaptureConstructor, _queryCaptureClass, "(Lch/usi/si/seart/treesitter/Node;I)V");
  _loadField(_queryCaptureNodeField, _queryCaptureClass, "node", "Lch/usi/si/seart/treesitter/Node;");
  _loadField(_queryCaptureIndexField, _queryCaptureClass, "index", "I");

  return JNI_VERSION;
}

void JNI_OnUnload(JavaVM* vm, void* reserved) {
  JNIEnv* env;
  vm->GetEnv(reinterpret_cast<void**>(&env), JNI_VERSION);
  _unloadClass(_nodeClass);
  _unloadClass(_pointClass);
  _unloadClass(_queryCaptureClass);
}

jlong __getPointer(JNIEnv* env, jclass objectClass, jobject objectInstance) {
  jfieldID pointerField = _getField(objectClass, "pointer", "J");
  return env->GetLongField(objectInstance, pointerField);
}

jobject __marshalNode(JNIEnv* env, TSNode node) {
  if (node.id == 0) {
    return NULL;
  } else {
    return env->NewObject(
      _nodeClass,
      _nodeConstructor,
      node.context[0],
      node.context[1],
      node.context[2],
      node.context[3],
      (jlong)node.id,
      (jlong)node.tree
    );
  }
}

TSNode __unmarshalNode(JNIEnv* env, jobject nodeObject) {
  return (TSNode){
      {
          (uint32_t)env->GetIntField(nodeObject, _nodeContext0Field),
          (uint32_t)env->GetIntField(nodeObject, _nodeContext1Field),
          (uint32_t)env->GetIntField(nodeObject, _nodeContext2Field),
          (uint32_t)env->GetIntField(nodeObject, _nodeContext3Field),
      },
      (const void*)env->GetLongField(nodeObject, _nodeIdField),
      (const TSTree*)env->GetLongField(nodeObject, _nodeTreeField)};
}

jobject __marshalPoint(JNIEnv* env, TSPoint point) {
  // Not sure why I need to divide by two, probably because of utf-16
  return env->NewObject(
    _pointClass,
    _pointConstructor,
    point.row,
    point.column / 2
  );
}

TSPoint __unmarshalPoint(JNIEnv* env, jobject pointObject) {
  return (TSPoint) {
    (uint32_t)env->GetIntField(pointObject, _pointRowField),
    (uint32_t)env->GetIntField(pointObject, _pointColumnField),
  };
}

jobject __marshalQueryCapture(JNIEnv* env, TSQueryCapture capture) {
  jobject nodeObject = __marshalNode(env, capture.node);
  return env->NewObject(
    _queryCaptureClass,
    _queryCaptureConstructor,
    capture.index,
    nodeObject
  );
}

jobject __marshalQueryMatch(JNIEnv* env, TSQueryMatch match) {
  jclass queryMatchClass = _getClass("ch/usi/si/seart/treesitter/QueryMatch");
  jfieldID queryMatchIdField = _getField(queryMatchClass, "id", "I");
  jfieldID queryMatchPatternIndexField = _getField(queryMatchClass, "patternIndex", "I");
  jfieldID queryMatchCapturesField = _getField(queryMatchClass, "captures", "[Lch/usi/si/seart/treesitter/QueryCapture;");
  jobject matchInstance = env->AllocObject(queryMatchClass);
  env->SetIntField(matchInstance, queryMatchIdField, match.id);
  env->SetIntField(matchInstance, queryMatchPatternIndexField, match.pattern_index);

  jobjectArray captures = (*env).NewObjectArray(match.capture_count, _queryCaptureClass, NULL);
  for (int i = 0; i < match.capture_count; i++) {
    env->SetObjectArrayElement(captures, i, __marshalQueryCapture(env, match.captures[i]));
  }
  env->SetObjectField(matchInstance, queryMatchCapturesField, captures);

  return matchInstance;
}

TSInputEdit __unmarshalInputEdit(JNIEnv* env, jobject inputEdit) {
  jclass inputEditClass = _getClass("ch/usi/si/seart/treesitter/InputEdit");
  jfieldID inputEditStartByteField = _getField(inputEditClass, "startByte", "I");
  jfieldID inputEditOldEndByteField = _getField(inputEditClass, "oldEndByte", "I");
  jfieldID inputEditNewEndByteField = _getField(inputEditClass, "newEndByte", "I");
  jfieldID inputEditStartPointField = _getField(inputEditClass, "startPoint", "Lch/usi/si/seart/treesitter/Point;");
  jfieldID inputEditOldEndPointField = _getField(inputEditClass, "oldEndPoint", "Lch/usi/si/seart/treesitter/Point;");
  jfieldID inputEditNewEndPointField = _getField(inputEditClass, "newEndPoint", "Lch/usi/si/seart/treesitter/Point;");
  return (TSInputEdit) {
    (uint32_t)env->GetIntField(inputEdit, inputEditStartByteField),
    (uint32_t)env->GetIntField(inputEdit, inputEditOldEndByteField),
    (uint32_t)env->GetIntField(inputEdit, inputEditNewEndByteField),
    __unmarshalPoint(env, env->GetObjectField(inputEdit, inputEditStartPointField)),
    __unmarshalPoint(env, env->GetObjectField(inputEdit, inputEditOldEndPointField)),
    __unmarshalPoint(env, env->GetObjectField(inputEdit, inputEditNewEndPointField)),
  };
}

jobject __marshalTreeCursorNode(JNIEnv* env, TreeCursorNode node) {
  jclass treeCursorNodeClass = _getClass("ch/usi/si/seart/treesitter/TreeCursorNode");
  jfieldID treeCursorNodeTypeField = _getField(treeCursorNodeClass, "type", "Ljava/lang/String;");
  jfieldID treeCursorNodeNameField = _getField(treeCursorNodeClass, "name", "Ljava/lang/String;");
  jfieldID treeCursorNodeStartByteField = _getField(treeCursorNodeClass, "startByte", "I");
  jfieldID treeCursorNodeEndByteField = _getField(treeCursorNodeClass, "endByte", "I");
  jfieldID treeCursorNodeStartPointField = _getField(treeCursorNodeClass, "startPoint", "Lch/usi/si/seart/treesitter/Point;");
  jfieldID treeCursorNodeEndPointField = _getField(treeCursorNodeClass, "endPoint", "Lch/usi/si/seart/treesitter/Point;");
  jfieldID treeCursorNodeIsNamed = _getField(treeCursorNodeClass, "isNamed", "Z");
  jobject treeCursorNodeInstance = env->AllocObject(treeCursorNodeClass);
  env->SetObjectField(treeCursorNodeInstance, treeCursorNodeTypeField, env->NewStringUTF(node.type));
  env->SetObjectField(treeCursorNodeInstance, treeCursorNodeNameField, env->NewStringUTF(node.name));
  env->SetIntField(treeCursorNodeInstance, treeCursorNodeStartByteField, node.startByte);
  env->SetIntField(treeCursorNodeInstance, treeCursorNodeEndByteField, node.endByte);
  env->SetObjectField(treeCursorNodeInstance, treeCursorNodeStartPointField, __marshalPoint(env, node.startPoint));
  env->SetObjectField(treeCursorNodeInstance, treeCursorNodeEndPointField, __marshalPoint(env, node.endPoint));
  env->SetBooleanField(treeCursorNodeInstance, treeCursorNodeIsNamed, node.isNamed);
  return treeCursorNodeInstance;
}
