//Copyright (c) 2023, Steve Devoy
//
//This is a personal project in progress. This project started on November 1, 2023.
//I spend about 30 minutes per day working on it.
//
//All of the code is in a single file. Yes, it will be broken into header and source files when I refactor it,
//but as this is a personal project with no collaborators and with no intention of applying it to any project,
//I am keeping all of the code in a single file until I hit my first milestone: getting it work. Until then,
//I expect many radical changes, as it is exploratory. Rather than refactor, over and over, I've decided to
//keep it all together until that point in time.
//
//The goal is to create a simple Lisp Interpreter in C as a means to study various concepts in practice, such as
//garbage collectors, LISP architecture, etc.
//
//I chose the C language simply to display my C skills. The project would be easier to carry out in C++ and very
//easy to carry out in C#, Java, Python, or another high level object oriented language with garbage collection.
//However, that would obscure exactly what I am exploring.

#include <stdio.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <ctype.h>

#define CONS_TYPE       0b0001
#define SYMBOL_TYPE     0b0010
#define INTEGER_TYPE    0b0100
#define FLOAT_TYPE      0b0110
#define STRING_TYPE     0b1000
#define ARRAY_TYPE      0b1100

#define REACHABLE_MASK      0b10000000
#define NOT_REACHABLE_MASK  0b01111111

#define MEMORY_SIZE 5120
#define ERROR_MESSAGE_BUFFER_SIZE 512
#define ERROR_MESSAGE_BUFFER

#define SYMBOL_CHARS "/*-+_=<>"

// Simple Linked List

typedef struct ll_node_struct {
    void* content;
    struct ll_node_struct* next;
} ll_node;

typedef struct ll_list_struct {
    ll_node *head;
    ll_node *tail;
    ll_node *current;
    int count;
} linked_list;

linked_list* create_linked_list() {
    linked_list* ll = (linked_list*) malloc(sizeof(linked_list));
    ll->current = ll->head = ll->tail = 0;
    ll->count = 0;
    return ll;
}

void free_linked_list(linked_list* ll) {
    if (ll == 0) return;
    if (ll->count == 0) return;

    ll_node* current = ll->head;
    ll_node* next = 0;

    ll->current = ll->head = ll->tail = 0;

    while (current != 0) {
        next = current->next;
        free(current);
        current = next;
    }
}

void* pop_from_linked_list(linked_list* ll) {
    ll_node* node = 0;
    void* item = 0;

    if (ll == 0) return 0;
    if (ll->count == 0) return 0;

    node = ll->head;
    item = ll->head->content;

    if (ll->count == 1) {
        ll->head = ll->tail = 0;
        ll->count = 0;
    } else {
        ll->head = node->next;
        ll->count -= 1;
    }

    free(node);

    return item;
}

void add_to_linked_list(linked_list* ll, void* content) {
    ll_node* node = (ll_node*) malloc(sizeof(node));

    node->content = content;
    node->next = 0;

    if (ll->count == 0) {
        ll->head = node;
        ll->tail = node;
        ll->current = node;
    } else {
        ll->tail->next = node;
        ll->tail = node;
    }

    ll->count += 1;
}

void remove_from_linked_list(linked_list* ll, void* content) {
    ll_node* node_to_remove = 0;
    ll_node* previous = 0;

    if (ll == 0) return;

    switch (ll->count) {
        case 0:
            return;
        case 1:
            if (ll->head->content == content) {
                node_to_remove = ll->head;
                ll->head = ll->tail = 0;
                ll->count = 0;
                ll->current = 0;
                free(node_to_remove);
            }
            return;
        case 2:
            if (ll->head->content == content) {
                node_to_remove = ll->head;
                ll->head = node_to_remove->next;
            } else if (ll->tail->content == content) {
                node_to_remove = ll->tail;
                ll->head = ll->tail;
            } else
                return;

            ll->count = 1;
            ll->current = 0;
            free(node_to_remove);
        default:
            if (ll->head->content == content) {
                node_to_remove = ll->head;

                while (node_to_remove != 0) {
                    if (node_to_remove->content == content) {
                        if (previous == 0) {
                            ll->head = node_to_remove->next;
                        } else if (node_to_remove == ll->tail) {
                            previous->next = 0;
                            ll->tail = previous;
                        } else {
                            previous->next = node_to_remove->next;
                        }

                        ll->count -= 1;
                        ll->current = 0;
                        free(node_to_remove);

                        break;
                    }
                    previous = node_to_remove;
                }
            }
    }
}

void* get_current_from_linked_list(linked_list* ll) {
    if (ll->current == 0) return 0;
    return ll->current->content;
}

void* get_next_from_linked_list(linked_list* ll) {
    void* next = 0;

    if (ll->current == 0) return 0;

    next = ll->current->content;
    ll->current = ll->current->next;

    return next;
}

void reset_current_for_linked_list(linked_list* ll) {
    ll->current = ll->head;
}


// Reading text from input

