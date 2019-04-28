package com.jigsaw.calendar;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * A container class that holds the list of Project tasks
 *
 * @author Raheeb Hassan
 */
public class TaskManager implements Serializable {
    private ArrayList<ProjectTask> projectTasks;

    transient private ObservableList<ProjectTask> willDoList;
    transient private ObservableList<ProjectTask> doingList;
    transient private ObservableList<ProjectTask> doneList;

    public TaskManager(){
        projectTasks = new ArrayList<>();
        initialize();
    }

    public void initialize() {
        willDoList = FXCollections.observableArrayList();
        doingList = FXCollections.observableArrayList();
        doneList = FXCollections.observableArrayList();
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
        System.out.println(projectTask);
        if (projectTask.getProgress() == Progress.willdo) {
            willDoList.add(projectTask);
        } else if (projectTask.getProgress() == Progress.doing) {
            doingList.add(projectTask);
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

//    public static void main(String[] args) {
//        ProjectTask projectTask = new ProjectTask("yolo", LocalDateTime.now(), "bolo", "jigsaw", new ArrayList<>());
//        TaskManager taskManager = new TaskManager();
//        taskManager.addTask(projectTask);
//    }
}
