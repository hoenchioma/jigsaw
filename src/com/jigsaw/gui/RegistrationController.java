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
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

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

    public void registerButtonAction(ActionEvent e) {

        System.out.println("Registration Done");
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
                profile.setName(firstName.getText()+ " " + lastName.getText());
                passwordStr = password.getText();
                profile.setEmail(eMail.getText());
                profile.setBirthDate(birthDay.getValue());
                profile.setProfession(profession.getText());
                profile.setInstitute(institute.getText());
                profile.setContactNumber(contact.getText());
                profile.setSex(getGender());

            } catch(Exception profileException) {
                System.out.println(profileException);
            }
        }
        String response = null;
        try {
            response = NetClient.getInstance().register(usernameStr, passwordStr, profile);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        System.out.println(response);
    }

    public String getGender()
    {
        String gender="";

        if(male.isSelected()) {
            gender="Male";
        }
        else if(female.isSelected()) {
            gender="Female";
        }
        else {
            gender="Others";
        }

        return gender;

    }

}
