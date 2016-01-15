
#include <stdio.h>
#include <stdlib.h>
#include <strings.h>
#include "functions.h"

int main(int argc, char *argv[])
{
	int instnum = 0;
	int maxpc;
	FILE *program;
	if (argc != 2)
	{
		printf("Usage: sim filename\n");
		exit(0);
	}

	maxpc = load(argv[1]) - 1;

	InstInfo *instArray[6];

	InstInfo fetchInst;
	InstInfo decodeInst;
	InstInfo executeInst;
	InstInfo memoryInst;
	InstInfo writebackInst;
	InstInfo blankInst;

	instArray[0] = (InstInfo*)malloc (sizeof ( InstInfo));//&fetchInst;
	instArray[1] = (InstInfo*)malloc (sizeof ( InstInfo));//&decodeInst;
	instArray[2] = (InstInfo*)malloc (sizeof ( InstInfo));//&executeInst;
	instArray[3] = (InstInfo*)malloc (sizeof ( InstInfo));//&memoryInst;
	instArray[4] = (InstInfo*)malloc (sizeof ( InstInfo));//&writebackInst;
	instArray[5] = (InstInfo*)malloc (sizeof ( InstInfo));//&blankInst;

	int cycle = 0;
	int instructions_executed = 0;
	int forwarda;
	int forwardb;
	int where1;
	int where2;
	int stall = 0;
	int branchstall = 0;
	while(pc <= maxpc+4){
		// printf("instruction: %s input1: %d\n",instArray[2]->string,instArray[2]->sourcereg);
			// if (stall==1)
			// {
			// 	printP2(instArray[0], instArray[1], instArray[2], instArray[3], instArray[4], cycle);

			// }

		// if (stall==0)
		fetch(instArray[0]);

		// if (stall==1)
		// if (instArray[1]->signals.rw && instArray[3]->destreg != 0 && (instArray[1]->targetreg == instArray[3]->destreg || instArray[1]->sourcereg==instArray[3]->destreg))
		// {
		// 	// instArray[1]->input2=instArray[2]->
		// 	instArray[1]->input1=instArray[3]->memout;
		// 	printf("instruction: %s memout: %d\n",instArray[3]->string,instArray[3]->memout);
		// }


		// if (stall==0)
		decode(instArray[1]);
		// stall = 0;
		//detect lw and stall
		// if (instArray[1]->sourcereg!=0 && instArray[1]->targetreg!=0 && instArray[2]->destreg!=0)
		// if (instArray[2]->signals.mr && instArray[1]->destreg != 0 && (instArray[1]->sourcereg==instArray[2]->destreg||instArray[1]->targetreg==instArray[2]->destreg)&&stall==0)
		// {
		// 	// printP2(instArray[0], instArray[1], instArray[2], instArray[3], instArray[4], cycle);
		// 	// InstInfo *temp = instArray[4];
		// 	// instArray[4]=instArray[3];
		// 	// // printf("sourcereg: %d\n",instArray[2]->sourcereg);
		// 	// // printf("destreg: %d\n",instArray[3]->destreg);
		// 	// instArray[3]=instArray[2];
		// 	// instArray[2]=instArray[5];
		// if (instArray[2]->si)
		// 	// free(temp);
			
		// 	stall = 1;
		// 	// printf("stall detected: %s\n",instArray[1]->string);
		// 	// cycle++;
		// 	// continue;
		// }
		



				// if (stall==0)
		if (instArray[3]==instArray[5] && instArray[4]->signals.mr)
		if (instArray[2]->signals.rw && instArray[2]->destreg!=0 && instArray[2]->sourcereg==instArray[4]->destreg)
		{
			instArray[2]->input1=instArray[4]->memout;
			// printf("instruction: %s destreg: %d\n",instArray[4]->string,instArray[4]->destreg);
		}
		if (instArray[3]==instArray[5] && instArray[4]->signals.mr)
		if (instArray[2]->signals.rw && instArray[2]->destreg!=0 && instArray[2]->targetreg==instArray[4]->destreg)
		{
			instArray[2]->input2=instArray[4]->memout;
			// printf("instruction: %s destreg: %d\n",instArray[4]->string,instArray[4]->destreg);
		}
		
		execute(instArray[2]);
		// printf("EX/ALU: %d\n", instArray[2]->aluout);
		if (instArray[2]->signals.mr==1 && (instArray[2]->targetreg==instArray[1]->fields.rs || instArray[2]->targetreg == instArray[1]->fields.rt)){
			stall=1;
		}

		// //Branch Dependencies
		// if (instArray[1]->signals.btype==3 && instArray[])
		// {
			
		// }
		
		// if (stall==0)
		// {
		if (instArray[1]->signals.btype==3 && instArray[1]->aluout==1)
		{
			branchstall=1;
		}

		//EX Hazard ForwardA
		if (instArray[2]->signals.rw && instArray[2]->destreg != 0 && instArray[1]->sourcereg == instArray[2]->destreg)
		{
			forwarda = 2;
			where1 = 2;
		}
		else if (instArray[2]->signals.rw && instArray[2]->destreg != 0 && instArray[1]->sourcereg == instArray[3]->destreg)
		{
			forwarda = 2;
			where1 = 3;
		}
		else if (instArray[2]->signals.rw && instArray[2]->destreg != 0 && instArray[1]->sourcereg == instArray[4]->destreg)
		{
			forwarda = 2;
			where1 = 4;
		}
		else
			forwarda = 0;

		//EX Hazard ForwardB
		if (instArray[2]->signals.rw && instArray[2]->destreg != 0 && instArray[1]->targetreg == instArray[2]->destreg)
		{
			forwardb = 2;
			where2 = 2;
		}
		else if (instArray[2]->signals.rw && instArray[2]->destreg != 0 && instArray[1]->targetreg == instArray[3]->destreg)
		{
			forwardb = 2;
			where2 = 3;
		}
		else if (instArray[2]->signals.rw && instArray[2]->destreg != 0 && instArray[1]->targetreg == instArray[4]->destreg)
		{
			forwardb = 2;
			where2 = 4;
		}
		else
			forwardb = 0;

		//Mem Hazard ForwardA




		if (forwarda == 2)
		{
			if (instArray[1]->signals.btype==3)
			{
				// printf("instruction: %s input1: %d input2 %d\n",instArray[1]->string,instArray[1]->input1,instArray[1]->input2);
				branchstall=2;
			}
			// printf("instruction: %s\n input1old: %d\n input1new: %d\n",instArray[1]->string,instArray[1]->input1,instArray[where1]->input1);
			if (instArray[1]->signals.mr!=1)
				instArray[1]->input1=instArray[where1]->aluout;
			
		}
		if (forwardb == 2)
		{
			if (instArray[1]->signals.btype==3)
			{
				// printf("instruction: %s input1: %d input2 %d\n",instArray[1]->string,instArray[1]->input1,instArray[1]->input2);
				branchstall=2;
			}
			// printf("instruction: %s\n input1old: %d\n input1new: %d\n",instArray[1]->string,instArray[1]->input2,instArray[where2]->input2);
			if (instArray[1]->signals.mr!=1)
				instArray[1]->input2=instArray[where2]->aluout;
		}

		if (instArray[1]->signals.mr && instArray[1]->sourcereg!=0 && instArray[1]->sourcereg == instArray[2]->targetreg)
		{

			instArray[1]->input1=instArray[2]->targetreg;
			// instArray[1]->memout=instArray[2]->input2;
			// printf("instrname: %s input1setting: %d input2settings: %d aluout: %d\n",instArray[2]->string,instArray[2]->input1,instArray[2]->input2,instArray[2]->aluout);
		}
		// if (instArray[2]->signals.rw && instArray[4]->destreg == instArray[])
		// printf("instrname: %s input1setting: %d input2settings: %d aluout: %d memout: %d\n",instArray[4]->string,instArray[4]->input1,instArray[4]->input2,instArray[4]->aluout,instArray[4]->memout);
		// }
		// if (stall==0)
		if (instArray[1]->signals.mr && instArray[1]->sourcereg == instArray[3]->targetreg)
		{
			// printf("instruction: %s\n input1old: %d\n input1new: %d\n",instArray[1]->string,instArray[1]->input2,instArray[4]->input2);
			instArray[1]->input1=instArray[3]->input2;
		// 	// printf("instrname: %s input1setting: %d input2settings: %d\n",instArray[3]->string,instArray[4]->input1,instArray[4]->input2);
		}
		memory(instArray[3]);
		// if (instArray[3]->signals.rw && instArray[3]->destreg != 0 && instArray[1]->targetreg == instArray[3]->destreg)
		// {
		// 	forwardb = 1;
		// 	where2 = 3;
		// }
		// else if (instArray[3]->signals.rw && instArray[3]->destreg != 0 && instArray[1]->targetreg == instArray[4]->destreg)
		// {
		// 	forwardb = 1;
		// 	where2 = 4;
		// }
		// else
		// 	forwardb = 0;

		// if (instArray[1]->destreg!=0)
		// if (instArray[3]->signals.mr && instArray[3]->destreg == instArray[1]->sourcereg)
		// {
		// 	instArray[1]->input1=instArray[3]->memout;
		// }
		// printf("instrname: %s input1setting: %d input2settings: %d\n",instArray[3]->string,instArray[3]->input1,instArray[3]->input2);
		// if (stall==0)

		// printf("instruction: %s memout: %d\n",instArray[4]->string,instArray[4]->memout);
		// if (stall==0)
		writeback(instArray[4]);

		// if (stall==0)
		if (strcmp(instArray[4]->string,"")!=0)
		{
			instructions_executed++;
		}
		printP2(instArray[0], instArray[1], instArray[2], instArray[3], instArray[4], cycle);
		
		// printf("input1 of lw: %d\n", instArray[4]->input1);
		// if (stall == 0)
		// {
		// printf("instrname: %s input1setting: %d input2settings: %d aluout: %d\n",instArray[1]->string,instArray[1]->input1,instArray[1]->input2,instArray[1]->aluout);
		// printf("instrname: %s input1: %d input2: %d aluout: %d\n",instArray[2]->string,instArray[2]->input1,instArray[2]->input2,instArray[2]->aluout);
		// int j = 0;
		// for (j = 0; j < 5; j++)
		// {
		// 	printf("aluout: %d\n",instArray[j]->aluout);
		// }
		// printf("instrname: %s input1setting: %d input2settings: %d aluout: %d\n",instArray[3]->string,instArray[3]->input1,instArray[3]->input2,instArray[3]->aluout);
		// printf("instrname: %s input1setting: %d input2settings: %d aluout: %d\n",instArray[4]->string,instArray[4]->input1,instArray[4]->input2,instArray[4]->aluout);
		InstInfo *temp = instArray[4];
		instArray[4]=instArray[3];
		instArray[3]=instArray[2];
		if (stall==0 && branchstall==0)
		{
			instArray[2]=instArray[1];
			instArray[1]=instArray[0];
		}
		if(stall==1){
			stall=0;
			instArray[2]=instArray[5];
			pc--;
		}
		if (branchstall==2)
		{
			branchstall=0;
			instArray[2]=instArray[5];
			// pc--;
			// instArray
		}
		if (branchstall==1)
		{
			branchstall=0;
			// printf("not taken branch: %s\n", instArray[0]->string);
			instArray[2]=instArray[1];
			instArray[1]=instArray[5];
			instArray[0]=(InstInfo*)malloc(sizeof(InstInfo));
			cycle++;
			continue;
			// pc--;
		}
		
		// instArray[2]=instArray[1];
		// instArray[1]=instArray[0];
		instArray[0]=(InstInfo*)malloc(sizeof(InstInfo));
		// free(temp);
		// }
		// else
		// {
			// InstInfo *temp = instArray[4];
			// instArray[4]=instArray[3];
			// // printf("sourcereg: %d\n",instArray[2]->sourcereg);
			// // printf("destreg: %d\n",instArray[3]->destreg);
			// instArray[3]=instArray[2];
			// instArray[2]=instArray[5];

			// free(temp);
			// stall = 0;
		// }
				// printf("Fetch: %s\n",instArray[0]->string);
		// printf("Decode: %s\n",instArray[1]->string);
		// printf("Exeute: %s\n",instArray[2]->string);
		// printf("Memory: %s\n",instArray[3]->string);
		// printf("WriteBack: %s\n",instArray[4]->string);
		// printf("\n");

		if (instArray[0]->pc >= maxpc)
			instArray[0]=instArray[5];
		if (instArray[1]->pc >= maxpc)
			instArray[0]=instArray[5];
		if (instArray[2]->pc >= maxpc)
			instArray[0]=instArray[5];
		if (instArray[3]->pc >= maxpc)
			instArray[0]=instArray[5];

	// 	int i = 0;
	// 	printf("datamem: ");
	// for (i=0; i < 30; i++)
	// printf("%d ",datamem[i]);
	// printf("\n");
	// 	printf("regfile: ");
	// for (i=0; i < 32; i++)
	// printf("%d",regfile[i]);
	// printf("\n");
		// printf("Deleted: %s\n", temp->string);
		cycle++;
		// printf("aluout: %d\n",instArray[4]->aluout);
		// printf("datamem: %d\n",datamem[4]);

	}
	// printf("datamem: %d\n",datamem[12]);
	// int i = 0;
	// for (i=0; i < 20; i++)
	// printf("%d",datamem[i]);
	// printf("\n");
	printf("Cycles: %d\n",cycle);
	printf("Instructions Executed: %d\n",instructions_executed);
	exit(0);
}

 


