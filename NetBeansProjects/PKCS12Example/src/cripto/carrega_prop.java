/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cripto;

import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author alvaro.molina
 */
public class carrega_prop {
    public Properties getProperties() throws IOException { 
        try { //se crea una instancia a la clase Properties 
            Properties propiedades = new Properties(); 
//se leen el archivo .properties 
propiedades.load( getClass().getResourceAsStream("properties/dades.properties") ); 
//si el archivo de propiedades NO esta vacio retornan las propiedes leidas 
        if (!propiedades.isEmpty()) 
        { return propiedades; 

        }else {//sino retornara NULL 
                return null; 
                } 
    }
     catch (IOException ex) 
    { 
        return null; 
    } 
}    
}
