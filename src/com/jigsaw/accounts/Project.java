package com.jigsaw.accounts;

import com.jigsaw.calendar.ProjectTask;
import com.jigsaw.calendar.TaskManager;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class Project implements Serializable {
    private String id;
    private String name;
    private String description;
    private LocalDate projectCreateDate;
    private LocalDate projectDueDate;

    // ArrayList of usernames of project members
    private ArrayList<String> members;

    private TaskManager taskManager = new TaskManager();

    public Project() {
        this.projectCreateDate=LocalDate.now();
    }

    public Project(String id, String name, String description, LocalDate projectDueDate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.projectDueDate = projectDueDate;
        projectCreateDate = LocalDate.now();
        taskManager = new TaskManager();
        members = new ArrayList<>();
    }

    public LocalDate getProjectCreateDate() {
        return projectCreateDate;
    }

    public void setProjectCreateDate(LocalDate projectCreateDate) {
        this.projectCreateDate = projectCreateDate;
    }

    public LocalDate getProjectDueDate() {
        return projectDueDate;
    }

    public void setProjectDueDate(LocalDate projectDueDate) {
        this.projectDueDate = projectDueDate;
    }

    public void setMembers(ArrayList<String> members) {
        this.members = members;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getMembers() {
        return members;
    }

    public void addMemberIfAbsent(User user) {
        addMemberByUsernameIfAbsent(user.getUsername());
    }

    public void addMemberByUsernameIfAbsent(String username) {
        if (!members.contains(username)) {
            members.add(username);
        }
    }

    public void removeUser(User user) {
        if (members.contains(user.getUsername())) {
            members.remove(user.getUsername());
        } else {
            throw new IllegalStateException(user.getUsername() + " doesn't exist in list");
        }
    }

    public ArrayList<ProjectTask> getTaskList() {
        return taskManager.getProjectTasks();
    }

    public TaskManager getTaskManager() {
        return taskManager;
    }

    synchronized public void setTaskManager(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    synchronized public void saveToFile() {
        Resource.getInstance().updateProject(this);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}