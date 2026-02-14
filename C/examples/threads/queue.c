/* Author: Stephen DeVoy*/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "macros.h"
#include "queue.h"
void init_queue(ARRAY_BASED_QUEUE *queue) {
    /* initailize a queue */
    CHECK_POINTER(init_queue, queue, return)
    queue->count = queue->start_index = queue->end_index = 0;
    memset(queue, 0, sizeof(ARRAY_BASED_QUEUE));
    pthread_mutex_init(&queue->mutex, NULL);
    pthread_cond_init(&queue->not_empty, NULL);
    pthread_cond_init(&queue->not_full, NULL);
    pthread_cond_init(&queue->is_empty, NULL);
}
ARRAY_BASED_QUEUE *create_queue() {
    ARRAY_BASED_QUEUE *new_queue = (ARRAY_BASED_QUEUE*) malloc(sizeof(ARRAY_BASED_QUEUE));
    init_queue(new_queue);
    return new_queue;
}
void free_queue(ARRAY_BASED_QUEUE *queue) {
    CHECK_POINTER(free_queue, queue, return)
    pthread_mutex_destroy(&queue->mutex);
    pthread_cond_destroy(&queue->not_empty);
    pthread_cond_destroy(&queue->not_full);
    pthread_cond_destroy(&queue->is_empty);
    free(queue);
}
void *enqueue(ARRAY_BASED_QUEUE *queue, void *element) {
    CHECK_POINTER2(enqueue, queue, element, return queue)
    /* wait for access to the queue
       and then grab it */
    pthread_mutex_lock(&queue->mutex);
    while (queue->count == QUEUE_MAX_SIZE) {
        /* signal that the queue is not full */
        pthread_cond_wait(&queue->not_full, &queue->mutex);
    }
    if (queue->count != QUEUE_MAX_SIZE) {
        /* add item to queue */
        queue->array[queue->end_index] = element;
        queue->end_index = QUEUE_INDEX_NEXT(queue->end_index);
        queue->count += 1;
    }
    /* if queue not empty, signal not empty */
    if (queue->count) pthread_cond_signal(&queue->not_empty);
    /* take our grubby hands off the queue
       so others can use it */
    pthread_mutex_unlock(&queue->mutex);
    return queue;
} 
void *dequeue(ARRAY_BASED_QUEUE *queue, void **element) {
    CHECK_POINTER2(dequeue, queue, element, return queue)
    pthread_mutex_lock(&queue->mutex);
    while (queue->count == 0) pthread_cond_wait(&queue->not_empty, &queue->mutex);
    if (! queue->count == 0) {
        *element = queue->array[queue->start_index];
        queue->array[queue->start_index++] = 0;
        queue->count -= 1;
        pthread_cond_signal(&queue->not_full);
    } else {
        *element = 0;
        pthread_cond_signal(&queue->is_empty);
    }

    pthread_mutex_unlock(&queue->mutex);
    return queue;
}
int queue_empty(ARRAY_BASED_QUEUE *queue) {
    CHECK_POINTER(queue_empty, queue, return 1)
    return queue->count == 0;
}