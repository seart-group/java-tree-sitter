#include <jni.h>
#include <tree_sitter/api.h>

#ifndef _Included_ch_usi_si_seart_treesitter
#define _Included_ch_usi_si_seart_treesitter

extern jclass _nullPointerExceptionClass;
extern jclass _illegalArgumentExceptionClass;
extern jclass _illegalStateExceptionClass;
extern jclass _ioExceptionClass;

extern jclass _timeoutExceptionClass;
extern jmethodID _timeoutExceptionConstructor;

extern jclass _indexOutOfBoundsExceptionClass;
extern jmethodID _indexOutOfBoundsExceptionConstructor;

extern jclass _externalClass;
extern jfieldID _externalPointerField;

extern jclass _nodeClass;
extern jmethodID _nodeConstructor;
extern jfieldID _nodeContext0Field;
extern jfieldID _nodeContext1Field;
extern jfieldID _nodeContext2Field;
extern jfieldID _nodeContext3Field;
extern jfieldID _nodeIdField;
extern jfieldID _nodeTreeField;

extern jclass _pointClass;
extern jmethodID _pointConstructor;
extern jfieldID _pointRowField;
extern jfieldID _pointColumnField;
extern jfieldID _pointOriginStaticField;
extern jobject _pointOrigin;

extern jclass _queryCaptureClass;
extern jmethodID _queryCaptureConstructor;
extern jfieldID _queryCaptureNodeField;
extern jfieldID _queryCaptureIndexField;

extern jclass _queryMatchClass;
extern jmethodID _queryMatchConstructor;
extern jfieldID _queryMatchIdField;
extern jfieldID _queryMatchPatternIndexField;
extern jfieldID _queryMatchCapturesField;

extern jclass _inputEditClass;
extern jmethodID _inputEditConstructor;
extern jfieldID _inputEditStartByteField;
extern jfieldID _inputEditOldEndByteField;
extern jfieldID _inputEditNewEndByteField;
extern jfieldID _inputEditStartPointField;
extern jfieldID _inputEditOldEndPointField;
extern jfieldID _inputEditNewEndPointField;

extern jclass _treeCursorNodeClass;
extern jmethodID _treeCursorNodeConstructor;
extern jfieldID _treeCursorNodeNameField;
extern jfieldID _treeCursorNodeTypeField;
extern jfieldID _treeCursorNodeContentField;
extern jfieldID _treeCursorNodeStartByteField;
extern jfieldID _treeCursorNodeEndByteField;
extern jfieldID _treeCursorNodeStartPointField;
extern jfieldID _treeCursorNodeEndPointField;
extern jfieldID _treeCursorNodeIsNamed;

extern jclass _parserClass;
extern jfieldID _parserLanguageField;

extern jclass _treeClass;
extern jmethodID _treeConstructor;

extern jclass _dotGraphPrinterClass;
extern jfieldID _dotGraphPrinterTreeField;

extern jclass _queryClass;

extern jclass _queryCursorClass;
extern jfieldID _queryCursorNodeField;
extern jfieldID _queryCursorQueryField;
extern jfieldID _queryCursorExecutedField;

extern jclass _treeCursorClass;
extern jfieldID _treeCursorContext0Field;
extern jfieldID _treeCursorContext1Field;
extern jfieldID _treeCursorIdField;
extern jfieldID _treeCursorTreeField;
extern jmethodID _treeCursorConstructor;

extern jclass _treeSitterExceptionClass;

extern jclass _querySyntaxExceptionClass;
extern jclass _queryNodeTypeExceptionClass;
extern jclass _queryFieldExceptionClass;
extern jclass _queryCaptureExceptionClass;
extern jclass _queryStructureExceptionClass;

extern jclass _parsingExceptionClass;
extern jmethodID _parsingExceptionConstructor;

#ifdef __cplusplus
extern "C" {
#endif

#define _getClass(NAME) \
  env->FindClass(NAME)

#define _loadClass(VARIABLE, NAME)               \
  {                                              \
    jclass local;                                \
    local = _getClass(NAME);                     \
    VARIABLE = (jclass)env->NewGlobalRef(local); \
    env->DeleteLocalRef(local);                  \
  }

#define _loadStaticObject(VARIABLE, CLASS, FIELD)    \
  {                                                  \
    jobject local;                                   \
    local = env->GetStaticObjectField(CLASS, FIELD); \
    VARIABLE = env->NewGlobalRef(local);             \
    env->DeleteLocalRef(local);                      \
  }

#define _unloadClass(VARIABLE) \
  { env->DeleteGlobalRef(VARIABLE); }

#define _getField(CLASS, NAME, TYPE) \
  env->GetFieldID(CLASS, NAME, TYPE)

#define _getStaticField(CLASS, NAME, TYPE) \
  env->GetStaticFieldID(CLASS, NAME, TYPE)

#define _loadField(VARIABLE, CLASS, NAME, TYPE) \
  { VARIABLE = _getField(CLASS, NAME, TYPE); }

#define _loadStaticField(VARIABLE, CLASS, NAME, TYPE) \
  { VARIABLE = _getStaticField(CLASS, NAME, TYPE); }

#define _getMethod(CLASS, NAME, SIGNATURE) \
  env->GetMethodID(CLASS, NAME, SIGNATURE)

#define _getConstructor(CLASS, SIGNATURE) \
  _getMethod(CLASS, "<init>", SIGNATURE)

#define _loadMethod(VARIABLE, CLASS, NAME, SIGNATURE) \
  { VARIABLE = _getMethod(CLASS, NAME, SIGNATURE); }

#define _loadConstructor(VARIABLE, CLASS, SIGNATURE) \
  { VARIABLE = _getConstructor(CLASS, SIGNATURE); }

#define _setNodeTreeField(NODE, TREE) \
  env->SetObjectField(NODE, _nodeTreeField, TREE)

jlong __getPointer(JNIEnv* env, jobject objectInstance);

jobject __marshalNode(JNIEnv* env, TSNode node);

TSNode __unmarshalNode(JNIEnv* env, jobject nodeObject);

void __copyTree(JNIEnv* env, jobject sourceNodeObject, jobject targetNodeObject);

jobject __marshalPoint(JNIEnv* env, TSPoint point);

TSPoint __unmarshalPoint(JNIEnv* env, jobject pointObject);

jobject __marshalQueryCapture(JNIEnv* env, TSQueryCapture capture);

jobject __marshalQueryMatch(JNIEnv* env, TSQueryMatch match);

TSInputEdit __unmarshalInputEdit(JNIEnv* env, jobject inputEdit);

#ifdef __cplusplus
}
#endif
#endif