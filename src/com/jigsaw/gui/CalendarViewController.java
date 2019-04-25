package com.jigsaw.gui;

import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
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
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.AnchorPane;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.TreeItem;
import javafx.util.Callback;


public class CalendarViewController implements Initializable {

    // FIXME: This scene doesn't load

    @FXML private DatePicker datePicker;
    @FXML private AnchorPane anchorPaneID;
    @FXML private JFXTreeTableView<CalendarEntry> treeView;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ////////////
        JFXTreeTableColumn<CalendarEntry, String> taskName = new JFXTreeTableColumn<>("Task Name");
        taskName.setPrefWidth(150);
        // taskName.setCellValueFactory(param -> param.getValue().getValue().taskName);
        taskName.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<CalendarEntry, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(JFXTreeTableColumn.CellDataFeatures<CalendarEntry, String> param) {
                return param.getValue().getValue().taskName;
            }
        });

        log("mew");

        JFXTreeTableColumn<CalendarEntry, String> description = new JFXTreeTableColumn<>("Description");
        taskName.setPrefWidth(150);
        // taskName.setCellValueFactory(param -> param.getValue().getValue().description);
        taskName.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<CalendarEntry, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<CalendarEntry, String> param) {
                return param.getValue().getValue().description;
            }
        });

        JFXTreeTableColumn<CalendarEntry, String> members = new JFXTreeTableColumn<>("Members");
        taskName.setPrefWidth(150);
        //taskName.setCellValueFactory(param -> param.getValue().getValue().members);
        taskName.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<CalendarEntry, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<CalendarEntry, String> param) {
                return param.getValue().getValue().members;
            }
        });

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

        ObservableList<CalendarEntry> tasks = FXCollections.observableArrayList();
        for(int i = 0; i < taskList.size(); i++){
            if(taskList.get(i).getDeadline().toLocalDate().equals(datePicker.getValue())) {
                //tasks.add(new CalendarEntry(taskList.get(i)));
               /* String name = taskList.get(i).getName();
                String des = taskList.get(i).getDetails();
                ArrayList<User> memberList = taskList.get(i).getAssignees();
                StringBuilder memberName = new StringBuilder(memberList.get(0).getUsername());
                for(int j = 1; i<memberList.size(); j++){
                    memberName.append(" ").append(memberList.get(j).getUsername());
                }
                tasks.add(new CalendarEntry(name, des, memberName.toString()));*/
            }
        }


        final TreeItem<CalendarEntry> root = new RecursiveTreeItem<CalendarEntry>(tasks, RecursiveTreeObject::getChildren);
        log(Boolean.toString(treeView == null));
        treeView.getColumns().setAll(taskName, members, description);
        treeView.setRoot(root);
        treeView.setShowRoot(false);
    }

    @FXML
    private void filter(ActionEvent event) {
    }

    class CalendarEntry extends RecursiveTreeObject<CalendarEntry> {
        StringProperty taskName;
        StringProperty members;
        StringProperty description;

        public CalendarEntry(String taskName, String description, String members) {
            this.taskName = new SimpleStringProperty(taskName);
            this.description = new SimpleStringProperty(description);
            this.members = new SimpleStringProperty(members);
            /*ArrayList<User> memberList = task.getAssignees();
            StringBuilder memberName = new StringBuilder();
            memberName = new StringBuilder(memberList.get(0).getUsername());
            for(int i = 1; i<memberList.size(); i++){
                memberName.append(" ").append(memberList.get(i).getUsername());
            }

            this.members = new SimpleStringProperty(memberName.toString());*/
        }

    }

    private void log(String str) {
        System.out.println(this.getClass().getCanonicalName() + ": " + str);
    }
}