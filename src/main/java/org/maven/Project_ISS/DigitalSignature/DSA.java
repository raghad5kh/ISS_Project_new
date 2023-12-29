package org.maven.Project_ISS.DigitalSignature;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.RSAPrivateKey;
import java.util.Arrays;
import java.util.List;


//The Digital Signature Algorithm (DSA)
//The DSA algorithm involves four operations: key generation (which creates the key pair), key distribution, signing and signature verification.

public class DSA {
    public  byte[] signMessage(byte[] message, PrivateKey privateKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(message);
        return signature.sign();
    }

    public boolean verifySignature(byte[] message, byte[] signature, PublicKey publicKey) throws Exception {
        Signature verifier = Signature.getInstance("SHA256withRSA");
        verifier.initVerify(publicKey);
        verifier.update(message);
        return verifier.verify(signature);
    }}










/*public class DSA {
    public byte[] signProfMessage(byte[] message, PrivateKey privateKey)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        try {
            // Create a Signature instance with the appropriate algorithm
            Signature sig = Signature.getInstance("SHA256withRSA");

            // Initialize the Signature instance with the private key
            sig.initSign(privateKey);

            // Update the data to be signed
            sig.update(message);
            System.out.println("Signature update msg !"+message);
            System.out.println("Signature update mm !"+Arrays.toString(message));
            // Generate the signature
            byte[] signature = sig.sign();
            System.out.println("Signature has been generated successfully !");
            System.out.println("signature byte arr "+"\t"+ Arrays.toString(signature));

            // Return the signature
            return signature;

        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            // Handle exceptions appropriately
            e.printStackTrace(); // Consider logging or throwing a more specific exception
            throw e;
        }
    }


   /* public  byte[] signProfMessage(byte[] message,PrivateKey privateKey) throws NoSuchAlgorithmException,
            InvalidKeyException, SignatureException {
        Signature sig = Signature.getInstance("DSA");
        RSAPrivateKey rsaPrivateKey=(RSAPrivateKey) privateKey;
        sig.initSign(privateKey);
        sig.update(message);
        System.out.println("Done Signnnnnn"+privateKey);
        byte[] sign= sig.sign();
        return sign;
    }*/
    /*public boolean validateProfMessageSignature(PublicKey publicKey, List<Student> list, byte[] signature) throws
            NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature clientSig = Signature.getInstance("SHA256withRSA");
        System.out.println(" ----------pp:"+publicKey);
        clientSig.initVerify(publicKey);
//        System.out.println(" ----------mm:"+Arrays.toString(message));
//        System.out.println(" ----------ll:"+message);
//        System.out.println(" ----------ll:"+message.toString());
//
//
      clientSig.update(list.toString().getBytes());
        System.out.println(" ----------val:"+clientSig.verify(signature));
        if (clientSig.verify(signature)) {
            System.out.println("Professor Signature is VALID!.");
            return  true;
        } else {
            System.err.println("It is not possible to validate the signature.");
            return  false;
        }
    }}
*/