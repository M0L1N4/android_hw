/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cripto;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;
import javax.security.auth.x500.X500Principal;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author Toni
 */
public class CreaPKCS12 {

    public void crearPkcs12(String path, String storeName, PrivateKey privateKey, 
            String aliasClave, String passwordStore, String passwordClave) 
            throws NoSuchAlgorithmException, IOException, CertificateEncodingException, 
            KeyStoreException, CertificateException {
       
        Properties prop = new Properties();
        InputStream entrada = null;

        try{
            // definim el nom del fitxer de propietats
            entrada = new FileInputStream("properties/dades.properties");
            // carreguem l'arxiu de propietats
            prop.load(entrada);
            
            
            // Aquesta classe ens crea un certificat X509        
            CreaCertificat c = new CreaCertificat();

            // Agafem la data del 7/11/2020 com a data d'expiraciÛ dels tres certificats
            Calendar cal = Calendar.getInstance();
            cal.set(2020, 11, 7);

            Date fechaExpiracionRoot = cal.getTime();
            Date fechaExpiracionC1 = cal.getTime();
            Date fechaExpiracionC2 = cal.getTime();


            Claves cla = new Claves();
            // Creem un parell de claus per cada X509
            KeyPair keyPairRoot = cla.generarClaves();
            KeyPair keyPairC1 = cla.generarClaves();
            KeyPair keyPairC2 = cla.generarClaves();

            // Les dades (noms indentificatius) de cada certificat van aixÌ
            X500Principal issuerRoot = new X500Principal(prop.getProperty("CERT_ROOT"));
            X500Principal nameC1 = new X500Principal(prop.getProperty("CERT_C1"));
            X500Principal nameC2 = new X500Principal(prop.getProperty("CERT_C2"));

            /* 
            * Generem el root. Amb data d'inici avui, d'expiraciÛ la donada, el parell 
            * de claus del root, la privada del root (COM ES DE LA CA, ES AUTOSIGNAT
            * I PER AIXO ES LA SEVA MATEIXA CLAU, i les dades del X509 i de qui el signa (ELL MATEIX)
            */
            X509Certificate root = c.generarCertificatX509(new Date(), fechaExpiracionRoot,
                    keyPairRoot, keyPairRoot.getPrivate(), issuerRoot, issuerRoot);

            /* 
            * Generem l'intermig. Amb data d'inici avui, d'expiraciÛ la donada, 
            * el parell de claus del c1, la privada del root (QUI SIGNA AL C1)
            * i les dades del X509 i de qui el signa (el ROOT) 
            */
            X509Certificate c1 = c.generarCertificatX509(new Date(), fechaExpiracionC1, 
                    keyPairC1, keyPairRoot.getPrivate(), nameC1, issuerRoot);

            /* 
            * Generem l'intermig. Amb data d'inici avui, d'expiraciÛ la donada, el parell 
            * de claus del c1, la privada del c1 (QUI SIGNA AL C2)
            * i les dades del X509 i de qui el signa (el C1)
            */
            X509Certificate c2 = c.generarCertificatX509(new Date(), fechaExpiracionC2, 
                    keyPairC2, keyPairC1.getPrivate(), nameC2, nameC1);

            // Creem la cadena de confianÁa c1--> c2
            Certificate[] outChain = {c2, c1};

            // Instanciem un keystore PKCS12
            KeyStore outStore = KeyStore.getInstance("PKCS12");

            // Amb el password que li hem passat
            outStore.load(null, passwordStore.toCharArray());

            /*
            * Entrem la clau amb l'alias que li enviem, i el password de la clau, i 
            * li fiquem la cadena de confian√ßa
            */
            outStore.setKeyEntry(aliasClave, privateKey, passwordClave.toCharArray(), outChain);

            // El guardem
            OutputStream outputStream = new FileOutputStream(path+prop.getProperty("SLASH")+storeName);
            outStore.store(outputStream, passwordStore.toCharArray());
            outputStream.flush();
            outputStream.close();

            // Guardem el certificat root a banda
            EntradaSortida e = new EntradaSortida();
            e.gravar(root, path+prop.getProperty("NOM_CERT"));
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
    }
}
