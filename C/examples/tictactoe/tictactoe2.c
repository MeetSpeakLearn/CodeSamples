#include "game.h"
#include "ui.h"
#define CR printf("\n");

GAME *create_game() {
    GAME *new_game = (GAME*) malloc(sizeof(GAME));
    return new_game;
}
void free_game(GAME* game) {
    CHECK_POINTER(game, "free_game(game)", return;)
    free(game);
}
PLAYER* create_player() {
    PLAYER *new_player = (PLAYER*) malloc(sizeof(PLAYER));
    return new_player;
}
void free_player(PLAYER* player) {
    CHECK_POINTER(player, "free_player(game)", return;)
    free(player);
}

GAME *game;
PLAYER *player1;
PLAYER *player2;

int main() {
    GAME_PIECE winner;
    PLAYER* player;
    int row, col;
    game = create_game();
    player1 = create_player();
    player2 = create_player();
    clear_game(game);
    reset_player(player1);
    reset_player(player2);
    print_board(game); CR CR
    prompt_for_player_name(player1);
    prompt_for_player_name(player2); CR
    while (prompt_to_start_game()) {
        clear_game(game);
        assign_players(game, player1, player2);
        print_player_info(player1); CR
        print_player_info(player2); CR
        do {
            prompt_for_move(game, &row, &col);
            make_move(game, row, col);
            print_board(game); CR CR
            toggle_turn(game);
        } while ((winner = winning_piece(game)) == BLANK);
        player = get_player(game, winner);
        announce_winner(player);
        add_win(player);
    }
}