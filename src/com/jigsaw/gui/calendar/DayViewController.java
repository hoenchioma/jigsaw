package com.jigsaw.gui.calendar;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import java.io.IOException;
import java.lang.String;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.util.ArrayList;

import com.jigsaw.accounts.User;
import com.jigsaw.calendar.ProjectTask;
import com.jigsaw.network.client.NetClient;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class DayViewController {

    @FXML
    private AnchorPane anchorPaneID;

    @FXML
    private JFXDatePicker datePickerID;

    @FXML
    private JFXTreeTableView<CalendarEntry> treeView;

    @FXML
    private TreeTableColumn<CalendarEntry, String> taskNameCol;

    @FXML
    private TreeTableColumn<CalendarEntry, String> taskDesCol;

    @FXML
    private TreeTableColumn<CalendarEntry, String> memberCol;

    @FXML
    private JFXButton previousDayButtionID;

    @FXML
    private JFXButton nextDayButtonID;


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


        ProjectTask rootProjectTask = new ProjectTask("name", LocalDateTime.now(), "creator", "00", new ArrayList<String>());
        TreeItem<CalendarEntry> root = new TreeItem<>(new CalendarEntry("Task Name", "Task Description", "Members", rootProjectTask));





        ObservableList<TreeItem<CalendarEntry>> taskEntry = FXCollections.observableArrayList();
        for (int i = 0; i < taskList.size(); i++) {
            if (taskList.get(i).getDeadline().toLocalDate().equals(datePickerID.getValue())) {
                ArrayList<User> assigneesList = taskList.get(i).getAssignees();
                String name = taskList.get(i).getName();
                String des = taskList.get(i).getDetails();
                StringBuilder memberName = new StringBuilder(assigneesList.get(0).getUsername());
                for (int j = 1; j < assigneesList.size(); j++) {
                    memberName.append(" ").append(assigneesList.get(j).getUsername());
                }
                taskEntry.add(new TreeItem<>(new CalendarEntry(name, des, memberName.toString(), taskList.get(i))));
                root.getChildren().add(taskEntry.get(taskEntry.size()-1));
            }
        }

        taskNameCol.setCellValueFactory(param -> param.getValue().getValue().taskName);
        taskDesCol.setCellValueFactory(param -> param.getValue().getValue().description);
        memberCol.setCellValueFactory(param -> param.getValue().getValue().members);

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


    public static void editTask(ProjectTask projectTask) {
        Stage window = new Stage();
        boolean checkBoxFlag = false;
        //Block events to other windows
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Edit Task");
        window.setMinWidth(250);

        Menu menu = new Menu("Member List");
        ArrayList<CheckBox> checkBoxes = new ArrayList<>();


        VBox vBox = new VBox();
        JFXTextField taskName = new JFXTextField(projectTask.getName());
        JFXTextArea taskDescription = new JFXTextArea(projectTask.getDetails());
        JFXDatePicker datePicker = new JFXDatePicker(LocalDate.now());
        datePicker.setEditable(false);

        Button closeButton = new Button("Add Task");
        closeButton.setOnAction(e -> overWriteData(projectTask, taskName, taskDescription, datePicker, window));



        vBox.getChildren().addAll(datePicker, taskName, taskDescription, closeButton);
        vBox.setAlignment(Pos.TOP_LEFT);

        Scene scene = new Scene(vBox);
        window.setScene(scene);
        window.showAndWait();

    }

    public static void overWriteData(ProjectTask projectTask, TextField taskName, TextArea taskDescription, DatePicker datePicker, Stage window){
        System.out.println("overwrite starts");
        projectTask.setName(taskName.getText());
        projectTask.setDetails(taskDescription.getText());
        projectTask.setDeadline(LocalDateTime.of(datePicker.getValue(), LocalTime.now()));
        window.close();
    }


    public void tableClickAction (MouseEvent mouseEvent) throws InterruptedException {
        if (mouseEvent.getButton() == MouseButton.SECONDARY) {
            CalendarEntry selectedEntry = treeView.getSelectionModel().getSelectedItem().getValue();
            editTask(selectedEntry.projectTasks);



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
