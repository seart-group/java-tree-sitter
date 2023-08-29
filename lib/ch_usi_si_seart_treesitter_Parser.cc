#include "ch_usi_si_seart_treesitter.h"
#include "ch_usi_si_seart_treesitter_Parser.h"
#include <jni.h>
#include <tree_sitter/api.h>

JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Parser_malloc(
    JNIEnv* env, jclass self) {
  return (jlong)ts_parser_new();
}

JNIEXPORT void JNICALL Java_ch_usi_si_seart_treesitter_Parser_close(
  JNIEnv* env, jobject thisObject) {
  jclass parserClass = _getClass("ch/usi/si/seart/treesitter/Parser");
  jlong parser = __getPointer(env, parserClass, thisObject);
  ts_parser_delete((TSParser*)parser);
}

JNIEXPORT jboolean JNICALL Java_ch_usi_si_seart_treesitter_Parser_setLanguage(
    JNIEnv* env, jclass self, jlong parser, jlong language) {
  return ts_parser_set_language((TSParser*)parser, (TSLanguage*)language) ? JNI_TRUE : JNI_FALSE;
}

JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Parser_getTimeout(
    JNIEnv* env, jobject thisObject) {
  jclass parserClass = _getClass("ch/usi/si/seart/treesitter/Parser");
  jlong parser = __getPointer(env, parserClass, thisObject);
  return (jlong)ts_parser_timeout_micros((TSParser*)parser);
}

JNIEXPORT void JNICALL Java_ch_usi_si_seart_treesitter_Parser_setTimeout(
    JNIEnv* env, jobject thisObject, jlong timeout) {
  if (timeout >= 0) {
      jclass parserClass = _getClass("ch/usi/si/seart/treesitter/Parser");
      jlong parser = __getPointer(env, parserClass, thisObject);
      ts_parser_set_timeout_micros((TSParser*)parser, (uint64_t)timeout);
  } else {
      jclass exceptionClass = _getClass("java/lang/IllegalArgumentException");
      env->ThrowNew(exceptionClass, "Timeout can not be negative!");
  }
}

JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Parser_parseBytes___3BI(
  JNIEnv* env, jobject thisObject, jbyteArray source_bytes, jint length) {
  jclass parserClass = _getClass("ch/usi/si/seart/treesitter/Parser");
  jlong parser = __getPointer(env, parserClass, thisObject);
  jbyte* source = env->GetByteArrayElements(source_bytes, NULL);
  jlong result = (jlong)ts_parser_parse_string_encoding(
      (TSParser*)parser, NULL, reinterpret_cast<const char*>(source), length, TSInputEncodingUTF16
  );
  env->ReleaseByteArrayElements(source_bytes, source, JNI_ABORT);
  ts_parser_reset((TSParser*)parser);
  if (result == 0) {
    jclass causeClass = _getClass("java/util/concurrent/TimeoutException");
    jmethodID causeConstructor = _getConstructor(causeClass, "()V");
    jobject cause = env->NewObject(causeClass, causeConstructor);
    jclass exceptionClass = _getClass("ch/usi/si/seart/treesitter/exception/ParsingException");
    jmethodID exceptionConstructor = _getConstructor(exceptionClass, "(Ljava/lang/Throwable;)V");
    jobject exception = env->NewObject(exceptionClass, exceptionConstructor, (jthrowable)cause);
    env->Throw((jthrowable)exception);
  }
  return result;
}

JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Parser_parseBytes___3BILch_usi_si_seart_treesitter_Tree_2(
  JNIEnv* env, jobject thisObject, jbyteArray source_bytes, jint length, jobject oldTree) {
  jclass parserClass = _getClass("ch/usi/si/seart/treesitter/Parser");
  jlong parser = __getPointer(env, parserClass, thisObject);
  if (oldTree == NULL) {
      jclass exceptionClass = _getClass("java/lang/NullPointerException");
      env->ThrowNew(exceptionClass, "Tree must not be null!");
      return (jlong)0;
  }
  jclass treeClass = _getClass("ch/usi/si/seart/treesitter/Tree");
  jlong tree = __getPointer(env, treeClass, oldTree);
  jbyte* source = env->GetByteArrayElements(source_bytes, NULL);
  jlong result = (jlong)ts_parser_parse_string_encoding(
      (TSParser*)parser, (TSTree*)tree, reinterpret_cast<const char*>(source), length, TSInputEncodingUTF16
  );
  env->ReleaseByteArrayElements(source_bytes, source, JNI_ABORT);
  ts_parser_reset((TSParser*)parser);
  if (result == 0) {
    jclass causeClass = _getClass("java/util/concurrent/TimeoutException");
    jmethodID causeConstructor = _getConstructor(causeClass, "()V");
    jobject cause = env->NewObject(causeClass, causeConstructor);
    jclass exceptionClass = _getClass("ch/usi/si/seart/treesitter/exception/ParsingException");
    jmethodID exceptionConstructor = _getConstructor(exceptionClass, "(Ljava/lang/Throwable;)V");
    jobject exception = env->NewObject(exceptionClass, exceptionConstructor, (jthrowable)cause);
    env->Throw((jthrowable)exception);
  }
  return result;
}
