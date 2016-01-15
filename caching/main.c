#include <stdio.h>
#include <stdlib.h>
#include "cachefunctions.h"
void printCache(void *);
void printTagArray(void *myCache);
int main ()
{
	int blocksize = 8;
	int cachesize = 32;
	int type = 1;

	Cache* myCache;
	myCache = createAndInitialize(blocksize, cachesize, type);
	printCache(myCache);
	printTagArray(myCache);
	int i;
	for (i = 0; i < 64; i++)
	{
		accessCache(myCache, i);
	}
	printTagArray(myCache);
}

void printCache(void *myCache)
{
	Cache* cache = (Cache*) myCache;
	int type = cache->type;
	switch(type)
	{
		case 0:
		{
			printf("\nDirectly-Mapped Cache Info\n");
			printf("# of blocks: %d\n", (int)(cache->nblocks));
			printf("index size: %d\n", (int)(cache->index_size));
			printf("total offset: %d\n", cache->total_offset);
			printf("tag size: %d\n\n", (int)(cache->tag_size));
			break;
		}
		case 1:
		{
			printf("\nPseudo-Cache Info\n");
			printf("# of blocks: %d\n", (int)(cache->nblocks));
			printf("index size: %d\n", (int)(cache->index_size));
			printf("total offset: %d\n", cache->total_offset);
			printf("tag size: %d\n\n", (int)(cache->tag_size));
			break;
		}
	}
	
}

void printTagArray(void *myCache)
{
	Cache* cache = (Cache*) myCache;
	int type = cache->type;
	switch(type)
	{
		case 0:
		{
			printf("\nsizeof tags array: %lu\n", sizeof(cache->direct_tags)/sizeof(int));

			printf("\nTags for Directly Mapped Cache\n");
			int i;
			for (i = 0; i < cache->nblocks; i++)
			{
				printf ("%d ", cache->direct_tags[i]);
			}
			printf("\n");
			break;
		}
		case 1:
		{
			printf("\nsizeof tags array: %lu\n", sizeof(cache->direct_tags)/sizeof(int));

			printf("\nTags for Pseudo Cache\n");
			int i;
			for (i = 0; i < cache->nblocks; i++)
			{
				printf ("%d ", cache->pseudo_tags1[i]);
			}
			for (i = 0; i < cache->nblocks; i++)
			{
				printf ("%d ", cache->pseudo_tags2[i]);
			}
			printf("\n");
			break;
		}
	}
}