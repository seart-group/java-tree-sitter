#include <jni.h>
#include <tree_sitter/api.h>

#ifndef _Included_usi_si_seart_treesitter
#define _Included_usi_si_seart_treesitter
#ifdef __cplusplus
extern "C" {
#endif

#define _getClass(NAME) \
  env->FindClass(NAME)

#define _getField(CLASS, NAME, TYPE) \
  env->GetFieldID(CLASS, NAME, TYPE)

jlong __getPointer(JNIEnv* env, jclass objectClass, jobject objectInstance);

jobject __marshalNode(JNIEnv* env, TSNode node);

TSNode __unmarshalNode(JNIEnv* env, jobject nodeObject);

jobject __marshalPoint(JNIEnv* env, TSPoint point);

TSPoint __unmarshalPoint(JNIEnv* env, jobject pointObject);

jobject __marshalQueryCapture(JNIEnv* env, TSQueryCapture capture);

jobject __marshalQueryMatch(JNIEnv* env, TSQueryMatch match);

TSInputEdit __unmarshalInputEdit(JNIEnv* env, jobject inputEdit);

#ifdef __cplusplus
}
#endif
#endif