/*
 * print out the loaded instructions.  This is to verify your loader
 * works.
 */
void printLoad(int max)
{
	int i;
	for(i=0;i<max;i++)
		printf("%d\n",instmem[i]);
}

/* print
 *
 * prints out the state of the simulator after each instruction
 */
void print(InstInfo *inst, int count)
{
	int i, j;
	printf("Instruction %d: %d\n",count,inst->inst);
	printf("%s\n\n",inst->string);
	printf("Fields:\n rd: %d\nrs: %d\nrt: %d\nimm: %d\n\n",
		inst->fields.rd, inst->fields.rs, inst->fields.rt, inst->fields.imm);
	printf("Control Bits:\nalu: %d\nmw: %d\nmr: %d\nmtr: %d\nasrc: %d\nbt: %d\nrdst: %d\nrw: %d\n\n",
		inst->signals.aluop, inst->signals.mw, inst->signals.mr, inst->signals.mtr, inst->signals.asrc,
		inst->signals.btype, inst->signals.rdst, inst->signals.rw);
	// if we are using the alu result, print out the alu output
	if (inst->signals.mtr == 0)
		printf("ALU Result: %d\n\n",inst->aluout);
	else
		printf("ALU Result: X\n\n",inst->aluout);
	// if we are using the mem result, print out the mem value
	if (inst->signals.mr == 1)
		printf("Mem Result: %d\n\n",inst->memout);
	else
		printf("Mem Result: X\n\n");
	for(i=0;i<8;i++)
	{
		for(j=0;j<32;j+=8)
			printf("$%d: %4d ",i+j,regfile[i+j]);
		printf("\n");
	}
	printf("\n");
}
