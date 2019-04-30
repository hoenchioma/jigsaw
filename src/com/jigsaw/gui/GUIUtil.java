package com.jigsaw.gui;

import javafx.event.Event;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class GUIUtil {
    /**
     * Change scene to next one
     * @param root Pane root of next scene
     * @param window window of application
     * @param resizability whether the window will be resizable
     */
    public static <T> void changeScene(T root, Stage window, boolean resizability) {
        Scene scene = new Scene((Parent) root);
        window.setScene(scene);
        window.show();
        window.setResizable(resizability);
        window.centerOnScreen();
    }

    public static void changeScene(Parent root, Stage window) {
        Scene scene = new Scene(root);
        window.setScene(scene);
        window.show();
        window.centerOnScreen();
    }

    public static <T> void changeScene(T root, Event event) {
        Scene scene = new Scene((Parent) root);
        Stage window = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /**
     * A method to show the Error Message in alert Box
     *
     * @param message String to show on popup
     * @param alertType alert type
     */
    public static void showAlert(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.setTitle("JIGSAW");
        alert.show();
    }

    public static void showError(String errorMessage) {
        showAlert(errorMessage, Alert.AlertType.ERROR);
    }
}
