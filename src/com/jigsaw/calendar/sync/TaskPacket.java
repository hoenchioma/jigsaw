package com.jigsaw.calendar.sync;

import com.jigsaw.accounts.User;
import com.jigsaw.calendar.ProjectTask;
import com.jigsaw.calendar.TaskManager;
import com.jigsaw.network.Packet;

import java.util.Map;

/**
 * A container object for sending entire task manager,
 * task edit, add and remove commands over the network
 *
 * @version %I%, %G%
 * @author Raheeb Hassan
 */
public class TaskPacket implements Packet {
    public String command;
    public TaskManager taskManager;
    public ProjectTask task;
    public Map<String, User> projectMemberInfo;

    public TaskPacket() {}

    public TaskPacket(String command, TaskManager taskManager, ProjectTask task, Map<String, User> projectMemberInfo) {
        this.command = command;
        this.taskManager = taskManager;
        this.task = task;
        this.projectMemberInfo = projectMemberInfo;
    }
}
