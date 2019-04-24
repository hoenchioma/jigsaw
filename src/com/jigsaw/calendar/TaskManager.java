package com.jigsaw.calendar;

import javafx.collections.ObservableList;

import java.io.Serializable;
import java.util.ArrayList;

public class TaskManager implements Serializable {
    private ArrayList<ProjectTask> projectTasks;

    private ObservableList<ProjectTask> willDoList;
    private ObservableList<ProjectTask> doingList;
    private ObservableList<ProjectTask> doneList;

    public TaskManager(){
        projectTasks = new ArrayList<ProjectTask>();
    }

    public void addTask(ProjectTask projectTask){
        this.projectTasks.add(projectTask);
        this.willDoList.add(projectTask);
    }

    public void removeTask(ProjectTask projectTask){
        this.projectTasks.remove(projectTask);
        this.willDoList.remove(projectTask);
        this.doingList.remove(projectTask);
        this.doneList.remove(projectTask);
    }

    //public void sync(SharedHandler handler ){  ///handle sync

    //}
}
