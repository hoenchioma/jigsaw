package com.jigsaw.gui.login;

import com.jfoenix.controls.JFXButton;
import com.jigsaw.accounts.Profile;
import com.jigsaw.gui.GUIUtil;
import com.jigsaw.network.client.NetClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.util.ResourceBundle;

/**
 * RegistrationViewController for Controlling Registration View
 *
 * @author Shadman Wadith
 * @version %I% %G%
 */
public class RegistrationViewController implements Initializable {

    @FXML
    public PasswordField password;
    private LocalDate todayDate;
    @FXML
    private RadioButton male;

    @FXML
    private RadioButton female;

    @FXML
    private RadioButton others;

    @FXML
    private DatePicker birthDay;

    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;

    @FXML
    private TextField profession;

    @FXML
    private TextField eMail;

    @FXML
    private TextField institute;

    @FXML
    private TextField contact;
    @FXML
    private PasswordField confirmPassword;
    @FXML
    private TextField username;
    @FXML
    private JFXButton register;
    @FXML
    private JFXButton signUpButton;


    /**
     * Initialize today's date
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        todayDate = LocalDate.now();
    }

    /**
     * Loads the SignUp page
     */
    @FXML
    private void backButtonAction(ActionEvent event) {
        try {
            GUIUtil.changeScene(LoginViewController.getRoot(), event);
        } catch (Exception sceneChangeException) {
            sceneChangeException.printStackTrace();
        }
    }

    /**
     * completes the registration process bu checking all information like
     * 1.User has filled the full form
     * 2.Confirms the password
     * 3.Checks the birthDay of he user
     * 4.Changes the scene of the stage
     */
    @FXML
    private void registerButtonAction(ActionEvent event) {
        Profile profile = new Profile();
        String usernameStr = "", passwordStr = "";


        if (username.getText().equals("")
                || firstName.getText().equals("")
                || lastName.getText().equals("")
                || password.getText().equals("")
                || eMail.getText().equals("")
                || profession.getText().equals("")
                || institute.getText().equals("")
                || contact.getText().equals("")) {
            System.out.println("Give all Info");
            GUIUtil.showError("Insufficient Information");
        } else if (password.getText().length() < 6) {
            GUIUtil.showError("Password too short");
        } else if (!password.getText().equals(confirmPassword.getText())) {
            GUIUtil.showError("Passwords didn't match");
            System.out.println("Passwords didn't match");
        } else if (todayDate.compareTo(birthDay.getValue()) < 0) {
            GUIUtil.showError("Hold it right there!\nWe don't accept people from the future");
        } else {
            try {
                usernameStr = username.getText();
                profile.setName(firstName.getText() + " " + lastName.getText());
                passwordStr = password.getText();
                profile.setEmail(eMail.getText());
                profile.setBirthDate(birthDay.getValue());
                profile.setProfession(profession.getText());
                profile.setInstitute(institute.getText());
                profile.setContactNumber(contact.getText());
                profile.setSex(getGender());

            } catch (Exception profileException) {
                profileException.printStackTrace();
            }

            try {
                NetClient.getInstance().connect();

                String response;

                try {
                    response = NetClient.getInstance().register(usernameStr, passwordStr, profile);
                } catch (Exception exp) {
                    exp.printStackTrace();
                    response = "error";
                }
                System.out.print(response);

                if (response.equals("success")) {
                    GUIUtil.showAlert("Registration Successful", Alert.AlertType.INFORMATION);
                }
                else GUIUtil.showError(response);

                try {
                    GUIUtil.changeScene(LoginViewController.getRoot(), event);
                } catch (Exception sceneChangeException) {
                    GUIUtil.showError("Error loading next scene");
                    sceneChangeException.printStackTrace();
                }
            } catch (UnknownHostException e) {
                GUIUtil.showError("Cannot find server");
            } catch (IOException e) {
                e.printStackTrace();
                GUIUtil.showError("Error finding or connecting to server");
            }

        }

    }

    /**
     * simple method to get the Gender from user
     *
     * @return gender Information
     */
    private String getGender() {
        String gender = "";

        if (male.isSelected()) {
            gender = "Male";
        } else if (female.isSelected()) {
            gender = "Female";
        } else {
            gender = "Others";
        }

        return gender;
    }

    /**
     * Returns the scene root (loading from fxml)
     * @return Pane type representing the scene root
     */
    public static ScrollPane getRoot() throws IOException {
        Parent root = FXMLLoader.load(
                RegistrationViewController.class.getResource("RegistrationView.fxml"));
        return (ScrollPane) root;
    }
}
