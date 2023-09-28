#include "ch_usi_si_seart_treesitter.h"
#include "ch_usi_si_seart_treesitter_Parser.h"
#include <jni.h>
#include <tree_sitter/api.h>

JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Parser_malloc(
  JNIEnv* env, jclass self) {
  return (jlong)ts_parser_new();
}

JNIEXPORT jint JNICALL Java_ch_usi_si_seart_treesitter_Parser_getLanguageVersion(
  JNIEnv *, jclass) {
  return (jint)TREE_SITTER_LANGUAGE_VERSION;
}

JNIEXPORT jint JNICALL Java_ch_usi_si_seart_treesitter_Parser_getMinimumCompatibleLanguageVersion(
  JNIEnv *, jclass) {
  return (jint)TREE_SITTER_MIN_COMPATIBLE_LANGUAGE_VERSION;
}

JNIEXPORT void JNICALL Java_ch_usi_si_seart_treesitter_Parser_delete(
  JNIEnv* env, jobject thisObject) {
  TSParser* parser = (TSParser*)__getPointer(env, thisObject);
  ts_parser_delete(parser);
  __clearPointer(env, thisObject);
}

JNIEXPORT jboolean JNICALL Java_ch_usi_si_seart_treesitter_Parser_setLanguage(
  JNIEnv* env, jclass self, jlong parser, jlong language) {
  return ts_parser_set_language((TSParser*)parser, (TSLanguage*)language) ? JNI_TRUE : JNI_FALSE;
}

JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Parser_getTimeout(
  JNIEnv* env, jobject thisObject) {
  TSParser* parser = (TSParser*)__getPointer(env, thisObject);
  return (jlong)ts_parser_timeout_micros(parser);
}

JNIEXPORT void JNICALL Java_ch_usi_si_seart_treesitter_Parser_setTimeout(
  JNIEnv* env, jobject thisObject, jlong timeout) {
  if (timeout >= 0) {
    TSParser* parser = (TSParser*)__getPointer(env, thisObject);
    ts_parser_set_timeout_micros(parser, (uint64_t)timeout);
  } else {
    __throwIAE(env, "Timeout can not be negative!");
  }
}

JNIEXPORT jobject JNICALL Java_ch_usi_si_seart_treesitter_Parser_parse(
  JNIEnv* env, jobject thisObject, jstring source, jbyteArray bytes, jint length, jobject oldTreeObject) {
  TSParser* parser = (TSParser*)__getPointer(env, thisObject);
  TSTree* oldTree = (oldTreeObject != NULL) ? (TSTree*)__getPointer(env, oldTreeObject) : NULL;
  jbyte* elements = env->GetByteArrayElements(bytes, NULL);
  TSTree* result = ts_parser_parse_string_encoding(
    parser, oldTree, reinterpret_cast<const char*>(elements), length, TSInputEncodingUTF16
  );
  env->ReleaseByteArrayElements(bytes, elements, JNI_ABORT);
  ts_parser_reset(parser);
  if (result == 0) {
    jobject cause = env->NewObject(
      _timeoutExceptionClass,
      _timeoutExceptionConstructor
    );
    jobject exception = env->NewObject(
      _parsingExceptionClass,
      _parsingExceptionConstructor,
      (jthrowable)cause
    );
    env->Throw((jthrowable)exception);
    return NULL;
  }
  jobject language = env->GetObjectField(thisObject, _parserLanguageField);
  return env->NewObject(_treeClass, _treeConstructor, (jlong)result, language, source);
}