typedef struct dynamic_buffer_struct {
    int current_size;
    int current_cursor;
    int growth_factor;
    char* text;
} dynamic_buffer;

int create_dynamic_buffer(dynamic_buffer *buff, int initial_size, int growth_factor) {
    char* text = (char*) calloc(initial_size, sizeof(char));

    if (! text) return 0;

    memset(text, '\0', initial_size);

    buff->current_size = initial_size;
    buff->current_cursor = 0;
    buff->growth_factor = growth_factor;
    buff->text = text;

    return 1;
}

void reset_dynamic_buffer(dynamic_buffer *buff) {
    memset(buff->text, '\0', buff->current_size);
    buff->current_cursor = 0;
}

void free_dynamic_buffer_text(dynamic_buffer *buff) {
    if (buff != 0) {
        buff->current_size = 0;
        buff->current_cursor = 0;
        free(buff->text);
        buff->text = 0;
    }
}

char* get_dynamic_buffer_string(dynamic_buffer *buff) {
    return buff->text;
}

int allocate_dynamic_buffer_string_copy(dynamic_buffer *buff, char** text) {
    *text = calloc(buff->current_cursor + 1, sizeof(char));

    if (! *text) return 0;

    strcpy(*text, buff->text);

    return 1;
}

int last_char_in_dynamic_buffer(dynamic_buffer *buff) {
    if ((buff->current_size == 0) || (buff->current_cursor == 0) || (buff->text == 0))
        return -1;
    else
        return *(buff->text + buff->current_cursor - 1);
}

int last_char_in_dynamic_buffer_is(dynamic_buffer *buff, char c) {
    int last_char = last_char_in_dynamic_buffer(buff);

    return (last_char == -1) ? 0 : (last_char == c);
}

int append_to_dynamic_buffer(dynamic_buffer *buff, char c) {
    if (buff->current_cursor >= buff->current_size - 1) {
        fprintf(stderr, "\n  Growing buffer:");
        fprintf(stderr, "\n    old text: \"%s\"", buff->text);
        char* text = (char*) calloc(buff->current_size * buff->growth_factor, sizeof(char));

        if (! text) return 0;

        buff->current_size *= buff->growth_factor;

        memset(text, '\0', buff->current_size);
        strcpy(text, buff->text);

        free(buff->text);
        buff->text = text;

        fprintf(stderr, "\n    new text: \"%s\"", buff->text);
    }

    *(buff->text + buff->current_cursor++) = c;
    *(buff->text + buff->current_cursor) = '\0';

    return 1;
}

dynamic_buffer input_buffer;

int begins_symbol(char ch) {
    return strchr(SYMBOL_CHARS, ch) || isalpha(ch);
}

int continues_symbol(char ch) {
    return begins_symbol(ch) || isdigit(ch);
}

char translate_escaped_c_char(char c) {
    switch (c) {
        case 'a': return '\a';
        case 'b': return '\b';
        case 'f': return '\f';
        case 'n': return '\n';
        case 'r': return '\r';
        case 't': return '\t';
        case 'v': return '\v';
        case '\\': return '\\';
        case '\'': return '\'';
        case '"': return '"';
        case '0': return '\0';
        default: return c;
    }
}

int read_string_text_from_input(FILE *input, char ch, char* error_string) {
    int escaped = 0;

    if (ch != '"') {
        sprintf(error_string, "Expected '\"', but read '%c'.", ch);
        return 0;
    }

    append_to_dynamic_buffer(&input_buffer, ch);

    while ((ch = getc(input)) != EOF) {
        if (ch == '\\') {
            escaped = 1;
        } else if (escaped) {
            append_to_dynamic_buffer(&input_buffer, translate_escaped_c_char(ch));
            escaped = 0;
        } else if (ch == '"') {
            append_to_dynamic_buffer(&input_buffer, ch);
            return 1;
        } else {
            append_to_dynamic_buffer(&input_buffer, ch);
        }
    }

    sprintf(error_string, "End of input encountered while reading a string.");

    return 0;
}

int read_symbol_text_from_input(FILE *input, char ch, char* error_string) {
    if (! begins_symbol(ch)) {
        sprintf(error_string, "Symbols do not begin with '%c'.", ch);
        return 0;
    }

    append_to_dynamic_buffer(&input_buffer, toupper(ch));

    while ((ch = getc(input)) != EOF) {
        if (continues_symbol(ch))
            append_to_dynamic_buffer(&input_buffer, toupper(ch));
        else if (iswspace(ch)) {
            ungetc(ch, input);
            return 1;
        } else if (ch == ')') {
            ungetc(ch, input);
            return 1;
        } else if (ch == '(') {
            ungetc(ch, input);
            return 1;
        } else {
            sprintf(error_string, "Symbols cannot contain '%c'.", ch);
            return 0;
        }
    }

    sprintf(error_string, "End of input encountered while reading a symbol.");

    return 0;
}

