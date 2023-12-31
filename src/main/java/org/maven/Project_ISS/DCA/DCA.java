package org.maven.Project_ISS.DCA;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.TimeZone;

public class DCA {


    public static KeyPair createKeyPairFromKeyBytes(PrivateKey privateKeyBytes, PublicKey publicKeyBytes) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");


        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes.getEncoded());
        PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);


        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes.getEncoded());
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);


        return new KeyPair(publicKey, privateKey);
    }

    public static String sign(String text, PrivateKey privateKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(text.getBytes());
        byte[] signatureBytes = signature.sign();
        return Base64.getEncoder().encodeToString(signatureBytes);
    }

    public static boolean verify(String text, String signature, PublicKey publicKey) throws Exception {
        Signature verifier = Signature.getInstance("SHA256withRSA");
        verifier.initVerify(publicKey);
        verifier.update(text.getBytes());
        byte[] signatureBytes = Base64.getDecoder().decode(signature);
        return verifier.verify(signatureBytes);
    }

    public static X509Certificate generateDigitalCertificate(KeyPair keyPair, String name) throws Exception {
        X500Name issuer = new X500Name("CN=MyCA");
        X500Name subject = new X500Name("CN=" + name);
        // BigInteger serial = BigInteger.valueOf(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        Date notBefore = dateFormat.parse("20230101000000Z"); // تاريخ البداية
        Date notAfter = dateFormat.parse("20241231235959Z"); // تاريخ الانتهاء

        BigInteger serial = BigInteger.valueOf(System.currentTimeMillis());
        JcaX509v3CertificateBuilder certBuilder = new JcaX509v3CertificateBuilder(
                issuer,
                serial,
                notBefore,
                notAfter,
                subject,
                keyPair.getPublic()
        );

        ContentSigner contentSigner = new JcaContentSignerBuilder("SHA256WithRSA").build(keyPair.getPrivate());
        X509CertificateHolder certHolder = certBuilder.build(contentSigner);

        return new JcaX509CertificateConverter().getCertificate(certHolder);
    }


    public static void saveCertificate(String filePath, X509Certificate certificate) throws Exception {
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(certificate.getEncoded());
        }
    }

    public static X509Certificate readCertificateFromFile(String filePath) throws Exception {
        try (FileInputStream fis = new FileInputStream(filePath)) {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            return (X509Certificate) certificateFactory.generateCertificate(fis);
        }
    }

    public static boolean isCertificateValid(X509Certificate certificate) {
        try {
            certificate.checkValidity(new Date());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}