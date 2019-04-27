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
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginViewController implements Initializable {

    @Override
    public void initialize(URL arg0, ResourceBundle arg1){
            popBox.setVisible(false);

    }

    //To Check whether the popBox is open or not;


    @FXML
    private VBox popBox;

    @FXML
    private JFXTextField username;

    @FXML
    private JFXPasswordField password;

    @FXML
    private JFXTextField projectID;

    @FXML
    private JFXTextField serverAddress;

    @FXML
    private JFXTextField portAddress;



    @FXML
    void popUpButtonAction(ActionEvent event) {

        if (popBox.isVisible() == false) popBox.setVisible(true);
        else if (popBox.isVisible() == true) {
            popBox.setVisible(false);


            String server = serverAddress.getText();
            String port = portAddress.getText();

            //TODO Server Address and  port Address implementation

        }
    }
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
            changeToNextScene(ProjectViewController.getRoot());
        }
        else
        {
            // TODO 3 different error messages

            showError(response);
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
    public static Pane getRoot() throws IOException {
        Parent root = FXMLLoader.load(LoginViewController.class.getResource("LoginView.fxml"));
        return (Pane) root;
    }

    // a method to show erro message
    public void showError(String erroMessage){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(erroMessage);
        alert.setTitle("JIGSAW");
        alert.show();
    }
}
