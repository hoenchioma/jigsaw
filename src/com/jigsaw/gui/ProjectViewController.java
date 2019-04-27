package com.jigsaw.gui;

import com.jfoenix.controls.JFXButton;
import com.jigsaw.gui.calendar.CalendarViewController;
import com.jigsaw.network.client.NetClient;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import javafx.event.ActionEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ProjectViewController implements Initializable {

    // TODO: make the icon backgrounds transparent

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
    private Label userNameLabel;

    @FXML
    private Label projectNameLabel;

    @FXML
    private Label projectIDLabel;


    @FXML
    private JFXButton logOutButton;

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
    public static Pane getRoot() throws IOException {
        Parent root = FXMLLoader.load(ProjectViewController.class.getResource("ProjectView.fxml"));
        return (Pane) root;
    }

    @FXML
    void logOutButtonAction(ActionEvent event) {
        try {
            NetClient.getInstance().logOut();
            NetClient.reset();
            changeScene("LoginView.fxml", event);
        } catch (Exception sceneChangeException) {
            sceneChangeException.printStackTrace();
        }
    }

    public void changeScene(String  location, ActionEvent event) throws IOException {
        Parent sceneView = FXMLLoader.load(getClass().getResource(location));
        Scene scene = new Scene(sceneView);
        Stage window = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }


}
