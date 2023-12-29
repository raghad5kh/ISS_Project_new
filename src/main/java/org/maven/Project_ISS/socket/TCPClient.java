package org.maven.Project_ISS.socket;

import org.maven.Project_ISS.AES.AsymmetricEncryption;
import org.maven.Project_ISS.DigitalSignature.DSA;
//import org.maven.Project_ISS.DigitalSignature.DigitalSignatureExample;
import org.maven.Project_ISS.DigitalSignature.Student;
import org.maven.Project_ISS.DigitalSignature.StudentMarks;
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
import java.lang.reflect.Array;
import java.net.Socket;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.*;


public class TCPClient {

    public static int type = 0;
    //    static void setType()
    static String answer = "";
    public static  boolean isEntered=false;
    public static  List<Student> studentList = new ArrayList<>();

    static String clientIPAddress;
    static int clientPortNumber;
    static ProfessorClient professorClient = new ProfessorClient();
    static StudentClient studentClient = new StudentClient();
    static commonDetails commonDetails = new commonDetails();

    public static String username;
    static UserInfo userInfo = new UserInfo();

    private static String sessionKey;

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        StudentMarks studentMarks = new StudentMarks();
        byte[] students_marks_msg;
        ArrayList students_mark_list;
        String publicKeyPath = "";
        String privateKeyPath = "";
        int id_number;
        String national_number;
        DSA dsa = new DSA();
//        DigitalSignatureExample digitalSignatureExample = new DigitalSignatureExample();
         students_marks_msg = studentMarks.getmessageByte(studentList);
        byte[] sign_message ;
        try {
            Socket socket = new Socket("127.0.0.1", 8888);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

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
            String compareString = "Your SignIn has been done successfully.";
            if (type == 2) {
                String serverMessage2 = in.readLine();

                if (serverMessage2 == null) {
                    return;
                }

                String[] parts = serverMessage2.split(",");
                id_number = Integer.parseInt(parts[0]);
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
                national_number = key;
                String serverMessage3_after_decrypt = AsymmetricEncryption.decrypt(serverMessage3, key);
                System.out.println("Server response: " + serverMessage3_after_decrypt);
                System.out.println("--start generate kesy --");

                // generate pair key
                publicKeyPath = "keys\\client\\pu" + id_number + "PL" + key + "ic.txt";
                privateKeyPath = "keys\\client\\pri" + id_number + "V" + key + "ate.txt";
                PrettyGoodPrivacy.generateKeyPair(privateKeyPath, publicKeyPath);
            }else {
                if (StudentDaoImpl.isStudent(username)) {
                    id_number = StudentDaoImpl.get_id_number(username);
                    StudentDaoImpl studentDao = new StudentDaoImpl();
                    national_number = studentDao.get_national_number(id_number);
                } else {
                    id_number = ProfessorDaoImpl.get_id_number(username);
                    ProfessorDaoImpl professorDao = new ProfessorDaoImpl();
                    national_number = professorDao.get_national_number(id_number);
                }
                publicKeyPath = "keys\\client\\pu" + id_number + "PL" + national_number + "ic.txt";
                privateKeyPath = "keys\\client\\pri" + id_number + "V" + national_number + "ate.txt";
            }

            //handshake
            PublicKey serverPublicKey = performHandshake(publicKeyPath, in, out);

            // generate session key and send it
            String sessionkey = AsymmetricEncryption.generateKey();
            sessionKey = sessionkey;
            System.out.println("sessionkey : " + sessionkey);

            String sessionkeyEncrypte = PrettyGoodPrivacy.encryptRSA(sessionkey, serverPublicKey);
            out.println(sessionkeyEncrypte);
            //receive ok message
            String okresponce = in.readLine();
            String okresponceAfter = AsymmetricEncryption.decrypt(okresponce, sessionkey);
            System.out.println("ok responce : " + okresponceAfter);
            PrivateKey privateKey= PrettyGoodPrivacy.readPrivateKeyFromFile(privateKeyPath);
            PublicKey publicKey=PrettyGoodPrivacy.readPublicKeyFromFile(publicKeyPath);
            System.out.println( "publll"+publicKey);

            if (clientMessage.equals("1")) {
//                studentMarks.MakeCoiceToEntermarks();

//                if (isEntered) {
                List<Student> studentsWithMarks = studentMarks.EnterMarks();
//                System.out.println("isEntered----"+isEntered);
//                    System.out.println( "students marks listtttt----");
//                    studentMarks.getStudentsWithMarks(studentsWithMarks);
                    byte[] serializedMatrix = studentMarks.getmessageByte(studentsWithMarks);
                    byte[] signature = dsa.signMessage(serializedMatrix,privateKey);
                    dsa.verifySignature(serializedMatrix,signature,publicKey);
                    System.out.println("publicKey"+publicKey);
                    System.out.println("verification staaaate"+dsa.verifySignature(serializedMatrix,signature,publicKey));
                    System.out.println("signature arr"+Arrays.toString(signature));
                System.out.println("serializedMatrix arr"+Arrays.toString(serializedMatrix));

                System.out.println("array");
                studentMarks.getStudentsWithMarks(studentsWithMarks);
                System.out.println("array");

//                    studentMarks.getStudentsWithMarks(studentsWithMarks);
//                    byte[] signature = digitalSignatureExample.signMessage(privateKey,serializedMatrix);
//                    System.out.println("privateKey----"+privateKey);
//                    System.out.println("signature arr----"+ Arrays.toString(signature));
//                    System.out.println("publicKey----"+publicKey );
//                System.out.println("verifySignature----"+dsa.verifySignature(serializedMatrix,signature,publicKey) );

//                String SignatureStr = Base64.getEncoder().encodeToString(signature);
//                    System.out.println("Signature String----"+SignatureStr );
                //----------------------------------
                SendStudentsMarks(objectOutputStream,studentsWithMarks);
                SignaturByteList(objectOutputStream,signature);
                //----------------------------------

                // Convert List<Student> to a string representation
//                String studentsString = convertListToString(studentsWithMarks);
                // Send the string to the server
//                out.println(studentsString);
//                    out.println(SignatureStr);
//                    out.println(studentMarks.getStudentsWithMarks(studentsWithMarks));
//                }
            }

// Add a function to combine matrix and signature

            //send list of student's project
            sendList(objectOutputStream);
            if(clientMessage.equals("2")){
                //recieve ok message
                String ok = in.readLine();
                System.out.println("received Message : " + AsymmetricEncryption.decrypt(ok, sessionkey));            }



      /*      in.close();
            out.close();
            socket.close();*/
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    private static String convertListToString(List<Student> students) {
        // You can use a library like Jackson or Gson for a more robust conversion
        // Here's a simple example using toString() method of each Student
        StringBuilder stringBuilder = new StringBuilder();

        for (Student student : students) {
            stringBuilder.append(student.toString()).append(",");
        }

        return stringBuilder.toString();
    }
    private  static byte[] combineMatrixAndSignature(byte[] matrix, byte[] signature) {
        // Combine the matrix and signature as needed
        // For simplicity, you can concatenate them, but in practice, use a proper format.
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            outputStream.write(matrix);
            outputStream.write(signature);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputStream.toByteArray();
    }

    private static void SendStudentsMarks(ObjectOutputStream objectOutputStream,List<Student> stuList) throws Exception{
        System.out.println("is prof"+!StudentDaoImpl.isStudent(username));
        if (!StudentDaoImpl.isStudent(username)){
        objectOutputStream.writeObject(stuList);
        objectOutputStream.flush();
        System.out.println("Marks List sent to the server.");
    }}

    private static void SignaturByteList(ObjectOutputStream objectOutputStream,byte[] listByte) throws Exception{
        System.out.println("is prof--"+!StudentDaoImpl.isStudent(username));
        if (!StudentDaoImpl.isStudent(username)){
            objectOutputStream.writeObject(listByte);
            objectOutputStream.flush();
            System.out.println("sinature List sent to the server.");
        }}
    private static void sendList(ObjectOutputStream objectOutputStream) throws Exception {

        if (!StudentDaoImpl.isStudent(username)) return;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Now, Please enter you projects number : ");
        int n = scanner.nextInt();
        scanner.nextLine();
        List<String> projectsList = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            System.out.println("Enter your project number " + (i + 1) + " : ");
            String project = scanner.nextLine();
            projectsList.add(AsymmetricEncryption.encrypt(project, sessionKey));
        }
        // Send the List to the server
        objectOutputStream.writeObject(projectsList);
        objectOutputStream.flush();
        System.out.println("Projects List sent to the server.");
    }

