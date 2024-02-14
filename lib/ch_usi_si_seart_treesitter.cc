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

jclass _collectionsClass;
jmethodID _collectionsEmptyListStaticMethod;

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
jfieldID _patternStartOffsetField;
jfieldID _patternValueField;
jfieldID _patternPredicatesField;
jfieldID _patternEnabledField;

jclass _predicateClass;
jmethodID _predicateConstructor;
jfieldID _predicatePatternField;
jfieldID _predicateStepsField;

jclass _predicateStepClass;
jmethodID _predicateStepConstructor;

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
jmethodID _treeSitterExceptionConstructor;

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

jclass _loggerClass;
jmethodID _loggerDebugMethod;

jclass _markerFactoryClass;
jmethodID _markerFactoryGetMarkerStaticMethod;

const TSPoint POINT_ORIGIN = {
  .row = 0,
  .column = 0,
};

const TSPoint POINT_MAX = {
  .row = UINT32_MAX,
  .column = UINT32_MAX,
};

const TSRange RANGE_DEFAULT = {
  .start_point = POINT_ORIGIN,
  .end_point = POINT_MAX,
  .start_byte = 0,
  .end_byte = UINT32_MAX,
};

const char* JNI_CALL_RESULT_NAMES[] = {
  "OK",
  "Unknown Error",
  "Thread Detached from the VM",
  "JNI Version Error",
  "Not Enough Memory",
  "VM Already Created",
  "Invalid Arguments"
};

const char* LOG_TYPE_NAMES[] = {
  "PARSE",
  "LEX"
};

JavaVM* JVM = NULL;

jclass QUERY_EXCEPTION_CLASSES[7];
jmethodID QUERY_EXCEPTION_CONSTRUCTORS[7];

jint JNI_OnLoad(JavaVM* vm, void* reserved) {
  JVM = vm;
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

  _loadClass(_collectionsClass, "java/util/Collections")
  _loadStaticMethod(_collectionsEmptyListStaticMethod, _collectionsClass, "emptyList", "()Ljava/util/List;")

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
  _loadConstructor(_patternConstructor, _patternClass,
    "(IIZZLjava/lang/String;[Lch/usi/si/seart/treesitter/Predicate;)V")
  _loadField(_patternQueryField, _patternClass, "query", "Lch/usi/si/seart/treesitter/Query;")
  _loadField(_patternIndexField, _patternClass, "index", "I")
  _loadField(_patternStartOffsetField, _patternClass, "startOffset", "I")
  _loadField(_patternValueField, _patternClass, "value", "Ljava/lang/String;")
  _loadField(_patternPredicatesField, _patternClass, "predicates", "Ljava/util/List;")
  _loadField(_patternEnabledField, _patternClass, "enabled", "Z")

  _loadClass(_predicateClass, "ch/usi/si/seart/treesitter/Predicate")
  _loadConstructor(_predicateConstructor, _predicateClass,
    "(Lch/usi/si/seart/treesitter/Pattern;[Lch/usi/si/seart/treesitter/Predicate$Step;)V")
  _loadField(_predicatePatternField, _predicateClass, "pattern", "Lch/usi/si/seart/treesitter/Pattern;")
  _loadField(_predicateStepsField, _predicateClass, "steps", "Ljava/util/List;")

  _loadClass(_predicateStepClass, "ch/usi/si/seart/treesitter/Predicate$Step")
  _loadConstructor(_predicateStepConstructor, _predicateStepClass, "(ILjava/lang/String;)V")

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
  _loadConstructor(_treeSitterExceptionConstructor, _treeSitterExceptionClass, "()V")

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

  _loadClass(_loggerClass, "org/slf4j/Logger")
  if (_loggerClass != NULL) {
    _loadMethod(_loggerDebugMethod, _loggerClass, "debug", "(Lorg/slf4j/Marker;Ljava/lang/String;)V");
  }

  _loadClass(_markerFactoryClass, "org/slf4j/MarkerFactory")
  if (_markerFactoryClass != NULL) {
    _loadStaticMethod(_markerFactoryGetMarkerStaticMethod, _markerFactoryClass, "getMarker",
      "(Ljava/lang/String;)Lorg/slf4j/Marker;");
  }

  QUERY_EXCEPTION_CLASSES[0] = NULL;
  QUERY_EXCEPTION_CLASSES[1] = _querySyntaxExceptionClass;
  QUERY_EXCEPTION_CLASSES[2] = _queryNodeTypeExceptionClass;
  QUERY_EXCEPTION_CLASSES[3] = _queryFieldExceptionClass;
  QUERY_EXCEPTION_CLASSES[4] = _queryCaptureExceptionClass;
  QUERY_EXCEPTION_CLASSES[5] = _queryStructureExceptionClass;
  QUERY_EXCEPTION_CLASSES[6] = _incompatibleLanguageExceptionClass;

  QUERY_EXCEPTION_CONSTRUCTORS[0] = NULL;
  QUERY_EXCEPTION_CONSTRUCTORS[1] = _querySyntaxExceptionConstructor;
  QUERY_EXCEPTION_CONSTRUCTORS[2] = _queryNodeTypeExceptionConstructor;
  QUERY_EXCEPTION_CONSTRUCTORS[3] = _queryFieldExceptionConstructor;
  QUERY_EXCEPTION_CONSTRUCTORS[4] = _queryCaptureExceptionConstructor;
  QUERY_EXCEPTION_CONSTRUCTORS[5] = _queryStructureExceptionConstructor;
  QUERY_EXCEPTION_CONSTRUCTORS[6] = _incompatibleLanguageExceptionConstructor;

  return JNI_VERSION;
}

