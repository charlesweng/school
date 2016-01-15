
#include <stdio.h>
#include <stdlib.h>
#include <strings.h>
#include "functions.h"
// op code
#define addi 48
#define same_op 33
#define and_func 40
#define sub_func 24
#define sgt_func 10
#define lw 18
#define sw 19
#define blt 14
#define jr 41
#define jal 10
// aluop code
#define plus 0
#define minus 1
#define and 2
#define or 3
#define not 4
#define xor 5
#define sgt 6
#define slt 7
#define blt_type 3
#define jr_type 2
#define jal_type 1

// these are the structures used in this simulator
// global variables
// register file
int regfile[32];
// instruction memory
int instmem[100];  // only support 100 static instructions
// data memory
int datamem[1024];
// program counter
int pc;
// int maxpc;
/* load
 *
 * Given the filename, which is a text file that 
 * contains the instructions stored as integers 
 *
 * You will need to load it into your global structure that 
 * stores all of the instructions.
 *
 * The return value is the maxpc - the number of instructions
 * in the file
 */
int load(char *filename)
{
  	FILE* file = fopen (filename, "r");
	if (file == NULL)
	{
		perror("cannot read file. \n");
		exit(1);
	}
  	int i = 0;
  	int j = 0;
  	fscanf (file, "%d", &i);
  	while (!feof (file))
    { 
	  instmem[j++] = i;
	  // printf("%d\n",instmem[j-1]); 
      fscanf (file, "%d", &i);      
    }
  	fclose (file); 
  	// maxpc=j;
	return j;
}

/* fetch
 *
 * This fetches the next instruction and updates the program counter.
 * "fetching" means filling in the inst field of the instruction.
 */
void fetch(InstInfo *instruction)
{
	instruction->inst = instmem[pc];
	instruction->pc = pc++;
	// printf( "\npc: %d\n", pc);
		// helper variables
	int val = instruction->inst;
	int op = (val >> 26) & 0x03f;

	int func = val & 0x03f;
	//fields
	int rs = (val >> 21) & 0x01f;
	int rt = (val >> 16) & 0x01f;
	int rd = (val >> 11) & 0x01f;
	short imm = (val & 0x0ffff);
	//signals
	switch (op)
	{
		case addi:
		{
			sprintf(instruction->string,"addi $%d, $%d, %d",
				rt, rs, 
				imm);
			break;
		}
		case blt:
		{
			sprintf(instruction->string,"blt $%d, $%d, %d",
				rs, rt, 
				imm);
			break;
		}
		case same_op:
		{
			switch(func)
			{
				case and_func:
				{
					sprintf(instruction->string,"and $%d, $%d, $%d",
						rd, rs, 
						rt);
					break;
				}
				case sub_func:
				{
					sprintf(instruction->string,"sub $%d, $%d, $%d",
						rd, rs, 
						rt);
					break;
				}
				case sgt_func:
				{
					sprintf(instruction->string,"sgt $%d, $%d, $%d",
						rd, rs, 
						rt);
					break;
				}
			}
			break;
		}//eof case same_op
		case lw:
		{
			sprintf(instruction->string,"lw $%d, %d($%d)",
				rt, imm, 
				rs);
			break;
		}
		case sw:
 		{
			sprintf(instruction->string,"sw $%d,%d($%d)",
				rt, imm, 
				rs);
			break;
		}
		case jr:
		{
			sprintf(instruction->string,"jr $%d",
				rs);			
			break;
		}
		case jal:
		{
			sprintf(instruction->string,"jal %d",
				imm);
			break;
		}
	}	
	// decode(instruction);
}

/* decode
 *
 * This decodes an instruction.  It looks at the inst field of the 
 * instruction.  Then it decodes the fields into the fields data 
 * member.  The first one is given to you.
 *
 * Then it checks the op code.  Depending on what the opcode is, it
 * fills in all of the signals for that instruction.
 */
