#include <stdio.h>
#include <ctype.h>
#include "mstring.h"

#define DEFAULT_BYTES 16
#define GROWTH_FACTOR 2

char* empty = "";

struct m_string {
    int _bytes;
    int _len;
    char *_cstr;
};
typedef struct m_string M_STR;

void print_string_details(M_STR *m_str) {
    printf("\n[_bytes=%d, _len=%d, _cstr=%s]\n", m_str->_bytes, m_str->_len, m_str->_cstr);
}

M_STR *create_m_str(char *str) {
    M_STR* new_str = (M_STR*) malloc(sizeof(M_STR));
    if (str == 0) {
        new_str->_bytes = 0;
        new_str->_len = 0;
        new_str->_cstr = 0;
    } else {
        int len = strlen(str);
        new_str->_bytes = len + 1;
        new_str->_len = new_str->_bytes;
        new_str->_cstr = (char*) calloc(new_str->_bytes, sizeof(char));
        strcpy(new_str->_cstr, str);
    }
    return new_str;
}

void free_m_str(M_STR *m_str) {
    if (m_str == 0) return;
    if (m_str->_cstr != 0) free(m_str->_cstr);
    m_str->_cstr = 0;
    free(m_str);
}

char *m_cstr(M_STR *m_str) {
    if (m_str == 0) return empty;
    else if (m_str->_bytes == 0) return empty;
    else return m_str->_cstr;
}

int m_len(M_STR *m_str) {
    if (m_str == 0) return -1;
    return m_str->_len - 1;
}

M_STR *m_clone(M_STR *m_str) {
    if (m_str == 0) return create_m_str(0);
    return create_m_str(m_str->_cstr);
}

M_STR *m_reset_to_cstr(M_STR *m_str, char *str) {
    if (m_str == 0) return 0;
    if (str == 0) {
        if (m_str->_cstr != 0) free(m_str->_cstr);
        m_str->_bytes = m_str->_len = 0;
        m_str->_cstr = 0;
    } else if (m_str->_bytes == 0) {
        m_str->_bytes = m_str->_len = strlen(str) + 1;
        m_str->_cstr = (char*) calloc(m_str->_len, sizeof(char));
        strcpy(m_str->_cstr, str);
    } else {
        int len = strlen(str);
        if (len + 1 < m_str->_bytes) {
            strcpy(m_str->_cstr, str);
            m_str->_len = len + 1;
        } else if (m_str->_bytes == 0) {
            m_str->_bytes = len + 1;
            m_str->_len = m_str->_bytes;
            m_str->_cstr = (char*) calloc(m_str->_bytes, sizeof(char));
            strcpy(m_str->_cstr, str);
        } else {
            m_str->_bytes = len * GROWTH_FACTOR;
            m_str->_len += len;
            m_str->_cstr = (char*) realloc(m_str->_cstr, m_str->_bytes * sizeof(char));
        }
    }
    return m_str;
}

M_STR *m_reset(M_STR *destin, M_STR *source) {
    if (destin == 0) return 0;
    if (source == 0) return m_reset_to_cstr(destin, 0);
    return m_reset_to_cstr(destin, source->_cstr);
}

int m_copy(M_STR *destin, M_STR *source) {
    if (destin == 0) return 0;
    if (source == 0)
        m_reset_to_cstr(destin, "");
    else
        m_reset_to_cstr(destin, source->_cstr);
    return 1;
}

M_STR *m_extend_by_cstr(M_STR *m_str, char *str) {
    if (m_str == 0) return 0;
    if (str == 0) return m_str;
    if (m_str->_bytes == 0) {
        return m_reset_to_cstr(m_str, str);
    } else {
        int len = strlen(str);
        if (m_str->_bytes > m_str->_len + len) {
            strcpy(m_str->_cstr + m_str->_len - 1, str);
            m_str->_len += len;
        } else {
            m_str->_bytes = (m_str->_len + len) * GROWTH_FACTOR;
            if (m_str->_bytes < DEFAULT_BYTES)
                m_str->_bytes = DEFAULT_BYTES;
            m_str->_cstr = (char*) realloc(m_str->_cstr, m_str->_bytes * sizeof(char));
            strcpy(m_str->_cstr + m_str->_len - 1, str);
            m_str->_len += len;
        }
        return m_str;
    }
}

M_STR *m_extend(M_STR *m_str, M_STR *source) {
    if (m_str == 0) return 0;
    if (source == 0) return m_str;
    if (source->_cstr == 0) return m_str;
    return m_extend_by_cstr(m_str, source->_cstr);
}