void JNI_OnUnload(JavaVM* vm, void* reserved) {
  JNIEnv* env;
  vm->GetEnv(reinterpret_cast<void**>(&env), JNI_VERSION);
  _unload(_stringClass)
  _unload(_listClass)
  _unload(_mapClass)
  _unload(_mapEntryClass)
  _unload(_collectionsClass)
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
  _unload(_predicateClass)
  _unload(_predicateStepClass)
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
  _unload(_loggerClass)
  _unload(_markerFactoryClass)
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
  jthrowable exception = _newThrowable(
    _indexOutOfBoundsExceptionClass,
    _indexOutOfBoundsExceptionConstructor,
    index
  );
  return env->Throw(exception);
}

jint __throwBOB(JNIEnv* env, jint index) {
  jthrowable exception = _newThrowable(
    _byteOffsetOutOfBoundsExceptionClass,
    _byteOffsetOutOfBoundsExceptionConstructor,
    index
  );
  return env->Throw(exception);
}

jint __throwPOB(JNIEnv* env, jobject pointObject) {
  jthrowable exception = _newThrowable(
    _pointOutOfBoundsExceptionClass,
    _pointOutOfBoundsExceptionConstructor,
    pointObject
  );
  return env->Throw(exception);
}

jint __throwILE(JNIEnv* env, jobject languageObject) {
  jthrowable exception = _newThrowable(
    _incompatibleLanguageExceptionClass,
    _incompatibleLanguageExceptionConstructor,
    languageObject
  );
  return env->Throw(exception);
}

jclass __getQueryExceptionClass(TSQueryError error) {
  return QUERY_EXCEPTION_CLASSES[error];
}

