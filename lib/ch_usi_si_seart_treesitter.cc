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
jfieldID _pointOriginStaticField;
jobject _pointOrigin;

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
jfieldID _treeCursorNodeNameField;
jfieldID _treeCursorNodeTypeField;
jfieldID _treeCursorNodeContentField;
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
jmethodID _queryCursorConstructor;
jfieldID _queryCursorNodeField;
jfieldID _queryCursorQueryField;
jfieldID _queryCursorExecutedField;

jclass _symbolClass;
jmethodID _symbolConstructor;

jclass _treeCursorClass;
jfieldID _treeCursorContext0Field;
jfieldID _treeCursorContext1Field;
jfieldID _treeCursorIdField;
jfieldID _treeCursorTreeField;
jmethodID _treeCursorConstructor;

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
  _loadConstructor(_nodeConstructor, _nodeClass, "(IIIIJLch/usi/si/seart/treesitter/Tree;)V")
  _loadField(_nodeContext0Field, _nodeClass, "context0", "I")
  _loadField(_nodeContext1Field, _nodeClass, "context1", "I")
  _loadField(_nodeContext2Field, _nodeClass, "context2", "I")
  _loadField(_nodeContext3Field, _nodeClass, "context3", "I")
  _loadField(_nodeIdField, _nodeClass, "id", "J")
  _loadField(_nodeTreeField, _nodeClass, "tree", "Lch/usi/si/seart/treesitter/Tree;")

  _loadClass(_pointClass, "ch/usi/si/seart/treesitter/Point")
  _loadConstructor(_pointConstructor, _pointClass, "(II)V")
  _loadField(_pointRowField, _pointClass, "row", "I")
  _loadField(_pointColumnField, _pointClass, "column", "I")
  _loadStaticField(_pointOriginStaticField, _pointClass, "ORIGIN", "Lch/usi/si/seart/treesitter/Point;")
  _loadStaticObject(_pointOrigin, _pointClass, _pointOriginStaticField)

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
    "(Ljava/lang/String;Lch/usi/si/seart/treesitter/Node;)V")
  _loadField(_treeCursorNodeNameField, _treeCursorNodeClass, "name", "Ljava/lang/String;")
  _loadField(_treeCursorNodeTypeField, _treeCursorNodeClass, "type", "Ljava/lang/String;")
  _loadField(_treeCursorNodeContentField, _treeCursorNodeClass, "content", "Ljava/lang/String;")
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
  _loadConstructor(_queryCursorConstructor, _queryCursorClass,
    "(JLch/usi/si/seart/treesitter/Node;Lch/usi/si/seart/treesitter/Query;)V")
  _loadField(_queryCursorNodeField, _queryCursorClass, "node", "Lch/usi/si/seart/treesitter/Node;")
  _loadField(_queryCursorQueryField, _queryCursorClass, "query", "Lch/usi/si/seart/treesitter/Query;")
  _loadField(_queryCursorExecutedField, _queryCursorClass, "executed", "Z")

  _loadClass(_symbolClass, "ch/usi/si/seart/treesitter/Symbol")
  _loadConstructor(_symbolConstructor, _symbolClass, "(IILjava/lang/String;)V")

  _loadClass(_treeCursorClass, "ch/usi/si/seart/treesitter/TreeCursor")
  _loadField(_treeCursorContext0Field, _treeCursorClass, "context0", "I")
  _loadField(_treeCursorContext1Field, _treeCursorClass, "context1", "I")
  _loadField(_treeCursorIdField, _treeCursorClass, "id", "J")
  _loadField(_treeCursorTreeField, _treeCursorClass, "tree", "Lch/usi/si/seart/treesitter/Tree;")
  _loadConstructor(_treeCursorConstructor, _treeCursorClass, "(JIIJLch/usi/si/seart/treesitter/Tree;)V")

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
  _unload(_externalClass)
  _unload(_nodeClass)
  _unload(_pointClass)
  _unload(_pointOrigin)
  _unload(_queryCaptureClass)
  _unload(_queryMatchClass)
  _unload(_inputEditClass)
  _unload(_treeCursorNodeClass)
  _unload(_parserClass)
  _unload(_treeClass)
  _unload(_dotGraphPrinterClass)
  _unload(_queryClass)
  _unload(_queryCursorClass)
  _unload(_symbolClass)
  _unload(_treeCursorClass)
  _unload(_nullPointerExceptionClass)
  _unload(_illegalArgumentExceptionClass)
  _unload(_illegalStateExceptionClass)
  _unload(_ioExceptionClass)
  _unload(_timeoutExceptionClass)
  _unload(_indexOutOfBoundsExceptionClass)
  _unload(_treeSitterExceptionClass)
  _unload(_querySyntaxExceptionClass)
  _unload(_queryNodeTypeExceptionClass)
  _unload(_queryFieldExceptionClass)
  _unload(_queryCaptureExceptionClass)
  _unload(_queryStructureExceptionClass)
  _unload(_parsingExceptionClass)
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
      NULL
    );
  }
}

TSNode __unmarshalNode(JNIEnv* env, jobject nodeObject) {
  jobject treeObject = env->GetObjectField(nodeObject, _nodeTreeField);
  jlong tree = (treeObject == NULL) ? (jlong)0 : __getPointer(env, treeObject);
  jlong node = env->GetLongField(nodeObject, _nodeIdField);
  return (TSNode) {
      {
          (uint32_t)env->GetIntField(nodeObject, _nodeContext0Field),
          (uint32_t)env->GetIntField(nodeObject, _nodeContext1Field),
          (uint32_t)env->GetIntField(nodeObject, _nodeContext2Field),
          (uint32_t)env->GetIntField(nodeObject, _nodeContext3Field),
      },
      (const void*)node,
      (const TSTree*)tree
  };
}

void __copyTree(JNIEnv* env, jobject sourceNodeObject, jobject targetNodeObject) {
  jobject treeObject = env->GetObjectField(sourceNodeObject, _nodeTreeField);
  _setNodeTreeField(targetNodeObject, treeObject);
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
