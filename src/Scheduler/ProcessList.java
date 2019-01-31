package Scheduler;



public class ProcessList {
    private Process[] processArray;
    private int earliestArrival, processCount, processIndex = 0;
    
    //Add processArray to ProcessList
    public void addProcess(Process newProcess){processArray[processIndex] = newProcess; ++processIndex;}
    //Peek at a specific job in ProcessList
    public Process getProcessAt(int index){return processArray[index];}
    //Verify the number of jobs the program will process
    public int getProcessCount(){return processCount;}
    //Re-verify number of processArrays being processed with length of ProcessList
    public int getProcessListSize(){return processArray.length;}
    //Obtain process that will arrive first
    public int getEarliestArrival(){return this.earliestArrival;}
    
    //Initialize and allocate space for the processArray array
    private void initProcess(){processArray = new Process[processCount];}    
    //Set the number of processArray that will be in ProcessList
    public void setProcessCount(int count){processCount = count; initProcess();}
    //Set which process begins first in order to ensure CPU runs through entire burst
    public void setEarliestArrival(){
        earliestArrival = processArray[0].getArrival();
        for(int i = 0; i < processCount; i++){
            if (processArray[i].getArrival() < this.earliestArrival){
                this.earliestArrival = processArray[i].getArrival();
            }
        }
    }  
}
