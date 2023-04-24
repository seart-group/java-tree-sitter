#include "usi_si_seart_treesitter.h"
#include "usi_si_seart_treesitter_Parser.h"
#include <jni.h>
#include <tree_sitter/api.h>

JNIEXPORT jlong JNICALL Java_usi_si_seart_treesitter_Parser_malloc(
    JNIEnv* env, jclass self) {
  return (jlong)ts_parser_new();
}

JNIEXPORT void JNICALL Java_usi_si_seart_treesitter_Parser_close(
  JNIEnv* env, jobject thisObject) {
  jclass parserClass = _getClass("usi/si/seart/treesitter/Parser");
  jlong parser = __getPointer(env, parserClass, thisObject);
  ts_parser_delete((TSParser*)parser);
}

JNIEXPORT jboolean JNICALL Java_usi_si_seart_treesitter_Parser_setLanguage(
    JNIEnv* env, jclass self, jlong parser, jlong language) {
  return ts_parser_set_language((TSParser*)parser, (TSLanguage*)language) ? JNI_TRUE : JNI_FALSE;
}

JNIEXPORT jlong JNICALL Java_usi_si_seart_treesitter_Parser_parseBytes__J_3BI(
    JNIEnv* env, jclass self, jlong parser, jbyteArray source_bytes, jint length) {
  jbyte* source = env->GetByteArrayElements(source_bytes, NULL);
  jlong result = (jlong)ts_parser_parse_string_encoding(
      (TSParser*)parser, NULL, reinterpret_cast<const char*>(source), length, TSInputEncodingUTF16
  );
  env->ReleaseByteArrayElements(source_bytes, source, JNI_ABORT);
  return result;
}

JNIEXPORT jlong JNICALL Java_usi_si_seart_treesitter_Parser_parseBytes__JJ_3BI(
    JNIEnv* env, jclass self, jlong parser, jlong old_tree, jbyteArray source_bytes, jint length) {
  jbyte* source = env->GetByteArrayElements(source_bytes, NULL);
  jlong result = (jlong)ts_parser_parse_string_encoding(
      (TSParser*)parser, (TSTree*)old_tree, reinterpret_cast<const char*>(source), length, TSInputEncodingUTF16
  );
  env->ReleaseByteArrayElements(source_bytes, source, JNI_ABORT);
  return result;
}
