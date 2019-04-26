/*
 * Profile
 *
 * JAVA 11.0.2
 *
 * @author Shadman Wadith
 */
package com.jigsaw.gui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jigsaw.gui.calendar.CalendarViewController;
import com.jigsaw.network.client.NetClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.io.IOException;

public class LoginViewController {

    @FXML
    private JFXTextField username;

    @FXML
    private JFXPasswordField password;

    @FXML
    private JFXTextField projectID;

    @FXML
    private JFXButton signUpButton;

    @FXML
    private JFXButton createProjectButton;


    @FXML
    public void signUpAction (ActionEvent event) throws IOException {
        Parent signupView = FXMLLoader.load(getClass().getResource("RegistrationView.fxml"));
        Scene  signupScene = new Scene(signupView);
        Stage window = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        window.setScene(signupScene);
        window.show();
    }
    @FXML
    public void loginAction(ActionEvent event) throws Exception {
        String usernameString = username.getText();
        String passwordString = password.getText();
        String projectIDString = projectID.getText();

        String response = NetClient.getInstance().login(usernameString, passwordString, projectIDString);

        System.out.println(response);
        if (response.equals("success")) {
            // change to next scene on login
            // FIXME: set scene to dashboard (project view) here
            changeToNextScene(CalendarViewController.getRoot());
        }
    }

    @FXML
    public void createProjectAction(ActionEvent event) throws  IOException{
        Parent createProjectView = FXMLLoader.load(getClass().getResource("CreateProjectView.fxml"));
        Scene createProjectScene = new Scene(createProjectView);
        Stage window = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        window.setScene(createProjectScene);
        window.show();
    }

    public void changeToNextScene(Parent root) throws IOException {
        Scene scene = new Scene(root);
        Stage window = (Stage) username.getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
}