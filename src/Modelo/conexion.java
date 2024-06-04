
package Modelo;

import java.sql.DriverManager;

import java.sql.Connection;

import java.sql.SQLException;

public class conexion{
    private static final String URL = "jdbc:mysql://localhost:3306/pepito";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexión exitosa a la base de datos.");
        } catch (SQLException e) {
            System.out.println("Error al conectar: " + e.getMessage());
        }
        return conn;
    }

    public static void main(String[] args) {
        connect();
    }
}

