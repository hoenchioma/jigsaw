package com.jigsaw.calendar;

import java.util.Comparator;

public class PriorityComparator implements Comparator<Task> {
    public int compare(Task a, Task b) {
        if (a.getPriority() > b.getPriority()) return -1;
        else if (a.getPriority() < b.getPriority()) return 1;
        return 0;
    }
}
