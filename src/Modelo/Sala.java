
package Modelo;

public class Sala {
    private int cantidad;
    private String horaInicio;
    private String horaFin;
    private String observaciones;
    private Sala SalaAsociada;
    
    public Sala(int cantidad, String horaInicio, String horaFin, String observaciones) {
        this.cantidad = cantidad;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.observaciones = observaciones;
    }

    public Sala getSalaAsociada() {
        return SalaAsociada;
    }

    public void setSalaAsociada(Sala SalaAsociada) {
        this.SalaAsociada = SalaAsociada;
    }
    
    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    
}



