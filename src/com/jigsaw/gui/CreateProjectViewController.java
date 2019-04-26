/*
 * CrateProjectController
 *
 * JAVA 11.0.2
 *
 * @author Shadman Wadith
 */

package com.jigsaw.gui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jigsaw.accounts.Project;
import com.jigsaw.network.client.NetClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class CreateProjectViewController implements Initializable {

    @Override
    public void initialize(URL arg0, ResourceBundle arg1){

    }

    @FXML
    private JFXButton submitButton;

    @FXML
    private DatePicker projectDueDate;

    @FXML
    private TextField projectName;

    @FXML
    private TextField projectID;

    @FXML
    private JFXTextArea projectDescription;

    @FXML
    public void createButtonAction(ActionEvent event) {

        /**
         * if all textFields are not filled
         */

        if (projectName.getText().equals("") || projectDueDate.getValue() == null) {
            System.out.println("Give all Info");
        }
        else {
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

    public void changeScene(String  location, ActionEvent event) throws IOException {
        Parent sceneView = FXMLLoader.load(getClass().getResource(location));
        Scene scene = new Scene(sceneView);
        Stage window = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
}
