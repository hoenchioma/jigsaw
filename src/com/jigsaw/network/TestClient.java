package com.jigsaw.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class TestClient {
    public TestClient(String address, int port) {
        try {
            Socket socket = new Socket(address, port);
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            out.writeUTF("login");
            out.writeUTF("malu");
            out.writeUTF("kalu");

            System.out.println(in.readUTF());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new TestClient("localhost", 4444);
    }
}
