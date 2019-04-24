package com.jigsaw.accounts;

import com.jigsaw.calendar.ProjectTask;

import java.io.Serializable;
import java.util.ArrayList;

public class Project implements Serializable {
    private String id;
    private String name;

    // ArrayList of usernames of project members
    private ArrayList<String> members;
    private ArrayList<ProjectTask> taskList = new ArrayList<>();

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

    public void addMember(User user) {
        members.add(user.getUsername());
    }

    public void addMemberByUsername(String username) {
        members.add(username);
    }

    public void removeUser(User user) {
        members.remove(user.getUsername());
    }

    public ArrayList<ProjectTask> getTaskList() {
        return taskList;
    }

    public void setTaskList(ArrayList<ProjectTask> taskList) {
        this.taskList = taskList;
    }
}