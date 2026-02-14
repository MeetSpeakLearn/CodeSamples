#include <pthread.h>
#include <unistd.h>

#define QUEUE_MAX_SIZE 128
#define NAME_MAX_SIZE 16
#define QUEUE_INDEX_NEXT(CURRENT) ((CURRENT + 1) % QUEUE_MAX_SIZE)
#define QUEUE_INDEX_PREV(CURRENT) ((CURRENT - 1) % QUEUE_MAX_SIZE)
#define PRINT_BUFFER_SIZE 128
#define CHECK_POINTER(FUNCNAME, PTR, RET) \
{if (PTR == 0) {fprintf(stderr, "\n%s(): %s is null.", #FUNCNAME, #PTR); RET;}}

typedef enum {QUEUEABLE_TYPE, CUSTOMER_TYPE, TELLER_TYPE} QE_TYPE;

typedef struct array_based_queue {
    int count;                    /*items in queue */
    int start_index;              /* head of queue */
    int end_index;                /* end of queue */
    void *array[QUEUE_MAX_SIZE];  /* queue */
    pthread_t enqueue_thread;     /* thread for enqueuing */
    pthread_t dequeue_thread;     /* thread for dequeuing */
    pthread_mutex_t mutex;        /* semiphore */
    pthread_cond_t not_empty;     /* signals not empty */
    pthread_cond_t not_full;      /* signals not full */
} ARRAY_BASED_QUEUE;

/* initialize queue */
void init_queue(ARRAY_BASED_QUEUE *queue);
/* create a new queue */
ARRAY_BASED_QUEUE *create_queue();
/* frees queue's memory */
void free_queue(ARRAY_BASED_QUEUE *queue);
/* enqueue item in queue */
void *enqueue(ARRAY_BASED_QUEUE *queue, void *element);
/* dequeue item from queue */
void *dequeue(ARRAY_BASED_QUEUE *queue, void **element);
/* is queue empty? */
int queue_empty(ARRAY_BASED_QUEUE *queue);

/* all queueable structures must begin with these fields
   . type indicates the kind of queueable.
   . freer is a pointer to the function that frees
   this kind of queueable
   . to_string is a pointer to a function that
   prints this kind of queueable.
   . thread is used if this queueable has its own behavior.
   . active is 1 if thread is running and 0 if not */
#define ESSENTIAL_QUEUEABLE_FIELDS \
    QE_TYPE type;\
    void (*freer)(void *);\
    void (*to_string)(void *, char *, int);\
    pthread_t thread;\
    int active;

/* used when element's type is not known */
typedef struct queueable {
    ESSENTIAL_QUEUEABLE_FIELDS
} QUEUEABLE;
/* returns enum indicating type of structure */
#define GET_QUEUEABLE_TYPE(QUEUEABLE_T) (QUEUEABLE_T)->type

/* dequeues a queueable structure */
void *dequeue_queueable(ARRAY_BASED_QUEUE *queue, QUEUEABLE **element);
/* enqueues a queueable structure */
void *enqueue_queueable(ARRAY_BASED_QUEUE *queue, QUEUEABLE *element);
/* prints a queue of queueable items */
void print_queue_of_queueables(ARRAY_BASED_QUEUE *queue);
/* saves readable representation in buffer */
void queueable_to_string(void *q, char *buffer, int len);

typedef struct customer {
    /* allows customer to be handled as a queueable */
    ESSENTIAL_QUEUEABLE_FIELDS
    char name[NAME_MAX_SIZE]; /* customer's name */
    time_t entered;           /* time customer entered queue */
    time_t served;            /* time cusomter served */
} CUSTOMER;

/* release customer memory */
void free_customer(void *q);
/* save readable representation in buffer */
void customer_to_string(void *q, char *buffer, int len);
/* allocate a customer */
CUSTOMER *create_customer(char *name);

typedef struct teller {
    /* allows customer to be handled as a queueable */
    ESSENTIAL_QUEUEABLE_FIELDS
    char name[NAME_MAX_SIZE]; /* teller's name */
    int working;              /* is teller working */
    char *behavior;           /* what the teller is doing */
    time_t started;           /* time teller began working */
    time_t ended;             /* time teler finished working */
    CUSTOMER* serving;        /* what customer is being served? */
    int served;
} TELLER;

/* deallocate teller */
void free_teller(void *q);
/* store text representation of teller into buffer */
void teller_to_string(void *q, char *buffer, int len);
/* allocate a teller */
TELLER *create_teller(char *name);
/* turn on teller behavior */
int activate_teller (TELLER *teller);
/* turn off teller behavior */
int deactivate_teller (TELLER *teller);

