/* Author: Stephen DeVoy */

#include "game4.h"
#include "player4.h"
#include "move.h"

#ifndef EXTENSIONS_INCLUDED
#define EXTENSIONS_INCLUDED

#define MAX_EXTENSION_NAME_SIZE 64
#define MAX_PLAYER_TYPE_NAME_SIZE 32
#define MAX_EXTENSIONS 10
#define MAX_MENU_SIZE 2024

/* MOVE_PICKER is a type of function that picks a move */
typedef int (*MOVE_PICKER)(GAME *g, GAME_PIECE piece, MOVE *move);

/* PLAYER_NAME_GENERATOR is a type of function  that generates a name
   for a player of type player_type */
typedef void (*PLAYER_NAME_GENERATOR)(char *buffer, PLAYER_TYPE player_type);

/* EXTENSION is a type that represents and extension */
typedef struct extension_struct {
    char name[MAX_EXTENSION_NAME_SIZE];
    char player_type_name[MAX_PLAYER_TYPE_NAME_SIZE];
    PLAYER_TYPE type;
    PLAYER_NAME_GENERATOR generate_name;
    MOVE_PICKER pick_move;
} EXTENSION;

/* create an extension and return a pointer to it */
EXTENSION *create_extension(char *name, char *player_type_name, PLAYER_TYPE type,
    PLAYER_NAME_GENERATOR generate_name, MOVE_PICKER pick_move);
void destroy_extension(EXTENSION *ext);

/* EXTENSION_PTR is a type whose value represents an array of EXTENSION_PTR
   of length MAX_EXTENSIONS */
typedef EXTENSION *EXTENSION_PTR[MAX_EXTENSIONS];

/* extensions will be a global variable that holds all registered extensions */
extern EXTENSION_PTR extensions;

/* extension_count will be an index into the next position of extensions
   where a pointer to an extension may be saved */
extern int extension_count;

/* register the extension pointed to by ext by placing it into the
   array extensions at the next available position if there are
   more available positions */
int register_extension(EXTENSION *ext);

/* given name, look up the registered extension of that name and return
   a pointer to it, if it has been registed; otherwise, return 0. */
EXTENSION *get_extension_by_name(char *name);

/* given name, look up the registered extension of that type and return
   a pointer to it, if it has been registed; otherwise, return 0. */
EXTENSION *get_extension_by_player_type(PLAYER_TYPE type);

/* given player, look up the registered extension of the type of that
   player and return a pointer to it, if it has been registed; otherwise,
   return 0. */
EXTENSION *get_extension_by_player(PLAYER *player);

/* generate a text-based menu of all extensions numbered from 1 to
   the number of extensions. Returns the number of menu items. */
int menu_from_extensions(char *menu_buffer, int size);

/* free all registered extensions. next, set extension_count to 0. */
void free_extensions();

#endif