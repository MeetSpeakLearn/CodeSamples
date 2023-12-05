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
// This c source file and its associated c header file will become the underpinning of a C++ library,
// where classes are used to represent whole numbers, fractions, whole numbers with fractions,
// imaginary numbers, and so on.
//
// The C++ library will override operators to allow these "grade school" representations to be
// used in combination of C++'s native numerical representations.

#include <stdlib.h>
#include <string.h>
#include <io.h>
#include "pch.h"
#include "decimal_arithmetic.h"

dv_int* new_int_from_uint32_t(uint32_t value) {
	digit_vector_scratch_pad* sp = _new_digit_vector_scratch_pad();
	int i = 0;

	while (value != 0) {
		_sp_set_digit(sp, i++, value % 10);
		value /= 10;
	}

	dv_int* result = _scratch_pad_to_dv_int(sp);
	_free_digit_vector_scratch_pad(sp);

	return result;
}

dv_int* new_int_from_string(const char* digits) {
	int digits_count = strlen(digits);
	int sign = 0; // zero is positive.

	if ((digits[0] == '+') || (digits[0] == '-')) {
		sign = (digits[0] == '+') ? 0 : 1;
		digits++;
		digits_count--;
	}

	if (digits_count == 0) return 0;

	dv_int* new_int = (dv_int*)malloc(sizeof(dv_int));
	nibbles* v = (nibbles*)calloc(digits_count, sizeof(nibbles));
	std::uint32_t info = ( DIGIT_VECTOR_INT_MASK | ((sign) ? DIGIT_VECTOR_SIGN_MASK : 0x00) );

	new_int->info = info;
	new_int->count = digits_count;
	new_int->vector = v;

	for (int i = 0; i < digits_count; i++)
		_set_digit(v, i, (char)digits[digits_count - i - 1] - '0');

	dv_int_reduce(new_int);

	return new_int;
}

dv_int* new_int_from_nibbles(nibbles* digits, int digits_count, int sign) {
	if (digits_count == 0) return 0;

	dv_int* new_int = (dv_int*)malloc(sizeof(dv_int));
	nibbles* v = (nibbles*)calloc(digits_count, sizeof(nibbles));
	std::uint32_t info = (DIGIT_VECTOR_INT_MASK | ((sign) ? DIGIT_VECTOR_SIGN_MASK : 0x00));

	new_int->info = info;
	new_int->count = digits_count;
	new_int->vector = v;

	for (int i = 0; i < digits_count; i++)
		_set_digit(v, i, _get_digit(digits, i));

	return new_int;
}

void free_dv_int(dv_int* dvint) {
	free(dvint->vector);
	dvint->count = 0;
	dvint->vector = 0;
	dvint->info = 0;
	free(dvint);
}

dv_int* dv_int_add(dv_int* op1, dv_int* op2) {
	digit_vector_scratch_pad* scratch = _new_digit_vector_scratch_pad();
	_set_digits_from_nibbles(scratch, op1->vector, op1->count);
	_scratch_pad_add_in(scratch, op2->vector, op2->count);
	dv_int* result = _scratch_pad_to_dv_int(scratch);
	_free_digit_vector_scratch_pad(scratch);
	dv_int_reduce(result);
	return result;
}

dv_int* dv_int_sub(dv_int* op1, dv_int* op2) {
	int comparison = _compare(op1->vector, op1->count, op2->vector, op2->count);
	int sign = (comparison < 0) ? 1 : 0;
	digit_vector_scratch_pad* sp = _new_digit_vector_scratch_pad();

	_set_digits_from_nibbles(sp, op1->vector, op1->count);
	_scratch_pad_delta(sp, op2->vector, op2->count);

	dv_int* result = new_int_from_nibbles(sp->vector, sp->count, sign);

	_free_digit_vector_scratch_pad(sp);	

	return result;
}

dv_int* dv_int_mult(dv_int* op1, dv_int* op2) {
	nibbles* top_v;
	int top_c;

	nibbles* bot_v;
	int bot_c;

	int carry = 0;

	if (op1->count < op2->count) {
		top_v = op2->vector;
		top_c = op2->count;
		bot_v = op1->vector;
		bot_c = op1->count;
	}
	else {
		top_v = op1->vector;
		top_c = op1->count;
		bot_v = op2->vector;
		bot_c = op2->count;
	}

	digit_vector_scratch_pad* product_scratch = _new_digit_vector_scratch_pad();
	digit_vector_scratch_pad* sum_scratch = _new_digit_vector_scratch_pad();

	fprintf(stderr, "\nBEFORE\nproduct_scratch is: ");
	_print_scratch_pad(stderr, product_scratch);

	fprintf(stderr, "\nBEFORE\nsum_scratch is: ");
	_print_scratch_pad(stderr, sum_scratch);

	for (int b = 0; b < bot_c; b++) {
		_set_digits_from_nibbles(product_scratch, top_v, top_c);

		fprintf(stderr, "\n(%da) product_scratch is: ", b);
		_print_scratch_pad(stderr, product_scratch);

		_scratch_pad_mult_by_digit(product_scratch, _get_digit(bot_v, b));

		fprintf(stderr, "\n(%db) product_scratch is: ", b);
		_print_scratch_pad(stderr, product_scratch);

		if (b > 0) {
			fprintf(stderr, "\nBEFORE SHIFT:");
			_print_scratch_pad(stderr, product_scratch);
			_shift_left_delta(product_scratch, b);
			fprintf(stderr, "\AFTER SHIFT:");
			_print_scratch_pad(stderr, product_scratch);
		}

		dv_int* product = _scratch_pad_to_dv_int(product_scratch);

		fprintf(stderr, "\nPRODUCT is "); print_dv_int(stderr, product); fprintf(stderr, "\n");

		_scratch_pad_add_in(sum_scratch, product->vector, product->count);

		fprintf(stderr, "\n(%d) sum_scratch is: ", b);
		_print_scratch_pad(stderr, sum_scratch);

		free_dv_int(product);
	}

	dv_int* result = _scratch_pad_to_dv_int(sum_scratch);
	_free_digit_vector_scratch_pad(product_scratch);
	_free_digit_vector_scratch_pad(sum_scratch);

	dv_int_reduce(result);

	return result;
}

int are_nibbles_divisable_by_nibbles(nibbles* dividend, int dividend_count, nibbles* divisor, int divisor_count) {
	int comparison = _compare(dividend, dividend_count, divisor, divisor_count);
	
	return comparison > 0;
}

