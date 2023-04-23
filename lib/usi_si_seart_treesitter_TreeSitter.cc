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
  _loadField(_queryMatchPatternIndexField, _queryMatchClass, "pattern_index", "I");
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

jobject _marshalNode(JNIEnv* env, TSNode node) {
  if (node.id == 0) return NULL;
  jobject javaObject = env->AllocObject(_nodeClass);
  env->SetIntField(javaObject, _nodeContext0Field, node.context[0]);
  env->SetIntField(javaObject, _nodeContext1Field, node.context[1]);
  env->SetIntField(javaObject, _nodeContext2Field, node.context[2]);
  env->SetIntField(javaObject, _nodeContext3Field, node.context[3]);
  env->SetLongField(javaObject, _nodeIdField, (jlong)node.id);
  env->SetLongField(javaObject, _nodeTreeField, (jlong)node.tree);
  return javaObject;
}

TSNode _unmarshalNode(JNIEnv* env, jobject javaObject) {
  return (TSNode){
      {
          (uint32_t)env->GetIntField(javaObject, _nodeContext0Field),
          (uint32_t)env->GetIntField(javaObject, _nodeContext1Field),
          (uint32_t)env->GetIntField(javaObject, _nodeContext2Field),
          (uint32_t)env->GetIntField(javaObject, _nodeContext3Field),
      },
      (const void*)env->GetLongField(javaObject, _nodeIdField),
      (const TSTree*)env->GetLongField(javaObject, _nodeTreeField)};
}

jobject _marshalPoint(JNIEnv* env, TSPoint point) {
  jobject javaObject = env->AllocObject(_pointClass);
  env->SetIntField(javaObject, _pointRowField, point.row);
  env->SetIntField(javaObject, _pointColumnField, point.column / 2);
  // Not sure why I need to divide by two, probably because of utf-16
  return javaObject;
}

TSPoint _unmarshalPoint(JNIEnv* env, jobject javaObject) {
  return (TSPoint) {
    (uint32_t)env->GetIntField(javaObject, _pointRowField),
    (uint32_t)env->GetIntField(javaObject, _pointColumnField),
  };
}

jobject _marshalQueryCapture(JNIEnv* env, TSQueryCapture capture) {
  jobject javaObject = env->AllocObject(_queryCaptureClass);
  env->SetIntField(javaObject, _queryCaptureIndex, capture.index);
  env->SetObjectField(javaObject, _queryCaptureNode, _marshalNode(env, capture.node));

  return javaObject;
}

jobject _marshalQueryMatch(JNIEnv* env, TSQueryMatch match) {
  jobject javaObject = env->AllocObject(_queryMatchClass);
  env->SetIntField(javaObject, _queryMatchIdField, match.id);
  env->SetIntField(javaObject, _queryMatchPatternIndexField, match.pattern_index);

  jobjectArray captures = (*env).NewObjectArray(match.capture_count, _queryCaptureClass, NULL);
  for (int i = 0; i < match.capture_count; i++) {
    env->SetObjectArrayElement(captures, i, _marshalQueryCapture(env, match.captures[i]));
  }
  env->SetObjectField(javaObject, _queryMatchCapturesField, captures);

  return javaObject;
}

jobject _marshalTreeCursorNode(JNIEnv* env, TreeCursorNode node) {
  jobject javaObject = env->AllocObject(_treeCursorNodeClass);
  env->SetObjectField(javaObject, _treeCursorNodeTypeField, env->NewStringUTF(node.type));
  env->SetObjectField(javaObject, _treeCursorNodeNameField, env->NewStringUTF(node.name));
  env->SetIntField(javaObject, _treeCursorNodeStartByteField, node.startByte);
  env->SetIntField(javaObject, _treeCursorNodeEndByteField, node.endByte);
  env->SetObjectField(javaObject, _treeCursorNodeStartPointField, _marshalPoint(env, node.startPoint));
  env->SetObjectField(javaObject, _treeCursorNodeEndPointField, _marshalPoint(env, node.endPoint));
  env->SetBooleanField(javaObject, _treeCursorNodeIsNamed, node.isNamed);
  return javaObject;
}

