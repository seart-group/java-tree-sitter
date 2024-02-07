#include "ch_usi_si_seart_treesitter.h"
#include <jni.h>

static jint JNI_VERSION = JNI_VERSION_10;

jclass _stringClass;

jclass _listClass;
jmethodID _listGet;
jmethodID _listOfStaticMethod;

jclass _mapClass;
jclass _mapEntryClass;
jmethodID _mapEntryStaticMethod;

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
jmethodID _pointOriginStaticMethod;

jclass _rangeClass;
jmethodID _rangeConstructor;
jfieldID _rangeStartByteField;
jfieldID _rangeEndByteField;
jfieldID _rangeStartPointField;
jfieldID _rangeEndPointField;

jclass _queryMatchClass;
jmethodID _queryMatchConstructor;
jfieldID _queryMatchIdField;
jfieldID _queryMatchPatternField;

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
jmethodID _parserConstructor;

jclass _treeClass;
jfieldID _treeLanguageField;
jfieldID _treeSourceField;
jmethodID _treeConstructor;

jclass _dotGraphPrinterClass;
jfieldID _dotGraphPrinterTreeField;

jclass _queryClass;
jfieldID _queryLanguageField;
jfieldID _queryPatternsField;
jfieldID _queryCapturesField;
jfieldID _queryStringsField;
jmethodID _queryConstructor;

jclass _patternClass;
jmethodID _patternConstructor;
jfieldID _patternQueryField;
jfieldID _patternIndexField;
jfieldID _patternValueField;
jfieldID _patternEnabledField;

jclass _captureClass;
jmethodID _captureConstructor;
jfieldID _captureQueryField;
jfieldID _captureIndexField;
jfieldID _captureNameField;
jfieldID _captureEnabledField;

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

jclass _lookaheadIteratorClass;
jfieldID _lookaheadIteratorHasNextField;
jfieldID _lookaheadIteratorLanguageField;
jmethodID _lookaheadIteratorConstructor;

jclass _noSuchElementExceptionClass;
jclass _nullPointerExceptionClass;
jclass _illegalArgumentExceptionClass;
jclass _illegalStateExceptionClass;
jclass _ioExceptionClass;

jclass _timeoutExceptionClass;
jmethodID _timeoutExceptionConstructor;

jclass _indexOutOfBoundsExceptionClass;
jmethodID _indexOutOfBoundsExceptionConstructor;

jclass _treeSitterExceptionClass;

jclass _byteOffsetOutOfBoundsExceptionClass;
jmethodID _byteOffsetOutOfBoundsExceptionConstructor;
jclass _pointOutOfBoundsExceptionClass;
jmethodID _pointOutOfBoundsExceptionConstructor;

jclass _querySyntaxExceptionClass;
jmethodID _querySyntaxExceptionConstructor;
jclass _queryNodeTypeExceptionClass;
jmethodID _queryNodeTypeExceptionConstructor;
jclass _queryFieldExceptionClass;
jmethodID _queryFieldExceptionConstructor;
jclass _queryCaptureExceptionClass;
jmethodID _queryCaptureExceptionConstructor;
jclass _queryStructureExceptionClass;
jmethodID _queryStructureExceptionConstructor;

jclass _parsingExceptionClass;
jmethodID _parsingExceptionConstructor;

jclass _incompatibleLanguageExceptionClass;
jmethodID _incompatibleLanguageExceptionConstructor;

const TSPoint POINT_ORIGIN = {
  .row = 0,
  .column = 0,
};

const TSPoint POINT_MAX = {
  .row = UINT32_MAX,
  .column = UINT32_MAX,
};

const TSRange RANGE_FULL = {
  .start_point = POINT_ORIGIN,
  .end_point = POINT_MAX,
  .start_byte = 0,
  .end_byte = UINT32_MAX,
};

