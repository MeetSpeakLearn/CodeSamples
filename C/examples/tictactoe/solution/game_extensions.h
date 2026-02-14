/* Author: Stephen DeVoy */

#ifndef GAME_EXTENSIONS_INCLUDED
#define GAME_EXTENSIONS_INCLUDED

#include <stdio.h>
#include <limits.h>
#include "player4.h"
#include "move.h"
#include "game4.h"

/* repeatedly pick random moves and a legal move is found */
int find_random_move_for_monkey(GAME *g, GAME_PIECE piece, MOVE *move);
/* pick the first available legal move */
int find_first_available_move(GAME *g, GAME_PIECE piece, MOVE *move);
/* uses author's own strategy to select the next move */
int autochoose_move(GAME *g, GAME_PIECE piece, MOVE *move);

/** ADD ADDITIONAL EXTENSION FUNCTIONS HERE! **/

/* create all extensions and register them */
void create_and_register_extensions();

#endif