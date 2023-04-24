#include "usi_si_seart_treesitter.h"
#include "usi_si_seart_treesitter_Query.h"
#include <jni.h>
#include <math.h>
#include <string.h>
#include <tree_sitter/api.h>

JNIEXPORT jlong JNICALL Java_usi_si_seart_treesitter_Query_malloc(
  JNIEnv* env, jclass self, jlong language, jstring source) {
  const char* c_source;
  uint32_t source_length = env->GetStringLength(source);
  c_source = env->GetStringUTFChars(source, NULL);
  uint32_t* error_offset = new uint32_t;
  TSQueryError* error_type = new TSQueryError;
  TSQuery* query = ts_query_new((TSLanguage*)language, c_source, source_length, error_offset, error_type);
  jclass exceptionClass;
  const char* c_pattern;
  switch (*error_type) {
    case TSQueryErrorNone:
      return (jlong)query;
    case TSQueryErrorSyntax:
      exceptionClass = _getClass("usi/si/seart/treesitter/exception/query/QuerySyntaxException");
      c_pattern = "Bad syntax at offset %d";
      break;
    case TSQueryErrorNodeType:
      exceptionClass = _getClass("usi/si/seart/treesitter/exception/query/QueryNodeTypeException");
      c_pattern = "Bad node name at offset %d";
      break;
    case TSQueryErrorField:
      exceptionClass = _getClass("usi/si/seart/treesitter/exception/query/QueryFieldException");
      c_pattern = "Bad field name at offset %d";
      break;
    case TSQueryErrorCapture:
      exceptionClass = _getClass("usi/si/seart/treesitter/exception/query/QueryCaptureException");
      c_pattern = "Bad capture at offset %d";
      break;
    case TSQueryErrorStructure:
      exceptionClass = _getClass("usi/si/seart/treesitter/exception/query/QueryStructureException");
      c_pattern = "Bad pattern structure at offset %d";
      break;
    default:
      exceptionClass = _getClass("usi/si/seart/treesitter/exception/TreeSitterException");
      return env->ThrowNew(exceptionClass, NULL);
  }
  int digits = static_cast<int>(floor(log10(*error_offset))) + 1;
  char c_message[strlen(c_pattern) + 1 + digits];
  snprintf(c_message, sizeof(c_message), c_pattern, *error_offset);
  return env->ThrowNew(exceptionClass, c_message);
}

JNIEXPORT void JNICALL Java_usi_si_seart_treesitter_Query_close(
  JNIEnv* env, jobject thisObject) {
  jclass queryClass = _getClass("usi/si/seart/treesitter/Query");
  jlong query = __getPointer(env, queryClass, thisObject);
  ts_query_delete((TSQuery*)query);
}

JNIEXPORT jint JNICALL Java_usi_si_seart_treesitter_Query_countStrings(
  JNIEnv* env, jobject thisObject) {
  jclass queryClass = _getClass("usi/si/seart/treesitter/Query");
  jlong query = __getPointer(env, queryClass, thisObject);
  return (jint)ts_query_string_count((TSQuery*)query);
}

JNIEXPORT jint JNICALL Java_usi_si_seart_treesitter_Query_countCaptures__(
  JNIEnv* env, jobject thisObject) {
  jclass queryClass = _getClass("usi/si/seart/treesitter/Query");
  jlong query = __getPointer(env, queryClass, thisObject);
  return (jint)ts_query_capture_count((TSQuery*)query);
}

JNIEXPORT jint JNICALL Java_usi_si_seart_treesitter_Query_countCaptures__J(
  JNIEnv* env, jclass self, jlong query) {
  return (jint)ts_query_capture_count((TSQuery*)query);
}

JNIEXPORT jint JNICALL Java_usi_si_seart_treesitter_Query_countPatterns(
  JNIEnv* env, jobject thisObject) {
  jclass queryClass = _getClass("usi/si/seart/treesitter/Query");
  jlong query = __getPointer(env, queryClass, thisObject);
  return (jint)ts_query_pattern_count((TSQuery*)query);
}

JNIEXPORT jstring JNICALL Java_usi_si_seart_treesitter_Query_getCaptureName(
  JNIEnv* env, jclass self, jlong query, jint index) {
  uint32_t* length = new uint32_t;
  const char* nameForIndex = ts_query_capture_name_for_id((TSQuery*)query, index, length);
  jstring result = env->NewStringUTF(nameForIndex);
  return result;
}
