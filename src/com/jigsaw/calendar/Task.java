package com.jigsaw.calendar;

import java.time.LocalDateTime;
import java.util.Comparator;

enum Progress { willdo, doing, done };

public class Task {
    private static int DEFAULT_PRIORITY = 1;

    private String name;
    private String details = "";
    private LocalDateTime timeAssigned;
    private LocalDateTime deadline;
    private String creatorUsername;
    private int priority;
    private Progress progress = Progress.willdo;
    private int progressPercentage = 0;

    public Task(String name, LocalDateTime deadline, String creatorUsername) {
        this.name = name;
        this.deadline = deadline;
        this.creatorUsername = creatorUsername;
        this.timeAssigned = LocalDateTime.now();
        this.priority = DEFAULT_PRIORITY;
    }

    public Task(String name, String details, LocalDateTime deadline, String creatorUsername, int priority) {
        this.name = name;
        this.details = details;
        this.deadline = deadline;
        this.creatorUsername = creatorUsername;
        this.priority = priority;
        this.timeAssigned = LocalDateTime.now();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public LocalDateTime getTimeAssigned() {
        return timeAssigned;
    }

    public void setTimeAssigned(LocalDateTime timeAssigned) {
        this.timeAssigned = timeAssigned;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public String getCreatorUsername() {
        return creatorUsername;
    }

    public void setCreatorUsername(String creatorUsername) {
        this.creatorUsername = creatorUsername;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Progress getProgress() {
        return progress;
    }

    public void setProgress(Progress progress) {
        this.progress = progress;
    }

    public int getProgressPercentage() {
        return progressPercentage;
    }

    public void setProgressPercentage(int progressPercentage) {
        this.progressPercentage = progressPercentage;
    }
}
