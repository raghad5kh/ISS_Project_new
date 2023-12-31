package org.maven.Project_ISS.AES;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class AsymmetricEncryption {
    private static  String ALGORITHM = "AES";


    public static String encrypt(String plainText,String KEY) throws Exception {
        byte[] key = KEY.getBytes(StandardCharsets.UTF_8);
        SecretKeySpec secretKey = new SecretKeySpec(key, ALGORITHM);
     //   SecretKeySpec secretKey = new SecretKeySpec(KEY.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String decrypt(String encryptedText,String KEY) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(KEY.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
        return new String(decryptedBytes);
    }
    public static byte[] encryptByteList(byte[] plainBytes, String KEY) throws Exception {
        byte[] key = KEY.getBytes(StandardCharsets.UTF_8);
        SecretKeySpec secretKey = new SecretKeySpec(key, ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return cipher.doFinal(plainBytes);
    }

    public static byte[] decryptByteList(byte[] encryptedBytes, String KEY) throws Exception {
        byte[] key = KEY.getBytes(StandardCharsets.UTF_8);
        SecretKeySpec secretKey = new SecretKeySpec(key, ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return cipher.doFinal(encryptedBytes);
    }

//    public static String generateKey() throws NoSuchAlgorithmException {
//        byte[] secureRandomKeyBytes = new byte[32];
//        SecureRandom secureRandom = new SecureRandom();
//        secureRandom.nextBytes(secureRandomKeyBytes);
//        Key aesKey =new SecretKeySpec(secureRandomKeyBytes, ALGORITHM);
//        // Display the generated key
//        byte[] keyBytes = aesKey.getEncoded();
//        return Base64.getEncoder().encodeToString(keyBytes);
//    }

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
