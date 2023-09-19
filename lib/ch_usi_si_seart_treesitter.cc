#include "ch_usi_si_seart_treesitter.h"
#include <jni.h>

static jint JNI_VERSION = JNI_VERSION_10;

jclass _externalClass;
jfieldID _externalPointerField;

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

jclass _queryMatchClass;
jmethodID _queryMatchConstructor;
jfieldID _queryMatchIdField;
jfieldID _queryMatchPatternIndexField;
jfieldID _queryMatchCapturesField;

jclass _inputEditClass;
jmethodID _inputEditConstructor;
jfieldID _inputEditStartByteField;
jfieldID _inputEditOldEndByteField;
jfieldID _inputEditNewEndByteField;
jfieldID _inputEditStartPointField;
jfieldID _inputEditOldEndPointField;
jfieldID _inputEditNewEndPointField;

jclass _treeCursorNodeClass;
jmethodID _treeCursorNodeConstructor;
jfieldID _treeCursorNodeTypeField;
jfieldID _treeCursorNodeNameField;
jfieldID _treeCursorNodeStartByteField;
jfieldID _treeCursorNodeEndByteField;
jfieldID _treeCursorNodeStartPointField;
jfieldID _treeCursorNodeEndPointField;
jfieldID _treeCursorNodeIsNamed;

jclass _parserClass;
jfieldID _parserLanguageField;

jclass _treeClass;
jmethodID _treeConstructor;

jclass _dotGraphPrinterClass;
jfieldID _dotGraphPrinterTreeField;

jclass _queryClass;

jclass _queryCursorClass;
jfieldID _queryCursorNodeField;
jfieldID _queryCursorQueryField;
jfieldID _queryCursorExecutedField;

jclass _treeCursorClass;

jclass _nullPointerExceptionClass;
jclass _illegalArgumentExceptionClass;
jclass _illegalStateExceptionClass;
jclass _ioExceptionClass;

jclass _timeoutExceptionClass;
jmethodID _timeoutExceptionConstructor;

jclass _indexOutOfBoundsExceptionClass;
jmethodID _indexOutOfBoundsExceptionConstructor;

jclass _treeSitterExceptionClass;

jclass _querySyntaxExceptionClass;
jclass _queryNodeTypeExceptionClass;
jclass _queryFieldExceptionClass;
jclass _queryCaptureExceptionClass;
jclass _queryStructureExceptionClass;

jclass _parsingExceptionClass;
jmethodID _parsingExceptionConstructor;

