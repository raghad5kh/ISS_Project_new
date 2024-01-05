package org.maven.Project_ISS.socket.ClientComponents;

import org.maven.Project_ISS.socket.TCPClient;

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


            if(true) {

            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}