int read_integer_text_from_input(FILE *input, char ch, char* error_string) {
    if (! isdigit(ch)) {
        sprintf(error_string, "'%c' is not a digit.", ch);
        return 0;
    }

    append_to_dynamic_buffer(&input_buffer, ch);

    while ((ch = getc(input)) != EOF) {
        if (isdigit(ch))
            append_to_dynamic_buffer(&input_buffer, ch);
        else if (iswspace(ch)) {
            ungetc(ch, input);
            return 1;
        } else if (ch == ')') {
            ungetc(ch, input);
            return 1;
        } else if (ch == '(') {
            ungetc(ch, input);
            return 1;
        } else {
            sprintf(error_string, "Integers cannot contain '%c'.", ch);
            return 0;
        }
    }

    sprintf(error_string, "End of input encountered while reading an integer.");

    return 0;
}

int read_expression_text_from_input(FILE *input, char* error_string) {
    char ch;
    int depth = 0;
    int previous_was_atom = 0;

    while ((ch = getc(input)) != EOF) {
        if (iswspace(ch))
            continue;
        else if (ch == '(') {
            if (previous_was_atom)
                append_to_dynamic_buffer(&input_buffer, ' ');

            previous_was_atom = 0;
            depth += 1;
            append_to_dynamic_buffer(&input_buffer, ch);
        } else if (ch == ')') {
            if (previous_was_atom)
                append_to_dynamic_buffer(&input_buffer, ' ');

            previous_was_atom = 0;
            if (depth == 1) {
                append_to_dynamic_buffer(&input_buffer, ch);
                return 1;
            } else if (depth > 1) {
                depth -= 1;
                append_to_dynamic_buffer(&input_buffer, ch);
            } else {
                sprintf(error_string, "Unmatched '%c'.", ch);
                return 0;
            }
        } else if (ch == '"') {
            if (previous_was_atom || last_char_in_dynamic_buffer_is(&input_buffer, ')'))
                append_to_dynamic_buffer(&input_buffer, ' ');

            previous_was_atom = 1;

            if (! read_string_text_from_input(input, ch, error_string))
                return 0;
            else if (depth == 0)
                return 1;
        } else if (begins_symbol(ch)) {
            if (previous_was_atom || last_char_in_dynamic_buffer_is(&input_buffer, ')') || last_char_in_dynamic_buffer_is(&input_buffer, '('))
                append_to_dynamic_buffer(&input_buffer, ' ');

            previous_was_atom = 1;

            if (! read_symbol_text_from_input(input, ch, error_string))
                return 0;
            else if (depth == 0)
                return 1;
        } else if (isdigit(ch)) {
            if ((previous_was_atom) || last_char_in_dynamic_buffer_is(&input_buffer, ')') || last_char_in_dynamic_buffer_is(&input_buffer, '('))
                append_to_dynamic_buffer(&input_buffer, ' ');

            previous_was_atom = 1;

            if (! read_integer_text_from_input(input, ch, error_string))
                return 0;
            else if (depth == 0)
                return 1;
        } else if (ch == '\'') {
            if (previous_was_atom || last_char_in_dynamic_buffer_is(&input_buffer, ')'))
                append_to_dynamic_buffer(&input_buffer, ' ');

            previous_was_atom = 0;

            append_to_dynamic_buffer(&input_buffer, ch);
        } else if (ch == '.') {
            previous_was_atom = 1;

            append_to_dynamic_buffer(&input_buffer, ' ');
            append_to_dynamic_buffer(&input_buffer, ch);
        }
    }

    sprintf(error_string, "End of input encountered while reading an expression.");

    return 0;
}

// Tokenizer

typedef enum token_type_enum {
    OPEN_PAREN_TOKEN,
    CLOSE_PAREN_TOKEN,
    DOT_TOKEN,
    QUOTE_TOKEN,
    COMMA_TOKEN,
    STRING_TOKEN,
    SYMBOL_TOKEN,
    NUMBER_TOKEN
} token_type;

char* token_type_to_string(token_type type) {
    switch (type) {
        case OPEN_PAREN_TOKEN: return "OPEN_PAREN";
        case CLOSE_PAREN_TOKEN: return "CLOSE_PAREN";
        case DOT_TOKEN: return "DOT";
        case QUOTE_TOKEN: return "QUOTE";
        case COMMA_TOKEN: return "COMA";
        case STRING_TOKEN: return "STRING";
        case SYMBOL_TOKEN: return "SYMBOL";
        case NUMBER_TOKEN: return "NUMBER";
        default: return "UNKOWN";
    }
}

typedef struct lisp_token_struct {
    char* text;
    token_type type;
} lisp_token;

lisp_token* create_lisp_token(token_type type, char* text) {
    // fprintf(stderr, "\n  create_lisp_token(): text=\"%s\"\n", text);
    lisp_token* new_token = (lisp_token*) malloc(sizeof(lisp_token));
    new_token->type = type;
    new_token->text = (char*) calloc(strlen(text) + 1, sizeof(char));
    strcpy(new_token->text, text);
    return new_token;
}

void free_lisp_token(lisp_token* token) {
    free(token->text);
    token->text = 0;
    free(token);
}