dv_int* dv_int_div(dv_int* dividend, dv_int* divisor, int provide_remainder, dv_int** remainder_if_requested) {
	fprintf(stderr, "\ndv_int_div()");
	int dividend_length = dividend->count;
	nibbles* dividend_vector = dividend->vector;

	int divisor_length = divisor->count;
	nibbles* divisor_vector = divisor->vector;

	int working_dividend_length = dividend_length;
	nibbles* working_dividend = _clone(dividend_vector, dividend_length);

	int divisible_subnibbles_length;
	nibbles* divisible_subnibbles;

	int remaining_subnibbles_length;
	nibbles* remaining_subnibbles;

	digit_vector_scratch_pad* quotient_sp = _new_digit_vector_scratch_pad();
	int quotient_insertion_point = dividend_length - 1;

	uint32_t difference = 0;
	dv_int* remainder = 0;

	int failure = false;

	char buffer[1024];

	while (working_dividend_length > 0) {
		_find_first_divisible_subnibble(working_dividend, working_dividend_length, divisor_vector, divisor_length, &divisible_subnibbles_length, &divisible_subnibbles);

		// quotient_insertion_point -= (divisible_subnibbles_length - 1);

		nibble_vector_to_string(divisible_subnibbles, divisible_subnibbles_length, buffer, 1024);
		fprintf(stdout, "\ndivisible_subnibbles=%s", buffer);

		remaining_subnibbles_length = _subnibbles(working_dividend, working_dividend_length, divisible_subnibbles_length, (working_dividend_length - divisible_subnibbles_length), &remaining_subnibbles);

		if (remaining_subnibbles_length > 0) {
			nibble_vector_to_string(remaining_subnibbles, remaining_subnibbles_length, buffer, 1024);
			fprintf(stdout, "\nremaining_subnibbles=%s", buffer);
		}

		/*if (divisible_subnibbles_length <= 8) {
			std::uint32_t sub_dividend = _nibbles_to_uint32_t(divisible_subnibbles, divisible_subnibbles_length);
			std::uint32_t d = _nibbles_to_uint32_t(divisor_vector, divisor_length);
			std::uint32_t subquotient = sub_dividend / d;
			std::uint32_t difference = sub_dividend - (subquotient * d);

			return;
		}*/

		if (divisible_subnibbles_length <= 8) {
			uint32_t sub_dividend = (int) _nibbles_to_uint32_t(divisible_subnibbles, divisible_subnibbles_length);
			uint32_t d = _nibbles_to_uint32_t(divisor_vector, divisor_length);
			uint32_t subquotient = sub_dividend / d;
			difference = sub_dividend - (subquotient * d);

			_sp_set_digit(quotient_sp, quotient_insertion_point--, subquotient);

			free_nibbles_vector(working_dividend);

			working_dividend_length = remaining_subnibbles_length + 1;

			if (working_dividend_length > 0) {
				working_dividend = new_nibbles_vector(working_dividend_length);

				for (int i = remaining_subnibbles_length - 1; i >= 0; i--)
					_set_digit(working_dividend, i, _get_digit(remaining_subnibbles, i));

				_set_digit(working_dividend, working_dividend_length - 1, difference);
			}
		}

		if (remaining_subnibbles_length == 0) break;
	}

	dv_int* result = new_int_from_nibbles(quotient_sp->vector, quotient_sp->count, 0);

	if (provide_remainder) {
		remainder = new_int_from_uint32_t(difference);
		*remainder_if_requested = remainder;
	}

	// free_nibbles_vector(working_dividend);

	return result;
}

void dv_int_reduce(dv_int* op) {
	int count = op->count;
	nibbles* v = op->vector;

	if (count > 1) {
		for (int i = count - 1; i > 0; i--) {
			if (_get_digit(v, i) != 0) {
				op->count = i + 1;
				return;
			}

			op->count = 1;
		}
	}
}

int dv_count_trailing_zeros(nibbles* digits, int digits_count) {
	int count = 0;

	if (digits_count == 1) return 0;

	for (int i = 0; i < digits_count; i++) {
		if (_get_digit(digits, i) != 0)
			return count;
		else
			count += 1;
	}
	return count;
}

char* dv_int_to_string(dv_int* dint) {
	std::uint32_t info = dint->info;
	int digit_count = dint->count;
	nibbles* v = dint->vector;
	char* as_string = 0;
	char* str_ptr;

	int sign = info & DIGIT_VECTOR_SIGN_MASK;

	if (sign) {
		as_string = (char*)calloc(digit_count + 2, sizeof(char));
		as_string[0] = '-';
		as_string[digit_count + 1] = '\0';
		str_ptr = (as_string + 1);
	}
	else {
		as_string = (char*)calloc(digit_count + 1, sizeof(char));
		as_string[digit_count] = '\0';
		str_ptr = as_string;
	}

	for (int i = digit_count - 1; i >= 0; i++)
		*str_ptr++ = '0' + (char)_get_digit(v, i);

	return as_string;
}

void print_dv_int(FILE* output, dv_int* dint) {
	std::uint32_t info = dint->info;
	int digit_count = dint->count;
	nibbles* v = dint->vector;
	int sign = info & DIGIT_VECTOR_SIGN_MASK;

	if (sign) putc('-', output);

	for (int i = (digit_count - 1); (i >= 0); i--)
		putc('0' + (char) _get_digit(v, i), output);
}

void test_dv_int() {
	dv_int* op1 = new_int_from_string("75");
	dv_int* op2 = new_int_from_string("4");

	fprintf(stdout, "\nop1 = ");
	print_dv_int(stdout, op1);

	fprintf(stdout, "\nop2 = ");
	print_dv_int(stdout, op2);

	dv_int* result1 = dv_int_mult(op1, op2);

	fprintf(stdout, "\n");
	print_dv_int(stdout, op1);
	fprintf(stdout, " * ");
	print_dv_int(stdout, op2);
	fprintf(stdout, " = ");
	print_dv_int(stdout, result1);
	fprintf(stdout, "\n");

	dv_int* result2 = dv_int_sub(op1, op2);

	fprintf(stdout, "\n");
	print_dv_int(stdout, op1);
	fprintf(stdout, " - ");
	print_dv_int(stdout, op2);
	fprintf(stdout, " = ");
	print_dv_int(stdout, result2);
	fprintf(stdout, "\n");

	dv_int* remainder;
	dv_int* result3 = dv_int_div(op1, op2, 1, &remainder);

	fprintf(stdout, "\n");
	print_dv_int(stdout, op1);
	fprintf(stdout, " / ");
	print_dv_int(stdout, op2);
	fprintf(stdout, " = ");
	print_dv_int(stdout, result3);
	fprintf(stdout, "\n");
	fprintf(stdout, "\n with a remainder of ");
	print_dv_int(stdout, remainder);
	fprintf(stdout, "\n");
}

