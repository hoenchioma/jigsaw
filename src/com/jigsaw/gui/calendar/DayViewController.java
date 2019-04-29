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
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import com.jigsaw.accounts.User;
import com.jigsaw.calendar.Progress;
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

/**
 * shows the project tasks on a table based on deadline.
 * Tasks can be edited.
 */

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

    @FXML void initialize(){
        datePickerID.setValue(LocalDate.now());
        updateTable();
    }

    @FXML
    public void DatePickerAction(ActionEvent event){
        updateTable();
    }

    public void updateTable(){
        ArrayList<ProjectTask> taskList = NetClient.getInstance().getClientTaskSyncHandler().getTaskManager().getProjectTasks();

        ProjectTask rootProjectTask = new ProjectTask("name", LocalDateTime.now(), "creator", "00", new ArrayList<String>());
        TreeItem<CalendarEntry> root = new TreeItem<>(new CalendarEntry("Task Name", "Task Description", "Members", rootProjectTask));

        ObservableList<TreeItem<CalendarEntry>> taskEntry = FXCollections.observableArrayList();
        for (int i = 0; i < taskList.size(); i++) {
            if (taskList.get(i).getDeadline().toLocalDate().equals(datePickerID.getValue()) && taskList.get(i).getProgress() != Progress.done) {
                Map<String, User> userDictionary = NetClient.getInstance().getClientTaskSyncHandler().getUserDictionary();
                ArrayList<User> assigneesList = new ArrayList<>();

                for(String assigneeID : taskList.get(i).getAssigneeIDs()) {
                    assigneesList.add(userDictionary.get(assigneeID));
                }

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

    public void prevDayButtonAction(ActionEvent actionEvent){
        if(datePickerID.getValue() != null ){
            datePickerID.setValue(datePickerID.getValue().minus(Period.ofDays(1)));
            updateTable();
        }

    }

    public void nextDayButtonAction(ActionEvent actionEvent){
        if(datePickerID.getValue() != null){
            datePickerID.setValue(datePickerID.getValue().plus(Period.ofDays(1)));
            updateTable();
        }
    }

    public void tableClickAction (MouseEvent mouseEvent){
        if (mouseEvent.getButton() == MouseButton.SECONDARY) {
            CalendarEntry selectedEntry = treeView.getSelectionModel().getSelectedItem().getValue();
            editTask(selectedEntry.projectTasks, datePickerID.getValue());
            updateTable();
        }
    }


    public static void editTask(ProjectTask projectTask, LocalDate localDate) {
        Stage window = new Stage();
        //Block events to other windows
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Edit Task");
        window.setMinWidth(250);
        Menu menu = new Menu("Member List");
        VBox vBox = new VBox();
        JFXTextField taskName = new JFXTextField(projectTask.getName());
        JFXTextField taskPriority = new JFXTextField(Integer.toString(projectTask.getPriority()));
        JFXTextArea taskDescription = new JFXTextArea(projectTask.getDetails());

        JFXDatePicker datePicker = new JFXDatePicker(localDate);
        datePicker.setEditable(false);

        ArrayList<CheckMenuItem> checkMenu = new ArrayList<>();
        ArrayList<String> userList = NetClient.getInstance().getClientAccountSyncHandler().getProject().getMembers();

        for(int i =0; i< userList.size(); i++){
            checkMenu.add(new CheckMenuItem(userList.get(i)));
            if(projectTask.getAssigneeIDs().contains(userList.get(i))){
                checkMenu.get(checkMenu.size() - 1).setSelected(true);
            }
            menu.getItems().add(checkMenu.get(i));
        }

        Button closeButton = new Button("Save Changes");
        closeButton.setOnAction(e -> overWriteData(projectTask, taskName,taskPriority, taskDescription, datePicker, window, checkMenu));

        vBox.getChildren().addAll(new MenuBar(menu), datePicker, taskName,taskPriority ,taskDescription, closeButton);
        vBox.setAlignment(Pos.TOP_LEFT);

        Scene scene = new Scene(vBox);
        window.setScene(scene);
        window.showAndWait();

    }

    public static void overWriteData(ProjectTask projectTask, TextField taskName,TextField taskPriority,  TextArea taskDescription, DatePicker datePicker, Stage window, ArrayList<CheckMenuItem> checkMenu){
        ArrayList<String> userList = new ArrayList<>();
        int priority = Integer.parseInt(taskPriority.getText());

        for(CheckMenuItem checkMenuItem : checkMenu){
            if(checkMenuItem.isSelected()){
                userList.add(checkMenuItem.getText());
            }
        }

        if(userList.size() > 0 && priority > 0){
            projectTask.setName(taskName.getText());
            projectTask.setDetails(taskDescription.getText());
            projectTask.setPriority(priority);
            projectTask.setDeadline(LocalDateTime.of(datePicker.getValue(), LocalTime.now()));
            projectTask.setAssigneeIDs(userList);
            window.close();
        }
    }

    public static Pane getRoot() throws IOException {
        Parent root = FXMLLoader.load(
                DayViewController.class.getResource("DayView.fxml"));
        return (Pane) root;
    }

    //Data model class
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
}
