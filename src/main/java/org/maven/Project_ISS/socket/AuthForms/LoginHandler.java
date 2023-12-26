package org.maven.Project_ISS.socket.AuthForms;


import org.maven.Project_ISS.dao.ProfessorDao;
import org.maven.Project_ISS.dao.StudentDao;
import org.maven.Project_ISS.socket.TCPClient;

import java.io.PrintWriter;

public class LoginHandler {
    private final PrintWriter out;
    private final StudentDao studentDao;
    private final ProfessorDao professorDao;

    public LoginHandler(PrintWriter out, StudentDao studentDao, ProfessorDao professorDao) {
        this.out = out;
        this.studentDao = studentDao;
        this.professorDao = professorDao;
    }

    public void handleLogin(String name, String password) {
        TCPClient.username=name;
        System.out.println("Client logged in: " + name);

        boolean isStudentExist = studentDao.exist_account(name,password);
        boolean isProfExist = professorDao.exist_account(name,password);

        if (isStudentExist || isProfExist) {
            out.println("Welcome to our System, " + name);
        } else {
            System.out.println("Client: Failed to log in with name: " + name +
                    ". This name doesn't exist in our records.");
            out.println("Sorry! This name {" + name + "} doesn't exist in our records");
        }
    }
}
