package org.maven.Project_ISS.socket;

import org.maven.Project_ISS.AES.AsymmetricEncryption;
import org.maven.Project_ISS.PGP.PrettyGoodPrivacy;
import org.maven.Project_ISS.dao.*;
import org.maven.Project_ISS.socket.AuthForms.LoginHandler;
import org.maven.Project_ISS.socket.AuthForms.SignInHandler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.PublicKey;

public class ServerClientThread extends Thread {
    private final Socket serverClient;
    private final int clientNo;

    public ServerClientThread(Socket inSocket, int counter) {
        serverClient = inSocket;
        clientNo = counter;
    }

    public void run() {
        try {
            StudentDao studentDao = new StudentDaoImpl();
            ProfessorDao professorDao = new ProfessorDaoImpl();
            BufferedReader in = new BufferedReader(new InputStreamReader(serverClient.getInputStream()));
            PrintWriter out = new PrintWriter(serverClient.getOutputStream(), true);


            // Read client request
            String request = in.readLine();

            System.out.println("Request: " + request);


            // Handling client's request
            if (request.contains("LogIn")) {
                System.out.println(request);
                String name = in.readLine();
                System.out.println("Name: " + name);
                String password = in.readLine();
                System.out.println("Password: " + password);

                String IPAddress = in.readLine();
                int PortNumber = Integer.parseInt(in.readLine());
                System.out.println("Client : " + name + " send request with IPAddress :" + IPAddress + " and Port Number =" + PortNumber);

                new LoginHandler(out, studentDao, professorDao).handleLogin(name, password);

            }


            if (request.contains("SignIn")) {
                System.out.println(request);
                int id_number = Integer.parseInt(in.readLine());
                System.out.println("id_number: " + id_number);
                String name = in.readLine();
                System.out.println("Name: " + name);

                String password = in.readLine();
                System.out.println("Password: " + password);

                String IPAddress = in.readLine();
                int PortNumber = Integer.parseInt(in.readLine());
                System.out.println("Client : " + name + " send request with IPAddress :" + IPAddress + " and Port Number =" + PortNumber);

                new SignInHandler(out, studentDao, professorDao).handleSignIn(id_number, name, password);


                String key = studentDao.get_national_number(id_number);
                if (key == null) {
                    key = professorDao.get_national_number(id_number);
                }
                String address = in.readLine();


                System.out.println("address: " + address);

                String address_after_decrypt = AsymmetricEncryption.decrypt(address, key);
                String phone_number = in.readLine();

                System.out.println("phone_number: " + phone_number);
                int phone_number_after_decrypt = Integer.parseInt(AsymmetricEncryption.decrypt(phone_number, key));
                String mobile_number = in.readLine();

                System.out.println("mobile_number: " + mobile_number);

                int mobile_number_after_decrypt = Integer.parseInt(AsymmetricEncryption.decrypt(mobile_number, key));
                int id = studentDao.get_id(name);
                if (id == 0) {
                    id = professorDao.get_id(name);
                    Professor professor = new Professor(id, name, password, address_after_decrypt, phone_number_after_decrypt, mobile_number_after_decrypt);
                    professorDao.update(professor);
                } else {
                    Student student = new Student(id, name, password, address_after_decrypt, phone_number_after_decrypt, mobile_number_after_decrypt);
                    studentDao.update(student);
                }
                String message = "The information completion stage has been completed";
                String message_after = AsymmetricEncryption.encrypt(message, key);
                System.out.println("done information : " + message);
                out.println(message_after);
//                out.flush();
            }
            // handshake
            PublicKey clientPublicKey = performHandshake(in, out);
            //recieve session key
            String clientMessage = in.readLine();
            String sessionkey = PrettyGoodPrivacy.decryptRSA(clientMessage, "keys\\server\\priSVerVerate.txt");
            System.out.println("session key : " + sessionkey);

            //send ok responce
            String serverMessage = "The session key has been received";
            serverMessage = AsymmetricEncryption.encrypt(serverMessage, sessionkey);
            out.println(serverMessage);



           /* out.close();
            in.close();
            serverClient.close();*/

        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            System.out.println("Client - " + clientNo + " exit!!");
        }

    }

