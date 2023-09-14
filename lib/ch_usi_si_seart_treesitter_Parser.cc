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
  jlong parser = __getPointer(env, _parserClass, thisObject);
  ts_parser_delete((TSParser*)parser);
}

JNIEXPORT jboolean JNICALL Java_ch_usi_si_seart_treesitter_Parser_setLanguage(
  JNIEnv* env, jclass self, jlong parser, jlong language) {
  return ts_parser_set_language((TSParser*)parser, (TSLanguage*)language) ? JNI_TRUE : JNI_FALSE;
}

JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_Parser_getTimeout(
  JNIEnv* env, jobject thisObject) {
  jlong parser = __getPointer(env, _parserClass, thisObject);
  return (jlong)ts_parser_timeout_micros((TSParser*)parser);
}

JNIEXPORT void JNICALL Java_ch_usi_si_seart_treesitter_Parser_setTimeout(
  JNIEnv* env, jobject thisObject, jlong timeout) {
  if (timeout >= 0) {
      jlong parser = __getPointer(env, _parserClass, thisObject);
      ts_parser_set_timeout_micros((TSParser*)parser, (uint64_t)timeout);
  } else {
      jclass exceptionClass = _getClass("java/lang/IllegalArgumentException");
      env->ThrowNew(exceptionClass, "Timeout can not be negative!");
  }
}

JNIEXPORT jobject JNICALL Java_ch_usi_si_seart_treesitter_Parser_parse(
  JNIEnv* env, jobject thisObject, jbyteArray bytes, jint length, jobject oldTree) {
  jclass treeClass = _getClass("ch/usi/si/seart/treesitter/Tree");
  TSParser* parser = (TSParser*)__getPointer(env, _parserClass, thisObject);
  TSTree* old = (oldTree != NULL) ? (TSTree*)__getPointer(env, treeClass, oldTree) : NULL;
  jbyte* source = env->GetByteArrayElements(bytes, NULL);
  TSTree* result = ts_parser_parse_string_encoding(
      parser, old, reinterpret_cast<const char*>(source), length, TSInputEncodingUTF16
  );
  env->ReleaseByteArrayElements(bytes, source, JNI_ABORT);
  ts_parser_reset(parser);
  if (result == 0) {
      jclass causeClass = _getClass("java/util/concurrent/TimeoutException");
      jmethodID causeConstructor = _getConstructor(causeClass, "()V");
      jobject cause = env->NewObject(causeClass, causeConstructor);
      jclass exceptionClass = _getClass("ch/usi/si/seart/treesitter/exception/ParsingException");
      jmethodID exceptionConstructor = _getConstructor(exceptionClass, "(Ljava/lang/Throwable;)V");
      jobject exception = env->NewObject(exceptionClass, exceptionConstructor, (jthrowable)cause);
      env->Throw((jthrowable)exception);
      return NULL;
  }
  jobject language = env->GetObjectField(thisObject, _parserLanguageField);
  jmethodID treeConstructor = _getConstructor(treeClass, "(JLch/usi/si/seart/treesitter/Language;)V");
  jobject tree = env->NewObject(treeClass, treeConstructor, (jlong)result, language);
  return tree;
}