jint JNI_OnLoad(JavaVM* vm, void* reserved) {
  JNIEnv* env;
  if (vm->GetEnv(reinterpret_cast<void**>(&env), JNI_VERSION) != JNI_OK) {
    return JNI_ERR;
  }

  _loadClass(_stringClass, "java/lang/String")

  _loadClass(_listClass, "java/util/List")
  _loadMethod(_listGet, _listClass, "get", "(I)Ljava/lang/Object;")
  _loadStaticMethod(_listOfStaticMethod, _listClass, "of", "([Ljava/lang/Object;)Ljava/util/List;")

  _loadClass(_mapClass, "java/util/Map")
  _loadClass(_mapEntryClass, "java/util/Map$Entry")
  _loadStaticMethod(_mapEntryStaticMethod, _mapClass, "entry",
    "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/util/Map$Entry;")

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
  _loadStaticMethod(_pointOriginStaticMethod, _pointClass, "ORIGIN", "()Lch/usi/si/seart/treesitter/Point;")

  _loadClass(_rangeClass, "ch/usi/si/seart/treesitter/Range")
  _loadConstructor(_rangeConstructor, _rangeClass,
    "(IILch/usi/si/seart/treesitter/Point;Lch/usi/si/seart/treesitter/Point;)V")
  _loadField(_rangeStartByteField, _rangeClass, "startByte", "I")
  _loadField(_rangeEndByteField, _rangeClass, "endByte", "I")
  _loadField(_rangeStartPointField, _rangeClass, "startPoint", "Lch/usi/si/seart/treesitter/Point;")
  _loadField(_rangeEndPointField, _rangeClass, "endPoint", "Lch/usi/si/seart/treesitter/Point;")

  _loadClass(_queryMatchClass, "ch/usi/si/seart/treesitter/QueryMatch")
  _loadConstructor(_queryMatchConstructor, _queryMatchClass,
    "(ILch/usi/si/seart/treesitter/Pattern;[Ljava/util/Map$Entry;)V")
  _loadField(_queryMatchIdField, _queryMatchClass, "id", "I")
  _loadField(_queryMatchPatternField, _queryMatchClass, "pattern", "Lch/usi/si/seart/treesitter/Pattern;")
  
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
  _loadConstructor(_parserConstructor, _parserClass, "(JLch/usi/si/seart/treesitter/Language;)V")

  _loadClass(_treeClass, "ch/usi/si/seart/treesitter/Tree")
  _loadField(_treeLanguageField, _treeClass, "language", "Lch/usi/si/seart/treesitter/Language;")
  _loadField(_treeSourceField, _treeClass, "source", "Ljava/lang/String;")
  _loadConstructor(_treeConstructor, _treeClass, "(JLch/usi/si/seart/treesitter/Language;Ljava/lang/String;)V")

  _loadClass(_dotGraphPrinterClass, "ch/usi/si/seart/treesitter/printer/DotGraphPrinter")
  _loadField(_dotGraphPrinterTreeField, _dotGraphPrinterClass, "tree", "Lch/usi/si/seart/treesitter/Tree;")

  _loadClass(_queryClass, "ch/usi/si/seart/treesitter/Query")
  _loadField(_queryLanguageField, _queryClass, "language", "Lch/usi/si/seart/treesitter/Language;")
  _loadField(_queryPatternsField, _queryClass, "patterns", "Ljava/util/List;")
  _loadField(_queryCapturesField, _queryClass, "captures", "Ljava/util/List;")
  _loadField(_queryStringsField, _queryClass, "strings", "Ljava/util/List;")
  _loadConstructor(_queryConstructor, _queryClass,
    "(JLch/usi/si/seart/treesitter/Language;[Lch/usi/si/seart/treesitter/Pattern;[Lch/usi/si/seart/treesitter/Capture;[Ljava/lang/String;)V")

  _loadClass(_patternClass, "ch/usi/si/seart/treesitter/Pattern")
  _loadConstructor(_patternConstructor, _patternClass, "(IZZLjava/lang/String;)V")
  _loadField(_patternQueryField, _patternClass, "query", "Lch/usi/si/seart/treesitter/Query;")
  _loadField(_patternIndexField, _patternClass, "index", "I")
  _loadField(_patternValueField, _patternClass, "value", "Ljava/lang/String;")
  _loadField(_patternEnabledField, _patternClass, "enabled", "Z")

  _loadClass(_captureClass, "ch/usi/si/seart/treesitter/Capture")
  _loadConstructor(_captureConstructor, _captureClass, "(ILjava/lang/String;)V")
  _loadField(_captureQueryField, _captureClass, "query", "Lch/usi/si/seart/treesitter/Query;")
  _loadField(_captureIndexField, _captureClass, "index", "I")
  _loadField(_captureNameField, _captureClass, "name", "Ljava/lang/String;")
  _loadField(_captureEnabledField, _captureClass, "enabled", "Z")

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

  _loadClass(_lookaheadIteratorClass, "ch/usi/si/seart/treesitter/LookaheadIterator")
  _loadField(_lookaheadIteratorHasNextField, _lookaheadIteratorClass, "hasNext", "Z")
  _loadField(_lookaheadIteratorLanguageField, _lookaheadIteratorClass, "language", "Lch/usi/si/seart/treesitter/Language;")
  _loadConstructor(_lookaheadIteratorConstructor, _lookaheadIteratorClass, "(JZLch/usi/si/seart/treesitter/Language;)V")

  _loadClass(_noSuchElementExceptionClass, "java/util/NoSuchElementException")
  _loadClass(_nullPointerExceptionClass, "java/lang/NullPointerException")
  _loadClass(_illegalArgumentExceptionClass, "java/lang/IllegalArgumentException")
  _loadClass(_illegalStateExceptionClass, "java/lang/IllegalStateException")
  _loadClass(_ioExceptionClass, "java/io/IOException")

  _loadClass(_timeoutExceptionClass, "java/util/concurrent/TimeoutException")
  _loadConstructor(_timeoutExceptionConstructor, _timeoutExceptionClass, "()V")

  _loadClass(_indexOutOfBoundsExceptionClass, "java/lang/IndexOutOfBoundsException")
  _loadConstructor(_indexOutOfBoundsExceptionConstructor, _indexOutOfBoundsExceptionClass, "(I)V")

  _loadClass(_treeSitterExceptionClass, "ch/usi/si/seart/treesitter/exception/TreeSitterException")

  _loadClass(_byteOffsetOutOfBoundsExceptionClass, "ch/usi/si/seart/treesitter/exception/ByteOffsetOutOfBoundsException")
  _loadConstructor(_byteOffsetOutOfBoundsExceptionConstructor, _byteOffsetOutOfBoundsExceptionClass, "(I)V")
  _loadClass(_pointOutOfBoundsExceptionClass, "ch/usi/si/seart/treesitter/exception/PointOutOfBoundsException")
  _loadConstructor(_pointOutOfBoundsExceptionConstructor, _pointOutOfBoundsExceptionClass,
    "(Lch/usi/si/seart/treesitter/Point;)V")

  _loadClass(_querySyntaxExceptionClass, "ch/usi/si/seart/treesitter/exception/query/QuerySyntaxException")
  _loadConstructor(_querySyntaxExceptionConstructor, _querySyntaxExceptionClass, "(I)V")
  _loadClass(_queryNodeTypeExceptionClass, "ch/usi/si/seart/treesitter/exception/query/QueryNodeTypeException")
  _loadConstructor(_queryNodeTypeExceptionConstructor, _queryNodeTypeExceptionClass, "(I)V")
  _loadClass(_queryFieldExceptionClass, "ch/usi/si/seart/treesitter/exception/query/QueryFieldException")
  _loadConstructor(_queryFieldExceptionConstructor, _queryFieldExceptionClass, "(I)V")
  _loadClass(_queryCaptureExceptionClass, "ch/usi/si/seart/treesitter/exception/query/QueryCaptureException")
  _loadConstructor(_queryCaptureExceptionConstructor, _queryCaptureExceptionClass, "(I)V")
  _loadClass(_queryStructureExceptionClass, "ch/usi/si/seart/treesitter/exception/query/QueryStructureException")
  _loadConstructor(_queryStructureExceptionConstructor, _queryStructureExceptionClass, "(I)V")

  _loadClass(_parsingExceptionClass, "ch/usi/si/seart/treesitter/exception/parser/ParsingException")
  _loadConstructor(_parsingExceptionConstructor, _parsingExceptionClass, "(Ljava/lang/Throwable;)V")

  _loadClass(_incompatibleLanguageExceptionClass,
    "ch/usi/si/seart/treesitter/exception/parser/IncompatibleLanguageException")
  _loadConstructor(_incompatibleLanguageExceptionConstructor, _incompatibleLanguageExceptionClass,
    "(Lch/usi/si/seart/treesitter/Language;)V")

  return JNI_VERSION;
}

