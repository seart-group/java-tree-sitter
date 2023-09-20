#include "ch_usi_si_seart_treesitter.h"
#include "ch_usi_si_seart_treesitter_TreeCursor.h"
#include <jni.h>
#include <string.h>
#include <tree_sitter/api.h>

JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_TreeCursor_malloc(
  JNIEnv* env, jclass self, jobject nodeInstance) {
  if (nodeInstance == NULL) {
    env->ThrowNew(_nullPointerExceptionClass, "Node must not be null!");
    return (jlong)0;
  }
  TSNode node = __unmarshalNode(env, nodeInstance);
  if (ts_node_is_null(node)) {
    env->ThrowNew(_illegalArgumentExceptionClass, "Cannot construct a TreeCursor instance from a `null` Node!");
    return (jlong)0;
  }
  TSTreeCursor* cursor = new TSTreeCursor(ts_tree_cursor_new(node));
  return (jlong)cursor;
}

JNIEXPORT void JNICALL Java_ch_usi_si_seart_treesitter_TreeCursor_close(
  JNIEnv* env, jobject thisObject) {
  jlong treeCursor = __getPointer(env, thisObject);
  delete (TSTreeCursor*)treeCursor;
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
