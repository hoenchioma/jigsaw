package com.jigsaw.gui.calendar;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import java.io.IOException;
import java.lang.String;
import java.time.Period;
import java.util.ArrayList;

import com.jigsaw.accounts.User;
import com.jigsaw.calendar.ProjectTask;
import com.jigsaw.network.client.NetClient;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.Pane;


public class DayViewController {

    @FXML
    private AnchorPane anchorPaneID;

    @FXML
    private JFXDatePicker datePickerID;

    @FXML
    private JFXButton openProjectButtonID;

    @FXML
    private JFXTreeTableView<CalendarEntry> treeView;


    @FXML
    private void filter(ActionEvent event) {
    }

    void updateTable() throws InterruptedException {
        ArrayList<ProjectTask> taskList = NetClient.getInstance().getClientTaskSyncHandler().getTaskManager().getProjectTasks();


        /*assigneesList.add("MemberList1");
        assigneesList.add("MemberList2");
        assigneesList.add("MemberList3");

        taskList.add(new ProjectTask("Raheeb", LocalDateTime.now(), "User 1","001",assigneesList));
        taskList.add(new ProjectTask("Samin", LocalDateTime.now(), "User 2", "001",assigneesList));
        taskList.add(new ProjectTask("Aahad", LocalDateTime.now(), "User 3","001",assigneesList));
        taskList.add(new ProjectTask("Farhan", LocalDateTime.now(), "User 4","001",assigneesList));
        taskList.add(new ProjectTask("Wadith", LocalDateTime.now(), "User 5","001",assigneesList));
        taskList.add(new ProjectTask("Shamim", LocalDateTime.now(), "User 6","001",assigneesList));*/


        JFXTreeTableColumn<CalendarEntry, String> taskNameCol = new JFXTreeTableColumn<>("Task Name");
        taskNameCol.setPrefWidth(150);
        taskNameCol.setCellValueFactory(param -> param.getValue().getValue().taskName);

        JFXTreeTableColumn<CalendarEntry, String> descriptionCol = new JFXTreeTableColumn<>("Task Description");
        descriptionCol.setPrefWidth(150);
        descriptionCol.setCellValueFactory(param -> param.getValue().getValue().description);

        JFXTreeTableColumn<CalendarEntry,String> memberCol = new JFXTreeTableColumn<>("Related Members");
        memberCol.setPrefWidth(150);
        memberCol.setCellValueFactory(param -> param.getValue().getValue().members);

        ObservableList<CalendarEntry> taskEntry = FXCollections.observableArrayList();
        for(int i  = 0; i<taskList.size(); i++){
            if(taskList.get(i).getDeadline().toLocalDate().equals(datePickerID.getValue())){
                ArrayList<User> assigneesList = taskList.get(i).getAssignees();
                String name = taskList.get(i).getName();
                String des = taskList.get(i).getDetails();
                StringBuilder memberName = new StringBuilder(assigneesList.get(0).getUsername());
                for(int j = 1; j<assigneesList.size(); j++){
                    memberName.append(" ").append(assigneesList.get(j).getUsername());
                }
                taskEntry.add(new CalendarEntry(name, des, memberName.toString(), taskList.get(i)));
            }
        }

        final TreeItem<CalendarEntry> root = new RecursiveTreeItem<CalendarEntry>(taskEntry, RecursiveTreeObject::getChildren);
        treeView.getColumns().setAll(taskNameCol, descriptionCol, memberCol);
        treeView.setRoot(root);
        treeView.setShowRoot(false);

    }

    @FXML
    void DatePickerAction(ActionEvent event) throws InterruptedException {
        updateTable();
    }

    public void prevDayButtonAction(ActionEvent actionEvent) throws InterruptedException {
        if(datePickerID.getValue() != null ){
            datePickerID.setValue(datePickerID.getValue().minus(Period.ofDays(1)));
            updateTable();
        }

    }

    public void nextDayButtonAction(ActionEvent actionEvent) throws InterruptedException {
        if(datePickerID.getValue() != null){
            datePickerID.setValue(datePickerID.getValue().plus(Period.ofDays(1)));
            updateTable();
        }
    }

    class CalendarEntry extends RecursiveTreeObject<CalendarEntry> {

        StringProperty taskName;
        StringProperty description;
        StringProperty members;
        ProjectTask projectTasks;

        public CalendarEntry(String taskName, String description, String members, ProjectTask projectTasks) {
            this.taskName = new SimpleStringProperty(taskName);
            this.description = new SimpleStringProperty(description);
            this.members = new SimpleStringProperty(members);
            this.projectTasks = projectTasks;
        }
    }

    public static Pane getRoot() throws IOException {
        Parent root = FXMLLoader.load(
                DayViewController.class.getResource("DayView.fxml"));
        return (Pane) root;
    }
}
