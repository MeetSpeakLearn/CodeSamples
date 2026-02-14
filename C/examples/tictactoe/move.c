/* Author: Stephen DeVoy */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "move.h"

MOVE *create_move(int row, int col) {
    MOVE *new_move = (MOVE*) malloc(sizeof(MOVE));
    new_move->col = col;
    new_move->row = row;
    return new_move;
}

void free_move(MOVE *move) {
    // if (move != 0) free_move(move);
}

void free_moves(MOVE **moves, int count) {
    if (moves == 0) return;
    int i;
    for (i = 0; i < count; i++) {
        free_move(moves[i]);
        moves[i] = 0;
    }
}

char *move_to_string(MOVE *move, char *buf, int size) {
    if (buf == 0) return 0;
    if (move == 0) {
        strncpy(buf, "", size);
        return buf;
    }
    snprintf(buf, size, "(%d, %d)", move->row, move->col);
    return buf;
}


char *move_to_string_for_human(MOVE *move, char *buf, int size) {
    if (buf == 0) return 0;
    if (move == 0) {
        strncpy(buf, "", size);
        return buf;
    }
    snprintf(buf, size, "(%d, %d)", move->row + 1, move->col + 1);
    return buf;
}