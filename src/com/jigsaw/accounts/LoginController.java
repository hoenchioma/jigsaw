/*
 * Profile
 *
 * JAVA 11.0.2
 *
 * @author Shadman Wadith
 */
package com.jigsaw.accounts;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jigsaw.network.client.NetClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.io.IOException;

public class LoginController {

    @FXML
    private JFXTextField username;

    @FXML
    private JFXPasswordField password;


    @FXML
    public void signUp (ActionEvent event) throws IOException {
        Parent signupView = FXMLLoader.load(getClass().getResource("Registration.fxml"));
        Scene  signupScene = new Scene(signupView);
        Stage window = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        window.setScene(signupScene);
        window.show();
    }
    @FXML
    public void login(ActionEvent event) throws IOException {
        String usernameString = username.getText();
        String passwordString = password.getText();

        // FIXME: Gets stuck on login
        String response = NetClient.getInstance().login(usernameString, passwordString);

        System.out.println(response);
    }
}
