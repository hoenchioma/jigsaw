package com.jigsaw.network.server;

import com.jigsaw.accounts.Profile;
import com.jigsaw.accounts.User;
import com.jigsaw.network.Packet;
import com.jigsaw.network.server.ClientHandler;
import com.jigsaw.network.server.Server;
import javafx.util.Pair;

import java.io.*;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
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
            String command = in.readUTF();
            if (command.equals("login")) {
                handleLogin();
            } else if (command.equals("register")) {
                handleRegister();
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
        String username = in.readUTF();
        String password = in.readUTF();

        // TODO: Implement project loading on login
        log("mello");

        if (!server.getResource().usernameExists(username)) {
            out.writeUTF("username not found");
            log("username not found");
        }
        else {
            Pair<String, String> passwordSaltPair = server.getResource().getUserPass(username);
            String storedPasswordHash = passwordSaltPair.getKey();
            String salt = passwordSaltPair.getValue();

            // hash the given password
            String hashedPassword = get_SHA_256_SecurePassword(password, salt);

            if (hashedPassword.equals(storedPasswordHash)) {
                out.writeUTF("success");
                log("login successful");

                // load user from file
                User user = server.getResource().findUser(username);
                // generate a random sessionID
                String sessionID = UUID.randomUUID().toString();

                initiateSession(user);
            }
            else {
                out.writeUTF("password wrong");
                log("Passwords don't match");
            }
        }
    }

    /**
     * handles the communications with client for accomplishing registration
     * of new User
     */
    private void handleRegister() throws Exception{
        String username = in.readUTF();
        String password = in.readUTF();
        Profile profile = (Profile) in.readObject();

        if (server.getResource().usernameExists(username)) {
            out.writeUTF("username already exists");
            log("username already exists");
        } else {
            out.writeUTF("success");

            String salt = getSalt();
            String hashedPassword = get_SHA_256_SecurePassword(password, salt);
            Pair<String, String> passwordSaltPair = new Pair<>(hashedPassword, salt);
            String userID = UUID.randomUUID().toString();

            User user = new User(userID, username, passwordSaltPair, profile);
            server.getResource().addUser(user);

            initiateSession(user);
        }
    }

    private void initiateSession(User user) throws IOException {
        // generate a random sessionID
        String sessionID = UUID.randomUUID().toString();

        // send the session ID to client
        out.writeUTF(sessionID);

        ClientHandler handler = new ClientHandler(out, in, user, sessionID);
        // add the handler to the server
        server.addHandler(handler);
        // add user to resource class
        server.getResource().activateUser(user);

        new Thread(handler).start();
    }

    /**
     * Get SHA1 hashed password using given salt
     * @param passwordToHash password to be hashed
     * @param saltInBytes extra random bytes added to the hash to make it more secure (Base64 encoded)
     * @return secure hashed password
     */
    private static String get_SHA_256_SecurePassword(String passwordToHash, String saltInBytes) {
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
    private static String getSalt() {
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

    private void log(String str) {
        System.out.println(this.getClass().getCanonicalName() + ": " + str);
    }

//    public static void main(String[] args) {
//        Resource resource = Resource.loadFromFile();
//        String salt = ServerLoginHandler.getSalt();
//        String hashPass = ServerLoginHandler.get_SHA_256_SecurePassword("palu", salt);
//        resource.addUser(new User("id", "malu", new Pair<>(hashPass, salt)));
//    }
}
