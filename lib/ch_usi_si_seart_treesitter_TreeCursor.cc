#include "ch_usi_si_seart_treesitter.h"
#include "ch_usi_si_seart_treesitter_TreeCursor.h"
#include <jni.h>
#include <string.h>
#include <tree_sitter/api.h>

JNIEXPORT void JNICALL Java_ch_usi_si_seart_treesitter_TreeCursor_delete(
  JNIEnv* env, jobject thisObject) {
  TSTreeCursor* treeCursor = (TSTreeCursor*)__getPointer(env, thisObject);
  delete treeCursor;
  __clearPointer(env, thisObject);
}

JNIEXPORT jobject JNICALL Java_ch_usi_si_seart_treesitter_TreeCursor_getCurrentNode(
  JNIEnv* env, jobject thisObject) {
  TSTreeCursor* cursor = (TSTreeCursor*)__getPointer(env, thisObject);
  TSNode node = ts_tree_cursor_current_node(cursor);
  jobject nodeObject = __marshalNode(env, node);
  jobject treeObject = env->GetObjectField(thisObject, _treeCursorTreeField);
  _setNodeTreeField(nodeObject, treeObject);
  return nodeObject;
}

JNIEXPORT jstring JNICALL Java_ch_usi_si_seart_treesitter_TreeCursor_getCurrentFieldName(
  JNIEnv* env, jobject thisObject) {
  TSTreeCursor* cursor = (TSTreeCursor*)__getPointer(env, thisObject);
  const char* name = ts_tree_cursor_current_field_name(cursor);
  return env->NewStringUTF(name);
}

JNIEXPORT jobject JNICALL Java_ch_usi_si_seart_treesitter_TreeCursor_getCurrentTreeCursorNode(
  JNIEnv* env, jobject thisObject) {
  TSTreeCursor* cursor = (TSTreeCursor*)__getPointer(env, thisObject);
  const char* name = ts_tree_cursor_current_field_name(cursor);
  TSNode node = ts_tree_cursor_current_node(cursor);
  jobject nodeObject = __marshalNode(env, node);
  jobject treeObject = env->GetObjectField(thisObject, _treeCursorTreeField);
  _setNodeTreeField(nodeObject, treeObject);
  return env->NewObject(
    _treeCursorNodeClass,
    _treeCursorNodeConstructor,
    env->NewStringUTF(name),
    nodeObject
  );
}

JNIEXPORT jboolean JNICALL Java_ch_usi_si_seart_treesitter_TreeCursor_gotoFirstChild(
  JNIEnv* env, jobject thisObject) {
  TSTreeCursor* cursor = (TSTreeCursor*)__getPointer(env, thisObject);
  bool result = ts_tree_cursor_goto_first_child(cursor);
  env->SetIntField(thisObject, _treeCursorContext0Field, cursor->context[0]);
  env->SetIntField(thisObject, _treeCursorContext1Field, cursor->context[1]);
  return result ? JNI_TRUE : JNI_FALSE;
}

JNIEXPORT jboolean JNICALL Java_ch_usi_si_seart_treesitter_TreeCursor_gotoNextSibling(
  JNIEnv* env, jobject thisObject) {
  TSTreeCursor* cursor = (TSTreeCursor*)__getPointer(env, thisObject);
  bool result = ts_tree_cursor_goto_next_sibling(cursor);
  env->SetIntField(thisObject, _treeCursorContext0Field, cursor->context[0]);
  env->SetIntField(thisObject, _treeCursorContext1Field, cursor->context[1]);
  return result ? JNI_TRUE : JNI_FALSE;
}

JNIEXPORT jboolean JNICALL Java_ch_usi_si_seart_treesitter_TreeCursor_gotoParent(
  JNIEnv* env, jobject thisObject) {
  TSTreeCursor* cursor = (TSTreeCursor*)__getPointer(env, thisObject);
  bool result = ts_tree_cursor_goto_parent(cursor);
  env->SetIntField(thisObject, _treeCursorContext0Field, cursor->context[0]);
  env->SetIntField(thisObject, _treeCursorContext1Field, cursor->context[1]);
  return result ? JNI_TRUE : JNI_FALSE;
}

JNIEXPORT jobject JNICALL Java_ch_usi_si_seart_treesitter_TreeCursor_clone(
  JNIEnv* env, jobject thisObject) {
  jobject treeObject = env->GetObjectField(thisObject, _treeCursorTreeField);
  const TSTreeCursor* cursor = (const TSTreeCursor*)__getPointer(env, thisObject);
  TSTreeCursor copy = ts_tree_cursor_copy(cursor);
  return env->NewObject(
    _treeCursorClass,
    _treeCursorConstructor,
    new TSTreeCursor(copy),
    copy.context[0],
    copy.context[1],
    copy.id,
    treeObject
  );
}
