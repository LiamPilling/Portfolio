CC = gcc
OBJA = AdderA.o
OBJB = AdderB.o

AdderA : $(OBJA)
	$(CC) -g -o AdderA $(OBJA) -lrt -lm

AdderB : $(OBJB)
	$(CC) -g -o AdderB $(OBJB) -lm -pthread

AdderA.o : AdderA.c AdderA.h
	$(CC) -g -c AdderA.c
	
AdderB.o : AdderB.c AdderB.h
	$(CC) -g -c AdderB.c
	
clean:
	rm -f AdderA AdderB $(OBJA) $(OBJB)
