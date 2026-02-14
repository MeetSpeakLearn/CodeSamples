#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <time.h>
#include "bank.h"

/* the queue */
ARRAY_BASED_QUEUE *queue = 0;
/* convert a queueable to a tring */
void queueable_to_string(void *q, char *buffer, int len) {
    CHECK_POINTER(queueable_to_string, q, return)
    QUEUEABLE* queueable = (QUEUEABLE*) q;
    /* call the to_string function for this queueable */
    void (*f)(void *, char *, int) = queueable->to_string;
    /* there is no to_string function, so empty string */
    if (f == 0) { buffer[0] = '\0'; return; }
    switch (GET_QUEUEABLE_TYPE(queueable)) {
        case QUEUEABLE_TYPE:
        case CUSTOMER_TYPE:
        case TELLER_TYPE:
            /* queueable is a type that has to_string function,
               call it */
            (f)(q, buffer, len);
            break;
        default:
            /* not a type that has to_string function,
               make string the empty string */
            buffer[0] = '\0';
            break;
    }
}
void free_customer(void *q) {
    /* cast to a customer */
    CUSTOMER *customer = (CUSTOMER*) q;
    CHECK_POINTER(free_customer, customer, return)
    /* clear out name */
    memset(customer->name, 0, NAME_MAX_SIZE * sizeof(char));
    /* clear out other fields */
    customer->entered = customer->served = 0;
    customer->to_string = 0;
    customer->freer = 0;
    free(customer); /* deallocate */
}
void customer_to_string(void *q, char *buffer, int len) {
    CHECK_POINTER(customer_to_string, q, return)
    CHECK_POINTER(customer_to_string, buffer, return)
    CUSTOMER *customer = (CUSTOMER*) q; /* cast to CUSTOMER* */
    char temp[PRINT_BUFFER_SIZE];
    /* write text rep to temp buffer */
    sprintf(temp, "%s - %lds", customer->name, (long int) time(0) - customer->entered);
    /* copy the temp buffer to buffer */
    strncpy(buffer, temp, len);
}
CUSTOMER *create_customer(char *name) {
    CHECK_POINTER(create_customer, name, return 0)
    /* allocate */
    CUSTOMER *new_customer = (CUSTOMER*) malloc(sizeof(CUSTOMER));
    /* initialize */
    new_customer->type = CUSTOMER_TYPE;
    new_customer->active = 0;
    if (name)
        strncpy(new_customer->name, name, NAME_MAX_SIZE);
    else
        memset(new_customer->name, 0, NAME_MAX_SIZE * sizeof(char));
    new_customer->freer = free_customer; /* func to free customer */
    /* func to convert customer to string */
    new_customer->to_string = customer_to_string;
    new_customer->entered = time(0);
    new_customer->served = 0;
    return new_customer;
}
/* random idle behaviors */
#define IDLE_TELLER_BEHAVIOR_COUNT 10
char *idle_teller_behavior[IDLE_TELLER_BEHAVIOR_COUNT]
        = {"*suffling papers*", "*brushing hair*", "*cat videos*", "*texting*", "*looking busy*",
           "*twiddling thumbs*", "*rubbing nose*", "*yawning*", "*fidgeting*", "*day dreaming*"};
char *random_idling_behavior() {
    return idle_teller_behavior[rand() % IDLE_TELLER_BEHAVIOR_COUNT];
}
/* random work behaviors */
#define WORKING_TELLER_BEHAVIOR_COUNT 6
char *working_teller_behavior[WORKING_TELLER_BEHAVIOR_COUNT]
        = {"counting money", "typing", "placing money in cash drawer",
           "taking money out of cash drawer", "printing receipt", "consulting boss"};
