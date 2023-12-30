package org.maven.Project_ISS.socket.ClientComponents;

import org.maven.Project_ISS.AES.AsymmetricEncryption;
import org.maven.Project_ISS.dao.*;
import org.maven.Project_ISS.socket.TCPClient;

import java.io.PrintWriter;
import java.util.Scanner;

public class commonDetails {
    public  void signin_processCommonStudentDetails(PrintWriter out, Scanner scanner,String clientIPAddress,int clientPortNumber) {

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
        out.println(password);
        System.out.println("Your password: " + password);
        TCPClient.PS=password;
        getClientDetails(out,clientIPAddress,clientPortNumber);
        TCPClient.isEntered=true;
        out.flush();
    }


    public  void signin_processCommonProfessorDetails(PrintWriter out, Scanner scanner,String clientIPAddress,int clientPortNumber) {
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
        out.println(password);
        System.out.println("Your password: " + password);

        TCPClient.PS=password;
        getClientDetails(out,clientIPAddress,clientPortNumber);
        TCPClient.isEntered=true;

        out.flush();
    }
    private static void getClientDetails(PrintWriter out , String clientIPAddress,int clientPortNumber ) {
        out.println(clientIPAddress);
        out.println(clientPortNumber);
    }
    public  void processCommonUserDetails_login(PrintWriter out, Scanner scanner,String clientIPAddress,int clientPortNumber) {

        String name;
        String password;

        System.out.println("Enter your name");
        name = scanner.next();
        System.out.println("Your name: " + name);
        out.println(name);
        TCPClient.username=name;

        System.out.println("Enter your password");
        password = scanner.next();
        out.println(password);
        System.out.println("Your password: " + password);
        getClientDetails(out,clientIPAddress,clientPortNumber);
        TCPClient.isEntered=true;
        out.flush();
    }


}
