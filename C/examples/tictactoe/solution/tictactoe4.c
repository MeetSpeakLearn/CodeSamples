/* Author: Stephen DeVoy */

#include <ctype.h>
#include <time.h>
#include "game4.h"
#include "player4.h"
#include "move.h"
#include "ui4.h"
#include "extensions.h"
#include "game_extensions.h"

#define CR printf("\n");

GAME *create_game() {
    GAME *new_game = (GAME*) malloc(sizeof(GAME));
    return new_game;
}
void free_game(GAME* game) {
    CHECK_POINTER(game, "free_game(game)", return)
    free(game);
}
void write_game(GAME* game, FILE *fp) {
    CHECK_POINTER(game, "write_game(game)", return)
    CHECK_POINTER(fp, "write_game(fp)", return)
    GAME buffer;
    memcpy(&buffer, game, sizeof(GAME));
    buffer.players[0] = 0;
    buffer.players[1] = 0;
    fwrite(&buffer, sizeof(GAME), 1, fp);
}
GAME *read_game(FILE *fp) {
    CHECK_POINTER(fp, "read_game(fp)", return 0)
    GAME buffer;
    GAME *new_game;
    if (fread(&buffer, sizeof(GAME), 1, fp) != 1) {
        fprintf(stderr, "\nFailed to read game.\n");
        return 0;
    }
    new_game = create_game();
    memcpy(new_game, &buffer, sizeof(GAME));
    return new_game;
}
PLAYER* create_player() {
    PLAYER *new_player = (PLAYER*) malloc(sizeof(PLAYER));
    init_player(new_player);
    return new_player;
}
void free_player(PLAYER* player) {
    CHECK_POINTER(player, "free_player(game)", return)
    free(player);
}
void write_player(PLAYER* player, FILE *fp) {
    CHECK_POINTER(player, "write_player(player)", return)
    CHECK_POINTER(fp, "write_player(fp)", return)
    fwrite(player, sizeof(PLAYER), 1, fp);
}
PLAYER *read_player(FILE *fp) {
    CHECK_POINTER(fp, "read_player(fp)", return 0)
    PLAYER buffer;
    PLAYER *new_player;
    if (fread(&buffer, sizeof(PLAYER), 1, fp) != 1) {
        fprintf(stderr, "\nFailed to read game.\n");
        return 0;
    }
    new_player = create_player();
    memcpy(new_player, &buffer, sizeof(PLAYER));
    return new_player;
}
int prompt_to_start_or_resume_game() {
    char input_buffer[MAX_INPUT_BUFFER] = {0};
    printf("\nWould you like to start a new game or resume previous game? (start/resume/no) ");
    if (scanf("%15s", input_buffer) != -1)
        return (toupper(input_buffer[0]) == 'S')
            ? 1
            : ((toupper(input_buffer[0]) == 'R')
                ? 2 : 0);
    return 0;
}

GAME *game = 0;
PLAYER *player1;
PLAYER *player2;
FILE *fp;
int file_open = 0;

int main() {
    GAME_PIECE winner;
    PLAYER* player;
    MOVE move;
    int row, col, choice;
    srand(time(0)); /* seed pseudorandom number generator */
    fill_triplet_template();
    create_and_register_extensions();

    while (choice = prompt_to_start_or_resume_game()) {
        switch (choice) {
            case 1: /* new game */
                game = (game == 0) ? create_game() : game;
                player1 = create_player();
                player2 = create_player();
                reset_player(player1);
                reset_player(player2);
                prompt_for_player_name(player1);
                prompt_for_player_name(player2); CR
                clear_game(game);
                break;
            case 2: /* resume game */
                if ((fp = fopen("previous_game.bin", "rb")) == 0) {
                    printf("\nNo previous game found!\n");
                    file_open = 0;
                    continue;
                }
                file_open = 1;
                player1 = read_player(fp);
                player2 = read_player(fp);
                if (game != 0) free_game(game);
                game = read_game(fp);
                break;
        }
        print_board(game); CR
        assign_players(game, player1, player2);
        print_player_info(player1); CR
        print_player_info(player2); CR
        do {
            prompt_for_move(game, &move);
            make_move(game, &move);
            print_board(game); CR CR
            toggle_turn(game);
        } while ((winner = winning_piece(game)) == BLANK);
        player = get_player(game, winner);
        if (player == 0)
            announce_tie();
        else {
            announce_winner(player);
            add_win(player);
        }
        print_player_info(player1); CR
        print_player_info(player2); CR
        clear_game(game);
        if (file_open) fclose(fp);
        fp = fopen("previous_game.bin", "wb");
        file_open = 1;
        write_player(player1, fp);
        write_player(player2, fp);
        write_game(game, fp);
        fclose(fp);
        file_open = 0;
        free_player(player1);
        free_player(player2);
        if (game != 0) free_game(game);
        game = 0;
    }
    
    free_extensions();
}