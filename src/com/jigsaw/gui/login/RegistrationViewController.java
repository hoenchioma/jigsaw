package com.jigsaw.gui.login;

import com.jfoenix.controls.JFXButton;
import com.jigsaw.accounts.Profile;
import com.jigsaw.network.client.NetClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
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
     *
     * @param arg0
     * @param arg1
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        todayDate = LocalDate.now();

    }

    /**
     * Loads the SignUp page
     *
     * @param event
     */
    @FXML
    void backButtonAction(ActionEvent event) {
        try {
            changeScene("LoginView.fxml", event);
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
     *
     * @param event
     */
    public void registerButtonAction(ActionEvent event) {
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
            showError("Insufficient Information");
        } else if (!password.getText().equals(confirmPassword.getText())) {
            showError("Password didn't match");
            System.out.println("Password didn't match ");
        } else if (todayDate.compareTo(birthDay.getValue()) < 0) {
            showError("Hold it right there!\nWe don't accept people from the future");
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
            String response = null;
            try {
                response = NetClient.getInstance().register(usernameStr, passwordStr, profile);
            } catch (Exception exp) {
                exp.printStackTrace();
            }
            System.out.println(response);
            System.out.println("Registration Done");
            try {
                changeScene("LoginView.fxml", event);
            } catch (Exception sceneChangeException) {
                sceneChangeException.printStackTrace();
            }
        }

    }

    /**
     * A function to change the current Scene of the window
     *
     * @param location
     * @param event
     * @throws IOException
     */

    public void changeScene(String location, ActionEvent event) throws IOException {
        Parent sceneView = FXMLLoader.load(getClass().getResource(location));
        Scene scene = new Scene(sceneView);
        Stage window = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /**
     * simple method to get the Gender from user
     *
     * @return gender Information
     */
    public String getGender() {
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
     * A method to show the Error Message in alert Box
     *
     * @param errorMessage
     */
    public void showError(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(errorMessage);
        alert.setTitle("JIGSAW");
        alert.show();
    }
}
