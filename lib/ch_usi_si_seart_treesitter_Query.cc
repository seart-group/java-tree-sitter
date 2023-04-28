#include "ch_usi_si_seart_treesitter.h"
#include "ch_usi_si_seart_treesitter_Query.h"
#include <jni.h>
#include <string.h>
#include <tree_sitter/api.h>

JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Query_malloc(
  JNIEnv* env, jclass self, jlong language, jstring source) {
  const char* c_source;
  uint32_t source_length = env->GetStringLength(source);
  c_source = env->GetStringUTFChars(source, NULL);
  uint32_t* error_offset = new uint32_t;
  TSQueryError* error_type = new TSQueryError;
  TSQuery* query = ts_query_new((TSLanguage*)language, c_source, source_length, error_offset, error_type);
  jclass exceptionClass;
  switch (*error_type) {
    case TSQueryErrorNone:
      return (jlong)query;
    case TSQueryErrorSyntax:
      exceptionClass = _getClass("ch/usi/si/seart/treesitter/exception/query/QuerySyntaxException");
      break;
    case TSQueryErrorNodeType:
      exceptionClass = _getClass("ch/usi/si/seart/treesitter/exception/query/QueryNodeTypeException");
      break;
    case TSQueryErrorField:
      exceptionClass = _getClass("ch/usi/si/seart/treesitter/exception/query/QueryFieldException");
      break;
    case TSQueryErrorCapture:
      exceptionClass = _getClass("ch/usi/si/seart/treesitter/exception/query/QueryCaptureException");
      break;
    case TSQueryErrorStructure:
      exceptionClass = _getClass("ch/usi/si/seart/treesitter/exception/query/QueryStructureException");
      break;
    default:
      exceptionClass = _getClass("ch/usi/si/seart/treesitter/exception/TreeSitterException");
      return env->ThrowNew(exceptionClass, NULL);
  }
  jmethodID exceptionConstructor = _getConstructor(exceptionClass, "(I)V");
  jobject exception = env->NewObject(exceptionClass, exceptionConstructor, *error_offset);
  return env->Throw((jthrowable)exception);
}

JNIEXPORT void JNICALL Java_ch_usi_si_seart_treesitter_Query_close(
  JNIEnv* env, jobject thisObject) {
  jclass queryClass = _getClass("ch/usi/si/seart/treesitter/Query");
  jlong query = __getPointer(env, queryClass, thisObject);
  ts_query_delete((TSQuery*)query);
}

JNIEXPORT jint JNICALL Java_ch_usi_si_seart_treesitter_Query_countStrings(
  JNIEnv* env, jobject thisObject) {
  jclass queryClass = _getClass("ch/usi/si/seart/treesitter/Query");
  jlong query = __getPointer(env, queryClass, thisObject);
  return (jint)ts_query_string_count((TSQuery*)query);
}

JNIEXPORT jint JNICALL Java_ch_usi_si_seart_treesitter_Query_countCaptures__(
  JNIEnv* env, jobject thisObject) {
  jclass queryClass = _getClass("ch/usi/si/seart/treesitter/Query");
  jlong query = __getPointer(env, queryClass, thisObject);
  return (jint)ts_query_capture_count((TSQuery*)query);
}

JNIEXPORT jint JNICALL Java_ch_usi_si_seart_treesitter_Query_countCaptures__J(
  JNIEnv* env, jclass self, jlong query) {
  return (jint)ts_query_capture_count((TSQuery*)query);
}

JNIEXPORT jint JNICALL Java_ch_usi_si_seart_treesitter_Query_countPatterns(
  JNIEnv* env, jobject thisObject) {
  jclass queryClass = _getClass("ch/usi/si/seart/treesitter/Query");
  jlong query = __getPointer(env, queryClass, thisObject);
  return (jint)ts_query_pattern_count((TSQuery*)query);
}

JNIEXPORT jstring JNICALL Java_ch_usi_si_seart_treesitter_Query_getCaptureName(
  JNIEnv* env, jclass self, jlong query, jint index) {
  uint32_t* length = new uint32_t;
  const char* nameForIndex = ts_query_capture_name_for_id((TSQuery*)query, index, length);
  jstring result = env->NewStringUTF(nameForIndex);
  return result;
}