char* lisp_token_to_string(lisp_token* token, char* buf) {
    if ((token == 0) || (buf == 0)) return 0;

    sprintf(buf, "%s:[%s]", token_type_to_string(token->type), token->text);

    while (*buf != '\0') buf += 1;

    return buf;
}

void print_all_lisp_tokens(linked_list* linked_list_of_tokens) {
    lisp_token* current = 0;
    int first_pass = 1;
    char* delimiter = "";
    char buf[256];

    reset_current_for_linked_list(linked_list_of_tokens);

    printf("\n[");

    while ((current = (lisp_token*) get_next_from_linked_list(linked_list_of_tokens))) {
        lisp_token_to_string(current, buf);
        printf("%s%s", delimiter, buf);
        if (first_pass) {
            delimiter = ", ";
            first_pass = 0;
        }
    }

    printf("]\n");
}

char* get_next_token_from_text(char* text_buffer, lisp_token** token) {
    int escaped = 0;
    int length = 0;
    char* strptr = 0;
    char* str = 0;

    fprintf(stderr, "\n  get_next_token_from_text()");

    for (char* current = text_buffer; *current != '\0'; current++) {
        fprintf(stderr, "\n    current='%c'", *current);
        if (*current == '(') {
            *token = create_lisp_token(OPEN_PAREN_TOKEN, "(");
            return current + 1;
        } else if (*current == ')') {
            *token = create_lisp_token(CLOSE_PAREN_TOKEN, ")");
            return current + 1;
        } else if (*current == '.') {
            *token = create_lisp_token(DOT_TOKEN, ".");
            return current + 1;
        } else if (*current == '\'') {
            *token = create_lisp_token(QUOTE_TOKEN, "'");
            return current + 1;
        } else if (*current == ',') {
            *token = create_lisp_token(COMMA_TOKEN, ",");
            return current + 1;
        } else if (*current == '"') {
            for (strptr = current + 1; ((! escaped) && (*strptr != '"')); strptr++)
                escaped = (*strptr == '\''); //!!!
            length = (strptr - current) - 1;
            str = (char*) calloc(length + 1, sizeof(char));
            strncpy(str, current + 1, length);
            *(str + length + 1) = '\0';
            *token = create_lisp_token(STRING_TOKEN, str);
            return strptr + 1;
        } else if (begins_symbol(*current)){
            for (strptr = current + 1; ((*strptr != '\0') && (continues_symbol(*strptr))); strptr++);
            length = strptr - current;
            str = (char*) calloc(length + 1, sizeof(char));
            strncpy(str, current, length);
            *(str + length + 1) = '\0';
            *token = create_lisp_token(SYMBOL_TOKEN, str);
            return strptr;
        } else if (isdigit(*current)) {
            for (strptr = current + 1; ((*strptr != '\0') && (isdigit(*strptr))); strptr++);
            length = strptr - current;
            str = (char*) calloc(length + 1, sizeof(char));
            strncpy(str, current, length);
            *(str + length + 1) = '\0';
            *token = create_lisp_token(NUMBER_TOKEN, str);
            return strptr;
        } else if (*current == '\0') {
            return 0;
        } else {
            fprintf(stderr, "\nDoes not begin a token: '%c'.\n", *current);
            return 0;
        }
    }

    return 0;
}

char* move_to_first_non_whitespace_char(char* text_buffer) {
    if (text_buffer != 0) {
        while (*text_buffer != '\0')
            if (! iswspace(*text_buffer))
                return text_buffer;
            else
                text_buffer += 1;
        return (*text_buffer == '\0') ? 0 : text_buffer;
    } else
        return 0;
}

int tokenize_lisp_text(char* text_buffer, linked_list** linked_list_of_tokens, char* error_string) {
    char* current = text_buffer;
    lisp_token* token = 0;

    if (current == 0) return 0;

    *linked_list_of_tokens = create_linked_list();

    while (current != 0) {
        if (*current == '\0') return 1;
        if (! iswspace(*current)) {
            current = get_next_token_from_text(current, &token);
            add_to_linked_list(*linked_list_of_tokens, token);
        } else
            current += 1;
    }

    return 1;
}


// Lispy stuff

#define RESERVED_FLAG 7
#define LISP_VALUE_FLAG 14235

#define SYMBOL_FLAG 0
#define INTEGER_FLAG 1
#define FLOAT_FLAG 2
#define STRING_FLAG 3
#define VECTOR_FLAG 4
#define ARRAY_FLAG 4

typedef struct tag_struct {
    unsigned int inuse : 1;
    unsigned int iscons : 1;
    unsigned int flagifatom : 3;
    unsigned int reserved : 3;
    unsigned int size : 8;
    unsigned int lisp_value_flag;
} tag_type;

typedef struct lisp_value_prototype_struct {
    tag_type tag;
} lisp_value;

void *get_memory_of_size(int size);
void release_memory_of_size(void* memory, int size);

typedef lisp_value* (*compiled_lisp_function)(lisp_value*);

