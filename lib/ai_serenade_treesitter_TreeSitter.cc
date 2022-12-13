#include "ai_serenade_treesitter_TreeSitter.h"

#include <jni.h>
#include <string.h>
#include <tree_sitter/api.h>

struct TreeCursorNode {
  const char* type;
  const char* name;
  uint32_t startByte;
  uint32_t endByte;
};

static jint JNI_VERSION = JNI_VERSION_10;

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

  _loadClass(_nodeClass, "ai/serenade/treesitter/Node");
  _loadField(_nodeContext0Field, _nodeClass, "context0", "I");
  _loadField(_nodeContext1Field, _nodeClass, "context1", "I");
  _loadField(_nodeContext2Field, _nodeClass, "context2", "I");
  _loadField(_nodeContext3Field, _nodeClass, "context3", "I");
  _loadField(_nodeIdField, _nodeClass, "id", "J");
  _loadField(_nodeTreeField, _nodeClass, "tree", "J");

  _loadClass(_pointClass, "ai/serenade/treesitter/Point");
  _loadField(_pointRowField, _pointClass, "row", "I");
  _loadField(_pointColumnField, _pointClass, "column", "I");

  _loadClass(_queryCursorClass, "ai/serenade/treesitter/QueryCursor");

  _loadClass(_queryCaptureClass, "ai/serenade/treesitter/QueryCapture");
  _loadField(_queryCaptureNode, _queryCaptureClass, "node", "Lai/serenade/treesitter/Node;");
  _loadField(_queryCaptureIndex, _queryCaptureClass, "index", "I");

  _loadClass(_queryMatchClass, "ai/serenade/treesitter/QueryMatch");
  _loadField(_queryMatchIdField, _queryMatchClass, "id", "I");
  _loadField(_queryMatchPatternIndexField, _queryMatchClass, "pattern_index", "I");
  _loadField(_queryMatchCapturesField, _queryMatchClass, "captures", "[Lai/serenade/treesitter/QueryCapture;");

  _loadClass(_treeCursorNodeClass, "ai/serenade/treesitter/TreeCursorNode");
  _loadField(_treeCursorNodeTypeField, _treeCursorNodeClass, "type", "Ljava/lang/String;");
  _loadField(_treeCursorNodeNameField, _treeCursorNodeClass, "name", "Ljava/lang/String;");
  _loadField(_treeCursorNodeStartByteField, _treeCursorNodeClass, "startByte", "I");
  _loadField(_treeCursorNodeEndByteField, _treeCursorNodeClass, "endByte", "I");

  return JNI_VERSION;
}

void JNI_OnUnload(JavaVM* vm, void* reserved) {
  JNIEnv* env;
  vm->GetEnv(reinterpret_cast<void**>(&env), JNI_VERSION);

  env->DeleteGlobalRef(_nodeClass);
  env->DeleteGlobalRef(_treeCursorNodeClass);
}

jobject _marshalNode(JNIEnv* env, TSNode node) {
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
  env->SetObjectField(javaObject, _treeCursorNodeTypeField,
                      env->NewStringUTF(node.type));
  env->SetObjectField(javaObject, _treeCursorNodeNameField,
                      env->NewStringUTF(node.name));
  env->SetIntField(javaObject, _treeCursorNodeStartByteField, node.startByte);
  env->SetIntField(javaObject, _treeCursorNodeEndByteField, node.endByte);
  return javaObject;
}

JNIEXPORT jobject JNICALL Java_ai_serenade_treesitter_TreeSitter_nodeChild(
    JNIEnv* env, jclass self, jobject node, jint child) {
  return _marshalNode(
      env, ts_node_child(_unmarshalNode(env, node), (uint32_t)child));
}

JNIEXPORT jint JNICALL Java_ai_serenade_treesitter_TreeSitter_nodeChildCount(
    JNIEnv* env, jclass self, jobject node) {
  return (jint)ts_node_child_count(_unmarshalNode(env, node));
}

JNIEXPORT jstring JNICALL Java_ai_serenade_treesitter_TreeSitter_nodeString(
    JNIEnv* env, jclass self, jobject node) {
  char* nodeString = ts_node_string(_unmarshalNode(env, node));
  jstring result = env->NewStringUTF(nodeString);
  free(nodeString);
  return result;
}

JNIEXPORT jint JNICALL Java_ai_serenade_treesitter_TreeSitter_nodeEndByte(
    JNIEnv* env, jclass self, jobject node) {
  return (jint)ts_node_end_byte(_unmarshalNode(env, node)) / 2;
}

JNIEXPORT jint JNICALL Java_ai_serenade_treesitter_TreeSitter_nodeStartByte(
    JNIEnv* env, jclass self, jobject node) {
  return (jint)ts_node_start_byte(_unmarshalNode(env, node)) / 2;
}