void JNI_OnUnload(JavaVM* vm, void* reserved) {
  JNIEnv* env;
  vm->GetEnv(reinterpret_cast<void**>(&env), JNI_VERSION);
  _unload(_stringClass)
  _unload(_listClass)
  _unload(_mapClass)
  _unload(_mapEntryClass)
  _unload(_externalClass)
  _unload(_nodeClass)
  _unload(_pointClass)
  _unload(_rangeClass)
  _unload(_queryMatchClass)
  _unload(_inputEditClass)
  _unload(_treeCursorNodeClass)
  _unload(_parserClass)
  _unload(_treeClass)
  _unload(_dotGraphPrinterClass)
  _unload(_queryClass)
  _unload(_patternClass)
  _unload(_captureClass)
  _unload(_queryCursorClass)
  _unload(_symbolClass)
  _unload(_treeCursorClass)
  _unload(_lookaheadIteratorClass)
  _unload(_noSuchElementExceptionClass)
  _unload(_nullPointerExceptionClass)
  _unload(_illegalArgumentExceptionClass)
  _unload(_illegalStateExceptionClass)
  _unload(_ioExceptionClass)
  _unload(_timeoutExceptionClass)
  _unload(_indexOutOfBoundsExceptionClass)
  _unload(_treeSitterExceptionClass)
  _unload(_byteOffsetOutOfBoundsExceptionClass)
  _unload(_pointOutOfBoundsExceptionClass)
  _unload(_querySyntaxExceptionClass)
  _unload(_queryNodeTypeExceptionClass)
  _unload(_queryFieldExceptionClass)
  _unload(_queryCaptureExceptionClass)
  _unload(_queryStructureExceptionClass)
  _unload(_parsingExceptionClass)
  _unload(_incompatibleLanguageExceptionClass)
}