digit_vector_scratch_pad* _new_digit_vector_scratch_pad() {
	digit_vector_scratch_pad* scratch_pad = (digit_vector_scratch_pad*)malloc(sizeof(digit_vector_scratch_pad));
	int nibble_count = NIBBLES_SIZE( MAX_DIGITS );

	scratch_pad->count = 0;

	for (int i = 0; i < nibble_count; i++) scratch_pad->vector[i].value = 0;

	return scratch_pad;
}

void _free_digit_vector_scratch_pad(digit_vector_scratch_pad* scratch_pad) {
	scratch_pad->count = 0;
	free(scratch_pad);
}

void _set_digits_from_ascii(digit_vector_scratch_pad* scratch_pad, char* digits) {
	int count = scratch_pad->count = strlen(digits);
	nibbles* v = scratch_pad->vector;

	for (int i = 0; i < count; i++)
		_set_digit(v, i, (char)digits[i] - '0');
}

void _set_digits_from_nibbles(digit_vector_scratch_pad* scratch_pad, nibbles* digits, int digit_count) {
	int nibbleCount = NIBBLES_SIZE(digit_count);
	nibbles* v = scratch_pad->vector;

	scratch_pad->count = digit_count;

	for (int i = 0; i < nibbleCount; i++)
		v[i].value = digits[i].value;
}

void _shift_left(digit_vector_scratch_pad* scratch_pad) {
	int count = scratch_pad->count;

	if (count >= MAX_DIGITS - 1) {
		fprintf(stderr, "\n_shift_left(): Vector is full. Cannot shift left.");
		return;
	}

	nibbles* vector = scratch_pad->vector;

	for (int i = count; i > 0; i--) {
		_set_digit(vector, i, _get_digit(vector, i - 1));
	}

	_set_digit(vector, 0, 0);

	scratch_pad->count += 1;
}

void _shift_right(digit_vector_scratch_pad* scratch_pad) {
	int count = scratch_pad->count;

	if (count <= 0) {
		fprintf(stderr, "\_shift_right(): Vector is empty. Cannot shift right.");
		return;
	}

	nibbles* vector = scratch_pad->vector;

	for (int i = 0; i < count; i++) {
		_set_digit(vector, i, _get_digit(vector, i + 1));
	}

	_set_digit(vector, count, 0);

	scratch_pad->count -= 1;
}

void _shift_left_delta(digit_vector_scratch_pad* scratch_pad, int delta) {
	int count = scratch_pad->count;
	int i;

	if (count + delta >= MAX_DIGITS - 1) {
		fprintf(stderr, "\_shift_left_delta(): Vector is full. Cannot shift left.");
		return;
	}

	nibbles* vector = scratch_pad->vector;

	for (i = count + delta; i >= delta; i--) {
		_set_digit(vector, i, _get_digit(vector, i - delta));
	}

	for (i = 0; i < delta; i++)	_set_digit(vector, i, 0);

	scratch_pad->count += delta;

	for (i = scratch_pad->count - 1; i >= 1; i--) {
		if (_get_digit(vector, i) != 0) {
			scratch_pad->count = i + 1;
			break;
		}
	}
}

void _shift_right_delta(digit_vector_scratch_pad* scratch_pad, int delta) {
	int count = scratch_pad->count;
	int i;

	if (count - delta <= 0) {
		fprintf(stderr, "\_shift_right_delta(): Vector is empty. Cannot shift right.");
		return;
	}

	nibbles* vector = scratch_pad->vector;

	for (i = delta; i < count; i++) {
		_set_digit(vector, i, _get_digit(vector, i + delta));
	}

	for (i = count; i < delta + count; i++)	_set_digit(vector, i, 0);

	scratch_pad->count -= delta;
}

int _get_digit(nibbles* v, int i) {
	int nibbleIndex = i >> 3;
	nibbles* nibbleSet = v + nibbleIndex;
	int whichNibble = i & 7;
	int valueAtNibble = 0;
	
	/*I've chosen to combine an if and a switch for efficiency.
	A switch statement is significantly faster than an if - else ladder if there are many
	nested if - else's involved. This is due to the creation of a jump table for switch
	during compilation. As a result, instead of checking which case is satisfied throughout
	execution, it just decides which case must be completed.*/

	if (whichNibble < 4) {
		switch (whichNibble) {
		case 0: valueAtNibble = nibbleSet->d0; break;
		case 1: valueAtNibble = nibbleSet->d1; break;
		case 2: valueAtNibble = nibbleSet->d2; break;
		case 3: valueAtNibble = nibbleSet->d3; break;
		}
	}
	else {
		switch (whichNibble) {
		case 4: valueAtNibble = nibbleSet->d4; break;
		case 5: valueAtNibble = nibbleSet->d5; break;
		case 6: valueAtNibble = nibbleSet->d6; break;
		case 7: valueAtNibble = nibbleSet->d7; break;
		}
	}

	return valueAtNibble;
}

void _set_digit(nibbles* v, int i, int d) {
	int nibbleIndex = i >> 3;
	nibbles* nibbleSet = v + nibbleIndex;
	int whichNibble = i & 7;

	/*I've chosen to combine an if and a switch for efficiency.
	A switch statement is significantly faster than an if - else ladder if there are many
	nested if - else's involved. This is due to the creation of a jump table for switch
	during compilation. As a result, instead of checking which case is satisfied throughout
	execution, it just decides which case must be completed.*/

	if (whichNibble < 4) {
		switch (whichNibble) {
		case 0: nibbleSet->d0 = d; break;
		case 1: nibbleSet->d1 = d; break;
		case 2: nibbleSet->d2 = d; break;
		case 3: nibbleSet->d3 = d; break;
		}
	}
	else {
		switch (whichNibble) {
		case 4: nibbleSet->d4 = d; break;
		case 5: nibbleSet->d5 = d; break;
		case 6: nibbleSet->d6 = d; break;
		case 7: nibbleSet->d7 = d; break;
		}
	}
}

