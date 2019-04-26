package com.jigsaw.accounts;

import javafx.util.Pair;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * The singleton class which provides an interface for storing
 * and retrieving User and Project data from files
 *
 * @author Raheeb Hassan
 */
public class Resource implements Serializable {
    // singleton instance of resource class
    private static Resource singleInstance;

    /**
     * Method to get the singleton instance of Resource class
     * @return the singleton instance of the class
     */
    public static Resource getInstance() {
        if (singleInstance == null) {
            // lazy initialization of Resource class
            singleInstance = Resource.loadFromFile();
        }
        return singleInstance;
    }

    // file paths
    public static final String serverStorageLocation = "storage/server/";
    public static final String saveFilePath = serverStorageLocation + "resource.save";
    public static final String usersDirPath = serverStorageLocation + "users/";
    public static final String projectsDirPath = serverStorageLocation + "projects/";

    /* maps from username to hashed password and salt */
    private Map <String, Pair<String, String>> userPassDictionary = new HashMap<>();
    /* Users that have already been loaded */
    transient private Map<String, User> activeUsers = new HashMap<>();

    /* a set of strings containing project IDs of existing projects */
    private Set<String> existingProjects = new HashSet<>();
    /* Projects that have already been loaded */
    transient private Map<String, Project> activeProjects = new HashMap<>();


    // private constructor so it can't be instantiated
    private Resource() {}

    // user related methods

    public boolean usernameExists(String username) {
        return userPassDictionary.containsKey(username);
    }

    public Pair<String, String> getUserPass(String username) {
        if (!userPassDictionary.containsKey(username)) return null;
        return userPassDictionary.get(username);
    }

    public void activateUser(User user) {
        activeUsers.putIfAbsent(user.getUsername(), user);
    }

    public void deactivateUser(User user) {
        activeUsers.remove(user.getUsername());
    }

    public void deactivateUser(String username) {
        activeUsers.remove(username);
    }

    public void addUser(User user) {
        if (usernameExists(user.getUsername())) {
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
        // if user is already active return loaded object
        if (activeUsers.containsKey(username)) return activeUsers.get(username);
        String fileName = username;
        return (User) loadObjFromFile(usersDirPath + fileName);
    }

    // project related methods

    public boolean projectIDExists(String projectID) {
        return existingProjects.contains(projectID);
    }

    public void addProject(Project project) {
        if (projectIDExists(project.getId())) {
            throw new IllegalArgumentException("Project with same project ID already exists");
        }
        existingProjects.add(project.getId());
        updateProject(project);
        saveToFile();
    }

    public void updateProject(Project project) {
        String fileName = project.getId();
        saveObjToFile(project, projectsDirPath + fileName);
    }

    public Project findProject(String projectID) {
        if (activeProjects.containsKey(projectID)) return activeProjects.get(projectID);
        String fileName = projectID;
        return (Project) loadObjFromFile(projectsDirPath + fileName);
    }

    public void activateProject(Project project) {
        activeProjects.putIfAbsent(project.getId(), project);
    }

    /**
     * Get a dictionary of all the project member info
     * @return a map from a username String to a User object
     */
    public Map<String, User> getProjectUsers(String projectID) {
        Project project = findProject(projectID);
        Map <String, User> userDic = new HashMap<>();
        for (String member: project.getMembers()) {
            userDic.put(member, findUser(member));
        }
        return userDic;
    }

    // file related methods

    /**
     * load an instance of Resource from file
     * @return a loaded Resource object
     */
    public static Resource loadFromFile() {
        File file = new File(saveFilePath);
        // create a new file if it doesn't exist
        if (!file.exists()) {
            // save an empty resource object
            Resource resource = new Resource();
            resource.saveToFile();
        }
        Resource resource = (Resource) loadObjFromFile(saveFilePath);
        resource.activeUsers = new HashMap<>();
        resource.activeProjects = new HashMap<>();
        return resource;
    }

    /**
     * save this instance of Resource to file
     */
    synchronized public void saveToFile() {
        saveObjToFile(this, saveFilePath);
    }

    /**
     * saves Object as binary data to file
     * @param obj object to be saved
     * @param filePath path of save file
     */
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

    /**
     * Load an object from file
     * @param filePath path of the file to be loaded
     * @return the loaded object
     */
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
