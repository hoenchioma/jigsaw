package com.jigsaw.chat.packet;

import com.jigsaw.chat.packet.FilePacket;
import com.jigsaw.chat.packet.MessagePacket;
import com.jigsaw.network.Packet;

import java.io.File;

public class ChatPacketHandler {
    public static Packet createMessagePacket(String clientUserName, String message){
        return (new MessagePacket(clientUserName, message));
    }

    public static Packet createFilePacket(String clientUserName, File file){
        return (new FilePacket(clientUserName, file));
    }

    public static String extractPacket(Packet packet) {
        if (packet instanceof MessagePacket) {
            MessagePacket messagePacket = (MessagePacket) packet;
            return messagePacket.toString();
        }
        else if (packet instanceof FilePacket) {
            FilePacket filePacket = (FilePacket) packet;
            return "Test";
        }
        else{
            throw new IllegalArgumentException("Illegal packet");
        }
    }
}
