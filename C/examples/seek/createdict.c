#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>

#define MAXWORDS 1000
#define MAXWORDLEN 11

typedef char WORD[MAXWORDLEN];

int main(int argc, char* argv[]) {
    int i, j, wc = 0, c;
    int max = MAXWORDS;
    WORD words[MAXWORDS];
    WORD retrieved_words[MAXWORDS];
    FILE *fp;
    char *source, *destin;
    if (argc < 3) {
        fprintf(stderr, "\nuseage createdict <source> <destination>");
        return 0;
    }
    source = argv[1];
    destin = argv[2];
    memset(words, MAXWORDS, sizeof(WORD));
    if ((fp = fopen(source, "r")) == 0) {
        fprintf(stderr, "Failed to open word list."); return 1;
    }
    for (i = 0; (i < MAXWORDS && fgets(words[i], MAXWORDLEN, fp) > 0); i++) {
        for (j = 0; j < MAXWORDLEN; j++)
            words[i][j] = (words[i][j] == '\n') ? '\0' : tolower(words[i][j]);
        wc += 1;
    }
    fclose(fp);
    fp = fopen(destin, "wb");
    fwrite(words, sizeof(WORD), wc, fp);
    fclose(fp);
    fp = fopen(destin, "rb");
    fread(retrieved_words, sizeof(WORD), wc, fp);
    fclose(fp);
    for (i = 0; (i < wc); i++) {
        printf("\n%s", words[i]);
    }
    return 0;
}
