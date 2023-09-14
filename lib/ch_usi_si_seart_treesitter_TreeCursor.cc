#include "ch_usi_si_seart_treesitter.h"
#include "ch_usi_si_seart_treesitter_TreeCursor.h"
#include <jni.h>
#include <string.h>
#include <tree_sitter/api.h>

JNIEXPORT jlong JNICALL Java_ch_usi_si_seart_treesitter_TreeCursor_malloc(
  JNIEnv* env, jclass self, jobject nodeInstance) {
  if (nodeInstance == NULL) {
    jclass exceptionClass = _getClass("java/lang/NullPointerException");
    env->ThrowNew(exceptionClass, "Node must not be null!");
    return (jlong)0;
  }
  TSNode node = __unmarshalNode(env, nodeInstance);
  if (ts_node_is_null(node)) {
    jclass exceptionClass = _getClass("java/lang/IllegalArgumentException");
    env->ThrowNew(exceptionClass, "Cannot construct a TreeCursor instance from a `null` Node!");
    return (jlong)0;
  }
  TSTreeCursor* cursor = new TSTreeCursor(ts_tree_cursor_new(node));
  return (jlong)cursor;
}

JNIEXPORT void JNICALL Java_ch_usi_si_seart_treesitter_TreeCursor_close(
  JNIEnv* env, jobject thisObject) {
  jlong treeCursor = __getPointer(env, _treeCursorClass, thisObject);
  delete (TSTreeCursor*)treeCursor;
}

JNIEXPORT jobject JNICALL Java_ch_usi_si_seart_treesitter_TreeCursor_getCurrentNode(
  JNIEnv* env, jobject thisObject) {
  jlong treeCursor = __getPointer(env, _treeCursorClass, thisObject);
  TSNode node = ts_tree_cursor_current_node((TSTreeCursor*)treeCursor);
  return __marshalNode(env, node);
}

JNIEXPORT jstring JNICALL Java_ch_usi_si_seart_treesitter_TreeCursor_getCurrentFieldName(
  JNIEnv* env, jobject thisObject) {
  jlong treeCursor = __getPointer(env, _treeCursorClass, thisObject);
  const char* name = ts_tree_cursor_current_field_name((TSTreeCursor*)treeCursor);
  return env->NewStringUTF(name);
}

JNIEXPORT jobject JNICALL Java_ch_usi_si_seart_treesitter_TreeCursor_getCurrentTreeCursorNode(
  JNIEnv* env, jobject thisObject) {
  jlong treeCursor = __getPointer(env, _treeCursorClass, thisObject);
  TSNode node = ts_tree_cursor_current_node((TSTreeCursor*)treeCursor);
  return __marshalTreeCursorNode(
      env,
      (TreeCursorNode){
        ts_node_type(node),
        ts_tree_cursor_current_field_name((TSTreeCursor*)treeCursor),
        ts_node_start_byte(node) / 2,
        ts_node_end_byte(node) / 2,
        ts_node_start_point(node),
        ts_node_end_point(node),
        ts_node_is_named(node)
      }
  );
}

JNIEXPORT jboolean JNICALL Java_ch_usi_si_seart_treesitter_TreeCursor_gotoFirstChild(
  JNIEnv* env, jobject thisObject) {
  jlong treeCursor = __getPointer(env, _treeCursorClass, thisObject);
  return ts_tree_cursor_goto_first_child((TSTreeCursor*)treeCursor) ? JNI_TRUE : JNI_FALSE;
}

JNIEXPORT jboolean JNICALL Java_ch_usi_si_seart_treesitter_TreeCursor_gotoNextSibling(
  JNIEnv* env, jobject thisObject) {
  jlong treeCursor = __getPointer(env, _treeCursorClass, thisObject);
  return ts_tree_cursor_goto_next_sibling((TSTreeCursor*)treeCursor) ? JNI_TRUE : JNI_FALSE;
}

JNIEXPORT jboolean JNICALL Java_ch_usi_si_seart_treesitter_TreeCursor_gotoParent(
  JNIEnv* env, jobject thisObject) {
  jlong treeCursor = __getPointer(env, _treeCursorClass, thisObject);
  return ts_tree_cursor_goto_parent((TSTreeCursor*)treeCursor) ? JNI_TRUE : JNI_FALSE;
}
