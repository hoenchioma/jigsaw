package com.jigsaw.accounts.sync;

import com.jigsaw.accounts.Project;
import com.jigsaw.accounts.User;
import com.jigsaw.network.Packet;
import com.jigsaw.network.client.NetClient;

import java.io.IOException;

/**
 * Handler for sending and processing account packets
 * on client side
 *
 * @version %I%, %G%
 * @author Raheeb Hassan
 */
public class ClientAccountSyncHandler {
    // user and project info
    private User user;
    private Project project;

    private final Object monitor = new Object();

    public ClientAccountSyncHandler() {
        try {
            NetClient.getInstance().sendPacket(new AccountPacket("init", null, null));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Project getProject() {
        synchronized (monitor) {
            try {
                while (project == null) monitor.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
        try {
            // send reset package to server for reseting project
            NetClient.getInstance().sendPacket(
                    new AccountPacket("reset", user, project)
            );
        } catch (IOException e) {
            e.printStackTrace();
            log("failed to sync project edit to server");
        }
    }

    public User getUser() {
        synchronized (monitor) {
            try {
                while (user == null) monitor.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        try {
            // send reset package to server for reseting project
            NetClient.getInstance().sendPacket(
                    new AccountPacket("reset", user, project)
            );
        } catch (IOException e) {
            e.printStackTrace();
            log("failed to sync user edit to server");
        }
    }

    public void receivePacket(Packet packet) {
        AccountPacket accountPacket = (AccountPacket) packet;
        if (accountPacket.command.equals("reset")) {
            project = accountPacket.project;
            user = accountPacket.user;
            synchronized (monitor) { monitor.notify(); }
        }
    }

    private void log(String str) {
        System.out.println(this.getClass().getCanonicalName() + ": " + str);
    }
}
