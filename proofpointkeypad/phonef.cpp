#include <iostream>
#include <map>
#include <string>
#include <vector>
#include <array>
#include "phoneh.h"
using namespace std;
#define PHONE_SIZE 10

/* COMPILE: make */
/* USAGE: ./easyphone 1232225555 1225703313 ... */

int keypad[10][10] = {
                  // 0,1,2,3,4,5,6,7,8,9
              /*0*/{ 1,0,0,0,0,0,0,1,1,1},
              /*1*/{ 0,1,1,0,1,1,0,0,0,0},
              /*2*/{ 0,1,1,1,1,1,1,0,0,0},
              /*3*/{ 0,0,1,1,0,1,1,0,0,0},
              /*4*/{ 0,1,1,0,1,1,0,1,1,0},
              /*5*/{ 0,1,1,1,1,1,1,1,1,1},
              /*6*/{ 0,0,1,1,0,1,1,0,1,1},
              /*7*/{ 1,0,0,0,1,1,0,1,1,0},
              /*8*/{ 1,0,0,0,1,1,1,1,1,1},
              /*9*/{ 1,0,0,0,0,1,1,0,1,1}
                   };

vector < vector<int> > keylist;

vector<int> createAdjList(int size,int values[])
{
  vector<int> adjNum(values,values+size); 
  return adjNum;
}

void makeKeyList()
{
  int v0[] = {0,7,8,9};
  int v1[] = {1,2,4,5};
  int v2[] = {1,2,3,4,5,6};
  int v3[] = {2,3,5,6};
  int v4[] = {1,2,4,5,7,8};
  int v5[] = {1,2,3,4,5,6,7,8,9};
  int v6[] = {2,3,5,6,8,9};
  int v7[] = {0,4,5,7,8};
  int v8[] = {0,4,5,6,7,8};
  int v9[] = {0,5,6,8,9};
  keylist.push_back(createAdjList(4,v0));
  keylist.push_back(createAdjList(4,v1));
  keylist.push_back(createAdjList(6,v2));
  keylist.push_back(createAdjList(4,v3));
  keylist.push_back(createAdjList(6,v4));
  keylist.push_back(createAdjList(9,v5));
  keylist.push_back(createAdjList(6,v6));
  keylist.push_back(createAdjList(5,v7));
  keylist.push_back(createAdjList(6,v8));
  keylist.push_back(createAdjList(5,v9));
}

struct ParseMap {
  static map<char,int> create_map()
  {
    map<char,int> myMap;
    myMap['0'] = 0;
    myMap['1'] = 1;
    myMap['2'] = 2;
    myMap['3'] = 3;
    myMap['4'] = 4;
    myMap['5'] = 5;
    myMap['6'] = 6;
    myMap['7'] = 7;
    myMap['8'] = 8;
    myMap['9'] = 9;
    return myMap;
  }
  static const map<char,int> charToInt;
};

const map<char,int> ParseMap::charToInt = ParseMap::create_map();

ParseMap pm;

vector<int> phoneSizes;


int** parse(int n, char **phoneNos)
{
  int **phoneNo = (int**)malloc(n*sizeof(int*));
  for (int i = 1; i < n; i++)
  {
    string phone = phoneNos[i];
    phoneNo[i-1] = (int*)malloc(phone.size()*sizeof(int));
    phoneSizes.push_back(phone.size());
    for (int j = 0; j < phone.size();j++)
    {
      if (phone[j]!='(' || phone[j]!=')' || phone[j]!='-')
      {
        phoneNo[i-1][j]=pm.charToInt.at(phone[j]);
      }
    }
  }
  return phoneNo;
}

bool easyPhoneNumber(int n, int* phoneNo)
{
  for (int i = 0; i < phoneSizes[n]-1; i++)
  {
     if (keypad[phoneNo[i]][phoneNo[i+1]]!=1)
     {
       return false;
     }
  }
  return true;
}

bool contains(vector<int> vec, int value)
{
  for (int i = 0; i < vec.size(); i++)
  {
    if (vec[i] == value)
      return true;
  }
  return false;
}

bool easyPhoneNumber2(int n, int* phoneNo)
{
  for (int i = 0; i < phoneSizes[n]-1; i++)
  {
     if (!contains(keylist[phoneNo[i]],phoneNo[i+1]))
     {
       return false;
     }
  }
  return true;
}
