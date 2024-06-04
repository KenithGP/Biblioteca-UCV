
package Vista;

// Manejo de PDF y Documentos
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

// Conexión y Manejo de Bases de Datos
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import Modelo.Conectar; 
// (asumiendo que es para la conexión con la base de datos)

// Interfaz Gráfica y Utilidades de Java
import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

// Manejo de Correos Electrónicos
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JFrame;

// Manejo de Tablas y Filtrado en Interfaz Gráfica
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;


public class Mostrar extends javax.swing.JFrame {
    Conectar conect = new Conectar();
    Connection conectar = (Connection) conect.Conectar();
    DefaultTableModel model,model2;
    DecimalFormat df;
      //instancias correo
    private static String emailRemitente = "ucvlimanortebiblioteca@gmail.com";
    private static String contraseñaRemitente = "pccw fnto cndb iuoe";
    private String emailDestinatario;
    private String asunto;
    private String contenido;

    private Properties mPropiedades;
    private Session mSesion;
    private MimeMessage mCorreo;
    //
    public Mostrar() {
        initComponents(); 
        jTextField1.setBackground(new Color(255, 255, 255, 250));
        this.setLocationRelativeTo(null);
        jpanel1.setBackground(new Color(0, 0, 0, 180));
        this.setLocationRelativeTo(null);
        jPanel2.setBackground(new Color(0, 0, 0, 180));
        this.setLocationRelativeTo(null);
        jPanel3.setBackground(new Color(0, 0, 0, 180));
        this.setLocationRelativeTo(null);
        String[] nombreColumnas = {"ID_REGISTRO", "ID_SALA", "CODIGO", "NOMBRES", 
        "APELLIDOS", "CORREO", "INTEGRANTES", "IEMPO DE INCIO", "TIEMPO FINAL", "OBSERVACION"};
        model = new DefaultTableModel(nombreColumnas, 0);
        tblRegistro.setModel(model);
        MostrarDatos();
        String[] nombreColumnas2 = {"CODIGO","NOMBRES","APELLIDOS","CORREO","VISTAS","PORCENTAJE TOTAL"};
        model2 = new DefaultTableModel(nombreColumnas2,0);
        tblRegistro1.setModel(model2);
       TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        tblRegistro.setRowSorter(sorter);
        mPropiedades = new Properties();
        MostrarDatos2();
         jTextField2.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                newFilter();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                newFilter();
            }

            @Override
            public void changedUpdate                                                                                                                                                                                                                                                                                                                                                                                                                       (DocumentEvent e) {
                newFilter();
            }
            
