#include <jni.h>
#include <tree_sitter/api.h>

#ifndef _Included_ch_usi_si_seart_treesitter
#define _Included_ch_usi_si_seart_treesitter

extern jclass _stringClass;

extern jclass _listClass;
extern jmethodID _listGet;

extern jclass _mapClass;
extern jclass _mapEntryClass;
extern jmethodID _mapEntryStaticMethod;

extern jclass _nullPointerExceptionClass;
extern jclass _illegalArgumentExceptionClass;
extern jclass _illegalStateExceptionClass;
extern jclass _ioExceptionClass;

extern jclass _timeoutExceptionClass;
extern jmethodID _timeoutExceptionConstructor;

extern jclass _indexOutOfBoundsExceptionClass;
extern jmethodID _indexOutOfBoundsExceptionConstructor;

extern jclass _externalClass;
extern jfieldID _externalPointerField;

extern jclass _nodeClass;
extern jmethodID _nodeConstructor;
extern jfieldID _nodeContext0Field;
extern jfieldID _nodeContext1Field;
extern jfieldID _nodeContext2Field;
extern jfieldID _nodeContext3Field;
extern jfieldID _nodeIdField;
extern jfieldID _nodeTreeField;

extern jclass _pointClass;
extern jmethodID _pointConstructor;
extern jfieldID _pointRowField;
extern jfieldID _pointColumnField;
extern jmethodID _pointOriginStaticMethod;

extern jclass _queryMatchClass;
extern jmethodID _queryMatchConstructor;
extern jfieldID _queryMatchIdField;
extern jfieldID _queryMatchPatternField;

extern jclass _inputEditClass;
extern jmethodID _inputEditConstructor;
extern jfieldID _inputEditStartByteField;
extern jfieldID _inputEditOldEndByteField;
extern jfieldID _inputEditNewEndByteField;
extern jfieldID _inputEditStartPointField;
extern jfieldID _inputEditOldEndPointField;
extern jfieldID _inputEditNewEndPointField;

extern jclass _treeCursorNodeClass;
extern jmethodID _treeCursorNodeConstructor;
extern jfieldID _treeCursorNodeNameField;
extern jfieldID _treeCursorNodeTypeField;
extern jfieldID _treeCursorNodeContentField;
extern jfieldID _treeCursorNodeStartByteField;
extern jfieldID _treeCursorNodeEndByteField;
extern jfieldID _treeCursorNodeStartPointField;
extern jfieldID _treeCursorNodeEndPointField;
extern jfieldID _treeCursorNodeIsNamed;

extern jclass _parserClass;
extern jfieldID _parserLanguageField;
extern jmethodID _parserConstructor;

extern jclass _treeClass;
extern jfieldID _treeLanguageField;
extern jfieldID _treeSourceField;
extern jmethodID _treeConstructor;

extern jclass _dotGraphPrinterClass;
extern jfieldID _dotGraphPrinterTreeField;

extern jclass _queryClass;
extern jfieldID _queryLanguageField;
extern jfieldID _queryPatternsField;
extern jfieldID _queryCapturesField;
extern jfieldID _queryStringsField;
extern jmethodID _queryConstructor;

extern jclass _patternClass;
extern jmethodID _patternConstructor;
extern jfieldID _patternQueryField;
extern jfieldID _patternIndexField;
extern jfieldID _patternValueField;
extern jfieldID _patternEnabledField;

extern jclass _captureClass;
extern jmethodID _captureConstructor;
extern jfieldID _captureQueryField;
extern jfieldID _captureIndexField;
extern jfieldID _captureNameField;
extern jfieldID _captureEnabledField;

extern jclass _queryCursorClass;
extern jmethodID _queryCursorConstructor;
extern jfieldID _queryCursorNodeField;
extern jfieldID _queryCursorQueryField;
extern jfieldID _queryCursorExecutedField;

extern jclass _symbolClass;
extern jmethodID _symbolConstructor;

