/* Author: Stephen DeVoy */

#ifndef GAME_EXTENSIONS_INCLUDED
#define GAME_EXTENSIONS_INCLUDED

#include <stdio.h>
#include <limits.h>
#include "player4.h"
#include "move.h"
#include "game4.h"

/* uses author's own strategy to select the next move */
int autochoose_move(GAME *g, GAME_PIECE piece, MOVE *move);
/* repeatedly pick random moves and a legal move is found */
int find_random_move_for_monkey(GAME *g, GAME_PIECE piece, MOVE *move);
/* pick the first available legal move */
int find_first_available_move(GAME *g, GAME_PIECE piece, MOVE *move);
/* find the best move for piece according to minmax */
int find_best_move_using_minmax(GAME *g, GAME_PIECE piece, MOVE *move);
/* find the worst move for piece according to minmax */
int find_worst_move_using_minmax(GAME *g, GAME_PIECE piece, MOVE *move);

/* create all extensions and register them */
void create_and_register_extensions();

#endif