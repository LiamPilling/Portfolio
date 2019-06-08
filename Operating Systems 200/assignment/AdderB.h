#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <fcntl.h>
#include <pthread.h>
#include <math.h>
#include <semaphore.h>

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
   sem_t full;
   sem_t empty;
   int value;
} SubTotal;

typedef struct {
   int index; 
   int size; 
   int k;
} ThreadInfo;

void readValues(LinkedList *values, char *filename);
void calcSum(LinkedList *values, int threads);
void *threadAdd(void *information);
void initGlobals();
void initBuffer(LinkedList *values);
int parentConsumer(int k);

//Global variables for the buffer and sub total
int *buffer;
SubTotal *st;

pthread_mutex_t mutex;
