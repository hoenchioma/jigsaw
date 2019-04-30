package com.jigsaw.gui.login;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jigsaw.gui.GUIUtil;
import com.jigsaw.gui.main.ProjectViewController;
import com.jigsaw.network.client.NetClient;
import javafx.event.ActionEvent;
import javafx.event.Event;
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
import java.net.UnknownHostException;
import java.util.ResourceBundle;

/**
 * LoginViewController class
 * Handles LoginUI
 *
 * @author Shadman Wadith
 * @version %I% %G%
 */
public class LoginViewController implements Initializable {

    @FXML
    private JFXButton login;
    @FXML
    private JFXButton signUpButton;
    @FXML
    private JFXButton createProjectButton;
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

    public static Pane getRoot() throws IOException {
        Parent root = FXMLLoader.load(LoginViewController.class.getResource("LoginView.fxml"));
        return (Pane) root;
    }

    /**
     * sets the default server Address which is localhost and default port address 4444
     *
     * @param arg0
     * @param arg1
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        popBox.setVisible(false);
        serverAddress.setText(NetClient.DEFAULT_SERVER_ADDR);
        portAddress.setText(NetClient.DEFAULT_SERVER_PORT + "");
        if (NetClient.getInstance().createdProjectID != null) {
            projectID.setText(NetClient.getInstance().createdProjectID);
        }
    }

    /**
     * pops a small pane which sets information of server and and port address
     *
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
     * Loads signUp screen
     *
     * @param event
     * @throws IOException
     */
    @FXML
    public void signUpAction(ActionEvent event) throws IOException {
        Parent signUpView = RegistrationViewController.getRoot();
        GUIUtil.changeScene(signUpView, event);
    }

    /**
     * checks the entered username,password and project id from the server and store the response given from server
     *
     * @param event
     * @throws Exception
     */

    @FXML
    public void loginAction(ActionEvent event) {
        String usernameString = username.getText();
        String passwordString = password.getText();
        String projectIDString = projectID.getText();

        try {
            NetClient.getInstance().connect();

            String response = NetClient.getInstance().login(usernameString, passwordString, projectIDString);

            if (response.equals("success")) {
                // change to next scene on login
                try {
                    GUIUtil.changeScene(ProjectViewController.getRoot(),
                            (Stage) username.getScene().getWindow(), true);
                } catch (IOException e) {
                    e.printStackTrace();
                    GUIUtil.showError("Error loading next screen");
                }
            } else {
                GUIUtil.showError(response);
            }
            System.out.println(response);
        } catch (UnknownHostException e) {
            GUIUtil.showError("Cannot find server");
        } catch (IOException e) {
            e.printStackTrace();
            GUIUtil.showError("Error finding or connecting to server");
        }
    }

    @FXML
    public void createProjectAction(ActionEvent event) throws IOException {
        Parent createProjectView = CreateProjectViewController.getRoot();
        Scene createProjectScene = new Scene(createProjectView);
        Stage window = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        window.setScene(createProjectScene);
        window.show();
    }
}