// int signExt(int immediate)
// {
// 	// if ((immediate >> 15) & 0x01)
// 	// {
// 	int value = (0x0000FFFF & immediate);
//     int mask = 0x00008000;
//     if (mask & immediate) {
//         value += 0xFFFF0000;
//     }
//     // printf("\nvalue: %d\n",value);
//     return value;
// 	// }
// }

void decode(InstInfo *instruction)
{
	// helper variables
	int val = instruction->inst;
	int op, func;
	short imm;
	instruction->fields.op = (val >> 26) & 0x03f;
	

	op = instruction->fields.op;
	// if (op==same_op)
	// {
	instruction->fields.func = val & 0x03f;
	func  = instruction->fields.func;
	// }
	// printf("\nop:%d\n",op);
	// printf("\nfunc:%d\n",func);
	// op = addi;
	// hard-coded tests for each funciton signal/field
	//fields
	instruction->fields.rs = (val >> 21) & 0x01f;
	instruction->fields.rt = (val >> 16) & 0x01f;
	instruction->fields.rd = (val >> 11) & 0x01f;
	imm = (val & 0x0ffff);
	instruction->fields.imm = imm;//signExt((val & 0x0ffff));
	//signals
	switch (op)
	{
		case addi:
		{
			instruction->signals.aluop = 0;
			instruction->signals.mw = 0;
			instruction->signals.mr = 0;
			instruction->signals.mtr = 0;
			instruction->signals.asrc = 1;
			instruction->signals.btype = 0;
			instruction->signals.rdst = 0;
			instruction->signals.rw = 1;
			sprintf(instruction->string,"addi $%d, $%d, %d",
				instruction->fields.rt, instruction->fields.rs, 
				instruction->fields.imm);
			break;
		}
		case blt:
		{
			instruction->signals.aluop = 7;
			instruction->signals.mw = 0;
			instruction->signals.mr = 0;
			instruction->signals.mtr = -1;
			instruction->signals.asrc = 0;
			instruction->signals.btype = 3;
			instruction->signals.rdst = -1;
			instruction->signals.rw = 0;
			sprintf(instruction->string,"blt $%d, $%d, %d",
				instruction->fields.rs, instruction->fields.rt, 
				instruction->fields.imm);
			break;
		}
		case same_op:
		{
			switch(func)
			{
				case and_func:
				{
					instruction->signals.aluop = 2;
					instruction->signals.mw = 0;
					instruction->signals.mr = 0;
					instruction->signals.mtr = 0;
					instruction->signals.asrc = 0;
					instruction->signals.btype = 0;
					instruction->signals.rdst = 1;
					instruction->signals.rw = 1;
					sprintf(instruction->string,"and $%d, $%d, $%d",
						instruction->fields.rd, instruction->fields.rs, 
						instruction->fields.rt);
					break;
				}
				case sub_func:
				{
					instruction->signals.aluop = 1;
					instruction->signals.mw = 0;
					instruction->signals.mr = 0;
					instruction->signals.mtr = 0;
					instruction->signals.asrc = 0;
					instruction->signals.btype = 0;
					instruction->signals.rdst = 1;
					instruction->signals.rw = 1;
					sprintf(instruction->string,"sub $%d, $%d, $%d",
						instruction->fields.rd, instruction->fields.rs, 
						instruction->fields.rt);
					break;
				}
				case sgt_func:
				{
					instruction->signals.aluop = 6;
					instruction->signals.mw = 0;
					instruction->signals.mr = 0;
					instruction->signals.mtr = 0;
					instruction->signals.asrc = 0;
					instruction->signals.btype = 0;
					instruction->signals.rdst = 1;
					instruction->signals.rw = 1;
					sprintf(instruction->string,"sgt $%d, $%d, $%d",
						instruction->fields.rd, instruction->fields.rs, 
						instruction->fields.rt);
					break;
				}
			}
			break;
		}//eof case same_op
		case lw:
		{
			instruction->signals.aluop = 0;
			instruction->signals.mw = 0;
			instruction->signals.mr = 1;
			instruction->signals.mtr = 1;
			instruction->signals.asrc = 1;
			instruction->signals.btype = 0;
			instruction->signals.rdst = 0;
			instruction->signals.rw = 1;
			sprintf(instruction->string,"lw $%d, %d($%d)",
				instruction->fields.rt, instruction->fields.imm, 
				instruction->fields.rs);
			break;
		}
		case sw:
 		{
			instruction->signals.aluop = 0;
			instruction->signals.mw = 1;
			instruction->signals.mr = 0;
			instruction->signals.mtr = -1;
			instruction->signals.asrc = 1;
			instruction->signals.btype = 0;
			instruction->signals.rdst = -1;
			instruction->signals.rw = 0;
			sprintf(instruction->string,"sw $%d,%d($%d)",
				instruction->fields.rt, instruction->fields.imm, 
				instruction->fields.rs);
			break;
		}
		case jr:
		{
			instruction->signals.aluop = -1;
			instruction->signals.mw = 0;
			instruction->signals.mr = 0;
			instruction->signals.mtr = -1;
			instruction->signals.asrc = -1;
			instruction->signals.btype = 2;
			instruction->signals.rdst = -1;
			instruction->signals.rw = 0;
			sprintf(instruction->string,"jr $%d",
				instruction->fields.rs);			
			break;
		}
		case jal:
		{
			instruction->signals.aluop = -1;
			instruction->signals.mw = 0;
			instruction->signals.mr = 0;
			instruction->signals.mtr = 2;
			instruction->signals.asrc = -1;
			instruction->signals.btype = 1;
			instruction->signals.rdst = 2;
			instruction->signals.rw = 1;
			sprintf(instruction->string,"jal %d",
				instruction->fields.imm);
						// printf("\nimm:%d\n",instruction->fields.imm);

			break;
		}
	}	

	instruction->sourcereg=instruction->fields.rs;
	instruction->targetreg=instruction->fields.rt;
	if(instruction->signals.rdst==0)
		instruction->destreg=instruction->targetreg;
	else if(instruction->signals.rdst==1)
		instruction->destreg=instruction->fields.rd;
	else if(instruction->signals.rdst==2)
		instruction->destreg=31;
	// read from the register file
	instruction->s1data = regfile[instruction->fields.rs];
	instruction->s2data = regfile[instruction->fields.rt];
	instruction->input1 = instruction->s1data;
	// fill in s1data and input2
	if (instruction->signals.asrc==0)
		instruction->input2 = instruction->s2data;
	else if(instruction->signals.asrc==1)
		instruction->input2 = instruction->fields.imm;
	
	// printf("\ninput2:%d\n",instruction->input2);
	// printf("\ninput1:%d\n",instruction->input1);
	// setPCWithInfo(instruction);
	// execute(instruction);
	// printf("aluout: %d\n", instruction->aluout);
	setPCWithInfo(instruction);

}

