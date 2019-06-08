#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <fcntl.h>
#include <sys/shm.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <sys/mman.h>
#include <semaphore.h>
#include <math.h>
#include <signal.h>

typedef struct Node {
	int number;
	struct Node *next;
} Node;

typedef struct {
	Node *head;
	Node *tail;
	int size;
} LinkedList;

typedef struct {
sem_t mutex;
sem_t full;
sem_t empty;
int value;
} SubTotal;


void readValues(LinkedList* values, char* filename);
void calcSum(LinkedList* values, int processes);
void fillBuffer(LinkedList* values);
void childProcess(int index, int size, int k);
int parentConsumer(int k);
void destroySM(int size);
void initValues(int size);

//Global variables for accessing shared memory
int shmidBUF1;
int shmidST;
