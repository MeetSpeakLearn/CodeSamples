/* Author: Stephen DeVoy */

#ifndef PLAYER_INCLUDED
#define PLAYER_INCLUDED

#define MAX_NAME_SIZE 16

#include <string.h>

typedef struct PLAYER_STRUCT {
    char name[MAX_NAME_SIZE];
    int wins;
} PLAYER;

void reset_player(PLAYER* p);

void add_win(PLAYER* p);

int get_player_info(PLAYER* p, char* name, int* wins);

#endif

