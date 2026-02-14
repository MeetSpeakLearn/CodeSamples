#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#define BUF_SIZE 1024
char *scramble(char *clear) {
    int len = strlen(clear), i, j;
    char temp;
    for (i = 0; i < len; i++) {
        j = rand() % len;
        temp = clear[j];
        clear[j] = clear[i];
        clear[i] = temp;
    }
    return clear;
}
char *unscramble(char *scrambled) {
    int len = strlen(scrambled), i, j;
    char temp;
    int *random_indices = (int*) calloc(len, sizeof(int));
    for (i = 0; i < len; i++)
        random_indices[i] = rand() % len;
    for (i = len - 1; i >= 0; i--) {
        j = random_indices[i];
        temp = scrambled[j];
        scrambled[j] = scrambled[i];
        scrambled[i] = temp;
    }
    free(random_indices);
    return scrambled;
}
char *encrypt(unsigned int key, char *clear) {
    srand(key);
    return scramble(clear);
}
char *decrypt(unsigned int key, char *scrambled) {
    srand(key);
    return unscramble(scrambled);
}
void read_until_end_of_file(char *buffer) {
    int current;
    int index = 0;
    while ((current = getchar()) != EOF) {
        buffer[index++] = current;
    }
    buffer[index] = '\0';
}
void write_string_to_file(char *buffer) {
    int current, i = 0;
    while ((current = buffer[i++]) != '\0') {
        putchar(current);
    }
}
int main(int argc, char *argv[]) {
    char input_buffer[BUF_SIZE];
    char *arg1 = argv[1];
    char *arg2 = argv[2];
    unsigned int key = (unsigned int) atol(arg2);
    read_until_end_of_file(input_buffer);
    if (strcmp(arg1, "-e") == 0) {
        encrypt(key, input_buffer);
    } else if (strcmp(arg1, "-d") == 0) {
        decrypt(key, input_buffer);
    }
    write_string_to_file(input_buffer);
}