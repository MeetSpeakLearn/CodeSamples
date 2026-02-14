/* Copyright © 2026 Stephen DeVoy. All rights reserved. */

#include <stdio.h>
#include <math.h>
#include "bitvector.h"
#include "primes.h"
/*  This program uses an optimized version of the Sieve of Eratosthenes
    to compute primes. The standard algorithm uses a vector indexed by
    all integers from 0 through limit and marks off non-primes as it
    processes. If we make an observation, only one prime is even: the number
    2. All other primes are odd. Consequently, we do not need even indices.
    A vector of half the size would be adequate if it could be indexed only
    by odd indices. Also, we know that 0 and 1 are not prime numbers and 2
    is defined to be a prime number, so we do not need to store space to
    mark 0, 1, or 2. Consequently, we index our vector using odd indices only,
    with 3 as the first element. We do a little arithmetic to map actual
    indices to indices of our odds only vector based on 3. Given any actual
    index i, we access the vecor with i/2, where i is odd. We skip any index
    that is even because we do not use it in our algorithm. */

/*  given a limit (an integer 3 or above, produce a bitvector where
    the element at index i is 1 if (i * 2 + 1) is a prime number and
    0 othewise. return the bitvector. */
BITVECTOR* compute_odd_primes(long limit) {
    if (limit < 3L) return 0;

    /*  odds is the number of odd numbers in the vector we use to mark
        of non-primes */
    int odds = (limit >> 1) + limit % 2;
    /*  terminal_index is 1/3 of the limit. Since we only consider odd
        values, we ignore the first multiple of any index (as it will be
        even). The next multiple will be three times the index. If that is
        beyond the end of the vector, then we've finished. */
    long terminal_index = (long) ceil(sqrt(limit - 1));
    /*  allocate the vector of odds */
    BITVECTOR* vec = new_bitvector(odds, 1); 
    long i, j, bi;
    /* exclude 1 */
    bv_put(vec, 0, 0);
    /* for i from 3 up until 1/3 of the limit... */
    for (i = 3; i < terminal_index; i++) {
        /* if i/2 is 1, then i is set, so it is a prime,
           clear the rest at multiples of i, ignoring the
           evens */
        if (bv_get(vec, i >> 1))
            for (j = i + i; j < limit; j += i) {
                /* calculate the odd bitvector index */
                bi = j >> 1;
                /* if j is odd, clear the bit */
                if (j & 1) bv_put(vec, bi, 0);
            }
    }
    return vec;
}
/* dump_primes, given a file pointer and a bitvector produced
    by compute_odd_primes, prints to file all of the prime numbers
    from 2 to the maximum prime marked in the bitvector. */
long dump_primes(FILE* file, BITVECTOR* vec) {
    /* check whether file is null */
    if (file == 0) {
        fprintf(stderr, "\nfile is null.");
        return 0;
    }
    /* check whether vec is null */
    if (vec == 0) {
        fprintf(stderr, "\vec is null.");
        return 0;
    }
    /* get the size of vec */
    long count = vec->size_in_bits, i;
    printf("\ncount=%ld\n", count);
    /* 2 is the only non-odd prime number. the vector marks
       only odd prime numbers, so print 2 before printing
       the odd prime numbers (which are all greater than 2). */
    fprintf(file, "2\n");
    /* each index of the bitvector represents an odd integer.
       index 0 represents 1, index 1 represents 3, index 2
       represents 5, and so on.
       for all bits in the bitvector...*/
    for (i = 0; i < count; i++) {
        /* if the bit at index i is set... */
        if (bv_get(vec, i)) {
            /* print the odd number corresponding to the index (the prime) */
            fprintf(file, "%ld\n", (i << 1) + 1);
        }
    }
    return count;
}