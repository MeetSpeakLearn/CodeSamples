#include <stdio.h>
#include <math.h>
#include <string.h>
#include <stdlib.h>

#define LEGAL_CHARS ".,0123456789"

long parseDollarAmountToCents(char *amtAsText) {
    if (amtAsText == 0) return -1;
    char *amtPtr = amtAsText;
    int fracLen = 0;

    /* remove all commas */
    char buffer[256];
    char *bufferPtr = buffer;
    memset(buffer, 0, 256);

    while (*amtPtr != '\0') {
        if (*amtPtr != ',')
            *bufferPtr++ = *amtPtr;
        amtPtr += 1;
    }

    /* does it contain only legal characters? */
    int i, l = strlen(buffer);
    int decimalPointCount = 0, decimalPointPos = -1;
    for (i = 0; i < l; i++) {
        if (strchr(LEGAL_CHARS, amtAsText[i]) == 0) {
            fprintf(stderr, "Illegal character in dollar amount: '%c'\n", amtAsText[i]);
            return -1;
        } else if (amtAsText[i] == '.') {
            decimalPointCount += 1;
        }
    }
    if (decimalPointCount > 1) {
        fprintf(stderr, "Only one decimal point is allowed.\n");
        return -1;
    }
    /* divide into whole part and fractional part */
    char wholePart[256], fractionalPart[256];
    char *substring = buffer;
    memset(wholePart, 0, 256);
    memset(fractionalPart, 0, 256);
    if (decimalPointCount == 1) {
        i = 0;
        while (*substring != '.')
            wholePart[i++] = *substring++;
        substring += 1;
        i = 0;
        while (*substring != '\0')
            fractionalPart[i++] = *substring++;
        fracLen = strlen(fractionalPart);
        if ((fracLen != 0) && (fracLen != 2)) {
            fprintf(stderr, "Invalid fractional part. 0 or 2 digits expected.\n");
            return -1;
        }
    } else
        strcpy(wholePart, amtAsText);
    if (fracLen == 0)
        return atol(wholePart) * 100;
    else
        return atol(wholePart) * 100 + atol(fractionalPart);
}

long monthly_payment(double yearly_interest_rate, long number_of_months, long priciple) {
    if (yearly_interest_rate == 0) {
        return (long) round((double) priciple / (double) number_of_months);
    } else {
        double monthly_interest_rate = yearly_interest_rate / 12.0;
        // return (long) round(monthly_interest_rate * priciple / (1 - (pow(1 + monthly_interest_rate, (-number_of_months * 12.0)))));
        // M = P [ i(1 + i)^n ] / [ (1 + i)^n – 1]
        return (long) round(priciple * (monthly_interest_rate * pow((monthly_interest_rate + 1), number_of_months))
                        / (pow((monthly_interest_rate + 1), number_of_months) - 1));
    }
}

#define PRINCIPLE_PROMPT "\n\nEnter the loan pinciple: $"
#define RATE_PROMPT "\nEnter the yearly interest rate as a percentage: "
#define MONTHS_PROMPT "\nEnter the number of months: "

#define CLEAR_INPUT_BUFFER { \
    int c; \
    while ((c = getchar()) != '\n' && c != EOF); \
}

int main() {
    char buffer[256];
    char *bufferPtr;
    long cents = 0;
    int months;
    double interestRateAsPercentage;
    int c;
    fprintf(stdout, PRINCIPLE_PROMPT);
    while (fgets(buffer, 256, stdin) > 0) {
        if ((bufferPtr = strchr(buffer, '\n')) != 0) {
            *bufferPtr = '\0';
        }
        cents = parseDollarAmountToCents(buffer);

        fprintf(stdout, RATE_PROMPT);
        scanf("%lf", &interestRateAsPercentage);

        fprintf(stdout, MONTHS_PROMPT);
        scanf("%d", &months);

        double payment = (double) monthly_payment(interestRateAsPercentage / 100.0, months, cents) / 100.0;
        fprintf(stdout, "\nMonthly Payment: $%.2f", payment);

        CLEAR_INPUT_BUFFER

        fprintf(stdout, PRINCIPLE_PROMPT);
    }
}