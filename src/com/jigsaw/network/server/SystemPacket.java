package com.jigsaw.network.server;

import com.jigsaw.network.Packet;

public class SystemPacket implements Packet {
    public String command;

    public SystemPacket(String command) {
        this.command = command;
    }
}
