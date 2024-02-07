#include "ch_usi_si_seart_treesitter.h"
#include "ch_usi_si_seart_treesitter_Tree.h"
#include <jni.h>
#include <tree_sitter/api.h>

JNIEXPORT void JNICALL Java_ch_usi_si_seart_treesitter_Tree_delete(
  JNIEnv* env, jobject thisObject) {
  TSTree* tree = (TSTree*)__getPointer(env, thisObject);
  ts_tree_delete(tree);
  __clearPointer(env, thisObject);
}

JNIEXPORT void JNICALL Java_ch_usi_si_seart_treesitter_Tree_edit(
  JNIEnv* env, jobject thisObject, jobject inputEditObject) {
  jlong tree = __getPointer(env, thisObject);
  if (inputEditObject == NULL) {
    __throwNPE(env, "Input edit must not be null!");
    return;
  }
  TSInputEdit inputEdit = __unmarshalInputEdit(env, inputEditObject);
  ts_tree_edit((TSTree*)tree, &inputEdit);
}

JNIEXPORT jobject JNICALL Java_ch_usi_si_seart_treesitter_Tree_getChangedRanges(
  JNIEnv* env, jobject thisObject, jobject otherObject) {
  TSTree* old_tree = (TSTree*)__getPointer(env, thisObject);
  TSTree* new_tree = (TSTree*)__getPointer(env, otherObject);
  uint32_t* length = new uint32_t;
  TSRange* ranges = ts_tree_get_changed_ranges(old_tree, new_tree, length);
  jobjectArray array = env->NewObjectArray(*length, _rangeClass, NULL);
  for (uint32_t i = 0; i < *length; i++) {
    TSRange range = ranges[i];
    jobject rangeObject = __marshalRange(env, range);
    env->SetObjectArrayElement(array, i, rangeObject);
  }
  return env->CallStaticObjectMethod(_listClass, _listOfStaticMethod, array);
}

JNIEXPORT jobject JNICALL Java_ch_usi_si_seart_treesitter_Tree_getRootNode(
  JNIEnv* env, jobject thisObject) {
  jlong tree = __getPointer(env, thisObject);
  TSNode node = ts_tree_root_node((TSTree*)tree);
  jobject nodeObject = __marshalNode(env, node);
  _setNodeTreeField(nodeObject, thisObject);
  return nodeObject;
}

JNIEXPORT jobject JNICALL Java_ch_usi_si_seart_treesitter_Tree_clone(
  JNIEnv* env, jobject thisObject) {
  jobject languageObject = env->GetObjectField(thisObject, _treeLanguageField);
  jobject sourceObject = env->GetObjectField(thisObject, _treeSourceField);
  jlong tree = __getPointer(env, thisObject);
  jlong copy = (jlong)ts_tree_copy((const TSTree*)tree);
  return env->NewObject(_treeClass, _treeConstructor, copy, languageObject, sourceObject);
}