M_STR *m_push(M_STR *m_str, char c) {
    if (m_str == 0) return 0;
    if (m_str->_bytes == 0) {
        m_str->_bytes = DEFAULT_BYTES;
        m_str->_len = 2;
        m_str->_cstr = (char*) calloc(DEFAULT_BYTES, sizeof(char));
        m_str->_cstr[0] = c;
        m_str->_cstr[1] = '\0';
    } else if (m_str->_len + 1 < m_str->_bytes) {
        m_str->_len += 1;
        m_str->_cstr[m_str->_len - 2] = c;
        m_str->_cstr[m_str->_len - 1] = '\0';
    } else {
        if (m_str->_bytes < DEFAULT_BYTES)
            m_str->_bytes = DEFAULT_BYTES;
        else
            m_str->_bytes *= GROWTH_FACTOR;
        m_str->_cstr = (char*) realloc(m_str->_cstr, m_str->_bytes * sizeof(char));
        m_str->_len += 1;
        m_str->_cstr[m_str->_len - 2] = c;
        m_str->_cstr[m_str->_len - 1] = '\0';
    }
    return m_str;
}

int m_pop(M_STR *m_str) {
    if (m_str == 0) return -1;
    if (m_str->_len < 1) return -1;
    int val = m_str->_cstr[m_str->_len - 2];
    m_str->_cstr[m_str->_len - 1] = '\0';
    m_str->_len -= 1;
    return val;
}

int m_set(M_STR *m_str, int i, char c) {
    if (m_str == 0) return -1;
    if ((m_str->_len == 0) || (i < 0) || (i >= m_str->_len - 1)) {
        fprintf(stderr, "m_set: Index out of bounds.");
        return -1;
    }
    m_str->_cstr[i] = c;
    return c;
}

int m_get(M_STR *m_str, int i) {
    if (m_str == 0) return -1;
    if ((m_str->_len == 0) || (i < 0) || (i >= m_str->_len - 1)) {
        fprintf(stderr, "m_get: Index out of bounds.");
        return -1;
    }
    return m_str->_cstr[i];
}

int m_insert(M_STR *m_str, int i, char c) {
    int j;
    if (m_str == 0) return -1;
    if ((m_str->_len == 0) || (i < 0) || (i >= m_str->_len - 1)) {
        fprintf(stderr, "m_insert: Index out of bounds.");
        return -1;
    }
    if (m_str->_len == m_str->_bytes) {
        int j;
        if (m_str->_bytes < DEFAULT_BYTES)
            m_str->_bytes = DEFAULT_BYTES;
        else
            m_str->_bytes *= GROWTH_FACTOR;
        m_str->_len += 1;
        m_str->_cstr = (char*) realloc(m_str->_cstr, m_str->_bytes * sizeof(char));
    } else
        m_str->_len += 1;
    for (j = m_str->_len + 1; j > i; j--)
        m_str->_cstr[j] = m_str->_cstr[j - 1];
    m_str->_cstr[i] = c;
    return c;
}

int m_delete(M_STR *m_str, int i) {
    if (m_str == 0) return -1;
    if ((m_str->_len == 0) || (i < 0) || (i >= m_str->_len - 1)) {
        fprintf(stderr, "m_delete: Index out of bounds.");
        return -1;
    } else {
        int j;
        int c = m_str->_cstr[i];
        for (j = i; j < m_str->_len - 1; j++)
            m_str->_cstr[j] = m_str->_cstr[j + 1];
        m_str->_len -= 1;
        m_str->_cstr[m_str->_len] = '\0';
        return c;
    }
}

int m_index_of_using_cstr(M_STR *m_str, char *str) {
    if (m_str == 0) return -1;
    if (str == 0) return -1;
    if (m_str->_len == 0) return -1;
    int i, len = strlen(str);
    for (i = 0; i < m_str->_len - 1; i++)
        if (strncmp(str, m_str->_cstr + i, len) == 0) return i;
    return -1;
}

int m_index_of(M_STR *m_str, M_STR *target) {
    if (target == 0) return -1;
    if (target->_len == 0) return -1;
    return m_index_of_using_cstr(m_str, target->_cstr);
}

int m_replace_using_cstr(M_STR *m_str, char *str1, char *str2) {
    if (str2 == 0) str2 = "";
    int index = m_index_of_using_cstr(m_str, str1);
    if (index == -1) return 0;
    int str1len = strlen(str1);
    int str2len = strlen(str2);
    int delta = (str1len - str2len);
    int m_str_len = m_str->_len;
    int new_len = m_str_len + str2len;
    int i, j;
    if (new_len > m_str->_bytes) {
        m_str->_bytes = new_len;
        m_str->_cstr = (char*) realloc(m_str->_cstr, m_str->_bytes * sizeof(char));
    }
    if (delta == 0) {
        for (i = index, j = 0; i < index + str1len; i++, j++)
            m_str->_cstr[i] = str2[j];
    } else if (delta < 0) {
        // Shift by -delta
        int limit = (index + str1len) + delta;
        for (i = m_str->_len - delta; i >= limit; i--)
            m_str->_cstr[i] = m_str->_cstr[i + delta];
        for (i = index, j = 0; i < index + str2len; i++, j++)
            m_str->_cstr[i] = str2[j];
    } else {
        for (i = index + str1len - delta; i < m_str->_len; i++)
            m_str->_cstr[i] =  m_str->_cstr[i + delta];
        for (i = index, j = 0; i < index + str2len; i++, j++)
            m_str->_cstr[i] = str2[j];
            
    }
    m_str->_len = new_len;
    m_str->_cstr[new_len - 1] = '\0';
    return 1;
}

