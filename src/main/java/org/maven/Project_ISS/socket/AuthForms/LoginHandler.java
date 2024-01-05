package org.maven.Project_ISS.socket.AuthForms;


import org.maven.Project_ISS.dao.ProfessorDao;
import org.maven.Project_ISS.dao.StudentDao;
import org.maven.Project_ISS.socket.ServerClientThread;
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
        String isLogged;
        System.out.println("Client logged in: " + name);

        boolean isStudentExist = studentDao.exist_account(name);
        boolean isProfExist = professorDao.exist_account(name);
        String passwordFromDB_stu = studentDao.get_password(name);
        String passwordFromDB_pro = professorDao.get_password(name);
        System.out.println(passwordFromDB_pro);
        PasswordHashing passwordHashing = new PasswordHashing();
        boolean check_student =  passwordHashing.checkPassword(password,passwordFromDB_stu);
        boolean check_professor =  passwordHashing.checkPassword(password,passwordFromDB_pro);

        if (isStudentExist && check_student ) {
            ServerClientThread.client_type=2;
            TCPClient.isEntered=true;
            isLogged="true";
//            out.println(isLogged);
            out.println("Welcome to our System, " + name);}
        else if(isProfExist && check_professor){
            TCPClient.isEntered=true;
            isLogged="true";
            ServerClientThread.client_type=1;
//            out.println(isLogged);
            out.println("Welcome to our System, " + name);
        }
        else {
            isLogged="false";
            TCPClient.isEntered=false;
            System.out.println("Client: Failed to log in with name: " + name +
                    ". This name doesn't exist in our records.");
//            out.println(isLogged);
            out.println("Sorry! incorrect NAME {" + name + "} or incorrect PASSWORD");

        }
    }
}