/* execute
 *
 * This fills in the aluout value into the instruction and destdata
 */

void execute(InstInfo *instruction)
{
	// instruction->input1 = regfile[instruction->fields.rs];

	switch(instruction->signals.aluop)
	{
		case plus:
		{
			instruction->aluout = instruction->input1 + instruction->input2;
			// printf("\nALU\n");
			// printf("%d\n",instruction->input2);
			break;
		}
		case minus:
		{
			instruction->aluout=instruction->input1 - instruction->input2;
			break;
		}
		case and:
		{
			instruction->aluout=instruction->input1 & instruction->input2;
			// printf("\n%d\n", instruction->input1);
			// printf("\n%d\n", instruction->input2);
			// printf("\nyay!\n");
			break;
		}
		case or:
		{
			instruction->aluout=instruction->input1 | instruction->input2;
			break;
		}
		case sgt:
		{
			instruction->aluout=((instruction->input1 > instruction->input2)?1:0);
			break;
		}
		case slt:
		{
			instruction->aluout=((instruction->input1 < instruction->input2)?1:0);
			break;
		}
	}
	// printf("aluout: %d\n",instruction->aluout);
	// printf("--------------executed----------\n");
}

/* memory
 *
 * If this is a load or a store, perform the memory operation
 */
void memory(InstInfo *instruction)
{
	// if (instruction->fields.op==lw || instruction->fields.op==sw)
	// {
		if (instruction->signals.mr==1)
		{
			instruction->memout=datamem[instruction->aluout];
			// regfile[instruction->destreg]=instruction->memout;
			instruction->destdata=instruction->memout;
			// printf("s1data: %i\n", instruction->input1);
			// printf("aluout: %i\n", instruction->aluout);

		}
		// else if (instruction->signals.mr==0)
		// {
		// 	instruction->memout=-1;
		// }
		if (instruction->signals.mw==1)
		{
			instruction->input2 = regfile[instruction->targetreg];
			datamem[instruction->aluout]=instruction->input2;
			instruction->destdata=instruction->input2;
			// printf("storing location: %d\n", instruction->aluout);
			// printf("value stored: %d\n", instruction->input2);
			// printf("input1: %d\n", instruction->input1);
			// printf("imm: %d\n", instruction->fields.imm);
			// printf("imm: %i\n", instruction->fields.imm);
			// printf("s1data: %i\n", instruction->input1);
			// printf("aluout: %i\n", instruction->aluout);
			// printf("s2data: %i\n", instruction->input2);
		}
	// }
		// writeback(instruction);
}

