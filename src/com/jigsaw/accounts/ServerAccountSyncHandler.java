package com.jigsaw.accounts;

import com.jigsaw.network.Packet;
import com.jigsaw.network.client.NetClient;
import com.jigsaw.network.server.ClientHandler;

import java.io.IOException;

public class ServerAccountSyncHandler {
    // user and project info
    private ClientHandler clientHandler;
    private User user;
    private Project project;

    public ServerAccountSyncHandler(User user, Project project, ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
        this.user = user;
        this.project = project;
    }

    public void receivePacket(Packet packet) {
        AccountPacket accountPacket = (AccountPacket) packet;
        if (accountPacket.command.equals("init")) {
            try {
                clientHandler.sendPacket(
                        new AccountPacket("reset", user, project));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (accountPacket.command.equals("reset")) {
            project = accountPacket.project;
            user = accountPacket.user;
            project.saveToFile();
        }
    }

    private void log(String str) {
        System.out.println(this.getClass().getCanonicalName() + ": " + str);
    }
}
