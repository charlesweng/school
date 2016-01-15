#include "cachefunctions.h"
#include <stdio.h>
#include <stdlib.h>

//default address length (bits)
int address_length = 32;
//access time for direct-map and time for each pseudo-associative caches (cycles)
int hit_time = 1;
//access time for 8-way associative (cycles)
int hit_time_2 = 6;
//miss penalty when accessing DRAM (cycles)
int miss_penalty = 100;

//function prototypes for helper funcitons
int convertToBits(int);

typedef struct _cache 
{
	//type needed to know which kind of cache
	int type;

	//sizes needed for the address
	int index_size;//used to determine which block to store in
	int tag_size;//determines if the tag corresponds with a particular chunk of memory
	int total_offset;//our total one whole line of data
	int nblocks;//number of blocks
	int blocksize;
	int cachesize;//the total size of our cache

	//array used to store tags for directly mapped caches
	int *direct_tags;

	//array used to store tags for pseudo-associative caches
	int *pseudo_tags1;
	int *pseudo_tags2;
	//additional time caused by accessing second table of pseudo cache
	int pseudo_time;

		//used for keeping track of which set is used more
	int *lru_val1;
	int *lru_val2;

	//array used to store tags for 8-way associative caches
	int *assoc_tags;

	// int address_size;//always 32 bits???
	int byte_address;//no clue
	int accesses;//do we need?
	int misses;
	int penalty;//do we need?

	//used to keep track of which sets within associative/pseudo-associative
	int cycle;

} Cache;

// You have a struct that contains all of the information for one cache. 
// In this function, you create the cache and initialize it, 
// returning a pointer to the struct. Because you are determining the struct, 
// you return a void * to our main. Type 0 is a direct-mapped cache. 
// Type 1 is a 2-way pseudo-associative cache. Type 2 is an 8-way 
// set-associative cache.
void *createAndInitialize(int blocksize, int cachesize, int type)
{
	Cache* cache = (Cache*) malloc(sizeof(Cache));
	//all accesses and misses
	cache->accesses = 0;
	cache->misses = 0;
	cache->pseudo_time = 0;
	//might or might not be useful values
	cache->blocksize = blocksize;
	cache->cachesize = cachesize;
	switch(type)
	{
		//direct-mapped cache
		case 0:
		{
			//type of cache
			cache->type = 0;

			//total offset includes block offset and byte offset
			int total_offset = 0;
			//this is for taking the log
			int i;
			for (i = blocksize; i > 1; i = i >> 1)
				total_offset++;
			cache->total_offset = total_offset;

			//# of blocks inside cache (used to create tag array and find index bits)
			cache->nblocks = cachesize/blocksize;
			int nblocks = cache->nblocks;

			//# of index
			int index_size = 0;
			//this is for taking the log
			int j;
			for (j = nblocks; j > 1; j = j >> 1)
				index_size++;
			cache->index_size = index_size;

			//tag is everything besides the index and the blocksize
			cache->tag_size = address_length - index_size - total_offset;

			//initialize the array for the addresses
			cache->direct_tags=(int*)malloc(nblocks*sizeof(int));
			int k = 0;
			for (k = 0; k < nblocks; k++)
			{
				cache->direct_tags[k] = -1;
			}

			//return the pointer to the struct
			return cache;

			break;
		}
		//2-way pseudo-associative cache
		case 1:
		{
			//type of cache
			cache->type = 1;

			//total offset includes block offset and byte offset
			int total_offset = 0;

			//this is for taking the log
			int i;
			for (i = blocksize; i > 1; i = i >> 1)
				total_offset++;
			cache->total_offset = total_offset;

			//# of blocks inside cache (used to create tag array and find index bits)
			cache->nblocks = cachesize/blocksize/2;
			int nblocks = cache->nblocks;

			//# of index
			int index_size = 0;
			//this is for taking the log
			int j;
			for (j = nblocks; j > 1; j = j >> 1)
				index_size++;
			cache->index_size = index_size;

			//tag is everything besides the index and the blocksize
			cache->tag_size = address_length - index_size - total_offset;

			//initialize the array for the tags
			cache->pseudo_tags1=(int*)malloc(nblocks*sizeof(int));
			cache->pseudo_tags2=(int*)malloc(nblocks*sizeof(int));
			// cache->lru_val1=(int*)malloc(nblocks*sizeof(int));
			// cache->lru_val2=(int*)malloc(nblocks*sizeof(int));
			int k = 0;
			for (k = 0; k < nblocks; k++)
			{
				cache->pseudo_tags1[k] = -1;
				cache->pseudo_tags2[k] = -1;
				// cache->lru_val1[k] = 0;
				// cache->lru_val2[k] = 0;
			}

			//return the pointer to the struct
			return cache;
			break;
		}
		//8-way set-associative cache
		case 2:
		{
			break;
		}
	}

}

