/*
 * Profile
 *
 * JAVA 11.0.2
 *
 * @author Shadman Wadith
 */

package com.jigsaw.accounts;


import java.io.Serializable;
import java.time.LocalDate;

public class Profile implements Serializable {
    private String name;
    private String email;
    private String userID;
    private String profession;
    private String institute;
    private String sex;
    private String contactNumber;
    private LocalDate birthDate;

    public Profile() {}

    public Profile(String name, String email, String userID, String profession, String institute, String sex, String contactNumber, LocalDate birthDate) {
        this.name = name;
        this.email = email;
        this.userID = userID;
        this.profession = profession;
        this.institute = institute;
        this.sex = sex;
        this.contactNumber = contactNumber;
        this.birthDate = birthDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getInstitute() {
        return institute;
    }

    public void setInstitute(String institute) {
        this.institute = institute;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
}
