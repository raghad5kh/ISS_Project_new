
package org.maven.Project_ISS.socket;

import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.maven.Project_ISS.AES.SymmetricEncryption;
import org.maven.Project_ISS.CSR.CSRGenerator;
import org.maven.Project_ISS.DigitalSignature.*;
import org.maven.Project_ISS.DigitalSignature.StudentInfo;
import org.maven.Project_ISS.PGoodP.PrettyGoodPrivacy;
import org.maven.Project_ISS.dao.ProfessorDaoImpl;
import org.maven.Project_ISS.dao.StudentDaoImpl;
import org.maven.Project_ISS.socket.ClientComponents.ProfessorClient;
import org.maven.Project_ISS.socket.ClientComponents.StudentClient;
import org.maven.Project_ISS.socket.ClientComponents.UserInfo;
import org.maven.Project_ISS.socket.ClientComponents.commonDetails;
import org.maven.Project_ISS.DCA.DCA;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.sql.SQLException;
import java.util.*;


public class TCPClient {

    public static int type = 0;
    //    static void setType()
    static String answer = "";
    public static  boolean isEntered;
    public static  List<StudentInfo> studentList = new ArrayList<>();

    public static boolean isValid=true;
   public static String clientIPAddress;
  public   static int clientPortNumber;
    static ProfessorClient professorClient = new ProfessorClient();
    static StudentClient studentClient = new StudentClient();
    static commonDetails commonDetails = new commonDetails();

    public static String username;
    public static String PS;
    public static String address_pro;
    public static int mobile_number;
    public static int phone_number;
    static UserInfo userInfo = new UserInfo();

    private static String sessionKey;

