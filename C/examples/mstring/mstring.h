#ifndef MSTRING_HEADER
#define MSTRING_HEADER
#include <stdlib.h>
#include <string.h>

struct m_string;
typedef struct m_string M_STR;
M_STR *create_m_str(char *str);
void free_m_str(M_STR *m_str);
char *m_cstr(M_STR *m_str);
int m_len(M_STR *m_str);
M_STR *m_clone(M_STR *m_str);
int m_compare(M_STR* s1, M_STR* s2);
int m_compare_ignore_case(M_STR* s1, M_STR* s2);
M_STR *m_push(M_STR *m_str, char c);
int m_pop(M_STR *m_str);
int m_set(M_STR *m_str, int i, char c);
int m_get(M_STR *m_str, int i);
int m_insert(M_STR *m_str, int i, char c);
int m_delete(M_STR *m_str, int i);
int m_index_of(M_STR *m_str, M_STR *target);
M_STR *m_extend(M_STR *m_str, M_STR *source);
M_STR** m_split(M_STR *m_str, char *del, M_STR** dest, int dest_len, int *count);
void free_array_of_m_str(M_STR** array, int count);
M_STR *m_reset_to_cstr(M_STR *m_str, char *str);
#endif
