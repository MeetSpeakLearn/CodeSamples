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
M_STR *create_m_str(char *str) {
    /* allocate an mstring */
    M_STR* new_str = (M_STR*) malloc(sizeof(M_STR));
    if (str == 0) {
        /* the caller provided 0 as an initializer, so set all fields to 0 */
        new_str->_bytes = 0;
        new_str->_len = 0;
        new_str->_cstr = 0;
    } else {
        /* the caller provided a C string as an initializer.
             get the length of the c string, set _bytes to one more (for null char),
             set _len to _bytes (same size), allocated a character array for _cstr,
             and copy str into _cstr */
        int len = strlen(str);
        new_str->_bytes = len + 1;
        new_str->_len = new_str->_bytes;
        new_str->_cstr = (char*) calloc(new_str->_bytes, sizeof(char));
        strcpy(new_str->_cstr, str);
    }
    /* return the initialized mstring */
    return new_str;
}
void free_m_str(M_STR *m_str) {
    if (m_str == 0) return; /* nothing to free */
    /* if there is a _cstr to free, free it */
    if (m_str->_cstr != 0) free(m_str->_cstr);
    m_str->_cstr = 0;
    free(m_str); /* free the structure */
}
char *m_cstr(M_STR *m_str) {
    if (m_str == 0) return empty; /* return the empty string */
    else if (m_str->_bytes == 0) return empty; /* return the empty string */
    else return m_str->_cstr; /* return the C string */
}
int m_len(M_STR *m_str) {
    if (m_str == 0) return -1; /* uninitialized, return -1 */
    return m_str->_len - 1; /* length is 1 less than the full string */
}
M_STR *m_clone(M_STR *m_str) {
    /* if m_str is null, return an unitialized mstring */
    if (m_str == 0) return create_m_str(0);
    /* create a new mstring initialized to the old mstring's _cstr */
    return create_m_str(m_str->_cstr);
}
int m_compare(M_STR* s1, M_STR* s2) {
    if ((s1 == 0) || (s2 == 0)) return 0; /* if either is null, return 0 */
    /* return strcmp of the two mstring's C strings.
         this will work even if one or more mstrings are uninitialized
         because m_cstr of an uninitialized mstring returns
         an empty C string */
    return strcmp(m_cstr(s1), m_cstr(s2));
}
int m_compare_ignore_case(M_STR* s1, M_STR* s2) {
    /* get the C strings of both mstrings. */
    char *cstr1 = m_cstr(s1);
    char *cstr2 = m_cstr(s2);
    char c1, c2; /* current char of each C string */
    int diff; /* difference of c1 and c2 */
    for (c1 = *cstr1, c2 = *cstr2; /* initialize the current chars */
            (c1 != '\0') && (c2 != '\0'); /* so long as both are not '\0' */
            c1 = *++cstr1, c2 = *++cstr2) { /* at the end of each iteration, update current chars */
        /* convert both current chars to the same case */
        c1 = toupper(c1);
        c2 = toupper(c2);
        diff = c1 - c2; /* compute the difference */
        if (diff < 0) return -1; /* if c1 is less than c2 return -1 */
        if (diff > 0) return 1; /* if c2 is less than c1 return 1 */
    }
    /* either *cstr1, *cstr2, or both are '\0'; */
    /* if they are equal, both strings are equal until the end. */
    if (*cstr1 == *cstr2) return 0; /* return 0 for equal */
    /* if they are not equal, return their difference */
    return *cstr1 - *cstr2;
}
M_STR *m_push(M_STR *m_str, char c) {
    if (m_str == 0) return 0; /* nothing to push onto */
    if (m_str->_bytes == 0) {
        /* m_str is uninitialized, so allocate the default number of bytes */
        m_str->_bytes = DEFAULT_BYTES;
        m_str->_len = 2; /* first byte for c and second for '\0'. */
        m_str->_cstr = (char*) calloc(DEFAULT_BYTES, sizeof(char));
        m_str->_cstr[0] = c;
        m_str->_cstr[1] = '\0';
    } else if (m_str->_len + 1 < m_str->_bytes) {
        /* if enough bytes have already been allocated, just increase the
             length by 1, replace the former '\0' with c, and add '\0' to end. */
        m_str->_len += 1;
        m_str->_cstr[m_str->_len - 2] = c;
        m_str->_cstr[m_str->_len - 1] = '\0';
    } else {
        /* there are not enough preallocated bytes to add a char to the end */
        if (m_str->_bytes < DEFAULT_BYTES)
            /* if we're under the default number of bytes, set the number of bytes
               to the default */
            m_str->_bytes = DEFAULT_BYTES;
        else
            /* if we're at or above the default number of bytes,
               grow the preallocated storage */
            m_str->_bytes *= GROWTH_FACTOR;
        /* reallocate storage to fit the new size */
        m_str->_cstr = (char*) realloc(m_str->_cstr, m_str->_bytes * sizeof(char));
        /* increase the length by 1, replace the former end of string
           null char with c, and end the extended string with a null char */
        m_str->_len += 1;
        m_str->_cstr[m_str->_len - 2] = c;
        m_str->_cstr[m_str->_len - 1] = '\0';
    }
    return m_str; /* return the m_str */
}
int m_pop(M_STR *m_str) {
    if (m_str == 0) return -1; /* no mstring */
    if (m_str->_len < 1) return -1; /* empty mstring */
    /* set val to the char being popped */
    int val = m_str->_cstr[m_str->_len - 2];
    /* replace the popped char with '\0' */
    m_str->_cstr[m_str->_len - 1] = '\0';
    /* decrement the size of the string */
    m_str->_len -= 1;
    /* return the popped char */
    return val;
}
int m_set(M_STR *m_str, int i, char c) {
    if (m_str == 0) return -1; /* nothing to set */
    if ((m_str->_len == 0) || (i < 0) || (i >= m_str->_len - 1)) {
        /* index out of bounds - complain and return -1 */
        fprintf(stderr, "m_set: Index out of bounds.");
        return -1;
    }
    /* replace the character at i with c */
    m_str->_cstr[i] = c;
    return c; /* return c */
}
int m_get(M_STR *m_str, int i) {
    if (m_str == 0) return -1; /* nothing to get */
    if ((m_str->_len == 0) || (i < 0) || (i >= m_str->_len - 1)) {
        /* index out of bounds - complain and return -1 */
        fprintf(stderr, "m_get: Index out of bounds.");
        return -1;
    }
    return m_str->_cstr[i]; /* return the char at i */
}
int m_insert(M_STR *m_str, int i, char c) {
    int j;
    if (m_str == 0) return -1; /* no where to insert into */
    if ((m_str->_len == 0) || (i < 0) || (i >= m_str->_len - 1)) {
        /* index out of bounds - complain and return -1 */
        fprintf(stderr, "m_insert: Index out of bounds.");
        return -1;
    }
    if (m_str->_len == m_str->_bytes) {
        /* all of the bytes are currently used, so grow _cstr */
        int j;
        if (m_str->_bytes < DEFAULT_BYTES)
            m_str->_bytes = DEFAULT_BYTES;
        else
            m_str->_bytes *= GROWTH_FACTOR;
        m_str->_len += 1; /* new length */
        m_str->_cstr = (char*) realloc(m_str->_cstr, m_str->_bytes * sizeof(char));
    } else
        m_str->_len += 1; /* new length */
    /* shift all chars from i upward to make space */
    for (j = m_str->_len + 1; j > i; j--)
        m_str->_cstr[j] = m_str->_cstr[j - 1];
    /* set location i to c */
    m_str->_cstr[i] = c;
    /* return c */
    return c;
}
int m_delete(M_STR *m_str, int i) {
    if (m_str == 0) return -1; /* nowhere to delete from */
    if ((m_str->_len == 0) || (i < 0) || (i >= m_str->_len - 1)) {
        /* index out of bounds - complain and return -1 */
        fprintf(stderr, "m_delete: Index out of bounds.");
        return -1;
    } else {
        int j;
        int c = m_str->_cstr[i]; /* save the char to be deleted */
        /* shift all characters from i until the end leftward by 1 */
        for (j = i; j < m_str->_len - 1; j++)
            m_str->_cstr[j] = m_str->_cstr[j + 1];
        /* decrement the length */
        m_str->_len -= 1;
        /* ensure termination with '\0';
        m_str->_cstr[m_str->_len] = '\0';
        return c; /* return deleted char */
    }
}
M_STR *m_extend_by_cstr(M_STR *m_str, char *str) {
    /* This is a helper function that does most of the work for m_extend.
       However, it may be called by anyone that imports mstring.h.
       Some users may wish to extend by using a C string. */
    if (m_str == 0) return 0; /* nothing to extend */
    if (str == 0) return m_str; /* nothing to extend by */
    if (m_str->_bytes == 0) {
        /* m_str is uninitialized and effectively empty,
           so just reset it with str */
        return m_reset_to_cstr(m_str, str);
    } else {
        int len = strlen(str); /* length of extension */
        if (m_str->_bytes > m_str->_len + len) {
            /* there are enough bytes to extend by len without reallocating */
            strcpy(m_str->_cstr + m_str->_len - 1, str); /* copy to end of _cstr */
            m_str->_len += len; /* increase _len to accommodate */
        } else {
            /* we need to reallocate a larger block of memory to fitthe extension */
            /* try growing naturally */
            m_str->_bytes = (m_str->_len + len) * GROWTH_FACTOR;
            if (m_str->_bytes < DEFAULT_BYTES)
                /* if natural growth is less than the default, use the default */
                m_str->_bytes = DEFAULT_BYTES;
            /* resize */
            m_str->_cstr
                = (char*) realloc(m_str->_cstr, m_str->_bytes * sizeof(char));
            /* copy extension to end of string */
            strcpy(m_str->_cstr + m_str->_len - 1, str);
            m_str->_len += len; /* adjust length */
        }
        return m_str; /* return the m_str */
    }
}
M_STR *m_extend(M_STR *m_str, M_STR *source) {
    if (m_str == 0) return 0; /* nothing to extend */
    if (source == 0) return m_str; /* nothing to extend by */
    if (source->_cstr == 0) return m_str; /* uninitialized */
    /* call helper function */
    return m_extend_by_cstr(m_str, source->_cstr);
}
int m_index_of_using_cstr(M_STR *m_str, char *str) {
    /* helper function and lighter version of m_index_of */
    if (m_str == 0) return -1; /* nothing to search */
    if (str == 0) return -1; /* nothing to search for */
    if (m_str->_len == 0) return -1; /* nothing to search */
    int i, len = strlen(str);
    for (i = 0; i < m_str->_len - 1; i++)
        /* step through each position, comparing str
           to the substring of _cstr with the same length */
        if (strncmp(str, m_str->_cstr + i, len) == 0)
           return i; /* found, return index */
    return -1; /* not found */
}
int m_index_of(M_STR *m_str, M_STR *target) {
    if (target == 0) return -1; /* nothing to search for */
    if (target->_len == 0) return -1; /* nothing to search for */
    return m_index_of_using_cstr(m_str, target->_cstr);
}
M_STR** m_split(M_STR *m_str, char *del, M_STR** dest, int dest_len, int *count) {
    if ((m_str == 0) || (del == 0) || (dest == 0) || (count == 0)) {
        /* can't do anything if there is no m_str to split, no delimiter,
             no destination, or no place to store the token count, so return */
        *count = 0; /* set count to 0 */
        return dest;
    }
    if (m_str->_len <= 1) {
        /* m_str is empty, so return */
        *count = 0;
        return dest;
    }
    M_STR *current_string = 0; /* current token plucked from m_str */
    char *source = m_str->_cstr; /* the C string to split */
    /* we will use strtok. strtok is destructive, so we will apply
         it to a copy of our string saved in a temporary buffer */
    char *buffer = (char*) calloc(m_str->_len, sizeof(char));
    strcpy(buffer, source);
    char *token = strtok(buffer, del); /* init strtok and get first token */
    *count = 0; /* initialize count */
    /* while there are more tokens and we haven't run
         out of places to store them… */
    while ((token != NULL) && (*count < dest_len)) {
        current_string = create_m_str(token); /* create an mstring for the token */
        dest[(*count)++] = current_string; /* store the mstring while incrementing counter */
        token = strtok(NULL, del); /* get the next token */
    }
    free(buffer); /* free the temporary buffer */
    return dest; /* return the array of tokens */
}
void free_array_of_m_str(M_STR** array, int count) {
    int i;
    for (i = 0; i < count; i++) {
        /* for every element of the array at indices 0 through,
             but not including, count, if the element is not null,
             free the element and set it to null. */
        if (array[i] != 0) {
            free_m_str(array[i]);
            array[i] = 0;
        }
    }
}
M_STR *m_reset_to_cstr(M_STR *m_str, char *str) {
    if (m_str == 0) return 0; /* nothing to reset */
    if (str == 0) {
        /* reset to uninitialized */
        if (m_str->_cstr != 0) free(m_str->_cstr);
        m_str->_bytes = m_str->_len = 0;
        m_str->_cstr = 0;
    } else if (m_str->_bytes == 0) {
        /* m_str is uninitialized, so initialize it */
        m_str->_bytes = m_str->_len = strlen(str) + 1;
        m_str->_cstr = (char*) calloc(m_str->_len, sizeof(char));
        strcpy(m_str->_cstr, str);
    } else {
        /* resize and reset */
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
            m_str->_cstr =
                (char*) realloc(m_str->_cstr, m_str->_bytes * sizeof(char));
        }
    }
    return m_str; /* return the mstring */
}
#define BUFFER_SIZE 1024
#define PUNCTUATION_LEN 8
int main() {
    char input_buffer[BUFFER_SIZE];
    char* punctuation[PUNCTUATION_LEN] = {".", ",", "'", "\"", ":", ";", "?", "!"};
    M_STR* punctuation_mstrings[PUNCTUATION_LEN];
    M_STR* token_buffer[BUFFER_SIZE];
    M_STR *text, *current;
    int i, j, d, p, index, len, token_count;
    for (i = 0; i < PUNCTUATION_LEN; i++)
        punctuation_mstrings[i] = create_m_str(punctuation[i]);
    printf("\nEnter text terminated with a newline: ");
    if (! fgets(input_buffer, BUFFER_SIZE, stdin)) {
        printf("\nNo text entered!");
        return 1;
    }
    printf("\nYou entered:\n%s\n", input_buffer);
    text = create_m_str(input_buffer);
    m_split(text, " ", token_buffer, BUFFER_SIZE, &token_count);
    printf("\nYour text contains %d words.\n", token_count);
    printf("\nWords:");
    for (i = 0; i < token_count; i++) {
        current = token_buffer[i];
        len = m_len(current);
        for (p = 0; p < PUNCTUATION_LEN; p++) {
            index = m_index_of(current, punctuation_mstrings[p]);
            if (index != -1) m_delete(current, index);
        }
        printf("\n    %s", m_cstr(current));
    }
    printf("\nPopping off the last character repeatedly:");
    while (m_len(text) != 0) {
        m_pop(text);
        printf("\n\"%s\"", m_cstr(text));
    }

    free_array_of_m_str(token_buffer, token_count);
    free_array_of_m_str(punctuation_mstrings, PUNCTUATION_LEN);
    free_m_str(text);
}