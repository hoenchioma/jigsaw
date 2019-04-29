/**
 *
 */
package com.jigsaw.calendar;

import com.jigsaw.accounts.Project;
import com.jigsaw.accounts.User;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class ProjectTask extends Task {
    private String projectID;
    transient private Project project;

    private ArrayList<String> assigneeIDs;
   // transient private ArrayList<User> assignees;

    public ProjectTask(String name, LocalDateTime deadline, String creatorUsername,
                       String projectID, ArrayList<String> assigneeIDs) {
        super(name, deadline, creatorUsername);
        this.projectID = projectID;
        this.assigneeIDs = assigneeIDs;
    }

    public ProjectTask(String name, String details, LocalDateTime deadline, String creatorUsername, int priority,
                       String projectID, ArrayList<String> assigneeIDs) {
        super(name, details, deadline, creatorUsername, priority);
        this.projectID = projectID;
        this.assigneeIDs = assigneeIDs;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    /*public ArrayList<User> getAssignees() {
        return assignees;
    }

    public void setAssignees(ArrayList<User> assignees) {
        this.assignees = assignees;
    }*/

    public ArrayList<String> getAssigneeIDs() {
        return assigneeIDs;
    }

    public void setAssigneeIDs(ArrayList<String> assigneeIDs) {
        this.assigneeIDs = assigneeIDs;
    }
}
