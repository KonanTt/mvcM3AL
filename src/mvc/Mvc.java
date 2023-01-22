/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mvc;

import vista.VistaPersonas;
import modelo.ModeloPersona;
import controlador.ContradorPersona;


/**
 *
 * @author monge
 */
public class Mvc {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        //Instancial los 
        VistaPersonas vista = new VistaPersonas();
        ModeloPersona modelo = new ModeloPersona();
        
        ContradorPersona control = new ContradorPersona(modelo, vista);
        control.iniciarControl();
    }
    
}
