package com.jigsaw.gui;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import javafx.event.ActionEvent;

public class ProjectViewController {

    @FXML
    private JFXButton kanBanButton;

    @FXML
    private JFXButton taskButton;

    @FXML
    private JFXButton teamMemberButton;

    @FXML
    private JFXButton groupChatButton;

    @FXML
    private JFXButton calendarButton;

    @FXML
    private AnchorPane testPane;
    @FXML
    private BorderPane borderPane;

    @FXML
    private JFXButton addTaskButton;

    @FXML
    void handleButtonAction(ActionEvent event)
    {


    }
    @FXML
    void addTaskButtonAction(ActionEvent event)
    {
         loadUI("AddTaskView.fxml");
    }
    @FXML
    void  kanBanAction(ActionEvent event){
        loadUI("KanBanView.fxml");
    }

    @FXML
    void calendarButtonAction(ActionEvent event)
    {
        loadUI("CalendarView.fxml");
    }

    @FXML
    void groupChatButtonAction(ActionEvent event){
        loadUI("ChatView.fxml");
    }

    public void loadUI(String location)
    {
        Parent root=null;
        try {

                 root= FXMLLoader.load(getClass().getResource(location));
            }
            catch (Exception e)
            {
                System.out.println(e);
            }

            borderPane.setCenter(root);


    }


}
