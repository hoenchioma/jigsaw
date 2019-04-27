package com.jigsaw.gui.calendar;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXButton;
import com.jigsaw.accounts.Project;
import com.jigsaw.accounts.User;
import com.jigsaw.calendar.ProjectTask;
import com.jigsaw.calendar.sync.TaskSyncHandler;
import com.jigsaw.network.client.NetClient;
import javafx.fxml.FXML;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.event.ActionEvent;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.layout.Pane;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;

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
    private Menu menuID;

    ArrayList<CheckMenuItem> checkMenu = new ArrayList<CheckMenuItem>();

    Map<String, User> userDictionary = NetClient.getInstance().getTaskSyncHandler().getUserDictionary();


    @FXML
    void addTaskButtonAction(ActionEvent event) {
        if(!creatorNameID.getText().isBlank() && !taskNameID.getText().isBlank() && !taskDescriptionID.getText().isBlank()){
            ArrayList<String> assignees = new ArrayList<String >();
            for(int i = 0; i < checkMenu.size(); i++){
                if(checkMenu.get(i).isSelected()){
                    assignees.add(checkMenu.get(i).getText());
                    System.out.println(assignees.get(assignees.size()-1));
                }
            }
            //////add task to project
            System.out.println("fictional task created");
           // ArrayList<ProjectTask> projectTask = NetClient.getInstance().getTaskSyncHandler().addTask(new ProjectTask(taskNameID.getText(), LocalDateTime.of(deadLineDatePickerID.getValue(), LocalTime.now()), creatorNameID.getText(), assignees ));


        }
    }

    public static Pane getRoot() throws IOException {
        Parent root = FXMLLoader.load(
                AddTaskViewController.class.getResource("AddTaskView.fxml"));
        return (Pane) root;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        /*userDictionary.put("samin", "ill");
        userDictionary.put("aahad", "lil");
        userDictionary.put("shamim", "!lil");*/
        int i = 0;
        for (Map.Entry<String, User> entry : userDictionary.entrySet()){
            checkMenu.add(new CheckMenuItem(entry.getKey()));
            menuID.getItems().add(checkMenu.get(i++));
        }

    }
}
