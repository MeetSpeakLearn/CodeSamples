#include <stdio.h>
#include <sys/types.h>
#include <unistd.h>
#include <sys/wait.h> // For wait()

int main() {
    pid_t pid;

    pid = fork(); // Create a child process

    if (pid < 0) {
        // Error occurred
        perror("fork failed");
        return 1;
    } else if (pid == 0) {
        // This is the child process
        printf("Child process: PID = %d, Parent PID = %d\n", getpid(), getppid());
        // Child can perform its specific tasks here
        _exit(0); // Use _exit() in child to avoid flushing buffers multiple times
    } else {
        // This is the parent process
        printf("Parent process: PID = %d, Child PID = %d\n", getpid(), pid);
        // Parent can wait for the child to finish
        wait(NULL); // Wait for any child process to terminate
        printf("Parent process: Child has terminated.\n");
    }

    return 0;
}