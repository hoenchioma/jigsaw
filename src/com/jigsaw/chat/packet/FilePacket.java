package com.jigsaw.chat.packet;

import com.jigsaw.network.Packet;

import java.io.File;
import java.io.Serializable;

public class FilePacket implements Packet, Serializable {
    private String clientUserName;
    private byte [] mybytearray;

    public FilePacket(){

    }

    public FilePacket(String clientUserName, File file){
        this.clientUserName = clientUserName;
        this.mybytearray = new byte [(int)file.length()];
    }

    public String getClientUserName() {
        return clientUserName;
    }

    public byte[] getMybytearray() {
        return mybytearray;
    }

    public void setClientUserName(String clientUserName) {
        this.clientUserName = clientUserName;
    }

    public void setMybytearray(File myFile) {
        this.mybytearray =  new byte [(int)myFile.length()];
    }
}