char *random_working_behavior() {
    return working_teller_behavior[rand() % WORKING_TELLER_BEHAVIOR_COUNT];
}
void free_teller(void *q) {
    TELLER *teller = (TELLER*) q; /* cast to TELLER* */
    CHECK_POINTER(free_teller, teller, return)
    /* clear the name */
    memset(teller->name, 0, NAME_MAX_SIZE * sizeof(char));
    /* clear remaining fields */
    teller->behavior = 0;
    teller->started = 0;
    teller->ended = 0;
    teller->serving = 0;
    teller->served = 0;
    teller->to_string = 0;
    teller->freer = 0;
    free(teller); /* deallocate */
}
void teller_to_string(void *q, char *buffer, int len) {
    CHECK_POINTER(teller_to_string, q, return)
    CHECK_POINTER(teller_to_string, buffer, return)
    TELLER *teller = (TELLER*) q;
    char temp[3 * PRINT_BUFFER_SIZE];
    char customer_buffer[PRINT_BUFFER_SIZE];
    char *behavior = (teller->behavior) ? teller->behavior : "";
    if (teller->serving != 0) {
        customer_to_string(teller->serving, customer_buffer, PRINT_BUFFER_SIZE);
    } else {
        customer_buffer[0] = '\0';
    }
    sprintf(temp, "[%s (%d served) %s]: %s", teller->name, teller->served, behavior, customer_buffer);
    strncpy(buffer, temp, len);
}
TELLER *create_teller(char *name) {
    CHECK_POINTER(create_teller, name, return 0)
    /* allocate */
    TELLER *new_teller = (TELLER*) malloc(sizeof(TELLER));
    /* initialize */
    new_teller->type = TELLER_TYPE;
    new_teller->active = 0;
    new_teller->working = 0;
    if (name)
        strncpy(new_teller->name, name, NAME_MAX_SIZE);
    else
        memset(new_teller->name, 0, NAME_MAX_SIZE * sizeof(char));
    new_teller->freer = free_teller;
    new_teller->to_string = teller_to_string;
    new_teller->behavior = 0;
    new_teller->started = time(0);
    new_teller->ended = 0;
    new_teller->serving = 0;
    new_teller->served = 0;
    return new_teller;
}
#define CLOSE_BANK_IDLE_DURATION 20
/* teller's behavior */
void *do_teller(void *p) {
    /* the teller drives the simulation */
    CHECK_POINTER(do_teller, p, return 0)
    QUEUEABLE *customer;
    char buffer[512];
    int take_a_break = 0;
    TELLER *teller = (TELLER*) p;
    long int idle_time_start = 0;
    while (teller->active) {
        /* while the teller is active */
        if (teller->serving == 0) {
            /* if the teller isn't serving anyone */
            if (queue) {
                if (queue_empty(queue)) {
                    /* if the queue is empty, start
                       monitoring idle time */
                    if (idle_time_start == 0)
                        idle_time_start = time(0);
                    /* can't work if no customers */
                    teller->working = 0;
                } else {
                    /* queue not empty, get next customer */
                    dequeue_queueable(queue, &customer);
                    idle_time_start = 0; /* not idling anymore */
                    teller->serving = (CUSTOMER *) customer;
                    teller->working = 1;
                    teller->served += 1;
                    /* teller will take a break after every
                       10 customers */
                    take_a_break = (teller->served % 10) == 9;
                }
            }
        }
        /* pick a random behavior per teller's status */
        teller->behavior
            = (teller->working)
                ? random_working_behavior()
                : random_idling_behavior();
        /* print teller and everyone in the queue */
        teller_to_string(p, buffer, 512);
        printf("\n\nTeller: %s\n", buffer);
        if (queue != 0) {
            print_queue_of_queueables(queue);
        }
        /* time to prepare for next customer */
        teller->serving = 0;
        teller->working = 0;
        if (take_a_break) {
            /* it's time for a break */
            teller->behavior = random_idling_behavior();
            teller_to_string(p, buffer, 512);
            printf("\n\nTeller on break: %s\n", buffer);
            print_queue_of_queueables(queue);
            sleep(5);
        } else
            sleep(rand() % 3);

        if (customer != 0) {
            /* if we've finished with a customer,
               deallocate the customer */
            free_customer(customer);
            customer = 0;
        }
        /* if the queue has been empty long enough,
           the teller is done working for today */
        if (idle_time_start && (idle_time_start + CLOSE_BANK_IDLE_DURATION <= time(0)))
            break;
    }
}
int activate_teller (TELLER *teller) {
    CHECK_POINTER(activate_teller, teller, return 1)
    if (teller->active) return 1; /* already active */
    teller->active = 1;
    /* spawn the teller's behavior thread */
    if (pthread_create(&(teller->thread), NULL, do_teller, (void*) teller) != 0) {
        perror("Failed to create teller thread");
        return EXIT_FAILURE;
    }
    return 0;
}

