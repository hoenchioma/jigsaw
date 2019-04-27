package com.jigsaw.network.server;

import com.jigsaw.accounts.Profile;
import com.jigsaw.accounts.Project;
import com.jigsaw.accounts.Resource;
import com.jigsaw.accounts.User;
import javafx.util.Pair;

import java.io.*;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Random;
import java.util.UUID;

/**
 * Handles the necessary communication for verification of login
 * or registration of a new user
 *
 * After successful login it passes control to Client Handler
 *
 * @author Raheeb Hassan
 */
public class ServerLoginHandler implements Runnable {
    private Server server;
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public ServerLoginHandler(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            log("Failed to read socket");
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            String command = (String) in.readObject();
            if (command.equals("login")) {
                log("login request accepted");
                handleLogin();
            } else if (command.equals("register")) {
                log("register request accepted");
                handleRegister();
            } else if (command.equals("create project")) {
                log("create project request accepted");
                handleCreateProject();
            } else {
                log("Invalid command from client");
                throw new IllegalArgumentException();
            }
        } catch (Exception e) {
            log("Login failed");
            e.printStackTrace();
        }
    }

    /**
     * handles the communications with client for accomplishing login
     */
    private void handleLogin() throws Exception {
        String username = (String) in.readObject();
        String password = (String) in.readObject();
        String projectID = (String) in.readObject();

        log("username: " + username);

        if (!server.getResource().projectIDExists(projectID)) {
            out.writeObject("projectID not found");
            log("projectID not found");
        }
        else if (!server.getResource().usernameExists(username)) {
            out.writeObject("username not found");
            log("username not found");
        }
        else if (server.getActiveConnections().containsKey(username)) {
            out.writeObject("user already online");
            log("user already online");
        }
        else {
            Pair<String, String> passwordSaltPair = server.getResource().getUserPass(username);
            String storedPasswordHash = passwordSaltPair.getKey();
            String salt = passwordSaltPair.getValue();

            // hash the given password
            String hashedPassword = get_SHA_256_SecurePassword(password, salt);

            if (hashedPassword.equals(storedPasswordHash)) {
                out.writeObject("success");
                log("login successful");

                // load user from file
                User user = server.getResource().findUser(username);
                // load profile from file
                Project project = server.getResource().findProject(projectID);
                project.addMemberIfAbsent(user);
                project.saveToFile();

                initiateSession(user, project);
            }
            else {
                out.writeObject("password wrong");
                log("Passwords don't match");
            }
        }
    }

    /**
     * handles the communications with client for accomplishing registration
     * of new User
     */
    private void handleRegister() throws Exception{
        String username = (String) in.readObject();
        String password = (String) in.readObject();
        Profile profile = (Profile) in.readObject();

        log("username: " + username);

        if (server.getResource().usernameExists(username)) {
            out.writeObject("username already exists");
            log("username already exists");
        } else {
            out.writeObject("success");

            String salt = getSalt();
            String hashedPassword = get_SHA_256_SecurePassword(password, salt);
            Pair<String, String> passwordSaltPair = new Pair<>(hashedPassword, salt);
            String userID = UUID.randomUUID().toString();

            User user = new User(userID, username, passwordSaltPair, profile);
            server.getResource().addUser(user);
        }
    }

    /**
     * handles communication with client for creating a project
     */
    private void handleCreateProject() {
        try {
            String projectName = (String) in.readObject();
            String projectDescription = (String) in.readObject();
            LocalDate projectDeadline = (LocalDate) in.readObject();

            out.writeObject("success");

            // generate a random human readable project ID
            String projectID;
            do projectID = generateRandomID(Resource.PROJECT_ID_LENGTH);
            while (server.getResource().projectIDExists(projectID));

            Project project = new Project(projectID, projectName, projectDescription, projectDeadline);
            server.getResource().addProject(project);

            log("project with projectID " + projectID + " created");

            out.writeObject(projectID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initiateSession(User user, Project project) throws IOException {
        // generate a random sessionID
        String sessionID = UUID.randomUUID().toString();

        // send the session ID to client
        out.writeObject(sessionID);

        ClientHandler handler = new ClientHandler(server, out, in, user, project, sessionID);
        // add the handler to the server
        server.addHandler(user.getUsername(), handler);
        // add user to resource class
        server.getResource().activateUser(user);

        new Thread(handler, user.getUsername() + " user thread").start();
    }

    /**
     * Get SHA1 hashed password using given salt
     * @param passwordToHash password to be hashed
     * @param saltInBytes extra random bytes added to the hash to make it more secure (Base64 encoded)
     * @return secure hashed password
     */
    public static String get_SHA_256_SecurePassword(String passwordToHash, String saltInBytes) {
        byte[] salt = Base64.getDecoder().decode(saltInBytes);
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] bytes = md.digest(passwordToHash.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    /**
     * Gives us a random salt for hashing
     * @return a Base64 encoded byte array of random bits (salt)
     */
    public static String getSalt() {
        try {
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            byte[] salt = new byte[16];
            sr.nextBytes(salt);
            return Base64.getEncoder().encodeToString(salt);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * generate a random human-readable id
     * @return a string with the random id
     */
    private static String generateRandomID(int length) {
        Random rng = new Random();

        final int probabilityOfSpecialRandom = 9929;
        if (rng.nextInt()%probabilityOfSpecialRandom == 0) {
            return Resource.secretProjectIDs.get(rng.nextInt(Resource.secretProjectIDs.size()));
        }

        String alphabet = "1234567890abcdefghijklmnopqrstuvwxyz";
        StringBuilder temp = new StringBuilder();
        for (int i = 0; i < length; i++) {
            temp.append(alphabet.charAt(rng.nextInt(alphabet.length())));
        }
        return temp.toString();
    }

    private void log(String str) {
        System.out.println(this.getClass().getCanonicalName() + ": " + str);
    }

//    public static void main(String[] args) {
//        byte[] bytes = String.valueOf(System.currentTimeMillis()).getBytes();
//        String s = Base64.getEncoder().encodeToString(bytes);
//        System.out.println(s);
//    }
}
