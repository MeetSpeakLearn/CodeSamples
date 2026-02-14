#include <stdio.h>
#include <math.h>
#include <time.h>
#include <string.h>
#include <stdlib.h>

#ifdef _WIN32
/* For Windows OS */
#include <windows.h>
#define SLEEPINT Sleep(500);
#define CLEAR_SCREEN system("cls");
#else
/* For Unix and Linux OS */
#include <unistd.h>
#define SLEEPINT sleep(1);
#define CLEAR_SCREEN system("clear");
#endif

/* Macro to check whether value is in bounds.
   Note that VAR is evaluated twice. It's best
   to use a variable for this parameter. */
#define CHECK_BOUNDS(FUNC, VAR, UBOUND, RET) \
{if ((VAR < 0) || (VAR >= UBOUND)) {fprintf(stderr, "\n%s(): %s is out of bounds.", #FUNC, #VAR); RET;}}

/* Grid Constants */
#define GRID_WIDTH 64
#define GRID_HEIGHT 32
#define CELL_COUNT GRID_WIDTH * GRID_HEIGHT

/* LIFE_GRID is a type for a GRID_HEIGHT by GRID_WIDTH 2D array of type char. */
typedef char LIFE_GRID[GRID_HEIGHT][GRID_WIDTH];

/* Our three grids */
LIFE_GRID grid1, grid2, grid3;

/* Our grid pointers */
LIFE_GRID *previous_grid, *current_grid, *next_grid;

/* current_grid is set to next_grid, previous_grid is set to current_grid,
   next_grid is set to the former previous grid */
void rotate_grids() {
    if (current_grid == &grid1) {
        previous_grid = &grid1;
        current_grid = &grid2;
        next_grid = &grid3;
    } else if (current_grid == &grid2) {
        previous_grid = &grid2;
        current_grid = &grid3;
        next_grid = &grid1;
    } else {
        previous_grid = &grid3;
        current_grid = &grid1;
        next_grid = &grid2;
    }
}

/* tests whether the next grid is equal to the current_grid or the previous grid */
int repeated_grid() {
    if (memcmp(next_grid, current_grid, sizeof(LIFE_GRID)) == 0) return 1;
    if (memcmp(next_grid, previous_grid, sizeof(LIFE_GRID)) == 0) return 1;
    return 0;
}

/* The percentage of the board to randomly fill with life. */
#define INITIAL_DENSITY 0.2

/* clears the given grid */
void clear_grid(LIFE_GRID grid) {
    int i, j;
    for (i = 0; i < GRID_HEIGHT; i++)
        for (j = 0; j < GRID_WIDTH; j++)
            grid[i][j] = 0;
}

/* randomly fills the grid with life up to INTITIAL_DENSITY percent */
void randomly_fill_to_density() {
    double filled = 0.0;
    double cell_count = CELL_COUNT;
    clear_grid(grid1);
    while ((filled / cell_count) < INITIAL_DENSITY) {
        int row = floor(rand() % GRID_HEIGHT);
        int col = floor(rand() % GRID_WIDTH);
        if (! (grid1[row][col])) {
            grid1[row][col] = 1;
            filled += 1.0;
        }
    }
}

/* returns the index of the row below */
int inc_row(int row) { return (row + 1) % GRID_HEIGHT; }
/* returns the index of the row above */
int dec_row(int row) { return (row == 0) ?  GRID_HEIGHT - 1 : row - 1; }

/* returns the index of the column to the right */
int inc_col(int col) { return (col + 1) % GRID_WIDTH; }
/* returns the index of the column to the left */
int dec_col(int col) { return (col == 0) ?  GRID_WIDTH - 1 : col - 1; }

/* returns the number of living neighbors of the cell at (row, col) */
int count_neighbors(LIFE_GRID grid, int row, int col) {
    CHECK_BOUNDS(count_neighbors, row, GRID_HEIGHT, return 0)
    CHECK_BOUNDS(count_neighbors, col, GRID_WIDTH, return 0)
    int count = 0;
    int above_row = dec_row(row);
    int below_row = inc_row(row);
    int left_col = dec_col(col);
    int right_col = inc_col(col);
    if (grid[above_row][left_col]) count++;
    if (grid[above_row][col]) count++;
    if (grid[above_row][right_col]) count++;
    if (grid[row][left_col]) count++;
    if (grid[row][right_col]) count++;
    if (grid[below_row][left_col]) count++;
    if (grid[below_row][col]) count++;
    if (grid[below_row][right_col]) count++;
    return count;
}

/* display the grid */
void display_grid(LIFE_GRID grid) {
    int i, j;
    CLEAR_SCREEN
    printf("\n");
    for (i = 0; i < GRID_HEIGHT; i++) {
        printf("\n");
        for (j = 0; j < GRID_WIDTH; j++)
            if (grid[i][j])
                printf("*");
            else
                printf(".");
    }
}

/* apply the rules and produce next grid from current grid */
void produce_next_generation(LIFE_GRID current, LIFE_GRID next) {
    int i, j, count, live;
    for (i = 0; i < GRID_HEIGHT; i++)
        for (j = 0; j < GRID_WIDTH; j++) {
            count = count_neighbors(current, i, j);
            live = current[i][j];
            if (live)
                switch (count) {
                    case 0:
                    case 1:
                        next[i][j] = 0;
                        break;
                    case 2:
                    case 3:
                        next[i][j] = 1;
                        break;
                    default:
                        next[i][j] = 0;
                        break;
                }
            else if (count == 3)
                next[i][j] = 1;
            else
                next[i][j] = 0;
        }
}

/* when auto_mode is not 0, the game automatically progresses through
   generations without human intervention */
int auto_mode = 0;

/* returns 1 if the next generation should be produced and 0 to quit */
int get_user_input() {
    char input_buffer[256];
    char* result = fgets(input_buffer, 256, stdin);
    if (!result) return 0;
    int c = input_buffer[0];
    switch (c) {
        case '\n': return 1;                            /* next generation */
        case 'c': clear_grid(*current_grid); return 1;  /* clear and next generation */
        case 'q': return 0;                             /* quit */
        case 'a': auto_mode = 1; return 1;              /* automode and next generation */    
        case EOF: return 0;                             /* quit */
        default: return 0;                              /* quit */
    }
}

int main() {
    /* initialize */
    LIFE_GRID *temp_grid;
    int user_input;
    current_grid = &grid1;
    next_grid = &grid2;
    previous_grid = &grid3;
    /* seed the random number generator */
    srand(time(NULL));
    /* initialize the grid */
    randomly_fill_to_density();
    /* display grid */
    display_grid(*current_grid);
    /* while there are generations to generate */
    while ((auto_mode) ? 1 : get_user_input()) {
        /* derive a new generation from current generation */
        produce_next_generation(*current_grid, *next_grid);
        /* if we are in auto_mode, quit if the new generation is
           repetitive, otherwise continue. */
        if (auto_mode ? repeated_grid() : 0) break;
        /* if in auto_mode, sleep for a bit */
        if (auto_mode) SLEEPINT
        /* rotate grids */
        rotate_grids();
        /* display current grid */
        display_grid(*current_grid);
    }
    return 0;
}