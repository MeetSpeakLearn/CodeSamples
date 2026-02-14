#define MAX_NAME_SIZE 32
#define TEL_SIZE 11
#define EMAIL_SIZE 64
#define ORG_SIZE 64
#define STREET_NAME_SIZE 32
#define STREET_NUMBER_SIZE 8
#define STATE_OR_PROVINCE_SIZE 3
#define POSTAL_CODE_SIZE 11

typedef struct _CUSTOMER_STRUCT {
    /* We use strings for all values.
       A street number could be something like 10A.
       A postal code could be a zip code (US) or
       a Canadian postal code (which contains letters
       and digits). */
    char first[MAX_NAME_SIZE];
    char middle[MAX_NAME_SIZE];
    char last[MAX_NAME_SIZE];
    char telephone[TEL_SIZE];
    char email[EMAIL_SIZE];
    char organization[ORG_SIZE];
    char street[STREET_NAME_SIZE];
    char number1[STREET_NUMBER_SIZE];
    char number2[STREET_NUMBER_SIZE];
    char state_or_province[STATE_OR_PROVINCE_SIZE];
    char postal_code[POSTAL_CODE_SIZE];
} customer;

customer *new_customer(char* first, char* middle, char* last,
    char* telephone, char* email, char* organization,
    char* street, char* number1, char* number2,
    char* state_or_province, char* postal_code);

void free_customer(customer* cust);

