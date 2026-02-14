#include <stdio.h>

int main(int argc, char* argv[]) {
    int terminal = (argc <= 1);
    FILE *input;
    char c;
    if (terminal)
        input = stdin;
    else {
        char* filename = argv[1];
        if (! (input = fopen(filename, "r"))) {
            fprintf(stderr, "Failed to open file %s.", filename);
            return 1;
        }
    }
    while ((c = fgetc(input)) != EOF) {
        putchar(c);
    }
    if (! terminal) fclose(input);
    return 0;
}