/* writeback   
 *
 * If a register file is supposed to be written, write to it now
 */
void writeback(InstInfo *instruction)
{
	if (instruction->signals.rw==1)
	{
		switch(instruction->signals.mtr)
		{
			case 0:
			{
				instruction->destdata=instruction->aluout;
				// printf("yes");
				// printf("%d",instruction->aluout);
				break;
			}
			case 1:
			{
				instruction->destdata=instruction->memout;
				break;
			}
			case 2:
			{
				instruction->destdata=instruction->pc+1;
				break;
			}
		}
		// printf("destdata: %d\n", instruction->destdata);
		regfile[instruction->destreg] = instruction->destdata;
		// switch (instruction->signals.rdst)
		// {
		// 	case 0://rt field used
		// 		if (instruction->signals.mtr==1)
		// 			regfile[instruction->fields.rt]=instruction->memout;
		// 		else if (instruction->signals.mtr==0)
		// 			regfile[instruction->fields.rt]=instruction->aluout;
		// 		break;
		// 	case 1://rd field used
		// 		regfile[instruction->fields.rd]=instruction->aluout;
		// 		// printf("\n yay:%d\n",instruction->aluout);
		// 		break;
		// 	case 2://$ra ($31) used
		// 		regfile[31]=instruction->pc+1;
		// 		break;
		// }
		}

}

void setPCWithInfo(InstInfo *instruction)
{
	switch (instruction->signals.btype)
	{
		case blt_type:
			instruction->aluout=((instruction->input1 < instruction->input2)?1:0);
			if (instruction->aluout==1)
			{
				pc=pc+instruction->fields.imm-1;//signExt(instruction->fields.imm);
				// fetch(instruction);
				// printf("jumped to: %s\n", instruction->string);
				// sprintf(instruction->string,"");
				// printf("\nyes\n");
				// instruction->pc = pc;
			}
			break;
		case jr_type:
			pc=instruction->s1data;
			// instruction->pc = pc;
			// printf("\nyes\n");
			break;
		case jal_type:
			// if ()
			pc=instruction->fields.imm;
			// printf("\nimm:%d\n",instruction->fields.imm);
			// instruction->pc = pc;
			// printf("\nyes\n");
			break;
	}
}