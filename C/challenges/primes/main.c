/* Copyright © 2026 Stephen DeVoy. All rights reserved. */

#include <stdio.h>
#include <stdlib.h>
#include "bitvector.h"
#include "primes.h"

#define MAX_PRIME 1000000

/* lists all prime numbers less than the first parameter to the function or
   less than MAX_PRIME if there is no first parameter */
int main(int argc, char *argv[]) {
    long limit = 0; /* will be set to the limit */
    if (argc <= 1) {
        /* if no parameter has been provided, set limit to MAX_PRIME */
        limit = MAX_PRIME;
    } else {
        /* try to parse the parameter. If the result is 0, assume it
           is unparsable and exit. Otherwise, continue. */
        limit = atol(argv[1]);
        if (limit <= 0L) return 1;
    }
    /* compute all odd prime numbers */
    BITVECTOR* vec = compute_odd_primes(limit);
    /* print all prime numbers under limit */
    dump_primes(stdout, vec);
    /* free the vector used to store the primes */
    free_bitvector(vec);
    return 0;
}