TSInputEdit _unmarshalInputEdit(JNIEnv* env, jobject inputEdit) {
  return (TSInputEdit) {
    (uint32_t)env->GetIntField(inputEdit, _inputEditStartByteField),
    (uint32_t)env->GetIntField(inputEdit, _inputEditOldEndByteField),
    (uint32_t)env->GetIntField(inputEdit, _inputEditNewEndByteField),
    _unmarshalPoint(env, env->GetObjectField(inputEdit, _inputEditStartPointField)),
    _unmarshalPoint(env, env->GetObjectField(inputEdit, _inputEditOldEndPointField)),
    _unmarshalPoint(env, env->GetObjectField(inputEdit, _inputEditNewEndPointField)),
  };
}

JNIEXPORT void JNICALL Java_usi_si_seart_treesitter_TreeSitter_parserDelete(
    JNIEnv* env, jclass self, jlong parser) {
  ts_parser_delete((TSParser*)parser);
}

JNIEXPORT void JNICALL Java_usi_si_seart_treesitter_TreeSitter_queryDelete(
    JNIEnv* env, jclass self, jlong query) {
  ts_query_delete((TSQuery*)query);
}

JNIEXPORT jlong JNICALL Java_usi_si_seart_treesitter_TreeSitter_queryNew(
    JNIEnv* env, jclass self, jlong language, jstring source) {
  const char* c_source;
  uint32_t source_length = env->GetStringLength(source);
  c_source = env->GetStringUTFChars(source, NULL);
  uint32_t* error_offset = new uint32_t;
  TSQueryError* error_type = new TSQueryError;
  TSQuery* query = ts_query_new((TSLanguage*)language, c_source, source_length, error_offset, error_type);
  jclass exceptionClass;
  const char* c_pattern;
  switch (*error_type) {
    case TSQueryErrorNone:
      return (jlong)query;
    case TSQueryErrorSyntax:
      exceptionClass = _querySyntaxExceptionClass;
      c_pattern = "Bad syntax at offset %d";
      break;
    case TSQueryErrorNodeType:
      exceptionClass = _queryNodeTypeExceptionClass;
      c_pattern = "Bad node name at offset %d";
      break;
    case TSQueryErrorField:
      exceptionClass = _queryFieldExceptionClass;
      c_pattern = "Bad field name at offset %d";
      break;
    case TSQueryErrorCapture:
      exceptionClass = _queryCaptureExceptionClass;
      c_pattern = "Bad capture at offset %d";
      break;
    case TSQueryErrorStructure:
      exceptionClass = _queryStructureExceptionClass;
      c_pattern = "Bad pattern structure at offset %d";
      break;
    default:
      return env->ThrowNew(_treeSitterException, NULL);
  }
  int digits = static_cast<int>(floor(log10(*error_offset))) + 1;
  char c_message[strlen(c_pattern) + 1 + digits];
  snprintf(c_message, sizeof(c_message), c_pattern, *error_offset);
  return env->ThrowNew(exceptionClass, c_message);
}

JNIEXPORT jstring JNICALL Java_usi_si_seart_treesitter_TreeSitter_queryCaptureName(
  JNIEnv* env, jclass self, jlong query, jint index) {
  uint32_t* length = new uint32_t;
  const char* nameForIndex = ts_query_capture_name_for_id((TSQuery*)query, index, length);
  jstring result = env->NewStringUTF(nameForIndex);
  return result;
}

JNIEXPORT jint JNICALL Java_usi_si_seart_treesitter_TreeSitter_queryPatternCount(
  JNIEnv* env, jclass self, jlong query) {
  return (jint)ts_query_pattern_count((TSQuery*)query);
}

JNIEXPORT jint JNICALL Java_usi_si_seart_treesitter_TreeSitter_queryCaptureCount(
  JNIEnv* env, jclass self, jlong query) {
  return (jint)ts_query_capture_count((TSQuery*)query);
}

JNIEXPORT jint JNICALL Java_usi_si_seart_treesitter_TreeSitter_queryStringCount(
  JNIEnv* env, jclass self, jlong query) {
  return (jint)ts_query_string_count((TSQuery*)query);
}

JNIEXPORT void JNICALL Java_usi_si_seart_treesitter_TreeSitter_queryCursorDelete(
    JNIEnv* env, jclass self, jlong query_cursor) {
  ts_query_cursor_delete((TSQueryCursor*)query_cursor);
}

JNIEXPORT jlong JNICALL Java_usi_si_seart_treesitter_TreeSitter_queryCursorNew(
    JNIEnv* env, jclass self) {
  TSQueryCursor* cursor = ts_query_cursor_new();
  return (jlong)cursor;
}

