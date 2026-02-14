/* Author: Stephen DeVoy */

#include <stdlib.h>
#include "game4.h"
#include "player4.h"
#include "move.h"

char *GAME_PIECE_NAMES[] = {"X_PIECE", "O_PIECE", "BLANK"};

char PIECE_TO_CHAR[3] = {'X', 'O', '_'};

char *game_piece_to_string(GAME_PIECE gp) {
    return GAME_PIECE_NAMES[gp];
}

MOVE TRIPLET_TEMPLATE[8][3];

#if DEBUG_GAME
void print_triplet_template() {
    int ti, mi;
    char buffer[7];
    for (ti = 0; ti < 8; ti++) {
        printf("\n");
        for (mi = 0; mi < 3; mi++) {
            move_to_string(&TRIPLET_TEMPLATE[ti][mi], buffer, 7);
            if (mi != 0) printf(", ");
            printf("%s", buffer);
        }
    }
    printf("\n");
}
#endif
#if AI_GAME
BOARD *clone_board(BOARD b) {
    BOARD *nb = (BOARD*) malloc(sizeof(BOARD));
    memcpy(nb, b, sizeof(BOARD));
    return nb;
}
#endif
void fill_triplet_template() {
    int i, j, row, col;

    /* r1 through r3*/
    for (j = 0, row = 0; row < 3; j++, row++)
        for (i = 0, col = 0; col < 3; i++, col++) {
            TRIPLET_TEMPLATE[j][i].row = row;
            TRIPLET_TEMPLATE[j][i].col = col;
        }
    /* c1 through c3 */
    for (col = 0; col < 3; j++, col++)
        for (i = 0, row = 0; row < 3; i++, row++) {
            TRIPLET_TEMPLATE[j][i].row = row;
            TRIPLET_TEMPLATE[j][i].col = col;
        }
    /* d1 */
    for (i = 0, row = 0, col = 0; i < 3; i++, row++, col++) {
        TRIPLET_TEMPLATE[j][i].row = row;
        TRIPLET_TEMPLATE[j][i].col = col;
    }
    /* d2 */
    for (j++, i = 0, row = 0, col = 2; i < 3; i++, row++, col--) {
        TRIPLET_TEMPLATE[j][i].row = row;
        TRIPLET_TEMPLATE[j][i].col = col;
    }
}

#define MAX_MOVES 9

int all_available_moves_board(BOARD board, MOVE **moves_buffer) {
    CHECK_POINTER(board, "all_available_moves_board (board)", return 0)
    CHECK_POINTER(moves_buffer, "all_available_moves_board (moves_buffer)", return 0)
    int i, j, k = 0;
    for (i = 0; i < 3; i++)
        for (j = 0; j < 3; j++)
            if (board[i][j] == BLANK)
                if (k < MAX_MOVES)
                    moves_buffer[k++] = create_move(i, j);
                else break;
    char buffer[16];
    return k;
}

int all_available_moves(GAME *g, MOVE **moves_buffer) {
    CHECK_POINTER(g, "all_available_moves (g)", return 0)
    return all_available_moves_board(g->board, moves_buffer);
}

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

int autochoose_move(GAME *g, GAME_PIECE piece, MOVE *move) {
    GAME_PIECE opponent = OPPONENT(piece);
    MOVE* moves[9];
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

    if (! get_next_winning_move(g, moves, number_of_possible_moves, piece, move))
        if (! get_next_winning_move(g, moves, number_of_possible_moves, opponent, move))
            if (! get_next_best_move(g, moves, number_of_possible_moves, piece, move))
                if (! get_next_best_move(g, moves, number_of_possible_moves, opponent, move)) {
                    move->row = moves[0]->row;
                    move->col = moves[0]->col;
                }
    free_moves(moves, number_of_possible_moves);
    return 1;
}

