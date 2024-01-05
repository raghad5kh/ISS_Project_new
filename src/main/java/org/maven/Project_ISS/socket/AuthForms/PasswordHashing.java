package org.maven.Project_ISS.socket.AuthForms;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordHashing {

    public  String hashPassword(String password) {
        // Hash a password using BCrypt
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public  boolean checkPassword(String candidate, String hashedPassword) {
        // Check if the entered password matches the stored hashed password
        if(hashedPassword==null){
            return false;
        }
        return BCrypt.checkpw(candidate, hashedPassword);
    }


}
