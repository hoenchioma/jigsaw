package com.jigsaw;

import com.jigsaw.gui.login.LoginViewController;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * This is the main point of access of the application
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = LoginViewController.getRoot();
        primaryStage.setTitle("Jigsaw");
        primaryStage.setScene(new Scene(root));
        primaryStage.getIcons().add(new Image("/com/jigsaw/gui/resources/JIGSAW-ICON.png"));
        primaryStage.show();
        primaryStage.setResizable(false);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
