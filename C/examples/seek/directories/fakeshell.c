/* Author: Stephen DeVoy */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <dirent.h>
#include <unistd.h>
#include <sys/stat.h>
#include <sys/types.h>

#define ERROR1(FORMAT_STR,PARAM,RET) {fprintf(stderr, FORMAT_STR, PARAM); return RET;}
#define CHECKCOUNT(FUNC,MSG) if (count == 0) {fprintf(stderr, "%s: %s.", #FUNC, MSG); return 0;}
#define FREE(LHS) {free(LHS); LHS = 0;}
#define COMMANDIS(CMD) (strcmp(command, CMD) == 0)
#define BUFFER_SIZE 512

char cwd[BUFFER_SIZE];

int readline(FILE *fp, char *buffer, int blen, int *eof) {
    int len;
    *eof = 0;
    if (fgets(buffer, blen, fp) == 0) {*eof = 1; return 0;}
    if ((len = strlen(buffer)) > 0)
        if (buffer[len - 1] == '\n') buffer[len - 1] = '\0';
    return strlen(buffer);
}
int split(char *source, char **destin, int destin_len) {
    int count = 0;
    char *current_string = 0;
    char del[2] = " ";
    char *token = strtok(source, del);
    while ((token != NULL) && (count < destin_len)) {
        current_string = (char *) calloc(strlen(token) + 1, sizeof(char));
        strcpy(current_string, token);
        destin[count++] = current_string;
        token = strtok(NULL, del);
    }
    return count;
}
int ls(int count, char **parameters) {
    DIR *dir = opendir(".");
    struct dirent *entry;
    int first = 1;
    if (dir == 0) ERROR1("ls: failed to open directory %s.", ".", 1)
    rewinddir(dir);
    while ((entry = readdir(dir)) != 0) {
        if (entry->d_type == DT_DIR)
            printf("%s[%s]", (first) ? "" : " ", entry->d_name);
        else
            printf("%s%s", (first) ? "" : " ", entry->d_name);
        first = 0;
    }
    closedir(dir);
    return 0;
}
int cd(int count, char **parameters) {
    DIR *dir = opendir(".");
    char *path;
    if (dir == 0) ERROR1("cd: failed to open directory %s.", ".", 1)
    CHECKCOUNT(cd, "directory path expected.")
    path = parameters[0];
    rewinddir(dir);
    closedir(dir);
    if (chdir(path) == -1) ERROR1("cd: %s is not a directory.", path, 1)
    return 0;
}
int makedir(int count, char **parameters) {
    mode_t permissions = S_IRWXU | S_IRGRP | S_IXGRP | S_IROTH | S_IXOTH;
    int i;
    CHECKCOUNT(mkdir, "directory path expected.")
    for (i = 0; i < count; i++) {
        if (mkdir(parameters[i], permissions) == -1)
            ERROR1("mkdir: Cannot created directory %s.", parameters[i], 1)
    }
    return 0;
}
int removedir(int count, char **parameters) {
    int i;
    CHECKCOUNT(mdir, "directory path expected.");
    for (i = 0; i < count; i++) {
        if (rmdir(parameters[i]) == -1)
            ERROR1("rmdir: Cannot remove directory %s.",parameters[i], 1)
    }
    return 0;
}
int removefile(int count, char **parameters) {
    int i;
    CHECKCOUNT(rm, "file path expected.")
    for (i = 0; i < count; i++) {
        if (unlink(parameters[i]) == -1)
            ERROR1("rm: Cannot remove file %s.", parameters[i], 1)
    }
    return 0;
}
int execute(int count, char** tokens) {
    if (count <= 0) return 0;
    char *command = tokens[0], **params = tokens + 1;
    int param_count = count - 1;
    if (COMMANDIS("pwd")) { printf("%s\n", cwd); return 0; }
    else if (COMMANDIS("ls")) return ls(param_count, params);
    else if (COMMANDIS("cd")) return cd(param_count, params);
    else if (COMMANDIS("mkdir")) return makedir(param_count, params);
    else if (COMMANDIS("rmdir")) return removedir(param_count, params);
    else if (COMMANDIS("rm")) return removefile(param_count, params);
    else ERROR1("Unknown command: %s", command, 1)
}
int main() {
    char buffer[BUFFER_SIZE], *tokens[BUFFER_SIZE];
    int chars_read, token_count, eof;
    int i;
    do {
        getcwd(cwd, sizeof(cwd));
        printf("\n%s$ ", cwd);
        chars_read = readline(stdin, buffer, BUFFER_SIZE, &eof);
        if (chars_read > 0) {
            token_count = split(buffer, tokens, BUFFER_SIZE);
            if ((token_count > 0) && (strcmp(tokens[0], "exit") == 0)) return 0;
            execute(token_count, tokens);
            for (i = 0; i < token_count; i++) FREE(tokens[i]);
        }   
    } while (! eof);
    return 0;
}