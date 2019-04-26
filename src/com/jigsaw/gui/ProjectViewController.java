package com.jigsaw.gui;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import javafx.event.ActionEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class ProjectViewController implements Initializable {

    @Override

    public void initialize(URL url , ResourceBundle resourceBundle)
    {
        kanBanButton.setVisible(false);
        dayViewButton.setVisible(false);
        addTaskButton.setVisible(false);
        teamMemberButton.setLayoutY(teamMemberButton.getLayoutY()-150);
        groupChatButton.setLayoutY(groupChatButton.getLayoutY()-150);
    }

    private int slideFlag=1;
    @FXML
    private JFXButton dayViewButton;

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
    void addTaskButtonAction()
    {
         loadUI("calendar/AddTaskView.fxml");
    }
    @FXML
    void  kanBanAction(){
        loadUI("calendar/KanBanView.fxml");
    }

    @FXML

    void dayViewButtonAction()
    {
        loadUI("calendar/DayView.fxml");
    }
    @FXML
    void calendarButtonAction()
    {
        loadUI("calendar/DayView.fxml");
    }

    @FXML
    void groupChatButtonAction(){
        loadUI("ChatView.fxml");
    }

    @FXML
    void slideDownButton(ActionEvent event)
    {
        if(event.getTarget()==calendarButton)
        {
            teamMemberButton.setLayoutY(teamMemberButton.getLayoutY()+slideFlag*150);
            groupChatButton.setLayoutY(groupChatButton.getLayoutY()+slideFlag*150);
        }
        if(slideFlag==1)
        {
            kanBanButton.setVisible(true);
            dayViewButton.setVisible(true);
            addTaskButton.setVisible(true);
        }
        else
        {
            kanBanButton.setVisible(false);
            dayViewButton.setVisible(false);
            addTaskButton.setVisible(false);
        }
        slideFlag*=-1;
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
