package com.jigsaw.gui;

import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import java.net.URL;
import java.lang.String;
import java.util.ArrayList;
import java.util.ResourceBundle;


//import com.jigsaw.accounts.Project;
//import com.jigsaw.accounts.User;
//import com.jigsaw.calendar.ProjectTask;
import com.jigsaw.accounts.Project;
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

import java.time.format.DateTimeFormatter;

public class CalendarViewController implements Initializable {

    // FIXME: This scene doesn't load

    @FXML private DatePicker datePicker;
    @FXML private AnchorPane anchorPaneID;
    private Project project = new Project();
    //DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    // LocalDate localDate = LocalDate.now();
    @FXML private JFXTreeTableView<CalendarEntry> treeView;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
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

        ObservableList<CalendarEntry> tasks = FXCollections.observableArrayList();
        for(int i = 0; i < project.getTaskList().size(); i++){
            if(project.getTaskList().get(i).getDeadline().toLocalDate() == datePicker.getValue()){
                tasks.add(new CalendarEntry(project.getTaskList().get(i)));
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

        public CalendarEntry(ProjectTask task) {
            this.taskName = new SimpleStringProperty(task.getName());
            this.description = new SimpleStringProperty(task.getDetails());

            ArrayList<User> memberList = task.getAssignees();
            StringBuilder memberName = new StringBuilder();
            memberName = new StringBuilder(memberList.get(0).getUsername());
            for(int i = 1; i<memberList.size(); i++){
                memberName.append(" ").append(memberList.get(i).getUsername());
            }

            this.members = new SimpleStringProperty(memberName.toString());
        }

    }

    private void log(String str) {
        System.out.println(this.getClass().getCanonicalName() + ": " + str);
    }
}