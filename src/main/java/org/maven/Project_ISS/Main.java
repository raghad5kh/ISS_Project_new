package org.maven.Project_ISS;

import org.maven.Project_ISS.dao.*;
import org.maven.Project_ISS.socket.TCPClient;

import java.sql.Connection;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
       int number=1;
       Scanner scanner = new Scanner(System.in);
       /* StudentDao studentDaot = new StudentDaoImpl();
        int k= studentDaot.get_national_number(53221);
        System.out.println(k);*/



       System.out.println("Choose Your Role Please!"+ "\n"+ "Enter 1 if you are Professor"
                + "\n"+ "Enter 2 if you are Student");

int choice_number =scanner.nextInt();
        switch (choice_number){
             case 1:
                 System.out.println("welcome ss");
             break;
            case 2:
                System.out.println("welcome pp");
                break;
            default:
                System.out.println("welcome all");

         }



    }
}