ComparisonResult intcmp(uint32_t x, uint32_t y) {
  return (x < y) ? LT : ((x == y) ? EQ : GT);
}

jint __throwNSE(JNIEnv* env, const char* message) {
  return _throwNew(_noSuchElementExceptionClass, message);
}

jint __throwNPE(JNIEnv* env, const char* message) {
  return _throwNew(_nullPointerExceptionClass, message);
}

jint __throwIAE(JNIEnv* env, const char* message) {
  return _throwNew(_illegalArgumentExceptionClass, message);
}

jint __throwISE(JNIEnv* env, const char* message) {
  return _throwNew(_illegalStateExceptionClass, message);
}

jint __throwIOE(JNIEnv* env, const char* message) {
  return _throwNew(_ioExceptionClass, message);
}

jint __throwIOB(JNIEnv* env, jint index) {
  jobject exception = env->NewObject(
    _indexOutOfBoundsExceptionClass,
    _indexOutOfBoundsExceptionConstructor,
    index
  );
  return env->Throw((jthrowable)exception);
}

jint __throwBOB(JNIEnv* env, jint index) {
  jobject exception = env->NewObject(
    _byteOffsetOutOfBoundsExceptionClass,
    _byteOffsetOutOfBoundsExceptionConstructor,
    index
  );
  return env->Throw((jthrowable)exception);
}

