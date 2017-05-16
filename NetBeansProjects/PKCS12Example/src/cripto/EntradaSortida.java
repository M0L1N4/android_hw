/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cripto;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Toni
 */
public class EntradaSortida {
 
    public void gravar(X509Certificate cert, String path) throws 
            CertificateEncodingException, IOException
    {
    
        // Guardem en disc un X509

        FileOutputStream bOut;
        try {
            bOut = new FileOutputStream(path);
            bOut.write(cert.getEncoded());
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(EntradaSortida.class.getName()).log(Level.SEVERE, null, ex);
        }
            
            

        
    }
}