            // Define el método newFilter aquí o como un método separado de la clase
            private void newFilter() {
                String text = jTextField2.getText();
                if (text.trim().length() == 0) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text)); 
                } } });
         setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
    
    private void crearEmail(String emailDestinatario, String asunto, String contenido) {
        mPropiedades.put("mail.smtp.host", "smtp.gmail.com");
        mPropiedades.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        mPropiedades.setProperty("mail.smtp.starttls.enable", "true");
        mPropiedades.setProperty("mail.smtp.port", "587");
        mPropiedades.setProperty("mail.smtp.user", emailRemitente);
        mPropiedades.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");
        mPropiedades.setProperty("mail.smtp.auth", "true");
        mSesion = Session.getDefaultInstance(mPropiedades);
        try {
            mCorreo = new MimeMessage(mSesion);
            mCorreo.setFrom(new InternetAddress(emailRemitente));
            mCorreo.setRecipient(Message.RecipientType.TO, new InternetAddress(emailDestinatario));
            mCorreo.setSubject(asunto);
            mCorreo.setContent(contenido, "text/html; charset=utf-8");
        } catch (AddressException ex) {
            Logger.getLogger(Correo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(Correo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void enviarEmail() {
        try {
            Transport mTransporte = mSesion.getTransport("smtp");
            mTransporte.connect(emailRemitente, contraseñaRemitente);
            mTransporte.sendMessage(mCorreo, mCorreo.getRecipients(Message.RecipientType.TO));
            mTransporte.close();

        } catch (NoSuchProviderException ex) {
            Logger.getLogger(Correo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(Correo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jCheckBoxMenuItem1 = new javax.swing.JCheckBoxMenuItem();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblRegistro = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new Modelo.PanelRound();
        jPanel2 = new Modelo.PanelRound();
        jLabel4 = new javax.swing.JLabel();
        jbtnModificar = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jbtnCorreo = new javax.swing.JButton();
        jbtn_actualizar = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblRegistro1 = new javax.swing.JTable();
        jpanel1 = new Modelo.PanelRound();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jLabel5 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jDateChooser3 = new com.toedter.calendar.JDateChooser();
        jDateChooser4 = new com.toedter.calendar.JDateChooser();
        jbtnNotficar = new javax.swing.JButton();
        jbtnBuscar = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jbtnVolver = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();

        jCheckBoxMenuItem1.setSelected(true);
        jCheckBoxMenuItem1.setText("jCheckBoxMenuItem1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tblRegistro.setFont(new java.awt.Font("Swis721 LtEx BT", 0, 12)); // NOI18N
        tblRegistro.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tblRegistro);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 210, 1060, 170));

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("Rockwell Condensed", 0, 48)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("   Historial de Registros");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 30, 410, 60));

        jPanel3.setRoundBottomLeft(80);
        jPanel3.setRoundBottomRight(80);
        jPanel3.setRoundTopLeft(80);
        jPanel3.setRoundTopRight(80);
        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 20, 520, 80));

        jPanel2.setRoundBottomLeft(90);
        jPanel2.setRoundBottomRight(90);
        jPanel2.setRoundTopLeft(90);
        jPanel2.setRoundTopRight(90);
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setFont(new java.awt.Font("Agency FB", 1, 36)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("OBSERVACIÓN");
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 20, 180, 50));

        jbtnModificar.setBackground(new java.awt.Color(153, 0, 0));
        jbtnModificar.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jbtnModificar.setForeground(new java.awt.Color(255, 255, 255));
        jbtnModificar.setText("Modificar");
        jbtnModificar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jbtnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnModificarActionPerformed(evt);
            }
        });
        jPanel2.add(jbtnModificar, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 220, 130, 40));

        jTextField1.setFont(new java.awt.Font("Segoe UI Historic", 0, 14)); // NOI18N
        jTextField1.setBorder(null);
        jPanel2.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 310, 110));

        jButton3.setBackground(new java.awt.Color(153, 0, 0));
        jButton3.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Reporte Registro");
        jButton3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 220, 150, 40));

        jbtnCorreo.setBackground(new java.awt.Color(153, 0, 0));
        jbtnCorreo.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jbtnCorreo.setForeground(new java.awt.Color(255, 255, 255));
        jbtnCorreo.setText("Enviar Correo");
        jbtnCorreo.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jbtnCorreo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnCorreoActionPerformed(evt);
            }
        });
        jPanel2.add(jbtnCorreo, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 290, 130, 40));

        jbtn_actualizar.setBackground(new java.awt.Color(153, 0, 0));
        jbtn_actualizar.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jbtn_actualizar.setForeground(new java.awt.Color(255, 255, 255));
        jbtn_actualizar.setText("Actualizar");
        jbtn_actualizar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jbtn_actualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtn_actualizarActionPerformed(evt);
            }
        });
        jPanel2.add(jbtn_actualizar, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 290, 150, 40));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1160, 220, 350, 390));

        tblRegistro1.setFont(new java.awt.Font("Swis721 LtEx BT", 0, 12)); // NOI18N
        tblRegistro1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane2.setViewportView(tblRegistro1);

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 460, 1060, 170));

        jpanel1.setRoundBottomLeft(90);
        jpanel1.setRoundBottomRight(90);
        jpanel1.setRoundTopLeft(90);
        jpanel1.setRoundTopRight(90);
        jpanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jDateChooser2.setDateFormatString("yyyy-MM-dd");
        jpanel1.add(jDateChooser2, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 40, 170, 40));

        jDateChooser1.setDateFormatString("yyyy-MM-dd");
        jpanel1.add(jDateChooser1, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 40, 160, 40));

        jLabel5.setFont(new java.awt.Font("Agency FB", 1, 24)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Fecha");
        jpanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 290, 80, 40));

        jButton1.setBackground(new java.awt.Color(153, 0, 0));
        jButton1.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Buscar");
        jButton1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jpanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(970, 290, 120, 40));

        jButton2.setBackground(new java.awt.Color(153, 0, 0));
        jButton2.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Reporte de Análisis");
        jButton2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jpanel1.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 530, 250, 40));

        jLabel6.setFont(new java.awt.Font("Agency FB", 1, 24)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("RESULTADO DE ANALISIS");
        jpanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 290, 200, 40));

        jTextField2.setBorder(null);
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });
        jpanel1.add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 30, 300, 50));

        jLabel2.setFont(new java.awt.Font("Agency FB", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Buscar por codigo");
        jpanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 40, 150, 40));

        jDateChooser3.setDateFormatString("yyyy-MM-dd");
        jpanel1.add(jDateChooser3, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 290, 170, 40));

        jDateChooser4.setDateFormatString("yyyy-MM-dd");
        jpanel1.add(jDateChooser4, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 290, 170, 40));

        jbtnNotficar.setBackground(new java.awt.Color(153, 0, 0));
        jbtnNotficar.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jbtnNotficar.setForeground(new java.awt.Color(255, 255, 255));
        jbtnNotficar.setText("Notificar ");
        jbtnNotficar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jbtnNotficar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnNotficarActionPerformed(evt);
            }
        });
        jpanel1.add(jbtnNotficar, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 290, 240, 40));

        jbtnBuscar.setBackground(new java.awt.Color(153, 0, 0));
        jbtnBuscar.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jbtnBuscar.setForeground(new java.awt.Color(255, 255, 255));
        jbtnBuscar.setText("Buscar");
        jbtnBuscar.setToolTipText("");
        jbtnBuscar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jbtnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnBuscarActionPerformed(evt);
            }
        });
        jpanel1.add(jbtnBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 40, 120, 40));

        jLabel7.setFont(new java.awt.Font("Agency FB", 1, 24)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Fecha");
        jpanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 20, 80, 70));

        jbtnVolver.setBackground(new java.awt.Color(153, 0, 0));
        jbtnVolver.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jbtnVolver.setForeground(new java.awt.Color(255, 255, 255));
        jbtnVolver.setText("Volver");
        jbtnVolver.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jbtnVolver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnVolverActionPerformed(evt);
            }
        });
        jpanel1.add(jbtnVolver, new org.netbeans.lib.awtextra.AbsoluteConstraints(920, 530, 140, 40));

        getContentPane().add(jpanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, 1120, 590));

        jLabel3.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/FONDO UCV azul osuro.png"))); // NOI18N
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1530, 720));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbtnVolverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnVolverActionPerformed
        /*Registro registro = new Registro();
        registro.setVisible(true);*/
        this.dispose();
    }//GEN-LAST:event_jbtnVolverActionPerformed

    private void jbtnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnBuscarActionPerformed
        java.util.Date fecha1 = jDateChooser2.getDate();
        java.util.Date fecha2 = jDateChooser1.getDate();
        String ingreso = jTextField2.getText();
        Long codigo = ingreso.isEmpty() ? null : Long.parseLong(ingreso);

        try {

            CallableStatement cmd = conectar.prepareCall("{CALL USP_RANGOFECHA(?, ?, ?)}");

            if (fecha1 != null) {
                cmd.setDate(1, new java.sql.Date(fecha1.getTime()));
            } else {
                cmd.setNull(1, Types.DATE);
            }

            if (fecha2 != null) {
                cmd.setDate(2, new java.sql.Date(fecha2.getTime()));
            } else {
                cmd.setNull(2, Types.DATE);
            }

            if (codigo != null) {
                cmd.setLong(3, codigo);
            } else {
                cmd.setNull(3, Types.BIGINT);
            }

            ResultSet set = cmd.executeQuery();
            EliminarDatos(); // Limpiar los datos existentes antes de agregar los nuevos.
            Object[] fila = new Object[10];
            while (set.next()) {
                fila[0] = set.getInt("ID_REGISTRO");
                fila[1] = set.getInt("ID_SALA");
                fila[2] = set.getLong("CODIGO_ES");
                fila[3] = set.getString("NOMBRE_ES");
                fila[4] = set.getString("APELLIDO_ES");
                fila[5] = set.getString("CORREO_ES");
                fila[6] = set.getInt("INTEGRANTES");
                fila[7] = set.getTimestamp("TIEMPO_INICIO");
                fila[8] = set.getTimestamp("TIEMPO_FIN");
                fila[9] = set.getString("OBSERVACION");
                model.addRow(fila);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "" + ex);
        }
    }//GEN-LAST:event_jbtnBuscarActionPerformed

    
    private void jbtnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnModificarActionPerformed
                                                  
    int fila = tblRegistro.getSelectedRow();
    if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione un registro para modificar.", "Error", 
                JOptionPane.ERROR_MESSAGE);
        return; // No hay fila seleccionada, salir del método.
    }   String obs = jTextField1.getText();
    if (obs.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El campo de observación no puede estar vacío.", "Error",
                JOptionPane.ERROR_MESSAGE);
        return; // El campo de observación está vacío, salir del método.
    }   String sql = "UPDATE REGISTRO SET OBSERVACION = ? WHERE ID_REGISTRO = ?";
    int filaSeleccionadaID = (int) tblRegistro.getValueAt(fila, 0);
        try {
        PreparedStatement preparedStatement = conectar.prepareStatement(sql);
        preparedStatement.setString(1, obs);
        preparedStatement.setInt(2, filaSeleccionadaID);
        int rowsAffected = preparedStatement.executeUpdate();
        
        if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(this, "Observación actualizada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            EliminarDatos();
            MostrarDatos();
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo actualizar la observación.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } catch (SQLException ex) {
               JOptionPane.showMessageDialog(this, "Error al actualizar la observación: " +
                ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    }
    }//GEN-LAST:event_jbtnModificarActionPerformed

    private void jbtn_actualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtn_actualizarActionPerformed
       EliminarDatos();
        MostrarDatos();
    }//GEN-LAST:event_jbtn_actualizarActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
       java.util.Date fecha1 = jDateChooser3.getDate();
        java.util.Date fecha2 = jDateChooser4.getDate();
        
        if ((fecha1 == null || fecha2 == null) || (fecha1==null && fecha2==null)){
            try{
            CallableStatement call = conectar.prepareCall("{CALL USP_MOSTRARCANTIDAD(?,?)}");
            
            call.setNull(1,Types.DATE);
            call.setNull(2, Types.DATE);
            
            ResultSet rs = call.executeQuery();
            EliminarDatos2();
            Object[] fila = new Object[6];
            while(rs.next()) {
                fila[0] = rs.getLong("CODIGO_ES");
                fila[1] = rs.getString("NOMBRE_ES");
                fila[2] = rs.getString("APELLIDO_ES");
                fila[3] = rs.getString("CORREO_ES");
                fila[4] = rs.getInt("CANTIDAD");
                fila[5] = rs.getDouble("PORCENTAJE"); 
                model2.addRow(fila);
             }
            }catch(SQLException ex){
                ex.printStackTrace();
            }
            
        }else{

            try{

            java.sql.Date fechaSql1 = new java.sql.Date(fecha1.getTime());
            java.sql.Date fechaSql2 = new java.sql.Date(fecha2.getTime());
            CallableStatement call = conectar.prepareCall("{CALL USP_MOSTRARCANTIDAD(?,?)}");
            call.setDate(1, fechaSql1);
            call.setDate(2, fechaSql2);
            ResultSet rs = call.executeQuery();
            EliminarDatos2();
            Object[] fila = new Object[6];
            while(rs.next()){
                 fila[0] = rs.getLong("CODIGO_ES");
                fila[1] = rs.getString("NOMBRE_ES");
                fila[2] = rs.getString("APELLIDO_ES");
                fila[3] = rs.getString("CORREO_ES");
                fila[4] = rs.getInt("CANTIDAD");
                fila[5] = rs.getDouble("PORCENTAJE"); 
                model2.addRow(fila);
            }
            }catch(SQLException ex){  
                ex.printStackTrace();
            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        int totalfilas = tblRegistro.getRowCount();
        JFileChooser filec = new JFileChooser();
        int opcion = JOptionPane.showConfirmDialog(null, 
        "¿Estás seguro de generar un reporte de estos registros?", "Selecciona una opción...",
        JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
        if (opcion == 0) {
            FileNameExtensionFilter filtroArchivo = new FileNameExtensionFilter("*.pdf", "pdf");
            filec.setFileFilter(filtroArchivo);
            int seleccion = filec.showSaveDialog(this);
            if (seleccion == JFileChooser.APPROVE_OPTION) {
                try {
                    File file = filec.getSelectedFile();
                    String fileString = file.toString();
                    FileOutputStream archivo = new FileOutputStream(fileString + ".pdf");
                    Document documento = new Document();
                    PdfWriter.getInstance(documento, archivo);
                    documento.open();
                    try {
                        Paragraph parrafo0 = new Paragraph();
                        parrafo0.setAlignment(Paragraph.ALIGN_CENTER);
                        parrafo0.setFont(FontFactory.getFont("Tahoma", 20, Font.BOLD, BaseColor.BLACK));
//                        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd-MM-yyyy");

                        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
                        String FechaActual = formato.format(new Date());

                        parrafo0.add("\n REPORTE  \n");
                        documento.add(parrafo0);

                        //TABLA 1
                        PdfPTable tabla = new PdfPTable(10);
                        documento.add(tabla);
                        Paragraph parrafo1 = new Paragraph();
                        parrafo1.setFont(FontFactory.getFont("Tahoma", 13, Font.BOLD, BaseColor.BLACK));
                        parrafo1.setAlignment(Paragraph.ALIGN_CENTER);
                        parrafo1.add("\n \n TABLA 1 - Registro de separacion \n \n");

                        documento.add(parrafo1);
                        // Ancho de la columna
                        tabla.setWidthPercentage(100);
                        // Alineación a la izquierda
                        tabla.setHorizontalAlignment(Element.ALIGN_LEFT);

                        // Ancho de cada columna
                        float[] TamañoColumnas = {0.5f, 1.2f, 2.5f, 2f, 2f, 1.5f, 1.1f, 1.8f, 1.8f, 2f};
                        tabla.setWidths(TamañoColumnas);

                        // Variable de tamaño de fuente
                        Font fontSize = FontFactory.getFont(FontFactory.TIMES, 12f);

                        // Se agrega el nombre de la celda 1
                        PdfPCell cell1 = new PdfPCell(new Paragraph("ID_REGISTRO", fontSize));
                        cell1.setBackgroundColor(BaseColor.GREEN); // se pone el color a la celda 1
                        tabla.addCell(cell1); // agregamos la celda a la tabla

                        PdfPCell cell2 = new PdfPCell(new Paragraph("ID_SALA", fontSize));
                        cell2.setBackgroundColor(BaseColor.GREEN);
                        tabla.addCell(cell2);

                        PdfPCell cell3 = new PdfPCell(new Paragraph("CODIGO", fontSize));
                        cell3.setBackgroundColor(BaseColor.GREEN);
                        tabla.addCell(cell3);

                        PdfPCell cell4 = new PdfPCell(new Paragraph("NOMBRES", fontSize));
                        cell4.setBackgroundColor(BaseColor.GREEN);
                        tabla.addCell(cell4);

                        PdfPCell cell5 = new PdfPCell(new Paragraph("APELLIDOS", fontSize));
                        cell5.setBackgroundColor(BaseColor.GREEN);
                        tabla.addCell(cell5);

                        PdfPCell cell6 = new PdfPCell(new Paragraph("CORREO", fontSize));
                        cell6.setBackgroundColor(BaseColor.GREEN);
                        tabla.addCell(cell6);

                        PdfPCell cell7 = new PdfPCell(new Paragraph("INTEGRANTES", fontSize));
                        cell7.setBackgroundColor(BaseColor.GREEN);
                        tabla.addCell(cell7);

                        PdfPCell cell8 = new PdfPCell(new Paragraph("IEMPO DE INCIO", fontSize));
                        cell8.setBackgroundColor(BaseColor.GREEN);
                        tabla.addCell(cell8);

                        PdfPCell cell9 = new PdfPCell(new Paragraph("TIEMPO FINAL", fontSize));
                        cell9.setBackgroundColor(BaseColor.GREEN);
                        tabla.addCell(cell9);

                        PdfPCell cell10 = new PdfPCell(new Paragraph("OBSERVACION", fontSize));
                        cell10.setBackgroundColor(BaseColor.GREEN);
                        tabla.addCell(cell10);

                        for (int z = 0; z < totalfilas; z++) {

                            int registro = (int) model.getValueAt(z, 0);
                            int sala = (int) model.getValueAt(z, 1);
                            Long codigo = (Long) model.getValueAt(z, 2);
                            String nombre = (String) model.getValueAt(z, 3);
                            String apellido = (String) model.getValueAt(z, 4);
                            String correo = (String) model.getValueAt(z, 5);
                            int integrantes = (int) model.getValueAt(z, 6);
                            java.sql.Timestamp tiempoinicio = (java.sql.Timestamp) model.getValueAt(z, 7);
                            java.sql.Timestamp tiempofin = (java.sql.Timestamp) model.getValueAt(z, 8);
                            String observacion = (String) model.getValueAt(z, 9);

                            tabla.addCell(new Paragraph("" + registro, fontSize));
                            tabla.addCell(new Paragraph("" + sala, fontSize));
                            tabla.addCell(new Paragraph("" + codigo, fontSize));
                            tabla.addCell(new Paragraph(nombre, fontSize));
                            tabla.addCell(new Paragraph(apellido, fontSize));
                            tabla.addCell(new Paragraph(correo, fontSize));
                            tabla.addCell(new Paragraph("" + integrantes, fontSize));
                            tabla.addCell(new Paragraph("" + tiempoinicio, fontSize));
                            tabla.addCell(new Paragraph("" + tiempofin, fontSize));
                            tabla.addCell(new Paragraph(observacion, fontSize));
                        }
                        documento.add(tabla);

                        Paragraph parrafo4 = new Paragraph();
                        parrafo4.setAlignment(Paragraph.ALIGN_LEFT);
                        parrafo4.setFont(FontFactory.getFont("Tahoma", 10, Font.BOLD, BaseColor.BLACK));

                        if (jDateChooser2.getDate() == null || jDateChooser1.getDate() == null) {

                            parrafo4.add("\n \n Generado hasta la fecha : " + FechaActual);
                        } else {
                            String fechainicio = formato.format(jDateChooser2.getDate());
                            String fechafin = formato.format(jDateChooser1.getDate());
                            parrafo4.add("\n \n Generado entre fechas : " + fechainicio + " - " + fechafin);
                        }

                        documento.add(parrafo4);
                        JOptionPane.showMessageDialog(null, " REPORTE CREADO ");
                        documento.close();

                    } catch (Exception e) {
                    }
                } catch (Exception e) {
                }}} else {
            if (opcion == 2) {
                JOptionPane.showMessageDialog(null, " REPORTE CANCELADO ");
            }}
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        int totalfilas = tblRegistro1.getRowCount();
        JFileChooser filec = new JFileChooser();
        int opcion = JOptionPane.showConfirmDialog(null, 
                "¿Estás seguro de generar un reporte de estos registros?", "Selecciona una opción...",
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
        if (opcion == 0) {
            FileNameExtensionFilter filtroArchivo = new FileNameExtensionFilter("*.pdf", "pdf");
            filec.setFileFilter(filtroArchivo);
            int seleccion = filec.showSaveDialog(this);
            if (seleccion == JFileChooser.APPROVE_OPTION) {
                try {
                    File file = filec.getSelectedFile();
                    String fileString = file.toString();
                    FileOutputStream archivo = new FileOutputStream(fileString + ".pdf");
                    Document documento = new Document();
                    PdfWriter.getInstance(documento, archivo);
                    documento.open();
                    try {
                        Paragraph parrafo0 = new Paragraph();
                        parrafo0.setAlignment(Paragraph.ALIGN_CENTER);
                        parrafo0.setFont(FontFactory.getFont("Tahoma", 20, Font.BOLD, BaseColor.BLACK));
                        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
                        String FechaActual = formato.format(new Date());

                        parrafo0.add("\n REPORTE  \n");
                        documento.add(parrafo0);

                        //TABLA 1
                        PdfPTable tabla = new PdfPTable(6);
                        documento.add(tabla);
                        Paragraph parrafo1 = new Paragraph();
                        parrafo1.setFont(FontFactory.getFont("Tahoma", 13, Font.BOLD, BaseColor.BLACK));
                        parrafo1.setAlignment(Paragraph.ALIGN_CENTER);
                        parrafo1.add("\n \n TABLA 1 - Registro de separacion \n \n");

                        documento.add(parrafo1);
                        // Ancho de la columna
                        tabla.setWidthPercentage(100);
                        // Alineación a la izquierda
                        tabla.setHorizontalAlignment(Element.ALIGN_LEFT);

                        // Ancho de cada columna
                        float[] TamañoColumnas = {2.2f, 2.5f, 2f, 4f, 3f, 4f};
                        tabla.setWidths(TamañoColumnas);

                        // Variable de tamaño de fuente
                        Font fontSize = FontFactory.getFont(FontFactory.TIMES, 12f);

                        // Se agrega el nombre de la celda 1
                        PdfPCell cell1 = new PdfPCell(new Paragraph("CODIGO", fontSize));
                        cell1.setBackgroundColor(BaseColor.GREEN); // se pone el color a la celda 1
                        tabla.addCell(cell1); // agregamos la celda a la tabla

                        PdfPCell cell2 = new PdfPCell(new Paragraph("NOMBRES", fontSize));
                        cell2.setBackgroundColor(BaseColor.GREEN);
                        tabla.addCell(cell2);

                        PdfPCell cell3 = new PdfPCell(new Paragraph("APELLIDOS", fontSize));
                        cell3.setBackgroundColor(BaseColor.GREEN);
                        tabla.addCell(cell3);

                        PdfPCell cell4 = new PdfPCell(new Paragraph("CORREO", fontSize));
                        cell4.setBackgroundColor(BaseColor.GREEN);
                        tabla.addCell(cell4);

                        PdfPCell cell5 = new PdfPCell(new Paragraph("VISTAS", fontSize));
                        cell5.setBackgroundColor(BaseColor.GREEN);
                        tabla.addCell(cell5);

                        PdfPCell cell6 = new PdfPCell(new Paragraph("PORCENTAJE", fontSize));
                        cell6.setBackgroundColor(BaseColor.GREEN);
                        tabla.addCell(cell6);

                        for (int z = 0; z < totalfilas; z++) {

                            Long codigo = (Long) model2.getValueAt(z, 0);
                            String nombres = (String) model2.getValueAt(z, 1);
                            String apellidos = (String) model2.getValueAt(z, 2);
                            String correo = (String) model2.getValueAt(z, 3);
                            int visitas = (int) model2.getValueAt(z, 4);
                            double porcentaje = (double) model2.getValueAt(z, 5);

                            tabla.addCell(new Paragraph("" + codigo, fontSize));
                            tabla.addCell(new Paragraph(nombres, fontSize));
                            tabla.addCell(new Paragraph(apellidos, fontSize));
                            tabla.addCell(new Paragraph(correo, fontSize));
                            tabla.addCell(new Paragraph(""+visitas, fontSize));
                            tabla.addCell(new Paragraph(""+porcentaje, fontSize));
                        }
                        documento.add(tabla);

                        Paragraph parrafo4 = new Paragraph();
                        parrafo4.setAlignment(Paragraph.ALIGN_LEFT);
                        parrafo4.setFont(FontFactory.getFont("Tahoma", 10, Font.BOLD, BaseColor.BLACK));

                        if (jDateChooser3.getDate() == null || jDateChooser4.getDate() == null) {

                            parrafo4.add("\n \n Generado hasta la fecha : " + FechaActual);
                        } else {
                            String fechainicio = formato.format(jDateChooser3.getDate());
                            String fechafin = formato.format(jDateChooser4.getDate());
                            parrafo4.add("\n \n Generado entre fechas : " + fechainicio + " - " + fechafin);
                        }

                        documento.add(parrafo4);
                        JOptionPane.showMessageDialog(null, " REPORTE CREADO ");
                        documento.close();

                    } catch (Exception e) {
                    } } catch (Exception e) {
                }}  } else { if (opcion == 2) {
                JOptionPane.showMessageDialog(null, " REPORTE CANCELADO ");
            }}
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jbtnNotficarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnNotficarActionPerformed
        int totalfilas = tblRegistro1.getRowCount();
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        String FechaActual = formato.format(new Date());
        boolean primerMensajeMostrado = false; // Variable para controlar el primer mensaje
        boolean segundoMensajeMostrado = false; // Variable para controlar el segundo mensaje

        for (int z = 0; z < totalfilas; z++) {
            Long codigo = (Long) model2.getValueAt(z, 0);
            String nombres = (String) model2.getValueAt(z, 1);
            String apellidos = (String) model2.getValueAt(z, 2);
            String correo = (String) model2.getValueAt(z, 3);
            int visitas = (int) model2.getValueAt(z, 4);
//            double porcentaje = (double) model2.getValueAt(z, 5);
            if (visitas >= 4) {
                emailDestinatario = correo;
                //JOptionPane.showMessageDialog(null, "Enviando correos. Este proceso puede tardar un poco. Espere a la confirmación");
                asunto = " - Notificación de frecuencia Sala Biblioteca - ";
                if (jDateChooser3.getDate() == null || jDateChooser4.getDate() == null) {
                    contenido = "<h1>¡DE NO CREER!</h1>" + "<br>" + "Usted <b>ha realizado</b> más de : "
                            + visitas + " visitas hasta la fecha : " + FechaActual;
                } else {
                    String fechainicio = formato.format(jDateChooser3.getDate());
                    String fechafin = formato.format(jDateChooser4.getDate());
                    contenido = "<h1> ¡DE NO CREER! </h1>" + "<br>" + "Usted <b> ha realizado </b> más de : "
                            + visitas + " visitas entre las fechas de : " + fechainicio + " - " + fechafin;
                }
                if (!primerMensajeMostrado) {
                    JOptionPane.showMessageDialog(null, "Enviando correos. Este proceso puede tardar un poco. Espere a la confirmación");
                    primerMensajeMostrado = true; // Marcar el primer mensaje como mostrado
                }
                crearEmail(emailDestinatario, asunto, contenido);
                enviarEmail();

            }

        }

        if (!segundoMensajeMostrado) {
            JOptionPane.showMessageDialog(null, "Correos enviado");
            segundoMensajeMostrado = true; // Marcar el segundo mensaje como mostrado
        }

    }//GEN-LAST:event_jbtnNotficarActionPerformed

    private void jbtnCorreoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnCorreoActionPerformed
       new Correo().setVisible(true);
    }//GEN-LAST:event_jbtnCorreoActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed
    public void EliminarDatos() {
        model = (DefaultTableModel) tblRegistro.getModel();
        model.setRowCount(0);}
     public void EliminarDatos2() {
        model2 = (DefaultTableModel) tblRegistro1.getModel();
        model2.setRowCount(0);  }
    public void MostrarDatos2() {
           java.util.Date fecha1 = jDateChooser3.getDate();
        java.util.Date fecha2 = jDateChooser4.getDate();

        if ((fecha1 == null || fecha2 == null) || (fecha1 == null && fecha2 == null)) {
            try {
                CallableStatement call = conectar.prepareCall("{CALL USP_MOSTRARCANTIDAD2(?,?)}");

                call.setNull(1, Types.TIMESTAMP);
                call.setNull(2, Types.TIMESTAMP);

                ResultSet rs = call.executeQuery();
                EliminarDatos2();
                Object[] fila = new Object[6];
                while (rs.next()) {
                    fila[0] = rs.getLong("CODIGO_ES");
                    fila[1] = rs.getString("NOMBRE_ES");
                    fila[2] = rs.getString("APELLIDO_ES");
                    fila[3] = rs.getString("CORREO_ES");
                    fila[4] = rs.getInt("CANTIDAD");
                    fila[5] = rs.getDouble("PORCENTAJE");
                    model2.addRow(fila);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        }
    }
    public void MostrarDatos() {
        String sql = "SELECT r.ID_REGISTRO, r.ID_SALA, e.CODIGO_ES, e.NOMBRE_ES, e.APELLIDO_ES, e.CORREO_ES, r.INTEGRANTES, r.TIEMPO_INICIO,"
                + "r.TIEMPO_FIN, r.OBSERVACION"
                + " FROM ESTUDIANTE e "
                + "JOIN REGISTRO r ON e.ID_ESTUDIANTE = r.ID_ESTUDIANTE "
                + "ORDER BY r.ID_REGISTRO ASC";

        try {

            PreparedStatement preparedStatement = conectar.prepareStatement(sql);
            //PreparedStatement ps = new PreparedStatement();
            ResultSet resultSet;

            resultSet = preparedStatement.executeQuery();
            Object[] fila = new Object[10];
            while (resultSet.next()) {

                fila[0] = resultSet.getInt("ID_REGISTRO");
                fila[1] = resultSet.getInt("ID_SALA");
                fila[2] = resultSet.getLong("CODIGO_ES");
                fila[3] = resultSet.getString("NOMBRE_ES");
                fila[4] = resultSet.getString("APELLIDO_ES");
                fila[5] = resultSet.getString("CORREO_ES");
                fila[6] = resultSet.getInt("INTEGRANTES");
                fila[7] = resultSet.getTimestamp("TIEMPO_INICIO");
                fila[8] = resultSet.getTimestamp("TIEMPO_FIN");
                fila[9] = resultSet.getString("OBSERVACION");

                model.addRow(fila);

            }

        } catch (SQLException ex) {
            Logger.getLogger(Mostrar.class.getName()).log(Level.SEVERE, null, ex);
        }
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
            java.util.logging.Logger.getLogger(Mostrar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Mostrar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Mostrar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Mostrar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Mostrar().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem1;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private com.toedter.calendar.JDateChooser jDateChooser3;
    private com.toedter.calendar.JDateChooser jDateChooser4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private Modelo.PanelRound jPanel2;
    private Modelo.PanelRound jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JButton jbtnBuscar;
    private javax.swing.JButton jbtnCorreo;
    private javax.swing.JButton jbtnModificar;
    private javax.swing.JButton jbtnNotficar;
    private javax.swing.JButton jbtnVolver;
    private javax.swing.JButton jbtn_actualizar;
    private Modelo.PanelRound jpanel1;
    private javax.swing.JTable tblRegistro;
    private javax.swing.JTable tblRegistro1;
    // End of variables declaration//GEN-END:variables
}
