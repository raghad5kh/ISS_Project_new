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
//        System.out.println("Enter your IP Address");
//        String ip_address = scanner.next();
//        System.out.println("Your ip address: " + ip_address);
//        out.println(ip_address);
//        TCPClient.clientIPAddress=ip_address;
//        System.out.println("Enter your port number");
//        int port_number = Integer.parseInt(scanner.next());
//        System.out.println("Your port number: " + port_number);
//        out.println(port_number);
//        TCPClient.clientPortNumber=port_number;
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
//        getClientDetails(out,clientIPAddress,clientPortNumber);
        if(studentDao.exist(id_number)){
        TCPClient.isEntered=true;}
        out.flush();
    }


    public  void signin_processCommonProfessorDetails(PrintWriter out, Scanner scanner) {
        ProfessorDao professorDao = new ProfessorDaoImpl();


        int id_number;
        String name;
        String password;
//        System.out.println("Enter your IP Address");
//        String ip_address = scanner.next();
//        System.out.println("Your ip address: " + ip_address);
//        out.println(ip_address);
//        TCPClient.clientIPAddress=ip_address;
//        System.out.println("Enter your port number");
//        int port_number = Integer.parseInt(scanner.next());
//        System.out.println("Your port number: " + port_number);
//        out.println(port_number);
//        TCPClient.clientPortNumber=port_number;
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
//        getClientDetails(out,clientIPAddress,clientPortNumber);
       if( professorDao.exist(id_number)) {
           TCPClient.isEntered = true;
       }
        out.flush();
    }

    private static void getClientDetails(PrintWriter out , String clientIPAddress,int clientPortNumber ) {
        out.println(clientIPAddress);
        out.println(clientPortNumber);
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
//        getClientDetails(out,clientIPAddress,clientPortNumber);
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
