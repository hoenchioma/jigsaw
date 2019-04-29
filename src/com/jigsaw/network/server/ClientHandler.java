package com.jigsaw.network.server;

import com.jigsaw.accounts.*;
import com.jigsaw.accounts.sync.AccountPacket;
import com.jigsaw.accounts.sync.ServerAccountSyncHandler;
import com.jigsaw.calendar.sync.ServerTaskSyncHandler;
import com.jigsaw.calendar.sync.TaskPacket;
import com.jigsaw.chat.ServerMessageHandler;
import com.jigsaw.chat.packet.FilePacket;
import com.jigsaw.chat.packet.FileRequestPacket;
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
 * (one per client)
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

    private volatile boolean isLoggedOut;

    public ClientHandler(Server server, ObjectOutputStream out, ObjectInputStream in, User user, Project project, String sessionID) {
        this.server = server;
        this.out = out;
        this.in = in;
        this.user = user;
        this.sessionID = sessionID;
        this.project = project;
        this.isLoggedOut = false;

        initializeHandlers();
    }

    private void initializeHandlers() {
        // register system packet callback
        registerCallback(SystemPacket.class.getName(), this::handleSystemPacket);

        // register task sync handler
        ServerTaskSyncHandler serverTaskSyncHandler = new ServerTaskSyncHandler(this, project);
        registerCallback(TaskPacket.class.getName(), serverTaskSyncHandler::receivePacket);
//        new Thread(serverTaskSyncHandler).start();

        // register message handler
        ServerMessageHandler serverMessageHandler = new ServerMessageHandler(user, project, this);
        registerCallback(MessagePacket.class.getName(), serverMessageHandler::receivePacket);
        registerCallback(FilePacket.class.getName(), serverMessageHandler::receivePacket);
        registerCallback(FileRequestPacket.class.getName(), serverMessageHandler::receivePacket);

        // register account handler
        ServerAccountSyncHandler serverAccountSyncHandler = new ServerAccountSyncHandler(user, project, this);
        registerCallback(AccountPacket.class.getName(), serverAccountSyncHandler::receivePacket);
    }

    @Override
    public void run() {
        while (!isLoggedOut) {
            try {
                Object obj = in.readObject();
                assert obj != null: "received packet is null";
                Packet receivedPacket = (Packet) obj;
                // when a packet is received pass in on to a registered handler
                callbackList.get(receivedPacket.getClass().getName()).accept(receivedPacket);
            } catch (EOFException e) {
//                e.printStackTrace();
                log("Client disconnected");
                // close connection and exit thread
                server.getActiveConnections().remove(user.getUsername());
                break;
            } catch (NullPointerException | ClassNotFoundException | IOException e) {
//                log("Invalid or corrupted packet received");
                e.printStackTrace();
            }
        }
        log(user.getUsername() + " has logged out");
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

    public void handleSystemPacket(Packet packet) {
        SystemPacket systemPacket = (SystemPacket) packet;
        if (systemPacket.command.equals("log out")) {
            Resource.getInstance().deactivateUser(user);
            server.removeHandler(user.getUsername());
            isLoggedOut = true;
        }
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
        System.out.println(this.getClass().getCanonicalName() + ": " + str);
    }

    public Server getServer() {
        return server;
    }
}
