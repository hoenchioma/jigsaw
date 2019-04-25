package com.jigsaw.calendar;

import com.jigsaw.network.Packet;
import com.jigsaw.network.client.NetClient;

import java.time.LocalDateTime;

public class TaskSyncHandler implements Runnable {
    private LocalDateTime currentSyncedTime;
    private TaskManager taskManager;

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

    public void receivePacket(Packet packet) {
        SendTaskPacket sendTaskPacket = (SendTaskPacket) packet;
        if (sendTaskPacket.timeCreated.isAfter(currentSyncedTime)) {
            taskManager = sendTaskPacket.taskManager;
            currentSyncedTime = sendTaskPacket.timeCreated;
        }
    }

    public void sendPacket() throws Exception {
        SendTaskPacket sendTaskPacket = new SendTaskPacket();
        sendTaskPacket.timeCreated = LocalDateTime.now();
        sendTaskPacket.taskManager = taskManager;
        NetClient.getInstance().sendPacket(sendTaskPacket);
    }

    public TaskManager getTaskManager() {
        return taskManager;
    }

    public void setTaskManager(TaskManager taskManager) {
        this.taskManager = taskManager;
    }
}
