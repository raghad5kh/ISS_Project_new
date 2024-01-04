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
        PasswordHashing passwordHashing = new PasswordHashing();
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
        String hashedPassword = passwordHashing.hashPassword(password);
        System.out.println("Your password: " + password);
        System.out.println("hashedPassword: " + hashedPassword);
        out.println(hashedPassword);
        TCPClient.PS=password;
//        getClientDetails(out,clientIPAddress,clientPortNumber);
        if(studentDao.exist(id_number)){
        TCPClient.isEntered=true;}
        out.flush();
    }


    public  void signin_processCommonProfessorDetails(PrintWriter out, Scanner scanner) {
        ProfessorDao professorDao = new ProfessorDaoImpl();
        PasswordHashing passwordHashing = new PasswordHashing();

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
        String hashedPassword = passwordHashing.hashPassword(password);
        System.out.println("Your password: " + password);
        System.out.println("hashedPassword: " + hashedPassword);
        out.println(hashedPassword);
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
        PasswordHashing passwordHashing = new PasswordHashing();
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
        System.out.println("Enter your name");
        name = scanner.next();
        System.out.println("Your name: " + name);
        out.println(name);
        TCPClient.username=name;

        System.out.println("Enter your password");
        password = scanner.next();

        String hashedPassword = passwordHashing.hashPassword(password);
        System.out.println("Your password: " + password);
        System.out.println("hashedPassword: " + hashedPassword);
        out.println(hashedPassword);
//        getClientDetails(out,clientIPAddress,clientPortNumber);
        if(studentDao.exist_account(name,password)|| professorDao.exist_account(name,password)){
        TCPClient.isEntered=true;}
        out.flush();
    }


}