int lisp_value_p(lisp_value* value) {
    if (value == 0) return 0;

    return (value->tag.reserved == RESERVED_FLAG) && (value->tag.lisp_value_flag == LISP_VALUE_FLAG);
}

void create_lisp_value_prototype(lisp_value* prototype) {
    prototype->tag.inuse = 0;
    prototype->tag.iscons = 0;
    prototype->tag.flagifatom = 0;
    prototype->tag.reserved = RESERVED_FLAG;
    prototype->tag.size = 0;
    prototype->tag.lisp_value_flag = LISP_VALUE_FLAG;
}

typedef struct cons_struct cons;

typedef struct cons_struct {
    tag_type tag;
    lisp_value* car;
    lisp_value* cdr;
} cons;

void create_cons(cons* cons_value) {
    create_lisp_value_prototype((lisp_value *) cons_value);
    cons_value->tag.iscons = 1;
    cons_value->tag.size = sizeof(cons);
    cons_value->car = 0;
    cons_value->cdr = 0;
}

void init_cons(cons* cons_value, lisp_value* car, lisp_value* cdr) {
    cons_value->car = car;
    cons_value->cdr = cdr;
}

cons* new_cons_0() {
    cons* new_cons = (cons *) get_memory_of_size((int) sizeof(cons));
    create_cons(new_cons);
    return new_cons;
}

void release_cons(cons* old_cons) {
    release_memory_of_size(old_cons, sizeof(cons));
}

// lisp_value* cons_car()

typedef struct string_struct {
    tag_type tag;
    int length;
    char* chars;
} string;

void create_string(string* string_value) {
    create_lisp_value_prototype((lisp_value *) string_value);
    string_value->tag.flagifatom = STRING_FLAG;
    string_value->tag.size = sizeof(string);
    string_value->length = 0;
    string_value->chars = 0;
}

void init_string(string* string_value, char* text) {
    char count = 0;

    if (text == 0) {
        string_value->length = count;
        string_value->chars = 0;
    } else {
        count = strlen(text);
        string_value->length = count;
        string_value->chars = (char *) calloc(count + 1, sizeof(char));
        strcpy(string_value->chars, text);
    }
}

string* new_string_1(char* name) {
    string* new_string = (string *) get_memory_of_size((int) sizeof(string));
    create_string(new_string);
    init_string(new_string, name);
    return new_string;
}

void release_string(string* old_string) {
    free(old_string->chars);
    old_string->chars = 0;
    old_string->length = 0;
    release_memory_of_size(old_string, sizeof(string));
}

typedef struct symbol_package_struct symbol_package;

typedef struct symbol_struct {
    tag_type tag;
    string* name;
    linked_list* properties;
    lisp_value* value;
    cons* lambda;
    compiled_lisp_function compiled_function;
    symbol_package* package;
} symbol;

void create_symbol(symbol* symbol_value) {
    create_lisp_value_prototype((lisp_value *) symbol_value);
    symbol_value->tag.flagifatom = SYMBOL_FLAG;
    symbol_value->tag.size = sizeof(symbol);
    symbol_value->name = 0;
    symbol_value->properties = 0;
    symbol_value->value = 0;
    symbol_value->lambda = 0;
    symbol_value->compiled_function = 0;
    symbol_value->package = 0;
}

void init_symbol(symbol* symbol_value, string* name) {
    symbol_value->name = name;
}

symbol* new_symbol_0() {
    symbol* new_symbol = (symbol *) get_memory_of_size((int) sizeof(symbol));
    create_symbol(new_symbol);
    return new_symbol;
}

symbol* new_symbol_1(char* name) {
    symbol* new_symbol = (symbol *) get_memory_of_size((int) sizeof(symbol));
    create_symbol(new_symbol);
    new_symbol->name = new_string_1(name);
    return new_symbol;
}

void release_symbol(symbol* old_symbol) {
    if (old_symbol->name != 0) {
        release_string(old_symbol->name);
        old_symbol->name = 0;
    }

    release_memory_of_size(old_symbol, sizeof(symbol));
}

int symbol_to_string(symbol* sym, char* buffer) {
    string* name = 0;

    if (! sym || ! buffer) return 0;

    name = sym->name;

    if (! name) return 0;

    if (! name->chars) return 0;

    strcpy(buffer, name->chars);

    return 1;
}

typedef struct integer_struct {
    tag_type tag;
    long value;
} int_value;

void create_integer(int_value* integer_value) {
    create_lisp_value_prototype((lisp_value *) integer_value);
    integer_value->tag.flagifatom = INTEGER_FLAG;
    integer_value->tag.size = sizeof(int_value);
    integer_value->value = 0;
}

void init_integer(int_value* integer_value, long value) {
    integer_value->value = value;
}

int_value* new_integer_1(long value) {
    int_value* new_int = (int_value *) get_memory_of_size((int) sizeof(int_value));
    create_integer(new_int);
    init_integer(new_int, value);
    return new_int;
}

