#include <stdio.h>
#include <stdlib.h>
#include <unistd.h> // For dup2 and close
#include <fcntl.h>  // For open flags

int main() {
    int file_descriptor;
    int saved_stdout;

    // Save the original stdout file descriptor
    saved_stdout = dup(STDOUT_FILENO); // STDOUT_FILENO is usually 1

    // Open the file to redirect output to
    file_descriptor = open("output.txt", O_WRONLY | O_CREAT | O_TRUNC, 0644);
    if (file_descriptor == -1) {
        perror("Error opening file");
        return 1;
    }

    // Redirect stdout to the file
    if (dup2(file_descriptor, STDOUT_FILENO) == -1) {
        perror("Error redirecting stdout");
        return 1;
    }
    close(file_descriptor); // The file descriptor is no longer needed after dup2

    // Now, any printf or puts will write to "output.txt"
    printf("This output goes to the file.\n");
    fprintf(stdout, "Another line to the file.\n");

    // Flush the buffer to ensure all data is written before restoring stdout
    fflush(stdout);

    // Restore original stdout
    if (dup2(saved_stdout, STDOUT_FILENO) == -1) {
        perror("Error restoring stdout");
        return 1;
    }
    close(saved_stdout); // Close the saved file descriptor

    // This output goes to the terminal
    printf("This output goes to the terminal.\n");

    return 0;
}