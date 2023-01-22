/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.xml.ws.Holder;
import modelo.ModeloPersona;
import modelo.Persona;
import vista.vistaPersonas;

/**
 *
 * @author Usuario
 */
public class ControladorPersonas {

    private ModeloPersona modelo;
    private vistaPersonas vista;
    private JFileChooser jfc; //Objeto de tipo JFileChooser

    public ControladorPersonas(ModeloPersona modelo, vistaPersonas vista) {
        this.modelo = modelo;
        this.vista = vista;
        vista.setVisible(true);
        cargaPersonasTabla();//carga dtos
    }

    private void cargaPersonasTabla() {
        vista.getTbPersona().setDefaultRenderer(Object.class, new ImagenTabla());//La manera de renderizar la tabla.
        vista.getTbPersona().setRowHeight(100);

        DefaultTableModel tblModel;
        tblModel = (DefaultTableModel) vista.getTbPersona().getModel();
        tblModel.setNumRows(0);//limpio filas de la tabla.

        List<Persona> listap = modelo.listaPersonasTabla();//Enlazo al Modelo y obtengo los datos
        Holder<Integer> i = new Holder<>(0);//Contador para las filas. 'i' funciona dentro de una expresion lambda

        listap.stream().forEach(pe -> {

            tblModel.addRow(new Object[9]);//Creo una fila vacia
            vista.getTbPersona().setValueAt(pe.getIdPersona(), i.value, 0);
            vista.getTbPersona().setValueAt(pe.getNombre(), i.value, 1);
            vista.getTbPersona().setValueAt(pe.getApellido(), i.value, 2);
            vista.getTbPersona().setValueAt(pe.getSexo(), i.value, 3);
            vista.getTbPersona().setValueAt(pe.getFechaNacimiento(), i.value, 4);
            vista.getTbPersona().setValueAt(pe.getTelefono(), i.value, 5);
            vista.getTbPersona().setValueAt(pe.getCorreo(), i.value, 6);
            vista.getTbPersona().setValueAt(pe.getSueldo(), i.value, 7);
            vista.getTbPersona().setValueAt(pe.getCupo(), i.value, 8);

            Image foto = pe.getImagen();
            if (foto != null) {

                Image nimg = foto.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                ImageIcon icono = new ImageIcon(nimg);
                DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
                renderer.setIcon(icono);
                vista.getTbPersona().setValueAt(new JLabel(icono), i.value, 9);

            } else {
                vista.getTbPersona().setValueAt(null, i.value, 9);
            }

            i.value++;
        });
    }

    public void iniciarControl() {
        vista.getBtnActualizar().addActionListener(l -> cargaPersonasTabla());

        //jDialog
        /*vista.getBtnCrear().addActionListener(l -> abrirDialogo(1));
        vista.getBtnEditar().addActionListener(l -> abrirDialogo(2));*/
        //***
        vista.getBtnCrear().addActionListener(l -> abrirDialogCrear());
        vista.getBtnEditar().addActionListener(l -> abrirYCargarDatosEnElDialog());
        //***

        vista.getBtnAceptar().addActionListener(l -> crearEditarPersona());
        vista.getBtnEliminar().addActionListener(l -> eliminarPersona());
        vista.getBtnExaminar().addActionListener(l -> seleccionarFoto());
        buscarPersona();
    }

    //jDialog
    /*private void abrirDialogo(int ce) {
        String title;
        if (ce == 1) {
            title = "Crear nueva persona";
            vista.getDlgPersona().setName(title);
        } else {

            title = "Editar";
            vista.getDlgPersona().setName(title);
            cargarDatosPersona();
        }

        vista.getDlgPersona().setLocationRelativeTo(vista);
        vista.getDlgPersona().setSize(1100, 500);
        vista.getDlgPersona().setTitle(title);
        vista.getDlgPersona().setVisible(true);
    }*/
    //***
    public void abrirDialogCrear() {
        vista.getDlgPersona().setName("Crear nueva persona");
        vista.getDlgPersona().setLocationRelativeTo(vista);
        vista.getDlgPersona().setSize(1100, 500);
        vista.getDlgPersona().setTitle("Crear nueva persona");
        vista.getDlgPersona().setVisible(true);
    }
    //***

