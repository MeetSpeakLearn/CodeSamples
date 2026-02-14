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

/* create all extensions and register them */
void create_and_register_extensions() {
    EXTENSION *monkey_extension = create_extension("Poop throwing monkey",
        "Monkey", MONKEY, default_player_name_generator, find_random_move_for_monkey);
    
    /** DECLARE AND INITIALIZE A VARIABLE FOR EACH EXTENSION HERE! **/

    register_extension(monkey_extension);

    /** REGISTER EACH NEW EXTENSION HERE! **/
}
