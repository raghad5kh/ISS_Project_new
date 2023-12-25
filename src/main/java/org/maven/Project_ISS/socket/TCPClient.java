package org.maven.Project_ISS.socket;

import org.maven.Project_ISS.AES.AsymmetricEncryption;
import org.maven.Project_ISS.PGoodP.PrettyGoodPrivacy;
import org.maven.Project_ISS.dao.ProfessorDao;
import org.maven.Project_ISS.dao.ProfessorDaoImpl;
import org.maven.Project_ISS.dao.StudentDao;
import org.maven.Project_ISS.dao.StudentDaoImpl;
import org.maven.Project_ISS.socket.ClientComponents.ProfessorClient;
import org.maven.Project_ISS.socket.ClientComponents.StudentClient;
import org.maven.Project_ISS.socket.ClientComponents.UserInfo;
import org.maven.Project_ISS.socket.ClientComponents.commonDetails;

import java.io.*;
import java.net.Socket;
import java.security.PublicKey;
import java.util.Scanner;


public class TCPClient {

    public static int type=0;
//    static void setType()
    static String answer = "";
    static String clientIPAddress;
    static int clientPortNumber;
    static ProfessorClient professorClient = new ProfessorClient();
    static StudentClient studentClient = new StudentClient();
    static commonDetails commonDetails = new commonDetails();
    static UserInfo userInfo = new UserInfo();

