/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;
import javax.swing.JOptionPane;

public class Estudiante extends Persona {
 
    private String correo;
    private String estado;

    // Constructor con parámetros

public Estudiante(String correo, String estado, String codigo, String Nombre, String Apellido, String Dni, String Celular) {
        super(codigo, Nombre, Apellido, Dni, Celular);
    this.correo = correo;
    this.estado = estado;
}

    public Estudiante() {
        // Inicialización de campos con valores predeterminados o vacíos
      super("", "", "", "", "");
        this.correo = "";
        this.estado = "activo";
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getcodigo() {
        return super.getCodigo();
    }
    
    public void setcodigo(String codigo){
        super.setCodigo(codigo);
    }
    public String getNombre(){
        return super.getNombre();
    }
    public void setNombre(String Nombre){
        super.setNombre(Nombre);
    }
    public String getApellido(){
        return super.getApellido();
    }
    public void setApellido(String Apellido){
        super.setApellido(Apellido);
    }
    public String getDni(){
        return super.getDni();
    }
    public void setDni(String Dni){
        super.setDni(Dni);
    }
    public String getCelular(){
        return super.getCelular();
    }
    public void setCelular(String celular){
        super.setCelular(celular);
    }
    // Método para cargar los datos de un estudiante desde la base de datos
    public boolean cargarDatos(Connection conectar, String codigoAlumno) {
        String sql = "SELECT NOMBRE_ES, APELLIDO_ES, DNI, CORREO_ES, CELULAR_ES, ESTADO FROM ESTUDIANTE WHERE CODIGO_ES = ? AND ESTADO = 1";
        try {
            PreparedStatement preparedStatement = conectar.prepareStatement(sql);
            preparedStatement.setString(1, codigoAlumno);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                setNombre(resultSet.getString("NOMBRE_ES"));
                setApellido(resultSet.getString("APELLIDO_ES"));
                setDni(resultSet.getString("DNI"));
                setCorreo(resultSet.getString("CORREO_ES"));
                setCelular(resultSet.getString("CELULAR_ES"));
                setEstado(resultSet.getString("ESTADO"));
                return true; // Datos cargados correctamente
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Esto imprimirá el error en tu consola
            JOptionPane.showMessageDialog(null, "Error al buscar los datos: " + e.getMessage());
        }

        return false; // No se encontraron datos
    }

 
}
