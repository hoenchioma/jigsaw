package com.jigsaw.accounts.sync;

import com.jigsaw.accounts.Project;
import com.jigsaw.accounts.User;
import com.jigsaw.network.Packet;

/**
 * A container object for sending user and project data
 * over network
 *
 * @version %I%, %G%
 * @author Raheeb Hassan
 */
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
