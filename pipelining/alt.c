if (instArray[2]->signals.mr==1 && (instArray[2]->targetreg==instArray[1]->fields.rs || instArray[2]->targetreg == instArray[1]->fields.rt)){
			stall=1;
		}
if(stall==1){
			stall=0;
			instArray[2]=instArray[5];
			pc--;
		}
		else{
			instArray[2]=instArray[1];
			instArray[1]=instArray[0];
		}