extern jclass _treeCursorClass;
extern jfieldID _treeCursorContext0Field;
extern jfieldID _treeCursorContext1Field;
extern jfieldID _treeCursorIdField;
extern jfieldID _treeCursorTreeField;
extern jmethodID _treeCursorConstructor;

extern jclass _treeSitterExceptionClass;

extern jclass _byteOffsetOutOfBoundsExceptionClass;
extern jmethodID _byteOffsetOutOfBoundsExceptionConstructor;
extern jclass _pointOutOfBoundsExceptionClass;
extern jmethodID _pointOutOfBoundsExceptionConstructor;

extern jclass _querySyntaxExceptionClass;
extern jmethodID _querySyntaxExceptionConstructor;
extern jclass _queryNodeTypeExceptionClass;
extern jmethodID _queryNodeTypeExceptionConstructor;
extern jclass _queryFieldExceptionClass;
extern jmethodID _queryFieldExceptionConstructor;
extern jclass _queryCaptureExceptionClass;
extern jmethodID _queryCaptureExceptionConstructor;
extern jclass _queryStructureExceptionClass;
extern jmethodID _queryStructureExceptionConstructor;

extern jclass _parsingExceptionClass;
extern jmethodID _parsingExceptionConstructor;

extern jclass _incompatibleLanguageExceptionClass;
extern jmethodID _incompatibleLanguageExceptionConstructor;

