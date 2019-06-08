/*********************************************\
File: AdderB.c
Author: Liam Pilling (15520564)

This file is responsible for PartB of the OS 
assignment which involves adding up the numbers
in a file using threads.
\*********************************************/

#include "AdderB.h"

int main(int argc, char *argv[])
{
   LinkedList* values;
   values = (LinkedList*)malloc(sizeof(LinkedList));
   values->size = 0;
   values->head = NULL;
   values->tail = NULL;

   //If we don't have three command line strings then prompt user to
   //start again
   if(argc == 3)
   {
      readValues(values, argv[1]);
      calcSum(values, atoi(argv[2]));
   }
   else
   {
      printf("Incorrect command line values. Must be of format:\n");
      printf("./AdderA <InputFile> <NumberOfProcesses>\n"); 
   }

   //free all allocated memory
   Node *currentNode, *node;
   currentNode = values->head;
   while(currentNode != NULL)
   {
      node = currentNode;
      currentNode = currentNode->next;
      free(node);
   }
   free(values);
}

void readValues(LinkedList *values, char *filename)
{
   FILE* file;
   Node* newValue;
   int value;
   
   file = fopen(filename, "r");
   if(file == NULL)
      perror("Error opening file\n");
   else
   {
      while(fscanf(file, "%d,", &value) == 1)
      {             
         //Here we read the values in the file into a linked list  
         newValue = (Node*)malloc(sizeof(Node));
         newValue->next = NULL;
         newValue->number = value;
         if(values->size == 0)
            values->head = newValue;
         else
            values->tail->next = newValue;
         values->tail = newValue;
         values->size++;
      }
      fclose(file);
   }
}

void calcSum(LinkedList *values, int threads)
{
   int i, rc, total;
   ThreadInfo *info;
   void *status;
   pthread_t thread[threads];
   Node *currentNode;   

   info = (ThreadInfo*)malloc(threads*sizeof(ThreadInfo));
   initGlobals();
   initBuffer(values);
   
   //Create all the information a thread needs
   for(i = 0; i < threads; i++)
   {
      info[i].index = i;
      info[i].size = values->size;
      info[i].k = threads;
   }
   
   //Create n number of threads
   for(i = 0; i < threads; i++)
   {
      rc = pthread_create(&thread[i], NULL, threadAdd, (void*)&info[i]);
   }


   total = parentConsumer(threads);
   
   printf("Total: %d\n", total);
   pthread_mutex_destroy(&mutex);
}

void *threadAdd(void *information)
{
   int i, subTotal, assignedSize, tempSize, tempK;  
   int tempAssignedSize, amount, index, size, k;
   void* ptr;
   ThreadInfo *info = information;
   
   pthread_t thisThread;
   thisThread = pthread_self();
   
   k = info->k;
   size = info->size;
   index = info->index;
   tempSize = size;
   tempK = k;
   assignedSize = (int)ceil((float)size/(float)k);
   i = subTotal = amount = 0;
   for(i=0;i <= index; i++)
   {
      tempAssignedSize = (int)ceil((float)tempSize/(float)tempK);
      tempK--;
      tempSize -= tempAssignedSize;
      
      if(i > 0)
         amount += assignedSize;
      if(tempAssignedSize < assignedSize)
      {
         assignedSize = tempAssignedSize;
      }
   }
   
   for(i = amount; i < (amount+assignedSize); i++)
   {
      if(i < size)
      {
         subTotal += buffer[i];  
      }
   }
   
   printf("Sub-total produced by Thread with ID%d: %d\n",  
          (int)thisThread, subTotal);
  
   sem_wait(&st->empty);
   pthread_mutex_lock(&mutex);
/******** Critical section begins ***********/
   //Write the subtotal to the global variable
   st->value = subTotal;
/******** End of critical section *****************/
   pthread_mutex_unlock(&mutex);
   sem_post(&st->full);
   
}

int parentConsumer(int k)
{
   int total, i;
   total = 0;
   
   for(i = 0; i < k; i++)
   {
      sem_wait(&st->full);
      pthread_mutex_lock(&mutex);

   /******** Critical section begins ***********/
    //Read the value in the shared memory block into SubTotal
      total += st->value;
   /****** Critical section ends ***************/

      pthread_mutex_unlock(&mutex);
      sem_post(&st->empty);
   }
   return total;
}

void initGlobals()
{
   st = (SubTotal*)malloc(sizeof(SubTotal));
   sem_init(&st->empty, 1, 1);
   sem_init(&st->full, 1, 0);
   pthread_mutex_init(&mutex, NULL);
}

void initBuffer(LinkedList *values)
{
   int i;
   i = 0;
   Node *currentNode;

   buffer = (int*)malloc(values->size * sizeof(int));
   
   currentNode = values->head;
   while(currentNode != NULL)
   {
      buffer[i] = currentNode->number;
      i++;
      currentNode = currentNode->next;
   }
}
