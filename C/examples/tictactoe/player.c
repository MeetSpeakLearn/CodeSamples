/* Author: Stephen DeVoy */

#include "player.h"
#include "game.h"
void reset_player(PLAYER* p) {
    CHECK_POINTER(p, "reset_player (p)", return)
    p->name[0] = '\0';
    p->wins = 0;
}
void add_win(PLAYER* p) {
    CHECK_POINTER(p, "reset_player (p)", return)
    p->wins++;
}
int get_player_info(PLAYER* p, char* name, int* wins) {
    CHECK_POINTER(p, "get_player_info (p)", return 0)
    CHECK_POINTER(name, "get_player_info (name)", return 0)
    CHECK_POINTER(wins, "get_player_info (wins)", return 0)
    strcpy(name, p->name);
    *wins = p->wins;
}