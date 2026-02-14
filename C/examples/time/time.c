#include <stdlib.h>
#include <stdio.h>

#define WEEK 7
char *days_of_week[WEEK] = {"MONDAY", "TUESDAY", "WEDNESDAY",
    "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"};
char *random_day() {
    int index = rand() % WEEK;
    return days_of_week[index];
}
int dice_roll_counts[11] = {0};

int roll_dice() {
    return ((rand() % 6) + 1) + ((rand() % 6) + 1);
}


int main() {
    int i;
    double sum = 0.0;
    for (i = 0; i < 10000000; i++)
        dice_roll_counts[roll_dice() - 2]++;
    for (i = 0; i < 11; i++) {
        printf("\n%d: %f", i + 2, dice_roll_counts[i] / 10000000.0);
        sum += dice_roll_counts[i] / 10000000.0;
    }
    printf("\n\nTotal: %f\n", sum);
    return 0;
}