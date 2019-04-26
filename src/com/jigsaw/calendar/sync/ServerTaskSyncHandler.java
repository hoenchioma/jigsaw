package com.jigsaw.calendar.sync;

import com.jigsaw.accounts.Project;
import com.jigsaw.accounts.Resource;
import com.jigsaw.network.Packet;
import com.jigsaw.network.server.ClientHandler;
import com.jigsaw.network.server.Server;

import java.io.IOException;

public class ServerTaskSyncHandler implements Runnable {
    private ClientHandler clientHandler;
    private Project project;

    public ServerTaskSyncHandler(ClientHandler clientHandler, Project project) {
        this.clientHandler = clientHandler;
        this.project = project;
    }

    @Override
    public void run() {
        while (true) {
            try {
                // saves the profile object to file every 1s
                Thread.sleep(1000);
                project.saveToFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Processes incoming packet
     */
    public void receivePacket(Packet packet) throws IOException {
        TaskPacket receivedTaskPacket = (TaskPacket) packet;
        // initial request for task manager
        if (receivedTaskPacket.command.equals("init")) {
            TaskPacket sendingPacket = new TaskPacket();
            sendingPacket.command = "reset";
            sendingPacket.projectMemberInfo =
                    Resource.getInstance().getProjectUsers(project.getId());
            sendingPacket.taskManager = project.getTaskManager();
            try {
                clientHandler.sendPacket(sendingPacket);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // request to add task
        else if (receivedTaskPacket.command.equals("add task")) {
            project.getTaskManager().addTask(receivedTaskPacket.task);
            sendBack(receivedTaskPacket);
        }
        else if (receivedTaskPacket.command.equals("remove task")) {
            project.getTaskManager().removeTask(receivedTaskPacket.task);
            sendBack(receivedTaskPacket);
        }
        else if (receivedTaskPacket.command.equals("update task")) {
            project.getTaskManager().updateTask(receivedTaskPacket.task);
            sendBack(receivedTaskPacket);
        }
    }

    /**
     * Utility method for sending all users in the project
     * the same packet
     */
    private void sendBack(TaskPacket taskPacket) throws IOException {
        for (String member: project.getMembers()) {
            Server server = clientHandler.getServer();
            if (server.getActiveConnections().containsKey(member)) {
                server.getActiveConnections().get(member).sendPacket(taskPacket);
            }
        }
    }


}
