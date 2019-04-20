package com.jigsaw.network;

import com.jigsaw.accounts.User;
import javafx.util.Pair;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
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
    private DataInputStream in;
    private DataOutputStream out;

    public ServerLoginHandler(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            log("Login failed");
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

        if (server.getResource().usernameExists(username)) {
            out.writeUTF("username not found");
            log("username not found");
        }
        else {
            Pair<String, String> passwordSaltPair = server.getResource().getUserPass(username);
            String storedPasswordHash = passwordSaltPair.getKey();
            byte[] salt = passwordSaltPair.getValue().getBytes();

            // hash the given password
            String hashedPassword = get_SHA_256_SecurePassword(password, salt);

            if (hashedPassword.equals(storedPasswordHash)) {
                out.writeUTF("success");
                log("login successful");

                // load user from file
                User user = server.getResource().findUser(username);
                // generate a random sessionID
                String sessionID = UUID.randomUUID().toString();

                ClientHandler handler = new ClientHandler(socket, user, sessionID);
                // add the handler to the server
                server.addHandler(handler);

                new Thread(handler).start();
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
    private void handleRegister() {
        // TODO: implement registration handler
    }

    /**
     * Get SHA1 hashed password using given salt
     * @param passwordToHash password to be hashed
     * @param salt extra random bytes added to the hash to make it more secure
     * @return secure hashed password
     */
    private static String get_SHA_256_SecurePassword(String passwordToHash, byte[] salt) {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] bytes = md.digest(passwordToHash.getBytes());
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
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
     * @return a byte array containing our string
     */
    private static byte[] getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }

    private void log(String str) {
        System.out.println(this.getClass().getCanonicalName() + ": " + str);
    }
}
