package com.jigsaw.chat.packet;

import com.jigsaw.network.Packet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;

public class FilePacket implements Packet, Serializable {
    private String userName;
    private String fileName;
    private byte[] mybytearray;

    public FilePacket(){
    }

    public FilePacket(File file) throws IOException {
        this.fileName = file.getName();
        this.mybytearray = Files.readAllBytes(file.toPath());
    }

    public FilePacket(String userName, File file) throws Exception {
        this.userName = userName;
        this.fileName = file.getName();
        this.mybytearray = Files.readAllBytes(file.toPath());
        System.out.println(file.length());
    }

    public String getClientUserName() {
        return userName;
    }

    public byte[] getMybytearray() {
        return mybytearray;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setClientUserName(String clientUserName) {
        this.userName = clientUserName;
    }

    public void setMybytearray(File myFile) {
        this.mybytearray =  new byte [(int)myFile.length()];
    }

    public void toFile(File file) throws  Exception{
        FileOutputStream fos = new FileOutputStream(file.getPath());
        fos.write(mybytearray);
    }
}
