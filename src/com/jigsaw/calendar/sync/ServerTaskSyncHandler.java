package com.jigsaw.calendar.sync;

import com.jigsaw.accounts.Project;
import com.jigsaw.accounts.Resource;
import com.jigsaw.network.Packet;
import com.jigsaw.network.server.ClientHandler;
import com.jigsaw.network.server.Server;

import java.io.IOException;

/**
 * Handler for sending and processing task packets
 * on server side (for each user)
 *
 * @version %I%, %G%
 * @author Raheeb Hassan
 */
public class ServerTaskSyncHandler {
    private ClientHandler clientHandler;
    private Project project;

    public ServerTaskSyncHandler(ClientHandler clientHandler, Project project) {
        this.clientHandler = clientHandler;
        this.project = project;

        // invoke a ghost init command to send stuff to client
        receivePacket(new TaskPacket("init", null, null, null));
    }

    /**
     * Processes incoming packet
     */
    public void receivePacket(Packet packet) {
        try {
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
            else {
                switch (receivedTaskPacket.command) {
                    case "add task":
                        project.getTaskManager().addTask(receivedTaskPacket.task);
                        break;
                    case "remove task":
                        project.getTaskManager().removeTask(receivedTaskPacket.task);
                        break;
                    case "update task":
                        project.getTaskManager().updateTask(receivedTaskPacket.task);
                        break;
                    default:
                        throw new IllegalArgumentException("Illegal Task packet");
                }
                sendBack(receivedTaskPacket);
                project.saveToFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
            log("error processing task packet");
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

    private void log(String str) {
        System.out.println(str);
    }
}