jint JNI_OnLoad(JavaVM* vm, void* reserved) {
  JNIEnv* env;
  if (vm->GetEnv(reinterpret_cast<void**>(&env), JNI_VERSION) != JNI_OK) {
    return JNI_ERR;
  }

  _loadClass(_externalClass, "ch/usi/si/seart/treesitter/External")
  _loadField(_externalPointerField, _externalClass, "pointer", "J")

  _loadClass(_nodeClass, "ch/usi/si/seart/treesitter/Node")
  _loadConstructor(_nodeConstructor, _nodeClass, "(IIIIJJ)V")
  _loadField(_nodeContext0Field, _nodeClass, "context0", "I")
  _loadField(_nodeContext1Field, _nodeClass, "context1", "I")
  _loadField(_nodeContext2Field, _nodeClass, "context2", "I")
  _loadField(_nodeContext3Field, _nodeClass, "context3", "I")
  _loadField(_nodeIdField, _nodeClass, "id", "J")
  _loadField(_nodeTreeField, _nodeClass, "tree", "J")

  _loadClass(_pointClass, "ch/usi/si/seart/treesitter/Point")
  _loadConstructor(_pointConstructor, _pointClass, "(II)V")
  _loadField(_pointRowField, _pointClass, "row", "I")
  _loadField(_pointColumnField, _pointClass, "column", "I")

  _loadClass(_queryCaptureClass, "ch/usi/si/seart/treesitter/QueryCapture")
  _loadConstructor(_queryCaptureConstructor, _queryCaptureClass, "(Lch/usi/si/seart/treesitter/Node;I)V")
  _loadField(_queryCaptureNodeField, _queryCaptureClass, "node", "Lch/usi/si/seart/treesitter/Node;")
  _loadField(_queryCaptureIndexField, _queryCaptureClass, "index", "I")

  _loadClass(_queryMatchClass, "ch/usi/si/seart/treesitter/QueryMatch")
  _loadConstructor(_queryMatchConstructor, _queryMatchClass, "(II[Lch/usi/si/seart/treesitter/QueryCapture;)V")
  _loadField(_queryMatchIdField, _queryMatchClass, "id", "I")
  _loadField(_queryMatchPatternIndexField, _queryMatchClass, "patternIndex", "I")
  _loadField(_queryMatchCapturesField, _queryMatchClass, "captures", "[Lch/usi/si/seart/treesitter/QueryCapture;")
  
  _loadClass(_inputEditClass, "ch/usi/si/seart/treesitter/InputEdit")
  _loadConstructor(_inputEditConstructor, _inputEditClass,
    "(IIILch/usi/si/seart/treesitter/Point;Lch/usi/si/seart/treesitter/Point;Lch/usi/si/seart/treesitter/Point;)V")
  _loadField(_inputEditStartByteField, _inputEditClass, "startByte", "I")
  _loadField(_inputEditOldEndByteField, _inputEditClass, "oldEndByte", "I")
  _loadField(_inputEditNewEndByteField, _inputEditClass, "newEndByte", "I")
  _loadField(_inputEditStartPointField, _inputEditClass, "startPoint", "Lch/usi/si/seart/treesitter/Point;")
  _loadField(_inputEditOldEndPointField, _inputEditClass, "oldEndPoint", "Lch/usi/si/seart/treesitter/Point;")
  _loadField(_inputEditNewEndPointField, _inputEditClass, "newEndPoint", "Lch/usi/si/seart/treesitter/Point;")

  _loadClass(_treeCursorNodeClass, "ch/usi/si/seart/treesitter/TreeCursorNode")
  _loadConstructor(_treeCursorNodeConstructor, _treeCursorNodeClass,
      "(Ljava/lang/String;Ljava/lang/String;IILch/usi/si/seart/treesitter/Point;Lch/usi/si/seart/treesitter/Point;Z)V")
  _loadField(_treeCursorNodeTypeField, _treeCursorNodeClass, "type", "Ljava/lang/String;")
  _loadField(_treeCursorNodeNameField, _treeCursorNodeClass, "name", "Ljava/lang/String;")
  _loadField(_treeCursorNodeStartByteField, _treeCursorNodeClass, "startByte", "I")
  _loadField(_treeCursorNodeEndByteField, _treeCursorNodeClass, "endByte", "I")
  _loadField(_treeCursorNodeStartPointField, _treeCursorNodeClass, "startPoint", "Lch/usi/si/seart/treesitter/Point;")
  _loadField(_treeCursorNodeEndPointField, _treeCursorNodeClass, "endPoint", "Lch/usi/si/seart/treesitter/Point;")
  _loadField(_treeCursorNodeIsNamed, _treeCursorNodeClass, "isNamed", "Z")

  _loadClass(_parserClass, "ch/usi/si/seart/treesitter/Parser")
  _loadField(_parserLanguageField, _parserClass, "language", "Lch/usi/si/seart/treesitter/Language;")

  _loadClass(_treeClass, "ch/usi/si/seart/treesitter/Tree")
  _loadConstructor(_treeConstructor, _treeClass, "(JLch/usi/si/seart/treesitter/Language;Ljava/lang/String;)V")

  _loadClass(_dotGraphPrinterClass, "ch/usi/si/seart/treesitter/printer/DotGraphPrinter")
  _loadField(_dotGraphPrinterTreeField, _dotGraphPrinterClass, "tree", "Lch/usi/si/seart/treesitter/Tree;")

  _loadClass(_queryClass, "ch/usi/si/seart/treesitter/Query")

  _loadClass(_queryCursorClass, "ch/usi/si/seart/treesitter/QueryCursor")
  _loadField(_queryCursorNodeField, _queryCursorClass, "node", "Lch/usi/si/seart/treesitter/Node;")
  _loadField(_queryCursorQueryField, _queryCursorClass, "query", "Lch/usi/si/seart/treesitter/Query;")
  _loadField(_queryCursorExecutedField, _queryCursorClass, "executed", "Z")

  _loadClass(_treeCursorClass, "ch/usi/si/seart/treesitter/TreeCursor")

  _loadClass(_nullPointerExceptionClass, "java/lang/NullPointerException")
  _loadClass(_illegalArgumentExceptionClass, "java/lang/IllegalArgumentException")
  _loadClass(_illegalStateExceptionClass, "java/lang/IllegalStateException")
  _loadClass(_ioExceptionClass, "java/io/IOException")

  _loadClass(_timeoutExceptionClass, "java/util/concurrent/TimeoutException")
  _loadConstructor(_timeoutExceptionConstructor, _timeoutExceptionClass, "()V")

  _loadClass(_indexOutOfBoundsExceptionClass, "java/lang/IndexOutOfBoundsException")
  _loadConstructor(_indexOutOfBoundsExceptionConstructor, _indexOutOfBoundsExceptionClass, "(I)V")

  _loadClass(_treeSitterExceptionClass, "ch/usi/si/seart/treesitter/exception/TreeSitterException")

  _loadClass(_querySyntaxExceptionClass, "ch/usi/si/seart/treesitter/exception/query/QuerySyntaxException")
  _loadClass(_queryNodeTypeExceptionClass, "ch/usi/si/seart/treesitter/exception/query/QueryNodeTypeException")
  _loadClass(_queryFieldExceptionClass, "ch/usi/si/seart/treesitter/exception/query/QueryFieldException")
  _loadClass(_queryCaptureExceptionClass, "ch/usi/si/seart/treesitter/exception/query/QueryCaptureException")
  _loadClass(_queryStructureExceptionClass, "ch/usi/si/seart/treesitter/exception/query/QueryStructureException")

  _loadClass(_parsingExceptionClass, "ch/usi/si/seart/treesitter/exception/ParsingException")
  _loadConstructor(_parsingExceptionConstructor, _parsingExceptionClass, "(Ljava/lang/Throwable;)V")

  return JNI_VERSION;
}

