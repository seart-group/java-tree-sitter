/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class ch_usi_si_seart_treesitter_TreeCursor */

#ifndef _Included_ch_usi_si_seart_treesitter_TreeCursor
#define _Included_ch_usi_si_seart_treesitter_TreeCursor
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     ch_usi_si_seart_treesitter_TreeCursor
 * Method:    close
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_ch_usi_si_seart_treesitter_TreeCursor_close
  (JNIEnv *, jobject);

/*
 * Class:     ch_usi_si_seart_treesitter_TreeCursor
 * Method:    getCurrentNode
 * Signature: ()Lch/usi/si/seart/treesitter/Node;
 */
JNIEXPORT jobject JNICALL Java_ch_usi_si_seart_treesitter_TreeCursor_getCurrentNode
  (JNIEnv *, jobject);

/*
 * Class:     ch_usi_si_seart_treesitter_TreeCursor
 * Method:    getCurrentFieldName
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_ch_usi_si_seart_treesitter_TreeCursor_getCurrentFieldName
  (JNIEnv *, jobject);

/*
 * Class:     ch_usi_si_seart_treesitter_TreeCursor
 * Method:    getCurrentTreeCursorNode
 * Signature: ()Lch/usi/si/seart/treesitter/TreeCursorNode;
 */
JNIEXPORT jobject JNICALL Java_ch_usi_si_seart_treesitter_TreeCursor_getCurrentTreeCursorNode
  (JNIEnv *, jobject);

/*
 * Class:     ch_usi_si_seart_treesitter_TreeCursor
 * Method:    gotoFirstChild
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_ch_usi_si_seart_treesitter_TreeCursor_gotoFirstChild
  (JNIEnv *, jobject);

/*
 * Class:     ch_usi_si_seart_treesitter_TreeCursor
 * Method:    gotoNextSibling
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_ch_usi_si_seart_treesitter_TreeCursor_gotoNextSibling
  (JNIEnv *, jobject);

/*
 * Class:     ch_usi_si_seart_treesitter_TreeCursor
 * Method:    gotoParent
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_ch_usi_si_seart_treesitter_TreeCursor_gotoParent
  (JNIEnv *, jobject);

/*
 * Class:     ch_usi_si_seart_treesitter_TreeCursor
 * Method:    clone
 * Signature: ()Lch/usi/si/seart/treesitter/TreeCursor;
 */
JNIEXPORT jobject JNICALL Java_ch_usi_si_seart_treesitter_TreeCursor_clone
  (JNIEnv *, jobject);

#ifdef __cplusplus
}
#endif
#endif
