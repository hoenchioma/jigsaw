package com.jigsaw.calendar.sync;

import com.jigsaw.accounts.User;
import com.jigsaw.calendar.ProjectTask;
import com.jigsaw.calendar.TaskManager;
import com.jigsaw.network.Packet;
import com.jigsaw.network.client.NetClient;

import java.io.IOException;
import java.util.Map;

public class ClientTaskSyncHandler {
    private volatile TaskManager taskManager;
    private Map<String, User> userDictionary;

    private final Object monitor = new Object();

    public ClientTaskSyncHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    /**
     * Processes incoming packet
     */
    public void receivePacket(Packet packet) {
        TaskPacket receivedTaskPacket = (TaskPacket) packet;
        // initial request for task manager
        if (receivedTaskPacket.command.equals("reset")) {
            taskManager = receivedTaskPacket.taskManager;
            userDictionary = receivedTaskPacket.projectMemberInfo;
            // notify other threads that the task manager is now set
            synchronized (monitor) { monitor.notify(); }
            log("initial task download finished");
        }
        else if (receivedTaskPacket.command.equals("add task")) {
            addTaskLocal(receivedTaskPacket.task);
        }
        else if (receivedTaskPacket.command.equals("remove task")) {
            removeTaskLocal(receivedTaskPacket.task);
        }
        else if (receivedTaskPacket.command.equals("update task")) {
            updateTaskLocal(receivedTaskPacket.task);
        }
        System.out.println(taskManager.getProjectTasks());
    }

    private void addTaskLocal(ProjectTask task) {
        taskManager.addTask(task);
    }

    private void removeTaskLocal(ProjectTask task) {
        taskManager.removeTask(task);
    }

    private void updateTaskLocal(ProjectTask task) {
        taskManager.updateTask(task);
    }

    public void addTask(ProjectTask task) {
        TaskPacket packet = new TaskPacket();
        packet.task = task;
        packet.command = "add task";
        try {
            NetClient.getInstance().sendPacket(packet);
        } catch (IOException e) {
            e.printStackTrace();
            log("unable to send add task packet");
        }
    }

    public void removeTask(ProjectTask task) {
        TaskPacket packet = new TaskPacket();
        packet.task = task;
        packet.command = "remove task";
        try {
            NetClient.getInstance().sendPacket(packet);
        } catch (IOException e) {
            e.printStackTrace();
            log("unable to send remove task packet");
        }
    }

    public void updateTask(ProjectTask task) {
        TaskPacket packet = new TaskPacket();
        packet.task = task;
        packet.command = "update task";
        try {
            NetClient.getInstance().sendPacket(packet);
        } catch (IOException e) {
            e.printStackTrace();
            log("unable to send update task packet");
        }
    }

    public TaskManager getTaskManager() {
        synchronized (monitor) {
            while (taskManager == null) {
                try {
                    monitor.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return taskManager;
        }
    }

    public User getUser(String username) {
        if (!userDictionary.containsKey(username)) {
            throw new IllegalArgumentException("User is not part of this project");
        }
        return userDictionary.get(username);
    }

    public Map<String, User> getUserDictionary() {
        return userDictionary;
    }

    private void log(String str) {
        System.out.println(this.getClass().getCanonicalName() + ": " + str);
    }
}
