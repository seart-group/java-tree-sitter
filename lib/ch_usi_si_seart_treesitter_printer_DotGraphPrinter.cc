#include "ch_usi_si_seart_treesitter.h"
#include "ch_usi_si_seart_treesitter_printer_DotGraphPrinter.h"
#include <jni.h>
#include <tree_sitter/api.h>

JNIEXPORT void JNICALL Java_ch_usi_si_seart_treesitter_printer_DotGraphPrinter_write(
  JNIEnv* env, jobject thisObject, jobject fileObject) {
  jobject treeObject = env->GetObjectField(thisObject, _dotGraphPrinterTreeField);
  if (treeObject == NULL) {
      env->ThrowNew(_nullPointerExceptionClass, "Tree must not be null!");
      return;
  }
  jlong tree = __getPointer(env, treeObject);
  if (tree == 0) {
      env->ThrowNew(_illegalArgumentExceptionClass, "Can not export an invalid tree!");
      return;
  }
  jclass fileClass = _getClass("java/io/File");
  jfieldID pathField = _getField(fileClass, "path", "Ljava/lang/String;");
  jstring path = (jstring)env->GetObjectField(fileObject, pathField);
  const char* pathPtr = env->GetStringUTFChars(path, NULL);
  FILE* file = fopen(pathPtr, "w");
  if (file == NULL) {
      env->ThrowNew(_ioExceptionClass, "Could not open a file for export");
      env->ReleaseStringUTFChars(path, pathPtr);
      return;
  }
  int fd = fileno(file);
  if (fd == -1) {
      env->ThrowNew(_ioExceptionClass, "Could not obtain the file descriptor");
      env->ReleaseStringUTFChars(path, pathPtr);
      return;
  }
  ts_tree_print_dot_graph((TSTree*)tree, fd);
  fclose(file);
  env->ReleaseStringUTFChars(path, pathPtr);
}