int deactivate_teller (TELLER *teller) {
    CHECK_POINTER(deactivate_teller, teller, return 1)
    if (teller->active) {
        /* if the teller is active, wait until
           the teller's behavior is finished */
        if (pthread_join(teller->thread, NULL) != 0) {
            perror("Failed to join teller thread");
            return EXIT_FAILURE;
        }
        /* set the teller to inactive */
        teller->active = 0;
        /* announce that the bank is closed */
        printf("\n\n*Bank closed!*\n");
    }
    return 0;
}
void print_queue_of_queueables(ARRAY_BASED_QUEUE *queue) {
    CHECK_POINTER(print_queue_of_queueables, queue, return)
    char buffer[PRINT_BUFFER_SIZE];
    int i;
    char *delimiter = "";
    if (queue->count == 0) return;
    for (i = queue->start_index; i != queue->end_index; i = QUEUE_INDEX_NEXT(i)) {
        /* walk through the queue...*/
        if (queue->array[i] != 0)
            if (((QUEUEABLE*) queue->array[i])->to_string != 0) {
                /* print the item in the queue */
                (((QUEUEABLE*) queue->array[i])->to_string)(queue->array[i], buffer, PRINT_BUFFER_SIZE);
                printf("%s%s", delimiter, buffer);
                delimiter = "\n";
            }
    }
}
void init_queue(ARRAY_BASED_QUEUE *queue) {
    /* initailize a queue */
    CHECK_POINTER(init_queue, queue, return)
    queue->count = queue->start_index = queue->end_index = 0;
    memset(queue, 0, sizeof(ARRAY_BASED_QUEUE));
    pthread_mutex_init(&queue->mutex, NULL);
    pthread_cond_init(&queue->not_empty, NULL);
    pthread_cond_init(&queue->not_full, NULL);
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
    free(queue);
}
void *enqueue(ARRAY_BASED_QUEUE *queue, void *element) {
    CHECK_POINTER(enqueue, queue, return queue)
    CHECK_POINTER(enqueue, element, return queue)
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
void *enqueue_queueable(ARRAY_BASED_QUEUE *queue, QUEUEABLE *element) {
    CHECK_POINTER(enqueue_queueable, queue, return queue)
    CHECK_POINTER(enqueue_queueable, element, return queue)
    /* enqueue a queueable */
    return enqueue(queue, element);
} 
void *dequeue(ARRAY_BASED_QUEUE *queue, void **element) {
    CHECK_POINTER(dequeue, queue, return queue)
    CHECK_POINTER(dequeue, element, return queue)
    pthread_mutex_lock(&queue->mutex);
    while (queue->count == 0) {
        pthread_cond_wait(&queue->not_empty, &queue->mutex);
    }
    if (! queue->count == 0) {
        *element = queue->array[queue->start_index];
        queue->array[queue->start_index++] = 0;
        queue->count -= 1;
        pthread_cond_signal(&queue->not_full);
    } else
        *element = 0;

    pthread_mutex_unlock(&queue->mutex);
    return queue;
}
void *dequeue_queueable(ARRAY_BASED_QUEUE *queue, QUEUEABLE **element) {
    CHECK_POINTER(dequeue_queueable, queue, return queue)
    CHECK_POINTER(dequeue_queueable, element, return queue)
    /* dequeue a queueable */
    void *result = dequeue(queue, (void*) element);
    return result;
}
int queue_empty(ARRAY_BASED_QUEUE *queue) {
    CHECK_POINTER(queue_empty, queue, return 1)
    return queue->count == 0;
}
/* data for random names */
#define FIRST_NAME_COUNT 20
char *given_names[FIRST_NAME_COUNT]
    = {
        "Irene", "Lela", "Michelle", "Yoko", "Xochitl",
        "Zhi", "Estefania", "Carmen", "Yana", "Aphrodite",
        "Ares", "Steve", "Alvaro", "Paul", "Vasilius",
        "Sean", "Kyle", "Liam", "Dov", "Alex"
    };
/* copy a random name into buffer */
char *random_name(char *buffer, int len) {
    CHECK_POINTER(random_name, buffer, return buffer)
    char temp[32];
    int gni = rand() % FIRST_NAME_COUNT;
    int fn = 'A' + rand() % 26;
    sprintf(temp, "%s %c", given_names[gni], fn);
    strncpy(buffer, temp, len);
    return buffer;
}
/* provide a stream of customers to the queue */
void *fill_queue(void *q) {
    ARRAY_BASED_QUEUE *queue = (ARRAY_BASED_QUEUE*) q;
    CHECK_POINTER(fill_queue, queue, return NULL)
    char buffer[32];
    int interval, count = 0;
    while (count++ < QUEUE_MAX_SIZE) {
        CUSTOMER *new_customer = create_customer(random_name(buffer, 32));
        enqueue_queueable(queue, (QUEUEABLE*) new_customer);
        interval = rand() % 3;
        sleep(interval);
    }
    return NULL;
}
/* main routine */
int main() {
    char buffer[NAME_MAX_SIZE];
    srand(time(0)); /* seed pseudorandom number generator */
    queue = create_queue(); /* allocate a queue */
    random_name(buffer, NAME_MAX_SIZE); /* get a random name */
    TELLER *teller = create_teller(buffer); /* create a teller */
    /* spawn threat fo fill queue */
    if (pthread_create(&(queue->enqueue_thread), NULL, fill_queue, (void*) queue) != 0) {
        perror("Failed to create thread");
        return EXIT_FAILURE;
    }
    activate_teller(teller); /* activate teller */
    printf("\nActivated teller.\n");
    if (pthread_join(queue->enqueue_thread, NULL) != 0) {
        /* wait for customers */
        perror("Failed to join thread");
        return EXIT_FAILURE;
    }
    deactivate_teller(teller); /* wait for teller to finish */
    free_queue(queue); /* dellocate queue */
    queue = 0;
    free_teller(teller); /* deallocate teller */
    teller = 0;
    return 0;
}