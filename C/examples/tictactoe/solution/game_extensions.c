/* Author: Stephen DeVoy */

#include <stdlib.h>
#include "extensions.h"
#include "game_extensions.h"

/* repeatedly pick random moves and a legal move is found */
int find_random_move_for_monkey(GAME *g, GAME_PIECE piece, MOVE *move) {
    CHECK_POINTER(g, "find_random_move_for_monkey (g)", return 0)
    CHECK_POINTER(move, "find_random_move_for_monkeys (move)", return 0)
    if (board_is_full(g)) {
        return 0;
    }
    char *monkey_behaviors[]
        = {"scratches armpits", "sways", "hoots", "beats chest"};
    PLAYER *current = g->players[piece];
    int row = rand() % 3, col = rand() % 3;
    printf("\n  * %s throw poop at board, hit ", current->name);
    while (g->board[row][col] != BLANK) {
        printf("unavailable square (%d, %d), %s *",
            row + 1, col + 1, monkey_behaviors[rand() % 4]);
        printf("\n  * %s throw poop at board, hit ", current->name);
        row = rand() % 3; col = rand() % 3;
    }
    printf("empty square (%d, %d), do flip *", row + 1, col + 1);
    move->row = row;
    move->col = col;
    return 1;
}

/* pick the first available legal move */
int find_first_available_move(GAME *g, GAME_PIECE piece, MOVE *move) {
    CHECK_POINTER(g, "find_first_available_move (g)", return 0)
    CHECK_POINTER(move, "find_first_available_move (move)", return 0)
    if (board_is_full(g)) {
        return 0;
    }
    MOVE* moves[MAX_MOVES];
    memset(moves, 0, 9 * sizeof(MOVE*));
    int count = all_available_moves(g, moves);
    if (count > 0) {
        move->row = moves[0]->row;
        move->col = moves[0]->col;
        free_moves(moves, count);
        return 1;
    }
    return 0;
}


/* if filling one position of the game's board with piece will result in a win for piece,
   update move to the row and col of the winning move and return 1; otherwise, return 0. */
int get_next_winning_move(GAME *g, MOVE **moves, int move_count, GAME_PIECE piece, MOVE *move) {
    CHECK_POINTER(g, "get_next_winning_move (g)", return 0)
#if DEBUG_GAME
    printf("\nget_next_winning_move(): move_count=%d, piece=%s\n",
        move_count, game_piece_to_string(piece));
#endif
    int mi;
    MOVE *current;
    GAME_PIECE winner;
    for (mi = 0; mi < move_count; mi++) {
        current = moves[mi];
        g->board[current->row][current->col] = piece;
        winner = winning_piece(g);
        g->board[current->row][current->col] = BLANK;
        if (winner == piece) {
            move->row = current->row;
            move->col = current->col;
#if DEBUG_GAME
            char mbuf[128];
            move_to_string(move, mbuf, 128);
            printf(" %s", mbuf);
#endif
            return 1;
        }
    }
    return 0;
}

/* evaluate all possible moves in terms of whether it could lead to a win.
   a move can lead to a win if it is on a tiplet without an opponent's piece
   on the triplet. The moves are ranked by whether piece would need to fill
   1, 2, or 3 of the available moves within the given triplet to win. The
   lower of needed fills to win, the higher the value of the move.
   updates move with the row and col of the easiest win potential and returns 1.
   If there are no wins thatcould lead to a win, it returns 0. */
int get_next_best_move(GAME *g, MOVE **moves, int move_count, GAME_PIECE piece, MOVE *move) {
    int max_win_potential = 0;
    int mi, tsi, ti, tsi1, ti1;
    MOVE *current;
    MOVE *max_move = 0;
    GAME_PIECE current_piece;
    int win_potential, saved_win_potential;
    MOVE *coords1, *coords2;
    for (mi = 0; mi < move_count; mi++) {
        win_potential = 0;
        current = moves[mi];
        for (tsi = 0; tsi < 8; tsi++) {
            for (ti = 0; ti < 3; ti++) {
                coords1 = &g->triplets[tsi][ti];
                if (current->row == coords1->row)
                    if (current->col == coords1->col) {
                        // Matches this triplet.
                        saved_win_potential = win_potential;
                        for (ti1 = 0; ti1 < 3; ti1++)
                            if (ti != ti1) {
                                coords2 = &g->triplets[tsi][ti1];
                                current_piece = g->board[coords2->row][coords2->col];
                                if (current_piece == BLANK)
                                    win_potential += 1;
                                else if (current_piece = piece) {
                                    win_potential += 2;
                                }
                                else {
                                    win_potential = saved_win_potential;
                                    break;
                                }
                            }
                    }
            }
        }
        if (win_potential > max_win_potential) {
            max_win_potential = win_potential;
            max_move = current;
        }
    }

    if (max_win_potential > 0) {
        move->row = max_move->row;
        move->col = max_move->col;
        return 1;
    }

    return 0;
}

/* uses author's own strategy to select the next move */
int autochoose_move(GAME *g, GAME_PIECE piece, MOVE *move) {
    GAME_PIECE opponent = OPPONENT(piece);
    MOVE* moves[MAX_MOVES];
    memset(moves, 0, 9 * sizeof(MOVE*));
    int number_of_possible_moves = all_available_moves(g, moves);
    int mi;
    int i;
#if DEBUG_GAME
    char mbuf[128];
    printf("\npossible moves(%d):\n", number_of_possible_moves);
    for (i = 0; i < number_of_possible_moves; i++) {
        move_to_string(moves[i], mbuf, 128);
        printf("  %s\n", mbuf);
    }
    printf("\n");
#endif

    if (number_of_possible_moves == 0) return 0;
    /* if piece could win in the next move, get the move and skip to end */
    if (! get_next_winning_move(g, moves, number_of_possible_moves, piece, move))
        /* if the opponent could win in the next move, get the move (to block it) and skip to the end */
        if (! get_next_winning_move(g, moves, number_of_possible_moves, opponent, move))
            /* get the next best move for the current player, and skip to the end */
            if (! get_next_best_move(g, moves, number_of_possible_moves, piece, move))
                /* get the next best move for the opponent (to block it) and skip to the end */
                if (! get_next_best_move(g, moves, number_of_possible_moves, opponent, move)) {
                    move->row = moves[0]->row;
                    move->col = moves[0]->col;
                }
    /* recycle moves */
    free_moves(moves, number_of_possible_moves);
    return 1;
}

/* create all extensions and register them */
void create_and_register_extensions() {
    EXTENSION *monkey_extension = create_extension("Poop throwing monkey",
        "Monkey", MONKEY, default_player_name_generator, find_random_move_for_monkey);
    EXTENSION *sloth_extension = create_extension("Sloth",
        "Sloth", SLOTH, default_player_name_generator, find_first_available_move);
    EXTENSION *strategic_extension = create_extension("Non AI strategic algorithm",
        "Computer", COMPUTER, default_player_name_generator, autochoose_move);
    
    /** DECLARE AND INITIALIZE A VARIABLE FOR EACH EXTENSION HERE! **/

    register_extension(monkey_extension);
    register_extension(sloth_extension);
    register_extension(strategic_extension);

    /** REGISTER EACH NEW EXTENSION HERE! **/
}
