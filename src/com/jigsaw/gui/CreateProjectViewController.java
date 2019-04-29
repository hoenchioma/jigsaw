package com.jigsaw.gui;

import com.jfoenix.controls.JFXTextArea;
import com.jigsaw.network.client.NetClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
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
    void backButtonAction(ActionEvent event) {
        try {
            changeScene("LoginView.fxml", event);
        } catch (Exception sceneChangeException) {
            sceneChangeException.printStackTrace();
        }
    }

    /**
     * it creates an instance of a project it generates a random projectID string from the server and pass to it and then changes the scene to Login screen
     *
     * @param event
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
                showMessage("Project ID : " + projectIDText);
                NetClient.getInstance().createdProjectID = projectIDText;

            } catch (Exception createProjectException) {
                createProjectException.printStackTrace();
            }


            try {
                changeScene("LoginView.fxml", event);
            } catch (Exception sceneChangeException) {
                sceneChangeException.printStackTrace();
            }

            System.out.println("Project Created");
        }
    }

    public void changeScene(String location, ActionEvent event) throws IOException {
        Parent sceneView = FXMLLoader.load(getClass().getResource(location));
        Scene scene = new Scene(sceneView);
        Stage window = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    public void showMessage(String Message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Project Created");
        alert.setContentText(Message);
        alert.setTitle("JIGSAW");
        alert.show();
    }
}
