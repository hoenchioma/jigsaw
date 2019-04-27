package com.jigsaw.chat.packet;

import com.jigsaw.network.Packet;

import java.io.Serializable;

public class FileRequestPacket implements Packet, Serializable {
    private String clientUserName;
    private String fileName;

    public  FileRequestPacket(){

    }

    public FileRequestPacket(String clientUserName, String fileName){
        this.clientUserName =  clientUserName;
        this.fileName = fileName;
    }

    public String getClientUserName() {
        return this.clientUserName;
    }

    public void setClientUserName(String clientUserName) {
        this.clientUserName = clientUserName;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString(){
        return this.fileName;
    }
}