int _sp_get_digit(digit_vector_scratch_pad* sp, int i) {
	if (i >= MAX_DIGITS) {
		fprintf(stderr, "\n_sp_get_digit(): Index %d is beyond limit.", i);
		return 0;
	}

	return _get_digit(sp->vector, i);
}

void _sp_set_digit(digit_vector_scratch_pad* sp, int i, int d) {
	if (i >= MAX_DIGITS) {
		fprintf(stderr, "\_sp_set_digit(): Index %d is beyond limit.", i);
		return;
	}

	sp->count = max(sp->count, i + 1);
	_set_digit(sp->vector, i, d);
}

void _scratch_pad_add_in(digit_vector_scratch_pad* scratch_pad, nibbles* digitVector, int digit_count) {
	int count = scratch_pad->count;
	nibbles* vector = scratch_pad->vector;
	int carry = 0;
	int partial = 0;
	int i = 0;

	if (count <= digit_count) {
		if (digit_count > MAX_DIGITS) {
			fprintf(stderr, "_scratch_pad_add_in(): Aborted to avoid overlow.");
			return;
		}

		scratch_pad->count = digit_count;

		for (i = count; i < digit_count; i++) _set_digit(vector, i, 0);

		count = digit_count;
	}

	for (i = 0; i < digit_count; i++) {
		_set_digit(vector, i, (partial = _get_digit(vector, i) + _get_digit(digitVector, i) + carry) % 10);
		carry = partial / 10;
	}

	if (carry) {
		scratch_pad->count = digit_count + 1;
		_set_digit(vector, digit_count, carry);
	}
}

int _borrow(nibbles* digits, int digit_count, int index) {
	if (index >= digit_count) return 0;

	int digit_value = _get_digit(digits, index);

	if (digit_value > 0) {
		_set_digit(digits, index, digit_value - 1);
		return 1;
	}
	else if (_borrow(digits, digit_count, index + 1)) {
		_set_digit(digits, index, 9);
		return 1;
	} else
		return 0;
}

int _compare(nibbles* op1, int op1_count, nibbles* op2, int op2_count) {
	nibbles* op1_reduced = op1;
	nibbles* op2_reduced = op2;
	int op1_reduced_count = op1_count;
	int op2_reduced_count = op2_count;
	int i;
	int diff;

	// char buffer[1024];

	for (i = op1_count - 1; i >= 1; i--)
		if (_get_digit(op1_reduced, i) == 0) {
			op1_reduced--; op1_reduced_count--;
		}

	// nibble_vector_to_string(op1_reduced, op1_reduced_count, buffer, 1024);
	// fprintf(stderr, "\nop1_reduced_count=%d, op1_reduced=%s", op1_reduced_count, buffer);

	for (i = op2_count - 1; i >= 1; i--)
		if (_get_digit(op2_reduced, i) == 0) {
			op2_reduced_count--; op2_reduced_count--;
		}

	// nibble_vector_to_string(op2_reduced, op2_reduced_count, buffer, 1024);
	// fprintf(stderr, "\nop2_reduced_count=%d, op2_reduced=%s", op2_reduced_count, buffer);

	if (op1_reduced_count < op2_reduced_count) return -1;
	if (op2_reduced_count < op1_reduced_count) return 1;

	for (i = op1_reduced_count - 1; i >= 0; i--) {
		diff = _get_digit(op1_reduced, i) - _get_digit(op2_reduced, i);

		// fprintf(stderr, "\ndiff=%d", diff);

		if (diff != 0) return diff;
	}

	return 0;
}

nibbles* _clone(nibbles* digits, int digits_count) {
	int nibbles_count = NIBBLES_SIZE(digits_count);
	nibbles* clone = (nibbles*)calloc(nibbles_count, sizeof(nibbles));
	for (int i = 0; i < digits_count; i++) _set_digit(clone, i, _get_digit(digits, i));
	return clone;
}

void _scratch_pad_delta(digit_vector_scratch_pad* scratch_pad, nibbles* digit_vector, int digit_count) {
	nibbles* larger;
	nibbles* smaller;
	int i;
	int larger_count, smaller_count, comparison;
	int digit_from_larger, digit_from_smaller;
	nibbles* clone = _clone(scratch_pad->vector, scratch_pad->count);
	char buffer[1024];

	nibble_vector_to_string(clone, scratch_pad->count, buffer, 1024);

	fprintf(stderr, "\nclone=%s", buffer);

	comparison = _compare(clone, scratch_pad->count, digit_vector, digit_count);

	fprintf(stderr, "\ncomparison=%d", comparison);

	if (comparison < 0) {
		larger = digit_vector;
		larger_count = digit_count;
		smaller = clone;
		smaller_count = scratch_pad->count;
	}
	else if (comparison > 0) {
		larger = clone;
		larger_count = scratch_pad->count;
		smaller = digit_vector;
		smaller_count = digit_count;
	}
	else {
		larger = clone;
		larger_count = scratch_pad->count;
		smaller = digit_vector;
		smaller_count = digit_count;
	}

	for (i = 0; i < smaller_count; i++) {
		digit_from_larger = _get_digit(larger, i);
		digit_from_smaller = _get_digit(smaller, i);

		if (digit_from_larger < digit_from_smaller) {
			if (_borrow(larger, larger_count, i + 1)) {
				digit_from_larger += 10;
			}
		}

		_set_digit(scratch_pad->vector, i, digit_from_larger - digit_from_smaller);
	}

	for (i = smaller_count; i < larger_count; i++)
		_set_digit(scratch_pad->vector, i, _get_digit(larger, i));

	scratch_pad->count = i;

	for (i = scratch_pad->count - 1; i > 0; i--) {
		if (_get_digit(scratch_pad->vector, i) != 0) {
			scratch_pad->count = i + 1;
			break;
		}
	}

	free_nibbles_vector(clone);
}

void test_scratch_pad_delta() {
	dv_int* op1 = new_int_from_string("10001");
	dv_int* op2 = new_int_from_string("1");
	digit_vector_scratch_pad* sp = _new_digit_vector_scratch_pad();
	_set_digits_from_nibbles(sp, op1->vector, op1->count);
	_scratch_pad_delta(sp, op2->vector, op2->count);
	_print_scratch_pad(stderr, sp);
	_free_digit_vector_scratch_pad(sp);
}

