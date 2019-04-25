package com.jigsaw.calendar;

import com.jigsaw.network.Packet;
import com.jigsaw.network.server.ClientHandler;

import java.time.LocalDateTime;

public class ServerTaskSyncHandler implements Runnable {
    private TaskManager taskManager;
    private LocalDateTime currentSyncedTime;
    private ClientHandler clientHandler;

    public ServerTaskSyncHandler(ClientHandler clientHandler, TaskManager taskManager) {
        this.clientHandler = clientHandler;
        this.taskManager = taskManager;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(500);
                sendPacket();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void sendPacket() throws Exception {
        SendTaskPacket sendTaskPacket = new SendTaskPacket();
        sendTaskPacket.timeCreated = LocalDateTime.now();
        sendTaskPacket.taskManager = taskManager;
        clientHandler.sendPacket(sendTaskPacket);
    }

    public void receivePacket(Packet packet) {
        SendTaskPacket sendTaskPacket = (SendTaskPacket) packet;
        if (sendTaskPacket.timeCreated.isAfter(currentSyncedTime)) {
            taskManager = sendTaskPacket.taskManager;
            currentSyncedTime = sendTaskPacket.timeCreated;
        }
    }
}
