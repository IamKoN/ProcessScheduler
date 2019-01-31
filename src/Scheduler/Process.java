package Scheduler;



public class Process {
    private int exit, burst, arrival, priority;
    private String job_name;
    private Process nextProcess;
    
    public int getExit(){return this.exit;}
    public int getBurst(){return this.burst;} 
    public int getArrival(){return this.arrival;}
    public int getPriority(){return this.priority;}
    public String getName(){return this.job_name;}
    public Process getNext(){return nextProcess;}
    
    public void setExit(int exit_received){this.exit = exit_received;}
    public void setBurst(int burst_received){this.burst = burst_received;}
    public void setArrival(int arrival_received){this.arrival = arrival_received;}
    public void setPriority(int priority_received){this.priority = priority_received;}
    public void setName(String name_received){this.job_name = name_received;}
    public void setNext(Process nextP){this.nextProcess = nextP;}
}
