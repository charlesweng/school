#include <stdio.h>
#ifndef FUNCTIONS_H
#define FUNCTIONS_H

// structures
typedef struct _signals{
        int aluop;
        int mw;
        int mr;
        int mtr;
        int asrc;
        int btype;
        int rdst;
        int rw;
} Signals;

/* these are the decoded fields for a single instruction */
typedef struct _fields{
        int rd;
        int rs;
        int rt;
        int imm;
        int op;
        int func;
} Fields;

/* this is all of the information for a single instruction */
typedef struct _instinfo{
        int inst;   // fetch fills this in
        Signals signals; // decode fills this in
        Fields fields; // decode fills this in
        int pc; // fetch fills this in
        int aluout; // execute fills this in
        int memout; // memory fills this in
        int sourcereg; //dont need cuz we can use fields
        int targetreg; //dont need
        int destreg;   //used with writeback
        int destdata;  //result written to this one Writeback
        char string[30];
        int s1data; //need decode
        int s2data; //need decode
        int input1; //need execute
        int input2; //need execute
} InstInfo;

// You need to fill in
int load(char *filename);
void fetch(InstInfo *);
void decode(InstInfo *);
void execute(InstInfo *);
void memory(InstInfo *);
void writeback(InstInfo *);
void setPCWithInfo(InstInfo *);
// this function is provided for you
 void print(InstInfo *, int);
 void printLoad(int);
 void printP2(InstInfo *fetchInst, InstInfo *decodeInst, InstInfo *executeInst,
                InstInfo *memoryInst, InstInfo *writebackInst, int cycle);

extern int pc;
extern int instmem[100];
extern int datamem[1024];
extern int regfile[32];

#endif

