/*
 * RegistrationController for Controlling Registration
 *
 * JAVA 11.0.2
 *
 * @author Shadman Wadith
 */
package com.jigsaw.gui;

import com.jfoenix.controls.JFXButton;
import com.jigsaw.accounts.Profile;
import com.jigsaw.network.client.NetClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class RegistrationController implements Initializable  {

    @Override
    public void initialize(URL arg0, ResourceBundle arg1){

    }
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
    public PasswordField password;

    @FXML
    private PasswordField confirmPassword;

    @FXML
    private TextField username;
    @FXML
    private JFXButton register;
    @FXML
    private JFXButton signUpButton;

    @FXML
    void backButtonAction(ActionEvent event) {
        try{
            changeScene("LoginView.fxml",event);
        }
        catch (Exception sceneChangeException){
            sceneChangeException.printStackTrace();
        }
    }

    public void registerButtonAction(ActionEvent event) {
        Profile profile = new Profile();
        String usernameStr = "", passwordStr = "";
        /**
         * if all textFields are not filled
          */
         
        if (username.getText().equals("")
                || firstName.getText().equals("")
                || lastName.getText().equals("")
                || password.getText().equals("")
                || eMail.getText().equals("")
                ||profession.getText().equals("")
                || institute.getText().equals("")
                || contact.getText().equals("")) {
            System.out.println("Give all Info");
        }
        else if (!password.getText().equals(confirmPassword.getText())){
            System.out.println("Password didn't match ");
        }
        else {
            try{
                usernameStr = username.getText();
                profile.setName(firstName.getText() + " " + lastName.getText());
                passwordStr = password.getText();
                profile.setEmail(eMail.getText());
                profile.setBirthDate(birthDay.getValue());
                profile.setProfession(profession.getText());
                profile.setInstitute(institute.getText());
                profile.setContactNumber(contact.getText());
                profile.setSex(getGender());

            } catch(Exception profileException) {
                profileException.printStackTrace();
            }
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
        } catch (Exception sceneChangeException){
            sceneChangeException.printStackTrace();
        }
    }

    public void changeScene(String  location, ActionEvent event)throws IOException {
        Parent sceneView = FXMLLoader.load(getClass().getResource(location));
        Scene scene = new Scene(sceneView);
        Stage window = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    public String getGender() {
        String gender="";

        if(male.isSelected()) {
            gender = "Male";
        } else if(female.isSelected()) {
            gender="Female";
        } else {
            gender="Others";
        }

        return gender;
    }

}
