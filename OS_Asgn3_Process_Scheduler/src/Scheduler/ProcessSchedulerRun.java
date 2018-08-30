package Scheduler;

/*
Arrival times will be strictly increasing in the input file.
Your program should accept any valid input file 
There will be at most 100 processes.
Lower numbers imply higher priority, with the highest priority a 0 and the lowest priority a 9.
Your scheduler should be a preemptive priority scheduler with
round-robin as a secondary scheduling criteria. 
allow the user to vary the time quantum but a default of 2 should be used for quantum.
Include enough output of your program to show processes being dispatched, preempted, completed, etc.
After simulation, output the turnaround time for each process as well as average turnaround time.
*/

import java.io.*;
import java.util.*;

public class ProcessSchedulerRun {

    public static void main(String[] args) {
        //ProcessSchedulerRun();
        initialize();
    }
    //public ProcessSchedulerRun(){initialize();}
    //Initialize the contents of the frame
    private static void initialize() {
    //Create an array to auto-assign newProcess names as they come in to be processed.
        String[] jobNames = new String[100];

        
        //Fill jobNames array with the alphabet
        for(int i=65;i<=90;i++) { jobNames[i-65]= Character.toString((char)i);} 
        
        Scanner sc = new Scanner(System.in);
	System.out.print("Enter file name to open: ");
 	String inFile = sc.nextLine();
       
        //FileInputStream fstream = new FileInputStream(inFile);
        //DataInputStream inputFile = new DataInputStream(fstream);    
        int jobCountFlag = 0;
        int errorFlag = 0;
        int jobs_counted = 0;    
        //track how many jobs have been read so newProcess names can be assigned          
        
        System.out.println("Reading file...");
        try (BufferedReader br = new BufferedReader(new FileReader(inFile))) {
            String line; // where we will store each line from input file
            //Create the ProcessList that will be processed.
            ProcessList pList = new ProcessList();    
            //While there are more lines in the file, process them
            while ((line = br.readLine()) != null) {
                // Check if first line has been processed
                if(jobCountFlag == 0){
                    //Ensure the first line of the file is an integer.
                    int jobCount = 0;
                    try {
                        //If the first line is has integer value, go ahead and parse to an integer.
                        jobCount = Integer.parseInt(line);
                        //Set the ProcessCount variable in ProcessList to be the value in the first line
                        pList.setProcessCount(jobCount);
                        //Tell the program we've processed the first line.
                        jobCountFlag = 1;
                        System.out.println("Total number of processes to schedule: " + pList.getProcessCount());	
                    } catch (NumberFormatException e1) {
                        //If the first line does not have integer value, throw error.
                        if (errorFlag == 0){
                                System.out.println("Invalid input file selected");
                                errorFlag = 1;
                        }
                    }
                }
                // If first line has been processed, begin adding Jobs to ProcessList.
                else {
                    //Split the Process input data on all instances of whitespace
                    String[] jobAspects = line.split(" ");
                    //Create a new Process Object to add to our ProcessList per input line
                    Process newProcess = new Process();
                    //Set all aspects of our new newProcess: Arrival, Priority, Burst, and Name.
                    newProcess.setArrival(Integer.parseInt(jobAspects[0]));
                    newProcess.setPriority(Integer.parseInt(jobAspects[1]));
                    newProcess.setBurst(Integer.parseInt(jobAspects[2]));
                    newProcess.setName(jobNames[jobs_counted]);

                    //Add the newProcess to the ProcessList
                    pList.addProcess(newProcess);
                    //Increment jobs_counted. Note, we can process a maximum of 100 jobs.
                    jobs_counted++;
                }
            }
            runSimulation(pList);
        }catch (IOException e1) {e1.printStackTrace();}
    }

    //"Run Simulation" button loads the jobs, and processes the newProcess information
    public static void runSimulation(ProcessList jl){

        //Create a thread that will run our simulation
        ProcessSchedulerThread newSimulation = new ProcessSchedulerThread();

        //Calculate total burst time for all jobs to know how long to run the simulation.
        int totalBurstTime = 0;
        for(int i = 0; i < jl.getProcessCount(); i++){totalBurstTime += jl.getProcessAt(i).getBurst();}

        //Tell our thread the total burst time.
        newSimulation.setTotalBurstTime(totalBurstTime);

        //Create Gannt Chart structureto generate full chart after processing
        jl.setEarliestArrival();
        int CPUBurstTime = totalBurstTime + jl.getEarliestArrival();
        String[] chart = new String[CPUBurstTime];

        //Send our newly created Gannt Chart to our thread.
        newSimulation.setChart(chart);

        //Create a comparator for our PriorityQueue.
        Comparator<Process> comparator = new ProcessComparator();

        //Create a Priority Queue for our simulator.
        PriorityQueue<Process> jobQueue = new PriorityQueue<Process>(totalBurstTime, comparator);
        List<Process> jobList = new ArrayList<Process>(totalBurstTime);
        //Send any other pertinent information to our thread.
        //newSimulation.setTextArea(log);
        newSimulation.setProcessList(jl);
        newSimulation.setQueue(jobQueue);
        newSimulation.setComparator(comparator);
        newSimulation.setSpeed(10);
        newSimulation.start();
    }
}
