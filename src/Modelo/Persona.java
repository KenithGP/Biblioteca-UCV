
package Modelo;

import javax.swing.JOptionPane;


public class Persona {
    private String codigo;
    private String Nombre;
    private String Apellido;
    private String Dni;
    private String Celular;
    
    
    public Persona() {
    }
    
    public Persona(String Nombre) {
        this.Nombre=Nombre;
    }
    
    public Persona(String codigo, String Nombre, String Apellido, String Dni, String Celular) {
        this.codigo = codigo;
        this.Nombre = Nombre;
        this.Apellido = Apellido;
        this.Dni = Dni;
        this.Celular = Celular;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public String getApellido() {
        return Apellido;
    }

    public void setApellido(String Apellido) {
        this.Apellido = Apellido;
    }

    public String getDni() {
        return Dni;
    }

    public void setDni(String Dni) {
        this.Dni = Dni;
    }

    public String getCelular() {
        return Celular;
    }

    public void setCelular(String Celular) {
        this.Celular = Celular;
    }
    
    
}


