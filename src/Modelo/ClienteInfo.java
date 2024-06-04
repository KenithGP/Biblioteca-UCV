package Modelo;

import java.net.Socket;

public class ClienteInfo {
    public Socket socket;
    private int id;

    public ClienteInfo(Socket socket, int id) {
        this.socket = socket;
        this.id = id;
    }

    // Getters y setters
    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
