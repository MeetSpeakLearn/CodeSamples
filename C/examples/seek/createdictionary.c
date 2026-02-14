#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>

#define MAXWORDS 1000
#define MAXWORDLEN 11

int main() {
    int i, j;
    int max = MAXWORDS;
    char words[MAXWORDS][MAXWORDLEN];
    char retrieved_words[MAXWORDS][MAXWORDLEN];
    FILE *fp;
    memset(words, MAXWORDS * MAXWORDLEN, sizeof(char));
    fp = fopen("sorted.txt", "r");
    for (i = 0; (i < MAXWORDS && fgets(words[i], MAXWORDLEN * sizeof(char), fp) != 0); i++) {
        for (j = 0; j < MAXWORDLEN; j++)
            words[i][j] = (words[i][j] == '\n') ? '\0' : tolower(words[i][j]);
        printf("\n%s", words[i]);
    }
    fclose(fp);
    fp = fopen("sorted.bin", "wb");
    fwrite(words, sizeof(words), 1, fp);
    fclose(fp);
    fp = fopen("sorted.bin", "rb");
    fread(retrieved_words, sizeof(words), 1, fp);
    fclose(fp);
    for (i = 0; (i < MAXWORDS); i++) {
        printf("\n%s", words[i]);
    }
    return 0;
}
