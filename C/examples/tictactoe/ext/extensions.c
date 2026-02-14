/* Author: Stephen DeVoy */

#include <stdlib.h>
#include <string.h>
#include <stdio.h>
#include "game4.h"
#include "player4.h"
#include "move.h"
#include "extensions.h"

/* keep the promise of declaring extensions and extensions count */
EXTENSION_PTR extensions;
int extension_count = 0;

/* create an extension and return a pointer to it */
EXTENSION *create_extension(char *name, char *player_type_name, PLAYER_TYPE type,
    PLAYER_NAME_GENERATOR generate_name, MOVE_PICKER pick_move) {
    EXTENSION *new_ext = (EXTENSION*) malloc(sizeof(EXTENSION));
    strncpy(new_ext->name, name, MAX_EXTENSION_NAME_SIZE);
    strncpy(new_ext->player_type_name, name, MAX_PLAYER_TYPE_NAME_SIZE);
    new_ext->type = type;
    new_ext->generate_name = generate_name;
    new_ext->pick_move = pick_move;
    return new_ext;
}

/* free the extension */
void destroy_extension(EXTENSION *ext) {
    if (ext == 0) return;
    free(ext);
}

/* register the extension pointed to by ext by placing it into the
   array extensions at the next available position if there are
   more available positions */
int register_extension(EXTENSION *ext) {
    if (ext == 0) return 0;
    if (extension_count >= MAX_EXTENSIONS) return 0;
    /* cannot register an extension more than once */
    if (get_extension_by_name(ext->name) != 0) return 0;
    if (get_extension_by_player_type(ext->type) != 0) return 0;
    extensions[extension_count++] = ext;
}

/* given name, look up the registered extension of that name and return
   a pointer to it, if it has been registed; otherwise, return 0. */
EXTENSION *get_extension_by_name(char *name) {
    if (name == 0) return 0;
    int i;
    for (i = 0; i < extension_count; i++)
        if (strncmp(extensions[i]->name, name, MAX_PLAYER_TYPE_NAME_SIZE) == 0)
            return extensions[i];
    return 0;
}

/* given name, look up the registered extension of that type and return
   a pointer to it, if it has been registed; otherwise, return 0. */
EXTENSION *get_extension_by_player_type(PLAYER_TYPE type) {
    int i;
    for (i = 0; i < extension_count; i++)
        if (extensions[i]->type == type)
            return extensions[i];
    return 0;
}

/* given player, look up the registered extension of the type of that
   player and return a pointer to it, if it has been registed; otherwise,
   return 0. */
EXTENSION *get_extension_by_player(PLAYER *player) {
    if (player == 0) return 0;
    return get_extension_by_player_type(player->type);
}

/* generate a text-based menu of all extensions numbered from 1 to
   the number of extensions. Returns the number of menu items. */
int menu_from_extensions(char *menu_buffer, int size) {
    if (menu_buffer == 0) return 0;
    int max_size = (size > MAX_MENU_SIZE) ? MAX_MENU_SIZE : size;
    int i;
    char *buf_ptr = menu_buffer;
    if (extension_count == 0) {
        menu_buffer[0] = '\0';
        return 0;
    }
    for (i = 0; i < extension_count; i++) {
        sprintf(buf_ptr, "(%d): %s\n", i + 1, extensions[i]->name);
        buf_ptr += strlen(buf_ptr);
    }
    return extension_count;
}

/* free all registed extensions. next, set extension_count to 0. */
void free_extensions() {
    int i;
    for (i = 0; i < extension_count; i++) {
        destroy_extension(extensions[i]);
        extensions[i] = 0; /* let's not leave rogue pointers around */
    }
    extension_count = 0;
}