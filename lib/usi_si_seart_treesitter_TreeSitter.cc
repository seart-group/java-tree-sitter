#include "usi_si_seart_treesitter_TreeSitter.h"

#include <jni.h>
#include <math.h>
#include <string.h>
#include <tree_sitter/api.h>

struct TreeCursorNode {
  const char* type;
  const char* name;
  uint32_t startByte;
  uint32_t endByte;
  TSPoint startPoint;
  TSPoint endPoint;
  bool isNamed;
};

static jint JNI_VERSION = JNI_VERSION_10;

static jclass _inputEditClass;
static jfieldID _inputEditStartByteField;
static jfieldID _inputEditOldEndByteField;
static jfieldID _inputEditNewEndByteField;
static jfieldID _inputEditStartPointField;
static jfieldID _inputEditOldEndPointField;
static jfieldID _inputEditNewEndPointField;

static jclass _nodeClass;
static jfieldID _nodeContext0Field;
static jfieldID _nodeContext1Field;
static jfieldID _nodeContext2Field;
static jfieldID _nodeContext3Field;
static jfieldID _nodeIdField;
static jfieldID _nodeTreeField;

static jclass _pointClass;
static jfieldID _pointRowField;
static jfieldID _pointColumnField;

static jclass _queryCursorClass;

static jclass _queryMatchClass;
static jfieldID _queryMatchIdField;
static jfieldID _queryMatchPatternIndexField;
static jfieldID _queryMatchCapturesField;

static jclass _queryCaptureClass;
static jfieldID _queryCaptureNode;
static jfieldID _queryCaptureIndex;

static jclass _treeCursorNodeClass;
static jfieldID _treeCursorNodeTypeField;
static jfieldID _treeCursorNodeNameField;
static jfieldID _treeCursorNodeStartByteField;
static jfieldID _treeCursorNodeEndByteField;
static jfieldID _treeCursorNodeStartPointField;
static jfieldID _treeCursorNodeEndPointField;
static jfieldID _treeCursorNodeIsNamed;

static jclass _treeSitterException;

static jclass _queryCaptureExceptionClass;
static jclass _queryFieldExceptionClass;
static jclass _queryNodeTypeExceptionClass;
static jclass _queryStructureExceptionClass;
static jclass _querySyntaxExceptionClass;

#define _loadClass(VARIABLE, NAME)             \
  {                                            \
    jclass tmp;                                \
    tmp = env->FindClass(NAME);                \
    VARIABLE = (jclass)env->NewGlobalRef(tmp); \
    env->DeleteLocalRef(tmp);                  \
  }

#define _loadField(VARIABLE, CLASS, NAME, TYPE) \
  { VARIABLE = env->GetFieldID(CLASS, NAME, TYPE); }

jint JNI_OnLoad(JavaVM* vm, void* reserved) {
  JNIEnv* env;
  if (vm->GetEnv(reinterpret_cast<void**>(&env), JNI_VERSION) != JNI_OK) {
    return JNI_ERR;
  }

  _loadClass(_nodeClass, "usi/si/seart/treesitter/Node");
  _loadField(_nodeContext0Field, _nodeClass, "context0", "I");
  _loadField(_nodeContext1Field, _nodeClass, "context1", "I");
  _loadField(_nodeContext2Field, _nodeClass, "context2", "I");
  _loadField(_nodeContext3Field, _nodeClass, "context3", "I");
  _loadField(_nodeIdField, _nodeClass, "id", "J");
  _loadField(_nodeTreeField, _nodeClass, "tree", "J");

  _loadClass(_pointClass, "usi/si/seart/treesitter/Point");
  _loadField(_pointRowField, _pointClass, "row", "I");
  _loadField(_pointColumnField, _pointClass, "column", "I");

  _loadClass(_queryCursorClass, "usi/si/seart/treesitter/QueryCursor");

  _loadClass(_queryCaptureClass, "usi/si/seart/treesitter/QueryCapture");
  _loadField(_queryCaptureNode, _queryCaptureClass, "node", "Lusi/si/seart/treesitter/Node;");
  _loadField(_queryCaptureIndex, _queryCaptureClass, "index", "I");

  _loadClass(_queryMatchClass, "usi/si/seart/treesitter/QueryMatch");
  _loadField(_queryMatchIdField, _queryMatchClass, "id", "I");
  _loadField(_queryMatchPatternIndexField, _queryMatchClass, "patternIndex", "I");
  _loadField(_queryMatchCapturesField, _queryMatchClass, "captures", "[Lusi/si/seart/treesitter/QueryCapture;");

  _loadClass(_treeCursorNodeClass, "usi/si/seart/treesitter/TreeCursorNode");
  _loadField(_treeCursorNodeTypeField, _treeCursorNodeClass, "type", "Ljava/lang/String;");
  _loadField(_treeCursorNodeNameField, _treeCursorNodeClass, "name", "Ljava/lang/String;");
  _loadField(_treeCursorNodeStartByteField, _treeCursorNodeClass, "startByte", "I");
  _loadField(_treeCursorNodeEndByteField, _treeCursorNodeClass, "endByte", "I");
  _loadField(_treeCursorNodeStartPointField, _treeCursorNodeClass, "startPoint", "Lusi/si/seart/treesitter/Point;");
  _loadField(_treeCursorNodeEndPointField, _treeCursorNodeClass, "endPoint", "Lusi/si/seart/treesitter/Point;");
  _loadField(_treeCursorNodeIsNamed, _treeCursorNodeClass, "isNamed", "Z");

  _loadClass(_inputEditClass, "usi/si/seart/treesitter/InputEdit");
  _loadField(_inputEditStartByteField, _inputEditClass, "startByte", "I");
  _loadField(_inputEditOldEndByteField, _inputEditClass, "oldEndByte", "I");
  _loadField(_inputEditNewEndByteField, _inputEditClass, "newEndByte", "I");
  _loadField(_inputEditStartPointField, _inputEditClass, "startPoint", "Lusi/si/seart/treesitter/Point;");
  _loadField(_inputEditOldEndPointField, _inputEditClass, "oldEndPoint", "Lusi/si/seart/treesitter/Point;");
  _loadField(_inputEditNewEndPointField, _inputEditClass, "newEndPoint", "Lusi/si/seart/treesitter/Point;");

  _loadClass(_treeSitterException, "usi/si/seart/treesitter/exception/TreeSitterException");

  _loadClass(_queryCaptureExceptionClass, "usi/si/seart/treesitter/exception/query/QueryCaptureException");
  _loadClass(_queryFieldExceptionClass, "usi/si/seart/treesitter/exception/query/QueryFieldException");
  _loadClass(_queryNodeTypeExceptionClass, "usi/si/seart/treesitter/exception/query/QueryNodeTypeException");
  _loadClass(_queryStructureExceptionClass, "usi/si/seart/treesitter/exception/query/QueryStructureException");
  _loadClass(_querySyntaxExceptionClass, "usi/si/seart/treesitter/exception/query/QuerySyntaxException");

  return JNI_VERSION;
}

void JNI_OnUnload(JavaVM* vm, void* reserved) {
  JNIEnv* env;
  vm->GetEnv(reinterpret_cast<void**>(&env), JNI_VERSION);
  env->DeleteGlobalRef(_nodeClass);
  env->DeleteGlobalRef(_treeCursorNodeClass);
}
