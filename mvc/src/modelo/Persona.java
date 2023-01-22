package modelo;

import java.awt.Image;
import java.io.FileInputStream;
import java.sql.Date;

public class Persona {

    String idPersona;
    String nombre;
    String apellido;
    String sexo;
    Date fechaNacimiento;
    String telefono;
    double sueldo;
    int cupo;
    String correo;

    private FileInputStream foto;
    private int longitud;

    private Image imagen;


    public Persona() {
    }

    public Persona(String idPersona, String nombre, String apellido, String sexo, Date fechaNacimiento, String telefono, double sueldo, int cupo, String correo, FileInputStream foto, int longitud, Image imagen) {
        this.idPersona = idPersona;
        this.nombre = nombre;
        this.apellido = apellido;
        this.sexo = sexo;
        this.fechaNacimiento = fechaNacimiento;
        this.telefono = telefono;
        this.sueldo = sueldo;
        this.cupo = cupo;
        this.correo = correo;
        this.foto = foto;
        this.longitud = longitud;
        this.imagen = imagen;
    }

    public String getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(String idPersona) {
        this.idPersona = idPersona;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public double getSueldo() {
        return sueldo;
    }

    public void setSueldo(double sueldo) {
        this.sueldo = sueldo;
    }

    public int getCupo() {
        return cupo;
    }

    public void setCupo(int cupo) {
        this.cupo = cupo;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public FileInputStream getFoto() {
        return foto;
    }

    public void setFoto(FileInputStream foto) {
        this.foto = foto;
    }

    public int getLongitud() {
        return longitud;
    }

    public void setLongitud(int longitud) {
        this.longitud = longitud;
    }

    public Image getImagen() {
        return imagen;
    }

    public void setImagen(Image imagen) {
        this.imagen = imagen;
    }

   
}
 