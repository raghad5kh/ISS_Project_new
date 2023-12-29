//package org.maven.Project_ISS.DigitalSignature;
//
//import java.security.*;
//import java.security.spec.ECGenParameterSpec;
//import java.security.spec.InvalidKeySpecException;
//import java.security.spec.PKCS8EncodedKeySpec;
//import java.security.spec.X509EncodedKeySpec;
//import java.util.Arrays;
//
//public class DigitalSignatureExample {
//
//    // Step 1: Generating Asymmetric Keys
////    public  KeyPair generateKeyPair() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
////        KeyPairGenerator keyPairGenerator;
//////        if ("Ed25519".equals(algorithm)) {
////            keyPairGenerator = KeyPairGenerator.getInstance("Ed25519");
//////        }
//////        else {
//////            keyPairGenerator = KeyPairGenerator.getInstance("EC");
//////            ECGenParameterSpec ecGenParameterSpec = new ECGenParameterSpec("secp384r1");
//////            keyPairGenerator.initialize(ecGenParameterSpec);
//////        }
////        return keyPairGenerator.generateKeyPair();
////    }
//
//    // Step 2: Generating Signature on sender side
//    // Step 2: Generating Signature on sender side
//    public  byte[] signMessage( PrivateKey privateKey, byte[] message) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
//        Signature signature;
//        Security.addProvider(new BouncyCastleProvider());
//// Use "BC" as the provider name when creating the signature instance
//        try {
//            signature = Signature.getInstance("SHA512withEdDSA", "BC");
//        } catch (NoSuchProviderException e) {
//            throw new RuntimeException(e);
//        }
//        signature.initSign(privateKey);
//        signature.update(message);
//        System.out.println("signature privateKey"+privateKey);
//        System.out.println("signature updated msg"+ Arrays.toString(message));
//        return signature.sign();
//    }
//
//    // Step 4 & 5: Verifying Signature on Receiver Side
//    public  boolean verifySignature( PublicKey publicKey, byte[] message, byte[] signatureToVerify) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
//        Signature verifySignature;
//        verifySignature = Signature.getInstance("SHA512withEdDSA");
//        verifySignature.initVerify(publicKey);
//        verifySignature.update(message);
//        System.out.println("signature publicKey"+publicKey);
//        System.out.println("signature updated msg"+ Arrays.toString(message));
//        return verifySignature.verify(signatureToVerify);
//    }
//
//}
