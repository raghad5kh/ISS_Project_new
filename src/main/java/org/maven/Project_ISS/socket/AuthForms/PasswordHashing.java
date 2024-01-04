package org.maven.Project_ISS.socket.AuthForms;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordHashing {

    public  String hashPassword(String password) {
        // Hash a password using BCrypt
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public  boolean checkPassword(String candidate, String hashedPassword) {
        // Check if the entered password matches the stored hashed password
        return BCrypt.checkpw(candidate, hashedPassword);
    }

//    public static void main(String[] args) {
//        // Example usage
//        String originalPassword = "mySecretPassword";
//
//        // Hash the password before storing it
//        String hashedPassword = hashPassword(originalPassword);
//        System.out.println("Hashed Password: " + hashedPassword);
//
//        // Check if a provided password matches the stored hash
//        String candidatePassword = "mySecretPassword";
//        if (checkPassword(candidatePassword, hashedPassword)) {
//            System.out.println("Password is correct!");
//        } else {
//            System.out.println("Incorrect password.");
//        }
//    }
}
