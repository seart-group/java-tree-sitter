#include <jni.h>
#include <tree_sitter/api.h>

#ifndef _Included_ch_usi_si_seart_treesitter
#define _Included_ch_usi_si_seart_treesitter

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

extern jclass _queryCaptureClass;
extern jmethodID _queryCaptureConstructor;
extern jfieldID _queryCaptureNodeField;
extern jfieldID _queryCaptureIndexField;

extern jclass _queryMatchClass;
extern jmethodID _queryMatchConstructor;
extern jfieldID _queryMatchIdField;
extern jfieldID _queryMatchPatternIndexField;
extern jfieldID _queryMatchCapturesField;

#ifdef __cplusplus
extern "C" {
#endif

#define _getClass(NAME) \
  env->FindClass(NAME)

#define _loadClass(VARIABLE, NAME)               \
  {                                              \
    jclass local;                                \
    local = env->FindClass(NAME);                \
    VARIABLE = (jclass)env->NewGlobalRef(local); \
    env->DeleteLocalRef(local);                  \
  }

#define _unloadClass(VARIABLE) \
  env->DeleteGlobalRef(VARIABLE)

#define _loadField(VARIABLE, CLASS, NAME, TYPE) \
  { VARIABLE = env->GetFieldID(CLASS, NAME, TYPE); }

#define _getField(CLASS, NAME, TYPE) \
  env->GetFieldID(CLASS, NAME, TYPE)

#define _loadMethod(VARIABLE, CLASS, NAME, SIGNATURE) \
  { VARIABLE = env->GetMethodID(CLASS, NAME, SIGNATURE); }

#define _loadConstructor(VARIABLE, CLASS, SIGNATURE) \
  { _loadMethod(VARIABLE, CLASS, "<init>", SIGNATURE); }

#define _getConstructor(CLASS, TYPE) \
  env->GetMethodID(CLASS, "<init>", TYPE)

typedef struct {
  const char* type;
  const char* name;
  uint32_t startByte;
  uint32_t endByte;
  TSPoint startPoint;
  TSPoint endPoint;
  bool isNamed;
} TreeCursorNode;

jlong __getPointer(JNIEnv* env, jclass objectClass, jobject objectInstance);

jobject __marshalNode(JNIEnv* env, TSNode node);

TSNode __unmarshalNode(JNIEnv* env, jobject nodeObject);

jobject __marshalPoint(JNIEnv* env, TSPoint point);

TSPoint __unmarshalPoint(JNIEnv* env, jobject pointObject);

jobject __marshalQueryCapture(JNIEnv* env, TSQueryCapture capture);

jobject __marshalQueryMatch(JNIEnv* env, TSQueryMatch match);

TSInputEdit __unmarshalInputEdit(JNIEnv* env, jobject inputEdit);

jobject __marshalTreeCursorNode(JNIEnv* env, TreeCursorNode node);

#ifdef __cplusplus
}
#endif
#endif