package com.jigsaw.calendar;

import com.jigsaw.network.Packet;

import java.time.LocalDateTime;

public class SendTaskPacket implements Packet {
    public LocalDateTime timeCreated;
    public TaskManager taskManager;
}
