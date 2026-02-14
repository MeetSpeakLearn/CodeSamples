#include <stdio.h>
#include <string.h>

size_t strnlen_s(char *str, size_t strsz) {
    int count = 0;
    char* ptr = str;
    while ((count < strsz) && (*ptr++ != '\0')) count++;
    return count;
}

char *strnchr(char *str, int c, int n) {
    int count = 0;
    char *ptr = str;
    while ((count++ < n) && (*ptr != '\0')) {
        if (*ptr == c) return ptr;
        ptr++;
    }
    return 0;
}

char *strnrchr(char *str, int c, int n) {
    int count = 0;
    char *ptr = str, *result = 0;
    while ((count++ < n) && (*ptr != '\0')) {
        if (*ptr == c) result = ptr;
        ptr++;
    }
    return result;
}

char *strnstr(char *str, char *t, int n_str, int n_t) {
    int i, t_len = 0;
    char *ptr = t;
    for (t_len = 0; ((t_len < n_t) && (*ptr != '\0')); t_len++);
    ptr = str;
    int limit = n_str - n_t;
    for (i = 0; ((i < limit) && (*ptr != '\0')); i++, ptr++) {
        if (strncmp(ptr, t, n_t) == 0)
            return ptr;
    }
    return 0;
}

int main() {
    char test_strlen_s_buffer[] = {'A', 'p', 'p', 'l', 'e'};
    size_t test_strlen_s_result = strnlen_s(test_strlen_s_buffer, 5);
    printf("\ntest_strlen_s_result=%d\n", (int) test_strlen_s_result);
    test_strlen_s_buffer[4] = '\0';
    test_strlen_s_result = strnlen_s(test_strlen_s_buffer, 5);
    printf("\ntest_strlen_s_result=%d\n", (int) test_strlen_s_result);

    char test_strnchr_buffer[10] = "Pineapple";
    char *pos = strnchr(test_strnchr_buffer, 'a', 2);
    printf("\n%s\n", pos);

    unsigned char buffer1[3] = {5u, 0u, 7u};
    unsigned char buffer2[3] = {0u, 0u, 0u};
    memmove(buffer2, buffer1, 3);

    return 0;
}