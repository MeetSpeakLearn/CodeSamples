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


/* minmax uses this function to score board for which there are no
   remaining moves to be made. If there is a win on the board, it
   returns 10 - depth when the winner is piece, and depth - 10
   when the winner is the opponent. depth is the level of recursing.
   it represents how many plays deeper into the game board is at
   compared to the board when minmax was entered at the top level.
   the idea is that the deeper the final move, the less valuable
   it is because so many other decisions might be made along the
   way to make it less relavent. Additionally, it's better to grab
   victory earlier than later. */
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
/* this is the workhorse of minmax. board is the board to find the next move for.
   piece is the player's piece. current is used as the piece whose point of view
   we wish to assume. depth is how many moves have been explored since the toplevel
   call of minmax_extended. win is normally 1. It tells the function to try to find
   a winning move. If win is 0, it tries to find a bad move. when win is one, think
   of it as artificial intelligence. when win is 0, think of it as artificial stupidity. */
int minmax_extended(BOARD board, GAME_PIECE piece, GAME_PIECE current, int depth, int win) {
    CHECK_POINTER(board, "minmax (board)", return 0)
    int best_score, mi;
    MOVE* moves[MAX_MOVES];
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

/* the typical minmax. uses minmax_extended with default arguments. */
int minmax(BOARD board, GAME_PIECE piece) {
    return minmax_extended(board, piece, piece, 0, 1);
}

/* this is the ordinary minmax function. when win is 1, it attempts to
   try all possible moves, first maximizing for itself and alternately
   minimizing for the opponent. When win is 0, it finds the worst move
   my minimizing for itself. */
int find_move_using_minmax(GAME *g, GAME_PIECE piece, MOVE *move, int win) {
    CHECK_POINTER(g, "find_move_using_minmax (g)", return 0)
    CHECK_POINTER(move, "find_move_using_minmax (move)", return 0)
    BOARD *b = &g->board;
    GAME_PIECE opponent = OPPONENT(piece);
    MOVE* moves[MAX_MOVES];
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

/* find the best move for piece according to minmax */
int find_best_move_using_minmax(GAME *g, GAME_PIECE piece, MOVE *move) {
    return find_move_using_minmax(g, piece, move, 1);
}

/* find the worst move for piece according to minmax */
int find_worst_move_using_minmax(GAME *g, GAME_PIECE piece, MOVE *move) {
    return find_move_using_minmax(g, piece, move, 0);
}

/* create all extensions and register them */
void create_and_register_extensions() {
    EXTENSION *monkey_extension = create_extension("Poop throwing monkey",
        "Monkey", MONKEY, default_player_name_generator, find_random_move_for_monkey);
    EXTENSION *sloth_extension = create_extension("Sloth",
        "Sloth", SLOTH, default_player_name_generator, find_first_available_move);
    EXTENSION *strategic_extension = create_extension("Non AI strategic algorithm",
        "Computer", COMPUTER, default_player_name_generator, autochoose_move);
    EXTENSION *ai_extension = create_extension("MinMax AI Strategy",
        "AI", AI, default_player_name_generator, find_best_move_using_minmax);
    EXTENSION *as_extension = create_extension("Artificial Stupidity",
        "AS", AS, default_player_name_generator, find_worst_move_using_minmax);

    register_extension(monkey_extension);
    register_extension(sloth_extension);
    register_extension(strategic_extension);
    register_extension(ai_extension);
    register_extension(as_extension);
}
