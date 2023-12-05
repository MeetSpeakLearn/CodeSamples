// Author: Steve Devoy, December 5, 2023
// Copyright (c) Steve Devoy

// This is a pet project that I am developing for fun. I began this project December 1, 2023.
// The goal is to compute arithmetic functions using grade-school "hand computation" methods.
// Everything is done "long hand". All numbers are represented in base 10. There are no decimal
// points. Numbers can be of any length, but I've arbitrarily limited them to 1024 digits in length.
// The limit can be changed by redefining MAX_DIGITS below.
//
// The final version will include fractions, math with fractions, etc. Of course, this is much slower
// than using native binary, but I hope to learn from the exericise. I have some ideas of applications
// using this technique.
//
// This header file and its associated c source file will become the underpinning of a C++ library,
// where classes are used to represent whole numbers, fractions, whole numbers with fractions,
// imaginary numbers, and so on.
//
// The C++ library will override operators to allow these "grade school" representations to be
// used in combination of C++'s native numerical representations.

#include <iostream>
#include <cstdint>

#pragma once

#define DEBUG_DECIMAL_ARITHMETIC 1

#define MAX_DIGITS 1024

#define NIBBLES_SIZE(C) (C << 3 + ((C & 7) ? 1 : 0))

#define DIGIT_VECTOR_SIGN_MASK 0x01
#define DIGIT_VECTOR_INT_MASK 0x010
#define DIGIT_VECTOR_FRACTION_MASK 0x020
#define DIGIT_VECTOR_INT_WITH_FRACTION_MASK 0x040

#pragma pack(push, 1)
typedef union
{
    std::uint32_t value;
    struct
    {
        std::uint32_t d0 : 4;
        std::uint32_t d1 : 4;
        std::uint32_t d2 : 4;
        std::uint32_t d3 : 4;
        std::uint32_t d4 : 4;
        std::uint32_t d5 : 4;
        std::uint32_t d6 : 4;
        std::uint32_t d7 : 4;
    };
} nibbles;
#pragma pack(pop)

typedef struct dv_int_struct {
    std::uint32_t info;
    std::uint32_t count;
    nibbles* vector;
} dv_int;

typedef struct dv_fct_struct {
    std::uint32_t info;
    std::uint32_t count;
    dv_int* numerator;
    dv_int* denominator;
} dv_fct;

typedef struct dv_int_and_fct_struct {
    std::uint32_t info;
    std::uint32_t count;
    dv_int* whole;
    dv_fct* fraction;
};

dv_int* new_int_from_int64_t(int64_t value);
dv_int* new_int_from_string(const char* digits);
dv_int* new_int_from_nibbles(nibbles* digits, int digits_count, int sign);
dv_int* new_int_from_dv_int(dv_int* value);
void free_dv_int(dv_int* dvint);
int dv_int_get_sign(dv_int* op);
void dv_int_set_sign(dv_int* op, int sign);
dv_int* dv_int_add(dv_int* op1, dv_int* op2);
dv_int* dv_int_sub(dv_int* op1, dv_int* op2);
dv_int* dv_int_mult(dv_int* op1, dv_int* op2);
dv_int* dv_int_div(dv_int* dividend, dv_int* divisor, int provide_remainder, dv_int** remainder_if_requested);
void dv_int_reduce(dv_int* op);
int dv_count_trailing_zeros(nibbles* digits, int digits_count);
char* dv_int_to_string(dv_int* dint);
void print_dv_int(FILE*, dv_int*);

void test_dv_int();

typedef struct digit_vector_scratch_pad_struct {
    int count;
    nibbles vector[NIBBLES_SIZE( MAX_DIGITS )];
} digit_vector_scratch_pad;

digit_vector_scratch_pad* _new_digit_vector_scratch_pad();
void _free_digit_vector_scratch_pad(digit_vector_scratch_pad* scratch_pad);
int _get_digit(nibbles* v, int i);
void _set_digit(nibbles* v, int i, int d);
int _sp_get_digit(digit_vector_scratch_pad* sp, int i);
void _sp_set_digit(digit_vector_scratch_pad* sp, int i, int d);
void _set_digits_from_ascii(digit_vector_scratch_pad* scratch_pad, char* digits);
void _set_digits_from_nibbles(digit_vector_scratch_pad* scratch_pad, nibbles* digits, int digit_count);
void _shift_left(digit_vector_scratch_pad* scratch_pad);
void _shift_right(digit_vector_scratch_pad* scratch_pad);
void _shift_left_delta(digit_vector_scratch_pad* scratch_pad, int delta);
void _shift_right_delta(digit_vector_scratch_pad* scratch_pad, int delta);
void _scratch_pad_add_in(digit_vector_scratch_pad* scratch_pad, nibbles* digitVector, int digit_count);
int _borrow(nibbles* digits, int digit_count, int index);
int _compare(nibbles* op1, int op1_count, nibbles* op2, int op2_count);
nibbles* _clone(nibbles* digits, int digits_count);
void _scratch_pad_delta(digit_vector_scratch_pad* scratch_pad, nibbles* digitVector, int digit_count);
void _scratch_pad_mult_by_digit(digit_vector_scratch_pad* scratch_pad, int d);
dv_int* _scratch_pad_to_dv_int(digit_vector_scratch_pad* scratch_pad);
void _print_scratch_pad(FILE*, digit_vector_scratch_pad*);
int _subnibbles(nibbles* vector, int length, int start, int count, nibbles** result);
int _find_first_divisible_subnibble(nibbles* dividend, int dividend_length, nibbles* divisor, int divisor_length, int* count, nibbles** result);
std::uint32_t _nibbles_to_uint32_t(nibbles* digits, int digits_length);

void test_scratch_pad_delta();

nibbles* new_nibbles_vector(int size);
nibbles* new_digits_vector(int size);
nibbles* newNibblesVectorFromString(const char* digitsAsString);
void free_nibbles_vector(nibbles* vec);

int nibble_vector_get(nibbles* vec, int index, int nibbleCount, int* value);
int nibble_vector_set(nibbles* vec, int index, int nibbleCount, int value);

int nibble_vector_to_string(nibbles* vec, int digitCount, char* buffer, int bufferSize);
int string_to_nibble_vector(const char* buffer, nibbles* vec, int digitCount);

nibbles* copy_digit_vector(nibbles* vector, int size);
nibbles* trim_digit_vector(nibbles* vector, int size, int* newSize);

bool has_leading_zero(nibbles* vector, int size);

// The rest of this file is scheduled for deletion.
// These functions were a first pass at implementing grade school methods of arithmetic computations.

int add_digit_vectors(nibbles** output, nibbles* input1, int size1, nibbles* input2, int size2);
int sum_digit_vectors(nibbles** output, nibbles** input, int* inputSizes, int inputCount);
int sum_digit_vectors_of_same_size(nibbles** output, nibbles** input, int inputSize, int inputCount);
int multiply_digit_vectors(nibbles** output, nibbles* input1, int size1, nibbles* input2, int size2);

void testNibbles();
void testTrimDigitVector();
void testHasLeadingZeros();
void testAddDigitVectors();
void testSumDigitVectors(const char** posIntegersAsText, int count);
void testMultiplyDigitVectors();

