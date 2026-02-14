/* Author: Stephen DeVoy */

#include <stdlib.h>
#include "game4.h"
#include "player4.h"
#include "move.h"
#include "extensions.h"

/* fulfill promise to declare GAME_PIECE_NAMES.
   indexed by GAME_PIECE. */
char *GAME_PIECE_NAMES[] = {"X_PIECE", "O_PIECE", "BLANK"};

/* fulfill promise to declare PIECE_TO_CHAR.
   indexed by GAME_PIECE. */
char PIECE_TO_CHAR[3] = {'X', 'O', '_'};

/* given a game_piece, returns the string describing it */
char *game_piece_to_string(GAME_PIECE gp) {
    return GAME_PIECE_NAMES[gp];
}

/* fulfill promise to declare TRIPLET_TEMPLATE */
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

/* returns a copy of board b. not currently used. */
BOARD *clone_board(BOARD b) {
    BOARD *nb = (BOARD*) malloc(sizeof(BOARD));
    memcpy(nb, b, sizeof(BOARD));
    return nb;
}

/* initializes a triplet template to all possible winning triplets */
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

/* given a board, fill moves_buffer with all available moves.
   return the count of available moves */
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
    return k;
}

/* given a game, fill moves_buffer with all available moves.
   return the count of available moves */
int all_available_moves(GAME *g, MOVE **moves_buffer) {
    CHECK_POINTER(g, "all_available_moves (g)", return 0)
    return all_available_moves_board(g->board, moves_buffer);
}

/* generate a random name for player. if there is an exension for player_type,
   use it for the name's stem */
void default_player_name_generator(char *buffer, PLAYER_TYPE player_type) {
    int random_suffix_as_int = rand() % 999999;
    EXTENSION *ext = get_extension_by_player_type(player_type);
    if (ext == 0)
        sprintf(buffer, "Player#%06d", random_suffix_as_int);
    else
        sprintf(buffer, "%s#%06d", ext->player_type_name, random_suffix_as_int);
}

/* initalize a game */
void clear_game(GAME *g) {
    CHECK_POINTER(g, "clear_game (g)", return)
    int i, j;
    MOVE *m, *tm;
    /* set all positions to BLANK */
    for (i = 0; i < 3; i++)
        for (j = 0; j < 3; j++)
            g->board[i][j] = BLANK;
    /* initialize turn to BLANK, when game is in play,
       turn is either X_PIECE or O_PIECE. */
    g->turn = BLANK;
    /* zero out pointers to players */
    for (i = X_PIECE; i <= O_PIECE; i++)
        g->players[i] = 0;
    /* copy the triplet template to the game's local copy.
       this is probably be eliminated. */
    for (i = 0; i < 8; i++)
        for (j = 0; j < 3; j++) {
            m = &g->triplets[i][j];
            tm = &TRIPLET_TEMPLATE[i][j];
            m->row = tm->row;
            m->col = tm->col;
        }
}

/* print character representation of board to stdout */
void print_board_board(BOARD board) {
    CHECK_POINTER(board, "print_board_board (board)", return)
    MOVE move;
    for (move.row = 0; move.row < 3; move.row++) {
        printf("\n");
        for (move.col = 0; move.col < 3; move.col++)
            printf("%c ", PIECE_TO_CHAR[board[move.row][move.col]]);
    }
}

/* print the game's character representation of board to stdout */
void print_board(GAME* g) {
    CHECK_POINTER(g, "print_board (g)", return)
    MOVE move;
    for (move.row = 0; move.row < 3; move.row++) {
        printf("\n");
        for (move.col = 0; move.col < 3; move.col++)
            printf("%c ", PIECE_TO_CHAR[get_piece_at_pos(g, &move)]);
    }
}

/* returns 1 if the board is full and 0 otherwise */
int board_is_full_board(BOARD b) {
    CHECK_POINTER(b, "board_is_full_board (b)", return 1)
    int i, j;
    for (i = 0; i < 3; i++)
        for (j = 0; j < 3; j++)
            if (b[i][j] == BLANK) return 0;
    return 1;
}

/* returns 1 if the game's board is full and 0 otherwise */
int board_is_full(GAME *g) {
    CHECK_POINTER(g, "board_is_full (g)", return 1)
    int i, j;
    for (i = 0; i < 3; i++)
        for (j = 0; j < 3; j++)
            if (g->board[i][j] == BLANK) return 0;
    return 1;
}

/* assigns x and y as players of g */
void assign_players(GAME* g, PLAYER *x, PLAYER *y) {
    CHECK_POINTER(g, "assign_players (g)", return)
    g->players[X_PIECE] = x;
    g->players[O_PIECE] = y;
    g->turn = X_PIECE;
}

/* returns the player of g with piece */
PLAYER* get_player(GAME* g, GAME_PIECE piece) {
    CHECK_POINTER(g, "get_player (g)", return 0)
    if (piece == NO_PIECE) return 0;
    return g->players[piece];
}

/* given board and triplet index triple, returns the game piece
   with occupying all three positions in the triplet, if there is
   such a piece, and blank if there are not. */
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

/* does the same as three_in_a_triple_board, but for the board pointed to
   by the game */
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

/* if there is a winner of board, returns a pointer to the winner; otherwise, 0 */
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

/* if there is a winner of game g's board, returns a pointer to the winner; otherwise, 0 */
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

/* fills the position at move with the game piece of the player
   whose turn it currently is */
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

/* returns the game piece of game g at position move */
GAME_PIECE get_piece_at_pos(GAME* g, MOVE *move) {
    CHECK_POINTER(g, "get_piece_at_pos (g)", BLANK)
    CHECK_POINTER(move, "get_piece_at_pos (move)", 0)
    if ((move->row < 0 || move->row > 2) || (move->col < 0 || move->col > 2)) return BLANK;
    return g->board[move->row][move->col];
}

/* returns the game piece of the player whose turn it currently is */
GAME_PIECE get_turn(GAME* g) {
    CHECK_POINTER(g, "get_turn (g)", BLANK)
    return g->turn;
}

/* if it is X's turn, change it to O's turn and vice versa */
void toggle_turn(GAME* g) {
    CHECK_POINTER(g, "get_turn (g)", BLANK)
    g->turn = (g->turn == X_PIECE) ? O_PIECE : X_PIECE;
}
