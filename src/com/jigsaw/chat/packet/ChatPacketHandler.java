package com.jigsaw.chat.packet;

import com.jigsaw.chat.packet.FilePacket;
import com.jigsaw.chat.packet.MessagePacket;
import com.jigsaw.network.Packet;

import java.io.File;

public class ChatPacketHandler {
    public static Packet createMessagePacket(String clientUserName, String message){
        return (new MessagePacket(clientUserName, message));
    }

    public static Packet createFilePacket(String clientUserName, File file) throws Exception{
        return (new FilePacket(clientUserName, file));
    }
    public static Packet createFileReqeustPacket(String clientUsername, String fileName){
        return (new FileRequestPacket(clientUsername, fileName));
    }

    public static String extractPacket(Packet packet) {
        if (packet instanceof MessagePacket) {
            MessagePacket messagePacket = (MessagePacket) packet;
            return messagePacket.toString();
        }
        else if (packet instanceof FileRequestPacket) {
            FileRequestPacket fileRequestPacket = (FileRequestPacket) packet;
            return fileRequestPacket.toString();
        }
        else{
            throw new IllegalArgumentException("Illegal packet");
        }
    }

    public static void extractFilePacket(File file, Packet packet) throws Exception{
        if(packet instanceof FilePacket){
            FilePacket filePacket = (FilePacket) packet;
            filePacket.toFile(file);
        }
    }
}
