/* Copyright © 2026 Stephen DeVoy. All rights reserved. */

#ifndef PRIMES_H

#define PRIMES_H

#include "bitvector.h"

/* given an upper limit to the size of the largest prime, return a bit vector
   where each index i is set if and only if (i * 2 + 1) is a prime number for
   all prime numbers less than limit. */
BITVECTOR* compute_odd_primes(long limit);

/*  given a pointer to an output stream and a bit vector marked by compute_odd_primes,
    this function prints all of the prime numbers marked in the bit vector. */
long dump_primes(FILE*, BITVECTOR*);

#endif