void release_integer(int_value* old_int) {
    release_memory_of_size(old_int, sizeof(int_value));
}

typedef struct float_struct {
    tag_type tag;
    double value;
} flt_value;

void create_float(flt_value* float_value) {
    create_lisp_value_prototype((lisp_value *) float_value);
    float_value->tag.flagifatom = FLOAT_FLAG;
    float_value->tag.size = sizeof(flt_value);
    float_value->value = 0;
}

void init_float(flt_value* float_value, float value) {
    float_value->value = value;
}

flt_value* new_float_1(double value) {
    flt_value* new_flt = (flt_value *) get_memory_of_size((int) sizeof(flt_value));
    create_float(new_flt);
    init_float(new_flt, value);
    return new_flt;
}

void release_float(flt_value* old_flt) {
    release_memory_of_size(old_flt, sizeof(flt_value));
}

typedef struct vector_struct {
    tag_type tag;
    int length;
    lisp_value** elements;
} vector;

void create_vector(vector* vector_value) {
    create_lisp_value_prototype((lisp_value *) vector_value);
    vector_value->tag.flagifatom = VECTOR_FLAG;
    vector_value->tag.size = sizeof(vector);
    vector_value->length = 0;
    vector_value->elements = 0;
}

void init_vector(vector* vector_value, int size) {
    lisp_value** vec = 0;

    if (size <= 0) {
        vector_value->length = 0;
        vector_value->elements = 0;
    } else {
        vec = (lisp_value **) calloc(size, sizeof(lisp_value *));
        for (int i = 0; i < size; i++) vec[i] = 0;
        vector_value->length = size;
        vector_value->elements = vec;
    }
}

vector* new_vector_1(int size) {
    vector* new_vector = (vector *) get_memory_of_size((int) sizeof(vector));
    create_vector(new_vector);
    init_vector(new_vector, size);
    return new_vector;
}

vector* new_vector_2(int size, lisp_value* initial_value) {
    vector* new_vector = (vector *) get_memory_of_size((int) sizeof(vector));
    create_vector(new_vector);
    init_vector(new_vector, size);

    for (int i = 0; i < size; i++)
        new_vector->elements[i] = initial_value;

    return new_vector;
}

void release_vector(vector* old_vector) {
    if (old_vector->elements) free(old_vector->elements);
    old_vector->elements = 0;
    old_vector->length = 0;
    release_memory_of_size(old_vector, sizeof(vector));
}

typedef struct memory_pool_struct {
    linked_list* available;
    linked_list* allocated;
} memory_pool;

// memory_pool* MEMORY_POOLS[sizeof(symbol)];
memory_pool** MEMORY_POOLS;

void init_memory_pools() {
    fprintf(stderr, "\nsizeof(symbol)=%lu\n", sizeof(symbol));
    MEMORY_POOLS = (memory_pool **) calloc(sizeof(symbol), sizeof(memory_pool *));
    for (int i = 0; (i < sizeof(symbol)) ; i++) {
        MEMORY_POOLS[i] = 0;
    }
}

int memory_pool_available_count(int size) {
    memory_pool* selected = MEMORY_POOLS[size - 1];
    linked_list* list = 0;

    if (selected == 0) return 0;

    list = selected->available;

    if (list == 0) return 0;

    return list->count;
}

int memory_pool_allocated_count(int size) {
    memory_pool* selected = MEMORY_POOLS[size - 1];
    linked_list* list = 0;

    if (selected == 0) return 0;

    list = selected->allocated;

    if (list == 0) return 0;

    return list->count;
}

void dump_memory_pools_info(FILE* output) {
    fprintf(output, "\n%4s %9s %9s%", "SIZE", "AVAILABLE", "ALLOCATED");
    fprintf(output, "\n%4s %9s %9s%", "----", "---------", "---------");

    for (int i = 1; (i <= sizeof(symbol)); i++) {
        fprintf(output, "\n%4d %9d %9d", i, memory_pool_available_count(i), memory_pool_allocated_count(i));
    }
}

void *get_memory_of_size(int size) {
    int index = size - 1;
    void* chunk = 0;

    memory_pool* selected = MEMORY_POOLS[index];

    if (selected != 0) {
        chunk = pop_from_linked_list(selected->available);

        if (chunk == 0) chunk = malloc(size);
    } else {
        selected = (memory_pool *) malloc(sizeof(memory_pool));

        selected->available = create_linked_list();
        selected->allocated = create_linked_list();

        MEMORY_POOLS[index] = selected;
        chunk = malloc(size);
    }

    add_to_linked_list(selected->allocated, chunk);

    return chunk;
}

void release_memory_of_size(void* memory, int size) {
    memory_pool* selected = 0;
    int index = 0;
    lisp_value* value = (lisp_value *) memory;

    if ((memory == 0) || (size <= 0) || (size > (sizeof(symbol)))) return;

    index = size - 1;
    selected = MEMORY_POOLS[index];

    if (selected == 0) {
        selected = (memory_pool *) malloc(sizeof(memory_pool));

        selected->available = create_linked_list();
        selected->allocated = create_linked_list();

        MEMORY_POOLS[index] = selected;
    } else
        remove_from_linked_list(selected->allocated, memory);

    value->tag.flagifatom = 0;
    value->tag.inuse = 0;
    value->tag.iscons = 0;

    add_to_linked_list(selected->available, memory);
}

