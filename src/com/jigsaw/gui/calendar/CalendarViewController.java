package com.jigsaw.gui.calendar;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class CalendarViewController {
    @FXML private AnchorPane mainPane;

    @FXML
    private void initialize() throws IOException {
        kanbanPress(new ActionEvent());
    }

    @FXML
    public void kanbanPress(ActionEvent event) throws IOException {
        mainPane.getChildren().setAll(KanbanViewController.getRoot());
    }

    public void dayViewPress(ActionEvent event) throws IOException {
        mainPane.getChildren().setAll(DayViewController.getRoot());
    }

    public void addTaskPress(ActionEvent event) throws IOException {
        mainPane.getChildren().setAll(AddTaskViewController.getRoot());
    }

    public static Pane getRoot() throws IOException {
        Parent root = FXMLLoader.load(CalendarViewController.class.getResource("CalendarView.fxml"));
        return (Pane) root;
    }
}
