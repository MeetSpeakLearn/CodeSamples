#include <time.h>
#include <string.h>
#include <stdio.h>
int main(int argc, char* argv[]) {
    int n = atoi(argv[1]);
    clock_t start_time = clock();
    printf("\n%ld\n", CLOCKS_PER_SEC);
    return 0;
}