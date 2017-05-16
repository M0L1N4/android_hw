/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cripto;

import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.cert.CertificateException;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author Toni
 */
public class Inicio {

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException,
            KeyStoreException, CertificateException {

        
        
        
        Properties prop = new Properties();
        InputStream entrada = null;
        
        try{
            // definim el nom del fitxer de propietats
//            prop.load(new FileInputStream("../../test/properties/dades.properties") );
            entrada = new FileInputStream("properties/dades.properties");
            // carreguem l'arxiu de propietats
            prop.load(entrada);
            
        // Ara farem una cadena de confiança PKCS12. Amb un certificat X509 
        // arrel que serà el de la CA i dos més, un de la autoritat intermitja i un d'usuari

        if (args.length != 5) {
            System.err.println(prop.getProperty("ERROR_PARAM"));
        }

        String path = args[0];
        String keyStore = args[1];
        String aliasClave = args[2];
        String passwordStore = args[3];
        String passwordClave = args[4];

        // Afegim el proveidor del bouncyCastle, el proveidor de la criptografia, 
        // al que hem de donar permis
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

        // Aquesta classe ens generarÃ  el magatzem pkcs12
        CreaPKCS12 p12 = new CreaPKCS12();

        // Aquesta classe ens generarÃ  un parell de claus RSA de 1024 bytes (privada-publica)
        Claves c = new Claves();

        // Generem un parell de claus (pel magatzem)
        KeyPair k = c.generarClaves();

        // Creem el magatzem pkcs12 a la ruta que donem, amb una clau privada, 
        // l'alias de la clau, el password del magatzem (keystore) i el password de la clau
        p12.crearPkcs12(path, keyStore, k.getPrivate(), aliasClave, passwordStore, passwordClave);
        }
        
        catch (IOException ex) {
            System.out.println(ex);
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
