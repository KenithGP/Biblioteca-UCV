
package Modelo;

import Vista.Registro;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

public class BarraProgreso {

    private JProgressBar jpb_progreso;
    private JFrame loginFrame;  // Referencia al frame de login

  /**
     * Constructor de la clase BarraProgreso.
     * 
     * @param jpb_progreso Es el componente JProgressBar que muestra el progreso del inicio de sesión.
     * @param loginFrame Es la ventana o marco principal donde se encuentra el formulario de inicio de sesión.
     */
    public BarraProgreso(JProgressBar jpb_progreso, JFrame loginFrame) {
        this.jpb_progreso = jpb_progreso;  // Inicializa el componente de barra de progreso con el proporcionado.
        this.loginFrame = loginFrame;      // Inicializa la ventana de inicio de sesión con la proporcionada.
    }

    public void mostrar() {
        jpb_progreso.setVisible(true);
        Thread h = new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < 101; i++) {
                    try {
                        sleep(10);
                        jpb_progreso.setValue(i);
                        if (i == 100) {
                            Registro registro = new Registro();
                            registro.setVisible(true);
                            loginFrame.dispose(); // Cierra el frame de login
                        }
                    } catch (InterruptedException e) {
                        JOptionPane.showMessageDialog(null, "Error en la barra de progreso");
                    }
                }
            }
        };
        h.start();
    }

}