    private static String sessionKey;

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        String publicKeyPath = "keys\\client\\pu" +"id_number" + "PL" + "key" + "ic.txt";
        String privateKeyPath = "keys\\client\\pri" +"id_number" + "V" + "key" + "ate.txt";
        try {
            Socket socket = new Socket("127.0.0.1", 8888);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            getClientDetails(socket);

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String clientMessage = "";


            System.out.println("Enter your role in this SYSTEM!\n" +
                    "1. Professor\n" +
                    "2. Student");

            clientMessage = br.readLine();
            System.out.println("User entered for role: " + clientMessage);

            processUserRole(clientMessage, out, scanner);

            String serverMessage = in.readLine();
            System.out.println("Server response: " + serverMessage);
            String compareString ="Your SignIn has been done successfully.";
            if (type==2) {
                String serverMessage2 = in.readLine();

                if (serverMessage2 == null) {
                    return;
                }

                String[] parts = serverMessage2.split(",");
                int id_number = Integer.parseInt(parts[0]);
                String name = parts[1];
                String password = parts[2];

                if (serverMessage.substring(0, serverMessage.indexOf('.') + 1).equals(compareString.substring(0, compareString.indexOf('.') + 1))) {
                    processUserinfo(clientMessage, out, scanner, id_number, name, password);
                }

                String serverMessage3 = in.readLine();
                StudentDao studentDao = new StudentDaoImpl();
                ProfessorDao professorDao = new ProfessorDaoImpl();
                String key = studentDao.get_national_number(id_number);
                if (key == null) {
                    key = professorDao.get_national_number(id_number);
                }
                String serverMessage3_after_decrypt = AsymmetricEncryption.decrypt(serverMessage3, key);
                System.out.println("Server response: " + serverMessage3_after_decrypt);
                System.out.println("--start generate kesy --");

                // generate pair key

                PrettyGoodPrivacy.generateKeyPair(privateKeyPath, publicKeyPath);

            }
             //handshake
            PublicKey serverPublicKey = performHandshake(publicKeyPath,in,out);

            // generate session key and send it
            String sessionkey = AsymmetricEncryption.generateKey();
            sessionKey=sessionkey;
            System.out.println("sessionkey : " +sessionkey);
            AsymmetricEncryption.encrypt("alaaaajhgf",sessionkey);
            String sessionkeyEncrypte =PrettyGoodPrivacy.encryptRSA(sessionkey,serverPublicKey);
            out.println(sessionkeyEncrypte);

            String okresponce=in.readLine();
            String okresponceAfter=AsymmetricEncryption.decrypt(okresponce,sessionkey);
            System.out.println("ok responce : "+okresponceAfter);

      /*      in.close();
            out.close();
            socket.close();*/
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private static void getClientDetails(Socket socket) {
        clientIPAddress = socket.getLocalAddress().getHostAddress();
        clientPortNumber = socket.getLocalPort();
        System.out.println("IP Address: " + clientIPAddress + ", Port Number: " + clientPortNumber);
    }

    private static void processUserRole(String clientMessage, PrintWriter out, Scanner scanner) {


        switch (clientMessage) {
            case "1":
             professorClient.processProfessor(answer,out,scanner,clientIPAddress,clientPortNumber);
//                processProfessor(out, scanner);
                break;
            case "2":
                studentClient.processStudent(out,scanner,answer,clientIPAddress,clientPortNumber);
//                processStudent(out, scanner);
                break;
            default:
                System.out.println("Unexpected answer!");
                break;
        }
    }

    private static void processUserinfo(String clientMessage, PrintWriter out, Scanner scanner,int id_number,String name,String password) throws Exception {


        switch (clientMessage) {
            case "1":
               userInfo.ProfessorInfo(out,scanner,clientIPAddress,clientPortNumber,id_number,name,password);
                break;
            case "2":
                userInfo.StudentInfo(out, scanner,clientIPAddress,clientPortNumber,id_number,name,password);
                break;
            default:
                System.out.println("Unexpected answer!");
                break;
        }
    }


    public static PublicKey performHandshake(String publicKeyPath,BufferedReader in,PrintWriter out) throws Exception {
        System.out.println(">> Handshake started ...");

        PublicKey publicKey = PrettyGoodPrivacy.readPublicKeyFromFile(publicKeyPath);
        // Send the client's public key to the server
        String publicKeyToString = PrettyGoodPrivacy.convertPublicKeyToString(publicKey);
        System.out.println("===============================================================================");
        out.println(publicKeyToString);

        // Receive the server's public key
        String serverMessage = in.readLine();

        PublicKey serverPublicKey = PrettyGoodPrivacy.convertStringToPublicKey(serverMessage);
        System.out.println("Server's public key: " + serverMessage);

//            PrettyGoodPrivacy.storePublicKeyToFile(serverPublicKey, "");
        return serverPublicKey;

    }

   /* private static void processProfessor(PrintWriter out, Scanner scanner) {
        try {
            System.out.println("Welcome Professor:\n" +
                    "1. LogIn\n" +
                    "2. SignIn");

            answer = scanner.next();
            System.out.println("Entering " + answer);

            switch (answer){
                case "1":
                    System.out.println("LogIn Start...");
                    out.println("LogIn");
                    break;
                case "2":
                    System.out.println("SignIn Start...");
                    out.println("SignIn");
            }


            processCommonUserDetails(out, scanner);
        } catch (Exception e) {
            System.out.println(e);
        }
    }*/

    /*private static void processStudent(PrintWriter out, Scanner scanner) {
        try {
            System.out.println("Welcome Student:\n" +
                    "1. LogIn\n" +
                    "2. SignIn");

            answer = scanner.next();
            System.out.println("Entering " + answer);

            switch (answer){
                case "1":
                    System.out.println("LogIn Start...");
                    out.println("LogIn");
                    break;
                case "2":
                    System.out.println("SignIn Start...");
                    out.println("SignIn");
            }
//            global_variable.Request_Type = "SignIn";

            processCommonUserDetails(out, scanner);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
*/
    /*private static void processCommonUserDetails(PrintWriter out, Scanner scanner) {
        String name;
        int password;

        System.out.println("Enter your name");
        name = scanner.next();
        System.out.println("Your name: " + name);
        out.println(name);

        System.out.println("Enter your password");
        password = scanner.nextInt();
        out.println(password);
        System.out.println("Your password: " + password);

        getClientDetails(out);

        out.flush();
    }*/

    /*private static void getClientDetails(PrintWriter out) {
        out.println(clientIPAddress);
        out.println(clientPortNumber);
    }*/
}

/*public class TCPClient {
    static String answer="";
    static String ClientIPAddress;
    static  int ClientPortNumber;
//    static global_variable global_variable;
    public static void main(String[] args) throws Exception {
        Scanner scanner= new Scanner(System.in);
        try{
            Socket socket=new Socket("127.0.0.1",8888);
//           DataInputStream inStream=new DataInputStream(socket.getInputStream());
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            ClientIPAddress = socket.getLocalAddress().getHostAddress();
            ClientPortNumber = socket.getLocalPort();
            System.out.println("IP Address :"+ ClientIPAddress+"port num:    "+ClientPortNumber);
            BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
            String clientMessage="",serverMessage="";
            System.out.println("Enter your role in this SYSTEM! :"
                    +"\n"+"Enter 1 if your are Professor"  +
                    "\n"+"\"Enter 2 if your are Student");

            clientMessage=br.readLine();
            System.out.println("user Entered for role"+clientMessage);

            if(clientMessage.equals("1")){
                System.out.println("Welcome Professor :"
                        +"\n"+"Enter 1 if you want to logIn please"  +
                        "\n"+"\"Don't have an account? Enter 2 to signIn");
                answer=scanner.next();
                System.out.println("entering"+clientMessage + "---" + answer);

            }
            if(clientMessage.equals("2")){
                System.out.println("Welcome Student :"
                        +"\n"+"Enter 1 if you want to logIn please"  +
                        "\n"+"\"Don't have an account? Enter 2 to signIn");
                answer=scanner.next();
                System.out.println("entering"+clientMessage + "---" + answer);
            }
            String name="";
            int password=0;


            int id=0;

            switch (answer){
                case "1":
                    System.out.println("logIn Start...");
                    out.println("LogIn");
                    System.out.println("Enter your name");
                    name=scanner.next();
                    System.out.println(" your name"+name);
                    out.println(name);
                    System.out.println("Enter your password");
                    password=scanner.nextInt();
                    out.println(password);
                    System.out.println(" your pass"+password);
                    out.println(ClientIPAddress);
                    out.println(ClientPortNumber);
                    break;
                case "2":
                    System.out.println("signIn Start...");
                    out.println("SignIn");
                    global_variable.Request_Type="SignIn";
//                        signImForm.signIn(name,password,id);
                    System.out.println("Enter your real name ");
                    name=scanner.next();
                    out.println(name);
                    System.out.println(" real name :" +"\t" + name);

                    System.out.println("Enter your password");
                    password=scanner.nextInt();
                    out.println(password);
                    System.out.println("  pass :" +"\t" + password);

                    System.out.println("Enter your ID ");
                    id=scanner.nextInt();

                    out.println(ClientIPAddress);
                    out.println(ClientPortNumber);
                    break;
                default:
                    System.out.println("unexpected answer !");
                    break;

            }
            String server_msg= in.readLine();
            System.out.println("server response :" + server_msg);

            out.close();
            socket.close();
        }catch(Exception e){
            System.out.println(e);
        }
    }
}
*/

/////-------
//            DataOutputStream name_stream=new DataOutputStream(socket.getOutputStream());
//            DataOutputStream password_stream=new DataOutputStream(socket.getOutputStream());

//            DataOutputStream name_logIn=new DataOutputStream(socket.getOutputStream());
//            DataOutputStream password_logIn=new DataOutputStream(socket.getOutputStream());
//            DataOutputStream name_signIn=new DataOutputStream(socket.getOutputStream());
//            DataOutputStream password_signIn=new DataOutputStream(socket.getOutputStream());
//            DataOutputStream ID=new DataOutputStream(socket.getOutputStream());

//            else{
//                System.out.println("unexpected answer please re-read our message!");
//            }
//            String user_name_logIn="";
//            String user_name_signIn="";

//            int user_password_logIn=0;
//            int user_password_signIn=0;






//////-----
//            name_stream.writeUTF(name);
//            password_stream.writeUTF(String.valueOf(password));

//            name_stream.flush();
//            name_stream.close();

          /*  name_logIn.writeUTF(user_name_logIn);
            password_logIn.writeUTF(String.valueOf(user_password_logIn));
            name_signIn.writeUTF(user_name_signIn);
            password_signIn.writeUTF(String.valueOf(user_password_signIn));*/
//            ID.writeUTF(String.valueOf(id));
            /*name_logIn.flush();
            password_logIn.flush();
            name_signIn.flush();*/
//            ID.flush();
//            password_signIn.flush();
//            serverMessage=inStream.readUTF();
//            System.out.println(serverMessage);

//            password_logIn.close();
//            name_logIn.close();
//            name_signIn.close();
//            password_signIn.close();
//            ID.close();

