package Scheduler;


import java.util.*;
public class ProcessSchedulerThread extends Thread {

    //calculate and log turnarounds of all jobs processed
    ProcessTurnaround turnarounds = new ProcessTurnaround();
    //How many times we need to process jobs in our ProcessList
    int totalBurstTime;
    
    int speed;
    //Loaded in ProcessSchedulerGUI.btnRunSimulation()
    ProcessList pList;
    //jobs will be added, top-priority jobs run first
    PriorityQueue<Process> pQueue;
    List<Process> processList = new ArrayList<Process>();

    //Create a comparator for priority comparison among received Jobs to process
    Comparator<Process> comparator;
    //Gannt Chart
    String[] gChart;
    
    //Set the total burst time after calculation in ProcessSched
    public void setTotalBurstTime(int bt){this.totalBurstTime = bt;}
    //set simulation speed
    public void setSpeed(int speed){this.speed = speed;}
    //Set the processList to be processed
    public void setProcessList(ProcessList pl){this.pList = pl;}
    //Set the pQueue specifications
    public void setQueue(PriorityQueue<Process> processQueue){this.pQueue = processQueue;}
    //Set a custom Comparator
    public void setComparator(Comparator<Process> comparator){this.comparator = comparator;}
    //Set the Gannt gChart specifications
    public void setChart(String[] chart){this.gChart = chart;}  
    //Create a Gannt Chart of the current simulation to be printed at the end of processing all Jobs
    public void makeChart(String[] chart, String process, int i){chart[i] = process;}
    //Display the Gannt gChart created by running the simulation.
    public void displayChart(String[] chart){for (String chart1 : chart) {System.out.print(chart1);}}
     
    public void run(){
        int turnaroundAvg;
        int turnaroundTotal = 0;
        //How many jobs need to be logged
        int total_jobs = pList.getProcessCount();
        int processType = 0;
        
        turnarounds.setProcessTotal(total_jobs);
        //Set the earliest arrival to know how long to burst
        pList.setEarliestArrival();
        Process nextProcess, newProcess, currProcess;
        //Ensure that ghost bursts don't prematurely end program.
        int CPUBurstTime = totalBurstTime + pList.getEarliestArrival();
        String schedType = "";
        int[] pTypeArray = new int[CPUBurstTime];
        int TimeQuantum = -1;
        String[] ScheduleType = new String[CPUBurstTime];
        //Take user input for the Time Quantum
        
        while(TimeQuantum < 0) {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Please enter the Time Quantum: ");
                TimeQuantum = scanner.nextInt();
                scanner.close();
        }
        
        System.out.println("CPUBurstTime:"+ CPUBurstTime);
        //Run the simulation from 0 to the end of all job CPU bursts.
        for(int i = 0; i < CPUBurstTime; i++){
            System.out.print("\nTimestamp: " + Integer.toString(i));
            
            //For every process in pList, check if any are arriving at the current CPU timestamp
            for(int k = 0; k < pList.getProcessCount(); k++){
                currProcess = pList.getProcessAt(k);
                int m = 0;
                if(k < pList.getProcessCount()-1){
                    m = k + 1;
                }
                nextProcess = pList.getProcessAt(m);
                    //if(nextProcess.getPriority() == currProcess.getPriority()){
                /*    if(comparator.compare(currProcess, pQueue.peek()) == 0){
                        schedType = ("Round Robin, Quantum ("+ TimeQuantum + ")");
                    }
                    else {
                        schedType = "Premptive Priority";
                    }
                    ScheduleType[k] = schedType;
                }*/
                //If Process arrives at timestamp i, print process name, add to pQueue
                if(currProcess.getArrival() == i){
                    System.out.print("\tProcess " + currProcess.getName() + " arrived\n\t");
                    pQueue.add(currProcess);
                    currProcess.setNext(nextProcess);
                }                               
            }
            //If pQueue is empty,'-' is entered.
            if(pQueue.isEmpty()){ makeChart(gChart, "|-", i);}
            
             //Else, peek outgoing process into gChart,then remove process from pQueue
            else {
                if((comparator.compare(pQueue.peek(), pQueue.peek().getNext()) == 0)){// && !(pQueue.peek().getName().equalsIgnoreCase(pQueue.peek().getNext().getName()))){  
                    System.out.print("\tRound Robin, Quantum (" + TimeQuantum + "): Process " + pQueue.peek().getName());
                    makeChart(gChart, "|" + pQueue.peek().getName(), i);
                    //makeChart(gChart, "|" + pQueue.peek().getName(), i+1);
                    pQueue.peek().setBurst(pQueue.peek().getBurst()-1);
                    //pQueue.peek().setPriority(pQueue.peek().getPriority()-1);
            
                } else {
                    System.out.print("\tPremptive Priority: Process " + pQueue.peek().getName());
                    makeChart(gChart, "|" + pQueue.peek().getName(), i);
                    //Decrement the amount of remaining burst time the Process should have
                    pQueue.peek().setBurst(pQueue.peek().getBurst()-1);
                }
            
                //If the peeking Process has no more CPU Burst remaining...
                if (pQueue.peek().getBurst() == 0){
                    //Set exit time for the process to calculate its turnaround
                    pQueue.peek().setExit(i);
                    Process currentProcess = pQueue.peek();
                    int turnaround = currentProcess.getExit() - currentProcess.getArrival();
                    turnaroundTotal += turnaround;
                    System.out.print(", turnaround: " + turnaround);
                    String turnaroundLog = "\nProcess " + currentProcess.getName() + 
                        "::\tEnd time: " + i + "\tTurn-around time: " + turnaround;
                    turnarounds.addTurnaround(turnaroundLog);
                    //Remove Process from Process Queue
                    pQueue.remove();
                    if(pQueue.peek() != null){
                        pQueue.peek().setNext(pQueue.peek());
                    }
                    
                }                   
            }
        }
        
        int x = turnarounds.getProcessTotal();
        double y = (double)x;
        turnaroundAvg = turnaroundTotal / x;
        //Interrupt the thread and return after simulation is completed
        Thread.currentThread().interrupt();
        //turnarounds.listTurnarounds();
        System.out.println("\nAverage turn-around time: " + turnaroundAvg + ", Total turn-around time: " + turnaroundTotal+", Number processes:" + y);
        displayChart(gChart);
        System.out.println("\nSimulation Completed.");
    }
    public static void main(String args[]) {(new ProcessSchedulerThread()).start();}
}