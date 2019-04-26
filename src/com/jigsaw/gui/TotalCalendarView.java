package com.jigsaw.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class TotalCalendarView {
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
        mainPane.getChildren().setAll(CalendarViewController.getRoot());
    }

    public void addTaskPress(ActionEvent event) throws IOException {
        mainPane.getChildren().setAll(AddTaskViewController.getRoot());
    }
}
