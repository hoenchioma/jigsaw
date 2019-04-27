package com.jigsaw.gui;

import com.jigsaw.chat.ClientMessageHandler;
import com.jigsaw.chat.packet.ChatPacketHandler;
import com.jigsaw.chat.packet.MessagePacket;
import com.jigsaw.network.client.NetClient;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ScrollPane;
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
    private ScrollPane container;
    @FXML
    private Button sendButton;
    @FXML
    private Button fileButton;

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
    public void userSendFile (ActionEvent event){
        FileChooser fc = new FileChooser();
        List<File> selectedFiles = fc.showOpenMultipleDialog(null);
        if (selectedFiles != null){

        }
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
}
