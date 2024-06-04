package Vista;

import java.awt.Color;
import java.io.File;
import java.util.Arrays;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
//Activación de Datos y Manejo de Archivos Adjuntos:
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
//Registro y Manejo de Errores:
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;



public class Correo extends javax.swing.JFrame {

    private static String emailRemitente = "ucvlimanortebiblioteca@gmail.com";
    private static String contraseñaRemitente = "pccw fnto cndb iuoe";
    private String emailDestinatario;
    private String asunto;
    private String contenido;
    private Properties mPropiedades;
    private Session mSesion;
    private MimeMessage mCorreo;
    private File[] mArchivosAdjuntos;
    private String nombreArchivo;

    public Correo() {
        initComponents();
        mPropiedades = new Properties();
        nombreArchivo = "";
        jPanel1.setBackground(new Color(0, 0, 0, 150));
        this.setLocationRelativeTo(null);
    }

    private boolean esEmailValido(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void crearEmail() {
        emailDestinatario = jTextField2.getText();
        asunto = jTextField3.getText();
        contenido = jTextField1.getText();
        if (!emailDestinatario.isEmpty() && !asunto.isEmpty()) {
            if (!esEmailValido(emailDestinatario)) {
    JOptionPane.showMessageDialog(null,"La dirección de correo electrónico no tiene un formato válido.");
                return;}
            mPropiedades.put("mail.smtp.host", "smtp.gmail.com");
            mPropiedades.put("mail.smtp.ssl.trust", "smtp.gmail.com");
            mPropiedades.setProperty("mail.smtp.starttls.enable", "true");
            mPropiedades.setProperty("mail.smtp.port", "587");
            mPropiedades.setProperty("mail.smtp.user", emailRemitente);
            mPropiedades.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");
            mPropiedades.setProperty("mail.smtp.auth", "true");
            mSesion = Session.getDefaultInstance(mPropiedades);
            try {
                MimeMultipart mElementos = new MimeMultipart();
                //contenido
                MimeBodyPart mContenido = new MimeBodyPart();
                mContenido.setContent(contenido, "text/html; charset = utf-8");
                
                mElementos.addBodyPart(mContenido);
                //AgregarArchivosAdjuntos
                MimeBodyPart mAdjuntos = null;
                for (int i = 0; i < mArchivosAdjuntos.length; i++) {
                    mAdjuntos = new MimeBodyPart();
                    mAdjuntos.setDataHandler(new DataHandler(new FileDataSource(mArchivosAdjuntos[i].getAbsolutePath())));
                    mAdjuntos.setFileName(mArchivosAdjuntos[i].getName());
                    mElementos.addBodyPart(mAdjuntos);
                }
                mCorreo = new MimeMessage(mSesion);
                mCorreo.setFrom(new InternetAddress(emailRemitente));
                mCorreo.setRecipient(Message.RecipientType.TO, new InternetAddress(emailDestinatario));
                mCorreo.setSubject(asunto);
                mCorreo.setContent(mElementos);
                enviarEmail();
                //mCorreo.setText(contenido, "ISO-8859-1","html");
            } catch (AddressException ex) {
                Logger.getLogger(Correo.class.getName()).log(Level.SEVERE, null, ex);
            } catch (MessagingException ex) {
                Logger.getLogger(Correo.class.getName()).log(Level.SEVERE, null, ex);
            }} else {
            JOptionPane.showMessageDialog(null, "No se puede enviar sin completarse los datos: Destinatario - Asunto");
        }
    }

    private void enviarEmail() {
        try {
            Transport mTransporte = mSesion.getTransport("smtp");
            mTransporte.connect(emailRemitente, contraseñaRemitente);
            mTransporte.sendMessage(mCorreo, mCorreo.getRecipients(Message.RecipientType.TO));
            mTransporte.close();
            JOptionPane.showMessageDialog(null, "Correo enviado");
        } catch (SendFailedException sfe) {
            JOptionPane.showMessageDialog(null, "Fallo al enviar el correo. Uno o más destinatarios no son válidos.");
            Address[] invalidAddresses = sfe.getInvalidAddresses();
            if (invalidAddresses != null) {
                JOptionPane.showMessageDialog(null, "Direcciones inválidas: " + Arrays.toString(invalidAddresses));}
        } catch (MessagingException me) {
            JOptionPane.showMessageDialog(null, "No se pudo enviar el correo debido a un problema de mensajería: " +
            me.getMessage());
        }}

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new Modelo.PanelRound();
        jButton2 = new javax.swing.JButton();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextField1 = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        panelRound1 = new Modelo.PanelRound();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));
        jPanel1.setRoundBottomLeft(90);
        jPanel1.setRoundBottomRight(90);
        jPanel1.setRoundTopLeft(90);
        jPanel1.setRoundTopRight(90);
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton2.setBackground(new java.awt.Color(153, 0, 0));
        jButton2.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Agregar Archivos adjuntos");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 320, 460, 40));

        jTextField2.setBorder(null);
        jPanel1.add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 30, 460, 40));

        jTextField3.setBorder(null);
        jPanel1.add(jTextField3, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 100, 460, 40));

        jTextField1.setColumns(20);
        jTextField1.setRows(5);
        jTextField1.setBorder(null);
        jScrollPane1.setViewportView(jTextField1);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 160, 460, 140));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Contenido ");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 160, 110, 30));

        jButton3.setBackground(new java.awt.Color(153, 0, 0));
        jButton3.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Limpiar");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 510, 150, 50));

        jButton1.setBackground(new java.awt.Color(153, 0, 0));
        jButton1.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Enviar Correo");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 510, 180, 50));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Destinatario ");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 30, 140, 30));

        jLabel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 370, 460, 120));

        panelRound1.setBackground(new java.awt.Color(255, 255, 255));
        panelRound1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel1.add(panelRound1, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 370, 460, 120));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Asunto ");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 90, 80, 50));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 30, 730, 590));

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/FONDO UCV azul osuro.png"))); // NOI18N
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 800, 640));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        crearEmail();
        //enviarEmail();
        Limpiar();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
         JFileChooser chooser = new JFileChooser();
        chooser.setMultiSelectionEnabled(true);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        if (chooser.showOpenDialog(this) != JFileChooser.CANCEL_OPTION) {
            mArchivosAdjuntos = chooser.getSelectedFiles();

            for (File archivo : mArchivosAdjuntos) {
                nombreArchivo += archivo.getName() + "<br>";

            }
            jLabel4.setText("<html><p>" + nombreArchivo + "</p></html>");
        }


    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        mArchivosAdjuntos = new File[0];
        nombreArchivo = "";
        jLabel4.setText("");
    }//GEN-LAST:event_jButton3ActionPerformed

    public void Limpiar() {
        jTextField2.setText("");
        jTextField3.setText("");
        jTextField1.setText("");
        mArchivosAdjuntos = new File[0];
        nombreArchivo = ""; // 
        jLabel4.setText(""); // 
    }

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
            java.util.logging.Logger.getLogger(Correo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Correo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Correo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Correo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Correo().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private Modelo.PanelRound jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private Modelo.PanelRound panelRound1;
    // End of variables declaration//GEN-END:variables
}
