package com.jigsaw.accounts;

import javafx.util.Pair;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * The Resource class provides an interface for storing
 * and retrieving User and Project data from files
 *
 * @author Raheeb Hassan
 */
public class Resource implements Serializable {
    /* maps from username to hashed password and salt */
    private Map <String, Pair<String, String>> userPassDictionary = new HashMap<>();

    // TODO: add project and team storage functionality

    // file paths
    public static final String serverStorageLocation = "storage/server/";
    public static final String saveFilePath = serverStorageLocation + "resource.save";
    public static final String usersDirPath = serverStorageLocation + "users/";

    private Resource() {}

    public boolean usernameExists(String username) {
        return userPassDictionary.containsKey(username);
    }

    public Pair<String, String> getUserPass(String username) {
        if (!userPassDictionary.containsKey(username)) return null;
        return userPassDictionary.get(username);
    }

    public void addUser(User user) {
        if (userPassDictionary.containsKey(user.getUsername())) {
            throw new IllegalArgumentException("User with same username already exists");
        }
        userPassDictionary.put(user.getUsername(), user.getPasswordSaltPair());
        updateUser(user);
        saveToFile();
    }

    public void updateUser(User user) {
        String fileName = user.getUsername();
        saveObjToFile(user, usersDirPath + fileName);
    }

    public User findUser(String username) {
        String fileName = username;
        return (User) loadObjFromFile(usersDirPath + fileName);
    }

    public static Resource loadFromFile() {
        File file = new File(saveFilePath);
        // create a new file if it doesn't exist
        if (!file.exists()) {
            // save an empty resource object
            Resource resource = new Resource();
            resource.saveToFile();
        }
        return (Resource) loadObjFromFile(saveFilePath);
    }

    public void saveToFile() {
        saveObjToFile(this, saveFilePath);
    }

    public static void saveObjToFile(Object obj, String filePath) {
        // create the enclosing folders if it doesn't exist
        File dir = new File(new File(filePath).getParentFile().getAbsolutePath());
        dir.mkdirs();
        try {
            FileOutputStream file = new FileOutputStream(filePath);
            ObjectOutputStream out = new ObjectOutputStream(file);
            out.writeObject(obj);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Object loadObjFromFile(String filePath) {
        try {
            FileInputStream file = new FileInputStream(filePath);
            ObjectInputStream in = new ObjectInputStream(file);
            Object obj = in.readObject();
            in.close();
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

//    public static void main(String[] args) {
//    }
}