    private static void getClientDetails(Socket socket) {
        clientIPAddress = socket.getLocalAddress().getHostAddress();
        clientPortNumber = socket.getLocalPort();
        System.out.println("IP Address: " + clientIPAddress + ", Port Number: " + clientPortNumber);
    }

    private static void processUserRole(String clientMessage, PrintWriter out, Scanner scanner) {


        switch (clientMessage) {
            case "1":
                professorClient.processProfessor(answer, out, scanner, clientIPAddress, clientPortNumber);
//                processProfessor(out, scanner);
                break;
            case "2":
                studentClient.processStudent(out, scanner, answer, clientIPAddress, clientPortNumber);
//                processStudent(out, scanner);
                break;
            default:
                System.out.println("Unexpected answer!");
                break;
        }
    }

    private static void processUserinfo(String clientMessage, PrintWriter out, Scanner scanner, int id_number, String name, String password) throws Exception {


        switch (clientMessage) {
            case "1":
                userInfo.ProfessorInfo(out, scanner, clientIPAddress, clientPortNumber, id_number, name, password);
                break;
            case "2":
                userInfo.StudentInfo(out, scanner, clientIPAddress, clientPortNumber, id_number, name, password);
                break;
            default:
                System.out.println("Unexpected answer!");
                break;
        }
    }


    public static PublicKey performHandshake(String publicKeyPath, BufferedReader in, PrintWriter out) throws Exception {
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

    }}

