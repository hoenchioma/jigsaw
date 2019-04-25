package com.jigsaw.chat.packet;

import com.jigsaw.chat.packet.FilePacket;
import com.jigsaw.chat.packet.MessagePacket;
import com.jigsaw.network.Packet;

import java.io.File;

public class ChatPacketHandler {
    public Packet createMessagePacket(String clientUserName, String message){
        return (new MessagePacket(clientUserName, message));
    }

    public Packet createFilePacket(String clientUserName, File file){

        return (new FilePacket(clientUserName, file));
    }

    public String extractPacket(Packet packet){
        if(packet.getClass() == (new MessagePacket()).getClass()){
            MessagePacket messagePacket = (MessagePacket) packet;
            return messagePacket.toString();
        }

        else if(packet.getClass() == (new FilePacket()).getClass()){
            FilePacket filePacket = (FilePacket) packet;
            return "Test";
        }

        else{
            return "Error recieving Packet";
        }
    }
}
