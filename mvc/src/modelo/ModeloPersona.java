package modelo;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.swing.ImageIcon;

/**
 *
 * @author monge
 */
public class ModeloPersona extends Persona {

    ConectPG conpg = new ConectPG();

    public ModeloPersona() {
    }

    public ModeloPersona(String idPersona, String nombre, String apellido, String sexo, Date fechaNacimiento, String telefono, double sueldo, int cupo, String correo, FileInputStream foto, int longitud, Image imagen) {
        super(idPersona, nombre, apellido, sexo, fechaNacimiento, telefono, sueldo, cupo, correo, foto, longitud, imagen);
    }

    public List<Persona> listaPersonas() {
        try {
            //Me retorna un "List" de "persona"
            List<Persona> lista = new ArrayList<>();

            String sql = "select idpersona,nombres,apellidos,fechanacimiento, telefono,sexo, sueldo, cupo, correo from persona";

            ResultSet rs = conpg.consulta(sql); //La consulta nos devuelve un "ResultSet"

            //Pasar de "ResultSet" a "List"
            while (rs.next()) {
                //Crear las instancias de las personas
                Persona persona = new Persona();

                //Todo lo que haga en la sentencia define como voy a extraer los datos
                persona.setIdPersona(rs.getString("idpersona"));
                persona.setNombre(rs.getString("nombres"));
                persona.setApellido(rs.getString("apellidos"));
                persona.setFechaNacimiento(rs.getDate("fechanacimiento"));
                persona.setTelefono(rs.getString("telefono"));
                persona.setSexo(rs.getString("sexo"));
                persona.setSueldo(rs.getDouble("sueldo"));
                persona.setCupo(rs.getInt("cupo"));
                persona.setCorreo(rs.getString("correo"));

                lista.add(persona);
            }

            rs.close();
            return lista;

        } catch (SQLException ex) {
            Logger.getLogger(ModeloPersona.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public List<Persona> listaPersonasTabla() {
        try {
            //Me retorna un "List" de "persona"
            List<Persona> lista = new ArrayList<>();

            String sql = "select * from persona";

            ResultSet rs = conpg.consulta(sql); //La consulta nos devuelve un "ResultSet"
            byte[] bytea;

            while (rs.next()) {
                //Crear las instancias de las personas
                Persona persona = new Persona();

                //Todo lo que haga en la sentencia define como voy a extraer los datos
                persona.setIdPersona(rs.getString("idpersona"));
                persona.setNombre(rs.getString("nombres"));
                persona.setApellido(rs.getString("apellidos"));
                persona.setFechaNacimiento(rs.getDate("fechanacimiento"));
                persona.setTelefono(rs.getString("telefono"));
                persona.setSexo(rs.getString("sexo"));
                persona.setSueldo(rs.getDouble("sueldo"));
                persona.setCupo(rs.getInt("cupo"));
                persona.setCorreo(rs.getString("correo"));
                bytea = rs.getBytes("foto");

                if (bytea != null) {

                    try {
                        persona.setImagen(obtenerImagen(bytea));
                    } catch (IOException ex) {
                        Logger.getLogger(ModeloPersona.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                lista.add(persona); //Agrego los datos a la lista
            }

            rs.close();
            return lista;

        } catch (SQLException ex) {
            Logger.getLogger(ModeloPersona.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public boolean crearPersonaNoFto() {

        String sql = "INSERT INTO persona(idpersona, nombres, apellidos, fechanacimiento, telefono, sexo, sueldo, cupo, foto, correo) VALUES ('" + getIdPersona() + "', '" + getNombre() + "', '" + getApellido() + "', '" + getFechaNacimiento() + "', '" + getTelefono() + "', '" + getSexo() + "'," + getSueldo() + ", " + getCupo() + ", null,'" + getCorreo() + "');";

        return conpg.accion(sql);
    }

    public boolean modificarPersonaNoFt() { //modificar 

        String sql = "UPDATE persona SET nombres='" + getNombre() + "', apellidos='" + getApellido() + "', fechanacimiento='" + getFechaNacimiento() + "', telefono='" + getTelefono() + "', sexo='" + getSexo() + "', sueldo=" + getSueldo() + ", cupo=" + getCupo() + ", foto=null" + ",correo='" + getCorreo() + "' WHERE idpersona = '" + getIdPersona() + "';";

        return conpg.accion(sql);
    }

    public boolean modificarPersonaF() {
        try {
            String sql;

            sql = "UPDATE persona SET nombres=?,apellidos=?,fechanacimiento=?,telefono=?,sexo=?,sueldo=?,cupo=?,foto=?,correo=? Where idpersona=?";
            PreparedStatement ps = conpg.getCon().prepareStatement(sql);
            ps.setString(1, getNombre());
            ps.setString(2, getApellido());
            ps.setDate(3, getFechaNacimiento());
            ps.setString(4, getTelefono());
            ps.setString(5, getSexo());
            ps.setDouble(6, getSueldo());
            ps.setInt(7, getCupo());
            ps.setBinaryStream(8, getFoto(), getLongitud());
            ps.setString(9, getCorreo());
            ps.setString(10, getIdPersona());
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ModeloPersona.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean eliminarPersona(String cedula) { //elimina 

        String sql = "DELETE FROM persona WHERE idpersona = '" + cedula + "';";

        return conpg.accion(sql);
    }

    public boolean crearPersonaFoto() {
        try {
            String sql;

            sql = "INSERT INTO persona (idpersona, nombres, apellidos, fechanacimiento, telefono, sexo, sueldo, cupo, foto, correo)";
            sql += "VALUES(?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement ps = conpg.getCon().prepareStatement(sql);
            ps.setString(1, getIdPersona());
            ps.setString(2, getNombre());
            ps.setString(3, getApellido());
            ps.setDate(4, getFechaNacimiento());
            ps.setString(5, getTelefono());
            ps.setString(6, getSexo());
            ps.setDouble(7, getSueldo());
            ps.setInt(8, getCupo());
            ps.setBinaryStream(9, getFoto(), getLongitud());
            ps.setString(10, getCorreo());
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ModeloPersona.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    private Image obtenerImagen(byte[] bytes) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        Iterator it = ImageIO.getImageReadersByFormatName("png");
        ImageReader reader = (ImageReader) it.next();
        Object source = bis;
        ImageInputStream iis = ImageIO.createImageInputStream(source);
        reader.setInput(iis, true);
        ImageReadParam param = reader.getDefaultReadParam();
        param.setSourceSubsampling(1, 1, 0, 0);
        return reader.read(0, param);
    }

    public ImageIcon ConsultarFoto(String cedula) {
        conpg.getCon();
        String sql = "select foto from \"persona\" where idpersona = '" + cedula + "';";
        ImageIcon foto;
        InputStream is;

        try {
            ResultSet rs = conpg.consulta(sql);
            while (rs.next()) {

                is = rs.getBinaryStream(1);

                BufferedImage bi = ImageIO.read(is);
                foto = new ImageIcon(bi);

                Image img = foto.getImage();
                Image newimg = img.getScaledInstance(118, 139, java.awt.Image.SCALE_SMOOTH);

                ImageIcon newicon = new ImageIcon(newimg);
                return newicon;
            }
        } catch (Exception ex) {
            return null;
        }
        return null;
    }

    public List<Persona> buscarPersonas(String cedula) {
        try {
            //Me retorna un "List" de "persona"
            List<Persona> lista = new ArrayList<>();

            String sql = "Select * from persona where idpersona like '" + cedula + "%'";

            //ConectPG conpg = new ConectPG();
            ResultSet rs = conpg.consulta(sql); //La consulta nos devuelve un "ResultSet"
            byte[] bytea;

            //Pasar de "ResultSet" a "List"
            while (rs.next()) {
                //Crear las instancias de las personas
                Persona persona = new Persona();

                //Todo lo que haga en la sentencia define como voy a extraer los datos
                persona.setIdPersona(rs.getString("idpersona"));
                persona.setNombre(rs.getString("nombres"));
                persona.setApellido(rs.getString("apellidos"));
                persona.setFechaNacimiento(rs.getDate("fechanacimiento"));
                persona.setTelefono(rs.getString("telefono"));
                persona.setSexo(rs.getString("sexo"));
                persona.setSueldo(rs.getDouble("sueldo"));
                persona.setCupo(rs.getInt("cupo"));
                bytea = rs.getBytes("foto");
                persona.setCorreo(rs.getString("correo"));

                if (bytea != null) {

                    try {
                        persona.setImagen(obtenerImagen(bytea));
                    } catch (IOException ex) {
                        Logger.getLogger(ModeloPersona.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                lista.add(persona); //Agrego los datos a la lista
            }

            //Cierro la conexion a la BD
            rs.close();
            //Retorno la lista
            return lista;
        } catch (SQLException ex) {
            Logger.getLogger(ModeloPersona.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
