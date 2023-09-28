#include "ch_usi_si_seart_treesitter.h"
#include "ch_usi_si_seart_treesitter_Query_Builder.h"
#include <jni.h>
#include <string.h>
#include <tree_sitter/api.h>

JNIEXPORT jobject JNICALL Java_ch_usi_si_seart_treesitter_Query_00024Builder_build(
  JNIEnv* env, jclass thisClass, jobject languageObject, jstring patterns) {
  const TSLanguage* language = __unmarshalLanguage(env, languageObject);
  uint32_t length = env->GetStringLength(patterns);
  const char* characters = env->GetStringUTFChars(patterns, NULL);
  uint32_t* offset = new uint32_t;
  TSQueryError* errorType = new TSQueryError;
  TSQuery* query = ts_query_new(language, characters, length, offset, errorType);
  jclass exceptionClass;
  jmethodID exceptionConstructor;
  switch (*errorType) {
    case TSQueryErrorNone:
      {
        uint32_t patternsLength = ts_query_pattern_count(query);
        uint32_t patternStartBytes[patternsLength];
        for (int i = 0; i < patternsLength; i++) {
          patternStartBytes[i] = ts_query_start_byte_for_pattern(query, i);
        }
        jobjectArray patterns = env->NewObjectArray(patternsLength, _stringClass, NULL);
        for (int i = 0; i < patternsLength; i++) {
          uint32_t patternStartByte = patternStartBytes[i];
          uint32_t patternEndByte = (i < patternsLength - 1) ? patternStartBytes[i + 1] : length;
          uint32_t patternLength = patternEndByte - patternStartByte;
          char* substring = new char[patternLength + 1];
          memcpy(substring, characters + patternStartByte, patternLength);
          substring[patternLength] = '\0';
          jstring patternString = env->NewStringUTF(substring);
          env->SetObjectArrayElement(patterns, i, patternString);
        }

        uint32_t capturesLength = ts_query_capture_count(query);
        jobjectArray captures = env->NewObjectArray(capturesLength, _stringClass, NULL);
        for (int i = 0; i < capturesLength; i++) {
          const char* capture = ts_query_capture_name_for_id(query, i, new uint32_t);
          jstring captureString = env->NewStringUTF(capture);
          env->SetObjectArrayElement(captures, i, captureString);
        }

        uint32_t stringsLength = ts_query_string_count(query);
        jobjectArray strings = env->NewObjectArray(stringsLength, _stringClass, NULL);
        for (int i = 0; i < stringsLength; i++) {
          const char* string = ts_query_string_value_for_id(query, i, new uint32_t);
          jstring stringString = env->NewStringUTF(string);
          env->SetObjectArrayElement(strings, i, stringString);
        }

        return env->NewObject(
          _queryClass,
          _queryConstructor,
          (jlong)query,
          languageObject,
          patterns,
          captures,
          strings
        );
      }
    case TSQueryErrorSyntax:
      exceptionClass = _querySyntaxExceptionClass;
      exceptionConstructor = _querySyntaxExceptionConstructor;
      break;
    case TSQueryErrorNodeType:
      exceptionClass = _queryNodeTypeExceptionClass;
      exceptionConstructor = _queryNodeTypeExceptionConstructor;
      break;
    case TSQueryErrorField:
      exceptionClass = _queryFieldExceptionClass;
      exceptionConstructor = _queryFieldExceptionConstructor;
      break;
    case TSQueryErrorCapture:
      exceptionClass = _queryCaptureExceptionClass;
      exceptionConstructor = _queryCaptureExceptionConstructor;
      break;
    case TSQueryErrorStructure:
      exceptionClass = _queryStructureExceptionClass;
      exceptionConstructor = _queryStructureExceptionConstructor;
      break;
    default:
      env->ThrowNew(_treeSitterExceptionClass, NULL);
      return NULL;
  }
  jobject exception = env->NewObject(exceptionClass, exceptionConstructor, *offset);
  env->Throw((jthrowable)exception);
  return NULL;
}
