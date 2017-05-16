/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cripto;

import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.x509.X509V3CertificateGenerator;
import org.bouncycastle.x509.extension.SubjectKeyIdentifierStructure;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author Toni
 */
public class CreaCertificat {
    
    public X509Certificate generarCertificatX509(Date startDate, Date expiryDate, 
            KeyPair keyPair, PrivateKey clauPrivadaSignatura, X500Principal subjectName, 
            X500Principal issuer) throws IOException, CertificateParsingException {
        
        Properties prop = new Properties();
        InputStream entrada = null;

        try{
            // definim el nom del fitxer de propietats
            entrada = new FileInputStream("properties/dades.properties");
            // carreguem l'arxiu de propietats
            prop.load(entrada);
        
            try {
                // Generarador de certificats X509 v3
                X509V3CertificateGenerator certGen = new X509V3CertificateGenerator();

                // Algoritme de signatura (RSA amb SHA1 de hash)
                String signatureAlgorithm = prop.getProperty("ALG_SIGNAT");

                // Número de sèrie
                certGen.setSerialNumber(BigInteger.valueOf(Math.abs(new Random().nextLong())));

                // Signant
                certGen.setIssuerDN(issuer);

                // Dates
                certGen.setNotBefore(startDate);
                certGen.setNotAfter(expiryDate);

                // Nom del certificat
                certGen.setSubjectDN(subjectName);

                // Clau pública (la que certifica el X509)
                certGen.setPublicKey(keyPair.getPublic());
                certGen.setSignatureAlgorithm(signatureAlgorithm);

                certGen.addExtension(X509Extensions.SubjectKeyIdentifier, false,
                        new SubjectKeyIdentifierStructure(keyPair.getPublic()));

    //            KeyUsage keyUsage = new KeyUsage(KeyUsage.keyCertSign | KeyUsage.cRLSign);
    //            X509Extension extension = new X509Extension(true, new DEROctetString(keyUsage));

    //            certGen.addExtension(X509Extensions.KeyUsage, false,
    //                    new AuthorityKeyIdentifierStructure(keyPair.getPublic()));


                X509Certificate cert = certGen.generate(clauPrivadaSignatura, 
                        prop.getProperty("CLAU_SIGNAT"));   // la clau privada de QUI SIGNA

                return cert;

            } catch (CertificateEncodingException | IllegalStateException | 
                    NoSuchProviderException | NoSuchAlgorithmException | 
                    SignatureException | InvalidKeyException ex) {
                Logger.getLogger(CreaCertificat.class.getName()).log(Level.SEVERE, null, ex);
            }
        }        
        catch (IOException ex) {
        } finally {
            if (entrada != null) {
                try {
                    entrada.close();
                } catch (IOException e) {
                }
            }
        }
        return null;
    }
}