#if AI_GAME
    int score_board(BOARD board, GAME_PIECE piece, int depth) {
        int ti; /* triplet index */
        GAME_PIECE opponent = OPPONENT(piece);
        MOVE *ct; /* current triplet */
        
        for (ti = 0; ti < 8; ti++) {
            ct = TRIPLET_TEMPLATE[ti];
            if (board[ct[0].row][ct[0].col] != BLANK) {
                if (board[ct[0].row][ct[0].col] == board[ct[1].row][ct[1].col]
                    && board[ct[1].row][ct[1].col] == board[ct[2].row][ct[2].col]) {
                    if (board[ct[0].row][ct[0].col] == piece)
                        return 10 - depth;
                    else
                        return depth - 10;
                }
            }
        }
        
       return 0;
    }

    int minmax_extended(BOARD board, GAME_PIECE piece, GAME_PIECE current, int depth, int win) {
        CHECK_POINTER(board, "minmax (board)", return 0)
        int best_score, mi;
        MOVE* moves[9];
        int score;
        MOVE *cm;
        GAME_PIECE is_there_winner = winning_piece_board(board);
        if (is_there_winner != BLANK)
            if (is_there_winner == piece)
                return 10 - depth;
            else
                return depth - 10;

        int number_of_possible_moves = all_available_moves_board(board, moves);

        if (number_of_possible_moves == 0) {
            free_moves(moves, number_of_possible_moves);
            return score_board(board, piece, depth);
        }
        if (piece == current) { // max
            best_score = (win) ? NEG_INFINITY : POS_INFINITY;
            for (mi = 0; mi < number_of_possible_moves; mi++) {
                cm = moves[mi];
                board[cm->row][cm->col] = current;
                score = minmax_extended(board, piece, OPPONENT(current), depth + 1, win);
                board[cm->row][cm->col] = BLANK;
                if (win)
                    if (score > best_score) best_score = score;
                else
                    if (score < best_score) best_score = score;
            }
        } else { // min
            best_score = POS_INFINITY;
            for (mi = 0; mi < number_of_possible_moves; mi++) {
                cm = moves[mi];
                board[cm->row][cm->col] = current;
                score = minmax_extended(board, piece, OPPONENT(current), depth + 1, win);
                board[cm->row][cm->col] = BLANK;
                if (score < best_score) best_score = score;
            }
        }

        free_moves(moves, number_of_possible_moves);
        return best_score;
    }

    int minmax(BOARD board, GAME_PIECE piece) {
        return minmax_extended(board, piece, piece, 0, 1);
    }

    int find_move_using_minmax(GAME *g, GAME_PIECE piece, MOVE *move, int win) {
        CHECK_POINTER(g, "find_best_move_using_minmax (g)", return 0)
        CHECK_POINTER(move, "find_best_move_using_minmax (move)", return 0)
        BOARD *b = &g->board;
        GAME_PIECE opponent = OPPONENT(piece);
        MOVE* moves[9];
        int number_of_possible_moves = all_available_moves_board(*b, moves);
        int best_value = (win) ? NEG_INFINITY : POS_INFINITY;
        int mi, score, best_move_index = -1;

        for (mi = 0; mi < number_of_possible_moves; mi++) {
            if (g->board[moves[mi]->row][moves[mi]->col] != BLANK) {
                fprintf(stderr, "\n*error, expected blank is not blank*");
                exit(0);
            }
            g->board[moves[mi]->row][moves[mi]->col] = piece;
            score = minmax_extended(g->board, piece, opponent, 0, win);
            g->board[moves[mi]->row][moves[mi]->col] = BLANK;
            if (win)
                if (score > best_value) {
                    best_value = score;
                    best_move_index = mi;
                }
            else
                if (score < best_value) {
                    best_value = score;
                    best_move_index = mi;
                }
        }

        if (best_move_index == -1)
            best_move_index = rand() % number_of_possible_moves;

        move->row = moves[best_move_index]->row;
        move->col = moves[best_move_index]->col;

        free_moves(moves, number_of_possible_moves);
        
        return 1;
    }

    int find_best_move_using_minmax(GAME *g, GAME_PIECE piece, MOVE *move) {
        return find_move_using_minmax(g, piece, move, 1);
    }

    int find_worst_move_using_minmax(GAME *g, GAME_PIECE piece, MOVE *move) {
        return find_move_using_minmax(g, piece, move, 0);
    }


#endif

