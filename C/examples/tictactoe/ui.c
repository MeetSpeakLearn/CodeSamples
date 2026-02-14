#include <ctype.h>
#include "ui.h"
char PIECE_TO_CHAR[3] = {'X', 'O', '_'};
void print_board(GAME* g) {
    CHECK_POINTER(g, "print_board (g)", return)
    int row, col;
    for (row = 0; row < 3; row++) {
        printf("\n");
        for (col = 0; col < 3; col++)
            printf("%c ", PIECE_TO_CHAR[get_piece_at_pos(g, row, col)]);
    }
}
int read_row_or_col(int *val) {
    int within_bounds = 0, tries = 0;
    do {
        while (scanf("%d", val) == -1)
            printf("\nInvalid integer.");
        within_bounds = ((*val >= 0) && (*val <= 3));
        if (! within_bounds)
            printf("\nInvalid row or column.");
    } while (! within_bounds && (tries++ < 3));
    return within_bounds;
}
int prompt_for_move(GAME* g, int *row, int *col) {
    CHECK_POINTER(g, "print_board (g)", return 0)
    char input_buffer[MAX_INPUT_BUFFER] = {0};
    GAME_PIECE turn = get_turn(g);
    PLAYER *p = get_player(g, turn);
    int valid_row_or_col = 0, r, c, valid_input;
    int valid_row_and_col = 0;
    CHECK_POINTER(p, "prompt_for_move (p)", 0)
    while (! valid_row_and_col) {
        printf("\nIt is player %c's turn. Enter a move, %s!\n",
            PIECE_TO_CHAR[turn], p->name);
        printf("\n  row: ");
        valid_input = read_row_or_col(&r);
        if (! valid_input) continue;
        printf("\n  col: ");
        valid_input = read_row_or_col(&c);
        printf("\n(%d, %d)\n", r, c);
        if (! valid_input) continue;
        valid_row_and_col = 1;
    }
    *row = r;
    *col = c;
    return 1;
}
int prompt_for_player_name(PLAYER* p) {
    CHECK_POINTER(p, "prompt_for_player_name (p)", return 0)
    char buffer[MAX_INPUT_BUFFER] = {};
    int input_size;
    printf("Please enter the name of the player: " );
    input_size = scanf("%15s", buffer);
    if (! input_size) {
        printf("\nThis player will be knows as Unknown.");
        strcpy(buffer, "Unknown");
    }
    strcpy(p->name, buffer);
}
int print_player_info(PLAYER* p) {
    CHECK_POINTER(p, "print_player_info (p)", return 0)
    char name[MAX_INPUT_BUFFER];
    int wins;
    if (get_player_info(p, name, &wins)) {
        printf("Player: %s with %d wins.", name, wins);
        return 1;
    }
    printf("Failed to retrieve player info.");
    return 0;
}
int prompt_to_start_game() {
    char input_buffer[MAX_INPUT_BUFFER] = {0};
    printf("\nWould you like to start a new game? (yes/no) ");
    if (scanf("%15s", input_buffer) != -1)
        return (toupper(input_buffer[0]) == 'Y');
    return 0;
}
void announce_winner(PLAYER* p) {
    printf("\n!!!%s wins!!!\n", p->name);
}