JNIEXPORT jobject JNICALL Java_ai_serenade_treesitter_TreeSitter_nodeStartPoint(
  JNIEnv* env, jclass self, jobject node) {
  return _marshalPoint(env, ts_node_start_point(_unmarshalNode(env, node)));
}

JNIEXPORT jobject JNICALL Java_ai_serenade_treesitter_TreeSitter_nodeEndPoint(
  JNIEnv* env, jclass self, jobject node) {
  return _marshalPoint(env, ts_node_end_point(_unmarshalNode(env, node)));
}

JNIEXPORT jboolean JNICALL Java_ai_serenade_treesitter_TreeSitter_nodeHasError(
  JNIEnv* env, jclass self, jobject node) {
  return (jboolean) ts_node_has_error(_unmarshalNode(env, node));
}

JNIEXPORT jboolean JNICALL Java_ai_serenade_treesitter_TreeSitter_nodeIsExtra(
  JNIEnv* env, jclass self, jobject node) {
  return (jboolean) ts_node_is_extra(_unmarshalNode(env, node));
}

JNIEXPORT jboolean JNICALL Java_ai_serenade_treesitter_TreeSitter_nodeIsMissing(
  JNIEnv* env, jclass self, jobject node) {
  return (jboolean) ts_node_is_missing(_unmarshalNode(env, node));
}

JNIEXPORT jboolean JNICALL Java_ai_serenade_treesitter_TreeSitter_nodeIsNamed(
  JNIEnv* env, jclass self, jobject node) {
  return (jboolean) ts_node_is_named(_unmarshalNode(env, node));
}

JNIEXPORT jobject JNICALL Java_ai_serenade_treesitter_TreeSitter_nodeParent(
  JNIEnv* env, jclass self, jobject node) {
  TSNode parent = ts_node_parent(_unmarshalNode(env, node));
  if (ts_node_is_null(parent)) {
    return NULL;
  }
  return _marshalNode(env, parent);
}

JNIEXPORT jobject JNICALL Java_ai_serenade_treesitter_TreeSitter_nextSibling(
    JNIEnv* env, jclass self, jobject node) {
  TSNode sibling = ts_node_next_sibling(_unmarshalNode(env, node));
  if (ts_node_is_null(sibling)) {
    return NULL;
  }
  return _marshalNode(env, sibling);
}

JNIEXPORT jobject JNICALL Java_ai_serenade_treesitter_TreeSitter_prevSibling(
    JNIEnv* env, jclass self, jobject node) {
  TSNode sibling = ts_node_prev_sibling(_unmarshalNode(env, node));
  if (ts_node_is_null(sibling)) {
    return NULL;
  }
  return _marshalNode(env, sibling);
}

JNIEXPORT jstring JNICALL Java_ai_serenade_treesitter_TreeSitter_nodeType(
    JNIEnv* env, jclass self, jobject node) {
  const char* type = ts_node_type(_unmarshalNode(env, node));
  jstring result = env->NewStringUTF(type);
  return result;
}

JNIEXPORT jlong JNICALL
Java_ai_serenade_treesitter_TreeSitter_parserNew(JNIEnv* env, jclass self) {
  return (jlong)ts_parser_new();
}

JNIEXPORT void JNICALL Java_ai_serenade_treesitter_TreeSitter_parserDelete(
    JNIEnv* env, jclass self, jlong parser) {
  ts_parser_delete((TSParser*)parser);
}

JNIEXPORT void JNICALL Java_ai_serenade_treesitter_TreeSitter_parserSetLanguage(
    JNIEnv* env, jclass self, jlong parser, jlong language) {
  ts_parser_set_language((TSParser*)parser, (TSLanguage*)language);
}

JNIEXPORT jlong JNICALL Java_ai_serenade_treesitter_TreeSitter_parserParseBytes(
    JNIEnv* env, jclass self, jlong parser, jbyteArray source_bytes,
    jint length) {
  jbyte* source = env->GetByteArrayElements(source_bytes, NULL);
  jlong result = (jlong)ts_parser_parse_string_encoding(
      (TSParser*)parser, NULL, reinterpret_cast<const char*>(source), length, TSInputEncodingUTF16);
  env->ReleaseByteArrayElements(source_bytes, source, JNI_ABORT);
  return result;
}

JNIEXPORT void JNICALL Java_ai_serenade_treesitter_TreeSitter_queryDelete(
    JNIEnv* env, jclass self, jlong query) {
  ts_query_delete((TSQuery*)query);
}

JNIEXPORT jlong JNICALL Java_ai_serenade_treesitter_TreeSitter_queryNew(
  JNIEnv* env, jclass self, jlong language, jstring source) {

  const char* c_source;
  uint32_t source_length = env->GetStringLength(source);
  c_source = env->GetStringUTFChars(source, NULL);
  uint32_t* error_offset = new uint32_t;
  TSQueryError* error_type = new TSQueryError;
  TSQuery* query = ts_query_new((TSLanguage*) language, c_source, source_length, error_offset, error_type);
  return (jlong) query;
}

