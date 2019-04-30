package com.jigsaw.gui.calendar;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jigsaw.accounts.User;
import com.jigsaw.calendar.ProjectTask;
import com.jigsaw.calendar.Task;
import com.jigsaw.calendar.sync.ClientTaskSyncHandler;
import com.jigsaw.network.client.NetClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * add tasks to project
 */

public class AddTaskViewController implements Initializable {

    private ArrayList<CheckMenuItem> checkMenu = new ArrayList<CheckMenuItem>();

    private Map<String, User> userDictionary = NetClient.getInstance().getClientTaskSyncHandler().getUserDictionary();


    @FXML
    private JFXDatePicker deadLineDatePickerID;

    @FXML
    private JFXTextField priorityID;

    @FXML
    private JFXTextField taskNameID;

    @FXML
    private JFXTextArea taskDescriptionID;

    @FXML
    private JFXButton addTaskButtonID;

    @FXML
    private Menu menuID;

    @FXML
    void addTaskButtonAction(ActionEvent event) throws InterruptedException, IOException {
        ArrayList<String> assignees = new ArrayList<>();
        for (int i = 0; i < checkMenu.size(); i++) {
            if (checkMenu.get(i).isSelected()) {
                assignees.add(checkMenu.get(i).getText());
            }
        }

        Label label = new Label();
        Stage window = new Stage();
        //Block events to other windows
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Alert");
        window.setMinWidth(250);
        Button closeButton = new Button("OK");
        closeButton.setOnAction(e -> window.close());

        if (priorityID.getText().isBlank()) {
            priorityID.setText(Integer.toString(Task.DEFAULT_PRIORITY));
        }

        if (Integer.parseInt(priorityID.getText()) > 0
                && !taskNameID.getText().isBlank()
                && !taskDescriptionID.getText().isBlank()
                && assignees.size() > 0
                && deadLineDatePickerID.getValue() != null) {
            String projectID = NetClient.getInstance().getClientAccountSyncHandler().getProject().getId();
            ClientTaskSyncHandler clientTaskSyncHandler = NetClient.getInstance().getClientTaskSyncHandler();
            String creatorName = NetClient.getInstance().getClientAccountSyncHandler().getUser().getUsername();
            ProjectTask newProjectTask = new ProjectTask(taskNameID.getText(), LocalDateTime.of(deadLineDatePickerID.getValue(), LocalTime.now()), creatorName, projectID, assignees);
            newProjectTask.setDetails(taskDescriptionID.getText());
            newProjectTask.setPriority(Integer.parseInt(priorityID.getText()));
            clientTaskSyncHandler.addTask(newProjectTask);
            window.setTitle("Notification");
            label.setText("New Task Created");
        } else if ((Integer.parseInt(priorityID.getText()) <= 0)) {
            label.setText("Set a positive priority value");
        } else if (taskNameID.getText().isBlank()) {
            label.setText("No Task Name has been set");
        } else if (taskDescriptionID.getText().isBlank()) {
            label.setText("No Task Description has been set");
        } else if (assignees.size() <= 0) {
            label.setText("No Member selected");
        } else if (deadLineDatePickerID.getValue() == null) {
            label.setText("No deadline selected");
        }

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, closeButton);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(10, 5, 10, 5));

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        menuID.setText("Project Members");
        int j = 0;
        for (Map.Entry<String, User> entry : userDictionary.entrySet()) {
            checkMenu.add(new CheckMenuItem(entry.getKey()));
            menuID.getItems().add(checkMenu.get(j++));
        }
    }

    public static Pane getRoot() throws IOException {
        Parent root = FXMLLoader.load(
                AddTaskViewController.class.getResource("AddTaskView.fxml"));
        return (Pane) root;
    }
}
