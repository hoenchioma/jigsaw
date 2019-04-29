package com.jigsaw.calendar;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * A container class that holds the list of Project tasks
 *
 * @version %I%, %G%
 * @author Raheeb Hassan
 */
public class TaskManager implements Serializable {
    private ArrayList<ProjectTask> projectTasks;

    public TaskManager() {
        projectTasks = new ArrayList<>();
    }

    public ArrayList<ProjectTask> getProjectTasks() {
        return projectTasks;
    }

    public void setProjectTasks(ArrayList<ProjectTask> projectTasks) {
        this.projectTasks = projectTasks;
    }

    public void addTask(ProjectTask projectTask) {
        this.projectTasks.add(projectTask);
    }

    public void removeTask(ProjectTask projectTask) {
        this.projectTasks.remove(projectTask);
    }

    public void updateTask(ProjectTask projectTask) {
        removeTask(projectTask);
        addTask(projectTask);
    }

    private void readObject(ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        in.defaultReadObject();
    }

//    public static void main(String[] args) {
//        ProjectTask projectTask = new ProjectTask("yolo", LocalDateTime.now(), "bolo", "jigsaw", new ArrayList<>());
//        TaskManager taskManager = new TaskManager();
//        taskManager.addTask(projectTask);
//    }
}
