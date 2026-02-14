/* Author: Stephen DeVoy */

#include <ctype.h>
#include "game4.h"
#include "player4.h"
#include "move.h"
#include "ui4.h"
int read_row_or_col(int *val) {
    int within_bounds = 0, tries = 0;
    do {
        while (scanf("%d", val) == -1)
            printf("\nInvalid integer.");
        within_bounds = ((*val >= 1) && (*val <= 3));
        if (! within_bounds)
            printf("\nInvalid row or column.");
    } while (! within_bounds && (tries++ < 3));
    *val -= 1;
    return within_bounds;
}
int prompt_for_move(GAME* g, MOVE *move) {
    CHECK_POINTER(g, "print_board (g)", return 0)
    CHECK_POINTER(move, "print_board (move)", return 0)
    char input_buffer[MAX_INPUT_BUFFER] = {0};
    char move_buffer[16];
    GAME_PIECE turn = get_turn(g);
    PLAYER *p = get_player(g, turn);
    int valid_row_or_col = 0, valid_input;
    int valid_row_and_col = 0;
    CHECK_POINTER(p, "prompt_for_move (p)", 0)
    while (! valid_row_and_col) {
        if (p->type == HUMAN) {
            printf("\nIt is player %c's turn. Enter a move, %s!\n",
                PIECE_TO_CHAR[turn], p->name);
            printf("\n  row: ");
            valid_input = read_row_or_col(&(move->row));
            if (! valid_input) continue;
            printf("\n  col: ");
            valid_input = read_row_or_col(&(move->col));
        } else if (p->type == COMPUTER) {
            autochoose_move(g, g->turn, move);
            valid_input = 1;
            printf("\n%s chose move:", p->name);
#if AI_GAME
        } else if (p->type == AI) {
            find_best_move_using_minmax(g, g->turn, move);
            char aibuf[128];
            move_to_string(move, aibuf, 128);
            printf("%s\n", aibuf);
            valid_input = 1;
            printf("\n%s chose move:", p->name);
        } else if (p->type == AS) {
            find_worst_move_using_minmax(g, g->turn, move);
            char aibuf[128];
            move_to_string(move, aibuf, 128);
            printf("%s\n", aibuf);
            valid_input = 1;
            printf("\n%s chose move:", p->name);
#endif
        } else if (p->type == MONKEY) {
            find_random_move_for_monkey(g, g->turn, move);
            valid_input = 1;
        } else {
            fprintf(stderr, "Invalid player type.");
            exit(-1);
        }
        move_to_string_for_human(move, move_buffer, 16);
        printf("\n%s\n", move_buffer);
        if (! valid_input) continue;
        valid_row_and_col = 1;
    }
    return 1;
}
void generate_random_computer_player_name(char *buffer, PLAYER_TYPE player_type) {
    int random_suffix_as_int = rand() % 999999;
    if (player_type == COMPUTER)
        sprintf(buffer, "ComputerPlayer#%06d", random_suffix_as_int);
    else if (player_type == AI)
        sprintf(buffer, "AIPlayer#%06d", random_suffix_as_int);
    else if (player_type == AS)
        sprintf(buffer, "ASPlayer#%06d", random_suffix_as_int);
    else if (player_type == MONKEY)
        sprintf(buffer, "Monkey#%06d", random_suffix_as_int);
    else
        sprintf(buffer, "UnknownType#%06d", random_suffix_as_int);
}
int prompt_for_player_name(PLAYER* p) {
    CHECK_POINTER(p, "prompt_for_player_name (p)", return 0)
    char buffer[MAX_INPUT_BUFFER] = {};
    int input_size;
    printf("\nIf this player is the computer, enter # as the player's name.\n");
#if AI_GAME
    printf("If this player is an AI, enter + (plus) as the player's name.\n");
    printf("If this player is AS (Arificially Stupid), enter - (dash) as the player's name.\n");
#endif
    printf("If this player is a monkey, enter @ as the player's name.\n");
    printf("Please enter the name of the player: " );
    p->type = HUMAN;
    input_size = scanf("%15s", buffer);
    if (! input_size) {
        printf("\nThis player will be knows as Unknown.\n");
        strcpy(buffer, "Unknown");
    } else if (strcmp(buffer, "#") == 0) {
        generate_random_computer_player_name(buffer, COMPUTER);
        printf("\nThis player will be known as %s.\n", buffer);
        p->type = COMPUTER;
    }
#if AI_GAME
    else if (strcmp(buffer, "+") == 0) {
        generate_random_computer_player_name(buffer, AI);
        printf("\nThis player will be known as %s.\n", buffer);
        p->type = AI;
    }
    else if (strcmp(buffer, "-") == 0) {
        generate_random_computer_player_name(buffer, AS);
        printf("\nThis player will be known as %s.\n", buffer);
        p->type = AI;
    }
#endif
    else if (strcmp(buffer, "@") == 0) {
        generate_random_computer_player_name(buffer, MONKEY);
        printf("\nThis player will be known as %s.\n", buffer);
        p->type = MONKEY;
    } 
    strcpy(p->name, buffer);
}
int print_player_info(PLAYER* p) {
    CHECK_POINTER(p, "print_player_info (p)", return 0)
    char name[MAX_INPUT_BUFFER];
    int wins;
    PLAYER_TYPE ptype;
    if (get_player_info(p, name, &wins, &ptype)) {
        printf("%s player: %s with %d wins.", player_type_to_string(ptype), name, wins);
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
    printf("\n!!! %s wins!!! \n", p->name);
}
void announce_tie() {
    printf("\n!!! We have a tie !!! \n");
}
