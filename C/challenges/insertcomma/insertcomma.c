#include <stdio.h>

#define MAX_LINE_LENGTH 256

int main() {
    char line[MAX_LINE_LENGTH];
    char *ptr;
    int first_pass;
    while ((ptr = fgets(line, sizeof(line), stdin)) != NULL) {
        first_pass = 1;
        while (*ptr) {
            if (*ptr == '\n') {
                putchar('\n');
                break;
            }
            if (! first_pass) {
                putchar(',');
            } else
                first_pass = 0;
            putchar(*ptr++);
        }
    }
}
