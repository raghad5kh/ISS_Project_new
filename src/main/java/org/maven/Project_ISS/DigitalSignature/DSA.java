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









