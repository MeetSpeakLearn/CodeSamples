#include <stdio.h>
#include <stdlib.h>
#include <unistd.h> // For pipe(), fork(), read(), write(), close()
#include <sys/wait.h> // For wait()

int main() {
    int pipefd[2]; // pipefd[0] for read end, pipefd[1] for write end
    pid_t pid;
    char buffer[256];
    const char *message = "Hello from parent!";

    // Create the pipe
    if (pipe(pipefd) == -1) {
        perror("pipe");
        exit(EXIT_FAILURE);
    }

    // Fork a child process
    pid = fork();

    if (pid == -1) {
        perror("fork");
        exit(EXIT_FAILURE);
    }

    if (pid == 0) { // Child process
        close(pipefd[1]); // Close the write end in the child

        ssize_t bytes_read = read(pipefd[0], buffer, sizeof(buffer));
        if (bytes_read == -1) {
            perror("read");
            exit(EXIT_FAILURE);
        }
        buffer[bytes_read] = '\0'; // Null-terminate the received data
        printf("Child received: %s\n", buffer);

        close(pipefd[0]); // Close the read end in the child
        exit(EXIT_SUCCESS);
    } else { // Parent process
        close(pipefd[0]); // Close the read end in the parent

        if (write(pipefd[1], message, strlen(message)) == -1) {
            perror("write");
            exit(EXIT_FAILURE);
        }
        printf("Parent sent: %s\n", message);

        close(pipefd[1]); // Close the write end in the parent
        wait(NULL); // Wait for the child process to terminate
        exit(EXIT_SUCCESS);
    }

    return 0; // Should not be reached
}