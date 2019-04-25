package com.jigsaw.network.client;

import com.jigsaw.accounts.Profile;
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

    // private constructor so it can't be instantiated
    private NetClient() {}

    synchronized public void connect(String address, int port) throws IOException{
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
    synchronized public String login(String username, String password) throws IOException {
        connect();
        out.writeUTF("login");
        out.writeUTF(username);
        out.writeUTF(password);
        String response =  in.readUTF();
        if (response.equals("success")) {
            String sessionID = in.readUTF();
            new PacketListener().start();
        }
        return response;
    }

    public String register(String username, String password, Profile profile) throws IOException {
        connect();
        out.writeUTF("register");
        out.writeUTF(username);
        out.writeUTF(password);
        out.writeObject(profile);
        String response = in.readUTF();
        if (response.equals("success")) {
            String sessionID = in.readUTF();
            new PacketListener().start();
        }
        return response;
    }

    synchronized public boolean sendPacket(Packet packet) throws IOException {
        out.writeObject(packet);
        String response = in.readUTF();
        return response.equals("success");
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

    private void log(String str) {
        System.out.println(this.getClass().getCanonicalName() + ": " + str);
    }
}
