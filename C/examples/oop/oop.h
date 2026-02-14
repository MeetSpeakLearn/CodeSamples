#include <stdlib.h>
#include <string.h>
// https://www.gnu.org/software/c-intro-and-ref/manual/html_node/Variable-Number-of-Arguments.html
#include <stdarg.h>

typedef enum _member_type_enum {
    UNKNOWN, ATTRIBUTE, METHOD
} _member_type;

typedef enum _restriction_enum {
    UNKNOWN, PUBLIC, PROTECTED, PRIVATE
} _restriction;

typedef struct _member_decl_struct {
    char *_name;
    _member_type _mem_type;
    _restriction _restrict_type;
    char *type_name;
} _member_decl;

typedef union _attribute_value_union {
    char _char_value;
    signed char _signed_char_value;
    unsigned int _unsigned_int_value;
    int _int_value;
    unsigned long _unsigned_log_value;
    long _long_value;
    unsigned long long _unsigned_long_long_value;
    long long _long_long_value;
    float _float_value;
    double _double_value;
    long double _long_double_value;
    void *_ptr_value;
} _attribute_value;

typedef struct _attribute_struct {
    _member_decl_struct *_member_decl;
    _attribute_value _value;
} _attribute;

typedef struct _class_struct {
    char *class_name;
    _class *_super;
    _member_decl *_class_members;
    int _class_member_count;
    _member_decl *_instance_members;
} _class;

typedef struct _instance_struct {
    _class *_instance_of;
} _instance;
