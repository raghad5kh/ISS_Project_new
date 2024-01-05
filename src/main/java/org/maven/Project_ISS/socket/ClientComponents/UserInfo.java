package org.maven.Project_ISS.socket.ClientComponents;

import org.maven.Project_ISS.AES.AsymmetricEncryption;
import org.maven.Project_ISS.dao.*;

import java.io.PrintWriter;
import java.util.Scanner;

public class UserInfo {

    private static void getClientDetails(PrintWriter out , String clientIPAddress,int clientPortNumber ) {
        out.println(clientIPAddress);
        out.println(clientPortNumber);
    }

    public  void StudentInfo(PrintWriter out, Scanner scanner, String clientIPAddress, int clientPortNumber, int id_number, String name, String password) throws Exception {
       String key = AsymmetricEncryption.readNational_numberFromFile("national_numbers\\client\\Student\\national_number_" + id_number +".txt");
        String address;
        int phone_number;
        int mobile_number;

        System.out.println("Complete your information...............");
        System.out.println("Enter your address");
        address = scanner.next();
        System.out.println("Your address: " + address);
        String address_after_encrypt = AsymmetricEncryption.encrypt(address,key);
        out.println(address_after_encrypt);

        System.out.println("Enter your phone_number");
        phone_number = Integer.parseInt(scanner.next());
        System.out.println("Your phone_number: " + phone_number);
        String phone_number_after_encrypt = AsymmetricEncryption.encrypt(String.valueOf(phone_number),key);
        out.println(phone_number_after_encrypt);

        System.out.println("Enter your mobile_number");
        mobile_number = Integer.parseInt(scanner.next());
        System.out.println("Your mobile_number: " + mobile_number);
        String mobile_number_after_encrypt = AsymmetricEncryption.encrypt(String.valueOf(mobile_number),key);
        out.println(mobile_number_after_encrypt);


        out.flush();
    }



    public  void ProfessorInfo(PrintWriter out, Scanner scanner, String clientIPAddress, int clientPortNumber, int id_number, String name, String password) throws Exception {



        String key = AsymmetricEncryption.readNational_numberFromFile("national_numbers\\client\\Professor\\national_number_" + id_number +".txt");
        String address;
        int phone_number;
        int mobile_number;

        System.out.println("Complete your information...............");
        System.out.println("Enter your address");
        address = scanner.next();
        System.out.println("Your address: " + address);
        String address_after_encrypt = AsymmetricEncryption.encrypt(address,key);
        out.println(address_after_encrypt);

        System.out.println("Enter your phone_number");
        phone_number = Integer.parseInt(scanner.next());
        System.out.println("Your phone_number: " + phone_number);
        String phone_number_after_encrypt = AsymmetricEncryption.encrypt(String.valueOf(phone_number),key);
        out.println(phone_number_after_encrypt);

        System.out.println("Enter your mobile_number");
        mobile_number = Integer.parseInt(scanner.next());
        System.out.println("Your mobile_number: " + mobile_number);
        String mobile_number_after_encrypt = AsymmetricEncryption.encrypt(String.valueOf(mobile_number),key);
        out.println(mobile_number_after_encrypt);

        out.flush();
    }
}
