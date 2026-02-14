/* Copyright © 2026 Stephen DeVoy. All rights reserved. */

#include "bitvector.h"
BITVECTOR* new_bitvector(unsigned long length_in_bits, unsigned int initial_value) {
    BITVECTOR* new_bv = (BITVECTOR*) calloc(1, sizeof(BITVECTOR));
    if (new_bv == 0) {
        fprintf(stderr, "\nNot enough memory.");
        exit(1);
    }
    long size_in_uints = length_in_bits / BITS_PER_UINT
                        + ((length_in_bits % BITS_PER_UINT) ? 1 : 0);
    unsigned int* vector = (unsigned int*) calloc(size_in_uints, sizeof(unsigned int));
    if (vector == 0) {
        fprintf(stderr, "\nNot enough memory.");
        exit(1);
    }
    if (initial_value) {
        initial_value = ~ 0U;
    }
    memset(vector, initial_value, size_in_uints * sizeof(unsigned int));
    new_bv->uint_vector = vector;
    new_bv->size_in_bits = length_in_bits;
    new_bv->size_in_uints = size_in_uints;
    new_bv->count = (initial_value) ? length_in_bits : 0;
    return new_bv;
}
void free_bitvector(BITVECTOR* vector) {
    if (vector == 0) return;
    if (vector->uint_vector != 0) {
        memset(vector->uint_vector, 0, vector->size_in_uints * sizeof(unsigned int));
        free(vector->uint_vector);
    }
    memset(vector, 0, sizeof(BITVECTOR));
    free(vector);
}
int bv_get(BITVECTOR* vector, long index) {
    if (vector == 0) return -1;
    if ((index < 0) || (index >= vector->size_in_bits)) {
        fprintf(stderr, "bv_get(): index %ld is out of range.\n", index);
        exit(1);
    }
    long bit_pos = index % BITS_PER_UINT;
    long uint_index = index / BITS_PER_UINT;
    return (vector->uint_vector[uint_index] & (1U << bit_pos)) != 0;
}
void bv_put(BITVECTOR* vector, long index, int v) {
    unsigned int mask;
    if (vector == 0) return;
    if ((index < 0) || (index >= vector->size_in_bits)) {
        fprintf(stderr, "bv_put(): index %ld is out of range.\n", index);
        exit(1);
    }
    v = (v) ? 1 : 0;
    long bit_pos = index % BITS_PER_UINT;
    long uint_index = index / BITS_PER_UINT;
    if (v) {
        mask = 1U << bit_pos;
        if (! bv_get(vector, index)) vector->count++;
        vector->uint_vector[uint_index] |= mask;
    } else {
        mask = ~(1U << bit_pos);
        if (bv_get(vector, index)) vector->count--;
        vector->uint_vector[uint_index] &= mask;
    }
}
void print_bv(FILE *output_file, BITVECTOR* vector) {
    if ((output_file == 0) || (vector == 0)) return;
    if (vector->uint_vector == 0) return;
    int first_pass = 1;
    long i;
    fprintf(output_file, "[");
    for (i = 0; i < vector->size_in_bits; i++) {
        if (! first_pass) {
            fprintf(output_file, ", ");
        } else {
            first_pass = 0;
        }
        fprintf(output_file, "%d", bv_get(vector, i));
    }
    fprintf(output_file, "]");
}