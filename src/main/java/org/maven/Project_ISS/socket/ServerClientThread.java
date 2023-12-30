package org.maven.Project_ISS.socket;

import org.maven.Project_ISS.AES.AsymmetricEncryption;
//import org.maven.Project_ISS.DigitalSignature.DSA;
//import org.maven.Project_ISS.DigitalSignature.DigitalSignatureExample;
import org.maven.Project_ISS.CSR.CSRGenerator;
import org.maven.Project_ISS.DCA.DCA;
import org.maven.Project_ISS.DigitalSignature.DSA;
import org.maven.Project_ISS.DigitalSignature.StudentInfo;
import org.maven.Project_ISS.DigitalSignature.StudentMarks;
import org.maven.Project_ISS.PGoodP.PrettyGoodPrivacy;
import org.maven.Project_ISS.dao.*;
import org.maven.Project_ISS.socket.AuthForms.LoginHandler;
import org.maven.Project_ISS.socket.AuthForms.SignInHandler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

public class ServerClientThread extends Thread {
    public static int client_type;
    private final Socket serverClient;
    private String sessionKey;
    private final int clientNo;

    private static String username;
    public static boolean testforsign=false;

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
            ObjectInputStream objectInputStream = new ObjectInputStream(serverClient.getInputStream());
            DSA dsa=new DSA();            // Read client request
            String request = in.readLine();
            StudentMarks studentMarks = new StudentMarks();
            System.out.println("Request: " + request);


