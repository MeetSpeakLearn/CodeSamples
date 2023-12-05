#include "decimal_arithmetic.h"
#include <iostream>

#pragma once

class Integer
{
private:
	dv_int* dint = 0;
	Integer(dv_int* value);
public:
	Integer();
	Integer(std::int64_t value);
	Integer(const char* value);
	~Integer();
	friend std::ostream& operator<<(std::ostream& os, const Integer& i);

	Integer operator+(Integer& other);
	Integer operator+(std::int64_t& other);

	Integer operator-(Integer& other);
	Integer operator-(std::int64_t& other);

	Integer operator*(Integer& other);
	Integer operator*(std::int64_t& other);

	Integer operator/(Integer& other);
	Integer operator/(std::int64_t& other);

	Integer operator%(Integer& other);
	Integer operator%(std::int64_t& other);

	Integer& operator=(const Integer&);
};

