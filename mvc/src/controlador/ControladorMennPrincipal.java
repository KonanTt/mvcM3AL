/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import modelo.ModeloPersona;
import vista.VistaPrincipal;
import vista.vistaPersonas;

/**
 *
 * @author Usuario
 */
public class ControladorMennPrincipal {

    VistaPrincipal vistaPrincipal;

    public ControladorMennPrincipal(VistaPrincipal vistaprincipal) {
        this.vistaPrincipal = vistaprincipal;
        vistaprincipal.setVisible(true);
    }

    public void iniciaControl() {
        vistaPrincipal.getMnuPersonas().addActionListener(l -> crudPersonas());
        vistaPrincipal.getBtnPersonas().addActionListener(l -> crudPersonas());
    }

    private void crudPersonas() {
        //Instancio las clases del Modelo y la Vista.
        vistaPersonas vista = new vistaPersonas();
        ModeloPersona modelo = new ModeloPersona();

        //Agregar Vista Personas al Desktop Pane.
        vistaPrincipal.getEscritorio().add(vista);

        ControladorPersonas control = new ControladorPersonas(modelo, vista);
        control.iniciarControl();//Empezamos las escuchas a los eventos de la vista, Listeners.
    }

    public void ControladorPrincipal(VistaPrincipal vistaPrincipal) {
        this.vistaPrincipal = vistaPrincipal;
        vistaPrincipal.setVisible(true);
    }
}
