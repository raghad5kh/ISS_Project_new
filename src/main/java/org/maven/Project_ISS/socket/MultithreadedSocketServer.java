package org.maven.Project_ISS.socket;

import org.maven.Project_ISS.PGoodP.PrettyGoodPrivacy;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetSocketAddress;

public class MultithreadedSocketServer {
    public static void main(String[] args) {
        startServer();
    }

    private static void startServer() {
        String ipAddress = "127.0.0.1";
        int portNumber = 8080;

        try (ServerSocket server = new ServerSocket()) {
            InetSocketAddress address = new InetSocketAddress(ipAddress, portNumber);
            server.bind(address);

            PrettyGoodPrivacy.generateKeyPair("keys\\server\\priSVerVerate.txt",
                    "keys\\server\\puSPerVerlic.txt");
            System.out.println("Server Started ....");

            // getting client request
            int counter = 0;
            while (true) {
                counter++;
                Socket serverClient = server.accept();
                // wait until client request a connection then accept it
                System.out.println(" >> Client No: " + counter + " started!");
                new ServerClientThread(serverClient, counter).start();
            }
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
}