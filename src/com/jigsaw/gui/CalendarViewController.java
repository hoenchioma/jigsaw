package com.jigsaw.gui;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import java.io.IOException;
import java.net.URL;
import java.lang.String;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.jigsaw.accounts.User;
import com.jigsaw.calendar.ProjectTask;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.AnchorPane;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.Pane;
import javafx.util.Callback;


public class CalendarViewController{

    @FXML
    private AnchorPane anchorPaneID;

    @FXML
    private JFXDatePicker datePickerID;

    @FXML
    private JFXButton openProjectButtonID;

    @FXML
    private Label projectLabelID;

    @FXML
    private JFXTreeTableView<CalendarEntry> treeView;


    @FXML
    private void filter(ActionEvent event) {
    }



    @FXML
    void DatePickerAction(ActionEvent event) {

        JFXTreeTableColumn<CalendarEntry, String> taskNameCol = new JFXTreeTableColumn<>("Task Name");
        taskNameCol.setPrefWidth(150);
        taskNameCol.setCellValueFactory(param -> param.getValue().getValue().taskName);

        JFXTreeTableColumn<CalendarEntry, String> descriptionCol = new JFXTreeTableColumn<>("Task Description");
        descriptionCol.setPrefWidth(150);
        descriptionCol.setCellValueFactory(param -> param.getValue().getValue().description);

        JFXTreeTableColumn<CalendarEntry,String> memberCol = new JFXTreeTableColumn<>("Related Members");
        memberCol.setPrefWidth(150);
        memberCol.setCellValueFactory(param -> param.getValue().getValue().members);


        ArrayList<String> assigneesList = new ArrayList<String>();
        assigneesList.add("MemberList1");
        assigneesList.add("MemberList2");
        assigneesList.add("MemberList3");
        ArrayList<ProjectTask> taskList =new ArrayList<ProjectTask>();// NetClient.getInstance().getTaskSyncHandler().getTaskManager().getProjectTasks();
        taskList.add(new ProjectTask("Raheeb", LocalDateTime.now(), "User 1","001",assigneesList));
        taskList.add(new ProjectTask("Samin", LocalDateTime.now(), "User 2", "001",assigneesList));
        taskList.add(new ProjectTask("Aahad", LocalDateTime.now(), "User 3","001",assigneesList));
        taskList.add(new ProjectTask("Farhan", LocalDateTime.now(), "User 4","001",assigneesList));
        taskList.add(new ProjectTask("Wadith", LocalDateTime.now(), "User 5","001",assigneesList));
        taskList.add(new ProjectTask("Shamim", LocalDateTime.now(), "User 6","001",assigneesList));


        ObservableList<CalendarEntry> taskEntry = FXCollections.observableArrayList();
        for(int i  = 0; i<taskList.size(); i++){
            if(taskList.get(i).getDeadline().toLocalDate().equals(datePickerID.getValue())){
                String name = taskList.get(i).getName();
                String des = taskList.get(i).getDetails();
                StringBuilder memberName = new StringBuilder(assigneesList.get(0));
                for(int j = 1; j<assigneesList.size(); j++){
                    memberName.append(" ").append(assigneesList.get(j));
                }
                taskEntry.add(new CalendarEntry(name, des, memberName.toString()));
            }
        }

        final TreeItem<CalendarEntry> root = new RecursiveTreeItem<CalendarEntry>(taskEntry, RecursiveTreeObject::getChildren);
        treeView.getColumns().setAll(taskNameCol, descriptionCol, memberCol);
        treeView.setRoot(root);
        treeView.setShowRoot(false);


    }

    class CalendarEntry extends RecursiveTreeObject<CalendarEntry> {

        StringProperty taskName;
        StringProperty description;
        StringProperty members;

        public CalendarEntry(String taskName, String description, String members) {
            this.taskName = new SimpleStringProperty(taskName);
            this.description = new SimpleStringProperty(description);
            this.members = new SimpleStringProperty(members);
        }

    }

    public static Pane getRoot() throws IOException {
        Parent root = FXMLLoader.load(
                AddTaskViewController.class.getResource("CalendarView.fxml"));
        return (Pane) root;
    }
}
