#include <stdio.h>
#include <stdlib.h>
int main(int argc, char* argv[]) {
    char buffer[1024];
    FILE *fp = popen("timedatectl", "r");
    while (fgets(buffer, sizeof(buffer), fp) != NULL) {
        printf("%s", buffer);
    }
    pclose(fp);
}