void test_memory_pools() {
    cons* some_cons_1 = 0;
    cons* some_cons_2 = 0;
    cons* some_cons_3 = 0;
    symbol* some_symbol_1 = 0;
    symbol* some_symbol_2 = 0;
    string* some_string_1 = 0;
    int_value* some_int_1 = 0;
    int_value* some_int_2 = 0;
    flt_value* some_flt_1 = 0;
    flt_value* some_flt_2 = 0;
    vector* some_vector_1 = 0;
    vector* some_vector_2 = 0;

    char buffer[16];

    for (long i = 0; i < 10000000; i++) {
        sprintf(buffer, "string%d", i);
        some_cons_1 = new_cons_0();
        some_cons_2 = new_cons_0();
        some_cons_3 = new_cons_0();
        some_symbol_1 = new_symbol_1(buffer);
        some_symbol_2 = new_symbol_0();
        some_string_1 = new_string_1(buffer);
        some_int_1 = new_integer_1(i);
        some_int_2 = new_integer_1(i);
        some_flt_1 = new_float_1(i);
        some_flt_2 = new_float_1(i);
        some_vector_1 = new_vector_2(10, 0);
        some_vector_2 = new_vector_2(10, 0);

        release_cons(some_cons_1);
        release_cons(some_cons_2);
        release_cons(some_cons_3);
        release_symbol(some_symbol_1);
        release_symbol(some_symbol_2);
        release_string(some_string_1);
        release_integer(some_int_1);
        release_integer(some_int_2);
        release_float(some_flt_1);
        release_float(some_flt_2);
        release_vector(some_vector_1);
        release_vector(some_vector_2);
    }
}

// Lisp Internals

typedef struct binding_struct {
    symbol* sym;
    lisp_value* value;
} binding;

typedef struct env_struct env;

typedef struct env_struct {
    void* bindings;
    env* parent;
} env;

typedef struct activation_frame_struct {
    symbol* calling_function;
    void* args;
    env* environment;
} act_frame;

typedef struct activation_stack {
    unsigned int max_size;
    unsigned int top_index;
    act_frame* stack;
} act_stack;

// Packages

typedef struct symbol_table_node_struct symbol_table_node;

struct symbol_table_node_struct {
    char* key;
    symbol* sym;
    symbol_table_node* less;
    symbol_table_node* greater;
};

typedef struct symbol_package_struct {
    symbol_package *parent;
    char* package_name;
    symbol_table_node* root;
} symbol_package;

void output_tabs(FILE* output, int count) {
    for (int i = 0; i < count; i++) fprintf(output, "\t");
}

void dump_symbol_table_to_file(FILE* output, symbol_table_node* node, int depth) {
    char buffer[512];

    if (! output || ! node) return;

    fprintf(output, "\n");
    output_tabs(output, depth);
    symbol_to_string(node->sym, buffer);
    fprintf(output, "Node: \"%s\", symbol=%s\n", node->key, buffer);

    if (node->less) {
        output_tabs(output, depth);
        fprintf(output, "Less:");
        dump_symbol_table_to_file(output, node->less, depth + 1);
    }

    if (node->greater) {
        output_tabs(output, depth);
        fprintf(output, "Greater:");
        dump_symbol_table_to_file(output, node->greater, depth + 1);
    }
}

void dump_symbol_package_to_file(FILE* output, symbol_package* package) {
    if (! output || ! package) return;

    fprintf(output, "\nPackage Name: \"%s\"", package->package_name);
    dump_symbol_table_to_file(output, package->root, 1);
}

symbol_package* create_symbol_package(char* name, symbol_package* parent) {
    symbol_package* new_package = (symbol_package *) malloc(sizeof(symbol_package));
    new_package->parent = parent;

    if (name != 0) {
        new_package->package_name = (char *) calloc(strlen(name) + 1, sizeof(char));
        strcpy(new_package->package_name, name);
    } else {
        new_package->package_name = 0;
    }

    new_package->root = 0;

    return new_package;
}

symbol* find_symbol_with_name_in_package_tree(char* name, symbol_table_node* node, symbol_table_node** continuation, int* left_is_zero) {
    int comparison = strcmp(name, node->key);

    if (comparison == 0)
        return node->sym;
    else if (comparison < 0) {
        if (node->less != 0)
            return find_symbol_with_name_in_package_tree(name, node->less, continuation, left_is_zero);
        else {
            if (continuation != 0) {
                *left_is_zero = 0;
                *continuation = node;
            }
        }
    } else {
        if (node->greater != 0)
            return find_symbol_with_name_in_package_tree(name, node->greater, continuation, left_is_zero);
        else {
            if (continuation != 0) {
                *left_is_zero = 1;
                *continuation = node;
            }
        }
    }

    return 0;
}

