package com.jigsaw.gui;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import java.net.URL;
import java.lang.String;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.TreeItem;
import javafx.util.Callback;

public class AddTaskViewController implements Initializable {

    // FIXME: This scene doesn't load

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

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
}