void _scratch_pad_mult_by_digit(digit_vector_scratch_pad* scratch_pad, int d) {
	int count = scratch_pad->count;
	nibbles* vector = scratch_pad->vector;
	int carry = 0;

	for (int i = 0; i < count; i++) {
		int product = d * _get_digit(vector, i) + carry;
		_set_digit(vector, i, product % 10);
		carry = product / 10;
	}

	if (carry) {
		if (count >= MAX_DIGITS) {
			fprintf(stderr, "\n_scratch_pad_mult_by_digit(): multiplication failed due to overflow.");
			return;
		}

		_set_digit(vector, count, carry);
		scratch_pad->count += 1;
	}
}

dv_int* _scratch_pad_to_dv_int(digit_vector_scratch_pad* scratch_pad) {
	return new_int_from_nibbles(scratch_pad->vector, scratch_pad->count, 0);
}

void _print_scratch_pad(FILE* output, digit_vector_scratch_pad* scratch_pad) {
	int count = scratch_pad->count;
	nibbles* v = scratch_pad->vector;

	fprintf(output, "\ndigit_vector_scratch_pad:\n  count=%d\n  vector:\n  [", count);

	for (int i = 0; i < count; i++)
		fprintf(output, "%s%c", (i) ? ", " : " ", '0' + (char)_get_digit(v, i));

	fprintf(output, "]\n");
}

int _subnibbles(nibbles* vector, int length, int start, int count, nibbles** result) {
	if ((start + count > length) || (count == 0)) {
		*result = 0;
		return 0;
	}

	*result = new_nibbles_vector(count);

	for (int i = 0; i < count; i++)
		_set_digit(*result, (count - 1) - i, _get_digit(vector, (length - 1) - start - i));

	return count;
}

int _find_first_divisible_subnibble(nibbles* dividend, int dividend_length, nibbles* divisor, int divisor_length, int* count, nibbles** result) {
	if (divisor_length > dividend_length) return 0;

	int current_length = divisor_length;
	nibbles* current;

	int subnibble_length = _subnibbles(dividend, dividend_length, 0, current_length, &current);

	while (subnibble_length != 0) {
		if (are_nibbles_divisable_by_nibbles(current, subnibble_length, divisor, divisor_length)) {
			*count = subnibble_length;
			*result = current;
			return subnibble_length;
		}

		free_nibbles_vector(current);
		subnibble_length = _subnibbles(dividend, dividend_length, 0, ++current_length, &current);
	}

	return 0;
}

std::uint32_t _nibbles_to_uint32_t(nibbles* digits, int digits_length) {
	if (digits_length <= 8) {
		std::uint32_t result = 0;
		for (int i = digits_length - 1; i >= 0; i--) {
			result = result * 10 + _get_digit(digits, i);
		}

		return result;
	}

	return 0;
}

nibbles* new_nibbles_vector(int size) {
	nibbles* nibbleSet = (nibbles*) calloc(size, sizeof(nibbles));
	for (int i = 0; i < size; i++) nibbleSet[i].value = 0;
	return nibbleSet;
}

nibbles* new_digits_vector(int size) {
	int nibblesCount = size / 8 + ((size % 8) ? 1 : 0);
	return new_nibbles_vector(nibblesCount);
}

nibbles* newNibblesVectorFromString(const char* digitsAsString) {
	int charCount = (int) strlen(digitsAsString);
	int nibblesCount = charCount / 8 + ((charCount % 8) ? 1 : 0);
	nibbles* nibblesVector = new_nibbles_vector(nibblesCount);

	if (!string_to_nibble_vector(digitsAsString, nibblesVector, charCount)) {
		fprintf(stdout, "Error converting \"%s\" to a nibbles vector.", digitsAsString);
		if (nibblesVector) free(nibblesVector);
		return 0;
	}

	return nibblesVector;
}

void free_nibbles_vector(nibbles* vec) {
	if (vec != NULL) {
		free(vec);
	}
}

int nibble_vector_get(nibbles* vec, int index, int nibbleCount, int* value) {
	if ((vec == 0) || (value == 0)) return 0;

	if ((index >> 3) > nibbleCount) return 0;

	*value = _get_digit(vec, index);

	return 1;
}

int nibble_vector_set(nibbles* vec, int index, int nibbleCount, int value) {
	if (vec == 0)
		return 0;

	if ((index >> 3) > nibbleCount) // Consider using shift instead of divide.
		return 0;

	_set_digit(vec, index, value);

	return 1;
}

int nibble_vector_to_string(nibbles* vec, int digitCount, char* buffer, int bufferSize) {
	if ((vec == 0) || (buffer == 0)) return 0;
	if (digitCount > (bufferSize - 1)) return 0;

	buffer[digitCount] = '\0';

	int val = 0;
	int nibblesCount = NIBBLES_SIZE(digitCount);

	for (int vecIndex = 0, bufIndex = digitCount - 1; vecIndex < digitCount; vecIndex++, bufIndex--) {
		if (!nibble_vector_get(vec, vecIndex, nibblesCount, &val))
			return 0;
		else
			buffer[bufIndex] = '0' + val;
	}

	return 1;
}

int string_to_nibble_vector(const char* buffer, nibbles* vec, int digitCount) {
	if ((vec == 0) || (buffer == 0))
#if DEBUG_DECIMAL_ARITHMETIC
	{
		fprintf(stderr, "\nstringToNibbleVector(): vec or buffer is 0.");
		return 0;
	}
#else
		return 0;
#endif		

	int bufferSize = strlen(buffer) + 1;

	if (digitCount > (bufferSize - 1))
#if DEBUG_DECIMAL_ARITHMETIC
	{
		fprintf(stderr, "\nstringToNibbleVector(): digitCount > (bufferSize - 1).");
		return 0;
	}
#else
		return 0;
#endif		

	int nibblesCount = (digitCount / 8) + ((digitCount % 8) ? 1 : 0);

	memset(vec, 0, nibblesCount * sizeof(nibbles));

	for (int vecIndex = 0, bufIndex = digitCount - 1; vecIndex < digitCount; vecIndex++, bufIndex--)
		if (! nibble_vector_set(vec, vecIndex, nibblesCount, buffer[bufIndex] - '0'))
#if DEBUG_DECIMAL_ARITHMETIC
		{
			fprintf(stderr, "\nstringToNibbleVector(): failed to set vector at index %d.", vecIndex);
			return 0;
		}
#else
			return 0;
#endif

	return 1;
}

