package com.jigsaw.calendar;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class TaskManager implements Serializable {
    private ArrayList<ProjectTask> projectTasks;

    transient private ObservableList<ProjectTask> willDoList = FXCollections.observableArrayList();
    transient private ObservableList<ProjectTask> doingList = FXCollections.observableArrayList();
    transient private ObservableList<ProjectTask> doneList = FXCollections.observableArrayList();

    public TaskManager(){
        projectTasks = new ArrayList<>();
        initialize();
    }

    public void initialize() {
        for (ProjectTask task: projectTasks) {
            if (task.getProgress() == Progress.willdo) {
                willDoList.add(task);
            } else if (task.getProgress() == Progress.doing) {
                doingList.add(task);
            } else {
                doneList.add(task);
            }
        }
    }

    public ArrayList<ProjectTask> getProjectTasks() {
        return projectTasks;
    }

    public void setProjectTasks(ArrayList<ProjectTask> projectTasks) {
        this.projectTasks = projectTasks;
    }

    public void addTask(ProjectTask projectTask){
        this.projectTasks.add(projectTask);
        if (projectTask.getProgress() == Progress.willdo) {
            willDoList.add(projectTask);
        } else if (projectTask.getProgress() == Progress.doing) {
            doneList.add(projectTask);
        } else {
            doneList.add(projectTask);
        }
    }

    public void removeTask(ProjectTask projectTask){
        this.projectTasks.remove(projectTask);
        this.willDoList.remove(projectTask);
        this.doingList.remove(projectTask);
        this.doneList.remove(projectTask);
    }

    public void updateTask(ProjectTask projectTask) {
        removeTask(projectTask);
        addTask(projectTask);
    }

    private void readObject(ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        initialize();
    }
}
