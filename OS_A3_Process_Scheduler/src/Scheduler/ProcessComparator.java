package Scheduler;


import java.util.Comparator;

public class ProcessComparator implements Comparator<Process>{
    @Override
    public int compare(Process o1, Process o2) {
        if (o1.getPriority() < o2.getPriority())
                return -1;
        if (o1.getPriority() > o2.getPriority())
                return 1;
        return 0;
    }
}