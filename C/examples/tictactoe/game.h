/* Author: Stephen DeVoy */

#ifndef GAME_INCLUDED
#define GAME_INCLUDED
#include <stdio.h>
#include "player.h"
#define CHECK_POINTER(POINTER, MESSAGE, RETURN) { \
    if (POINTER == 0) { \
        printf("\nNull pointer: %s\n", MESSAGE); \
        RETURN; \
    } \
}
typedef enum GAME_PIECE_ENUM {X_PIECE, O_PIECE, BLANK} GAME_PIECE;
typedef struct GAME_STRUCT {
    GAME_PIECE board[3][3];
    int triplets[8][3][2];
    GAME_PIECE turn;
    PLAYER* players[2];
} GAME;
void clear_game(GAME* g);
void assign_players(GAME* g, PLAYER *x, PLAYER *y);
PLAYER* get_player(GAME* g, GAME_PIECE piece);
GAME_PIECE winning_piece(GAME* g);
int make_move(GAME* g, int row, int col);
GAME_PIECE get_piece_at_pos(GAME* g, int row, int col);
GAME_PIECE get_turn(GAME* g);
void toggle_turn(GAME* g);
#endif
