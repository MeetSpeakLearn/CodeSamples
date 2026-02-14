#ifndef UI_HEADER
#define UI_HEADER
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <pthread.h>
#include <unistd.h>

#define WIDTH 80
#define HEIGHT 40

#define MIN(A,B) ((A < B) ? A : B)
#define MAX(A,B) ((A > B) ? A : B)

#define UPDATE_FREQUENCY 10

typedef struct {
    int width;
    int height;
    int updated;
    char **grid;
    int active;
    pthread_t thread;
    pthread_mutex_t mutex;
    pthread_cond_t not_printing;
} TEXT_WINDOW;

TEXT_WINDOW *create_text_window(int width, int height);

void free_text_window(TEXT_WINDOW *w);

void print_text_window(TEXT_WINDOW *tw);

int print_tw_char(TEXT_WINDOW *tw, int row, int col, char c);

int print_tw_cstr(TEXT_WINDOW *tw, int row, int col, char *cstr);

int print_tw_cstr_n(TEXT_WINDOW *tw, int row, int col, char *cstr, int size);

void clear_tw_region(TEXT_WINDOW *tw, int x, int y, int width, int height);

void activate_tw(TEXT_WINDOW *tw);
void deactivate_tw(TEXT_WINDOW *tw);

typedef struct {
    int x;
    int y;
    int width;
    int height;
    TEXT_WINDOW *parent;
} TEXT_VIEW;

TEXT_VIEW *create_text_view(TEXT_WINDOW *tw, int x, int y, int width, int height);

void free_text_view(TEXT_VIEW *tv);

int text_view_in_view(TEXT_VIEW *tv);

void clear_tv_region(TEXT_VIEW *tv);

int print_tv_cstr(TEXT_VIEW *tv, int y, char *str);

#endif