void JNI_OnUnload(JavaVM* vm, void* reserved) {
  JNIEnv* env;
  vm->GetEnv(reinterpret_cast<void**>(&env), JNI_VERSION);
  _unloadClass(_externalClass)
  _unloadClass(_nodeClass)
  _unloadClass(_pointClass)
  _unloadClass(_queryCaptureClass)
  _unloadClass(_queryMatchClass)
  _unloadClass(_inputEditClass)
  _unloadClass(_treeCursorNodeClass)
  _unloadClass(_parserClass)
  _unloadClass(_treeClass)
  _unloadClass(_dotGraphPrinterClass)
  _unloadClass(_queryClass)
  _unloadClass(_queryCursorClass)
  _unloadClass(_treeCursorClass)
  _unloadClass(_nullPointerExceptionClass)
  _unloadClass(_illegalArgumentExceptionClass)
  _unloadClass(_illegalStateExceptionClass)
  _unloadClass(_ioExceptionClass)
  _unloadClass(_timeoutExceptionClass)
  _unloadClass(_indexOutOfBoundsExceptionClass)
  _unloadClass(_treeSitterExceptionClass)
  _unloadClass(_querySyntaxExceptionClass)
  _unloadClass(_queryNodeTypeExceptionClass)
  _unloadClass(_queryFieldExceptionClass)
  _unloadClass(_queryCaptureExceptionClass)
  _unloadClass(_queryStructureExceptionClass)
  _unloadClass(_parsingExceptionClass)
}

jlong __getPointer(JNIEnv* env, jobject objectInstance) {
  return env->GetLongField(objectInstance, _externalPointerField);
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
  return env->NewObject(
    _queryCaptureClass,
    _queryCaptureConstructor,
    __marshalNode(env, capture.node),
    capture.index
  );
}

jobject __marshalQueryMatch(JNIEnv* env, TSQueryMatch match) {
  jobjectArray captures = (*env).NewObjectArray(match.capture_count, _queryCaptureClass, NULL);
  for (int i = 0; i < match.capture_count; i++) {
    jobject capture = __marshalQueryCapture(env, match.captures[i]);
    env->SetObjectArrayElement(captures, i, capture);
  }
  return env->NewObject(
    _queryMatchClass,
    _queryMatchConstructor,
    match.id,
    match.pattern_index,
    captures
  );
}

TSInputEdit __unmarshalInputEdit(JNIEnv* env, jobject inputEditObject) {
  return (TSInputEdit) {
    (uint32_t)env->GetIntField(inputEditObject, _inputEditStartByteField),
    (uint32_t)env->GetIntField(inputEditObject, _inputEditOldEndByteField),
    (uint32_t)env->GetIntField(inputEditObject, _inputEditNewEndByteField),
    __unmarshalPoint(env, env->GetObjectField(inputEditObject, _inputEditStartPointField)),
    __unmarshalPoint(env, env->GetObjectField(inputEditObject, _inputEditOldEndPointField)),
    __unmarshalPoint(env, env->GetObjectField(inputEditObject, _inputEditNewEndPointField)),
  };
}

jobject __marshalTreeCursorNode(JNIEnv* env, TreeCursorNode node) {
  jobject startPointObject = __marshalPoint(env, node.startPoint);
  jobject endPointObject = __marshalPoint(env, node.endPoint);
  return env->NewObject(
    _treeCursorNodeClass,
    _treeCursorNodeConstructor,
    env->NewStringUTF(node.type),
    env->NewStringUTF(node.name),
    node.startByte,
    node.endByte,
    startPointObject,
    endPointObject,
    node.isNamed
  );
}