void testNibbles() {
	char inputBuffer[256];
	char outputBuffer[256];
	nibbles* vec;
	int j;

	for (int i = 1; i < 128; i++) {
		for (j = 0; j < i; j++) inputBuffer[j] = '0' + (j % 10);
		inputBuffer[j] = '\0';

		vec = newNibblesVectorFromString(inputBuffer);

		if (vec == 0) {
			fprintf(stderr, "\nFailed to create a vector.");
			return;
		}

		if (!nibble_vector_to_string(vec, j, outputBuffer, 256)) {
			fprintf(stderr, "\nFailed to convert a vector into a string.");
		}

		fprintf(stderr, "\nVector as string: \"%s\"", outputBuffer);

		free_nibbles_vector(vec);
	}
}

nibbles* copy_digit_vector(nibbles* vector, int size) {
	nibbles* newVector = new_digits_vector(size);
	int nibblesCount = size / 8 + ((size % 8) ? 1 : 0);
	for (int i = 0; i < nibblesCount; i++) newVector[i].value = vector[i].value;
	return newVector;
}

nibbles* trim_digit_vector(nibbles* vector, int size, int* newSize) {
	int trimmedSize = size;
	int nibbleCount = NIBBLES_SIZE(size);
	int i, value;

	if (size <= 0) return copy_digit_vector(vector, size);

	for (i = size - 1; i >= 0; i--) {
		if (!nibble_vector_get(vector, i, nibbleCount, &value)) {
			fprintf(stdout, "\ntrimDigitVector(): error whiling reading value at index %d.", i);
			return 0;
		}
		else if (value) {
			*newSize = i + 1;
			return copy_digit_vector(vector, i + 1);
		}
		else if (!value) {
			if (i == 0) {
				*newSize = 1;
				return newNibblesVectorFromString("0");
			}
		}
	}

	*newSize = size;

	return copy_digit_vector(vector, size);
}

void testTrimDigitVector() {
	const char* samples[] = { "0", "01", "10", "012", "00123" };
	nibbles* currentVector;
	nibbles* trimmedVector;
	char buffer[1024];
	int newSize;

	fprintf(stderr, "\nTesting trim_digit_vector:");

	for (int i = 0; i < 5; i++) {
		const char* current = samples[i];
		currentVector = newNibblesVectorFromString(current);

		if (has_leading_zero(currentVector, strlen(current))) {
			trimmedVector = trim_digit_vector(currentVector, strlen(current), &newSize);
			nibble_vector_to_string(trimmedVector, newSize, buffer, 1024);

			fprintf(stderr, "\n  \"%s\" has been trimmed to %s", current, buffer);
			free_nibbles_vector(trimmedVector);
		}
		else {
			fprintf(stderr, "\n  \"%s\" no leading zeros.", current);
		}

		free_nibbles_vector(currentVector);
	}

}

bool has_leading_zero(nibbles* vector, int size) {
	int lastValue;

	if (size <= 1) return false;
	
	if (!nibble_vector_get(vector, size - 1, NIBBLES_SIZE(size), &lastValue)) {
		fprintf(stderr, "\nhasLeadingZero(): error checking last value.");
		return 0;
	}

	return !lastValue;
}

void testHasLeadingZeros() {
	const char* samples[] = { "0", "01", "10", "012", "00123" };
	nibbles* currentVector;

	fprintf(stderr, "\nTesting hasLeadingZeros:");

	for (int i = 0; i < 5; i++) {
		const char* current = samples[i];
		currentVector = newNibblesVectorFromString(current);
		fprintf(stderr, "\n  \"%s\" %s leading zeros.", current, ((has_leading_zero(currentVector, strlen(current))) ? "has" : "does not have"));
		free_nibbles_vector(currentVector);
	}
}

int add_digit_vectors(nibbles** output, nibbles* input1, int size1, nibbles* input2, int size2) {
	nibbles scratch[MAX_DIGITS];
	int MAX_NIBBLES = NIBBLES_SIZE( MAX_DIGITS );  // MAX_DIGITS / 8 + ((MAX_DIGITS % 8) ? 1 : 0);
	int carry = 0;
	int digitSum = 0;
	int minCount;
	int maxCount;
	int minNibbleCount;
	int maxNibbleCount;
	nibbles* minInput;
	nibbles* maxInput;
	int digit1, digit2;
	int i;
	int outputSize = 0;
	int indexOfMostSignificantNonZero = 0;

	if ((output == 0) || (input1 == 0) || (input2 == 0)) {
		fprintf(stderr, "\naddDigitVectors(): null pointer passed in.");
		return 0;
	}

	if ((size1 <= 0) || (size2 <= 0)) {
		fprintf(stderr, "\naddDigitVectors(): size of input cannot be non-negative.");
		return 0;
	}

	if ((size1 > MAX_DIGITS) || (size2 > MAX_DIGITS)) {
		fprintf(stderr, "\naddDigitVectors(): size of input cannot be greater than %d.", MAX_DIGITS);
		return 0;
	}

	if (size1 == size2) {
		minInput = input1;
		maxInput = input2;
		minCount = maxCount = size1;
		minNibbleCount = maxNibbleCount = NIBBLES_SIZE(size1);
	}
	else if (size1 < size2) {
		minInput = input1;
		maxInput = input2;
		minCount = size1;
		maxCount = size2;
		minNibbleCount = NIBBLES_SIZE(size1);
		maxNibbleCount = NIBBLES_SIZE(size2);
	}
	else {
		minInput = input2;
		maxInput = input1;
		minCount = size2;
		maxCount = size1;
		minNibbleCount = NIBBLES_SIZE(size2);
		maxNibbleCount = NIBBLES_SIZE(size1);
	}

	for (i = 0; i < minCount; i++) {
		if (! nibble_vector_get(minInput, i, minNibbleCount, &digit1)) {
			fprintf(stderr, "\naddDigitVectors(): overflow.");
			return 0;
		}
		if (! nibble_vector_get(maxInput, i, maxNibbleCount, &digit2)) {
			fprintf(stderr, "\naddDigitVectors(): overflow.");
			return 0;
		}

		digitSum = digit1 + digit2 + carry;

		if (digitSum < 10) {
			if (!nibble_vector_set(scratch, i, MAX_NIBBLES, digitSum)) {
				fprintf(stderr, "\naddDigitVectors(): overflow.");
				return 0;
			}
			carry = 0;
		}
		else {
			carry = digitSum / 10;
			if (!nibble_vector_set(scratch, i, MAX_NIBBLES, digitSum % 10)) {
				fprintf(stderr, "\naddDigitVectors(): overflow.");
				return 0;
			}
		}
	}

	for (; i < maxCount; i++) {
		if (!nibble_vector_get(maxInput, i, maxNibbleCount, &digit2)) {
			fprintf(stderr, "\naddDigitVectors(): overflow.");
			return 0;
		}

		digitSum = digit2 + carry;

		if (digitSum < 10) {
			if (!nibble_vector_set(scratch, i, MAX_NIBBLES, digitSum)) {
				fprintf(stderr, "\naddDigitVectors(): overflow.");
				return 0;
			}
			carry = 0;
		}
		else {
			carry = digitSum / 10;
			if (!nibble_vector_set(scratch, i, MAX_NIBBLES, digitSum % 10)) {
				fprintf(stderr, "\naddDigitVectors(): overflow.");
				return 0;
			}
		}
	}

	if (carry) {
		if (!nibble_vector_set(scratch, i, MAX_NIBBLES, carry)) {
			fprintf(stderr, "\naddDigitVectors(): overflow.");
			return 0;
		}
		i++;
	}

	outputSize = i;
	*output = new_digits_vector(outputSize);

	size_t sizeToCopy = NIBBLES_SIZE(outputSize);

	for (int j = 0; j < outputSize; j++) {
		int v;
		nibble_vector_get(scratch, j, MAX_NIBBLES, &v);
		nibble_vector_set(*output, j, sizeToCopy, v);
	}

	return outputSize;
}

