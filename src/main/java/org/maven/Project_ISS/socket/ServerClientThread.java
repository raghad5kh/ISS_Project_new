package org.maven.Project_ISS.socket;

import org.maven.Project_ISS.AES.SymmetricEncryption;
import org.maven.Project_ISS.CSR.CSRGenerator;
import org.maven.Project_ISS.DCA.DCA;
import org.maven.Project_ISS.DigitalSignature.DSA;
import org.maven.Project_ISS.DigitalSignature.StudentInfo;
import org.maven.Project_ISS.DigitalSignature.StudentMarks;
import org.maven.Project_ISS.PGoodP.PrettyGoodPrivacy;
import org.maven.Project_ISS.dao.*;
import org.maven.Project_ISS.socket.AuthForms.LoginHandler;
import org.maven.Project_ISS.socket.AuthForms.PasswordHashing;
import org.maven.Project_ISS.socket.AuthForms.SignInHandler;
import java.io.*;
import java.net.Socket;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServerClientThread extends Thread {
    public static int client_type;
    private final Socket serverClient;
    private String sessionKey;
    private final int clientNo;

    private static String username;
    public static int id_number;
    public static boolean testforsign=false;
    public static PublicKey clientPublicKey;

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
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(serverClient.getOutputStream());
            DSA dsa=new DSA();

            String ClientIPAddress = in.readLine();
            System.out.println("ClientIPAddress: " + ClientIPAddress);
            int ClientPortNumber = Integer.parseInt(in.readLine());
            System.out.println("ClientPortNumber: " + ClientPortNumber);            // Read client request
            String request = in.readLine();
            StudentMarks studentMarks = new StudentMarks();
            System.out.println("Request: " + request);
            PublicKey publicKey_server= PrettyGoodPrivacy.readPublicKeyFromFile("keys\\server\\puSPerVerlic.txt");
            PrivateKey privateKey_server = PrettyGoodPrivacy.readPrivateKeyFromFile("keys\\server\\priSVerVerate.txt");

            // Handling client's request
            if (request.contains("LogIn")) {
                System.out.println(request);
                String name = in.readLine();
                System.out.println("Name: " + name);
                username = name;
                String password = in.readLine();
                System.out.println("Password: " + password);
                System.out.println("Client : " + name + " send request with IPAddress :" + ClientIPAddress + " and Port Number =" + ClientPortNumber);
                new LoginHandler(out, studentDao, professorDao).handleLogin(name, password);

            }


            if (request.contains("SignIn")) {
                PasswordHashing passwordHashing = new PasswordHashing();
                System.out.println(request);

                id_number = Integer.parseInt(in.readLine());
                System.out.println("id_number: " + id_number);
                String name = in.readLine();
                System.out.println("Name: " + name);
                username = name;
                String password = in.readLine();
                System.out.println("Password: " + password);
                String hashedPassword = passwordHashing.hashPassword(password);
                System.out.println("hashedPassword: " + hashedPassword);

                System.out.println("Client : " + name + " send request with IPAddress :" + ClientIPAddress + " and Port Number =" + ClientPortNumber);

                new SignInHandler(out, studentDao, professorDao).handleSignIn(id_number, name, hashedPassword);

///--------------------Level 2
                String key = studentDao.get_national_number(id_number);
                if (key == null) {
                    key = professorDao.get_national_number(id_number);
                }
                String address = in.readLine();
                System.out.println("address: " + address);
                String address_after_decrypt = SymmetricEncryption.decrypt(address, key);
                System.out.println("address_after_decrypt:"+address_after_decrypt);
                String phone_number = in.readLine();
                System.out.println("phone_number: " + phone_number);
                int phone_number_after_decrypt = Integer.parseInt(SymmetricEncryption.decrypt(phone_number, key));
                System.out.println("phone_number_after_decrypt:"+phone_number_after_decrypt);
                String mobile_number = in.readLine();
                System.out.println("mobile_number: " + mobile_number);
                int mobile_number_after_decrypt = Integer.parseInt(SymmetricEncryption.decrypt(mobile_number, key));
                System.out.println("mobile_number_after_decrypt:"+mobile_number_after_decrypt);
                int id = studentDao.get_id(name);
                if (id == 0) {
                    id = professorDao.get_id(name);
                    Professor professor = new Professor(id, name, hashedPassword, address_after_decrypt, phone_number_after_decrypt, mobile_number_after_decrypt);
                    professorDao.update(professor);
                } else {
                    Student student = new Student(id, name, hashedPassword, address_after_decrypt, phone_number_after_decrypt, mobile_number_after_decrypt);
                    studentDao.update(student);
                }
                String message = "Dear " + username + ",your information has been received";
                String message_after = SymmetricEncryption.encrypt(message, key);
                System.out.println("done information : " + message);
                out.println(message_after);
//                out.flush();
            }
            ///--------------------End of Level 2



            ///--------------------Level 3
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
            serverMessage = SymmetricEncryption.encrypt(serverMessage, sessionkey);
            out.println(serverMessage);

            if(client_type==1 && request.contains("LogIn")){
                out.println("Enter path your digital_certificate");
                System.out.println("Now, client send your digital_certificate");
                String path = in.readLine();
                String certificate_path = path +"\\digital_certificate.cer";
                X509Certificate digitalCertificate = DCA.readCertificateFromFile(certificate_path);
                System.out.println("digital_certificate\n"+digitalCertificate);
                System.out.println(DCA.isCertificateValid(digitalCertificate));
                String name = DCA.extractSubjectFromCertificate(digitalCertificate);
                if (DCA.isCertificateValid(digitalCertificate) && name.equals("CN=" +username)) {
                    out.println("The certificate is valid");
                } else {
                    out.println("The certificate is invalid");
                }
                out.println("Enter path your digital_certificate");
                System.out.println("Now, client send your digital_certificate");
                String path2 = in.readLine();
                String clientCertificate_path = path2 +"\\client_certificate.cer";
                X509Certificate clientCertificate = DCA.readCertificateFromFile(clientCertificate_path);
                System.out.println("digital_certificate\n"+clientCertificate);
                System.out.println(DCA.isCertificateValid(clientCertificate));
                String subject = DCA.extractSubjectFromCertificate(clientCertificate);
                System.out.println(subject);
                Pattern pattern = Pattern.compile("\\((.*?)\\)");
                Matcher matcher = pattern.matcher(subject);
                if (matcher.find()) {
                    String result = matcher.group(1);
                    System.out.println("result" + result);
                    Pattern pattern1 = Pattern.compile("CN=([^(]+)");
                    Matcher matcher2 = pattern1.matcher(subject);
                    System.out.println(subject);
                    System.out.println(matcher2.find());
                    String result2 = matcher2.group(1);
                    System.out.println(result2);
                    if (DCA.isCertificateValid(clientCertificate) && result2.equals(username)) {
                        out.println("The certificate is valid");
                        String permission = professorDao.get_permission("(" + result + ")");
                        System.out.println("permission_pro" + permission);
                        out.println(permission);
                        String nameTable = professorDao.get_nameTable(permission);
                        System.out.println(nameTable);
                        List<StudentInfo> studentInfo = professorDao.get_marks(nameTable);
                        StudentMarks studentMarks1 = new StudentMarks();
                        studentMarks1.getStudentsWithMarks(studentInfo);
                        SendStudentsMarks(objectOutputStream, studentInfo);

                    } else {
                        out.println("The certificate is invalid");
                    }
                }
            }

//-----------------
            if(client_type==1) {
                String receivedMessage = "Dear prof , " + username + " , your signed file have been received";
                //Professor
                String publicKeyString = professorDao.get_publicKey(username);
                PublicKey publicKey= PrettyGoodPrivacy.convertStringToPublicKey(publicKeyString);
                System.out.println("Client publicKey"+publicKey);
                List<StudentInfo> DecodedMarksList;
//                ------ NOT USED!
                List<StudentInfo> ReceivedMarksList= (List<StudentInfo>) objectInputStream.readObject();
//                ------ NOT USED!
// Variables TO BE STORED in DB----------------------------------------
                byte[] serializedMatrixFrimClient =(byte[]) objectInputStream.readObject();//1) Byte Array of Marks List
                // byte[] DecryptedserializedMatrixFrimClient= AsymmetricEncryption.decryptByteList(serializedMatrixFrimClient,sessionkey);
                DecodedMarksList=studentMarks.convertBytesToList(serializedMatrixFrimClient);//2) convert byte arr into list<StudentInfo>
                System.out.println("ReceivedMarksList");
                studentMarks.getStudentsWithMarks(DecodedMarksList);
                System.out.println(sessionkey);
                byte[] receivedSignatureByteList =(byte[]) objectInputStream.readObject();//3) signature byte arr decrypted
                byte[] receivedSignatureDecoded=  SymmetricEncryption.decryptByteList(receivedSignatureByteList,sessionkey);
                String receivedSignatureBase64 = Base64.getEncoder().encodeToString(receivedSignatureByteList);
                //verification of signature
                dsa.verifySignature(serializedMatrixFrimClient,receivedSignatureDecoded,publicKey);
                if ( dsa.verifySignature(serializedMatrixFrimClient,receivedSignatureDecoded,publicKey)) {
                    System.out.println("Professor Signature is VALID!.");
                    String EncryptedResponse= SymmetricEncryption.encrypt(receivedMessage,sessionkey);
                    out.println(EncryptedResponse);
                } else {
                    System.err.println("It is not possible to validate the signature.");
                }
                professorDao.save_level4Data(receivedMessage,receivedSignatureBase64);
                int id =professorDao.get_id_level4data(receivedSignatureBase64);
                System.out.println(id);
                for (StudentInfo student : DecodedMarksList) {
                    String name= student.getName();
                    int mark = student.getMarks();
                    professorDao.save_list_students_marks(name,mark,id);
                }



            }
// Variables TO BE STORED in DB----------------------------------------

            //-----------------
            if(client_type==2) {
                //receive Projects list from student
                List<String> projectsListEncoded = (List<String>) objectInputStream.readObject();
                List<String> projectList = new ArrayList<>();
                for (int i = 0; i < projectsListEncoded.size(); i++) {
                    String project = SymmetricEncryption.decrypt(projectsListEncoded.get(i), sessionkey);
                    projectList.add(project);
                    System.out.println("Project number " + (i + 1) + " : " + projectsListEncoded.get(i));
                    System.out.println("Project number " + (i + 1) + " : " + project);
                }
                String receivedMessage = "Dear student , " + username + " , your project has been received";
                out.println(SymmetricEncryption.encrypt(receivedMessage, sessionkey));
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
                String test = in.readLine();
                String sign = in.readLine();
                PublicKey publicKey = CSRGenerator.extractPublicKeyFromCSR(generatedCSR);
                String publicKey_fromDB = professorDao.get_publicKey(username);
                PublicKey publicKey_pro = PrettyGoodPrivacy.convertStringToPublicKey(publicKey_fromDB);
                String name = CSRGenerator.extractNameFromCSR(generatedCSR);
                boolean isSign = DSA.verify(publicKey_fromDB,sign,clientPublicKey);
                if(publicKey.equals(publicKey_pro) && professorDao.exist_account(name) && isSign) {
                    out.println("Received CSR without errors");
                }
                else {
                    out.println("There are errors in the information");
                }


                int a = (int) (Math.random() * 10) + 1;
                int b = (int) (Math.random() * 10) + 1;
                int x = (int) (Math.random() * 10) + 1;
                int c = a * x + b;

                String equation = a + "x + " + b + " = " + c;
                out.println(equation);
                int solution = Integer.parseInt(in.readLine());
                System.out.println("solution:" + solution);
                int correctSolution = (c - b) / a;
                String isCorrect ;

                KeyPair keyPair = DCA.createKeyPairFromKeyBytes(privateKey_server,publicKey_server);
                if (solution == correctSolution) {
                    isCorrect ="true";
                    out.println(isCorrect);
                    System.out.println("client solution is correct.");
                    X509Certificate digitalCertificate = DCA.generateDigitalCertificate(keyPair,username);
                    System.out.println("digitalCertificate"+digitalCertificate);
                    String certString = Base64.getEncoder().encodeToString(digitalCertificate.getEncoded());
                    String signature = DCA.sign(certString, keyPair.getPrivate());
                    System.out.println("Signature: " + signature);
                    boolean isVerified = DCA.verify(certString, signature, keyPair.getPublic());
                    System.out.println("Verification result: " + isVerified);
                    if (isVerified){
                        out.println(certString);
                        out.println(signature);
                    }

                } else {
                    isCorrect= "false";
                    out.println(isCorrect);
                    System.out.println("client solution is incorrect. The correct solution is: " + correctSolution);
                    out.println("Sorry, your solution is incorrect.");
                }
                int number_year = professorDao.get_numberYear(id_number);
               String permission= professorDao.get_symbol(number_year);
                System.out.println(permission);
               X509Certificate clientCertificate= DCA.generateClientCertificate(keyPair,username,permission);
                System.out.println("digitalCertificate"+clientCertificate);
                String certString = Base64.getEncoder().encodeToString(clientCertificate.getEncoded());
                String signature = DCA.sign(certString, keyPair.getPrivate());
                System.out.println("Signature: " + signature);
                boolean isVerified = DCA.verify(certString, signature, keyPair.getPublic());
                System.out.println("Verification result: " + isVerified);
                if (isVerified){
                    out.println(certString);
                    out.println(signature);
                }

            }


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
    private static void SendStudentsMarks(ObjectOutputStream objectOutputStream, List<StudentInfo> stuList) throws Exception{
            objectOutputStream.writeObject(stuList);
            objectOutputStream.flush();
            System.out.println("Marks List sent to the server.");
        }
    private PublicKey performHandshake(BufferedReader in, PrintWriter out) throws Exception {
        System.out.println(">> Handshake started ...");

        // Create input and output streams for communication
//        BufferedReader in = new BufferedReader(new InputStreamReader(serverClient.getInputStream()));
//        PrintWriter out = new PrintWriter(serverClient.getOutputStream(), true);

        // Receive the client's handshake message
        String clientMessage = in.readLine();

        System.out.println("Client public key: " + clientMessage);

         clientPublicKey = PrettyGoodPrivacy.convertStringToPublicKey(clientMessage);

        PublicKey serverPublicKey = PrettyGoodPrivacy.readPublicKeyFromFile("keys\\server\\puSPerVerlic.txt");
        String publicKeyToString = PrettyGoodPrivacy.convertPublicKeyToString(serverPublicKey);

        out.println(publicKeyToString);
        System.out.println("===============================================================================");

        return clientPublicKey;
    }

}