/**
 * LoginViewController class
 * Handles LoginUI
 *
 * @version %I% %G%
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

    /**
     * sets the default server Address which is localhost and default port address 4444
     * @param arg0
     * @param arg1
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1){
        popBox.setVisible(false);
        serverAddress.setText(NetClient.DEFAULT_SERVER_ADDR);
        portAddress.setText(NetClient.DEFAULT_SERVER_PORT + "");
        if (NetClient.getInstance().createdProjectID != null) {
            projectID.setText(NetClient.getInstance().createdProjectID);
        }
    }

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


    /**
     * pops a small pane which sets information of server and and port address
     * @param event
     */
    @FXML
    void popUpButtonAction(ActionEvent event) {

        if (!popBox.isVisible()) popBox.setVisible(true);
        else {
            popBox.isVisible();
            popBox.setVisible(false);

            String server = serverAddress.getText();
            int port = Integer.parseInt(portAddress.getText());

            NetClient.getInstance().setCurrentServerAddress(server);
            NetClient.getInstance().setCurrentServerPort(port);
        }
    }

    /**
     * Loads signUp scree
     * @param event
     * @throws IOException
     */
    @FXML
    public void signUpAction (ActionEvent event) throws IOException {
        Parent signupView = FXMLLoader.load(getClass().getResource("RegistrationView.fxml"));
        Scene  signupScene = new Scene(signupView);
        Stage window = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        window.setScene(signupScene);
        window.show();
    }

    /**
     * checks the entered username,password and project id from the server and store the response given from server
     *
     * @param event
     * @throws Exception
     */

    @FXML
    public void loginAction(ActionEvent event) throws Exception {
        String usernameString = username.getText();
        String passwordString = password.getText();
        String projectIDString = projectID.getText();

        String response = NetClient.getInstance().login(usernameString, passwordString, projectIDString);

        System.out.println(response);
        if (response.equals("success")) {
            // change to next scene on login
            changeToNextScene(ProjectViewController.getRoot(),true);
        }
        else
        {


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

    public void changeToNextScene(Parent root,boolean resizability) throws IOException {
        Scene scene = new Scene(root);
        Stage window = (Stage) username.getScene().getWindow();
        window.setScene(scene);
        window.show();
        window.setResizable(resizability);
        window.centerOnScreen();
    }
    public static Pane getRoot() throws IOException {
        Parent root = FXMLLoader.load(LoginViewController.class.getResource("LoginView.fxml"));
        return (Pane) root;
    }


    public void showError(String errorMessage){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(errorMessage);
        alert.setTitle("JIGSAW");
        alert.show();
    }
}
