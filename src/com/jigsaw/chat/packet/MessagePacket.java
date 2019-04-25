package com.jigsaw.chat.packet;

import com.jigsaw.network.Packet;

import java.io.Serializable;

public class MessagePacket implements Packet, Serializable {
    private String clientUserName;
    private String message;

    public MessagePacket(){

    }

    public MessagePacket(String clientUserName, String message){
        this.clientUserName =  clientUserName;
        this.message = message;
    }

    public String getClientUserName() {
        return this.clientUserName;
    };

    public void setClientUserName(String clientUserName) {
        this.clientUserName = clientUserName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString(){
        return this.clientUserName + ":\n" + this.message +"\n";
    }
}
