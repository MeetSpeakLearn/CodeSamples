/* Author: Stephen DeVoy */

#ifndef PLAYER_INCLUDED
#define PLAYER_INCLUDED

#define MAX_NAME_SIZE 32

#include <string.h>

typedef enum {HUMAN, COMPUTER, AI, AS, MONKEY} PLAYER_TYPE;

char *player_type_to_string(PLAYER_TYPE type);

typedef struct PLAYER_STRUCT {
    char name[MAX_NAME_SIZE];
    int wins;
    PLAYER_TYPE type;
} PLAYER;

void init_player(PLAYER *p);
void reset_player(PLAYER* p);
void add_win(PLAYER* p);
int get_player_info(PLAYER* p, char* name, int* wins, PLAYER_TYPE *type);

#endif

