package com.jigsaw.chat;

import com.jigsaw.network.Packet;
import javafx.application.Platform;


import java.io.*;

/**
 * Class to write objects into a file
 *
 * @author A.M. Aahad
 */

public class ServerMessageHandler {

    //TODO: fix directory
    private static String dir = "D:\\Learn\\AahadChatModule\\src\\sample\\Resources";
    private static FileOutputStream fout;
    private static AppendingObjectOutputStream aoosF;

    //method to write chat history to file
    synchronized public static void writeToFile(Packet packetToWrite) throws IOException{
        fout = new FileOutputStream(dir + File.separator + "ChatHistory.bin", true);
        aoosF = new AppendingObjectOutputStream(fout);
        aoosF.writeObject(packetToWrite);
        aoosF.close();
        fout.close();
    }

    //method to read chat history from file
    public void loadFromFile(String dir, ObjectOutputStream oos) throws Exception{
        FileInputStream fis = new FileInputStream(dir + File.separator + "ChatHistory.bin");
        ObjectInputStream ois = new ObjectInputStream(fis);
        Object obj = ois.readObject();
        while (true) {
            obj = ois.readObject();
            if (obj != null) {
                Packet pac = (Packet) obj;
                oos.writeObject(pac);
                oos.flush();
            }
            else {
                System.out.println("Exited");
                break;
            }
        }
    }
}