JNIEXPORT void JNICALL Java_usi_si_seart_treesitter_TreeSitter_queryCursorExec(
    JNIEnv* env, jclass self, jlong query_cursor, jlong query, jobject node) {
  ts_query_cursor_exec((TSQueryCursor*) query_cursor, (TSQuery*) query, _unmarshalNode(env,node));
}

JNIEXPORT jobject JNICALL Java_usi_si_seart_treesitter_TreeSitter_queryCursorNextMatch(
    JNIEnv * env, jclass self, jlong query_cursor) {
  TSQueryMatch query_match;
  bool found = ts_query_cursor_next_match((TSQueryCursor*)query_cursor, &query_match);
  if (!found) return NULL;
  return _marshalQueryMatch(env, query_match);
}

JNIEXPORT jlong JNICALL Java_usi_si_seart_treesitter_TreeSitter_treeCursorNew(
    JNIEnv* env, jclass self, jobject node) {
  TSTreeCursor* cursor = new TSTreeCursor(ts_tree_cursor_new(_unmarshalNode(env, node)));
  return (jlong)cursor;
}

JNIEXPORT jstring JNICALL Java_usi_si_seart_treesitter_TreeSitter_treeCursorCurrentFieldName(
    JNIEnv* env, jclass self, jlong cursor) {
  const char* name = ts_tree_cursor_current_field_name((TSTreeCursor*)cursor);
  jstring result = env->NewStringUTF(name);
  return result;
}

JNIEXPORT jobject JNICALL Java_usi_si_seart_treesitter_TreeSitter_treeCursorCurrentNode(
    JNIEnv* env, jclass self, jlong cursor) {
  return _marshalNode(env, ts_tree_cursor_current_node((TSTreeCursor*)cursor));
}

JNIEXPORT jobject JNICALL Java_usi_si_seart_treesitter_TreeSitter_treeCursorCurrentTreeCursorNode(
    JNIEnv* env, jclass self, jlong cursor) {
  TSNode node = ts_tree_cursor_current_node((TSTreeCursor*)cursor);
  return _marshalTreeCursorNode(
      env,
      (TreeCursorNode){
        ts_node_type(node),
        ts_tree_cursor_current_field_name((TSTreeCursor*)cursor),
        ts_node_start_byte(node) / 2,
        ts_node_end_byte(node) / 2,
        ts_node_start_point(node),
        ts_node_end_point(node),
        ts_node_is_named(node)
      }
  );
}

JNIEXPORT void JNICALL Java_usi_si_seart_treesitter_TreeSitter_treeCursorDelete(
    JNIEnv* env, jclass self, jlong cursor) {
  delete (TSTreeCursor*)cursor;
}

JNIEXPORT jboolean JNICALL Java_usi_si_seart_treesitter_TreeSitter_treeCursorGotoFirstChild(
    JNIEnv* env, jclass self, jlong cursor) {
  return ts_tree_cursor_goto_first_child((TSTreeCursor*)cursor) ? JNI_TRUE : JNI_FALSE;
}

JNIEXPORT jboolean JNICALL Java_usi_si_seart_treesitter_TreeSitter_treeCursorGotoNextSibling(
    JNIEnv* env, jclass self, jlong cursor) {
  return ts_tree_cursor_goto_next_sibling((TSTreeCursor*)cursor) ? JNI_TRUE : JNI_FALSE;
}

JNIEXPORT jboolean JNICALL Java_usi_si_seart_treesitter_TreeSitter_treeCursorGotoParent(
    JNIEnv* env, jclass self, jlong cursor) {
  return ts_tree_cursor_goto_parent((TSTreeCursor*)cursor) ? JNI_TRUE : JNI_FALSE;
}

JNIEXPORT void JNICALL Java_usi_si_seart_treesitter_TreeSitter_treeDelete(
    JNIEnv* env, jclass self, jlong tree) {
  ts_tree_delete((TSTree*)tree);
}


JNIEXPORT void JNICALL Java_usi_si_seart_treesitter_TreeSitter_treeEdit(
    JNIEnv* env, jclass self, jlong tree, jobject inputEdit) {
  TSInputEdit edit = _unmarshalInputEdit(env, inputEdit);
  ts_tree_edit((TSTree*) tree, &edit);
}

JNIEXPORT jobject JNICALL Java_usi_si_seart_treesitter_TreeSitter_treeRootNode(
    JNIEnv* env, jclass self, jlong tree) {
  return _marshalNode(env, ts_tree_root_node((TSTree*)tree));
}
