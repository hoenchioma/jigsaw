package com.jigsaw.gui.calendar;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListCell;
import com.jfoenix.controls.JFXListView;
import com.jigsaw.accounts.Project;
import com.jigsaw.calendar.PriorityComparator;
import com.jigsaw.calendar.Progress;
import com.jigsaw.calendar.ProjectTask;
import com.jigsaw.calendar.TaskManager;
import com.jigsaw.calendar.sync.ClientTaskSyncHandler;
import com.jigsaw.network.client.NetClient;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.util.Callback;

import java.io.IOException;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ClientTaskSyncHandler clientTaskSyncHandler = NetClient.getInstance().getClientTaskSyncHandler();
        resetItems();

        willDoListView.setItems(willDoList);
        doingListView.setItems(doingList);
        doneListView.setItems(doneList);

        expandListView(willDoListView);
        expandListView(doingListView);
        expandListView(doneListView);

//        willDoList.addAll(
//                new ProjectTask("Task 1", LocalDateTime.of(2099, 12, 12, 6, 6),
//                        "Raheeb", "R56", new ArrayList<>()),
//                new ProjectTask("Task 2", LocalDateTime.of(2099, 12, 12, 6, 6),
//                        "Farhan", "R56", new ArrayList<>()),
//                new ProjectTask("Task 3", LocalDateTime.of(2099, 12, 12, 6, 6),
//                        "Samin", "R56", new ArrayList<>()),
//                new ProjectTask("Task 4", LocalDateTime.of(2099, 12, 12, 6, 6),
//                        "Shamim", "R56", new ArrayList<>())
//        );
//
//        willDoList.get(3).setPriority(3);
//        willDoList.get(0).setPriority(2);

//        FXCollections.sort(willDoList, new PriorityComparator());

//        willDoListView.setPrefHeight();

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
                                item.setProgress(Progress.doing);
                                doingList.add(item);
                                clientTaskSyncHandler.updateTask(item);

                                recalculateListSize(willDoListView, getHeight());
                                recalculateListSize(doingListView, getHeight());

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
                                item.setProgress(Progress.willdo);
                                willDoList.add(item);
                                clientTaskSyncHandler.updateTask(item);

                                recalculateListSize(doingListView, getHeight());
                                recalculateListSize(willDoListView, getHeight());

                                // sort the list added to
                                FXCollections.sort(willDoList, new PriorityComparator());
                            });

                            JFXButton forwardButton = new JFXButton(">");

                            forwardButton.setOnAction((ActionEvent)->{
                                doingList.remove(item);
                                item.setProgress(Progress.done);
                                doneList.add(item);
                                clientTaskSyncHandler.updateTask(item);

                                recalculateListSize(doingListView, getHeight());
                                recalculateListSize(doneListView, getHeight());

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
                                item.setProgress(Progress.doing);
                                doingList.add(item);
                                clientTaskSyncHandler.updateTask(item);

                                recalculateListSize(doneListView, getHeight());
                                recalculateListSize(doingListView, getHeight());

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

    private void resetItems() {
        ClientTaskSyncHandler clientTaskSyncHandler = NetClient.getInstance().getClientTaskSyncHandler();
        ArrayList<ProjectTask> projectTasks = clientTaskSyncHandler.getTaskManager().getProjectTasks();

        willDoList.clear();
        doingList.clear();
        doneList.clear();

        for (ProjectTask task: projectTasks) {
            switch (task.getProgress()) {
                case willdo:
                    willDoList.add(task);
                    break;
                case doing:
                    doingList.add(task);
                    break;
                case done:
                    doneList.add(task);
                    break;
            }
        }

        FXCollections.sort(willDoList, new PriorityComparator());
        FXCollections.sort(doingList, new PriorityComparator());
        FXCollections.sort(doneList, new PriorityComparator());
    }

    private void recalculateListSize(JFXListView listView, double cellHeight) {
        listView.setPrefHeight(cellHeight * listView.getItems().size() + 10);
    }

    private void expandListView(JFXListView listView) {
        listView.depthProperty().set(1);
        listView.setExpanded(true);
    }

    public static Pane getRoot() throws IOException {
        Parent root = FXMLLoader.load(
                KanbanViewController.class.getResource("KanbanView.fxml"));
        return (Pane) root;
    }
}
