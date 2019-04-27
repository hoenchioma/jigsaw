package com.jigsaw.accounts;

import com.jigsaw.network.Packet;

public class AccountPacket implements Packet {
    public String command;
    public User user;
    public Project project;

    public AccountPacket(String command, User user, Project project) {
        this.command = command;
        this.user = user;
        this.project = project;
    }
}
