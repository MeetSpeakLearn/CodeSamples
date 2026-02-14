#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <termios.h>
#include "macros.h"
#include "queue.h"
#define MAX_MESSAGE_SIZE 2048
#define PROMPT "\n* "
ARRAY_BASED_QUEUE incoming;
int puts_without_newline(char* str) {
	CHECK_POINTER(puts_without_newline, str, return 0)
	if (str == 0) return 0;
	char *ptr = str; int count = 0;
	while (*ptr != '\0') {
		if ((*(ptr + 1) == '\0') ? (*ptr == '\n') : 0) break;
		putchar(*ptr++); count++;
	}
	return count;
}
char* fgets_without_newline(char *s, int n, FILE *f) {
	char *result = fgets(s, n, f);
	if (result > 0) {
		char *ptr = strchr(result, '\n');
		if (ptr != 0) *ptr = '\0';
		return result;
	} else return result;
}
int is_running = 0;
void *receiver(void *s) {
	CHECK_POINTER(receiver, s, return 0)
	int *sock = (int*) s, reply_size;
	char server_reply[MAX_MESSAGE_SIZE], *msg;
	while (is_running) {
		reply_size = recv(*sock, server_reply, MAX_MESSAGE_SIZE, 0);
		if (reply_size > 0) {
			msg = (char*) calloc(reply_size + 1, sizeof(char));
			strncpy(msg, server_reply, reply_size);
			msg[reply_size] = '\0';
			enqueue(&incoming, msg);
		}
	}
	return 0;
}
void *reporter(void *s) {
	CHECK_POINTER(reporter, s, return 0)
	char *msg, *prmt = PROMPT; int len;
	while (is_running) {
		dequeue(&incoming, (void*) &msg);
		if (msg == 0) printf("\nmsg is null.\n");
		len = strlen(msg);
		if (strcmp(msg, prmt) == 0) printf(PROMPT);
		else if ((len >= strlen(prmt))
					? (strcmp(msg + len - strlen(prmt), prmt) == 0)
					: 0) {
			puts_without_newline(msg);
			puts_without_newline(prmt);
		} else printf("%s", msg);
		sleep(1);
	}
	return 0;
}
#define DIE {is_running = 0; close(*sock); return 0;}
void *ui(void *s) {
	CHECK_POINTER(ui, s, return 0)
	int *sock = (int*) s, reply_size;
	char message[MAX_MESSAGE_SIZE], server_reply[MAX_MESSAGE_SIZE];
	while(1) {
		memset(message, 0, MAX_MESSAGE_SIZE * sizeof(char));
		fgets(message, MAX_MESSAGE_SIZE, stdin);
		if (strncmp(message, ":exit", 5) == 0) DIE
		if(send(*sock, message, strlen(message), 0) < 0) {
			puts("Send failed");
			DIE
		}
		sleep(1);
	}
	return 0;
}
int main(int argc, char *argv[]) {
	pthread_t ui_thread;
    pthread_attr_t ui_thread_attr;
	int sock;
	struct sockaddr_in server;
	char message[MAX_MESSAGE_SIZE], server_reply[MAX_MESSAGE_SIZE];
	init_queue(&incoming);
	//Create socket
	sock = socket(AF_INET, SOCK_STREAM, 0);
	if (sock == -1) {
		printf("Could not create socket");
	}
	puts("Socket created");
	server.sin_addr.s_addr = inet_addr("127.0.0.1"); /* your local machine */
	server.sin_family = AF_INET;
	server.sin_port = htons( 8888 );
	/* connect to remote server */
	if (connect(sock, (struct sockaddr *) &server, sizeof(server)) < 0) {
		perror("connect failed. Error");
		return 1;
	}
	puts("Connected\n");
	is_running = 1;
	pthread_attr_init(&incoming.enqueue_thread_attr);
	pthread_attr_setschedpolicy(&incoming.enqueue_thread_attr, SCHED_RR);
	if (pthread_create(&incoming.enqueue_thread,
			&incoming.enqueue_thread_attr,  receiver, (void*) &sock)
			< 0) {
		perror("could not create thread");
		return 1;
	}
	pthread_attr_init(&incoming.dequeue_thread_attr);
	pthread_attr_setschedpolicy(&incoming.dequeue_thread_attr, SCHED_RR);
	if (pthread_create(&incoming.dequeue_thread,
			&incoming.dequeue_thread_attr,
			reporter, (void*) &sock) < 0) {
		perror("could not create thread");
		return 1;
	}
	pthread_attr_init(&ui_thread_attr);
	pthread_attr_setschedpolicy(&ui_thread_attr, SCHED_RR);
	if (pthread_create(&ui_thread,
			&ui_thread_attr, ui, (void*) &sock) < 0) {
		perror("could not create thread");
		return 1;
	}
	if (pthread_join(ui_thread, NULL) != 0) {
        /* wait for UI to terminate */
        perror("Failed to join thread");
        return EXIT_FAILURE;
    }
	is_running = 0;	
	close(sock);
	return 0;
}