    private void crearEditarPersona() {
        if ("Crear nueva persona".equals(vista.getDlgPersona().getName())) {
            //INSERTAR
            String cedula = vista.getTxtIdentificacion().getText();
            String nombres = vista.getTxtNombres().getText();
            String apellidos = vista.getTxtApellidos().getText();
            String sexo = vista.getTxtSexo().getText();
            String telefono = vista.getTxtTelefono().getText();
            Date fecha = vista.getjFecha().getDate();
            double sueldo = Double.parseDouble(vista.getTxtSueldo().getText());
            int cupo = Integer.parseInt(vista.getTxtCupo().getText());
            String correo = vista.getTxtCorreo().getText();

            ModeloPersona persona = new ModeloPersona();
            persona.setIdPersona(cedula);
            persona.setNombre(nombres);
            persona.setApellido(apellidos);
            persona.setSexo(sexo);
            persona.setTelefono(telefono);

            java.sql.Date fechaSQL = new java.sql.Date(fecha.getTime());//Paso de util.Date a sql.Date
            persona.setFechaNacimiento(fechaSQL);
            persona.setSueldo(sueldo);
            persona.setCupo(cupo);
            persona.setCorreo(correo);

            if (vista.getLabelFoto().getIcon() == null) { //Verifico si el label esta vacio o no

                if (persona.crearPersonaNoFto()) {
                    vista.getDlgPersona().setVisible(false);
                    JOptionPane.showMessageDialog(vista, "Persona Creada Satisfactoriamente");
                } else {
                    JOptionPane.showMessageDialog(vista, "No se pudo crear la persona");
                }

            } else {

                //Foto
                try {

                    FileInputStream foto = new FileInputStream(jfc.getSelectedFile());
                    int longitud = (int) jfc.getSelectedFile().length();

                    persona.setFoto(foto);
                    persona.setLongitud(longitud);

                } catch (FileNotFoundException ex) {
                    Logger.getLogger(ControladorPersonas.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (persona.crearPersonaFoto()) {
                    vista.getDlgPersona().setVisible(false);
                    JOptionPane.showMessageDialog(vista, "Persona Creada Satisfactoriamente");
                } else {
                    JOptionPane.showMessageDialog(vista, "No se pudo crear la persona");
                }
            }

        } else {

            //EDITAR
            String cedula = vista.getTxtIdentificacion().getText();
            String nombres = vista.getTxtNombres().getText();
            String apellidos = vista.getTxtApellidos().getText();
            String sexo = vista.getTxtSexo().getText();
            String telefono = vista.getTxtTelefono().getText();
            Date fecha = vista.getjFecha().getDate();
            double sueldo = Double.parseDouble(vista.getTxtSueldo().getText());
            int cupo = Integer.parseInt(vista.getTxtCupo().getText());
            String correo = vista.getTxtCorreo().getText();

            ModeloPersona persona = new ModeloPersona();
            persona.setIdPersona(cedula);
            persona.setNombre(nombres);
            persona.setApellido(apellidos);
            persona.setSexo(sexo);
            persona.setTelefono(telefono);

            java.sql.Date fechaSQL = new java.sql.Date(fecha.getTime());
            persona.setFechaNacimiento(fechaSQL);
            persona.setSueldo(sueldo);
            persona.setCupo(cupo);
            persona.setCorreo(correo);

            if (vista.getLabelFoto().getIcon() == null) {
                if (persona.modificarPersonaNoFt()) {

                    vista.getDlgPersona().setVisible(false);
                    JOptionPane.showMessageDialog(vista, "Persona Modificada Satisfactoriamente");
                } else {
                    JOptionPane.showMessageDialog(vista, "No se pudo modificar la persona");
                }
            } else {

                //Foto
                try {

                    FileInputStream img = new FileInputStream(jfc.getSelectedFile());
                    int longitud = (int) jfc.getSelectedFile().length();
                    persona.setFoto(img);
                    persona.setLongitud(longitud);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(ControladorPersonas.class.getName()).log(Level.SEVERE, null, ex);
                }

                if (persona.modificarPersonaF()) {

                    vista.getDlgPersona().setVisible(false);
                    JOptionPane.showMessageDialog(vista, "Persona Modificada Satisfactoriamente");
                } else {
                    JOptionPane.showMessageDialog(vista, "No se pudo modificar la persona");
                }
            }
        }

        cargaPersonasTabla(); //Actualiza
    }

    public void eliminarPersona() {

        int fila = vista.getTbPersona().getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "Aun no ha seleccionado una fila");
        } else {

            int response = JOptionPane.showConfirmDialog(vista, "¿Seguro que desea eliminar la información?", "Confirme", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (response == JOptionPane.YES_OPTION) {

                String cedula;
                cedula = vista.getTbPersona().getValueAt(fila, 0).toString();

                if (modelo.eliminarPersona(cedula)) {
                    JOptionPane.showMessageDialog(null, "La persona fue eliminada exitosamente");
                    cargaPersonasTabla();
                } else {
                    JOptionPane.showMessageDialog(null, "La persona no se pudo eliminar");
                }
            }
        }

    }

    public void abrirYCargarDatosEnElDialog() {
        int seleccion = vista.getTbPersona().getSelectedRow();

        if (seleccion == -1) {
            JOptionPane.showMessageDialog(null, "Aun no ha seleccionado una fila");
        } else {

            String cedula = vista.getTbPersona().getValueAt(seleccion, 0).toString();
            modelo.listaPersonas().forEach((pe) -> {
                if (pe.getIdPersona().equals(cedula)) {

                    //Abre el jDialog y carga los datos en el jDialog
                    vista.getDlgPersona().setName("Editar");
                    vista.getDlgPersona().setLocationRelativeTo(vista);
                    vista.getDlgPersona().setSize(1100, 500);
                    vista.getDlgPersona().setTitle("Editar");
                    vista.getDlgPersona().setVisible(true);

                    vista.getTxtIdentificacion().setText(pe.getIdPersona());
                    vista.getTxtNombres().setText(pe.getNombre());
                    vista.getTxtApellidos().setText(pe.getApellido());
                    vista.getTxtSexo().setText(pe.getSexo());

                    vista.getTxtTelefono().setText(pe.getTelefono());
                    vista.getTxtCorreo().setText(pe.getCorreo());
                    vista.getjFecha().setDate(pe.getFechaNacimiento());
                    vista.getTxtSueldo().setText(String.valueOf(pe.getSueldo()));
                    vista.getTxtCupo().setText(String.valueOf(pe.getCupo()));
                    vista.getLabelFoto().setIcon(modelo.ConsultarFoto(cedula)); //Llamo al metodo 'ConsultarFoto' del modelo
                }
            });
        }
    }

    public void buscarPersona() {

        KeyListener eventoTeclado = new KeyListener() {//Crear un objeto de tipo keyListener(Es una interface) por lo tanto se debe implementar sus metodos abstractos

            @Override
            public void keyTyped(KeyEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void keyPressed(KeyEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void keyReleased(KeyEvent e) {

                vista.getTbPersona().setDefaultRenderer(Object.class, new ImagenTabla());//La manera de renderizar la tabla.
                vista.getTbPersona().setRowHeight(100);

                //Enlazar el modelo de tabla con mi controlador.
                DefaultTableModel tblModel;
                tblModel = (DefaultTableModel) vista.getTbPersona().getModel();
                tblModel.setNumRows(0);//limpio filas de la tabla.

                List<Persona> listap = modelo.buscarPersonas(vista.getTxtBuscar().getText());//Enlazo al Modelo y obtengo los datos
                Holder<Integer> i = new Holder<>(0);//contador para el no. fila
                listap.stream().forEach(pe -> {

                    tblModel.addRow(new Object[9]);//Creo una fila vacia/
                    vista.getTbPersona().setValueAt(pe.getIdPersona(), i.value, 0);
                    vista.getTbPersona().setValueAt(pe.getNombre(), i.value, 1);
                    vista.getTbPersona().setValueAt(pe.getApellido(), i.value, 2);
                    vista.getTbPersona().setValueAt(pe.getSexo(), i.value, 3);
                    vista.getTbPersona().setValueAt(pe.getFechaNacimiento(), i.value, 4);
                    vista.getTbPersona().setValueAt(pe.getTelefono(), i.value, 5);
                    vista.getTbPersona().setValueAt(pe.getCorreo(), i.value, 6);
                    vista.getTbPersona().setValueAt(pe.getSueldo(), i.value, 7);
                    vista.getTbPersona().setValueAt(pe.getCupo(), i.value, 8);

                    Image foto = pe.getImagen();
                    if (foto != null) {

                        Image nimg = foto.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                        ImageIcon icono = new ImageIcon(nimg);
                        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
                        renderer.setIcon(icono);
                        vista.getTbPersona().setValueAt(new JLabel(icono), i.value, 9);

                    } else {
                        vista.getTbPersona().setValueAt(null, i.value, 9);
                    }

                    i.value++;
                });
            }
        };

        vista.getTxtBuscar().addKeyListener(eventoTeclado); //"addKeyListener" es un metodo que se le tiene que pasar como argumento un objeto de tipo keyListener 
    }

    public void seleccionarFoto() {

        vista.getLabelFoto().setIcon(null);
        jfc = new JFileChooser();
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int estado = jfc.showOpenDialog(null);

        if (estado == JFileChooser.APPROVE_OPTION) {
            try {
                Image imagen = ImageIO.read(jfc.getSelectedFile()).getScaledInstance(vista.getLabelFoto().getWidth(), vista.getLabelFoto().getHeight(), Image.SCALE_DEFAULT);
                vista.getLabelFoto().setIcon(new ImageIcon(imagen));
                vista.getLabelFoto().updateUI();

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(vista, "Error: " + ex);
            }
        }
    }
}
