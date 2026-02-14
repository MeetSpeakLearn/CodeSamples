/* Author: Stephen DeVoy */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>

/* all_combinations: source is an array of pointers to objects of an unknown type,
   slen is the number of elements in source, and rlen is a pointer to an integer.
   Returns a dynamically allocated array of length *rlen. Each element of the
   returned array represents a combination of the elements in source. All
   possible combinations are returned. Each combination is an array of void* and
   is also dynamically allocated. To free the returned dynamic memory, call
   free_combinations.
*/
void ***all_combinations(void **source, unsigned long slen, unsigned long *rlen) {
    unsigned long j, i_cont_len;
    /* we initialize the result length to 0 */
    *rlen = 0u;
    /* if the array is empty, there are no combinations */
    if (slen == 0) return 0;
    /* the number of possible combinations is 2 ^ slen, which can be
       efficiently computed by shifting the value 1's bits leftward
       by slen */
    unsigned long i, limit = 1 << slen;
    unsigned bit;
    void **current_combo;
    /* allocate memory for the result */
    void ***result = (void ***) calloc(limit, sizeof(void*));
    if (result == 0) {
        /* if slen is large, it is possible that the heap does not have
           enough memory to store all combinations. if the heap fails
           to find enough memory, print an error message and return */
        fprintf(stderr, "all_combinations(): failed to obtain enough memory to represent all combinations.");
        return 0;
    }
    /* set *rlen to the size of the array we will return */
    *rlen = limit;
    /* the bits in values from 0 to limit indicate whether the corresponding
       element of source is in the current combination */
    for (i = 0; i < limit; i++) {
        /* compute the number of set bits in i */
        for (i_cont_len = 0, bit = 0u; bit < slen; bit++) {
            if (((i >> bit) & 1u) != 0u) i_cont_len++;
        }
        /* i_cont_len is the number of elements in the current combination.
           we allocate enough memory for all elements and one 0 element.
           the zero element indicates the end of the combination. */
        result[i] = current_combo = (void**) calloc(i_cont_len + 1, sizeof(void*));
        /* error if we run out of memory */
        if (current_combo == 0) {
            fprintf(stderr, "all_combinations(): failed to obtain enough memory to represent all combinations.");
            return 0;
        }
        
        for (bit = 0u, j = 0; bit < slen; bit++) {
            if (((i >> bit) & 1u) != 0u) {
                current_combo[j++] = source[bit];
            }
        }
        current_combo[j] = 0;
    }
    return result;
}

void free_combinations(void ***combinations, unsigned long len) {
    unsigned long i;
    for (i = 0; i < len; i++) free(combinations[i]);
    free(combinations);
}

void print_combination_of_strings(void **combination) {
    unsigned long i, count = 0;
    while (combination[count] != 0) count++;
    if (count == 0) {
        printf("\n0:");
        return;
    }
    printf("\n%lu:", count);
    for (i = 0; i < count; i++) {
        if (i != 0) printf(",");
        printf("%s", (char*) combination[i]);
    }
}

void print_all_combinations(void ***combinations, unsigned long len,
        void (*print_func)(void **combination)) {
    unsigned i;
    for (i = 0; i < len; i++) (print_func)(combinations[i]);
    printf("\n");
}

int main(int argc, char* argv[]) {
    if (argc == 0) return 0;
    unsigned long count;
    void **source = (void**) calloc(argc - 1, sizeof(void*));
    int i;
    for (i = 1; i < argc; i++) {
        char *str = calloc(strlen(argv[i]) + 1, sizeof(char));
        strcpy(str, argv[i]);
        source[i - 1] = str;
    }
    void ***combinations = all_combinations(source, (unsigned) (argc - 1), &count);
    printf("%lu:*", count);
    print_all_combinations(combinations, count, print_combination_of_strings);
    free_combinations(combinations, count);
    for (i = 0; i < argc - 1; i++) free(source[i]);
    free(source);
    return 0;
}