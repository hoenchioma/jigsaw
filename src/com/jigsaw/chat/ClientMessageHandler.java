package com.jigsaw.chat;

import com.jigsaw.network.Packet;
import com.jigsaw.network.client.NetClient;
import com.jigsaw.gui.ChatViewController;
import javafx.application.Platform;

import com.jigsaw.chat.packet.*;
import java.io.*;
import java.util.ArrayList;

/**
 *
 * Class for handling server-client messages
 *
 * @author A.M. Aahad
 *
 */

public class ClientMessageHandler {
    private ChatViewController controller = null;
//    private static String userName;
    private ArrayList<MessagePacket> messageList;

    //constructor
    public ClientMessageHandler() {
        messageList = new ArrayList<>();
    }

    //method to send object to server
    public void sendMessage(String userMessage) {
        try {
            // username left null because it will be filled in by server
            Packet sendingPacket = ChatPacketHandler.createMessagePacket(null, userMessage);
            NetClient.getInstance().sendPacket(sendingPacket);
//            log("message sent, content: " + userMessage);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    //method to send file request
    public void sendFileRequest(String fileName){
        try{
            Packet sendingPacket = ChatPacketHandler.createFileReqeustPacket(null, fileName);
            NetClient.getInstance().sendPacket(sendingPacket);
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    //method to send files
    public void sendFile(File inputFile) throws  Exception {
        try{
            Packet sendingPacket = ChatPacketHandler.createFilePacket(null, inputFile);
            NetClient.getInstance().sendPacket(sendingPacket);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    //method to extract string from packet and call append
    synchronized public void receiveMessage(Packet receivedPacket) {
//        log(receivedPacket.toString());
        String receiveStr = ChatPacketHandler.extractPacket(receivedPacket);
        messageList.add((MessagePacket) receivedPacket);
        if (controller != null) {
            Platform.runLater(() -> this.controller.textAppend(receiveStr));
        }
    }

    //method to call append file name in the file area
    synchronized public void receiveFileName(Packet receivedPacket){
        String tempFileName = ChatPacketHandler.extractPacket(receivedPacket);
        Platform.runLater(() -> this.controller.fileAppend(tempFileName));
    }

    //method to receive file and save to directory
    synchronized public void receiveFile(Packet receivedPacket){
        Platform.runLater(
                () -> {
                    try {
                        this.controller.saveFile(receivedPacket);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        );
    }

    public static void saveFile(File file, Packet receivedPacket) throws Exception{
        ChatPacketHandler.extractFilePacket(file, receivedPacket);
    }

    public ArrayList<MessagePacket> getMessageList() {
        return messageList;
    }

    public void setController(ChatViewController controller) {
        this.controller = controller;
    }

    private void log(String str) {
        System.out.println(this.getClass().getCanonicalName() + ": " + str);
    }
}
