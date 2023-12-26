package org.maven.Project_ISS.socket;


/*import java.net.ServerSocket;
import java.net.Socket;
public class MultithreadedSocketServer {
    public static void main(String[] args) throws Exception {
        try{
            ServerSocket server=new ServerSocket(8888); //create server listens on port 8888
            int counter=0;
            System.out.println("Server Started ....");
            // getting client request
            while(true){
                counter++;
                Socket serverClient=server.accept();
                //wait until client request a connection then accept it
                System.out.println(" >> " + "Client No:" + counter + " started!");
                ServerClientThread sct = new ServerClientThread(serverClient,counter); //send  the request to a separate thread
                sct.start();
            }
        }catch(Exception e){
            System.out.println(e);
        }
    }
}*/

import org.maven.Project_ISS.PGoodP.PrettyGoodPrivacy;

import java.net.ServerSocket;
import java.net.Socket;

public class MultithreadedSocketServer {
    public static void main(String[] args) {
        startServer();
    }

    private static void startServer() {

        try (ServerSocket server = new ServerSocket(8888)) {
            PrettyGoodPrivacy.generateKeyPair("keys\\server\\priSVerVerate.txt",
                    "keys\\server\\puSPerVerlic.txt");
            System.out.println("Server Started ....");
            // getting client request
            int counter = 0;
            while (true) {
                counter++;
                Socket serverClient = server.accept();
                //wait until client request a connection then accept it
                System.out.println(" >> Client No: " + counter + " started!");
                new ServerClientThread(serverClient, counter).start();

            }
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
}
