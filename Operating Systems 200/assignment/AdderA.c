/*********************************************\
File: AdderA.c
Author: Liam Pilling (15520564)

This file is responsible for PartA of the OS 
assignment which involves adding up the numbers
in a file using processes.
\*********************************************/

#include "AdderA.h"

key_t BUFFER1 = 1390;
key_t SUBTOTAL = 2740;

int main(int argc, char* argv[])
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

void readValues(LinkedList* values, char* filename)
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

void calcSum(LinkedList* values, int processes)
{
   int i, size, total;
   total = 0;
   pid_t pid;

   //Initialise the shared memory and semaphores
   initValues(values->size);
   
   //Fill BUFFER1 with the values
   fillBuffer(values);
   
   //Parent produces k children
   for(i = 0; i < processes; i++)
   {
      pid = fork();
      if(pid < 0)
      {
         perror("Fork failed\n");
         exit(1);
      }
      else if(pid == 0)
      {
         //Here the child process will read the values and add
         //them up to put into the subtotal shared memory block
         childProcess(i, values->size, processes);
      }
   }          
   //Parent consumes the values and returns the total
   total = parentConsumer(processes);

   printf("Total: %d\n", total);
   
   //Destroy the shared memory segments
   destroySM(values->size);
}

void fillBuffer(LinkedList* values)
{
   int j;
   Node* current;
   void* ptr;
   int* array;
   j = 0;

   //Bind the shared memory block to our array
   if((array = shmat(shmidBUF1, NULL, 0)) == (int*)-1)
   {
      perror("Error binding shared memory in fillBuffer()\n");
      exit(1);
   }
   
   //Fill buffer with our values
   current = values->head;
   while(current != NULL)
   {
      //Writing the values for our processes to the Buffer block
      array[j] = current->number;
      j++;
      current = current->next;
   }
}

void childProcess(int index, int size, int k)
{
   int i, subTotal, assignedSize, tempSize, tempK;  
   int tempAssignedSize, amount;
   void* ptr;
   int* array;
   SubTotal* st;
   tempSize = size;
   tempK = k;
   assignedSize = (int)ceil((float)size/(float)k);
   i = subTotal = amount = 0;
  
   //This helps us to find where we are up to and to 
   //evenly distribute values among the processes
 
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
   
   //Bind Buffer1 to our array to add up
   if((array = shmat(shmidBUF1, NULL, SHM_RDONLY)) == (int*)-1)
   {
      perror("Error binding BUFFER1 to child\n");
      exit(1);
   }

   //Bind the SubTotal block to our value
   if((st = shmat(shmidST, NULL, 0)) == (SubTotal*)-1)
   {
      perror("Error binding SUBTOTAL to child\n");
      exit(1);
   }

   //Reading the values in Buffer1 and adding them up to retrieve the
   //sub-total
   for(i = amount; i < (amount+assignedSize); i++)
   {
      if(i < size)
      {
         subTotal += array[i];  
      }
   }
   
   printf("Sub-total produced by Processor with ID %d: %d\n", getpid(), 
          subTotal);
  
   sem_wait(&st->empty);
   sem_wait(&st->mutex);
/******** Critical section begins ***********/
   //Write the subtotal to the shared memory block
   st->value = subTotal;
/******** End of critical section *****************/
   sem_post(&st->mutex);
   sem_post(&st->full);
   
   exit(0);
}

int parentConsumer(int k)
{
   int total, i;
   total = 0;
   SubTotal *st;
   
   //Bind the SubTotal block to our value
   if((st = shmat(shmidST, NULL, 0)) == (SubTotal*)-1)
   {
      perror("Error binding shared memory in parentConsumer()\n");
      exit(1);
   }
   
   for(i = 0; i < k; i++)
   {
      sem_wait(&st->full);
      sem_wait(&st->mutex);
       
   /******** Critical section begins ***********/
    //Read the value in the shared memory block into SubTotal
      total += st->value;   
   /****** Critical section ends ***************/
   
      sem_post(&st->mutex);
      sem_post(&st->empty);
   }
   return total;
}

void initValues(int size)
{
   SubTotal *st;
      
   //Initialise the Buffer1 shared memory block
   if((shmidBUF1 = shmget(BUFFER1, size*sizeof(int), IPC_CREAT | 0666)) <
   0)
   {
      perror("Error creating shared memory BUFFER1 in initValues()\n");
      exit(1);
   }
   
   //Initialise the SubTotal shared memory block
   if((shmidST = shmget(SUBTOTAL, sizeof(SubTotal), IPC_CREAT | 0666)) <
   0)
   {
      perror("Error creating shared memory SUBTOTAL in initValues()\n");
      exit(1);
   }
   
   //Bind the SubTotal block to our value
   if((st = shmat(shmidST, NULL, 0)) == (SubTotal*)-1)
   {
      perror("Error binding shared memory to SubTotal in initValues()\n");
      exit(1);
   }
   
   //Initialise the semaphores
   sem_init(&st->mutex, 1, 1);
   sem_init(&st->full, 1, 0);
   sem_init(&st->empty, 1, 1);
}

void destroySM(int size)
{
   //Destroy the shared memory
   shmctl(shmidBUF1, IPC_RMID, NULL);
   shmctl(shmidST, IPC_RMID, NULL);
}
