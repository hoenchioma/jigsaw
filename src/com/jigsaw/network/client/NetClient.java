package com.jigsaw.network.client;

import com.jigsaw.accounts.sync.AccountPacket;
import com.jigsaw.accounts.sync.ClientAccountSyncHandler;
import com.jigsaw.accounts.Profile;
import com.jigsaw.calendar.sync.ClientTaskSyncHandler;
import com.jigsaw.calendar.sync.TaskPacket;
import com.jigsaw.chat.ClientMessageHandler;
import com.jigsaw.chat.packet.MessagePacket;
import com.jigsaw.chat.packet.FilePacket;
import com.jigsaw.chat.packet.FileRequestPacket;
import com.jigsaw.network.Packet;
import com.jigsaw.network.server.SystemPacket;

import java.io.*;
import java.net.Socket;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * A singleton instance of NetClient
 * It acts as the central portal for connecting to the Server
 *
 * @version %I%, %G%
 * @author Raheeb Hassan
 */
public class NetClient {
    // singleton instance of itself
    private static NetClient singleInstance;

    /**
     * Method to get the singleton instance of NetClient class
     * @return the singleton instance of the class
     */
    public static NetClient getInstance() {
        if (singleInstance == null) {
            singleInstance = new NetClient();
        }
        return singleInstance;
    }

    /**
     * Method to reset the singleton
     */
    public static void reset() {
        singleInstance = null;
    }

    // static (constant) variables
    public static final String DEFAULT_SERVER_ADDR = "localhost";
    public static final int DEFAULT_SERVER_PORT = 4444;

    // instance attributes
    private Socket socket;
    private ObjectInputStream in; 
    private ObjectOutputStream out;

    // related handlers
    private ClientTaskSyncHandler clientTaskSyncHandler;
    private ClientMessageHandler clientMessageHandler;
    private ClientAccountSyncHandler clientAccountSyncHandler;

    private String currentServerAddress = DEFAULT_SERVER_ADDR;
    private int currentServerPort = DEFAULT_SERVER_PORT;

    private volatile boolean isLoggedOut = true;

    public volatile String createdProjectID = null;

    /**
     * A map from a Packet class name (String) to a functor
     * to be called when such a Packet is received
     */
    private Map<String, Consumer<Packet>> callbackList = new HashMap<>();


    // private constructor so it can't be instantiated
    private NetClient() {}

    synchronized public void connect(String address, int port)
            throws IOException {
        if (socket != null && socket.isConnected()) socket.close();

        socket = new Socket(address, port);

        // initialize the I/O streams
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
    }
    synchronized public void connect() throws IOException {
        connect(currentServerAddress, currentServerPort);
    }

    /**
     * Communicates with server and returns server response
     * @return String with server response
     */
    synchronized public String login(String username, String password, String projectID)
            throws IOException, ClassNotFoundException {
        connect();
        out.writeObject("login"); // command
        out.writeObject(username);
        out.writeObject(password);
        out.writeObject(projectID);
        String response = (String) in.readObject();
        if (response.equals("success")) {
            // TODO: Implement sessionID security
            String sessionID = (String) in.readObject();
            isLoggedOut = false;
            log("successfully logged in");
            initialize();
        }
        return response;
    }

    public String register(String username, String password, Profile profile)
            throws IOException, ClassNotFoundException {
        connect();
        out.writeObject("register"); // command
        out.writeObject(username);
        out.writeObject(password);
        out.writeObject(profile);
        return (String) in.readObject();
    }

    public String createProject(String name, String description, LocalDate deadline)
            throws IOException, ClassNotFoundException {
        connect();
        out.writeObject("create project"); // command
        out.writeObject(name);
        out.writeObject(description);
        out.writeObject(deadline);
        String response = (String) in.readObject();
        if (response.equals("success")) {
            String projectID = (String) in.readObject();
            return projectID;
        }
        return null;
    }

    public void logOut() {
        try {
            sendPacket(new SystemPacket("log out"));
            isLoggedOut = true;
        } catch (IOException e) {
            e.printStackTrace();
            log("log out failed");
        }
//        reset();
    }

    synchronized public void sendPacket(Packet packet) throws IOException {
        out.writeObject(packet);
    }

    /**
     * Private inner class to listen for incoming packets
     */
    private class PacketListener extends Thread {

        @Override
        public void run() {
            while (!isLoggedOut) {
                try {
                    Object obj = in.readObject();
                    assert obj != null: "received packet is null";
                    Packet receivedPacket = (Packet) obj;
//                    log(receivedPacket.toString());
                    // forward packet to respective callback
                    callbackList.get(receivedPacket.getClass().getName()).accept(receivedPacket);
                } catch (IOException e) {
//                    e.printStackTrace();
                    log("Server disconnected");
                    logOut();
                    break;
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            try {
                in.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
                log("error closing socket");
            }
            log("successfully logged out");
        }

    }
    private void initialize() {
        new PacketListener().start();
        // register task sync handler
        clientTaskSyncHandler = new ClientTaskSyncHandler(null);
        registerCallback(TaskPacket.class.getName(), clientTaskSyncHandler::receivePacket);
        // register message handler
        clientMessageHandler = new ClientMessageHandler();
        registerCallback(MessagePacket.class.getName(), clientMessageHandler::receiveMessage);
        registerCallback(FilePacket.class.getName(), clientMessageHandler::receiveFile);
        registerCallback(FileRequestPacket.class.getName(), clientMessageHandler::receiveFileName);
        // register account handler
        clientAccountSyncHandler = new ClientAccountSyncHandler();
        registerCallback(AccountPacket.class.getName(), clientAccountSyncHandler::receivePacket);
    }
    public void registerCallback(String packetClassName, Consumer<Packet> callbackFunction) {
        callbackList.put(packetClassName, callbackFunction);
    }

    public ClientTaskSyncHandler getClientTaskSyncHandler() {
        if (clientTaskSyncHandler == null) {
            throw new IllegalStateException("Task Sync Handler not instantiated yet");
        }
        return clientTaskSyncHandler;
    }

    public ClientMessageHandler getClientMessageHandler() {
        if (clientMessageHandler == null) {
            throw new IllegalStateException("Message Handler not instantiated yet");
        }
        return clientMessageHandler;
    }

    public ClientAccountSyncHandler getClientAccountSyncHandler() {
        if (clientAccountSyncHandler == null) {
            throw new IllegalStateException("Account Handler not instantiated yet");
        }
        return clientAccountSyncHandler;
    }

    public String getCurrentServerAddress() {
        return currentServerAddress;
    }

    public void setCurrentServerAddress(String currentServerAddress) {
        this.currentServerAddress = currentServerAddress;
    }

    public int getCurrentServerPort() {
        return currentServerPort;
    }

    public void setCurrentServerPort(int currentServerPort) {
        this.currentServerPort = currentServerPort;
    }

    private void log(String str) {
        System.out.println(this.getClass().getCanonicalName() + ": " + str);
    }
}
