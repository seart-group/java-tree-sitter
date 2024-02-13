#include "ch_usi_si_seart_treesitter.h"
#include "ch_usi_si_seart_treesitter_LookaheadIterator.h"
#include <jni.h>
#include <tree_sitter/api.h>

JNIEXPORT void JNICALL Java_ch_usi_si_seart_treesitter_LookaheadIterator_delete(
  JNIEnv* env, jobject thisObject) {
  TSLookaheadIterator* iterator = (TSLookaheadIterator*)__getPointer(env, thisObject);
  ts_lookahead_iterator_delete(iterator);
  __clearPointer(env, thisObject);
}

JNIEXPORT jobject JNICALL Java_ch_usi_si_seart_treesitter_LookaheadIterator_next(
  JNIEnv* env, jobject thisObject) {
  bool hasNext = (bool)env->GetBooleanField(thisObject, _lookaheadIteratorHasNextField);
  if (!hasNext) {
    __throwNSE(env, NULL);
    return NULL;
  }
  TSLookaheadIterator* iterator = (TSLookaheadIterator*)__getPointer(env, thisObject);
  TSSymbol symbol = ts_lookahead_iterator_current_symbol(iterator);
  const TSLanguage* language = ts_lookahead_iterator_language(iterator);
  const char* name = ts_language_symbol_name(language, symbol);
  TSSymbolType type = ts_language_symbol_type(language, symbol);
  env->SetBooleanField(
    thisObject,
    _lookaheadIteratorHasNextField,
    ts_lookahead_iterator_next(iterator) ? JNI_TRUE : JNI_FALSE
  );
  return _newObject(
    _symbolClass,
    _symbolConstructor,
    (jint)symbol,
    (jint)type,
    env->NewStringUTF(name)
  );
}
