package com.jigsaw.network.client;

import com.jigsaw.accounts.Profile;
import com.jigsaw.calendar.TaskSyncHandler;
import com.jigsaw.network.Packet;

import java.io.*;
import java.net.Socket;

/**
 * A singleton instance of NetClient
 * It acts as the central portal for connecting to the Server
 *
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

    public static String DEFAULT_SERVER_ADDR = "localhost";
    public int DEFAULT_SERVER_PORT = 4444;

    // instance attributes

    private Socket socket;
    private ObjectInputStream in; 
    private ObjectOutputStream out;

    private TaskSyncHandler taskSyncHandler;

    // private constructor so it can't be instantiated

    private NetClient() {}
    synchronized public void connect(String address, int port) throws IOException{
        if (socket != null && socket.isConnected()) socket.close();

        socket = new Socket(address, port);

        // initialize the I/O streams
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
    }

    synchronized public void connect() throws IOException {
        connect(DEFAULT_SERVER_ADDR, DEFAULT_SERVER_PORT);
    }

    /**
     * Communicates with server and returns server response
     * @return String with server response
     */
    synchronized public String login(String username, String password, String projectID) throws Exception {
        connect();
        out.writeObject("login");
        out.writeObject(username);
        out.writeObject(password);
        out.writeObject(projectID);
        String response = (String) in.readObject();
        if (response.equals("success")) {
            String sessionID = (String) in.readObject();
            new PacketListener().start();
        }
        return response;
    }

    public String register(String username, String password, Profile profile) throws Exception {
        connect();
        out.writeObject("register");
        out.writeObject(username);
        out.writeObject(password);
        out.writeObject(profile);
        String response = (String) in.readObject();
        if (response.equals("success")) {
            String sessionID = (String) in.readObject();
            new PacketListener().start();
        }
        return response;
    }

    synchronized public void sendPacket(Packet packet) throws Exception {
        out.writeObject(packet);
//        String response = (String) in.readObject();
//        return response.equals("success");
    }

    /**
     * Private inner class to listen for incoming packets
     */
    private class PacketListener extends Thread {

        @Override
        public void run() {
            while (true) {
                try {
                    Packet receivedPacket = (Packet) in.readObject();
                    if (receivedPacket.getClass().getName().equals("SendTaskPacket")) {
                        taskSyncHandler.receivePacket(receivedPacket);
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
    }
    private void initial() {
        new PacketListener().start();
        taskSyncHandler = new TaskSyncHandler();
        new Thread(taskSyncHandler).start();
    }

    private void log(String str) {
        System.out.println(this.getClass().getCanonicalName() + ": " + str);
    }

    public TaskSyncHandler getTaskSyncHandler() {
        return taskSyncHandler;
    }

    public void setTaskSyncHandler(TaskSyncHandler taskSyncHandler) {
        this.taskSyncHandler = taskSyncHandler;
    }
}
