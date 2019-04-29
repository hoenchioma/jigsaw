package com.jigsaw.calendar.sync;

import com.jigsaw.accounts.User;
import com.jigsaw.calendar.ProjectTask;
import com.jigsaw.calendar.TaskManager;
import com.jigsaw.network.Packet;
import com.jigsaw.network.client.NetClient;

import java.io.IOException;
import java.util.Map;

/**
 * Handler for syncing for sending, receiving
 * and processing
 *
 * @version %I%, %G%
 * @author Raheeb Hassan
 */
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
//        System.out.println(taskManager.getProjectTasks());
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

    /**
     * Send packet to server for requesting adding task
     * @param task projectTask to be added
     */
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

    /**
     * Send packet to server for requesting removing task
     * @param task projectTask to be added
     */
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

    /**
     * Send packet to server for requesting updating task
     * @param task projectTask to be added
     */
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

    /**
     * getter method for getting the related task manager instance
     * makes the thread wait if the task manager is not yet available
     * (not arrived from server yet)
     *
     * @return the task manager
     */
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

    /**
     * Getter for user from username
     * throws illegal argument exception when user is not in dictionary
     *
     * @param username to get user from
     * @return user with username equal to @param
     */
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
