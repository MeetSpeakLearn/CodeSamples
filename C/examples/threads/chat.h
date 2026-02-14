/* Author: Stephen DeVoy */

#ifndef CHAT_HEADER
#define CHAT_HEADER
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <semaphore.h>
#include <pthread.h>
#include <unistd.h>
#define MAX_NAME_SIZE 32
#define MAX_MESSAGE_SIZE 2048
#define MAX_CONCURRENT_ACCESS 6
/* Promise participant_semaphore will be defined */
extern sem_t participant_semaphore;
/* Represents chat room participant */
typedef struct participant {
    int id;                     /* unique */
    char name[MAX_NAME_SIZE];   /* name */
    int socket_descriptor;      /* socket for com with part */
    int alive;                  /* part's thread running */
    pthread_t thread;           /* part's thread */
    pthread_attr_t thread_attr; /* type of thread */
    pthread_mutex_t mutex;      /* only one at a time can send mess */
} PARTICIPANT;
/* returns array of pointers to parts. sets p_count to quant */
PARTICIPANT **get_participants(int *p_count);
/* adds a participant & assigns it an id */
int add_participant(PARTICIPANT *p);
/* invalidates the part. does not free it. */
void delete_participant(PARTICIPANT *p);
/* allocates a new part from heap. adds it. initializes fields. */
PARTICIPANT *create_participant(int socdesc);
/* closes p's socket, releases semaphore, invalidates
   p, and returns p to heap */
void free_participant(PARTICIPANT *p);
/* copies name to p->name */
void set_participant_name(PARTICIPANT *p, char *name);
/* returns p's name */
char *get_participant_name(PARTICIPANT *p);
/* fills buffer with part names and returns char count */
int participant_names(char* buffer, int size);
/* looks up part by socket descriptor */
PARTICIPANT* participant_given_socket_descriptor(int i);
/* runs in participants thread,
   handles all communication with part */
void *participant_connection_handler(void *p);
/* sends message to participant */
void send_message_to_participant(PARTICIPANT* participant, char *message);
/* represents message */
typedef struct message {
    int participant_id;             /* part's id */
    PARTICIPANT *sender;            /* part sending message */
    char content[MAX_MESSAGE_SIZE]; /* text of message */
} MESSAGE;
/* allocates memory for message, assigns participant_id,
   assigns sender, copies content into message, returns message */
MESSAGE *create_message(PARTICIPANT *sender, char *content);
/* zeros out message data and frees message */
void free_message(MESSAGE *msg);
/* non zero if msg's part is valid; otherwise, 0 */
int valid_message(MESSAGE *msg);
/* prints readable info about message */
void dump_message(MESSAGE *msg, FILE *file);
#endif