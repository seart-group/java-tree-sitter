#include "ch_usi_si_seart_treesitter.h"
#include "ch_usi_si_seart_treesitter_Parser.h"
#include <jni.h>
#include <tree_sitter/api.h>

JNIEXPORT void JNICALL Java_ch_usi_si_seart_treesitter_Parser_delete(
  JNIEnv* env, jobject thisObject) {
  TSParser* parser = (TSParser*)__getPointer(env, thisObject);
  ts_parser_delete(parser);
  __clearPointer(env, thisObject);
}

JNIEXPORT void JNICALL Java_ch_usi_si_seart_treesitter_Parser_setLanguage(
  JNIEnv* env, jclass thisClass, jobject parserObject, jobject languageObject) {
  TSParser* parser = (TSParser*)__getPointer(env, parserObject);
  const TSLanguage* language = __unmarshalLanguage(env, languageObject);
  if (!ts_parser_set_language(parser, language)) {
    __throwILE(env, languageObject);
  } else {
    env->SetObjectField(
      parserObject,
      _parserLanguageField,
      languageObject
    );
  }
}

JNIEXPORT jobject JNICALL Java_ch_usi_si_seart_treesitter_Parser_getIncludedRanges(
  JNIEnv* env, jobject thisObject) {
  TSParser* parser = (TSParser*)__getPointer(env, thisObject);
  uint32_t length = 0;
  const TSRange* ranges = ts_parser_included_ranges(parser, &length);
  if (
    length == 0 || (length == 1 && __isDefaultRange(ranges[0]))
  ) {
    return env->CallStaticObjectMethod(_collectionsClass, _collectionsEmptyListStaticMethod);
  } else {
    jobjectArray array = env->NewObjectArray(length, _rangeClass, NULL);
    for (uint32_t i = 0; i < length; i++) {
      TSRange range = ranges[i];
      jobject rangeObject = __marshalRange(env, range);
      env->SetObjectArrayElement(array, i, rangeObject);
    }
    return env->CallStaticObjectMethod(_listClass, _listOfStaticMethod, array);
  }
}

JNIEXPORT void JNICALL Java_ch_usi_si_seart_treesitter_Parser_setIncludedRanges(
  JNIEnv* env, jobject thisObject, jobjectArray rangeObjectArray, jint length) {
  TSParser* parser = (TSParser*)__getPointer(env, thisObject);
  TSRange ranges[length];
  for (int i = 0; i < length; i++) {
    jobject rangeObject = env->GetObjectArrayElement(rangeObjectArray, i);
    ranges[i] = __unmarshalRange(env, rangeObject);
  }
  if (!ts_parser_set_included_ranges(parser, ranges, length)) {
    __throwISE(env, NULL);
  }
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
  JNIEnv* env, jobject thisObject, jstring source, jbyteArray bytes, jint length, jobject treeObject) {
  TSParser* parser = (TSParser*)__getPointer(env, thisObject);
  TSTree* tree = (treeObject != NULL) ? (TSTree*)__getPointer(env, treeObject) : NULL;
  jbyte* elements = env->GetByteArrayElements(bytes, NULL);
  TSTree* result = ts_parser_parse_string_encoding(
    parser, tree, reinterpret_cast<const char*>(elements), length, TSInputEncodingUTF16
  );
  env->ReleaseByteArrayElements(bytes, elements, JNI_ABORT);
  ts_parser_reset(parser);
  if (result == 0) {
    jthrowable cause = (jthrowable)env->NewObject(
      _timeoutExceptionClass,
      _timeoutExceptionConstructor
    );
    jthrowable exception = (jthrowable)env->NewObject(
      _parsingExceptionClass,
      _parsingExceptionConstructor,
      cause
    );
    env->Throw(exception);
    return NULL;
  }
  jobject languageObject = env->GetObjectField(thisObject, _parserLanguageField);
  return env->NewObject(
    _treeClass,
    _treeConstructor,
    (jlong)result,
    languageObject,
    source
  );
}
