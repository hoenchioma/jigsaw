package com.jigsaw.network.server;

import com.jigsaw.accounts.User;

import java.net.Socket;
import java.util.Objects;

/**
 * The main handler which communicates with clients
 *
 * @author Raheeb Hassan
 */
public class ClientHandler implements Runnable {
    private Socket socket;
    private User user;
    private String sessionID;

    // TODO: Implement project related functionality

    public ClientHandler(Socket socket, User user, String sessionID) {
        this.socket = socket;
        this.user = user;
        this.sessionID = sessionID;
    }

    @Override
    public void run() {
        // TODO: Implement Client Handler
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
}
