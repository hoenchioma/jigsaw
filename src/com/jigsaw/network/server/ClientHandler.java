package com.jigsaw.network.server;

import com.jigsaw.accounts.Project;
import com.jigsaw.accounts.User;
import com.jigsaw.calendar.TaskManager;
import com.jigsaw.calendar.sync.ServerTaskSyncHandler;
import com.jigsaw.calendar.sync.TaskPacket;
import com.jigsaw.chat.ServerMessageHandler;
import com.jigsaw.chat.packet.FilePacket;
import com.jigsaw.chat.packet.MessagePacket;
import com.jigsaw.network.Packet;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * The main handler which communicates with clients
 *
 * @author Raheeb Hassan
 */
public class ClientHandler implements Runnable {
    private Server server;

    private ObjectOutputStream out;
    private ObjectInputStream in;

    private User user;
    private Project project;

    private String sessionID;

    private Map<String, Consumer<Packet>> callbackList = new HashMap<>();

    public ClientHandler(Server server, ObjectOutputStream out, ObjectInputStream in, User user, Project project, String sessionID) {
        this.server = server;
        this.out = out;
        this.in = in;
        this.user = user;
        this.sessionID = sessionID;
        this.project = project;

        initializeHandlers();
    }

    private void initializeHandlers() {
        // register the system handler
        registerCallback(SystemPacket.class.getName(), ServerSystemHandler::receivePacket);

        // register task sync handler
        ServerTaskSyncHandler serverTaskSyncHandler = new ServerTaskSyncHandler(this, project);
        registerCallback(TaskPacket.class.getName(), serverTaskSyncHandler::receivePacket);
//        new Thread(serverTaskSyncHandler).start();

        // register message handler
        ServerMessageHandler serverMessageHandler = new ServerMessageHandler(user, project, this);
        registerCallback(MessagePacket.class.getName(), serverMessageHandler::receivePacket);
        registerCallback(FilePacket.class.getName(), serverMessageHandler::receivePacket);
    }

    @Override
    public void run() {
        while (true) {
            try {
                Packet receivedPacket = (Packet) in.readObject();
                // when a packet is received pass in on to a registered handler
                callbackList.get(receivedPacket.getClass().getName()).accept(receivedPacket);
            } catch (NullPointerException e) {
                log("Invalid or corrupted packet received");
            } catch (EOFException e) {
//                e.printStackTrace();
                log("Client disconnected");
                // close connection and exit thread
                server.getActiveConnections().remove(user.getUsername());
                break;
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void sendPacket(Packet packet) throws IOException {
        out.writeObject(packet);
    }

    /**
     * register a handler for a specific packet class name
     * @param packetClassName the class name of the packet
     * @param callbackFunction the function to be called
     */
    public void registerCallback(String packetClassName, Consumer<Packet> callbackFunction) {
        callbackList.put(packetClassName, callbackFunction);
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

    public Server getServer() {
        return server;
    }
}
