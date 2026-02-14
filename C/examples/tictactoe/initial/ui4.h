#ifndef UI_INCLUDED
#define UI_INCLUDED
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include "move.h"
#include "game4.h"
#include "player4.h"
#define MAX_INPUT_BUFFER 64
int read_row_or_col(int *val);
int prompt_for_move(GAME* g, MOVE* move);
int prompt_for_player_name(PLAYER* p);
int print_player_info(PLAYER* p);
int prompt_to_start_game();
void announce_winner(PLAYER* p);
void announce_tie();
#endif

