package com.jigsaw.network;

import com.jigsaw.accounts.Resource;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

/**
 * Server class for accepting connection requests from clients
 *
 * Passes control to a ServerLoginHandler on receiving request
 *
 * @author Raheeb Hassan
 */
public class Server {
    private ServerSocket serverSocket;
    private Set<ClientHandler> activeConnections;
    private Resource resource;

    public Server(int port) {
        try {
            serverSocket = new ServerSocket(port);
            activeConnections = new HashSet<>();
            resource = Resource.loadFromFile();

            while (true) {
                Socket socket = serverSocket.accept();

                // pass on to login handler to complete login process
                new Thread(new ServerLoginHandler(socket, this)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    synchronized public void addHandler(ClientHandler handler) {
        activeConnections.add(handler);
    }

    public Resource getResource() {
        return resource;
    }
}
