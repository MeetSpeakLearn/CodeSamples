
#include "ui.h"
#include <time.h>

TEXT_WINDOW *create_text_window(int width, int height) {
    TEXT_WINDOW *new_window = (TEXT_WINDOW *) malloc(sizeof(TEXT_WINDOW));
    new_window->width = width;
    new_window->height = height;
    new_window->updated = 0;
    new_window->grid = (char**) calloc(height, sizeof(char*));
    int i = 0;
    for (i = 0; i < height; i++)
        new_window->grid[i] = 0;
    new_window->active = 0;
    return new_window;
}

void *do_tw(void *p) {
    if (p == 0) return 0;
    TEXT_WINDOW *tw = (TEXT_WINDOW*) p;
    while (tw->active) {
        print_text_window(tw);
        // sleep(UPDATE_FREQUENCY);
    }
    return p;
}

void activate_tw(TEXT_WINDOW *tw) {
    if (tw->active) return;
    tw->active = 1;
    if (pthread_create(&(tw->thread), NULL, do_tw, (void*) tw) != 0) {
        perror("Failed to create text window thread");
    }
}

void deactivate_tw(TEXT_WINDOW *tw) {
    if (tw->active) {
        tw->active = 0;
        if (pthread_join(tw->thread, NULL) != 0) {
            perror("Failed to join text window thread");
        }
    }
}

void free_text_window(TEXT_WINDOW *w) {
    if (w == 0) return;
    if (w->grid) {
        int i;
        for (i = 0; i < HEIGHT; i++) {
            if (w->grid[i]) {
                free(w->grid[i]);
                w->grid[i] = 0;
            }
        }
    }
}

void print_text_window(TEXT_WINDOW *tw) {
    if (tw == 0) return;
    // pthread_mutex_lock(&tw->mutex);
    printf("\033[2J\033[1;1H"); /* clear the screen */
    // printf("\ntime: %ld\n", time(0));
    int row, col;
    for (row = 0; row < tw->height; row++) {
        if (tw->grid[row] == 0)
            for (col = 0; col < tw->width; col++)
                putchar(' ');
        else
            for (col = 0; col < tw->width; col++);
                putchar(tw->grid[row][col]);
        //putchar('\n');
    }
    tw->updated = 0;
    // pthread_mutex_unlock(&tw->mutex);
}

int print_tw_char(TEXT_WINDOW *tw, int row, int col, char c) {
    if (tw == 0) return EOF;
    pthread_mutex_lock(&tw->mutex);
    if ((row >= 0) && (row < tw->height)) {
        if ((col >= 0) && (col < tw->width)) {
            if (tw->grid[row] == 0) {
                tw->grid[row] = (char*) calloc(tw->width + 1, sizeof(char));
                memset(tw->grid[row], ' ', tw->width);
            }
            tw->grid[row][col] = c;
            tw->updated = 1;
            return c;
        }
    }
    pthread_mutex_unlock(&tw->mutex);
    return EOF;
}

int print_tw_cstr(TEXT_WINDOW *tw, int row, int col, char *cstr) {
    if (tw == 0) return 0;
    // pthread_mutex_lock(&tw->mutex);
    if ((row >= 0) && (row < tw->height)) {
        if ((col >= 0) && (col < tw->width)) {
            if (tw->grid[row] == 0) {
                tw->grid[row] = (char*) calloc(tw->width + 1, sizeof(char));
                memset(tw->grid[row], ' ', tw->width);
            }
            int max_str_width = tw->width - col;
            strncpy((tw->grid + row) + col, cstr, max_str_width);
            tw->updated = 1;
            return 0;
        }
    }
    // pthread_mutex_unlock(&tw->mutex);
    return 0;
}

int print_tw_cstr_n(TEXT_WINDOW *tw, int row, int col, char *cstr, int size) {
    if (tw == 0) return 0;
    // pthread_mutex_lock(&tw->mutex);
    if ((row >= 0) && (row < tw->height)) {
        if ((col >= 0) && (col < tw->width)) {
            if (tw->grid[row] == 0) {
                tw->grid[row] = (char*) calloc(tw->width + 1, sizeof(char));
                memset(tw->grid[row], ' ', tw->width);
            }
            int max_str_width = tw->width - col;
            if (size < max_str_width) max_str_width = size;
            tw->updated = 1;
            strncpy((tw->grid + row) + col, cstr, max_str_width);
            return 0;
        }
    }
    // pthread_mutex_unlock(&tw->mutex);
    return 0;
}

void clear_tw_region(TEXT_WINDOW *tw, int x, int y, int width, int height) {
    if (tw == 0) return;
    pthread_mutex_lock(&tw->mutex);
    int right = (tw->width < x + width) ? tw->width - x : width - x;
    int bottom = (tw->height < y + height) ? tw->height - y : height - y;
    int row, col;
    for (row = y; y < right; y++) {
        if (tw->grid[row])
            for (col = x; x < right; col++)
                tw->grid[row][col] = ' ';
    }
    tw->updated = 1;
    pthread_mutex_unlock(&tw->mutex);
}

TEXT_VIEW *create_text_view(TEXT_WINDOW *tw, int x, int y, int width, int height) {
    TEXT_VIEW *tv = (TEXT_VIEW *) malloc(sizeof(TEXT_VIEW));
    tv->x = x; tv->y = y; tv->width = width; tv->height = height;
    tv->parent = tw;
}

void free_text_view(TEXT_VIEW *tv) {
    if (tv == 0) return;
    free(tv);
}

int text_view_in_view(TEXT_VIEW *tv) {
    if (tv == 0) return 0;
    if (tv->x >= tv->parent->width) return 0;
    if (tv->y >= tv->parent->height) return 0;
    if (tv->x - tv->width < 0) return 0;
    if (tv->y - tv->height < 0) return 0;
    return 1;
}

void clear_tv_region(TEXT_VIEW *tv) {
    if (text_view_in_view(tv)) {
        clear_tw_region(tv->parent, MAX(tv->x, 0), MAX(tv->y, 0),
            MIN(tv->x + tv->width, tv->parent->width),
            MIN(tv->y + tv->height, tv->parent->height));
    }
}

int print_tv_cstr(TEXT_VIEW *tv, int y, char *str) {
    int parent_y = tv->y + y;
    int parent_x = tv->x + 0;
    int real_height = MIN(tv->parent->height, parent_y + tv->height);
    int real_width = MIN(tv->parent->width, parent_x + tv->height);
    return print_tw_cstr_n(tv->parent, parent_y, parent_x, str, real_width);
}
