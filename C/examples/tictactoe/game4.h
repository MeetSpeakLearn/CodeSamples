/* Author: Stephen DeVoy */

#ifndef GAME_INCLUDED
#define GAME_INCLUDED
#include <stdio.h>
#include <limits.h>
#include "player4.h"
#include "move.h"
#define DEBUG_GAME 0
#define AI_GAME 1
#define CHECK_POINTER(POINTER, MESSAGE, RETURN) { \
    if (POINTER == 0) { \
        printf("\nNull pointer: %s\n", MESSAGE); \
        RETURN; \
    } \
}
#define POS_INFINITY INT_MAX
#define NEG_INFINITY INT_MIN
typedef enum GAME_PIECE_ENUM {X_PIECE, O_PIECE, BLANK, NO_PIECE} GAME_PIECE;
#define OPPONENT(P) ((P == X_PIECE) ? O_PIECE : X_PIECE)
extern char PIECE_TO_CHAR[3];
char *game_piece_to_string(GAME_PIECE gp);
#if DEBUG_GAME
void print_triplet_template();
#endif
void fill_triplet_template();
typedef GAME_PIECE BOARD[3][3];
#if AI_GAME
BOARD *clone_board(BOARD b);
#endif
typedef struct GAME_STRUCT {
    BOARD board;
    MOVE triplets[8][3];
    GAME_PIECE turn;
    PLAYER* players[2];
} GAME;
void clear_game(GAME* g);
void print_board_board(BOARD board);
void print_board(GAME* g);
int board_is_full_board(BOARD b);
int board_win_full_or_tie(BOARD b);
int board_is_full(GAME *g);
void assign_players(GAME* g, PLAYER *x, PLAYER *y);
PLAYER* get_player(GAME* g, GAME_PIECE piece);
GAME_PIECE winning_piece_board(BOARD board);
GAME_PIECE winning_piece(GAME* g);
int make_move(GAME* g, MOVE *move);
GAME_PIECE get_piece_at_pos(GAME* g, MOVE *move);
GAME_PIECE get_turn(GAME* g);
void toggle_turn(GAME* g);
int autochoose_move(GAME *g, GAME_PIECE piece, MOVE *move);
int find_best_move_using_minmax(GAME *g, GAME_PIECE piece, MOVE *move);
int find_worst_move_using_minmax(GAME *g, GAME_PIECE piece, MOVE *move);
int find_random_move_for_monkey(GAME *g, GAME_PIECE piece, MOVE *move);
#endif
