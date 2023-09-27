#include "ch_usi_si_seart_treesitter.h"
#include "ch_usi_si_seart_treesitter_Query_Builder.h"
#include <jni.h>
#include <string.h>
#include <tree_sitter/api.h>

JNIEXPORT jobject JNICALL Java_ch_usi_si_seart_treesitter_Query_00024Builder_build(
  JNIEnv* env, jclass thisClass, jobject languageObject, jstring pattern) {
  const TSLanguage* language = __unmarshalLanguage(env, languageObject);
  uint32_t patternLength = env->GetStringLength(pattern);
  const char* characters = env->GetStringUTFChars(pattern, NULL);
  uint32_t* offset = new uint32_t;
  TSQueryError* errorType = new TSQueryError;
  TSQuery* query = ts_query_new(language, characters, patternLength, offset, errorType);
  jclass exceptionClass;
  jmethodID exceptionConstructor;
  switch (*errorType) {
    case TSQueryErrorNone:
      {
        uint32_t capturesLength = ts_query_capture_count(query);
        jobjectArray captures = env->NewObjectArray(capturesLength, _stringClass, NULL);
        for (int i = 0; i < capturesLength; i++) {
          const char* capture = ts_query_capture_name_for_id(query, i, new uint32_t);
          jstring captureString = env->NewStringUTF(capture);
          env->SetObjectArrayElement(captures, i, captureString);
        }
        return env->NewObject(
          _queryClass,
          _queryConstructor,
          (jlong)query,
          languageObject,
          pattern,
          captures
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
