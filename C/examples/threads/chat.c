/* Author: Stephen DeVoy */

#include <ctype.h>
#include <sys/socket.h>
#include <semaphore.h>
#include "macros.h"
#include "chat.h"
#define MAX_PARTICIPANTS 8
#define MAX_TOTAL_NEW_PARTICIPANTS 64
#define PROMPT "\n* "
#define HELP_INFO "To send a direct message, preface a participant's" \
"\nname with @. All other messages will be sent to" \
"\neveryone in the room." \
"\nTo issue a command, preface the command with :.\n" \
"\nAvailable commands are:" \
"\n  :who  - lists everyone in the room" \
"\n  :exit - exits the chat" \
"\n  :help - prints the above information" \
"\n" \
PROMPT
sem_t participant_semaphore; /* controls number of active participants */
/* maximum number of particpants over lifetime of server */
PARTICIPANT* _participants[MAX_TOTAL_NEW_PARTICIPANTS];
/* running total of number of participants */
int _participants_count = 0;
PARTICIPANT **get_participants(int *p_count) {
    *p_count = _participants_count;
    return _participants;
}
/* returns the next available part id */
int new_participant_id() {
    if (_participants_count == 0)
        /* if first call, set all elem of _participants to 0 */
        memset(_participants, 0, MAX_PARTICIPANTS * sizeof(PARTICIPANT*));
    /* return id and inc counter */
    return _participants_count++;
}
/* is participant id valid? */
int valid_participant_id(int i) {
    if ((i < 0) || (i >= MAX_TOTAL_NEW_PARTICIPANTS)) return 0;
    return _participants[i] != 0;
}
/* invalidate participant */
void invalidate_participant(int i) {
    if ((i < 0) || (i >= MAX_TOTAL_NEW_PARTICIPANTS)) return;
    _participants[i] = 0;
}
int add_participant(PARTICIPANT *p) {
    CHECK_POINTER(delete_participant, p, return -1)
    p->id = new_participant_id();
    _participants[p->id] = p;
    return p->id;
}
void delete_participant(PARTICIPANT *p) {
    CHECK_POINTER(delete_participant, p, return)
    invalidate_participant(p->id);
}
/* looks up valid and alive participant by name */
PARTICIPANT *find_participant_by_name(char *name) {
    CHECK_POINTER(find_participant_by_name, name, 0)
    int i;
    for (i = 0; i < _participants_count; i++)
        if (_participants[i] != 0)
            if (_participants[i]->alive)
                if ((strcmp(name, _participants[i]->name) == 0))
                    return _participants[i];
    return 0;
}
PARTICIPANT *create_participant(int socdesc) {
    PARTICIPANT *new_part = (PARTICIPANT *) malloc(sizeof(PARTICIPANT));
    memset(new_part, 0, sizeof(PARTICIPANT));
    add_participant(new_part);
    printf("\nid=%d\n", new_part->id);
    new_part->alive = 0;
    new_part->socket_descriptor = socdesc;
    pthread_attr_init(&new_part->thread_attr);
    pthread_attr_setschedpolicy(&new_part->thread_attr, SCHED_RR);
    pthread_mutex_init(&new_part->mutex, NULL);
    return new_part;
}
void free_participant(PARTICIPANT *p) {
    CHECK_POINTER(free_participant, p, return)
    close(p->socket_descriptor);
    sem_post(&participant_semaphore);
    pthread_mutex_destroy(&p->mutex);
    delete_participant(p);
    memset(p, 0, sizeof(PARTICIPANT));
    free(p);
}
void set_participant_name(PARTICIPANT *p, char *name) {
    CHECK_POINTER2(set_participant_name, p, name, return)
    strncpy(p->name, name, MAX_NAME_SIZE);
}
char *get_participant_name(PARTICIPANT *p) {
    CHECK_POINTER(get_participant_name, p, return 0)
    return p->name;
}
int participant_names(char* buffer, int size) {
    CHECK_POINTER(participant_names, buffer, return 0)
    int i, total_size = 0, name_size;
    char *ptr = buffer, *name;
    memset(buffer, 0, size * sizeof(char));
    for (i = 0; i < _participants_count; i++) {
        if (_participants[i] == 0) continue;
        if (! _participants[i]->alive) continue;
        name = _participants[i]->name;
        name_size = strlen(name);
        if (total_size + name_size + 1 >= size) return total_size;
        strcpy(ptr, name);
        total_size += strlen(ptr) + 1;
        ptr = ptr + strlen(ptr);
        *ptr++ = '\n';
    }
    return total_size;
}
PARTICIPANT* participant_given_socket_descriptor(int i) {
    int j;
    for (j = 0; j < _participants_count; j++) {
        if (_participants[j]->socket_descriptor == i)
            return _participants[j];
    }
    return 0;
}
MESSAGE *create_message(PARTICIPANT *sender, char *content) {
    CHECK_POINTER2(create_message, sender, content, return 0)
    MESSAGE *new_msg = (MESSAGE*) malloc(sizeof(MESSAGE));
    new_msg->participant_id = sender->id;
    new_msg->sender = sender;
    strcpy(new_msg->content, content);
    return new_msg;
}
void free_message(MESSAGE *msg) {
    CHECK_POINTER(free_message, msg, return)
    memset(msg, 0, sizeof(MESSAGE));
    free(msg);
}
int valid_message(MESSAGE *msg) {
    CHECK_POINTER(valid_message, msg, return 0)
    return valid_participant_id(msg->participant_id);
}
void dump_message(MESSAGE *msg, FILE *file) {
    CHECK_POINTER2(dump_message, msg, file, return)
    fprintf(file, "[from: %s, msg: %s, valid: %d]",
        msg->sender->name, msg->content, valid_message(msg));
}
/* runs inside of the queue's threads */
void *do_queue(void *p) {
    CHECK_POINTER(do_queue, p, return NULL)
    PARTICIPANT *participant = (PARTICIPANT*) p;
    if (participant->alive) return NULL;
    while (participant->alive) sched_yield();
    return NULL;
}
/* Returns non zero if the msg contains a '@' char */
int contains_ref_to_name(MESSAGE *msg) {
    CHECK_POINTER(contains_ref_to_name, msg, 0);
    return strchr(msg->content, '@') != 0;
}
/* populates buffer with partipants mentioned in the msg */
int refed_participants(MESSAGE *msg, PARTICIPANT **buffer, int bufferlen) {
    CHECK_POINTER2(refed_participants, msg, buffer, return 0)
    char *content = msg->content, *ptr = strchr(content, '@');
    char *enptr = 0, name[MAX_NAME_SIZE];
    int name_size = 0, count = 0;
    PARTICIPANT *p;
    while (ptr != 0) {
        name_size = 0;
        if (isalpha(*(ptr + 1))) {
            enptr = ptr + 1;
            while (isalnum(*enptr) && (name_size < MAX_NAME_SIZE - 1))
                enptr += 1, name_size += 1;
            strncpy(name, ptr + 1, name_size);
            name[name_size] = '\0';
            if ((p = find_participant_by_name(name)) != 0) {
                if (count < bufferlen) buffer[count++] = p;
                else break;
            }
        }
        ptr = strchr(ptr + 1, '@');
    }
    return count;
}
/* performs the message sending behavior of broadcast */
#define DISTRIBUTE(PARTS) {\
    printf("\n - ");\
    dump_message(msg, stdout);\
    printf("\n");\
    printf("\n - Processing message from %d to %d\n", part->id, PARTS[i]->id);\
    sprintf(buf, "(%s) %s* ", part->name, msg->content);\
    write(PARTS[i]->socket_descriptor, buf, strlen(buf) + 1);\
}
void broadcast(MESSAGE *msg) {
	CHECK_POINTER(broadcase, msg, return)
	PARTICIPANT *part, **parts;
	int part_count, i, addressee_count;
    char buf[MAX_MESSAGE_SIZE*2];
	if (! valid_message(msg)) return;
	part = msg->sender;
	parts = get_participants(&part_count);
	printf("\nBroadcast:"); dump_message(msg, stdout); printf("\n");
    if (contains_ref_to_name(msg)) {
        PARTICIPANT *addressees[MAX_PARTICIPANTS];
        /* in the following condition, = is an assignment not a comparitor */
        if (addressee_count = refed_participants(msg, addressees, MAX_PARTICIPANTS))
            for (i = 0; i < addressee_count; i++) DISTRIBUTE(addressees)
    } else {
        for (i = 0; i < part_count; i++) {
            if ((parts[i] != 0) ? parts[i]->alive : 0)
                if (parts[i]->id != part->id) DISTRIBUTE(parts)
        }
    }
    sched_yield();
}
/* processes a command on behalf of the part */
int process_command(PARTICIPANT *p, char *str) {
    CHECK_POINTER2(process_command, p, str, return 0)
    if (strncmp(str, ":who", 4) == 0) {
        char *preface = "Currently in chat:\n\n";
        char names_buffer[(MAX_NAME_SIZE + 1) * MAX_PARTICIPANTS + 25];
        strcpy(names_buffer, preface);
        int preface_len = strlen(names_buffer);
        int size = participant_names(names_buffer + preface_len,
            (MAX_NAME_SIZE + 1) * MAX_PARTICIPANTS);
        strcpy(names_buffer + strlen(names_buffer), PROMPT);
        write(p->socket_descriptor, names_buffer, strlen(names_buffer) + 1);
        sched_yield();
        return 1;
    }
    if (strncmp(str, ":help", 5) == 0) {
        write(p->socket_descriptor, HELP_INFO, strlen(HELP_INFO) + 1);
        sched_yield();
        return 1;
    }
    return 0;
}
/* this is the main driver for communication between part p
   and the server */