int find_random_move_for_monkey(GAME *g, GAME_PIECE piece, MOVE *move) {
    CHECK_POINTER(g, "find_random_move_for_monkey (g)", return 0)
    CHECK_POINTER(move, "find_random_move_for_monkeys (move)", return 0)
    if (board_is_full(g)) {
        return 0;
    }
    char *monkey_behaviors[]
        = {"scratchs armpits", "sways", "hoots", "beats chest"};
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

void clear_game(GAME *g) {
    CHECK_POINTER(g, "clear_game (g)", return)
    int i, j;
    MOVE *m, *tm;
    for (i = 0; i < 3; i++)
        for (j = 0; j < 3; j++)
            g->board[i][j] = BLANK;
    g->turn = BLANK;
    for (i = X_PIECE; i <= O_PIECE; i++)
        g->players[i] = 0;
    for (i = 0; i < 8; i++)
        for (j = 0; j < 3; j++) {
            m = &g->triplets[i][j];
            tm = &TRIPLET_TEMPLATE[i][j];
            m->row = tm->row;
            m->col = tm->col;
        }
}

void print_board_board(BOARD board) {
    CHECK_POINTER(board, "print_board_board (board)", return)
    MOVE move;
    for (move.row = 0; move.row < 3; move.row++) {
        printf("\n");
        for (move.col = 0; move.col < 3; move.col++)
            printf("%c ", PIECE_TO_CHAR[board[move.row][move.col]]);
    }
}

void print_board(GAME* g) {
    CHECK_POINTER(g, "print_board (g)", return)
    MOVE move;
    for (move.row = 0; move.row < 3; move.row++) {
        printf("\n");
        for (move.col = 0; move.col < 3; move.col++)
            printf("%c ", PIECE_TO_CHAR[get_piece_at_pos(g, &move)]);
    }
}

int board_is_full_board(BOARD b) {
    CHECK_POINTER(b, "board_is_full_board (b)", return 1)
    int i, j;
    for (i = 0; i < 3; i++)
        for (j = 0; j < 3; j++)
            if (b[i][j] == BLANK) return 0;
    return 1;
}

int board_win_full_or_tie(BOARD b) {
    CHECK_POINTER(b, "board_win_full_or_tie (b)", return 1)
    int ti;
    MOVE *triplet;
    for (ti = 0; ti < 8; ti++) {
       triplet = TRIPLET_TEMPLATE[ti];
       if ((b[triplet[0].row][triplet[0].col] != BLANK)
            && (b[triplet[0].row][triplet[0].col] == b[triplet[1].row][triplet[1].col])
            && (b[triplet[1].row][triplet[1].col] == b[triplet[2].row][triplet[2].col]))
            return 1;
    }
    return board_is_full_board(b);
}

int board_is_full(GAME *g) {
    CHECK_POINTER(g, "board_is_full (g)", return 1)
    int i, j;
    for (i = 0; i < 3; i++)
        for (j = 0; j < 3; j++)
            if (g->board[i][j] == BLANK) return 0;
    return 1;
}

void assign_players(GAME* g, PLAYER *x, PLAYER *y) {
    CHECK_POINTER(g, "assign_players (g)", return)
    g->players[X_PIECE] = x;
    g->players[O_PIECE] = y;
    g->turn = X_PIECE;
}

void swap_players(GAME* g) {
    PLAYER *temp = g->players[X_PIECE];
    g->players[X_PIECE] = g->players[O_PIECE];
    g->players[O_PIECE] = temp;
}

PLAYER* get_player(GAME* g, GAME_PIECE piece) {
    CHECK_POINTER(g, "get_player (g)", return 0)
    if (piece == NO_PIECE) return 0;
    return g->players[piece];
}

GAME_PIECE three_in_a_triple_board(BOARD board, int triple) {
    CHECK_POINTER(board, "three_in_a_triple_board (board)", return BLANK)
    int piece_counts[3] = {0};
    int i, row, col;
    MOVE *m;
    for (i = 0; i < 3; i++) {
        m = &TRIPLET_TEMPLATE[triple][i];
        row = m->row;
        col = m->col;
        piece_counts[board[row][col]]++;
    }
    for (i = X_PIECE; (i <= O_PIECE); i++)
        if (piece_counts[i] == 3) return (GAME_PIECE) i;
    return BLANK;

}

GAME_PIECE three_in_a_triple(GAME* g, int triple) {
    CHECK_POINTER(g, "three_in_a_triple (g)", return BLANK)
    int piece_counts[3] = {0};
    int i, row, col;
    MOVE *m;
    for (i = 0; i < 3; i++) {
        m = &g->triplets[triple][i];
        row = m->row;
        col = m->col;
        piece_counts[g->board[row][col]]++;
    }
    for (i = X_PIECE; (i <= O_PIECE); i++)
        if (piece_counts[i] == 3) return (GAME_PIECE) i;
    return BLANK;
}

GAME_PIECE winning_piece_board(BOARD board) {
    CHECK_POINTER(board, "winning_piece (board)", return BLANK)
    int i;
    GAME_PIECE piece;
    if (board_is_full_board(board)) return NO_PIECE;
    for (i = 0; i < 8; i++) {
        piece = three_in_a_triple_board(board, i);
        if (piece != BLANK) return piece;
    }
    return BLANK;
}

GAME_PIECE winning_piece(GAME* g) {
    CHECK_POINTER(g, "winning_piece (g)", return BLANK)
    int i;
    GAME_PIECE piece;
    if (board_is_full(g)) return NO_PIECE;
    for (i = 0; i < 8; i++) {
        piece = three_in_a_triple(g, i);
        if (piece != BLANK) return piece;
    }
    return BLANK;
}

int make_move(GAME* g, MOVE *move) {
    CHECK_POINTER(g, "make_move (g)", 0)
    CHECK_POINTER(move, "make_move (move)", 0)
    if ((move->row < 0 || move->row > 2) || (move->col < 0 || move->col > 2)) return 0;
    if (g->board[move->row][move->col] != BLANK) {
        fprintf(stderr, "\n*Attempted to change a board piece.*\n");
        return 0;
    }
    g->board[move->row][move->col] = g->turn;
    return 1;
}

GAME_PIECE get_piece_at_pos(GAME* g, MOVE *move) {
    CHECK_POINTER(g, "get_piece_at_pos (g)", BLANK)
    CHECK_POINTER(move, "get_piece_at_pos (move)", 0)
    if ((move->row < 0 || move->row > 2) || (move->col < 0 || move->col > 2)) return BLANK;
    return g->board[move->row][move->col];
}

GAME_PIECE get_turn(GAME* g) {
    CHECK_POINTER(g, "get_turn (g)", BLANK)
    return g->turn;
}

void toggle_turn(GAME* g) {
    CHECK_POINTER(g, "get_turn (g)", BLANK)
    g->turn = (g->turn == X_PIECE) ? O_PIECE : X_PIECE;
}
