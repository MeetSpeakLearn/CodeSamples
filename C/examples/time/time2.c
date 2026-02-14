#include <time.h>
#include <stdio.h>
int main() {
    char buffer[256];
    time_t now = time(0);
    struct tm *time_info = localtime((time_t*) &now);
    /*
    strftime(buffer, sizeof(buffer), "%a %Y-%m-%d %H:%M:%S %Z", time_info);
    printf("\n%s\n", buffer);
    */
    printf("\n%d\n", now);
    printf("\n%s\n", asctime(time_info));
    return 0;
}