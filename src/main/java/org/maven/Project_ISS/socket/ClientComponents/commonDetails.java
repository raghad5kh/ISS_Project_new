package org.maven.Project_ISS.socket.ClientComponents;

import org.maven.Project_ISS.AES.AsymmetricEncryption;
import org.maven.Project_ISS.dao.*;
import org.maven.Project_ISS.socket.AuthForms.PasswordHashing;
import org.maven.Project_ISS.socket.TCPClient;

import java.io.PrintWriter;
import java.util.Scanner;

public class commonDetails {
    public  void signin_processCommonStudentDetails(PrintWriter out, Scanner scanner) {

        StudentDao studentDao = new StudentDaoImpl();
        int id_number;
        String name;
        String password;
        System.out.println("Enter your id_number");
        id_number = Integer.parseInt(scanner.next());
        System.out.println("Your id_number: " + id_number);
        out.println(id_number);

        System.out.println("Enter your name");
        name = scanner.next();
        System.out.println("Your name: " + name);
        out.println(name);
        TCPClient.username=name;

        System.out.println("Enter your password");
        password = scanner.next();
        System.out.println("Your password: " + password);
        out.println(password);
        TCPClient.PS=password;
        if(studentDao.exist(id_number)){
        TCPClient.isEntered=true;}
        out.flush();
    }


    public  void signin_processCommonProfessorDetails(PrintWriter out, Scanner scanner) {
        ProfessorDao professorDao = new ProfessorDaoImpl();
        int id_number;
        String name;
        String password;
        System.out.println("Enter your id_number");
        id_number = Integer.parseInt(scanner.next());
        System.out.println("Your id_number: " + id_number);
        out.println(id_number);

        System.out.println("Enter your name");
        name = scanner.next();
        System.out.println("Your name: " + name);
        out.println(name);
        TCPClient.username=name;
        System.out.println("Enter your password");
        password = scanner.next();
        System.out.println("Your password: " + password);
        out.println(password);
        TCPClient.PS=password;
       if( professorDao.exist(id_number)) {
           TCPClient.isEntered = true;
       }
        out.flush();
    }

    public  void processCommonUserDetails_login(PrintWriter out, Scanner scanner) {

        String name;
        String password;
        StudentDao studentDao = new StudentDaoImpl();
        ProfessorDao professorDao = new ProfessorDaoImpl();
        System.out.println("Enter your name");
        name = scanner.next();
        System.out.println("Your name: " + name);
        out.println(name);
        TCPClient.username=name;
        System.out.println("Enter your password");
        password = scanner.next();
        System.out.println("Your password: " + password);
        out.println(password);
        String passwordFromDB_stu = studentDao.get_password(name);
        String passwordFromDB_pro = professorDao.get_password(name);
        PasswordHashing passwordHashing = new PasswordHashing();
        boolean check_student =  passwordHashing.checkPassword(password,passwordFromDB_stu);
        boolean check_professor =  passwordHashing.checkPassword(password,passwordFromDB_pro);
        if((studentDao.exist_account(name)&& check_student)|| (professorDao.exist_account(name)&&check_professor)){
        TCPClient.isEntered=true;}
        out.flush();
    }




}
