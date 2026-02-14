/* Author: Stephen DeVoy */

#include "player4.h"
#include "game4.h"

char *player_types[] = {"Human", "Computer", "AI", "AS", "Monkey",
    "Sloth", "Ext1", "Ext2" "Ext3" "Ext4"};

char *player_type_to_string(PLAYER_TYPE type) {
    return player_types[type];
}

void init_player(PLAYER* p) {
    CHECK_POINTER(p, "init_player (p)", return)
    p->name[0] = '\0';
    p->wins = 0;
    p->type = HUMAN;
}

void reset_player(PLAYER* p) {
    CHECK_POINTER(p, "reset_player (p)", return)
    p->name[0] = '\0';
    p->wins = 0;
    p->type = HUMAN;
}
void add_win(PLAYER* p) {
    CHECK_POINTER(p, "reset_player (p)", return)
    p->wins++;
}
int get_player_info(PLAYER* p, char* name, int* wins, PLAYER_TYPE *type) {
    CHECK_POINTER(p, "get_player_info (p)", return 0)
    CHECK_POINTER(name, "get_player_info (name)", return 0)
    CHECK_POINTER(wins, "get_player_info (wins)", return 0)
    CHECK_POINTER(type, "get_player_info (type)", return 0)
    strcpy(name, p->name);
    *wins = p->wins;
    *type = p->type;
}