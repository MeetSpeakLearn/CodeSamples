/* Author: Stephen DeVoy */

#ifndef GAME_INCLUDED
#define GAME_INCLUDED

#include <stdio.h>
#include <limits.h>
#include "player4.h"
#include "move.h"

/* adds functionality for debugging if 1 */
#define DEBUG_GAME 0

/* check if POINTER is NULL. If it is NULL, print a message and return. */
#define CHECK_POINTER(POINTER, MESSAGE, RETURN) { \
    if (POINTER == 0) { \
        printf("\nNull pointer: %s\n", MESSAGE); \
        RETURN; \
    } \
}

/* meaningful names for most positive and most negative scores */
#define POS_INFINITY INT_MAX
#define NEG_INFINITY INT_MIN

#define MAX_MOVES 9

/* the tic-tac-toe game pieces. BLANK is used to represent an empty cell.
   NO_PIECE may be used to represent not X_PIECE, O_PIECE, or BLANK. */
typedef enum GAME_PIECE_ENUM {X_PIECE, O_PIECE, BLANK, NO_PIECE} GAME_PIECE;

/* evaluates to the inverse piece of X_PIECE or O_PIECE */
#define OPPONENT(P) ((P == X_PIECE) ? O_PIECE : X_PIECE)

/* promises an array of the characters representing X_PIECE, O_PIECE, and BLANK */
extern char PIECE_TO_CHAR[3];

/* given a game_piece, returns the string describing it */
char *game_piece_to_string(GAME_PIECE gp);

#if DEBUG_GAME
void print_triplet_template();
#endif

extern MOVE TRIPLET_TEMPLATE[8][3];

/* initializes a triplet template to all possible winning triplets */
void fill_triplet_template();

/* type representing the 3 x 3 gameboard */
typedef GAME_PIECE BOARD[3][3];

/* returns a copy of board b. not currently used. */
BOARD *clone_board(BOARD b);

/* GAME represents a structure that holds the pieces of the game together */
typedef struct GAME_STRUCT {
    BOARD board;
    MOVE triplets[8][3];
    GAME_PIECE turn;
    PLAYER* players[2];
} GAME;

/* initalize a game */
void clear_game(GAME* g);
/* print character representation of board to stdout */
void print_board_board(BOARD board);
/* print the game's character representation of board to stdout */
void print_board(GAME* g);
/* returns 1 if the board is full and 0 otherwise */
int board_is_full_board(BOARD b);
/* returns 1 if the game's board is full and 0 otherwise */
int board_is_full(GAME *g);
/* assigns x and y as players of g */
void assign_players(GAME* g, PLAYER *x, PLAYER *y);
/* returns the player of g with piece */
PLAYER* get_player(GAME* g, GAME_PIECE piece);
/* if there is a winner of board, returns a pointer to the winner; otherwise, 0 */
GAME_PIECE winning_piece_board(BOARD board);
/* if there is a winner of game g's board, returns a pointer to the winner; otherwise, 0 */
GAME_PIECE winning_piece(GAME* g);

/* fills the position at move with the game piece of the player
   whose turn it currently is */
int make_move(GAME* g, MOVE *move);
/* given a board, fill moves_buffer with all available moves.
   return the count of available moves */
int all_available_moves_board(BOARD board, MOVE **moves_buffer);
/* given a game, fill moves_buffer with all available moves.
   return the count of available moves */
int all_available_moves(GAME *g, MOVE **moves_buffer);
/* returns the game piece of game g at position move */
GAME_PIECE get_piece_at_pos(GAME* g, MOVE *move);
/* returns the game piece of the player whose turn it currently is */
GAME_PIECE get_turn(GAME* g);
/* if it is X's turn, change it to O's turn and vice versa */
void toggle_turn(GAME* g);
/* generate a random name for player. if there is an exension for player_type,
   use it for the name's stem */
void default_player_name_generator(char *buffer, PLAYER_TYPE player_type);
#endif