symbol* find_symbol_with_name_in_package(char* name, symbol_package* package) {
    int dummy = 0;
    if (package == 0) return 0;
    if (name == 0) return 0;
    if (package->root == 0) return 0;

    return find_symbol_with_name_in_package_tree(name, package->root, 0, &dummy);
}

void intern_symbol_in_package_tree(symbol* sym, char* name, symbol_table_node* node) {
    int comparison = strcmp(name, node->key);

    if (comparison == 0) {
        node->sym = sym;
    } else if (comparison < 0) {
        node->less = (symbol_table_node *) malloc(sizeof(symbol_table_node));
        node->less->key = (char *) calloc(strlen(name) + 1, sizeof(char));
        strcpy(node->less->key, name);
        node->less->less = node->less->greater = 0;
        node->less->sym = sym;
    } else {
        node->greater = (symbol_table_node *) malloc(sizeof(symbol_table_node));
        node->greater->key = (char *) calloc(strlen(name) + 1, sizeof(char));
        strcpy(node->greater->key, name);
        node->greater->less = node->less->greater = 0;
        node->greater->sym = sym;
    }
}

symbol* intern_symbol_with_name_in_package(char* name, symbol_package* package) {
    symbol* sym = 0;
    symbol_table_node* continuation = 0;
    int left_is_zero = 0;

    if (package == 0) return 0;
    if (name == 0) return 0;

    if (package->root == 0) {
        package->root = (symbol_table_node *) malloc(sizeof(symbol_table_node));
        package->root->key = (char *) calloc(strlen(name) + 1, sizeof(char));
        strcpy(package->root->key, name);
        package->root->less = package->root->greater = 0;
        package->root->sym = new_symbol_1(name);
        package->root->sym->package = package;
        return package->root->sym;
    } else if (sym = find_symbol_with_name_in_package_tree(name, package->root, &continuation, &left_is_zero)) { // This is an assignment, not a comparison.
        return sym;
    } else {
        sym = new_symbol_1(name);
        if (left_is_zero == 0) {
            continuation->less = (symbol_table_node *) malloc(sizeof(symbol_table_node));
            continuation->less->key = (char *) calloc(strlen(name) + 1, sizeof(char));
            strcpy(continuation->less->key, name);
            continuation->less->less = continuation->less->greater = 0;
            continuation->less->sym = new_symbol_1(name);
            continuation->less->sym->package = package;
        } else {
            continuation->greater = (symbol_table_node *) malloc(sizeof(symbol_table_node));
            continuation->greater->key = (char *) calloc(strlen(name) + 1, sizeof(char));
            strcpy(continuation->greater->key, name);
            continuation->greater->less = continuation->greater->greater = 0;
            continuation->greater->sym = new_symbol_1(name);
            continuation->greater->sym->package = package;
        }

        return sym;
    }
}

void test_symbol_packages() {
    char buffer[512];
    symbol* sym = 0;
    char *names[] = {"NIL", "T", "COND", "PROGN", "AND", "OR", "NOT", "IF", "CONS", "CAR", "CDR", "LIST", "+", "-", "*", "/", "="};

    symbol_package* user = create_symbol_package("USER", 0);

    for (int i = 0; i < 17; i++) {
        sym = intern_symbol_with_name_in_package(names[i], user);

        if (symbol_to_string(sym, buffer)) {
            fprintf(stderr, "\n\"%s\" = %s\n", names[i], buffer);
        } else {
            fprintf(stderr, "\nError printing %s.\n", names[i]);
        }
    }

    dump_symbol_package_to_file(stderr, user);
}

int main()
{
    char error_message[512];
    char* expression_text = 0;
    char ch;
    int quit = 0;

    init_memory_pools();

    // test_memory_pools();
    // dump_memory_pools_info(stderr);

    create_dynamic_buffer(&input_buffer, 8, 2);
    linked_list* token_list = 0;

    /*
    append_to_dynamic_buffer(&input_buffer, '!');
    allocate_dynamic_buffer_string_copy(&input_buffer, &expression_text);
    printf("\n%s\n", expression_text);
    */

    test_symbol_packages();

    printf("* ");

    while ((ch = getc(stdin)) != EOF) {
        ungetc(ch, stdin);
        if (! read_expression_text_from_input(stdin, error_message)) {
            printf("\n  Error: %s", error_message);
        } else {
            if (allocate_dynamic_buffer_string_copy(&input_buffer, &expression_text)) {

                printf("\n%s\n", expression_text);

                tokenize_lisp_text(expression_text, &token_list, error_message);
                print_all_lisp_tokens(token_list);

                if (strcmp(expression_text, "quit") == 0)
                    quit = 1;

                free(expression_text);
                free(token_list);

                if (quit) return 0;
            } else {
                printf("\nNothing read.\n");
            }
        }
        reset_dynamic_buffer(&input_buffer);
        printf("* ");
    }

    return 0;
}
