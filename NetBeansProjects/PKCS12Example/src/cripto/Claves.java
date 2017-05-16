/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cripto;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

import java.io.IOException;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author Toni
 */
public class Claves {

    public KeyPair generarClaves() throws NoSuchAlgorithmException {
      
        Properties prop = new Properties();
        InputStream entrada = null;
        try{
            // definim el nom del fitxer de propietats
            entrada = new FileInputStream("properties/dades.properties");
            // carreguem l'arxiu de propietats
            prop.load(entrada);
        
            // Generem un parell de claus (pública-privada RSA de 1024 bytes)

            KeyPairGenerator keyGen = KeyPairGenerator.getInstance(prop.getProperty("PARELL_CLAUS"));
            keyGen.initialize(1024);
            KeyPair keyPair = keyGen.generateKeyPair();
            
            return keyPair;
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
