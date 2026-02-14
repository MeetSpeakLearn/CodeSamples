/* Author: Stephen DeVoy*/

#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <semaphore.h>
#include <pthread.h>
#include "macros.h"
#include "chat.h"
#define MAX_MESSAGE_SIZE 2048
int main(int argc, char *argv[]) {
	int socket_desc, client_sock = 0, c, *new_sock;
	struct sockaddr_in server, client;
	sem_init(&participant_semaphore, 0, MAX_CONCURRENT_ACCESS);
	/* create a socket */
	socket_desc = socket(AF_INET , SOCK_STREAM , 0);
	if (socket_desc == -1) {
		printf("Could not create socket");
	}
	puts("Socket created");
	/* initialize the socket address structure */
	server.sin_family = AF_INET;
	server.sin_addr.s_addr = INADDR_ANY;
	server.sin_port = htons(8888);
	/* bind to the socket */
	if (bind(socket_desc, (struct sockaddr*) &server, sizeof(server)) < 0) {
		perror("bind failed");
		return 1;
	}
	puts("binding done");
	/* listen for a connection */
	listen(socket_desc, 3);
	/* wait for an incoming connection */
	puts("Waiting for incoming connections...");
	c = sizeof(struct sockaddr_in);
	while((sem_wait(&participant_semaphore) == 0)
			? (client_sock = accept(socket_desc, (struct sockaddr*) &client, (socklen_t*) &c))
			: 0) {
		puts("Connection has been accepted");
        PARTICIPANT *participant = create_participant(client_sock);
		new_sock = malloc(sizeof(int*));
		*new_sock = client_sock;
		if (pthread_create(&(participant->thread), &(participant->thread_attr), 
				participant_connection_handler, (void*) participant) < 0) {
			perror("could not create thread");
			return 1;
		}
		puts("handler assigned to socket");
		client_sock = 0;
	}
	sem_close(&participant_semaphore);
	sem_destroy(&participant_semaphore);
	if (client_sock < 0) {
		perror("accept failed");
		return 1;
	}
	return 0;
}