int m_replace(M_STR *m_str, M_STR *str1, M_STR *str2) {
    if (str1 == 0) return 0;
    return m_replace_using_cstr(m_str, str1->_cstr, (str2 == 0) ? 0 : str2->_cstr);
}

M_STR *m_reverse(M_STR *m_str) {
    if (m_str == 0) return 0;
    int len = m_str->_len - 1;
    if (len <= 1) return m_str;
    int i, j;
    char *str = m_str->_cstr, temp;
    for (i = 0, j = len - 1; (j > i); i++, j--) {
        temp = str[i];
        str[i] = str[j];
        str[j] = temp;
    }
    return m_str;
}

M_STR *m_toupper(M_STR *m_str) {
    if (m_str == 0) return 0;
    if (m_str->_len <= 1) return m_str;
    int len = m_str->_len - 1, i;
    char *str = m_str->_cstr;
    for (i = 0; i < len; i++) str[i] = toupper(str[i]);
    return m_str;
}

M_STR *m_tolower(M_STR *m_str) {
    if (m_str == 0) return 0;
    if (m_str->_len <= 1) return m_str;
    int len = m_str->_len - 1, i;
    char *str = m_str->_cstr;
    for (i = 0; i < len; i++) str[i] = tolower(str[i]);
    return m_str;
}

int m_toint(M_STR *m_str) {
    if (m_str == 0) return 0;
    if (m_str->_len <= 1) return 0;
    return atoi(m_str->_cstr);
}

float m_tofloat(M_STR *m_str) {
    if (m_str == 0) return 0.0;
    if (m_str->_len <= 1) return 0.0;
    return atof(m_str->_cstr);
}

M_STR** m_split(M_STR *m_str, char *del, M_STR** dest, int dest_len, int *count) {
    if ((m_str == 0) || (del == 0) || (dest == 0) || (count == 0)) {
        *count = 0;
        return dest;
    }
    if (m_str->_len <= 1) {
        *count = 0;
        return dest;
    }
    M_STR *current_string = 0;
    char *source = m_str->_cstr;
    char *buffer = (char*) calloc(m_str->_len, sizeof(char));
    strcpy(buffer, source);
    char *token = strtok(buffer, del);
    *count = 0;
    while ((token != NULL) && (*count < dest_len)) {
        current_string = create_m_str(token);
        dest[(*count)++] = current_string;
        token = strtok(NULL, del);
    }
    free(buffer);
    return dest;
}

void free_array_of_m_str(M_STR** array, int count) {
    int i;
    for (i = 0; i < count; i++) {
        if (array[i] != 0) {
            free_m_str(array[i]);
            array[i] = 0;
        }
    }
}

int m_compare(M_STR* s1, M_STR* s2) {
    if ((s1 == 0) || (s2 == 0)) return 0;
    return strcmp(m_cstr(s1), m_cstr(s2));
}

int m_compare_ignore_case(M_STR* s1, M_STR* s2) {
    char *cstr1 = m_cstr(s1);
    char *cstr2 = m_cstr(s2);
    char c1, c2;
    int diff;
    for (c1 = *cstr1, c2 = *cstr2; (c1 != '\0') && (c2 != '\0'); c1 = *++cstr1, c2 = *++cstr2) {
        c1 = toupper(c1);
        c2 = toupper(c2);
        diff = c1 - c2;
        if (diff < 0) return -1;
        if (diff > 0) return 1;
    }
    if (*cstr1 == *cstr2) return 0;
    return *cstr1 - *cstr2;
}

int main() {
    char buffer[1024];
    M_STR *s1, *s2;
    M_STR *tokens[5];
    int i, count;
    s1 = create_m_str("Fleas: Adam, Had 'em" );
    m_split(s1, " ", tokens, 5, &count);
    for (i = 0; i < count; i++) {
        printf("\n%s\n", m_cstr(tokens[i]));
    }
    s2 = create_m_str("");
    for (i = 0; i < 260; i++) {
        m_extend_by_cstr(s2, "ABC_");
        printf("\n%s\n", m_cstr(s2));
    }
    return 0;
}
