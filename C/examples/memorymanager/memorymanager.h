#include <stdlib.h>

typedef struct flex_array_struct {
    int len;
    int allocated;
    void **array;
} FLEX_ARRAY;

FLEX_ARRAY *create_flex_array(int len) {
    FLEX_ARRAY* new_array = (FLEX_ARRAY*) malloc(sizeof(FLEX_ARRAY));
    new_array->len = len;
    new_array->allocated = len * 2;
    new_array->array = (void**) calloc(new_array->allocated, sizeof(void*));
    return new_array;
}

void free_flex_array(FLEX_ARRAY *fa) {
    if (fa == 0) return;
    int i;
    for (i = 0; i < fa->len; i++) {
        free(fa->array[i]);
    }
    free(fa);
}

void grow_flex_array(FLEX_ARRAY *fa) {
    if (fa == 0) return;
    fa->allocated *= 2;
    fa->array = (void**) realloc(fa->array, fa->allocated * sizeof(void*));
}

void flex_array_insert(FLEX_ARRAY *fa, int index, void *mem) {
    if (fa == 0) return;
    if (fa->len == fa->allocated) grow_flex_array(fa);
    if (index == fa->len) {
        fa->array[fa->len++] = mem;
    } else {
        int i;
        for (i = fa->len; i > index; i--)
            fa->array[i + 1] = fa->array[i];
        fa->array[index] = mem;
        fa->len += 1;
    }
}

void push_onto_flex_array(FLEX_ARRAY *fa, void *mem) {
    if (fa == 0) return;
    flex_array_insert(fa, fa->len, mem);
}

void *pop_from_flex_array(FLEX_ARRAY *fa, int *success) {
    if (fa == 0) {*success = 0; return 0;}
    if (fa->len == 0) {*success = 0; return 0;}
    *success = 1;
    return fa->array[--(fa->len)];
}

void *flex_array_get(FLEX_ARRAY *fa, int index, int *success) {
    if (fa == 0) {*success = 0; return 0;}
    if (index >= fa->len) {*success = 0; return 0;}
    if (index < 0) {*success = 0; return 0;}
    *success = 1;
    return fa->array[index];
}

void *flex_array_set(FLEX_ARRAY *fa, int index, void* mem, int *success) {
    if (fa == 0) {*success = 0; return 0;}
    if (index >= fa->len) {*success = 0; return 0;}
    if (index < 0) {*success = 0; return 0;}
    *success = 1;
    fa->array[index] = mem;
}
