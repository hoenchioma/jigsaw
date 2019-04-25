package com.jigsaw.gui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jigsaw.calendar.PriorityComparator;
import com.jigsaw.calendar.ProjectTask;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.util.Callback;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class KanbanViewController implements Initializable {
    @FXML private JFXListView<ProjectTask> willDoListView;
    @FXML private JFXListView<ProjectTask> doingListView;
    @FXML private JFXListView<ProjectTask> doneListView;

    private ObservableList<ProjectTask> willDoList = FXCollections.observableArrayList();
    private ObservableList<ProjectTask> doingList = FXCollections.observableArrayList();
    private ObservableList<ProjectTask> doneList = FXCollections.observableArrayList();

    static final int LIST_HEIGHT = 400;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        willDoListView.setItems(willDoList);
        doingListView.setItems(doingList);
        doneListView.setItems(doneList);

        willDoListView.setPrefHeight(LIST_HEIGHT);
        doingListView.setPrefHeight(LIST_HEIGHT);
        doneListView.setPrefHeight(LIST_HEIGHT);

        willDoList.addAll(
                new ProjectTask("Task 1", LocalDateTime.of(2099, 12, 12, 6, 6),
                        "Bablu", "R56", new ArrayList<>()),
                new ProjectTask("Task 2", LocalDateTime.of(2099, 12, 12, 6, 6),
                        "Bablu", "R56", new ArrayList<>()),
                new ProjectTask("Task 3", LocalDateTime.of(2099, 12, 12, 6, 6),
                        "Bablu", "R56", new ArrayList<>()),
                new ProjectTask("Task 4", LocalDateTime.of(2099, 12, 12, 6, 6),
                        "Bablu", "R56", new ArrayList<>())
        );

        willDoList.get(3).setPriority(3);

        FXCollections.sort(willDoList, new PriorityComparator());

        willDoListView.setCellFactory(new Callback<>() {
            @Override
            public ListCell<ProjectTask> call(ListView<ProjectTask> list) {
                return new ListCell<>() {
                    @Override
                    public void updateItem(ProjectTask item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty || item == null) {
                            setGraphic(null);
                        }
                        else {
                            Label label = new Label(item.getName());
                            JFXButton forwardButton = new JFXButton(">");

                            forwardButton.setOnAction((ActionEvent)->{
                                willDoList.remove(item);
                                doingList.add(item);
                                // sort the list added to
                                FXCollections.sort(doingList, new PriorityComparator());
                            });

                            GridPane grid = new GridPane();

                            grid.add(label, 0, 1);
                            grid.add(forwardButton, 1, 1);

                            grid.setPadding(new Insets(1, 10, 1, 10));

                            GridPane.setHgrow(label, Priority.ALWAYS);
                            GridPane.setHalignment(label, HPos.LEFT);
                            GridPane.setHalignment(forwardButton, HPos.RIGHT);

                            setGraphic(grid);
                        }
                    }
                };
            }
        });

        doingListView.setCellFactory(new Callback<>() {
            @Override
            public ListCell<ProjectTask> call(ListView<ProjectTask> list) {
                return new ListCell<>() {
                    @Override
                    public void updateItem(ProjectTask item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty || item == null) {
                            setGraphic(null);
                        }
                        else {
                            Label label = new Label(item.getName());

                            JFXButton prevButton = new JFXButton("<");

                            prevButton.setOnAction((ActionEvent)->{
                                doingList.remove(item);
                                willDoList.add(item);
                                // sort the list added to
                                FXCollections.sort(willDoList, new PriorityComparator());
                            });

                            JFXButton forwardButton = new JFXButton(">");

                            forwardButton.setOnAction((ActionEvent)->{
                                doingList.remove(item);
                                doneList.add(item);
                                // sort the list added to
                                FXCollections.sort(doneList, new PriorityComparator());
                            });

                            GridPane grid = new GridPane();

                            grid.add(prevButton, 0, 1);
                            grid.add(label, 1, 1);
                            grid.add(forwardButton, 2, 1);

                            grid.setPadding(new Insets(1, 10, 1, 10));

                            GridPane.setHgrow(label, Priority.ALWAYS);
                            GridPane.setHalignment(label, HPos.CENTER);

                            GridPane.setHalignment(prevButton, HPos.LEFT);
                            GridPane.setHalignment(forwardButton, HPos.RIGHT);

                            setGraphic(grid);
                        }
                    }
                };
            }
        });

        doneListView.setCellFactory(new Callback<>() {
            @Override
            public ListCell<ProjectTask> call(ListView<ProjectTask> list) {
                return new ListCell<>() {
                    @Override
                    public void updateItem(ProjectTask item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty || item == null) {
                            setGraphic(null);
                        }
                        else {
                            Label label = new Label(item.getName());

                            JFXButton prevButton = new JFXButton("<");

                            prevButton.setOnAction((ActionEvent)->{
                                doneList.remove(item);
                                doingList.add(item);
                                // sort the list added to
                                FXCollections.sort(doingList, new PriorityComparator());
                            });

                            GridPane grid = new GridPane();

                            grid.add(prevButton, 0, 1);
                            grid.add(label, 1, 1);

                            grid.setPadding(new Insets(1, 10, 1, 10));

                            GridPane.setHgrow(label, Priority.ALWAYS);
                            GridPane.setHalignment(label, HPos.RIGHT);

                            GridPane.setHalignment(prevButton, HPos.LEFT);

                            setGraphic(grid);
                        }
                    }
                };
            }
        });

    }
}
