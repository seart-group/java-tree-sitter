#include "ch_usi_si_seart_treesitter.h"
#include "ch_usi_si_seart_treesitter_Query_Builder.h"
#include <jni.h>
#include <string.h>
#include <tree_sitter/api.h>

JNIEXPORT jobject JNICALL Java_ch_usi_si_seart_treesitter_Query_00024Builder_build(
  JNIEnv* env, jclass thisClass, jobject languageObject, jstring patternString) {
  const TSLanguage* language = __unmarshalLanguage(env, languageObject);
  uint32_t length = env->GetStringLength(patternString);
  const char* characters = env->GetStringUTFChars(patternString, NULL);
  uint32_t offset;
  TSQueryError type = TSQueryErrorNone;
  TSQuery* query = ts_query_new(language, characters, length, &offset, &type);
  jobject result = NULL;
  jthrowable exception = NULL;
  switch (type) {
    case TSQueryErrorNone:
      {
        uint32_t patternsLength = ts_query_pattern_count(query);
        uint32_t patternStartBytes[patternsLength];
        for (uint32_t i = 0; i < patternsLength; i++) {
          patternStartBytes[i] = ts_query_start_byte_for_pattern(query, i);
        }
        jobjectArray patterns = env->NewObjectArray(patternsLength, _patternClass, NULL);
        for (uint32_t i = 0; i < patternsLength; i++) {
          uint32_t patternStartByte = patternStartBytes[i];
          uint32_t patternEndByte = (i < patternsLength - 1) ? patternStartBytes[i + 1] : length;
          uint32_t patternLength = patternEndByte - patternStartByte;
          char* substring = new char[patternLength + 1];
          memcpy(substring, characters + patternStartByte, patternLength);
          substring[patternLength] = '\0';
          bool rooted = ts_query_is_pattern_rooted(query, i);
          bool nonLocal = ts_query_is_pattern_non_local(query, i);
          uint32_t stepsLength = 0;
          uint32_t predicatesLength = 0;
          const TSQueryPredicateStep* steps = ts_query_predicates_for_pattern(query, i, &stepsLength);
          jobjectArray predicatesArray = __marshalPredicates(env, query, steps, &stepsLength, &predicatesLength);
          jobject patternObject = _newObject(
            _patternClass,
            _patternConstructor,
            (jint)i,
            (rooted) ? JNI_TRUE : JNI_FALSE,
            (nonLocal) ? JNI_TRUE : JNI_FALSE,
            env->NewStringUTF(substring),
            predicatesArray
          );
          for (uint32_t j = 0; j < predicatesLength; j++) {
            jobject predicateObject = env->GetObjectArrayElement(predicatesArray, j);
            env->SetObjectField(predicateObject, _predicatePatternField, patternObject);
          }
          env->SetObjectArrayElement(patterns, i, patternObject);
        }

        uint32_t capturesLength = ts_query_capture_count(query);
        jobjectArray captures = env->NewObjectArray(capturesLength, _captureClass, NULL);
        for (uint32_t i = 0; i < capturesLength; i++) {
          uint32_t ignored;
          const char* capture = ts_query_capture_name_for_id(query, i, &ignored);
          jobject captureObject = _newObject(
            _captureClass,
            _captureConstructor,
            (jint)i,
            env->NewStringUTF(capture)
          );
          env->SetObjectArrayElement(captures, i, captureObject);
        }

        uint32_t stringsLength = ts_query_string_count(query);
        jobjectArray strings = env->NewObjectArray(stringsLength, _stringClass, NULL);
        for (uint32_t i = 0; i < stringsLength; i++) {
          uint32_t ignored;
          const char* string = ts_query_string_value_for_id(query, i, &ignored);
          jstring stringString = env->NewStringUTF(string);
          env->SetObjectArrayElement(strings, i, stringString);
        }

        jobject queryObject = _newObject(
          _queryClass,
          _queryConstructor,
          (jlong)query,
          languageObject,
          patterns,
          captures,
          strings
        );

        for (uint32_t i = 0; i < capturesLength; i++) {
          jobject captureObject = env->GetObjectArrayElement(captures, i);
          env->SetObjectField(captureObject, _captureQueryField, queryObject);
        }

        for (uint32_t i = 0; i < patternsLength; i++) {
          jobject patternObject = env->GetObjectArrayElement(patterns, i);
          env->SetObjectField(patternObject, _patternQueryField, queryObject);
        }

        result = queryObject;
        break;
      }
    case TSQueryErrorSyntax:
    case TSQueryErrorNodeType:
    case TSQueryErrorField:
    case TSQueryErrorCapture:
    case TSQueryErrorStructure:
      exception = _newThrowable(
        __getQueryExceptionClass(type),
        __getQueryExceptionConstructor(type),
        (jint)offset
      );
      break;
    case TSQueryErrorLanguage:
      exception = _newThrowable(
        __getQueryExceptionClass(type),
        __getQueryExceptionConstructor(type),
        languageObject
      );
      break;
    default:
      exception = _newThrowable(
        _treeSitterExceptionClass,
        _treeSitterExceptionConstructor
      );
  }
  env->ReleaseStringUTFChars(patternString, characters);
  if (exception != NULL) env->Throw(exception);
  return result;
}