JNIEXPORT void JNICALL Java_ai_serenade_treesitter_TreeSitter_queryCursorDelete(
  JNIEnv* env, jclass self, jlong query_cursor) {
  ts_query_cursor_delete((TSQueryCursor*)query_cursor);
}

JNIEXPORT jlong JNICALL Java_ai_serenade_treesitter_TreeSitter_queryCursorNew(
  JNIEnv* env, jclass self) {
  TSQueryCursor* cursor = ts_query_cursor_new();
  return (jlong) cursor;
}

JNIEXPORT void JNICALL Java_ai_serenade_treesitter_TreeSitter_queryCursorExec(
  JNIEnv* env, jclass self, jlong query_cursor, jlong query, jobject node) {
  ts_query_cursor_exec((TSQueryCursor*) query_cursor, (TSQuery*) query, _unmarshalNode(env,node));
}

JNIEXPORT jobject JNICALL Java_ai_serenade_treesitter_TreeSitter_queryCursorNextMatch(
  JNIEnv * env, jclass self, jlong query_cursor) {
    TSQueryMatch query_match;

    bool found = ts_query_cursor_next_match((TSQueryCursor*)query_cursor, &query_match);

    if (!found) {
      return NULL;
    }

    return _marshalQueryMatch(env, query_match);
}

JNIEXPORT jlong JNICALL Java_ai_serenade_treesitter_TreeSitter_treeCursorNew(
    JNIEnv* env, jclass self, jobject node) {
  TSTreeCursor* cursor =
      new TSTreeCursor(ts_tree_cursor_new(_unmarshalNode(env, node)));
  return (jlong)cursor;
}

JNIEXPORT jstring JNICALL
Java_ai_serenade_treesitter_TreeSitter_treeCursorCurrentFieldName(
    JNIEnv* env, jclass self, jlong cursor) {
  const char* name = ts_tree_cursor_current_field_name((TSTreeCursor*)cursor);
  jstring result = env->NewStringUTF(name);
  return result;
}

JNIEXPORT jobject JNICALL
Java_ai_serenade_treesitter_TreeSitter_treeCursorCurrentNode(JNIEnv* env,
                                                             jclass self,
                                                             jlong cursor) {
  return _marshalNode(env, ts_tree_cursor_current_node((TSTreeCursor*)cursor));
}

JNIEXPORT jobject JNICALL
Java_ai_serenade_treesitter_TreeSitter_treeCursorCurrentTreeCursorNode(
    JNIEnv* env, jclass self, jlong cursor) {
  TSNode node = ts_tree_cursor_current_node((TSTreeCursor*)cursor);
  return _marshalTreeCursorNode(
      env,
      (TreeCursorNode){ts_node_type(node),
                       ts_tree_cursor_current_field_name((TSTreeCursor*)cursor),
                       ts_node_start_byte(node) / 2, ts_node_end_byte(node) / 2});
}

JNIEXPORT void JNICALL Java_ai_serenade_treesitter_TreeSitter_treeCursorDelete(
    JNIEnv* env, jclass self, jlong cursor) {
  delete (TSTreeCursor*)cursor;
}

JNIEXPORT jboolean JNICALL
Java_ai_serenade_treesitter_TreeSitter_treeCursorGotoFirstChild(JNIEnv* env,
                                                                jclass self,
                                                                jlong cursor) {
  return (jboolean)ts_tree_cursor_goto_first_child((TSTreeCursor*)cursor);
}

JNIEXPORT jboolean JNICALL
Java_ai_serenade_treesitter_TreeSitter_treeCursorGotoNextSibling(JNIEnv* env,
                                                                 jclass self,
                                                                 jlong cursor) {
  return (jboolean)ts_tree_cursor_goto_next_sibling((TSTreeCursor*)cursor);
}

JNIEXPORT jboolean JNICALL
Java_ai_serenade_treesitter_TreeSitter_treeCursorGotoParent(JNIEnv* env,
                                                            jclass self,
                                                            jlong cursor) {
  return (jboolean)ts_tree_cursor_goto_parent((TSTreeCursor*)cursor);
}

JNIEXPORT void JNICALL Java_ai_serenade_treesitter_TreeSitter_treeDelete(
    JNIEnv* env, jclass self, jlong tree) {
  ts_tree_delete((TSTree*)tree);
}

JNIEXPORT jobject JNICALL Java_ai_serenade_treesitter_TreeSitter_treeRootNode(
    JNIEnv* env, jclass self, jlong tree) {
  return _marshalNode(env, ts_tree_root_node((TSTree*)tree));
}
