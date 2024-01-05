package org.maven.Project_ISS.socket.ClientComponents;
import org.maven.Project_ISS.socket.TCPClient;

import java.io.BufferedReader;

import java.io.PrintWriter;
import java.util.Scanner;

public class ProfessorClient {
    public  void processProfessor(String answer,PrintWriter out,BufferedReader in, Scanner scanner,String clientIPAddress
    , int clientPortNumber) {
        commonDetails commonDetails = new commonDetails();
        try {
            System.out.println("Welcome Professor:\n" +
                    "1. LogIn\n" +
                    "2. SignIn");

            answer = scanner.next();
            System.out.println("Entering " + answer);

            switch (answer){
                case "1":
                    TCPClient.type=1;
                    System.out.println("LogIn Start...");
                    out.println("LogIn");
                    commonDetails.processCommonUserDetails_login(out,scanner);
                    break;
                case "2":
                    System.out.println("SignIn Start...");
                    out.println("SignIn");
                    TCPClient.type=2;
                    commonDetails.signin_processCommonProfessorDetails(out,scanner);
            }



        } catch (Exception e) {
            System.out.println(e);
        }
    }
}