jint __throwPOB(JNIEnv* env, jobject pointObject) {
  jobject exception = env->NewObject(
    _pointOutOfBoundsExceptionClass,
    _pointOutOfBoundsExceptionConstructor,
    pointObject
  );
  return env->Throw((jthrowable)exception);
}

jint __throwILE(JNIEnv* env, jobject languageObject) {
  jobject exception = env->NewObject(
    _incompatibleLanguageExceptionClass,
    _incompatibleLanguageExceptionConstructor,
    languageObject
  );
  return env->Throw((jthrowable)exception);
}

jlong __getPointer(JNIEnv* env, jobject objectInstance) {
  return env->GetLongField(objectInstance, _externalPointerField);
}

void __clearPointer(JNIEnv* env, jobject objectInstance) {
  env->SetLongField(objectInstance, _externalPointerField, (jlong)0);
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

ComparisonResult __comparePoints(TSPoint left, TSPoint right) {
  ComparisonResult result = intcmp(left.row, right.row);
  return (result != EQ) ? result : intcmp(left.column, right.column);
}

ComparisonResult __compareRanges(TSRange left, TSRange right) {
  ComparisonResult result;
  result = __comparePoints(left.start_point, right.start_point);
  if (result != EQ) return result;
  result = __comparePoints(left.end_point, right.end_point);
  if (result != EQ) return result;
  result = intcmp(left.start_byte, right.start_byte);
  if (result != EQ) return result;
  return intcmp(left.end_byte, right.end_byte);
}

bool __rangeIsFull(TSRange range) {
  return __compareRanges(range, RANGE_FULL) == EQ;
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
  // Not sure why I need to multiply by two, probably because of utf-16
  return (TSPoint) {
    (uint32_t)env->GetIntField(pointObject, _pointRowField),
    (uint32_t)env->GetIntField(pointObject, _pointColumnField) * 2,
  };
}

jobject __marshalRange(JNIEnv* env, TSRange range) {
  return env->NewObject(
    _rangeClass,
    _rangeConstructor,
    (jint)range.start_byte / 2,
    (jint)range.end_byte / 2,
    __marshalPoint(env, range.start_point),
    __marshalPoint(env, range.end_point)
  );
}

TSRange __unmarshalRange(JNIEnv* env, jobject rangeObject) {
  return (TSRange) {
    __unmarshalPoint(env, env->GetObjectField(rangeObject, _rangeStartPointField)),
    __unmarshalPoint(env, env->GetObjectField(rangeObject, _rangeEndPointField)),
    (uint32_t)env->GetIntField(rangeObject, _rangeStartByteField) * 2,
    (uint32_t)env->GetIntField(rangeObject, _rangeEndByteField) * 2
  };
}

TSInputEdit __unmarshalInputEdit(JNIEnv* env, jobject inputEditObject) {
  return (TSInputEdit) {
    (uint32_t)env->GetIntField(inputEditObject, _inputEditStartByteField) * 2,
    (uint32_t)env->GetIntField(inputEditObject, _inputEditOldEndByteField) * 2,
    (uint32_t)env->GetIntField(inputEditObject, _inputEditNewEndByteField) * 2,
    __unmarshalPoint(env, env->GetObjectField(inputEditObject, _inputEditStartPointField)),
    __unmarshalPoint(env, env->GetObjectField(inputEditObject, _inputEditOldEndPointField)),
    __unmarshalPoint(env, env->GetObjectField(inputEditObject, _inputEditNewEndPointField)),
  };
}

const TSLanguage* __unmarshalLanguage(JNIEnv* env, jobject languageObject) {
  jclass languageClass = env->GetObjectClass(languageObject);
  jfieldID languageIdField = env->GetFieldID(languageClass, "id", "J");
  jlong languageId = env->GetLongField(languageObject, languageIdField);
  return (const TSLanguage*)languageId;
}
