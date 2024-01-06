package org.maven.Project_ISS.AES;
import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.*;
import java.util.Base64;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class SymmetricEncryption {
    private static final String ALGORITHM = "AES";


   public static String encrypt(String plaintext, String key) throws NoSuchAlgorithmException,
           NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException,
           IllegalBlockSizeException, BadPaddingException {
       // Generate a random IV (Initialization Vector)
       SecureRandom random = new SecureRandom();
       byte[] iv = new byte[12];
       random.nextBytes(iv);

       // Create the GCM parameter specification
       GCMParameterSpec parameterSpec = new GCMParameterSpec(128, iv);

       // Convert the key string to a SecretKey object
       SecretKey secretKey = new SecretKeySpec(key.getBytes(), "AES");

       // Create the cipher instance with GCM mode and AES algorithm
       Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
       cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);

       // Encrypt the plaintext
       byte[] ciphertext = cipher.doFinal(plaintext.getBytes());

       // Concatenate the IV and ciphertext
       byte[] result = new byte[iv.length + ciphertext.length];
       System.arraycopy(iv, 0, result, 0, iv.length);
       System.arraycopy(ciphertext, 0, result, iv.length, ciphertext.length);

       // Convert the encrypted data to a Base64-encoded string
       return Base64.getEncoder().encodeToString(result);
   }

    public static String decrypt(String ciphertext, String key) throws NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {
        // Decode the Base64-encoded ciphertext
        byte[] encryptedData = Base64.getDecoder().decode(ciphertext);

        // Extract the IV from the ciphertext
        byte[] iv = new byte[12];
        System.arraycopy(encryptedData, 0, iv, 0, iv.length);

        // Convert the key string to a SecretKey object
        SecretKey secretKey = new SecretKeySpec(key.getBytes(), "AES");

        // Create the GCM parameter specification
        GCMParameterSpec parameterSpec = new GCMParameterSpec(128, iv);

        // Create the cipher instance with GCM mode and AES algorithm
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec);

        // Decrypt the ciphertext
        byte[] decryptedText = cipher.doFinal(encryptedData, iv.length, encryptedData.length - iv.length);

        return new String(decryptedText);
    }
    public static byte[] encryptByteList(byte[] plaintextBytes, String keyBytes) throws NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {
        // Generate a random IV (Initialization Vector)
        SecureRandom random = new SecureRandom();
        byte[] iv = new byte[12];
        random.nextBytes(iv);

        // Create the GCM parameter specification
        GCMParameterSpec parameterSpec = new GCMParameterSpec(128, iv);

        // Convert the key bytes to a SecretKey object
        SecretKey secretKey = new SecretKeySpec(keyBytes.getBytes(), "AES");

        // Create the cipher instance with GCM mode and AES algorithm
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);

        // Encrypt the plaintext
        byte[] ciphertext = cipher.doFinal(plaintextBytes);

        // Concatenate the IV and ciphertext
        byte[] result = new byte[iv.length + ciphertext.length];
        System.arraycopy(iv, 0, result, 0, iv.length);
        System.arraycopy(ciphertext, 0, result, iv.length, ciphertext.length);

        return result;
    }

    public static byte[] decryptByteList(byte[] encryptedData, String keyBytes) throws NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {
        // Extract the IV from the encrypted data
        byte[] iv = new byte[12];
        System.arraycopy(encryptedData, 0, iv, 0, iv.length);

        // Convert the key bytes to a SecretKey object
        SecretKey secretKey = new SecretKeySpec(keyBytes.getBytes(), "AES");

        // Create the GCM parameter specification
        GCMParameterSpec parameterSpec = new GCMParameterSpec(128, iv);

        // Create the cipher instance with GCM mode and AES algorithm
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec);

        // Decrypt the ciphertext
        byte[] decryptedText = cipher.doFinal(encryptedData, iv.length, encryptedData.length - iv.length);

        return decryptedText;
    }
//    public static byte[] encryptByteList(byte[] plainBytes, String KEY) throws Exception {
//        byte[] key = KEY.getBytes(StandardCharsets.UTF_8);
//        SecretKeySpec secretKey = new SecretKeySpec(key, ALGORITHM);
//        Cipher cipher = Cipher.getInstance(ALGORITHM);
//        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
//        return cipher.doFinal(plainBytes);
//    }
//
//    public static byte[] decryptByteList(byte[] encryptedBytes, String KEY) throws Exception {
//        byte[] key = KEY.getBytes(StandardCharsets.UTF_8);
//        SecretKeySpec secretKey = new SecretKeySpec(key, ALGORITHM);
//        Cipher cipher = Cipher.getInstance(ALGORITHM);
//        cipher.init(Cipher.DECRYPT_MODE, secretKey);
//        return cipher.doFinal(encryptedBytes);
//    }

    public static String readNational_numberFromFile(String filePath) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine();
            if (line != null) {
                return line.trim();
            } else {
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String generateKey() {
        // Define the character set from which the random string will be composed
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz123456789";

        // Generate a random length for the string (between 5 and 15 characters, for example)
        int minLength = 32;
        int maxLength = 32;
        int randomLength = new Random().nextInt(maxLength - minLength + 1) + minLength;

        // Generate the random string
        StringBuilder randomStringBuilder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < randomLength; i++) {
            int index = random.nextInt(characters.length());
            randomStringBuilder.append(characters.charAt(index));
        }

        return randomStringBuilder.toString();
    }
}
