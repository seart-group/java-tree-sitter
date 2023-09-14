#include "ch_usi_si_seart_treesitter.h"
#include "ch_usi_si_seart_treesitter_Tree.h"
#include <jni.h>
#include <tree_sitter/api.h>

JNIEXPORT void JNICALL Java_ch_usi_si_seart_treesitter_Tree_close(
  JNIEnv* env, jobject thisObject) {
  jlong tree = __getPointer(env, _treeClass, thisObject);
  ts_tree_delete((TSTree*)tree);
}

JNIEXPORT void JNICALL Java_ch_usi_si_seart_treesitter_Tree_edit(
  JNIEnv* env, jobject thisObject, jobject inputEditObject) {
  jlong tree = __getPointer(env, _treeClass, thisObject);
  if (inputEditObject == NULL) {
      env->ThrowNew(_nullPointerExceptionClass, "Input edit must not be null!");
      return;
  }
  TSInputEdit inputEdit = __unmarshalInputEdit(env, inputEditObject);
  ts_tree_edit((TSTree*)tree, &inputEdit);
}

JNIEXPORT jobject JNICALL Java_ch_usi_si_seart_treesitter_Tree_getRootNode(
  JNIEnv* env, jobject thisObject) {
  jlong tree = __getPointer(env, _treeClass, thisObject);
  return __marshalNode(env, ts_tree_root_node((TSTree*)tree));
}
