package com.jigsaw.gui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTreeTableView;
import com.jigsaw.chat.ClientMessageHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.AnchorPane;

public class ProjectViewController {

    @FXML
    private JFXButton kanBanButton;

    @FXML
    private JFXButton teamMemberButton;

    @FXML
    private JFXButton calendarButton;

    @FXML
    private AnchorPane teamMemberPane;

    @FXML
    private AnchorPane calendarPane;

    @FXML
    private AnchorPane kanBanPane;

    @FXML
    private JFXListView<?> willDoListView;

    @FXML
    private JFXListView<?> doingListView;

    @FXML
    private JFXListView<?> doneListView;

    @FXML
    private JFXTreeTableView<?> treeView;

    @FXML
    private DatePicker datePicker;

    @FXML
    private JFXButton groupChatButton;

    @FXML
    private AnchorPane groupChatPane;

    @FXML
    public void userSendMessage (ActionEvent event){
//        String userMessage = typeArea.getText();
//        typeArea.clear();
//        if(userMessage!=null && !userMessage.isEmpty()) {
//            ClientMessageHandler.sendMessage(userMessage);
//        }
    }

    public void handleButtonAction(ActionEvent event)
    {
        if(event.getTarget()==kanBanButton)
        {
            kanBanPane.setVisible(true);
            calendarPane.setVisible(false);
            teamMemberPane.setVisible((false));
            groupChatPane.setVisible((false));
        }
        else if(event.getTarget()==teamMemberButton)
        {
            kanBanPane.setVisible(false);
            calendarPane.setVisible(false);
            teamMemberPane.setVisible((true));
            groupChatPane.setVisible((false));
        }
        else if(event.getTarget()==calendarButton)
        {
            kanBanPane.setVisible(false);
            calendarPane.setVisible(true);
            teamMemberPane.setVisible((false));
            groupChatPane.setVisible((false));
        }
        else if(event.getTarget()==groupChatButton)
        {
            kanBanPane.setVisible(false);
            calendarPane.setVisible(false);
            teamMemberPane.setVisible((false));
            groupChatPane.setVisible((true));
        }
    }

}