//In this function, we access the cache with a particular address. 
//If the address results in a hit, return 1. If it is a miss, return 0.
int accessCache(void *cache, int address)
{
	//my currently myCache cache
	Cache* myCache = (Cache*)cache;

	
	//type of cache myCache
	switch(myCache->type)
	{
		case 0:
		{
			//values probablyb not needed
			int blocksize = myCache->blocksize;
			int cachesize = myCache->cachesize;

			//total offset only used for shifting in this case
			int total_offset = myCache->total_offset;

			//index_size used for shifting
			int index_size = myCache->index_size;
			//index_mask used for masking
			int index_mask = convertToBits(index_size);

			//tag size used for shifting
			int tag_size = myCache->tag_size;
			//tag_mask used for masking
			int tag_mask = convertToBits(tag_size);

			//shifting and masking the index
			int index = (address >> total_offset) & index_mask;
			index %= myCache->nblocks;

			//shifting and masking the address
			int tag = address >> (total_offset+index_size);

			//accesses
			myCache->accesses++;

			//determining hit or miss increase the count
			if (myCache->direct_tags[index] == tag)
			{
				return 1;
			}
			else
			{
				//sets the tag for the array
				myCache->direct_tags[index] = tag;

				//increment miss
				myCache->misses++;
				return 0;
			}
			break;
		}
		case 1:
		{
			//values probablyb not needed
			int blocksize = myCache->blocksize;
			int cachesize = myCache->cachesize;

			//total offset only used for shifting in this case
			int total_offset = myCache->total_offset;

			//index_size used for shifting
			int index_size = myCache->index_size;

			//index_mask used for masking
			int index_mask = convertToBits(index_size);

			//tag size used for shifting
			int tag_size = myCache->tag_size;

			//tag_mask used for masking
			int tag_mask = convertToBits(tag_size);

			//shifting and masking the index
			int index = (address >> total_offset) & index_mask;
			index %= myCache->nblocks;

			//shifting and masking the address
			int tag = address >> (total_offset+index_size);

			//accesses
			myCache->accesses++;

			//determining hit or miss increase the count
			if (myCache->pseudo_tags1[index] == tag)
			{
				// myCache->accesses++;
				//updates which of the arrays were last updated
				return 1;
			}
			else if (myCache->pseudo_tags2[index] == tag)
			{
				//updates which of the arrays were last updated
				int temp = myCache->pseudo_tags1[index];
				myCache->pseudo_tags1[index] = myCache->pseudo_tags2[index];
				myCache->pseudo_tags2[index] = temp;
				myCache->pseudo_time++;
				// myCache->accesses+=2;
				return 1;
			}
			else
			{
				// myCache->accesses+=2;
				myCache->pseudo_time++;
				//evaluates which pseudo-set to store the tag in
				if (myCache->pseudo_tags1[index] == -1)
				{
					//sets the tag for the array
					myCache->pseudo_tags1[index] = tag;
				}
				else if (myCache->pseudo_tags2[index] == -1)
				{
					myCache->pseudo_tags2[index] = tag;
				}
				else
				{
					myCache->pseudo_tags2[index] = myCache->pseudo_tags1[index];
					myCache->pseudo_tags1[index] = tag;
				}
				//increment miss
				myCache->misses++;
				return 0;
			}
			break;
		}
		case 2:
		{
			break;
		}
	}
}

//This returns the number of misses that have occurred so far
int missesSoFar(void *cache)
{	
	return ((Cache*)cache)->misses;
}

//This returns the number of accesses that have occurred so far
int accessesSoFar(void *cache)
{
	return ((Cache*)cache)->accesses;
}

//This returns the total number of cycles that all of the accesses 
//have taken so far.
int totalAccessTime(void *cache)
{
	Cache* myCache = (Cache*)cache;
	switch(myCache->type)
	{
		case 0:
		{
			int access_time = hit_time*myCache->accesses;
			double miss_rate = (double)myCache->misses/(double)(myCache->accesses);
			int total_access_time = access_time + (double)miss_rate*miss_penalty*access_time;
			return (int)total_access_time;
			break;
		}
		case 1:
		{
			int access_time = hit_time*myCache->accesses + pseudo_time*hit_time;
			double miss_rate = (double)myCache->misses/(double)(myCache->accesses);
			int total_access_time = access_time + (double)miss_rate*miss_penalty*access_time;
			return (int)total_access_time;
			break;
		}
		case 2:
		{
			break;
		}
	}
}

int convertToBits(int n)
{
	int bits=0;
	int i;
	for (i = n; i > 0; i--)
	{
		bits <<= 1;
		bits++;
	}
	return bits;
}