    private static String getUserInput(String prompt) {
        System.out.print(prompt);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            return reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        StudentMarks studentMarks = new StudentMarks();
        String publicKeyPath = "";
        String privateKeyPath = "";
        int id_number;
        String national_number;
        DSA dsa = new DSA();
        Socket socket = null;
        try {
            String ipAddress = getUserInput("Enter IP address: ");
            int portNumber = Integer.parseInt(getUserInput("Enter port number: "));
            InetAddress address = InetAddress.getByName(ipAddress);
            // socket constructor of type (InetAdress, portnumber)
            socket = new Socket(address, portNumber);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String clientMessage = "";


            System.out.println("Enter your role in this SYSTEM!\n" +
                    "1. Professor\n" +
                    "2. Student");

            clientMessage = br.readLine();
            System.out.println("User entered for role: " + clientMessage);

            out.println(ipAddress);
            out.println(portNumber);
            processUserRole(clientMessage, out, in, scanner);
            String serverMessage = in.readLine();
            System.out.println("Server response: " + serverMessage);
            String compareString = "Your SignIn has been done successfully.";

            System.out.println("---- isEntered----: " + isEntered);
            if (isEntered == true) {
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

                    String key;
                    if (clientMessage.equals("1")) {
                        key = SymmetricEncryption.readNational_numberFromFile("national_numbers\\client\\Professor\\national_number_" + id_number + ".txt");
                    } else {
                        key = SymmetricEncryption.readNational_numberFromFile("national_numbers\\client\\Student\\national_number_" + id_number + ".txt");
                    }
                    national_number = key;
                    String serverMessage3_after_decrypt = SymmetricEncryption.decrypt(serverMessage3, key);
                    System.out.println("Server response: " + serverMessage3);
                    System.out.println("Server response after decrypt: " + serverMessage3_after_decrypt);
                    System.out.println("--start generate kesy --");
                    //----------------------------------------End level 1&2



                    //-----------------------------Level 3
                    // generate pair key
                    publicKeyPath = "keys\\client\\pu" + id_number + "PL" + key + "ic.txt";
                    privateKeyPath = "keys\\client\\pri" + id_number + "V" + key + "ate.txt";
                    PrettyGoodPrivacy.generateKeyPair(privateKeyPath, publicKeyPath);
                } else {
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
                String sessionkey = SymmetricEncryption.generateKey();
                sessionKey = sessionkey;
                System.out.println("sessionkey : " + sessionkey);

                String sessionkeyEncrypte = PrettyGoodPrivacy.encryptRSA(sessionkey, serverPublicKey);
                out.println(sessionkeyEncrypte);
                //receive ok message
                String okresponce = in.readLine();
                String okresponceAfter = SymmetricEncryption.decrypt(okresponce, sessionkey);
                System.out.println("ok responce : " + okresponceAfter);
                if (clientMessage.equals("1") && type == 1) {
                    String message1 = in.readLine();
                    System.out.println(message1);
                    String path = scanner.next();
                    out.println(path);
                    String message2 = in.readLine();
                    System.out.println(message2);
                    if (message2.equals("The certificate is invalid")) {
                        isValid = false;
                    }
                    String messageFinal = in.readLine();
                    System.out.println(messageFinal);
                    String path_pro = scanner.next();
                    out.println(path_pro);
                    String ok = in.readLine();
                    System.out.println(ok);
                    if (ok.equals("The certificate is invalid")) {
                        isValid = false;
                    } else {
                        String message_permission = in.readLine();
                        System.out.println("Your permission on the server is:" + message_permission);
                        List<StudentInfo> ReceivedMarksList = (List<StudentInfo>) objectInputStream.readObject();
                        StudentMarks studentMarks1 = new StudentMarks();
                        System.out.println("List of marks");
                        studentMarks1.getStudentsWithMarks(ReceivedMarksList);
                    }
                }
                if (isValid == true) {
                    PrivateKey privateKey = PrettyGoodPrivacy.readPrivateKeyFromFile(privateKeyPath);
                    String PrivateKeyAsString = PrettyGoodPrivacy.privateKeyToString(privateKey);
                    PublicKey publicKey = PrettyGoodPrivacy.readPublicKeyFromFile(publicKeyPath);
                    System.out.println("publicKey" + publicKey);



                    //---------------Question 4-------------
                    if (clientMessage.equals("1")) {
                        List<StudentInfo> studentsWithMarks = studentMarks.EnterMarks();
                        byte[] serializedMatrix = studentMarks.convertListToBytes(studentsWithMarks);
                        byte[] signature = dsa.signMessage(serializedMatrix, privateKey);
                        System.out.println("Client publicKey =" + "\t" + publicKey);
                        byte[] signatureEncrypted = SymmetricEncryption.encryptByteList(signature, sessionkey);
                        SendStudentsMarks(objectOutputStream, studentsWithMarks);
                        SignaturByteList(objectOutputStream, serializedMatrix);
                        SignaturByteList(objectOutputStream, signatureEncrypted);
                        String ok1 = in.readLine();
                        String DecryptedResponse = SymmetricEncryption.decrypt(ok1, sessionkey);
                        System.out.println("Server response: " + DecryptedResponse);
                        System.out.print("Enter the directory path to save server response: ");
                        String directoryPath = scanner.next();
                        String fileName = "output.txt";
                        FileHandler.saveTextToFile(directoryPath, fileName, DecryptedResponse);

                        //----------------------------------
                    }
                    if (clientMessage.equals("2")) {
                        sendList(objectOutputStream);
                        //recieve ok message
                        String ok = in.readLine();
                        System.out.println("received Message : " + SymmetricEncryption.decrypt(ok, sessionkey));
                    }

                    if (clientMessage.equals("1") && type==2) {
                            sendCSR(id_number, username, PS,address_pro,phone_number,mobile_number, publicKeyPath, privateKeyPath, out);
                            String ok1 = in.readLine();
                            System.out.println("Server response: " + ok1);
                            String equation = in.readLine();
                            System.out.println(equation);
                            System.out.print("Enter your solution for x: ");
                            String userSolution = String.valueOf(scanner.nextInt());
                            out.println(userSolution);
                            String isCorrect = in.readLine();
                            if (isCorrect.equals("true")) {
                                String digital_certificate = in.readLine();
                                String Signature_sever = in.readLine();
                                System.out.println("digital_certificate\n" + digital_certificate);
                                byte[] decodedBytes = Base64.getDecoder().decode(digital_certificate);
                                CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
                                X509Certificate certificate = (X509Certificate) certificateFactory.generateCertificate(new ByteArrayInputStream(decodedBytes));
                                System.out.println("certificate" + certificate);
                                System.out.println(DCA.verify(digital_certificate, Signature_sever, serverPublicKey));
                                if (DCA.verify(digital_certificate, Signature_sever, serverPublicKey)) {
                                    System.out.println("The digital certificate is valid");
                                    System.out.println("Enter path to save digital_certificate");
                                    String path = scanner.next();
                                    String certificate_path = path + "\\digital_certificate.cer";
                                    System.out.println(certificate_path);
                                    DCA.saveCertificate(certificate_path, certificate);
                                } else {
                                    System.out.println("The certificate was not saved because it is invalid");
                                }

                            } else {
                                String error = in.readLine();
                                System.out.println("Server response: " + error);
                            }
                            String client_certificate = in.readLine();
                            String Signature_sever = in.readLine();
                            System.out.println("client_certificate\n" + client_certificate);
                            byte[] decodedBytes = Base64.getDecoder().decode(client_certificate);
                            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
                            X509Certificate certificate = (X509Certificate) certificateFactory.generateCertificate(new ByteArrayInputStream(decodedBytes));
                            System.out.println("certificate" + certificate);
                            System.out.println(DCA.verify(client_certificate, Signature_sever, serverPublicKey));
                            if (DCA.verify(client_certificate, Signature_sever, serverPublicKey)) {
                                System.out.println("The client certificate is valid");
                                System.out.println("Enter path to save digital_certificate");
                                String path = scanner.next();
                                String certificate_path = path + "\\client_certificate.cer";
                                System.out.println(certificate_path);
                                DCA.saveCertificate(certificate_path, certificate);
                            } else {
                                System.out.println("The certificate was not saved because it is invalid");
                            }


                        }



                        }


            }
        } catch (IOException e) {
        } catch (InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (CertificateException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
        }
    }


    private static void SendStudentsMarks(ObjectOutputStream objectOutputStream,List<StudentInfo> stuList) throws Exception{
        if (!StudentDaoImpl.isStudent(username)){
            objectOutputStream.writeObject(stuList);
            objectOutputStream.flush();
            System.out.println("Marks List sent to the server.");
        }}

    private static void SignaturByteList(ObjectOutputStream objectOutputStream,byte[] listByte) throws Exception{
        if (!StudentDaoImpl.isStudent(username)){
            objectOutputStream.writeObject(listByte);
            objectOutputStream.flush();
            System.out.println("sinature List sent to the server.");
        }}
    private static void sendCSR(int id_number,String name,String passwordd,String address,int phone_number,int mobile_number,String publickeypath,String privatekeypath,PrintWriter out) throws Exception{
        System.out.println("Now, send CSR to sever : ");
        try {
            PublicKey publicKey= PrettyGoodPrivacy.readPublicKeyFromFile(publickeypath);
            PrivateKey privateKey= PrettyGoodPrivacy.readPrivateKeyFromFile(privatekeypath);
            KeyPair keyPair = CSRGenerator.createKeyPairFromKeyBytes(privateKey,publicKey);
            String publickeyString = PrettyGoodPrivacy.convertPublicKeyToString(publicKey);
            String sign = DSA.sign(publickeyString,privateKey);

            String Name = name;
            String Id_number = String.valueOf(id_number);
            String password = passwordd;
            String pro_address = address;
            String pro_phoneNumber = String.valueOf(phone_number);
            String pro_mobileNumber = String.valueOf(mobile_number);

            PKCS10CertificationRequest csr = CSRGenerator.generateCSR(
                    keyPair, Name, Id_number, password, pro_address, pro_phoneNumber, pro_mobileNumber);


            String csrPEM = CSRGenerator.convertToPEM(csr);
            out.println(csrPEM);
            out.println("");
            out.println(sign);





        } catch (Exception e) {
            e.printStackTrace();
        }
    }
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
            projectsList.add(SymmetricEncryption.encrypt(project, sessionKey));
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

    private static void processUserRole(String clientMessage, PrintWriter out,BufferedReader in, Scanner scanner) {


        switch (clientMessage) {
            case "1":
                professorClient.processProfessor(answer, out,in, scanner, clientIPAddress, clientPortNumber);
                break;
            case "2":
                studentClient.processStudent(out, scanner, answer, clientIPAddress, clientPortNumber);
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
