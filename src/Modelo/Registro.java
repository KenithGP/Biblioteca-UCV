
package Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;

public class Registro extends Persona {
    private String Integrantes;
    private String TiempoInicio;
    private String TiempoFin;
    private String Observacion;
    private int salaSeleccionada;
    private Connection conectar;

     public Registro(String Codigo, String Nombre, String Apellido, String Celular, String Integrantes, String Dni, int salaSeleccionada, Connection conectar) {
        super(Codigo, Nombre, Apellido, Dni, Celular);
        this.Integrantes = Integrantes;
        this.salaSeleccionada = salaSeleccionada;
        this.conectar = conectar;
        
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        this.TiempoInicio = formatoFecha.format(new Date());
        this.TiempoFin = "--";
        this.Observacion = "--";
    }

    public String getCodigo() {
        return super.getCodigo();
    }

    public void setCodigo(String Codigo) {
        super.setCodigo(Codigo);
    }

    public String getNombre() {
        return super.getNombre();
    }

    public void setNombre(String Nombre) {
        super.setNombre(Nombre);
    }

    public String getApellido() {
        return super.getApellido();
    }

    public void setApellido(String Apellido) {
        super.setApellido(Apellido);
    }

    public String getCelular() {
        return super.getCelular();
    }

    public void setCelular(String Celular) {
       super.setCelular(Celular);
    }

    public String getDni() {
        return super.getDni();
    }

    public void setDni(String Dni) {
        super.setDni(Dni);
    } 
    
    public String getIntegrantes() {
        return Integrantes;
    }

    public void setIntegrantes(String Integrantes) {
        this.Integrantes = Integrantes;
    }

    public String getTiempoInicio() {
        return TiempoInicio;
    }

    public void setTiempoInicio(String TiempoInicio) {
        this.TiempoInicio = TiempoInicio;
    }

    public String getTiempoFin() {
        return TiempoFin;
    }

    public void setTiempoFin(String tiempoFin) {
        this.TiempoFin = TiempoFin;
    }

    public String getObservacion() {
        return Observacion;
    }

    // Getters y setters para cada atributo
    public void setObservacion(String observacion) {
        this.Observacion = Observacion;
    }

//    public void guardar(Connection conectar) {
//        String sql = "INSERT INTO ESTUDIANTE (CODIGO_ES, NOMBRE_ES, APELLIDO_ES, DNI, CORREO_ES ,CELULAR_ES,ESTADO , TIEMPOINI, TIEMPOFIN, OBSERVACION) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
//
//        try {
//            PreparedStatement preparedStatement = conectar.prepareStatement(sql);
//            preparedStatement.setString(1, getCodigo());
//            preparedStatement.setString(2, getNombre());
//            preparedStatement.setString(3, getApellido());
//            preparedStatement.setString(4, getDni());
//            preparedStatement.setString(5, getIntegrantes());
//            preparedStatement.setString(6, getCelular());
//            preparedStatement.setString(7, getTiempoInicio());
//            preparedStatement.setString(8, getTiempoFin());
//            preparedStatement.setString(9, getObservacion());
//
//            preparedStatement.executeUpdate();
//            JOptionPane.showMessageDialog(null, "Datos guardados correctamente!");
//            
//        } catch (SQLException es) {
//            JOptionPane.showMessageDialog(null, "Error al guardar los datos: " + es.getMessage());
//        }
//    }
//}
   public void Guardar() {
        String sqlE = "SELECT ID_ESTUDIANTE FROM ESTUDIANTE WHERE CODIGO_ES = ? AND ESTADO = 1";
        String sqlA = "SELECT ID_ADMINISTRADOR FROM USUARIO WHERE ESTADO = 1";  // Suponiendo que solo queremos administradores activos
        String sql = "INSERT INTO REGISTRO (ID_ESTUDIANTE, ID_SALA, ID_ADMINISTRADOR, TIEMPO_INICIO, OBSERVACION) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatementE = conectar.prepareStatement(sqlE)) {
            preparedStatementE.setString(1, super.getCodigo());
            ResultSet resultSetE = preparedStatementE.executeQuery();
            
            if (resultSetE.next()) {
                int idEstudiante = resultSetE.getInt("ID_ESTUDIANTE");
                
                try (PreparedStatement preparedStatementA = conectar.prepareStatement(sqlA);
                     ResultSet resultSetA = preparedStatementA.executeQuery()) {
                    
                    if (resultSetA.next()) {
                        int idAdministrador = resultSetA.getInt("ID_ADMINISTRADOR");
                        try (PreparedStatement preparedStatement = conectar.prepareStatement(sql)) {
                            preparedStatement.setInt(1, idEstudiante);
                            preparedStatement.setInt(2, salaSeleccionada);
                            preparedStatement.setInt(3, idAdministrador);
                            preparedStatement.setString(4, this.TiempoInicio);
                            preparedStatement.setString(5, this.Observacion);
                            preparedStatement.executeUpdate();
                         
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "No se encontró un administrador activo.");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "El estudiante no está habilitado o no coincide con la base de datos.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al guardar los datos: " + ex.getMessage());
            ex.printStackTrace(); // Para depuración
        }
    }
}


