/* Author: Stephen DeVoy */

#ifndef PLAYER_INCLUDED
#define PLAYER_INCLUDED

/* Maximum size of player name */
#define MAX_NAME_SIZE 64

#include <string.h>

/* player types. These are used to determine which extension is used to
   choose moves of a player. The exception is HUMAN which is built into
   the game. END_OF_PLAYER_TYPES marks the end of the enumeration and
   can be used in a for-loop if one is enumarating all types. You
   are free to refedine the names of all player types beginning with
   EXT1. Use meaningful name if you wish. You may also insert new
   types before END_OF_PLAYER_TYPES. */
typedef enum {NOT_A_PLAYER_TYPE, HUMAN, COMPUTER, AI, AS, MONKEY,
    SLOTH, EXT1, EXT2, EXT3, EXT4, END_OF_PLAYER_TYPES}
PLAYER_TYPE;

/* returns a string representation for the player type.
   this is being phased out in favor of using the name
   provided by the extension. It is currently used only
   for the HUMAN type (which has no extension). */
char *player_type_to_string(PLAYER_TYPE type);

/* PLAYER represents a player. */
typedef struct PLAYER_STRUCT {
    char name[MAX_NAME_SIZE];       /* name of player */
    int wins;                       /* win count of player */
    PLAYER_TYPE type;               /* player type */
} PLAYER;

/* initializes player p to have an empty string as a name,
   a win count of 0, and a player type of HUMAN */
void init_player(PLAYER *p);

/* reinitializes player p to have an empty string as a name,
   a win count of 0, and a player type of HUMAN */
void reset_player(PLAYER* p);

/* increaments the player's wins by 1 */
void add_win(PLAYER* p);

/* retrieves the player's name, wins, and type. the player's
   name is copied into the buffer name */
int get_player_info(PLAYER* p, char* name, int* wins, PLAYER_TYPE *type);

#endif

