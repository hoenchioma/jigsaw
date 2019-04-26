package com.jigsaw.gui.calendar;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.event.ActionEvent;
import javafx.scene.layout.Pane;

public class AddTaskViewController implements Initializable {
    //Project project;    ///////assign project
    @FXML
    private JFXDatePicker deadLineDatePickerID;

    @FXML
    private JFXTextField creatorNameID;

    @FXML
    private JFXTextField taskNameID;

    @FXML
    private JFXTextArea taskDescriptionID;

    @FXML
    private JFXButton addTaskButtonID;

    @FXML
    void addTaskButtonAction(ActionEvent event) {
        if(!creatorNameID.getText().isBlank() && !taskNameID.getText().isBlank() && !taskDescriptionID.getText().isBlank()){
            //////add task to project
            System.out.println("fictional task created");
            //project.addTask(new Task(taskNameID.getText(), deadLineDatePickerID.getValue()., creatorNameID.getText()));
        }
    }

    public static Pane getRoot() throws IOException {
        Parent root = FXMLLoader.load(
                AddTaskViewController.class.getResource("AddTaskView.fxml"));
        return (Pane) root;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
}