jmethodID __getQueryExceptionConstructor(TSQueryError error) {
  return QUERY_EXCEPTION_CONSTRUCTORS[error];
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
    return _newObject(
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

bool __pointEqual(TSPoint left, TSPoint right) {
  return left.row == right.row && left.column == right.column;
}

bool __rangeEqual(TSRange left, TSRange right) {
  return left.start_byte == right.start_byte &&
  left.end_byte == right.end_byte &&
    __pointEqual(left.start_point, right.start_point) &&
    __pointEqual(left.end_point, right.end_point);
}

bool __isDefaultRange(TSRange range) {
  return __rangeEqual(range, RANGE_DEFAULT);
}

jobject __marshalPoint(JNIEnv* env, TSPoint point) {
  // Not sure why I need to divide by two, probably because of utf-16
  return _newObject(
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
  return _newObject(
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

jobject __marshalPredicateStep(JNIEnv* env, const TSQuery* query, TSQueryPredicateStep step) {
    TSQueryPredicateStepType type = step.type;
    uint32_t id = step.value_id;
    uint32_t ignored = 0;
    jstring value = NULL;
    switch (type) {
      case TSQueryPredicateStepTypeCapture: {
        const char* characters = ts_query_capture_name_for_id(query, id, &ignored);
        value = env->NewStringUTF(characters);
        break;
      }
      case TSQueryPredicateStepTypeString: {
        const char* characters = ts_query_string_value_for_id(query, id, &ignored);
        value = env->NewStringUTF(characters);
        break;
      }
      default: break;
    }
    return _newObject(
      _predicateStepClass,
      _predicateStepConstructor,
      (jint)type,
      value
    );
}

jobjectArray __marshalPredicates(
  JNIEnv* env, const TSQuery* query, const TSQueryPredicateStep* steps, uint32_t* stepsLength, uint32_t* predicatesLength) {
  for (uint32_t i = 0; i < *stepsLength; i++) {
    TSQueryPredicateStep step = steps[i];
    TSQueryPredicateStepType type = step.type;
    bool sentinel = type == TSQueryPredicateStepTypeDone;
    if (sentinel) (*predicatesLength)++;
  }
  uint32_t indexes[*predicatesLength];
  indexes[0] = 0;
  for (uint32_t i = 1, j = 0; i < *stepsLength; i++) {
    TSQueryPredicateStep step = steps[i];
    TSQueryPredicateStepType type = step.type;
    bool sentinel = type == TSQueryPredicateStepTypeDone;
    if (sentinel) indexes[++j] = i + 1;
  }
  jobjectArray predicatesArray = env->NewObjectArray(
    *predicatesLength,
    _predicateClass,
    NULL
  );
  for (uint32_t i = 0; i < *predicatesLength; i++) {
    uint32_t lower = indexes[i];
    uint32_t upper = (i != (*predicatesLength) - 1)
      ? indexes[i + 1]
      : *stepsLength;
    uint32_t length = upper - lower;
    jobjectArray stepsArray = env->NewObjectArray(
      length,
      _predicateStepClass,
      NULL
    );
    for (uint32_t j = lower; j < upper; j++) {
      TSQueryPredicateStep step = steps[j];
      jobject predicateStepObject = __marshalPredicateStep(env, query, step);
      env->SetObjectArrayElement(stepsArray, j - lower, predicateStepObject);
    }
    jobject predicateObject = _newObject(
      _predicateClass,
      _predicateConstructor,
      NULL,
      stepsArray
    );
    env->SetObjectArrayElement(predicatesArray, i, predicateObject);
  }
  return predicatesArray;
}

const TSLanguage* __unmarshalLanguage(JNIEnv* env, jobject languageObject) {
  jclass languageClass = env->GetObjectClass(languageObject);
  jfieldID languageIdField = env->GetFieldID(languageClass, "id", "J");
  jlong languageId = env->GetLongField(languageObject, languageIdField);
  return (const TSLanguage*)languageId;
}

void __log_in_java(void* payload, TSLogType log_type, const char* buffer) {
  if (payload == NULL) return;
  JNIEnv* env;
  int status = JVM->GetEnv(reinterpret_cast<void**>(&env), JNI_VERSION_10);
  switch (status) {
    case JNI_OK: break;
    case JNI_EDETACHED: {
      int attached = JVM->AttachCurrentThread(reinterpret_cast<void**>(&env), NULL);
      if (attached == JNI_OK) break;
      const char* error = JNI_CALL_RESULT_NAMES[attached];
      printf("Unable to attach current thread to the JVM: %s\n", error);
      return;
    }
    default: {
      const char* error = JNI_CALL_RESULT_NAMES[status];
      printf("Unable to get JVM environment: %s\n", error);
      return;
    }
  }
  const char* type = LOG_TYPE_NAMES[log_type];
  jobject markerObject = env->CallStaticObjectMethod(
    _markerFactoryClass,
    _markerFactoryGetMarkerStaticMethod,
    env->NewStringUTF(type)
  );
  env->CallVoidMethod(
    reinterpret_cast<jobject>(payload),
    _loggerDebugMethod,
    markerObject,
    env->NewStringUTF(buffer)
  );
}
