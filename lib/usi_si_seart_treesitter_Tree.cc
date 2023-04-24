#include "usi_si_seart_treesitter.h"
#include "usi_si_seart_treesitter_Tree.h"
#include <jni.h>
#include <tree_sitter/api.h>

JNIEXPORT void JNICALL Java_usi_si_seart_treesitter_Tree_edit(
  JNIEnv* env, jobject thisObject, jobject inputEdit) {
  jclass treeClass = _getClass("usi/si/seart/treesitter/Tree");
  jlong tree = __getPointer(env, treeClass, thisObject);
  TSInputEdit edit = __unmarshalInputEdit(env, inputEdit);
  ts_tree_edit((TSTree*)tree, &edit);
}

JNIEXPORT jobject JNICALL Java_usi_si_seart_treesitter_Tree_getRootNode(
  JNIEnv* env, jobject thisObject) {
  jclass treeClass = _getClass("usi/si/seart/treesitter/Tree");
  jlong tree = __getPointer(env, treeClass, thisObject);
  return __marshalNode(env, ts_tree_root_node((TSTree*)tree));
}
