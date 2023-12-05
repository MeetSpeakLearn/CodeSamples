#include "pch.h"
#include "Integer.h"


Integer::Integer(dv_int* value) {
	dint = value;
}

Integer::Integer() {
	dint = new_int_from_int64_t(0);
}

Integer::Integer(std::int64_t value) {
	std::uint32_t v;
	int sign = 0;

	if (value < 0) {
		v = (std::uint32_t)(-1 * value);
		sign = 1;
	}
	else {
		v = (std::uint32_t)value;
		sign = 0;
	}

	dint = new_int_from_int64_t(v);
	dv_int_set_sign(dint, sign);
}

Integer::Integer(const char* value) {
	dint = new_int_from_string(value);
}

Integer::~Integer() {
	if (dint != 0) {
		free_dv_int(dint);
		dint = 0;
	}
}

std::ostream& operator<<(std::ostream& os, const Integer& i)
{
	char* c = dv_int_to_string(i.dint);
	os << c;
	free(c);
	return os;
}

Integer Integer::operator+(Integer& other) {
	dv_int* sum = dv_int_add(dint, other.dint);
	return Integer(sum);
}


Integer Integer::operator+(std::int64_t& other) {
	Integer otherAsInteger(other);
	return *this + otherAsInteger;
}

Integer Integer::operator-(Integer& other) {
	if (this == &other) {
		return Integer();
	}
	dv_int* dif = dv_int_sub(dint, other.dint);
	return Integer(dif);
}

Integer Integer::operator-(std::int64_t& other) {
	Integer otherAsInteger(other);
	return *this - otherAsInteger;
}

Integer Integer::operator*(Integer& other) {
	dv_int* sum = dv_int_mult(dint, other.dint);
	return Integer(sum);
}


Integer Integer::operator*(std::int64_t& other) {
	Integer otherAsInteger(other);
	return *this * otherAsInteger;
}

Integer Integer::operator/(Integer& other) {
	dv_int* sum = dv_int_div(dint, other.dint, 0, 0);
	return Integer(sum);
}


Integer Integer::operator/(std::int64_t& other) {
	Integer otherAsInteger(other);
	return *this / otherAsInteger;
}

Integer Integer::operator%(Integer& other) {
	dv_int* quotient;
	dv_int* sum = dv_int_div(dint, other.dint, 1, &quotient);
	free_dv_int(sum);
	return Integer(quotient);
}

Integer Integer::operator%(std::int64_t& other) {
	Integer otherAsInteger(other);
	return *this % otherAsInteger;
}

Integer& Integer::operator=(const Integer& other) {
	free_dv_int(dint);
	dint = new_int_from_dv_int(other.dint);
	return *this;
}

