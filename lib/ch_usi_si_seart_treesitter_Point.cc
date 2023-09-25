#include "ch_usi_si_seart_treesitter.h"
#include "ch_usi_si_seart_treesitter_Point.h"
#include <jni.h>
#include <tree_sitter/api.h>

JNIEXPORT jint JNICALL Java_ch_usi_si_seart_treesitter_Point_compareTo(
  JNIEnv* env, jobject thisObject, jobject otherObject) {
  if (otherObject == NULL)
    return __throwNPE(env, "Point must not be null!");
  TSPoint thisPoint = __unmarshalPoint(env, thisObject);
  TSPoint otherPoint = __unmarshalPoint(env, otherObject);
  return (jint)__comparePoints(thisPoint, otherPoint);
}
