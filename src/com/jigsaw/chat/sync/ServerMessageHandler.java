package com.jigsaw.chat.sync;

import com.jigsaw.accounts.Project;
import com.jigsaw.accounts.Resource;
import com.jigsaw.accounts.User;
import com.jigsaw.chat.packet.ChatPacketHandler;
import com.jigsaw.chat.packet.FilePacket;
import com.jigsaw.chat.packet.FileRequestPacket;
import com.jigsaw.chat.packet.MessagePacket;
import com.jigsaw.network.Packet;
import com.jigsaw.network.server.ClientHandler;
import com.jigsaw.network.server.Server;


import java.io.*;
import java.util.ArrayList;

/**
 * Class to write objects into a file
 *
 * @author A.M. Aahad
 */

public class ServerMessageHandler {
    private User user;
    private Project project;
    private String messagePath;
    private String fileDir;
    private String fileListPath;
    private ArrayList<FileRequestPacket> fileList;
    private ClientHandler clientHandler;

    public ServerMessageHandler(User user, Project project, ClientHandler clientHandler) {
        this.user = user;
        this.project = project;
        this.clientHandler = clientHandler;
        this.messagePath = Resource.messagesDirPath + project.getId() + "-chathistory";
        this.fileDir = Resource.filesDirPath + project.getId() + "-files/";
        this.fileListPath = Resource.filesDirPath + project.getId() + "-filelist";
        this.fileList = new ArrayList<>();

        sendChatHistory();
        sendFileList();
    }

    private void sendChatHistory() {
        try {
            for (Packet messages: loadFromFile(messagePath)) {
                clientHandler.sendPacket(messages);
            }
        } catch (IOException e) {
            e.printStackTrace();
            log("error sending chat history");
        }
    }

    private void sendFileList() {
        try {
            if ((new File(fileListPath).isFile())) {
                fileList = (ArrayList) Resource.loadObjFromFile(fileListPath);
            }
            for (FileRequestPacket fileName: fileList) {
                clientHandler.sendPacket(fileName);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            log("error sending file list");
        }
    }

    synchronized public void receivePacket(Packet packet) {
        if (packet instanceof MessagePacket) {
            try {
                MessagePacket messagePacket = (MessagePacket) packet;
                // set client username in packet (as it was not done on client side)
                messagePacket.setClientUserName(user.getUsername());
//                log("message received on server, content: " + messagePacket.toString());
                writeToFile(messagePacket, messagePath);
                for (String member : project.getMembers()) {
//                    log(member);
                    Server server = clientHandler.getServer();
                    if (server.getActiveConnections().containsKey(member)) {
//                        log(clientHandler.toString());
                        server.getActiveConnections().get(member).sendPacket(messagePacket);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (packet instanceof FilePacket) {
            try{
                FilePacket filePacket = (FilePacket) packet;
                filePacket.setClientUserName(user.getUsername());
                Resource.createDir(fileDir + File.separator + filePacket.getFileName());
                File storeFile = new File(fileDir + File.separator + filePacket.getFileName());
                filePacket.toFile(storeFile);
                FileRequestPacket requestPacket = new FileRequestPacket(filePacket.getClientUserName(),
                        filePacket.getFileName());
//                ServerHistoryHandler.writeToFile(requestPacket);
                fileList.add(requestPacket);
                Resource.saveObjToFile(fileList, fileListPath);
                for (String member : project.getMembers()) {
                    log(member);
                    Server server = clientHandler.getServer();
                    if (server.getActiveConnections().containsKey(member)) {
                        //log(clientHandler.toString());
                        server.getActiveConnections().get(member).sendPacket(requestPacket);
                    }
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        else if (packet instanceof FileRequestPacket){
            FileRequestPacket fileRequestPacket = (FileRequestPacket) packet;
            File sendFile = new File(fileDir + File.separator + fileRequestPacket.getFileName());
            try {
                Packet filePacket = ChatPacketHandler.createFilePacket(user.getUsername(), sendFile);
                clientHandler.sendPacket(filePacket);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //method to write chat history to file
    synchronized public void writeToFile(Packet packetToWrite, String path) throws IOException {
        // create the enclosing folders if they don't exist
        Resource.createDir(path);
        createFileIfNotPresent(path);

        FileOutputStream fout = new FileOutputStream(path, true);
        AppendingObjectOutputStream aoosF = new AppendingObjectOutputStream(fout);
        aoosF.writeObject(packetToWrite);
        aoosF.close();
        fout.close();
    }

    //method to read chat history from file
    public ArrayList<Packet> loadFromFile(String path) throws IOException {
        // create the enclosing folders if they don't exist
        Resource.createDir(path);
        createFileIfNotPresent(path);

        FileInputStream fis = new FileInputStream(path);
        ObjectInputStream ois = new ObjectInputStream(fis);
        ArrayList<Packet> list = new ArrayList<>();
        try {
            // initially read header and discard it
            ois.readObject();
            Object obj;
            while (true) {
                try {
                    obj = ois.readObject();
                } catch (EOFException e) {
                    break;
                }
                Packet pac = (Packet) obj;
                list.add(pac);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            log("error reading object from file");
        }
//        log("finished reading chat history file");
        return list;
    }

    private void createFileIfNotPresent(String path) throws IOException {
        if (!(new File(path).isFile())) {
            FileOutputStream fout = new FileOutputStream(path, true);
            ObjectOutputStream oosF = new ObjectOutputStream(fout);
            oosF.writeObject(new MessagePacket("Server", "Chat History"));
            oosF.close();
            fout.close();
        }
    }

    private void log(String str) {
        System.out.println(this.getClass().getCanonicalName() + ": " + str);
    }
}
