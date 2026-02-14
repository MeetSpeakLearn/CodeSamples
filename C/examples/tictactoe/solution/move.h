/* Author: Stephen DeVoy */

#ifndef MOVE_INCLUDED
#define MOVE_INCLUDED

/* MOVE represents a move. */
typedef struct MOVE_STRUCT {
    int row;    /* row of board */
    int col;    /* col of board */
} MOVE;

/* allocates a new move, initializes it's row and col fields,
   and then returns a pointer to the move. */
MOVE *create_move(int row, int col);
void free_move(MOVE *move);
void free_moves(MOVE **moves, int count);
char *move_to_string(MOVE *move, char *buf, int size);
char *move_to_string_for_human(MOVE *move, char *buf, int size);

#endif