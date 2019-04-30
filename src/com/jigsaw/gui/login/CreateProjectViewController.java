package com.jigsaw.gui.login;

import com.jfoenix.controls.JFXTextArea;
import com.jigsaw.gui.GUIUtil;
import com.jigsaw.network.client.NetClient;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

/**
 * CreateProjectView Controller to create a project
 *
 * @author Shadman Wadith
 * @version %I% %G%
 */

public class CreateProjectViewController implements Initializable {

    @FXML
    private DatePicker projectDueDate;
    @FXML
    private TextField projectName;
    @FXML
    private JFXTextArea projectDescription;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

    }

    @FXML
    private void backButtonAction(Event event) {
        try {
            GUIUtil.changeScene(LoginViewController.getRoot(), event);
        } catch (Exception sceneChangeException) {
            sceneChangeException.printStackTrace();
        }
    }

    /**
     * it creates an instance of a project it generates a random projectID string
     * from the server and pass to it and then changes the scene to Login screen
     */
    @FXML
    public void createButtonAction(ActionEvent event) {


        if (projectName.getText().equals("") || projectDueDate.getValue() == null) {
            System.out.println("Give all Info");
        } else {
            try {
                String projectNameText = projectName.getText();
                String projectDescriptionText = projectDescription.getText();
                LocalDate projectDueDateValue = projectDueDate.getValue();

                String projectIDText = NetClient.getInstance().createProject(
                        projectNameText,
                        projectDescriptionText,
                        projectDueDateValue
                );

                System.out.println(projectIDText);
                GUIUtil.showAlert("Project ID : " + projectIDText, Alert.AlertType.INFORMATION);
                NetClient.getInstance().createdProjectID = projectIDText;

            } catch (Exception createProjectException) {
                createProjectException.printStackTrace();
            }


            try {
                GUIUtil.changeScene(LoginViewController.getRoot(), event);
            } catch (Exception sceneChangeException) {
                sceneChangeException.printStackTrace();
            }

            System.out.println("Project Created");
        }
    }

    /**
     * Returns the scene root (loading from fxml)
     * @return Pane type representing the scene root
     */
    public static Pane getRoot() throws IOException {
        Parent root = FXMLLoader.load(
                CreateProjectViewController.class.getResource("CreateProjectView.fxml"));
        return (Pane) root;
    }
}
