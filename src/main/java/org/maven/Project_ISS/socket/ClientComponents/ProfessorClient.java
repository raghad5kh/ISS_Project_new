package org.maven.Project_ISS.socket.ClientComponents;
import org.maven.Project_ISS.DigitalSignature.StudentMarks;
import org.maven.Project_ISS.socket.TCPClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
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
                    commonDetails.processCommonUserDetails_login(out,scanner,clientIPAddress,clientPortNumber);
                    break;
                case "2":
                    System.out.println("SignIn Start...");
                    out.println("SignIn");
                    TCPClient.type=2;
                    commonDetails.signin_processCommonProfessorDetails(out,scanner,clientIPAddress,clientPortNumber);
            }



        } catch (Exception e) {
            System.out.println(e);
        }
    }
}




/*public class ProfessorClient {
    private final PrintWriter out;
    private final BufferedReader in;

    public ProfessorClient(PrintWriter out, BufferedReader in) {
        this.out = out;
        this.in = in;
    }

    public void processProfessor() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            Scanner scanner = new Scanner(System.in);
            String clientMessage;
            String name;
            int password;
            System.out.println("Welcome Professor:\n" +
                    "1. LogIn\n" +
                    "2. SignIn");

            String answer = br.readLine();
            System.out.println("Entering " + answer);

            switch (answer) {
                case "1":
                    System.out.println("LogIn Start...");
                    out.println("LogIn");

                    System.out.println("Enter your name");
                    name = scanner.next();
                    out.println(name);

                    System.out.println("Your name: " + name);
                    System.out.println("Enter your password");
                    password = scanner.nextInt();
                    out.println(password);
                    System.out.println("Your password: " + password);
//                    getClientDetails();
                    out.flush();

                    break;
                case "2":
                    System.out.println("SignIn Start...");
                    out.println("SignIn");
                    System.out.println("Enter your name");
                    name = in.readLine();
                    System.out.println("Your name: " + name);
                    out.println(name);

                    System.out.println("Enter your password");
                    password = Integer.parseInt(in.readLine());
                    out.println(password);
                    System.out.println("Your password: " + password);
                    out.flush();

//                    getClientDetails();

                    break;
                default:
                    System.out.println("Unexpected answer!");
                    break;
            }
            out.close();
            in.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void processCommonUserDetails() {
        try {

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}*/
//    private void getClientDetails() {
//        try {
//            String clientIPAddress = socket.getLocalAddress().getHostAddress();
//            int clientPortNumber = socket.getLocalPort();
//            System.out.println("Client IP Address: " + clientIPAddress + ", Port Number: " + clientPortNumber);
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//    }
