package com.jigsaw.gui.main;

import com.jfoenix.controls.JFXButton;
import com.jigsaw.gui.calendar.AddTaskViewController;
import com.jigsaw.gui.calendar.DayViewController;
import com.jigsaw.gui.calendar.KanbanViewController;
import com.jigsaw.gui.chat.ChatViewController;
import com.jigsaw.gui.login.LoginViewController;
import com.jigsaw.network.client.NetClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * ProjectViewController Class
 * It controls the projectVIew
 *
 * @author Shadman Wadith
 * @version %I% %G%
 */
public class ProjectViewController implements Initializable {

    private int slideFlag = 1;
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
    private AnchorPane UIPane;

    public static Pane getRoot() throws IOException {
        Parent root = FXMLLoader.load(ProjectViewController.class.getResource("ProjectView.fxml"));
        return (Pane) root;
    }


    /**
      Initializes the initial view of Project
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userNameLabel.setText("Username : " + NetClient.getInstance().getClientAccountSyncHandler().getUser().getUsername());
        projectIDLabel.setText("Project ID : " + NetClient.getInstance().getClientAccountSyncHandler().getProject().getId());
        projectNameLabel.setText("Project Name : " + NetClient.getInstance().getClientAccountSyncHandler().getProject().getName());
        kanBanButton.setVisible(false);
        dayViewButton.setVisible(false);
        addTaskButton.setVisible(false);
        //teamMemberButton.setLayoutY(teamMemberButton.getLayoutY()-150);
        groupChatButton.setLayoutY(groupChatButton.getLayoutY() - 150);
        try {
            loadUI(ChatViewController.getRoot());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void addTaskButtonAction() throws IOException {
        loadUI(AddTaskViewController.getRoot());
    }

    @FXML
    void kanBanAction() throws IOException {
        loadUI(KanbanViewController.getRoot());
    }

    @FXML
    void dayViewButtonAction() throws IOException {
        loadUI(DayViewController.getRoot());
    }

    @FXML
    void calendarButtonAction() throws IOException {
        loadUI(DayViewController.getRoot());
    }

    @FXML
    void groupChatButtonAction() throws IOException {
        loadUI(ChatViewController.getRoot());
    }

    /**
     * a method to slide down the underneath buttons and show the hidden buttons
     */
    @FXML
    void slideDownButton(ActionEvent event) {
        if (event.getTarget() == calendarButton) {
            // teamMemberButton.setLayoutY(teamMemberButton.getLayoutY()+slideFlag*150);
            groupChatButton.setLayoutY(groupChatButton.getLayoutY() + slideFlag * 150);
        }
        if (slideFlag == 1) {
            kanBanButton.setVisible(true);
            dayViewButton.setVisible(true);
            addTaskButton.setVisible(true);
        } else {
            kanBanButton.setVisible(false);
            dayViewButton.setVisible(false);
            addTaskButton.setVisible(false);
        }
        slideFlag *= -1;
    }

    /**
     * loads the anchor pane with the given pane
     *
     * @param root the root pane of the scene
     */
    private void loadUI(Pane root) {
        UIPane.getChildren().setAll(root);
        AnchorPane.setTopAnchor(root, 0.0);
        AnchorPane.setBottomAnchor(root, 0.0);
        AnchorPane.setLeftAnchor(root, 0.0);
        AnchorPane.setRightAnchor(root, 0.0);
    }

    /**
     * performs logout and get to the sign-up page and  notify the server that the user has logged out
     */
    @FXML
    void logOutButtonAction(ActionEvent event) {
        try {
            NetClient.getInstance().logOut();
            NetClient.reset();
            changeScene(LoginViewController.getRoot(), event, false);
        } catch (Exception sceneChangeException) {
            sceneChangeException.printStackTrace();
        }
    }

    /**
     * a method to change the scene of the full window
     *
     * @param sceneView the root pane of the scene which will be loaded
     * @param resizability whether the scene should be resizable
     */
    private void changeScene(Pane sceneView, ActionEvent event, boolean resizability) throws IOException {
        Scene scene = new Scene(sceneView);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.setResizable(resizability);
        window.show();
    }
}