            // Handling client's request
            if (request.contains("LogIn")) {
                System.out.println(request);
                String name = in.readLine();
                System.out.println("Name: " + name);
                username = name;
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
                username = name;
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

            //store public key in database
            addingPublicKeyToDB(clientPublicKey);
            //recieve session key
            String clientMessage = in.readLine();
            String sessionkey = PrettyGoodPrivacy.decryptRSA(clientMessage, "keys\\server\\priSVerVerate.txt");
            System.out.println("session key : " + sessionkey);
            sessionKey = sessionkey;

            //send ok responce
            String serverMessage = "The session key has been received";
            serverMessage = AsymmetricEncryption.encrypt(serverMessage, sessionkey);
            out.println(serverMessage);
//-----------------
            if(client_type==1) {
                String receivedMessage = "Dear prof , " + username + " , your signed file have been received";
                //Professor
               String publicKeyString = professorDao.get_publicKey(username);
               PublicKey publicKey= PrettyGoodPrivacy.convertStringToPublicKey(publicKeyString);
                System.out.println("Client publicKey"+publicKey);
                List<StudentInfo> DecodedMarksList= new ArrayList<StudentInfo>();
//                ------ NOT USED!
                        List<StudentInfo> ReceivedMarksList=
                        (List<StudentInfo>) objectInputStream.readObject();
//                ------ NOT USED!
// Variables TO BE STORED in DB----------------------------------------
                byte[] serializedMatrixFrimClient =(byte[]) objectInputStream.readObject();//1) Byte Array of Marks List
             DecodedMarksList=studentMarks.convertBytesToList(serializedMatrixFrimClient);//2) convert byte arr into list<StudentInfo>
                System.out.println("ReceivedMarksList");
                studentMarks.getStudentsWithMarks(DecodedMarksList);
                byte[] receivedSignatureByteList =(byte[]) objectInputStream.readObject();//3) signature byte arr
                //verification of signature
                dsa.verifySignature(serializedMatrixFrimClient,receivedSignatureByteList,publicKey);
                if ( dsa.verifySignature(serializedMatrixFrimClient,receivedSignatureByteList,publicKey)) {
                    System.out.println("Professor Signature is VALID!.");
                    out.println(receivedMessage);
                } else {
                    System.err.println("It is not possible to validate the signature.");
                }

            }
// Variables TO BE STORED in DB----------------------------------------

            //-----------------
            if(client_type==2) {
                //receive Projects list from student
                List<String> projectsListEncoded = (List<String>) objectInputStream.readObject();
                List<String> projectList = new ArrayList<>();
                for (int i = 0; i < projectsListEncoded.size(); i++) {
                    String project = AsymmetricEncryption.decrypt(projectsListEncoded.get(i), sessionkey);
                    projectList.add(project);
                    System.out.println("Project number " + (i + 1) + " : " + projectsListEncoded.get(i));
                    System.out.println("Project number " + (i + 1) + " : " + project);
                }
                String receivedMessage = "Dear student , " + username + " , your project has been received";
                out.println(AsymmetricEncryption.encrypt(receivedMessage, sessionkey));
                System.out.println("receivedMessage : " + receivedMessage);
            }

            if (client_type == 1){
                StringBuilder csrBuilder = new StringBuilder();
                String line;

                while (!(line = in.readLine()).equals("")) {
                    //  System.out.println(line);
                    csrBuilder.append(line).append("\n");
                }
                String generatedCSR = csrBuilder.toString();
                System.out.println("receive CSR:");
                System.out.println(generatedCSR);
                PublicKey publicKey = CSRGenerator.extractPublicKeyFromCSR(generatedCSR);
                String publicKey_fromDB = professorDao.get_publicKey(username);
                PublicKey publicKey_pro = PrettyGoodPrivacy.convertStringToPublicKey(publicKey_fromDB);
                String name = CSRGenerator.extractNameFromCSR(generatedCSR);
                String password = CSRGenerator.extractPasswordFromCSR(generatedCSR);
                if(publicKey.equals(publicKey_pro)|| professorDao.exist_account(name,password)) {
                    out.println("Received CSR without errors");
                }
                else {
                    out.println("There are errors in the information");
                }


                int a = (int) (Math.random() * 10) + 1;
                int b = (int) (Math.random() * 10) + 1;
                int c = a * 5 + b;

                String equation = a + "x + " + b + " = " + c;
                out.println(equation);
                String test = in.readLine();
                int solution = Integer.parseInt(in.readLine());
                System.out.println("solution:" + solution);
                int correctSolution = (c - b) / a;
                String isCorrect ;
                if (solution == correctSolution) {
                    isCorrect ="true";
                    out.println(isCorrect);
                    System.out.println("client solution is correct.");
                    PublicKey publicKey_server= PrettyGoodPrivacy.readPublicKeyFromFile("keys\\server\\puSPerVerlic.txt");
                    PrivateKey privateKey_server = PrettyGoodPrivacy.readPrivateKeyFromFile("keys\\server\\priSVerVerate.txt");
                    KeyPair keyPair = DCA.createKeyPairFromKeyBytes(privateKey_server,publicKey_server);
                    X509Certificate digitalCertificate = DCA.generateDigitalCertificate(keyPair,username);
                    System.out.println("digitalCertificate"+digitalCertificate);
                    String certString = Base64.getEncoder().encodeToString(digitalCertificate.getEncoded());
                    String signature = DCA.sign(certString, keyPair.getPrivate());
                    System.out.println("Signature: " + signature);
                    boolean isVerified = DCA.verify(certString, signature, keyPair.getPublic());
                    System.out.println("Verification result: " + isVerified);
                    if (isVerified){
                    out.println(certString);}

                } else {
                    isCorrect= "false";
                    out.println(isCorrect);
                    System.out.println("client solution is incorrect. The correct solution is: " + correctSolution);
                    out.println("Sorry, your solution is incorrect.");
                }

            }
            //-----------------
//            System.out.println("jjjjjj"+testforsign);
//               byte[] message = studentMarks.getmessageByte(TCPClient.studentList);
//               String publicKeyString = professorDao.get_publicKey(username);
//               PublicKey publicKey= PrettyGoodPrivacy.convertStringToPublicKey(publicKeyString);
////            DigitalSignatureExample digitalSignatureExample = new DigitalSignatureExample();
//            System.out.println("isEnt"+TCPClient.isEntered);
////            if(TCPClient.isEntered){
//               System.out.println("enter condition");
//               ////
//            String receivedString = in.readLine();
//
//            String signature = in.readLine();
//               byte[] receivedSignature = Base64.getDecoder().decode(signature);
             // Receive the string from the client

            // Convert the string back to List<Student>
           /* List<org.maven.Project_ISS.DigitalSignature.Student> receivedStudents = convertStringToList(receivedString);

// Now 'receivedStudents' contains the List<Student> sent from the client               System.out.println("receivedSignature arr"+ Arrays.toString(receivedSignature));
               System.out.println("studentMarks.getmessageByte(TCPClient.studentList)"+ Arrays.toString(message));
               System.out.println( "publicKey"+publicKey);
               System.out.println( "String signature"+signature);
            System.out.println("listtttttt");
            studentMarks.getStudentsWithMarks(receivedStudents);
//               System.out.println("test for validation return"+dsa.validateProfMessageSignature(publicKey,receivedSignature));
               if(dsa.verifySignature(receivedStudents.toString().getBytes(),receivedSignature,publicKey)

               ){
                   System.out.println("trueeee");

                   out.println("Your Signature is Valid so Your CONNECTION is secured!");
               }
               else{
                   out.println("Your Signature is NOT Valid!");
               }
//           */
//            System.out.println("verifySignature----"+dsa.verifySignature(receivedStudents.toString().getBytes(),receivedSignature,publicKey) );

//            }
     


           /* out.close();
            in.close();
            serverClient.close();*/

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            System.out.println("Client - " + clientNo + " exit!!");
        }
    }
    private static List<StudentInfo> convertStringToList(String studentsString) {
        List<StudentInfo> students = new ArrayList<>();

        // Assuming the string is comma-separated, you can split it
        String[] studentArray = studentsString.split(",");

        // Iterate through each studentInfo in the array
        for (String studentInfo : studentArray) {
            // Assuming each studentInfo is formatted as "Name:Marks"
            String[] parts = studentInfo.split(":");

            if (parts.length == 2) {
                // Extract name and marks, then create a Student object
                String name = parts[0];
                int marks = Integer.parseInt(parts[1]);

                org.maven.Project_ISS.DigitalSignature.StudentInfo student = new StudentInfo(name, marks);
                students.add(student);
            } else {
                // Handle incorrect format if needed
                System.out.println("Invalid format for studentInfo: " + studentInfo);
            }
        }

        return students;
    }
    private static void addingPublicKeyToDB(PublicKey publicKey) {

        StudentDao studentDao = new StudentDaoImpl();
        ProfessorDao professorDao = new ProfessorDaoImpl();
        int id = studentDao.get_id(username);
        if (id == 0) {
            id = professorDao.get_id(username);
            professorDao.updatePublicKey(username, PrettyGoodPrivacy.convertPublicKeyToString(publicKey));
        } else {
            studentDao.updatePublicKey(username, PrettyGoodPrivacy.convertPublicKeyToString(publicKey));
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

        PublicKey clientPublicKey = PrettyGoodPrivacy.convertStringToPublicKey(clientMessage);

        PublicKey serverPublicKey = PrettyGoodPrivacy.readPublicKeyFromFile("keys\\server\\puSPerVerlic.txt");
        String publicKeyToString = PrettyGoodPrivacy.convertPublicKeyToString(serverPublicKey);

        out.println(publicKeyToString);
        System.out.println("===============================================================================");

        return clientPublicKey;
    }

}
