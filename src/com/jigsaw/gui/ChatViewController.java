package com.jigsaw.gui;

import com.jigsaw.chat.ClientMessageHandler;
import com.jigsaw.chat.packet.ChatPacketHandler;
import com.jigsaw.chat.packet.FileRequestPacket;
import com.jigsaw.chat.packet.MessagePacket;
import com.jigsaw.network.Packet;
import com.jigsaw.network.client.NetClient;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.List;

/**
 *
 * Class for controlling chat GUI
 *
 * @author A.M. Aahad
 *
 */

public class ChatViewController {
    @FXML
    private TextField typeArea;
    @FXML
    private TextArea chatBox;
    @FXML
    private ScrollPane chatScrollPane;
    @FXML
    private ScrollPane fileScrollPane;
    @FXML
    private Button sendButton;
    @FXML
    private Button fileButton;
    @FXML
    private VBox fileBox;

    private ClientMessageHandler clientMessageHandler;

    @FXML
    public void initialize() {
//        this.chatBox = new TextArea();
//        this.container = new ScrollPane();
//        this.container.setContent(this.chatBox);

        clientMessageHandler = NetClient.getInstance().getClientMessageHandler();
        clientMessageHandler.setController(this);
        for (MessagePacket messages: clientMessageHandler.getMessageList()) {
            textAppend(ChatPacketHandler.extractPacket(messages));
        }
        for (FileRequestPacket files: clientMessageHandler.getFileList()) {
            fileAppend(ChatPacketHandler.extractPacket(files));
        }
    }

    //method which is called to send message from GUI
    @FXML
    public void userSendMessage (ActionEvent event){
        String userMessage = typeArea.getText();
        typeArea.clear();
            if(userMessage!=null && !userMessage.isEmpty()) {
                clientMessageHandler.sendMessage(userMessage);
            }
    }

    @FXML
    public void userSendFile (ActionEvent event) throws Exception{
        FileChooser fc = new FileChooser();
        List<File> selectedFiles = fc.showOpenMultipleDialog(null);
        if (selectedFiles != null){
            for(int i=0; i<selectedFiles.size(); i++){
                if(selectedFiles.get(i).length() <= 10240){
                    clientMessageHandler.sendFile(selectedFiles.get(i));
                }
                else{
                    System.out.println("File size too big");
                }
            }
        }
    }

    public void userSendFileRequest(String fileName){
        clientMessageHandler.sendFileRequest(fileName);
    }

    //method to append text in the textarea
    @FXML
    public synchronized void textAppend(String appendMessage){
        try {
            this.chatBox.appendText(appendMessage+"\n");
        } catch (Exception e){
            System.out.println(e);
        }
    }

    //method to append file name in the filearea
    @FXML public synchronized void fileAppend(String appendName){
        Hyperlink fileLink = new Hyperlink(appendName);
        fileLink.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                userSendFileRequest(appendName);
            }
        });
        fileBox.getChildren().add(fileLink);
    }

    @FXML
    public void saveFile (Packet receivedPacket) throws Exception{
        FileChooser fc = new FileChooser();
        File file = fc.showSaveDialog(null);
        if(file != null){
            ClientMessageHandler.saveFile(file, receivedPacket);
        }
    }
}
