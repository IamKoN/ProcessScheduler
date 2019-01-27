package Scheduler;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ProcessTurnaround {
    int processTotal = 0, turnaroundCount = 0;
    int turnaroundAvg = 0;
    String[] turnaroundArray;

    public void setProcessTotal(int processCount){
        this.processTotal = processCount;
        this.turnaroundArray = new String[this.processTotal];
    }
    public int getProcessTotal(){
        return this.processTotal;
    }
    public void addTurnaround(String turnaround){
        turnaroundArray[turnaroundCount] = turnaround;
        ++turnaroundCount;
    }
    public void listTurnarounds(){
        List<String> finalList = sortTurnarounds();
        for(int i = 0; i < this.processTotal; i++){
            System.out.print(finalList.get(i));
        }
    }
    public List<String> sortTurnarounds(){
        List<String> processTurnarounds = Arrays.stream(turnaroundArray).collect(Collectors.toList());
        processTurnarounds.sort(String::compareToIgnoreCase);
        return processTurnarounds;
    }
}
