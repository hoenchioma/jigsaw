package com.jigsaw.network.server;

import com.jigsaw.accounts.User;
import com.jigsaw.calendar.ServerTaskSyncHandler;
import com.jigsaw.calendar.TaskManager;
import com.jigsaw.network.Packet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Objects;

/**
 * The main handler which communicates with clients
 *
 * @author Raheeb Hassan
 */
public class ClientHandler implements Runnable {
    private ObjectOutputStream out;
    private ObjectInputStream in;

    private User user;
    private String sessionID;

    private TaskManager taskManager;

    private ServerTaskSyncHandler serverTaskSyncHandler;

    // TODO: Implement project related functionality

    public ClientHandler(ObjectOutputStream out, ObjectInputStream in, User user, String sessionID, TaskManager taskManager) {
        this.out = out;
        this.in = in;
        this.user = user;
        this.sessionID = sessionID;
        this.taskManager = taskManager;
    }

    @Override
    public void run() {
        serverTaskSyncHandler = new ServerTaskSyncHandler(this, taskManager);
        new Thread(serverTaskSyncHandler).start();
        while (true) {
            try {
                Packet receivedPacket = (Packet) in.readObject();
                if (receivedPacket.getClass().getName().equals("SendTaskPacket")) {
                    serverTaskSyncHandler.receivePacket(receivedPacket);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
                log("Server disconnected");
                break;
            }
        }
    }

    public synchronized void sendPacket(Packet packet) throws Exception {
        out.writeObject(packet);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientHandler that = (ClientHandler) o;
        return sessionID.equals(that.sessionID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sessionID);
    }

    private void log(String str) {
        System.out.println(this.getClass() + ": " + str);
    }
}
