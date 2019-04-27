package com.jigsaw.network.server;

import com.jigsaw.accounts.Resource;
//import testing.QuickLogin;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Server class for accepting connection requests from clients
 *
 * Passes control to a ServerLoginHandler on receiving request
 *
 * @author Raheeb Hassan
 */
public class Server {
    public static int DEFAULT_SERVER_PORT = 4444;

    private ServerSocket serverSocket;
    private Map<String, ClientHandler> activeConnections;
    private Resource resource;

    public Server(int port) {
        try {
            log("Server started");

            serverSocket = new ServerSocket(port);
            activeConnections = new HashMap<>();
            resource = Resource.getInstance();

            while (true) {
                Socket socket = serverSocket.accept();

                log("socket connect with address " + socket.getInetAddress().getHostAddress());

                // pass on to login handler to complete login process
                new Thread(new ServerLoginHandler(socket, this)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    synchronized public void addHandler(String username, ClientHandler handler) {
        activeConnections.put(username, handler);
    }

    public Resource getResource() {
        return resource;
    }

    public Map<String, ClientHandler> getActiveConnections() {
        return activeConnections;
    }

    public static void main(String[] args) {
//        QuickLogin.createProject();
        int port = DEFAULT_SERVER_PORT;
        if (args.length >= 1) port = Integer.parseInt(args[0]);
        new Server(port);
    }

    public void log(String str) {
        System.out.println(str);
    }
}
