#ifndef PHONEH_H
#define PHONEH_H

#include <iostream>
#include <map>
#include <string>
#include <vector>
#include "phoneh.h"
using namespace std;

extern int keypad[10][10];
extern vector< vector<int> > keylist;


int** parse(int, char**);
void makeKeyList();
bool easyPhoneNumber(int, int*);
bool easyPhoneNumber2(int, int*);

#endif
