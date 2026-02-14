#include <math.h>
#include <stdio.h>
#include <string.h>
#include <stdlib.h>

/* computes the greatest common divisor by using
   the Euclidean algorithm */
long gcd(long a, long b) {
    long larger, smaller;
    if (a == b) {
        if (a == 0) return 0;       /* gcd of 0 and 0 is defined to be 0 */
        return abs(a);              /* gcd of two non zero equal values is either one of the values */
    }
    else if (a == 0) return abs(b); /* gcd of 0 n is n when n is not 0 */
    else if (b == 0) return abs(a); /* gcd of n 0 is n when n is not 0 */
    else if (a > b) {               /* larger is a, smaller is b */
        larger = a;
        smaller = b;
    } else {                        /* larger is b, smaller is a */
        larger = b;
        smaller = a;
    }
    int gcd = smaller, remainder;
    /* while remainder is not 0 */
    while ((remainder = larger % smaller) != 0) {
        gcd = remainder;        /* remember remainder */
        larger = smaller;       /* the smaller becomes the larger */
        smaller = remainder;    /* the remainder becomes the smaller */
    }
    return abs(gcd);            /* return the abs of the last non-zero remainder */
}

int main(int argc, char *argv[]) {
    /* no args provided, so complain */
    if (argc < 2) {
        fprintf(stderr, "usage: gcd <int>*\n");
        return 0;
    }
    /* convert first arg to long */
    long arg_as_long = atol(argv[1]);
    /* if there is only one arg, it is its own gcd, print it and return */
    if (argc < 3) {
        printf("%ld\n", arg_as_long);
        return 0;
    }
    /* otherwise, compute the gcd of the first two args and then
       loop through the rest of them, getting the gcd of the previous
       args and the current arg, until we run out of args */
    long result = gcd(arg_as_long, atol(argv[2]));
    int i = 3;
    while (i < argc) {
        result = gcd(result, atol(argv[i]));
        i++;
    }
    /* print the GCD of all the args and return */
    printf("%ld\n",result);
    return 0;
}
