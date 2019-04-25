package sample;

import javafx.application.Platform;

import sample.Packets.*;
import java.io.*;

/**
 *
 * Class for handling server-client messages
 *
 * @author A.M. Aahad
 *
 */

public class ClientMessageHandler {
    private String receiveStr;
    public ChatViewController myController;
    private static PacketHandler packetHandler;
    private static String userName;

    //constructor
    public ClientMessageHandler (ChatViewController controller) throws Exception{
        this.myController = controller;
        userName = "Samin"; //todo have to get from login module
        packetHandler = new PacketHandler();
    }

    //method to send object to server
    public static void sendMessage(String userMessage){
        try{
            Packet sendingPacket = packetHandler.createMessagePacket(userName, userMessage);
            NetClient.getInstance().sendPacket(sendingPacket);
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    //method to send files
    public void sendFile(File inputFile){
        //Todo implement file sending
    }

    //method to extract string from packet and call append
    synchronized void receiveMessage(Packet receivedPacket){
        this.receiveStr = packetHandler.extractPacket(receivedPacket);
        String temp = this.receiveStr;
        Platform.runLater(
                () -> {
                    this.myController.textAppend(temp);
                }
        );
    }

}
