package com.jigsaw.network.server;

import com.jigsaw.accounts.User;

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

    // TODO: Implement project related functionality

    public ClientHandler(ObjectOutputStream out, ObjectInputStream in, User user, String sessionID) {
        this.out = out;
        this.in = in;
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
