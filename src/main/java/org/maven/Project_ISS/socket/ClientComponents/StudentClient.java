package org.maven.Project_ISS.socket.ClientComponents;

import org.maven.Project_ISS.socket.TCPClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class StudentClient {
    public  void processStudent(PrintWriter out, Scanner scanner ,String answer,String clientIPAddress
            , int clientPortNumber) {
        try {
            commonDetails commonDetails = new commonDetails();
            System.out.println("Welcome Student:\n" +
                    "1. LogIn\n" +
                    "2. SignIn");

            answer = scanner.next();
            System.out.println("Entering " + answer);

            switch (answer){
                case "1":
                    System.out.println("LogIn Start...");
                    out.println("LogIn");
                    TCPClient.type=1;
                    commonDetails.processCommonUserDetails_login(out, scanner);
                    break;
                case "2":
                    System.out.println("SignIn Start...");
                    out.println("SignIn");
                    TCPClient.type=2;
                    commonDetails.signin_processCommonStudentDetails(out, scanner);

            }

//            global_variable.Request_Type = "SignIn";

            if(true) {

            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
   /* private final PrintWriter out;
    private final BufferedReader in;

    public StudentClient(PrintWriter out, BufferedReader in) {
        this.out = out;
        this.in = in;
    }

    public void processStudent() {
        try {
            System.out.println("Welcome Student:\n" +
                    "1. LogIn\n" +
                    "2. SignIn");

            String answer = in.readLine();
            System.out.println("Entering " + answer);

            switch (answer) {
                case "1":
                    System.out.println("LogIn Start...");
                    out.println("LogIn");
                    break;
                case "2":
                    System.out.println("SignIn Start...");
                    out.println("SignIn");
                    break;
                default:
                    System.out.println("Unexpected answer!");
                    break;
            }

            processCommonUserDetails();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void processCommonUserDetails() {
        try {
            String name;
            int password;

            System.out.println("Enter your name");
            name = in.readLine();
            System.out.println("Your name: " + name);
            out.println(name);

            System.out.println("Enter your password");
            password = Integer.parseInt(in.readLine());
            out.println(password);
            System.out.println("Your password: " + password);

            getClientDetails();

            out.flush();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void getClientDetails() {
        try {
            String clientIPAddress = in.readLine();
            int clientPortNumber = Integer.parseInt(in.readLine());
            System.out.println("Client IP Address: " + clientIPAddress + ", Port Number: " + clientPortNumber);
        } catch (Exception e) {
            System.out.println(e);
        }
    }*/
}