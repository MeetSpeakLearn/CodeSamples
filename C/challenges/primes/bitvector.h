/* Author: Stephen DeVoy */
/* Copyright (c) 2026, Stephen DeVoy */

#ifndef BITVECTOR_H

#define BITVECTOR_H

#include "stdlib.h"
#include "string.h"
#include "stdio.h"

#define BITS_PER_UINT (sizeof(unsigned int) * 8)

typedef struct _bitvector {
    unsigned int* uint_vector;
    long size_in_bits;
    long size_in_uints;
    long count;
} BITVECTOR;

BITVECTOR* new_bitvector(unsigned long length_in_bits, unsigned int initial_value);

void free_bitvector(BITVECTOR* vector);

int bv_get(BITVECTOR* vector, long index);

void bv_put(BITVECTOR* vector, long index, int v);

void print_bv(FILE *output_file, BITVECTOR* vector);

#endif
