package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import modelo.ModeloPersona;
import modelo.Persona;
import vista.VistaPersonas;

public class ContradorPersona {

    private ModeloPersona modelo;
    private VistaPersonas vista;

    public void iniciaControl(){
    
        vista.getBtnActualizar().addActionListener(l-> cargaPersonas());
        vista.getBtnCrear().addActionListener(l-> abrirDial(2));
        vista.getBtnEditar().addActionListener(l-> abrirDial(1));
    }
    
    public ContradorPersona(ModeloPersona modelo, VistaPersonas vista) {
        this.modelo = modelo;
        this.vista = vista;
        vista.setVisible(true);//Aprovecho el constructor para hacer visible la vista 
    }

    private void cargaPersonas() {
        //Control para consultar al modelo y luego en la vista.
        List<Persona> listap = modelo.listaPersonas(); //Llamo al metodo de listar persona del paquete modelo y agrego a otro list

        DefaultTableModel mTabla;
        mTabla = (DefaultTableModel) vista.getTbPersona().getModel(); //Me devueleve el 'modelo' y tengo que hacer un casting para pasarlo a "DefaultModelTable"
        mTabla.setNumRows(0);//Limpiar las filas de la tabla

        listap.stream().forEach(pe -> {

            //Intero el bucle con los datos extraidos desde la BD y agrego al array de tipo String "filanueva"
            String[] filanueva = {pe.getIdPersona(), pe.getNombre() + " " + pe.getApellido(), pe.getSexo(), String.valueOf(pe.getFechaDeNacimiento()), pe.getTelefono(), String.valueOf(pe.getSueldo()), String.valueOf(pe.getCupo())};
            mTabla.addRow(filanueva);

        });
    }

//    public void iniciarControl(){
//        vista.getBtnActualizar().addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//            }
//        });
//    }
    public void iniciarControl() {
        vista.getBtnActualizar().addActionListener(l -> cargaPersonas());
    }

    private void abrirDial(int ce) {
        String title;
        if (ce == 1) {
            title = "Crear nueva persona";
            vista.getDlgCrud().setName("crear");
        } else {
            title = "Ediat persona";
            vista.getDlgCrud().setName("editar");
        }
        vista.getDlgCrud().setLocationRelativeTo(vista);
        vista.getDlgCrud().setSize(400,300);
        vista.getDlgCrud().setTitle(title);
        vista.getDlgCrud().setVisible(true);
    }
    
    private void crearEditarPersona(){
    
    }
    
}
