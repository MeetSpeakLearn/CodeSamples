#include <stdio.h>
#include <stdlib.h>
#include <string.h>

/* The largest LCD our program can find.
   Also, the length of the count_array. */
#define MAX_LCD 2000000

/* The maximum number of parameters to the program. */
#define MAX_ARGS 256

unsigned char count_array[MAX_LCD] = {0};   /* count of occurences */
int args[MAX_ARGS] = {0};                   /* init args to 0 */
int args_count = 0;                         /* number of args */

/* increment the element of count_array at index v and then
   return the count */
int inc_count(int v) {
    /* only values between 0 and v - 1 are valid */
    if ((v < 0) || (v >= MAX_LCD)) {
        fprintf(stderr, "\ninc_count(): out of range.");
        exit(1);
    }
    /* increment the element and return it */
    return ++(count_array[v]);
}

int main(int argc, char *argv[]) {
    /* no args provided, so complain */
    if (argc < 2) {
        fprintf(stderr, "usage: lcd <int>*\n");
        return 1;
    }
    int converted_arg, i;
    /* for every arg... */
    for (i = 1; i < argc; i++) {
        /* if the arg is 0, it is invalid */
        if (strcmp(argv[i], "0") == 0) {
            fprintf(stderr, "invalid arg: 0\n");
            return 1;
        }
        /* if the arg cannot be converted to an int, it is invalid */
        if ((converted_arg = atoi(argv[i])) == 0) {
            fprintf(stderr, "invalid arg: %s\n", argv[i]);
            return 1;
        }
        /* store the converted arg into args, increment args_count */
        args[args_count++] = converted_arg;
    }
    int factor, current, candidate;
    factor = 1;
    /* for all factors, from 1 to infinity... */
    while (1) {
        /* for each of the args... */
        for (i = 0, current = args[i]; i < args_count; i++, current = args[i]) {
            /* the current candidate lcd is factor * current arg */
            candidate = factor * current;
            /* if the current candidate is too big, complain and exit */
            if (candidate >= MAX_LCD) {
                fprintf(stderr, "lcd is too large\n");
                return 1;
            }
            /* if all of the args have a factor equal to this candidate,
               then this candidate is the lcd of all of the args */
            if (inc_count(candidate) == args_count) {
                printf("%d\n", candidate);
                return 0;
            }
        }
        factor += 1;
    }
    return 0;
}
