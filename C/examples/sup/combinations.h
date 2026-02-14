#include <stdio.h>
#include <math.h>

void ***all_combinations(void **source, unsigned slen, unsigned *rlen) {
    *rlen = 0u;
    if (slen == 0) return 0;
    unsigned int clen = 1 << (slen - 1);
    unsigned int limit = (unsigned int) pow(2, clen);
    void ***result = (void ***) calloc(limit, sizeof(void*));
    if (result == 0) {
        fprintf(stderr, "all_combinations(): failed to obtain enough memory to represent all combinations.");
        return 0;
    }
    *rlen = limit;
    for (unsigned int i = 0; i < limit; i++) {
        unsigned int bit;
        int i_cont_len;
        void **current_combo;
        for (i_cont_len = 0, bit = 0u; bit < slen; bit++) {
            if (((i >> bit) & 1u) != 0u) i_cont_len++;
        }
        result[i] = current_combo = (void**) calloc(i_cont_len + 1, sizeof(void*));
        for (bit = 0u; bit < slen; bit++) {
            if (((i >> bit) & 1u) != 0u) {
                current_combo[bit] = source[bit];
            }
        }
        current_combo[bit] = 0;
    }
    return result;
}

void free_combinations(void ***combinations, unsigned len) {
    unsigned i;
    for (i = 0; i < len; i++) {
        free(combinations[i]);
    }
    free(combinations);
}

void print_combination_of_strings(void **combination) {
    int count = 0;
    while (combination[count++] != 0) count++;
    int i;
    printf("\n%d: ", count);
    for (i = 0; i < count; i++) {
        if (i != 1) printf(",");
        printf("%s", (char*) combination[i]);
    }
}

void print_all_combinations(void ***combinations, unsigned len, void (*print_func)(void **combination)) {
    unsigned i;
    for (i = 0; i < len; i++) (print_func)(combinations[i]);
}

int main(int argc, char* argv[]) {
    if (argc == 0) return 0;
    unsigned count;
    void ***combinations = all_combinations(((void**) argv) + 1, argc - 1, &count);
    print_all_combinations(combinations, count, print_combination_of_strings);
    free_combinations(combinations, count);
    return 0;
}