void *participant_connection_handler(void *p) {
    CHECK_POINTER(participant_connection_handler, p, return 0)
    PARTICIPANT *participant = (PARTICIPANT *) p;
    int read_size, i, asking_for_name = 1;
	char *message, *ptr, *end_ptr;
    char client_message[MAX_MESSAGE_SIZE], client_name[MAX_NAME_SIZE];
    char formatted_client_message[MAX_MESSAGE_SIZE + MAX_NAME_SIZE + 2];
    participant->alive = 1;
    memset(client_name, '\0', MAX_NAME_SIZE * sizeof(char));
    message = "What is your name?\n";
	write(participant->socket_descriptor, message, strlen(message) + 1);
    memset(client_message, 0, sizeof(client_message));
    while((read_size = recv(participant->socket_descriptor, client_message, MAX_MESSAGE_SIZE, 0)) > 0) {
        printf("\nclient_message=%s\n", client_message);
        if (asking_for_name) {
            ptr = client_message;
            while (isspace(*ptr)) ptr++;
            if (*ptr == '\0')
                set_participant_name(participant, "");
            else {
                end_ptr = ptr;
                while (isalnum(*end_ptr)) end_ptr++;
                *end_ptr = '\0';
                if (find_participant_by_name(ptr) != 0) {
                    char new_name[MAX_NAME_SIZE + 1];
                    sprintf(new_name, "%s_%d", ptr, _participants_count);
                    set_participant_name(participant, new_name);
                } else set_participant_name(participant, ptr);
            }
            sprintf(client_message,
                "Welcome %s!\nType :help for information." PROMPT,
                participant->name);
            printf("\n - Sending: %s\n", client_message);
            send_message_to_participant(participant, client_message);
            asking_for_name = 0;
        } else if (! process_command(participant, client_message)) {
            client_message[read_size] = '\0';
            printf("\nSending: %s\n", client_message);
            broadcast(create_message(participant, client_message));
            write(participant->socket_descriptor, PROMPT, strlen(PROMPT) + 1);
            sched_yield();
        }
        memset(client_message, 0, sizeof(client_message));
	}
	if (read_size == 0) {
		puts("Client disconnected");
		fflush(stdout);
	} else if (read_size == -1) perror("recv failed");
    participant->alive = 0;
    delete_participant(participant);
	free_participant(participant);
	return 0;
}
/* sends message to participant */
void send_message_to_participant(PARTICIPANT *participant, char *message) {
    CHECK_POINTER2(send_message_to_participant, participant, message, return)
    pthread_mutex_lock(&participant->mutex);
    printf("\n - Sending to %s: \"%s\"\n", participant->name, message);
    write(participant->socket_descriptor, message, strlen(message) + 1);
    pthread_mutex_unlock(&participant->mutex);
}