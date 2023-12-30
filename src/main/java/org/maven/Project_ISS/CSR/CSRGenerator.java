package org.maven.Project_ISS.CSR;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.pkcs.CertificationRequest;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.openssl.PEMWriter;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCS10CertificationRequestBuilder;
import org.bouncycastle.util.Strings;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;


public class CSRGenerator {
    public static KeyPair generateRSAKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }
    public static KeyPair createKeyPairFromKeyBytes(PrivateKey privateKeyBytes, PublicKey publicKeyBytes) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");


        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes.getEncoded());
        PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);


        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes.getEncoded());
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);


        return new KeyPair(publicKey, privateKey);
    }

    public static PKCS10CertificationRequest generateCSR(KeyPair keyPair, String commonName,
                                                         String organizationName, String organizationalUnit,
                                                         String locality, String state, String country)
            throws OperatorCreationException, IOException {
        X500Name subject = new X500Name(
                "CN=" + commonName +
                        ", O=" + organizationName +
                        ", OU=" + organizationalUnit +
                        ", L=" + locality +
                        ", ST=" + state +
                        ", C=" + country);

        SubjectPublicKeyInfo publicKeyInfo = SubjectPublicKeyInfo.getInstance(keyPair.getPublic().getEncoded());

        // Convert SubjectPublicKeyInfo to PublicKey using JcaPEMKeyConverter
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
        PublicKey publicKey = converter.getPublicKey(publicKeyInfo);

        // Note: You might want to replace "SHA256WithRSA" with the appropriate signature algorithm
        PKCS10CertificationRequestBuilder csrBuilder = new PKCS10CertificationRequestBuilder(subject, publicKeyInfo);
        return csrBuilder.build(new JcaContentSignerBuilder("SHA256WithRSA").build(keyPair.getPrivate()));
    }
    public static String convertToPEM(PKCS10CertificationRequest csr) throws IOException {
        try (StringWriter stringWriter = new StringWriter();
             PEMWriter pemWriter = new PEMWriter(stringWriter)) {
            pemWriter.writeObject(csr);
            pemWriter.flush();
            return stringWriter.toString();
        }
    }
    public static String extractNameFromCSR(String csr) {
        try {
            PemReader pemReader = new PemReader(new StringReader(csr));
            PemObject pemObject = pemReader.readPemObject();
            pemReader.close();

            ASN1InputStream asn1InputStream = new ASN1InputStream(pemObject.getContent());
            ASN1Primitive obj = asn1InputStream.readObject();
            asn1InputStream.close();

            CertificationRequest certificationRequest = CertificationRequest.getInstance(obj);


            X500Name subject = certificationRequest.getCertificationRequestInfo().getSubject();
            RDN[] rdns = subject.getRDNs(BCStyle.CN);

            if (rdns.length > 0) {
                return rdns[0].getFirst().getValue().toString();
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static PublicKey extractPublicKeyFromCSR(String csr) {
        try {
            PemReader pemReader = new PemReader(new StringReader(csr));
            PemObject pemObject = pemReader.readPemObject();
            pemReader.close();

            ASN1InputStream asn1InputStream = new ASN1InputStream(pemObject.getContent());
            ASN1Primitive obj = asn1InputStream.readObject();
            asn1InputStream.close();

            CertificationRequest certificationRequest = CertificationRequest.getInstance(obj);
            SubjectPublicKeyInfo publicKeyInfo = certificationRequest.getCertificationRequestInfo().getSubjectPublicKeyInfo();

            // Convert SubjectPublicKeyInfo to PublicKey using JcaPEMKeyConverter
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
            return converter.getPublicKey(publicKeyInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String extractPasswordFromCSR(String csr) {
        try {
            PemReader pemReader = new PemReader(new StringReader(csr));
            PemObject pemObject = pemReader.readPemObject();
            pemReader.close();

            ASN1InputStream asn1InputStream = new ASN1InputStream(pemObject.getContent());
            ASN1Primitive obj = asn1InputStream.readObject();
            asn1InputStream.close();

            CertificationRequest certificationRequest = CertificationRequest.getInstance(obj);


            X500Name subject = certificationRequest.getCertificationRequestInfo().getSubject();
            RDN[] rdns = subject.getRDNs(BCStyle.OU);

            if (rdns.length > 0) {
                return rdns[0].getFirst().getValue().toString();
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }}
}



