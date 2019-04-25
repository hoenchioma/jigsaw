package com.jigsaw.gui;

import com.jigsaw.chat.ClientMessageHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ScrollPane;

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

    //constructor
    public ChatViewController() throws Exception {
        this.chatBox = new TextArea();
        this.container = new ScrollPane();
        this.container.setContent(this.chatBox);
    }

    //method which is called to send message from GUI
    @FXML
    public void userSendMessage (ActionEvent event){
        String userMessage = typeArea.getText();
        typeArea.clear();
            if(userMessage!=null && !userMessage.isEmpty()) {
                ClientMessageHandler.sendMessage(userMessage);
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
