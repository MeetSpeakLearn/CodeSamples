#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define MAXWORDS 1000
#define MAXWORDLEN 11

typedef char WORD[MAXWORDLEN];

int main(int argc, char* argv[]) {
    int i, len, cmp;
    /* start, end, and length of file in units of bytes */
    long fstart, fend, flen;
    /* first & last define subparts of array in units of WORD */
    long first, last, middle;
    WORD target_word; /* the word searched for */
    WORD current_word; /* the current word inspected in file */
    FILE *fp;
    char* dictionary; /* the name of dictionary file */

    /* check args to command */
    if (argc < 3) {
        fprintf(stderr, "\nuseage: indictionary <dictionary> <word>\n");
        return 1;
    }

    /* get args to command */
    dictionary = argv[1];
    strncpy(target_word, argv[2], MAXWORDLEN);

    /* open dictionary */
    if ((fp = fopen(dictionary, "rb")) == 0) {
        fprintf(stderr, "Failed to open dictionary."); return 1;
    }

    /* rewind and get location of beginning of file */
    rewind(fp);
    fstart = ftell(fp);

    /* goto the end of the file and get location of end of file */
    fseek(fp, 0, SEEK_END);
    fend = ftell(fp);

    /* compute length of file */
    flen = fend - fstart;

    first = 0;
    last = flen / sizeof(current_word);

    /* do a binary search on the dictionary */
    while (first <= last) {
        /* position in units of WORD of word in center of interval */
        middle = first + (last - first) / 2;
        fseek(fp, middle * sizeof(current_word), SEEK_SET);
        fread(current_word, sizeof(current_word), 1, fp);
        cmp = strcmp(current_word, target_word);
        if (cmp == 0) {
            /* word is in dictionary */
            fclose(fp);
            printf("%s\n", current_word);
            return 0;
        } else if (cmp < 0) {
            first = middle + 1;
        } else {
            last = middle - 1;
        }
    }
    fclose(fp);
    return 0;
}
