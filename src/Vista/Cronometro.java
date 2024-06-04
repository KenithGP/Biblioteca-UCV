package Vista;

import Modelo.Conectar;
import Vista.Registro.EstadoSala;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class Cronometro extends javax.swing.JFrame { 
    
    Conectar conect = new Conectar();
    Connection conectar = conect.Conectar();
    // Arrays para almacenar los cronómetros, sus tiempos y las etiquetas asociadas.
    private Timer[] cronometros = new Timer[11];
    private int[] segundos = new int[11];
    private JLabel[] jLabels = new JLabel[11];
    private final int TIEMPO_INICIAL = 30;//tiempo en segundos
    private Registro registro;
    private EstadoSala[] estadosDeSalas = new EstadoSala[11];
    private boolean isDialogOpen = false;
    private Queue<Integer> colaDialogos = new LinkedList<>();
    
    
       public Cronometro(Registro registro) {
        this.registro = registro;

        initComponents();

// Asociar etiquetas a su posición correspondiente en el array
        jLabels[0] = jLabel1;
        jLabels[1] = jLabel2;
        jLabels[2] = jLabel3;
        jLabels[3] = jLabel4;
        jLabels[4] = jLabel5;
        jLabels[5] = jLabel6;
        jLabels[6] = jLabel7;
        jLabels[7] = jLabel8;
        jLabels[8] = jLabel9;
        jLabels[9] = jLabel10;
        jLabels[10] = jLabel11;

        for (int i = 0; i < estadosDeSalas.length; i++) {
            estadosDeSalas[i] = EstadoSala.LIBRE;
        }

        // Inicializar cada cronómetro y asociarlo a una etiqueta.
        for (int i = 0; i < 11; i++) {
            segundos[i] = TIEMPO_INICIAL; // Asegúra tener definido TIEMPO_INICIAL
            final int currentIndex = i;
            cronometros[i] = new Timer(1000, new ActionListener() { // 1000 = 1 segundo de delay
                @Override
                public void actionPerformed(ActionEvent e) {
                    /// Lógica del cronómetro: decrementar segundos, actualizar etiqueta y detenerse si llega a cero.
                    segundos[currentIndex]--;
                    jLabels[currentIndex].setText(formatoTiempo(segundos[currentIndex]));
                    if (segundos[currentIndex] <= 0) {
                        cronometros[currentIndex].stop();
                        GuardarTiempo();
                        segundos[currentIndex] = TIEMPO_INICIAL; // Reestablece el tiempo para esa sala

                        // En lugar de llamar directamente al diálogo, lo encolamos
                        colaDialogos.add(currentIndex);

                        //JOptionPane.showMessageDialog(Cronometro.this, "Tiempo terminado en la sala: " + (currentIndex + 1));
                        if (!isDialogOpen && !colaDialogos.isEmpty()) {
                            isDialogOpen = true;
                            Integer idSala = colaDialogos.poll();
                            SwingUtilities.invokeLater(() -> {
                                String observacion = JOptionPane.showInputDialog(Cronometro.this, "Tiempo terminado en la sala: " + (currentIndex + 1));
                                GuardarObservacion(observacion);
                                isDialogOpen = false;
                                // Después de cerrar el diálogo, verifica si hay más diálogos pendientes y si es así, muestra el siguiente.
                                if (!colaDialogos.isEmpty()) {
                                    Integer siguienteIdSala = colaDialogos.poll();
                                    SwingUtilities.invokeLater(() -> {
                                        String observacion2 = JOptionPane.showInputDialog(Cronometro.this, "Tiempo terminado en la sala: " + (siguienteIdSala + 1));
                                        GuardarObservacion(observacion2);
                                    });

                                }
                            });
                        }
                        // Cambia el estado de la sala a LIBRE
                        if (registro != null) {
                            registro.cambiarEstadoSala(currentIndex, EstadoSala.LIBRE);
                            
                        }

                    }

                }
            });
        }
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
public void GuardarTiempo() {
    
    // Obtener datos desde los componentes de la interfaz gráfica
    
       String sql = "UPDATE REGISTRO SET TIEMPO_FIN = ? WHERE TIEMPO_FIN IS NULL LIMIT 1";

        try {

            // Preparar la consulta para ejecución usando PreparedStatement
            // Esto es útil para evitar inyecciones SQL y mejorar la performance
            PreparedStatement preparedStatement = conectar.prepareStatement(sql);
            Date fecha = new Date();
            long tiempoEnMilisegundos = fecha.getTime();
            java.sql.Timestamp timestamp = new java.sql.Timestamp(tiempoEnMilisegundos);
            
            // Asignar valores a los parámetros de la consulta
            preparedStatement.setTimestamp(1, timestamp);

            // Ejecutar la consulta
            preparedStatement.executeUpdate();

            // Mostrar un mensaje indicando que los datos se guardaron correctamente
            //JOptionPane.showMessageDialog(null, "Datos guardados correctamente!");
        } catch (SQLException e) { // Capturar excepciones relacionadas con SQL
            JOptionPane.showMessageDialog(null, "Error al guardar los datos: " + e.getMessage());
        }

}
    
public void GuardarObservacion(String observacion) {
    
    String sql = "UPDATE REGISTRO SET OBSERVACION= ? WHERE OBSERVACION='--'LIMIT 1";

    try {
        // Preparar la consulta para ejecución usando PreparedStatement
        // Esto es útil para evitar inyecciones SQL y mejorar la performance
        PreparedStatement preparedStatement = conectar.prepareStatement(sql);
        
        // Asignar valores a los parámetros de la consulta
        
        preparedStatement.setString(1, observacion);
        
        // Ejecutar la consulta
        preparedStatement.executeUpdate();

        // Mostrar un mensaje indicando que los datos se guardaron correctamente
        //JOptionPane.showMessageDialog(null, "Datos guardados correctamente!");

    } catch (SQLException e) { // Capturar excepciones relacionadas con SQL
        JOptionPane.showMessageDialog(null, "Error al guardar los datos: " + e.getMessage());
    }
}

    //defino la varaible sala
    private int getCurrentSala() {
        return menuSala.getSelectedIndex();
    }
    /**
     * Convierte un valor de tiempo en segundos al formato HH:MM:SS.
     * @param totalSegundos Total de segundos.
     * @return Tiempo en formato HH:MM:SS.
     */
    private String formatoTiempo(int totalSegundos) {
        int hours = totalSegundos / 3600;
        int remainder = totalSegundos % 3600;
        int minutes = remainder / 60;
        int seconds = remainder % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
     /**
     * Inicia el cronómetro para la sala seleccionada.
     */
    public void startTimer() {
        int sala = getCurrentSala();
        cronometros[sala].start();
    }

     /**
     * Establece el índice de la sala seleccionada.
     * @param index Índice de la sala.
     */

    public void setMenuSalaTabIndex(int index) {
        menuSala.setSelectedIndex(index);//significa que cualquier otra clase puede llamarlo utilizando el setMenuSalaTabIndex(int index)
    }
 
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel13 = new javax.swing.JPanel();
        menuSala = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        tableMenusala2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 850, Short.MAX_VALUE)
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 540, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        menuSala.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        menuSala.setFont(new java.awt.Font("Showcard Gothic", 0, 14)); // NOI18N

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Showcard Gothic", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("00:00:00");
        jLabel1.setMaximumSize(new java.awt.Dimension(50, 16));
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 130, 210, 58));

        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Neon_Blue_and_Black_Gamer_Badge_Logo-removebg-preview.png"))); // NOI18N
        jPanel1.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, -10, 360, 310));

        menuSala.addTab("Sala 1", jPanel1);

        jPanel2.setBackground(new java.awt.Color(0, 0, 0));

        tableMenusala2.setBackground(new java.awt.Color(0, 0, 0));
        tableMenusala2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        tableMenusala2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Showcard Gothic", 0, 36)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("00:00:00");
        tableMenusala2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 140, 216, 60));

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Neon_Blue_and_Black_Gamer_Badge_Logo-removebg-preview.png"))); // NOI18N
        tableMenusala2.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 0, 360, 310));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tableMenusala2, javax.swing.GroupLayout.DEFAULT_SIZE, 744, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tableMenusala2, javax.swing.GroupLayout.DEFAULT_SIZE, 311, Short.MAX_VALUE)
        );

        menuSala.addTab("Sala 2", jPanel2);

        jPanel3.setBackground(new java.awt.Color(0, 0, 0));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setFont(new java.awt.Font("Showcard Gothic", 0, 36)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("00:00:00");
        jPanel3.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 140, 160, -1));

        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Neon_Blue_and_Black_Gamer_Badge_Logo-removebg-preview.png"))); // NOI18N
        jPanel3.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, -10, 730, 320));

        menuSala.addTab("Sala 3", jPanel3);

        jPanel4.setBackground(new java.awt.Color(0, 0, 0));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setFont(new java.awt.Font("Showcard Gothic", 1, 36)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("00:00:00");
        jPanel4.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 140, -1, -1));

        jLabel16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Neon_Blue_and_Black_Gamer_Badge_Logo-removebg-preview.png"))); // NOI18N
        jPanel4.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, -10, 490, 320));

        menuSala.addTab("Sala 4", jPanel4);

        jPanel5.setBackground(new java.awt.Color(0, 0, 0));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setFont(new java.awt.Font("Showcard Gothic", 1, 36)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("00:00:00");
        jPanel5.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 150, -1, -1));

        jLabel17.setBackground(new java.awt.Color(0, 0, 0));
        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Neon_Blue_and_Black_Gamer_Badge_Logo-removebg-preview.png"))); // NOI18N
        jPanel5.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, -10, 590, 320));

        menuSala.addTab("Sala 5", jPanel5);

        jPanel6.setBackground(new java.awt.Color(0, 0, 0));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel6.setFont(new java.awt.Font("Showcard Gothic", 0, 36)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("00:00:00");
        jPanel6.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 130, 200, 90));

        jLabel18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Neon_Blue_and_Black_Gamer_Badge_Logo-removebg-preview.png"))); // NOI18N
        jPanel6.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 0, 530, 320));

        menuSala.addTab("Sala 6", jPanel6);

        jPanel7.setBackground(new java.awt.Color(0, 0, 0));
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel7.setFont(new java.awt.Font("Showcard Gothic", 0, 36)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("00:00:00");
        jPanel7.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 140, -1, -1));

        jLabel19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Neon_Blue_and_Black_Gamer_Badge_Logo-removebg-preview.png"))); // NOI18N
        jPanel7.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, -10, 510, 320));

        menuSala.addTab("Sala 7", jPanel7);

        jPanel8.setBackground(new java.awt.Color(0, 0, 0));
        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel8.setFont(new java.awt.Font("Showcard Gothic", 0, 36)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("00:00:00");
        jPanel8.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 140, -1, -1));

        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Neon_Blue_and_Black_Gamer_Badge_Logo-removebg-preview.png"))); // NOI18N
        jPanel8.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, -10, 460, 320));

        menuSala.addTab("Sala 8", jPanel8);

        jPanel9.setBackground(new java.awt.Color(0, 0, 0));
        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel9.setFont(new java.awt.Font("Showcard Gothic", 0, 36)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("00:00:00");
        jPanel9.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 160, -1, -1));

        jLabel21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Neon_Blue_and_Black_Gamer_Badge_Logo-removebg-preview.png"))); // NOI18N
        jPanel9.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 0, 550, 320));

        menuSala.addTab("Sala 9", jPanel9);

        jPanel10.setBackground(new java.awt.Color(0, 0, 0));
        jPanel10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel10.setFont(new java.awt.Font("Showcard Gothic", 0, 36)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("00:00:00");
        jPanel10.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 150, -1, -1));

        jLabel22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Neon_Blue_and_Black_Gamer_Badge_Logo-removebg-preview.png"))); // NOI18N
        jPanel10.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 0, 600, 310));

        menuSala.addTab("Sala 10", jPanel10);

        jPanel11.setBackground(new java.awt.Color(0, 0, 0));
        jPanel11.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel11.setFont(new java.awt.Font("Showcard Gothic", 0, 36)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("00:00:00");
        jPanel11.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 140, 170, 60));

        jLabel23.setFont(new java.awt.Font("Showcard Gothic", 0, 36)); // NOI18N
        jLabel23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Neon_Blue_and_Black_Gamer_Badge_Logo-removebg-preview.png"))); // NOI18N
        jPanel11.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 0, 490, 310));

        menuSala.addTab("Sala 11", jPanel11);

        getContentPane().add(menuSala, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 140, 760, 350));

        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/CRONO.jpg"))); // NOI18N
        getContentPane().add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(3, 0, 870, 540));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Cronometro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Cronometro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Cronometro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Cronometro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Registro registro = new Registro();

                // Ahora crea la instancia de Cronometro con la instancia de Registro
                Cronometro cronometro = new Cronometro(registro);

                // Muestra la ventana de Registro (o Cronometro, según lo que desees)
                registro.setVisible(true);
                // cronometro.setVisible(true); // Descomenta si deseas mostrar Cronometro
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JTabbedPane menuSala;
    private javax.swing.JPanel tableMenusala2;
    // End of variables declaration//GEN-END:variables
}
