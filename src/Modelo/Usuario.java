/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;


public class Usuario extends Persona {

    private String contrasenaHash;

    public Usuario(String nombre, String contrasena) {
        super(nombre); // Llama al constructor de Persona que solo necesita el nombre
        Hash hash = new Hash();
        this.contrasenaHash = hash.sha256(contrasena.toCharArray());
    }
    // Método para verificar si la contraseña ingresada es correcta
    public boolean verificarContrasena(String contrasena) {
        Hash hash = new Hash();
        String contrasenaHashVerificar = hash.sha256(contrasena.toCharArray());
        return contrasenaHashVerificar.equals(this.contrasenaHash);
    }

    // Getters y setters
    public String getNombre() {
        return super.getNombre();
    }

    public void setNombre(String Nombre) {
        super.setNombre(Nombre);
    }

    public String getContrasenaHash() {
        return contrasenaHash;
    }

}