#ifdef __cplusplus
extern "C" {
#endif

#define _throwNew(CLASS, MESSAGE) \
  env->ThrowNew(CLASS, MESSAGE)

#define _getClass(NAME) \
  env->FindClass(NAME)

#define _loadClass(VARIABLE, NAME)               \
  {                                              \
    jclass local;                                \
    local = _getClass(NAME);                     \
    VARIABLE = (jclass)env->NewGlobalRef(local); \
    env->DeleteLocalRef(local);                  \
  }

#define _loadStaticObject(VARIABLE, CLASS, FIELD)    \
  {                                                  \
    jobject local;                                   \
    local = env->GetStaticObjectField(CLASS, FIELD); \
    VARIABLE = env->NewGlobalRef(local);             \
    env->DeleteLocalRef(local);                      \
  }

#define _unload(VARIABLE) \
  { env->DeleteGlobalRef(VARIABLE); }

#define _getField(CLASS, NAME, TYPE) \
  env->GetFieldID(CLASS, NAME, TYPE)

#define _getStaticField(CLASS, NAME, TYPE) \
  env->GetStaticFieldID(CLASS, NAME, TYPE)

#define _loadField(VARIABLE, CLASS, NAME, TYPE) \
  { VARIABLE = _getField(CLASS, NAME, TYPE); }

#define _loadStaticField(VARIABLE, CLASS, NAME, TYPE) \
  { VARIABLE = _getStaticField(CLASS, NAME, TYPE); }

#define _getMethod(CLASS, NAME, SIGNATURE) \
  env->GetMethodID(CLASS, NAME, SIGNATURE)

#define _getStaticMethod(CLASS, NAME, SIGNATURE) \
  env->GetStaticMethodID(CLASS, NAME, SIGNATURE)

#define _getConstructor(CLASS, SIGNATURE) \
  _getMethod(CLASS, "<init>", SIGNATURE)

#define _loadMethod(VARIABLE, CLASS, NAME, SIGNATURE) \
  { VARIABLE = _getMethod(CLASS, NAME, SIGNATURE); }

#define _loadStaticMethod(VARIABLE, CLASS, NAME, SIGNATURE) \
  { VARIABLE = _getStaticMethod(CLASS, NAME, SIGNATURE); }

#define _loadConstructor(VARIABLE, CLASS, SIGNATURE) \
  { VARIABLE = _getConstructor(CLASS, SIGNATURE); }

#define _setNodeTreeField(NODE, TREE) \
  env->SetObjectField(NODE, _nodeTreeField, TREE)

typedef enum {
  LT = -1,
  EQ =  0,
  GT =  1,
} ComparisonResult;

ComparisonResult intcmp(uint32_t x, uint32_t y);

jint __throwNPE(JNIEnv* env, const char* message);

jint __throwIAE(JNIEnv* env, const char* message);

jint __throwISE(JNIEnv* env, const char* message);

jint __throwIOE(JNIEnv* env, const char* message);

jint __throwIOB(JNIEnv* env, jint index);

jint __throwBOB(JNIEnv* env, jint index);

jint __throwPOB(JNIEnv* env, jobject pointObject);

jint __throwILE(JNIEnv* env, jobject languageObject);

jlong __getPointer(JNIEnv* env, jobject objectInstance);

void __clearPointer(JNIEnv* env, jobject objectInstance);

jobject __marshalNode(JNIEnv* env, TSNode node);

TSNode __unmarshalNode(JNIEnv* env, jobject nodeObject);

void __copyTree(JNIEnv* env, jobject sourceNodeObject, jobject targetNodeObject);

ComparisonResult __comparePoints(TSPoint left, TSPoint right);

jobject __marshalPoint(JNIEnv* env, TSPoint point);

TSPoint __unmarshalPoint(JNIEnv* env, jobject pointObject);

TSInputEdit __unmarshalInputEdit(JNIEnv* env, jobject inputEdit);

const TSLanguage* __unmarshalLanguage(JNIEnv* env, jobject languageObject);

#ifdef TS_LANGUAGE_ADA
TSLanguage* tree_sitter_ada();
#endif
#ifdef TS_LANGUAGE_BASH
TSLanguage* tree_sitter_bash();
#endif
#ifdef TS_LANGUAGE_C
TSLanguage* tree_sitter_c();
#endif
#ifdef TS_LANGUAGE_C_SHARP
TSLanguage* tree_sitter_c_sharp();
#endif
#ifdef TS_LANGUAGE_CLOJURE
TSLanguage* tree_sitter_clojure();
#endif
#ifdef TS_LANGUAGE_CMAKE
TSLanguage* tree_sitter_cmake();
#endif
#ifdef TS_LANGUAGE_COMMON_LISP
TSLanguage* tree_sitter_commonlisp();
#endif
#ifdef TS_LANGUAGE_CPP
TSLanguage* tree_sitter_cpp();
#endif
#ifdef TS_LANGUAGE_CSS
TSLanguage* tree_sitter_css();
#endif
#ifdef TS_LANGUAGE_DART
TSLanguage* tree_sitter_dart();
#endif
#ifdef TS_LANGUAGE_DOCKERFILE
TSLanguage* tree_sitter_dockerfile();
#endif
#ifdef TS_LANGUAGE_DOT
TSLanguage* tree_sitter_dot();
#endif
#ifdef TS_LANGUAGE_DTD
TSLanguage* tree_sitter_dtd();
#endif
#ifdef TS_LANGUAGE_ELIXIR
TSLanguage* tree_sitter_elixir();
#endif
#ifdef TS_LANGUAGE_ELM
TSLanguage* tree_sitter_elm();
#endif
#ifdef TS_LANGUAGE_EMBEDDED_TEMPLATE
TSLanguage* tree_sitter_embedded_template();
#endif
#ifdef TS_LANGUAGE_ERLANG
TSLanguage* tree_sitter_erlang();
#endif
#ifdef TS_LANGUAGE_FORTRAN
TSLanguage* tree_sitter_fortran();
#endif
#ifdef TS_LANGUAGE_GITATTRIBUTES
TSLanguage* tree_sitter_gitattributes();
#endif
#ifdef TS_LANGUAGE_GITIGNORE
TSLanguage* tree_sitter_gitignore();
#endif
#ifdef TS_LANGUAGE_GO
TSLanguage* tree_sitter_go();
#endif
#ifdef TS_LANGUAGE_GRAPHQL
TSLanguage* tree_sitter_graphql();
#endif
#ifdef TS_LANGUAGE_HASKELL
TSLanguage* tree_sitter_haskell();
#endif
#ifdef TS_LANGUAGE_HCL
TSLanguage* tree_sitter_hcl();
#endif
#ifdef TS_LANGUAGE_HTML
TSLanguage* tree_sitter_html();
#endif
#ifdef TS_LANGUAGE_JAVA
TSLanguage* tree_sitter_java();
#endif
#ifdef TS_LANGUAGE_JAVASCRIPT
TSLanguage* tree_sitter_javascript();
#endif
#ifdef TS_LANGUAGE_JSON
TSLanguage* tree_sitter_json();
#endif
#ifdef TS_LANGUAGE_JULIA
TSLanguage* tree_sitter_julia();
#endif
#ifdef TS_LANGUAGE_KOTLIN
TSLanguage* tree_sitter_kotlin();
#endif
#ifdef TS_LANGUAGE_LATEX
TSLanguage* tree_sitter_latex();
#endif
#ifdef TS_LANGUAGE_LUA
TSLanguage* tree_sitter_lua();
#endif
#ifdef TS_LANGUAGE_MARKDOWN
TSLanguage* tree_sitter_markdown();
#endif
#ifdef TS_LANGUAGE_NIX
TSLanguage* tree_sitter_nix();
#endif
#ifdef TS_LANGUAGE_OBJECTIVE_C
TSLanguage* tree_sitter_objc();
#endif
#ifdef TS_LANGUAGE_OCAML
TSLanguage* tree_sitter_ocaml();
#endif
#ifdef TS_LANGUAGE_PASCAL
TSLanguage* tree_sitter_pascal();
#endif
#ifdef TS_LANGUAGE_PHP
TSLanguage* tree_sitter_php();
#endif
#ifdef TS_LANGUAGE_PYTHON
TSLanguage* tree_sitter_python();
#endif
#ifdef TS_LANGUAGE_R
TSLanguage* tree_sitter_r();
#endif
#ifdef TS_LANGUAGE_RACKET
TSLanguage* tree_sitter_racket();
#endif
#ifdef TS_LANGUAGE_RUBY
TSLanguage* tree_sitter_ruby();
#endif
#ifdef TS_LANGUAGE_RUST
TSLanguage* tree_sitter_rust();
#endif
#ifdef TS_LANGUAGE_SCALA
TSLanguage* tree_sitter_scala();
#endif
#ifdef TS_LANGUAGE_SCHEME
TSLanguage* tree_sitter_scheme();
#endif
#ifdef TS_LANGUAGE_SCSS
TSLanguage* tree_sitter_scss();
#endif
#ifdef TS_LANGUAGE_SVELTE
TSLanguage* tree_sitter_svelte();
#endif
#ifdef TS_LANGUAGE_SWIFT
TSLanguage* tree_sitter_swift();
#endif
#ifdef TS_LANGUAGE_THRIFT
TSLanguage* tree_sitter_thrift();
#endif
#ifdef TS_LANGUAGE_TOML
TSLanguage* tree_sitter_toml();
#endif
#ifdef TS_LANGUAGE_TSX
TSLanguage* tree_sitter_tsx();
#endif
#ifdef TS_LANGUAGE_TWIG
TSLanguage* tree_sitter_twig();
#endif
#ifdef TS_LANGUAGE_TYPESCRIPT
TSLanguage* tree_sitter_typescript();
#endif
#ifdef TS_LANGUAGE_VERILOG
TSLanguage* tree_sitter_verilog();
#endif
#ifdef TS_LANGUAGE_XML
TSLanguage* tree_sitter_xml();
#endif
#ifdef TS_LANGUAGE_YAML
TSLanguage* tree_sitter_yaml();
#endif
#ifdef TS_LANGUAGE_ZIG
TSLanguage* tree_sitter_zig();
#endif

#ifdef __cplusplus
}
#endif
#endif
