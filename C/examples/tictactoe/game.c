/* Author: Stephen DeVoy */

#include "game.h"

int TRIPLET_TEMPLATE[8][3][2]
        = { {{0, 0}, {0, 1}, {0, 2}}, /* r1 */
            {{1, 0}, {1, 1}, {1, 2}}, /* r2 */
            {{2, 0}, {2, 1}, {2, 2}}, /* r3 */
            {{0, 0}, {1, 0}, {2, 0}}, /* c1 */
            {{0, 1}, {1, 1}, {2, 1}}, /* c2 */
            {{0, 2}, {1, 2}, {2, 2}}, /* c3 */
            {{0, 0}, {1, 1}, {2, 2}}, /* d1 */
            {{0, 2}, {1, 1}, {2, 0}}  /* d2 */
        };

void clear_game(GAME* g) {
    CHECK_POINTER(g, "clear_game (g)", return)
    int i, j;
    for (i = 0; i < 3; i++)
        for (j = 0; j < 3; j++)
            g->board[i][j] = BLANK;
    g->turn = BLANK;
    for (i = X_PIECE; i <= O_PIECE; i++)
        g->players[i] = 0;
    for (i = 0; i < 8; i++)
        for (j = 0; j < 3; j++) {
            g->triplets[i][j][0] = TRIPLET_TEMPLATE[i][j][0];
            g->triplets[i][j][1] = TRIPLET_TEMPLATE[i][j][1];
        }
}

void assign_players(GAME* g, PLAYER *x, PLAYER *y) {
    CHECK_POINTER(g, "assign_players (g)", return)
    g->players[X_PIECE] = x;
    g->players[O_PIECE] = y;
    g->turn = X_PIECE;
}

PLAYER* get_player(GAME* g, GAME_PIECE piece) {
    CHECK_POINTER(g, "get_player (g)", return 0)
    return g->players[piece];
}

GAME_PIECE three_in_a_triple(GAME* g, int triple) {
    CHECK_POINTER(g, "three_in_a_triple (g)", return BLANK)
    int piece_counts[3] = {0};
    int i, row, col;
    for (i = 0; i < 3; i++) {
        row = g->triplets[triple][i][0];
        col = g->triplets[triple][i][1];
        piece_counts[g->board[row][col]]++;
    }
    for (i = X_PIECE; (i <= O_PIECE); i++)
        if (piece_counts[i] == 3) return (GAME_PIECE) i;
    return BLANK;
}

GAME_PIECE winning_piece(GAME* g) {
    CHECK_POINTER(g, "winning_piece (g)", return BLANK)
    int i;
    GAME_PIECE piece;
    for (i = 0; i < 8; i++) {
        piece = three_in_a_triple(g, i);
        if (piece != BLANK) return piece;
    }
    return BLANK;
}

int make_move(GAME* g, int row, int col) {
    CHECK_POINTER(g, "make_move (g)", 0)
    if ((row < 0 || row > 2) || (col < 0 || col > 2)) return 0;
    if (g->board[row][col] != BLANK) return 0;
    g->board[row][col] = g->turn;
    return 1;
}

GAME_PIECE get_piece_at_pos(GAME* g, int row, int col) {
    CHECK_POINTER(g, "get_piece_at_pos (g)", BLANK)
    if ((row < 0 || row > 2) || (col < 0 || col > 2)) return BLANK;
    return g->board[row][col];
}

GAME_PIECE get_turn(GAME* g) {
    CHECK_POINTER(g, "get_turn (g)", BLANK)
    return g->turn;
}

void toggle_turn(GAME* g) {
    CHECK_POINTER(g, "get_turn (g)", BLANK)
    g->turn = (g->turn == X_PIECE) ? O_PIECE : X_PIECE;
}
