
package Modelo;


import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;

public class Conectar {
    public Connection Conectar() {
        Connection conect = null;
        String ipAddress = "db4free.net"; 
        String port = "3306"; // 
        String database = "bibliotecaucv"; 
        String username = "userbiblioteca"; // Nombre de usuario de MySQL
        String password = "112291210fd"; // Contraseña de MySQL
        String additionalParameters = "useSSL=false&serverTimezone=UTC"; // Parámetros adicionales si son necesarios

        try {
            // Asegúrate de tener el conector JDBC para MySQL en tu classpath
            Class.forName("com.mysql.cj.jdbc.Driver");
            conect = DriverManager.getConnection(
                "jdbc:mysql://" + ipAddress + ":" + port + "/" + database + "?" + additionalParameters,
                username,
                password
            );
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Hubo un error al establecer la conexión: " + e.getMessage());
            e.printStackTrace();
        }

        return conect;
    }

}
  