    private PublicKey performHandshake(BufferedReader in, PrintWriter out) throws Exception {
        System.out.println(">> Handshake started ...");

        // Create input and output streams for communication
//        BufferedReader in = new BufferedReader(new InputStreamReader(serverClient.getInputStream()));
//        PrintWriter out = new PrintWriter(serverClient.getOutputStream(), true);

        // Receive the client's handshake message
        String clientMessage = in.readLine();

        System.out.println("Client public key: " + clientMessage);

        System.out.println("===============================================================================");
        PublicKey clientPublicKey = PrettyGoodPrivacy.convertStringToPublicKey(clientMessage);

        PublicKey serverPublicKey = PrettyGoodPrivacy.readPublicKeyFromFile("keys\\server\\puSPerVerlic.txt");
        String publicKeyToString = PrettyGoodPrivacy.convertPublicKeyToString(serverPublicKey);

        out.println(publicKeyToString);

        return clientPublicKey;
    }

}
/*
import org.maven.Project_ISS.dao.ProfessorDao;
import org.maven.Project_ISS.dao.ProfessorDaoImpl;
import org.maven.Project_ISS.dao.StudentDao;
import org.maven.Project_ISS.dao.StudentDaoImpl;

import java.io.*;
import java.net.Socket;

public class ServerClientThread extends Thread {
    Socket serverClient;
    int clientNo;
     static global_variable global_variable;
    ServerClientThread(Socket inSocket,int counter){
        serverClient = inSocket;
        clientNo=counter;
    }
//    DataOutputStream outStream;
    public void run(){
        try{
            StudentDao studentDao = new StudentDaoImpl();
            ProfessorDao professorDao = new ProfessorDaoImpl();
            boolean is_student_exist ;
            boolean is_prof_exist ;
//           DataOutputStream outStream = new DataOutputStream(serverClient.getOutputStream());
            // provides sending different types of primitive data to the clients
//            String clientMessage="", serverMessage="";
            BufferedReader in = new BufferedReader(new InputStreamReader(serverClient.getInputStream()));
            PrintWriter out = new PrintWriter(serverClient.getOutputStream(), true);

            // Read client request
            String request = in.readLine();
            System.out.println("request"+ "\t"+request);
            String name =in.readLine();
            System.out.println("name :"+name);
            int password =Integer.parseInt(in.readLine());
            System.out.println("pass:"+password);
            String IPAddress = in.readLine();
            int PortNumber =Integer.parseInt(in.readLine());

            ///handling clients request
            //----------------LogIn Request----------
            if(request.contains("LogIn")){
            System.out.println("client logged in"+  "\t"+"called" + "\t"+name+ "\t"+ "With IP Address : " +IPAddress
                    + "\t" + "and Port number =" + PortNumber);
            //--------------------------
            is_student_exist=  studentDao.exist(name);
            is_prof_exist = professorDao.exist(name);
            //-----------------------
            if(is_student_exist || is_prof_exist){
            System.out.println("From Client-"+  "\t" + clientNo + "\t" +  ": name  :" + "\t" + name +"\n"+
                    "password" +  "\t" + + password);
//                serverMessage="Welcome to our System";
            out.println("Welcome to our System"+ "\t" + name);
    }
    else{
        System.out.println("Client-"  + "\t" + +clientNo + "\t" + "with name:"  + "\t" +name + " Failed to logIn.."
        + "\n" + "This name Doesn't exist in our records ");
                out.println("Sorry! This name" + "\t {" +name + "\t }" +"Doesn't exist in our records");
//        serverMessage =" Sorry! This name Doesn't exist in our records" ;
            }}
            //----------------SignIn Request----------

            if(request.contains("SignIn")){
                is_student_exist=  studentDao.exist(name);
                is_prof_exist = professorDao.exist(name);
                if(is_student_exist || is_prof_exist){
                    System.out.println("From Client-"+  "\t" + clientNo + "\t" +  ": name  :" + "\t" + name +"\n"+
                            "password" +  "\t" + + password);
//                serverMessage="Welcome to our System";
                    out.println("Your SignIn Have Done Successfully: Welcome!"+ "\t" + name);
                    System.out.println("SignIn Done!");
       }}
//               outStream.writeUTF(serverMessage);
//               outStream.flush();
            out.flush();
            in.close();
            out.close();
//            outStream.close();
            serverClient.close();

               }
        catch(Exception ex){
               System.out.println(ex); }

        finally{
           System.out.println("Client -" + clientNo + " exit!! ");
        }

    }}*/
//////////////////
// obtaining input and out streams to communicate with the connected clients
//        DataInputStream inStream = new DataInputStream(serverClient.getInputStream());
//getInputStream provides reading different types of primitive data
//to read what clients send to server