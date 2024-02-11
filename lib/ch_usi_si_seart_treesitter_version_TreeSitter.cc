#include "ch_usi_si_seart_treesitter.h"
#include "ch_usi_si_seart_treesitter_version_TreeSitter.h"
#include <jni.h>
#include <tree_sitter/api.h>

JNIEXPORT jint JNICALL Java_ch_usi_si_seart_treesitter_version_TreeSitter_getCurrentABIVersion(
  JNIEnv* env, jclass thisClass) {
  return TREE_SITTER_LANGUAGE_VERSION;
}

JNIEXPORT jint JNICALL Java_ch_usi_si_seart_treesitter_version_TreeSitter_getMinimumABIVersion(
  JNIEnv* env, jclass thisClass) {
  return TREE_SITTER_MIN_COMPATIBLE_LANGUAGE_VERSION;
}