void testOneAddition(const char* op1, const char* op2) {
	fprintf(stderr, "\nTesting addition");
	fprintf(stderr, "\n  op1 as string: \"%s\"", op1);
	fprintf(stderr, "\n  op2 as string: \"%s\"", op2);

	char buffer[MAX_DIGITS + 1];
	nibbles* output = 0;
	nibbles* op1AsVector = newNibblesVectorFromString(op1);
	nibbles* op2AsVector = newNibblesVectorFromString(op2);

	if (op1AsVector == 0) {
		fprintf(stderr, "\n  Failed to convert \"%s\" to a digit vector.", op1);
		return;
	}

	if (op2AsVector == 0) {
		fprintf(stderr, "\n  Failed to convert \"%s\" to a digit vector.", op2);
		return;
	}

	int sizeOfOutput = add_digit_vectors(&output, op1AsVector, (int)strlen(op1), op2AsVector, (int)strlen(op2));

	if (sizeOfOutput == 0) {
		fprintf(stderr, "\n  Failed to add operands.");
		return;
	}
	
	if (!nibble_vector_to_string(output, sizeOfOutput, buffer, MAX_DIGITS)) {
		fprintf(stderr, "\n  Failed to convert a vector into a string.");
		return;
	}

	fprintf(stderr, "\n  Sum vector as string: \"%s\"", buffer);

	free_nibbles_vector(op1AsVector);
	free_nibbles_vector(op2AsVector);
	free_nibbles_vector(output);
}


int sum_digit_vectors(nibbles** output, nibbles** input, int* inputSizes, int inputCount) {
	int i;
	int maxNibblesSize;
	int sizeOfSum = 0;
	nibbles* accumulator = 0;
	nibbles* partialSum = 0;

	for (i = 0; i < inputCount; i++) sizeOfSum = max(sizeOfSum, inputSizes[i]);

	sizeOfSum += 1;
	maxNibblesSize = NIBBLES_SIZE(sizeOfSum);
	accumulator = new_digits_vector(sizeOfSum);

	for (i = 0; i < inputCount; i++) {
		sizeOfSum = add_digit_vectors(&partialSum, accumulator, sizeOfSum, input[i], inputSizes[i]);
		
		if (!sizeOfSum) {
			fprintf(stderr, "\nsumDigitVectors(): failed to sum.");
			return 0;
		}

		free_nibbles_vector(accumulator);
		accumulator = partialSum;
	}

	if (has_leading_zero(accumulator, sizeOfSum)) {
		int newSize;
		nibbles* trimmed = trim_digit_vector(accumulator, sizeOfSum, &newSize);
		free_nibbles_vector(accumulator);
		accumulator = trimmed;
		sizeOfSum = newSize;
	}

	*output = accumulator;
	return sizeOfSum;
}

int sum_digit_vectors_of_same_size(nibbles** output, nibbles** input, int inputSize, int inputCount) {
	int i;
	int maxNibblesSize;
	int sizeOfSum = (int)floor(log10(inputSize)) + (int) floor(log10(inputCount));
	nibbles* accumulator = 0;
	nibbles* partialSum = 0;

	for (i = 0; i < inputCount; i++) sizeOfSum = max(sizeOfSum, inputSize);

	sizeOfSum += 1;
	maxNibblesSize = NIBBLES_SIZE(sizeOfSum);
	accumulator = new_digits_vector(sizeOfSum);

	for (i = 0; i < inputCount; i++) {
		sizeOfSum = add_digit_vectors(&partialSum, accumulator, sizeOfSum, input[i], inputSize);
		
		{
			char buffer[1024];
			nibble_vector_to_string(input[i], inputSize, buffer, 1024);
			fprintf(stderr, "\n  partial: %s", buffer);
		}

		if (!sizeOfSum) {
			fprintf(stderr, "\nsumDigitVectorsOfSameSize(): failed to sum.");
			return 0;
		}

		free_nibbles_vector(accumulator);
		accumulator = partialSum;
	}

	if (has_leading_zero(accumulator, sizeOfSum)) {
		int newSize;
		nibbles* trimmed = trim_digit_vector(accumulator, sizeOfSum, &newSize);
		free_nibbles_vector(accumulator);
		accumulator = trimmed;
		sizeOfSum = newSize;
	}

	*output = accumulator;
	return sizeOfSum;
}

void testSumDigitVectors(const char** posIntegersAsText, int count) {
	nibbles** posIntsAsDigitVectors = (nibbles**)calloc(count, sizeof(nibbles));
	int* sizes = (int*)calloc(count, sizeof(int));
	int i;
	nibbles* sum;
	int sizeOfSum;
	char buffer[MAX_DIGITS + 1];

	fprintf(stderr, "\nSumming:");

	for (i = 0; i < count; i++) {
		sizes[i] = strlen(posIntegersAsText[i]);
		posIntsAsDigitVectors[i] = newNibblesVectorFromString(posIntegersAsText[i]);
		nibble_vector_to_string(posIntsAsDigitVectors[i], sizes[i], buffer, MAX_DIGITS);
		fprintf(stderr, "\n  %s", buffer);
	}

	sizeOfSum = sum_digit_vectors(&sum, posIntsAsDigitVectors, sizes, count);

	if (!sizeOfSum) {
		fprintf(stderr, "\nFailed to sum digit vectors.");
		return;
	}

	if (!nibble_vector_to_string(sum, sizeOfSum, buffer, MAX_DIGITS)) {
		fprintf(stderr, "\n  Failed to convert a vector into a string.");
		return;
	}

	fprintf(stderr, "\nThe sum is: \"%s\".", buffer);

	free_nibbles_vector(sum);

	for (i = 0; i < count; i++) free_nibbles_vector(posIntsAsDigitVectors[i]);
}

