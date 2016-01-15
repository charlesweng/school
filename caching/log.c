#include <stdio.h>
#include <stdlib.h>

int main ()
{
	int number = 4;
	int log_2 = 0;
	while (number >>= 1)
	{
		log_2++;
	}

	//reset
	log_2 = 0;
	number = 4;
	int j;
	for (j = number; j > 1; j >>=1)
		log_2++;
	// for (int i = number; i > 0; i = i >> 1)
	// 			log_2++;
	printf("The log_2 of %d is %d\n", number, log_2);
}