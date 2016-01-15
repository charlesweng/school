#ifndef CACHEFUNCTIONS_H
#define CACHEFUNCTIONS_H

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

void *createAndInitialize(int blocksize, int cachesize, int type); 
// You have a struct that contains all of the information for one cache. 
// In this function, you create the cache and initialize it, 
// returning a pointer to the struct. Because you are determining the struct, 
// you return a void * to our main. Type 0 is a direct-mapped cache. 
// Type 1 is a 2-way pseudo-associative cache. Type 2 is an 8-way 
// set-associative cache.

int accessCache(void *cache, int address); 
//In this function, we access the cache with a particular address. 
//If the address results in a hit, return 1. If it is a miss, return 0.

int missesSoFar(void *cache); 
//This returns the number of misses that have occurred so far

int accessesSoFar(void *cache); 
//This returns the number of accesses that have occurred so far

int totalAccessTime(void *cache); 
//This returns the total number of cycles that all of the accesses 
//have taken so far.


// If you want to declare structs or extra helper functions, do so at the 
// top of your cachefunctions.c.  Do not add anything to this file.


#endif

