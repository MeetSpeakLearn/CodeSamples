/* Author: Stephen DeVoy */

#ifndef MOVE_INCLUDED
#define MOVE_INCLUDED

typedef struct MOVE_STRUCT {
    int row;
    int col;
} MOVE;

MOVE *create_move(int row, int col);
void free_move(MOVE *move);
void free_moves(MOVE **moves, int count);
char *move_to_string(MOVE *move, char *buf, int size);
char *move_to_string_for_human(MOVE *move, char *buf, int size);

#endif