void testAddDigitVectors() {
	testOneAddition("0", "0");
	testOneAddition("0", "1");
	testOneAddition("1", "1");
	testOneAddition("1", "9");
	testOneAddition("0000000000", "0000000000");
	testOneAddition("0000000000", "0000000001");
	testOneAddition("0000000001", "9999999999");
	testOneAddition("1", "9999999999");
}

int multiply_digit_vectors(nibbles** output, nibbles* input1, int size1, nibbles* input2, int size2) {
	int partialsCount = 0;
	int carry = 0;
	int digitSum = 0;
	int minCount;
	int maxCount;
	int minNibbleCount;
	int maxNibbleCount;
	nibbles* minInput;
	nibbles* maxInput;
	int digit1, digit2;
	int i, j;
	int outputSize = 0;

	if ((output == 0) || (input1 == 0) || (input2 == 0)) {
		fprintf(stderr, "\naddDigitVectors(): null pointer passed in.");
		return 0;
	}

	if ((size1 <= 0) || (size2 <= 0)) {
		fprintf(stderr, "\naddDigitVectors(): size of input cannot be non-negative.");
		return 0;
	}

	if ((size1 > MAX_DIGITS) || (size2 > MAX_DIGITS)) {
		fprintf(stderr, "\naddDigitVectors(): size of input cannot be greater than %d.", MAX_DIGITS);
		return 0;
	}

	if (size1 == size2) {
		minInput = input1;
		maxInput = input2;
		minCount = maxCount = size1;
		minNibbleCount = maxNibbleCount = NIBBLES_SIZE(size1);
	}
	else if (size1 < size2) {
		minInput = input1;
		maxInput = input2;
		minCount = size1;
		maxCount = size2;
		minNibbleCount = NIBBLES_SIZE(size1);
		maxNibbleCount = NIBBLES_SIZE(size2);
	}
	else {
		minInput = input2;
		maxInput = input1;
		minCount = size2;
		maxCount = size1;
		minNibbleCount = NIBBLES_SIZE(size2);
		maxNibbleCount = NIBBLES_SIZE(size1);
	}

	partialsCount = minCount;
	nibbles** partials = (nibbles**)calloc(partialsCount, sizeof(nibbles*));
	int numberOfDigits = minCount + maxCount + 1;
	int sizeOfPartials = NIBBLES_SIZE(numberOfDigits);

	for (i = 0; i < partialsCount; i++) {
		partials[i] = new_digits_vector(numberOfDigits);
	}

	for (i = 0; i < minCount; i++) { // For each digit in bottom row of multiplication
		if (!nibble_vector_get(minInput, i, minNibbleCount, &digit1)) {
			fprintf(stderr, "\nmultiplyDigitVectors(): overflow.");
			return 0;
		}

		for (j = 0; j < i; j++) {
			if (!nibble_vector_set(partials[i], i, sizeOfPartials, 0)) {
				fprintf(stderr, "\nmultiplyDigitVectors(): overflow.");
				return 0;
			}
		}

		carry = 0;

		for (j = i; j < maxCount; j++) { // For each digit in the top row of the multiplication
			if (!nibble_vector_get(maxInput, j, maxNibbleCount, &digit2)) {
				fprintf(stderr, "\nmultiplyDigitVectors(): overflow.");
				return 0;
			}

			int partialProduct = digit1 * digit2 + carry;

			if (!nibble_vector_set(partials[i], j, sizeOfPartials, partialProduct % 10)) {
				fprintf(stderr, "\nmultiplyDigitVectors(): overflow.");
				return 0;
			}

			carry = partialProduct / 10;
		}

		if (carry) {
			if (!nibble_vector_set(partials[i], j, sizeOfPartials, carry)) {
				fprintf(stderr, "\nmultiplyDigitVectors(): overflow.");
				return 0;
			}
		}
	}

	int sizeOfResult = sum_digit_vectors_of_same_size(output, partials, numberOfDigits, partialsCount);

	for (i = 0; i < partialsCount; i++) {
		free_nibbles_vector(partials[i]);
		partials[i] = 0;
	}

	return sizeOfResult;
}

void testOneMutiplication(const char* op1, const char* op2) {
	fprintf(stderr, "\nTesting mutiplication");
	fprintf(stderr, "\n  op1 as string: \"%s\"", op1);
	fprintf(stderr, "\n  op2 as string: \"%s\"", op2);

	char buffer[MAX_DIGITS + 1];
	nibbles* output = 0;
	nibbles* op1AsVector = newNibblesVectorFromString(op1);
	nibbles* op2AsVector = newNibblesVectorFromString(op2);

	if (op1AsVector == 0) {
		fprintf(stderr, "\n  Failed to convert \"%s\" to a digit vector.", op1);
		return;
	}

	if (op2AsVector == 0) {
		fprintf(stderr, "\n  Failed to convert \"%s\" to a digit vector.", op2);
		return;
	}

	int sizeOfOutput = multiply_digit_vectors(&output, op1AsVector, (int)strlen(op1), op2AsVector, (int)strlen(op2));

	if (sizeOfOutput == 0) {
		fprintf(stderr, "\n  Failed to add operands.");
		return;
	}

	if (!nibble_vector_to_string(output, sizeOfOutput, buffer, MAX_DIGITS)) {
		fprintf(stderr, "\n  Failed to convert a vector into a string.");
		return;
	}

	fprintf(stderr, "\n  Product vector as string: \"%s\"", buffer);

	free_nibbles_vector(op1AsVector);
	free_nibbles_vector(op2AsVector);
	free_nibbles_vector(output);
}

void testMultiplyDigitVectors() {
	testOneMutiplication("0", "0");
	testOneMutiplication("1", "1");
	testOneMutiplication("3", "10");
	testOneMutiplication("33", "10");
	testOneMutiplication("33", "100");
	testOneMutiplication("11", "55");
}
