#include <time.h>
#include <stdio.h>
#include <stdlib.h>

long fibonacci(int n) {
    if (n <= 0) return 0;
    if (n == 1) return 1;
    return fibonacci(n - 2) + fibonacci(n - 1);
}

int main(int argc, char* argv[]) {
    int n = atoi(argv[1]);
    long result;
    clock_t start_time = clock();
    clock_t elapsed_time;
    result = fibonacci(n);
    elapsed_time = clock() - start_time;
    printf("\nfibonacci(%d)=%ld was computed in %ld seconds.\n", n, result, elapsed_time / CLOCKS_PER_SEC);
    return 0;
}