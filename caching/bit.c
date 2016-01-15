#include <stdio.h>
#include <stdlib.h>

int convertToBits(int);

int main ()
{
	int n = 3;
	printf("convert %d to bits: %d\n", n, convertToBits(n));
	printf("7 & 3 = %d\n", n&convertToBits(n));
}

int convertToBits(int n)
{
	int bits=0;
	int i;
	for (i = n; i > 0; i--)
	{
		// bits++;
		bits = bits << 1;
		bits++;
	}
	return bits;
}