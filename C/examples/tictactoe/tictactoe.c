/* Author: Stephen DeVoy */

#include "game.h"
#include "ui.h"
#define CR printf("\n");
GAME game;
PLAYER player1, player2;

int main() {
    GAME_PIECE winner;
    PLAYER* player;
    int row, col;
    clear_game(&game);
    reset_player(&player1);
    reset_player(&player2);
    print_board(&game); CR CR
    prompt_for_player_name(&player1);
    prompt_for_player_name(&player2); CR
    while (prompt_to_start_game()) {
        clear_game(&game);
        assign_players(&game, &player1, &player2);
        print_player_info(&player1); CR
        print_player_info(&player2); CR
        do {
            prompt_for_move(&game, &row, &col);
            make_move(&game, row, col);
            print_board(&game); CR CR
            toggle_turn(&game);
        } while ((winner = winning_piece(&game)) == BLANK);
        player = get_player(&game, winner);
        announce_winner(player);
        add_win(player);
    }
}