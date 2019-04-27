package com.jigsaw.accounts;

import javafx.util.Pair;

import java.io.Serializable;

public class User implements Serializable {
    private String id;
    private String username;
    private Pair<String, String> passwordSaltPair;

    private Profile profile;

    public User(String id, String username, Pair<String, String> passwordSaltPair) {
        this.id = id;
        this.username = username;
        this.passwordSaltPair = passwordSaltPair;
    }

    public User(String id, String username, Pair<String, String> passwordSaltPair, Profile profile) {
        this.id = id;
        this.username = username;
        this.passwordSaltPair = passwordSaltPair;
        this.profile = profile;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public String getHashedPassword() {
        return passwordSaltPair.getKey();
    }

    public String getPasswordSalt() {
        return passwordSaltPair.getValue();
    }

    public Pair<String, String> getPasswordSaltPair() {
        return passwordSaltPair;
    }

    public void setPasswordSaltPair(Pair<String, String> passwordSaltPair) {
        this.passwordSaltPair = passwordSaltPair;
    }

    public void saveToFile() {
        Resource.getInstance().updateUser(this);
    }
}
