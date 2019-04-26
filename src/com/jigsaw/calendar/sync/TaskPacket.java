package com.jigsaw.calendar.sync;

import com.jigsaw.accounts.User;
import com.jigsaw.calendar.ProjectTask;
import com.jigsaw.calendar.TaskManager;
import com.jigsaw.network.Packet;

import java.util.Map;

public class TaskPacket implements Packet {
    public String command;
    public TaskManager taskManager;
    public ProjectTask task;
    public Map<String, User> projectMemberInfo;
}
