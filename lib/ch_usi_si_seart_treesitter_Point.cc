#include "ch_usi_si_seart_treesitter.h"
#include "ch_usi_si_seart_treesitter_Point.h"
#include <jni.h>
#include <tree_sitter/api.h>

JNIEXPORT jint JNICALL Java_ch_usi_si_seart_treesitter_Point_compareTo(
  JNIEnv* env, jobject thisObject, jobject otherObject) {
  if (otherObject == NULL) {
    env->ThrowNew(
      _nullPointerExceptionClass,
      "Point must not be null!"
    );
    return 0;
  }
  TSPoint thisPoint = __unmarshalPoint(env, thisObject);
  TSPoint otherPoint = __unmarshalPoint(env, otherObject);
  return (jint)__comparePoints(thisPoint, otherPoint);
}
