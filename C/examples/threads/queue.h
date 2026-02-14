/* Author: Stephen DeVoy*/

#ifndef QUEUE_HEADER
#define QUEUE_HEADER
#include <pthread.h>
#include <unistd.h>
#define QUEUE_MAX_SIZE 128
#define NAME_MAX_SIZE 16
#define QUEUE_INDEX_NEXT(CURRENT) ((CURRENT + 1) % QUEUE_MAX_SIZE)
#define QUEUE_INDEX_PREV(CURRENT) ((CURRENT - 1) % QUEUE_MAX_SIZE)
#define PRINT_BUFFER_SIZE 128
typedef struct array_based_queue {
    int count;                    /*items in queue */
    int start_index;              /* head of queue */
    int end_index;                /* end of queue */
    void *array[QUEUE_MAX_SIZE];  /* queue */
    pthread_t enqueue_thread;     /* thread for enqueuing */
    pthread_t dequeue_thread;     /* thread for dequeuing */
    pthread_attr_t enqueue_thread_attr;
    pthread_attr_t dequeue_thread_attr;
    pthread_mutex_t mutex;        /* semiphore */
    pthread_cond_t not_empty;     /* signals not empty */
    pthread_cond_t not_full;      /* signals not full */
    pthread_cond_t is_empty;      /* signals empty */
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
#endif