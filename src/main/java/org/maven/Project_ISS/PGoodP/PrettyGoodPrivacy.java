package org.maven.Project_ISS.PGoodP;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class PrettyGoodPrivacy {

    public static String encryptRSA(String plaintext, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String decryptRSA(String encryptedMessage, String privateKeyPath) throws Exception {
        PrivateKey privateKey=readPrivateKeyFromFile(privateKeyPath);
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedMessage);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes);
    }
    public static byte[] encryptRSA_ByteArray(byte[] ByteList, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(ByteList);
    }

    public static byte[] decryptRSA_ByteArray(byte[] encryptedBytes, String privateKeyPath) throws Exception {
        PrivateKey privateKey = readPrivateKeyFromFile(privateKeyPath);

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(encryptedBytes);
    }
    public static byte[] encryptRSA_Byte(byte[] ByteList, PublicKey publicKey) throws Exception {
        // Generate a symmetric key
        SecretKey secretKey = KeyGenerator.getInstance("AES").generateKey();

        // Use symmetric key to encrypt the data
        Cipher cipherAES = Cipher.getInstance("AES");
        cipherAES.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedData = cipherAES.doFinal(ByteList);

        // Use RSA to encrypt the symmetric key
        Cipher cipherRSA = Cipher.getInstance("RSA");
        cipherRSA.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedKey = cipherRSA.doFinal(secretKey.getEncoded());

        // Combine the encrypted data and encrypted key
        byte[] result = new byte[encryptedKey.length + encryptedData.length];
        System.arraycopy(encryptedKey, 0, result, 0, encryptedKey.length);
        System.arraycopy(encryptedData, 0, result, encryptedKey.length, encryptedData.length);

        return result;
    }

    public static byte[] decryptRSA_Byte(byte[] encryptedBytes, PrivateKey privateKey) throws Exception {
        // Split the combined array into encrypted key and encrypted data
     try   {
            int keySize = 256; // Assuming a 2048-bit RSA key
            byte[] encryptedKey = new byte[keySize / 8];
            byte[] encryptedData = new byte[encryptedBytes.length - encryptedKey.length];
            System.arraycopy(encryptedBytes, 0, encryptedKey, 0, encryptedKey.length);
            System.arraycopy(encryptedBytes, encryptedKey.length, encryptedData, 0, encryptedData.length);

            // Use RSA to decrypt the symmetric key
            Cipher cipherRSA = Cipher.getInstance("RSA");
            cipherRSA.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] decryptedKey = cipherRSA.doFinal(encryptedKey);

            // Use the decrypted symmetric key to decrypt the data
            SecretKey secretKey = new SecretKeySpec(decryptedKey, "AES");
            Cipher cipherAES = Cipher.getInstance("AES");
            cipherAES.init(Cipher.DECRYPT_MODE, secretKey);
            return cipherAES.doFinal(encryptedData);
        }
     catch (Exception e){
       e.printStackTrace();
       return e.toString().getBytes();
     }
    }
    public static void generateKeyPair(String privatePath , String publicPath) throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(1024);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        storePrivateKeyToFile(keyPair.getPrivate(),privatePath);
        storePublicKeyToFile(keyPair.getPublic(),publicPath);

//        return keyPair;
    }

    public static void storePublicKeyToFile(PublicKey publicKey, String filePath) throws Exception {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
        boolean ok = file.createNewFile();
        if (ok) {
//            String key=convertPublicKeyToString(publicKey);
            byte[] publicKeyBytes = publicKey.getEncoded();
//            TODO As encrypt
            Files.write(Paths.get(filePath), publicKeyBytes);
        }
    }

    private static void storePrivateKeyToFile(PrivateKey privateKey, String filePath) throws Exception {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
        boolean ok = file.createNewFile();
        if (ok) {
            byte[] publicKeyBytes = privateKey.getEncoded();
            Files.write(Paths.get(filePath), publicKeyBytes);
        }
    }

    public static PublicKey readPublicKeyFromFile(String filePath) throws Exception {
        byte[] publicKeyBytes = Files.readAllBytes(Paths.get(filePath));
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
        return KeyFactory.getInstance("RSA").generatePublic(keySpec);
    }

    public static PrivateKey readPrivateKeyFromFile(String filePath) throws Exception {
        byte[] privateKeyBytes = Files.readAllBytes(Paths.get(filePath));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA"); // Adjust as needed
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        return keyFactory.generatePrivate(keySpec);
    }
    public static String convertPublicKeyToString(PublicKey publicKey) {
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }
    public static PublicKey convertStringToPublicKey(String encodedPublicKeyString) throws NoSuchAlgorithmException, InvalidKeySpecException {
        // Convert the Base64-encoded string to a byte array
        byte[] decodedPublicKeyBytes = Base64.getDecoder().decode(encodedPublicKeyString);

        // Create a KeyFactory for RSA
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        // Create an X509EncodedKeySpec with the decoded public key bytes
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(decodedPublicKeyBytes);

        // Generate the PublicKey from the X509EncodedKeySpec
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
        return publicKey;

    }
    public static String privateKeyToString(PrivateKey privateKey) {
        byte[] privateKeyBytes = privateKey.getEncoded();
        return Base64.getEncoder().encodeToString(privateKeyBytes);
    }

    public static PrivateKey stringToPrivateKey(String privateKeyString) throws Exception {
        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyString);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA"); // Adjust as needed
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        return keyFactory.generatePrivate(keySpec);
    }

}
