package Vista;

import Modelo.ClienteInfo;
import Modelo.Conectar;
import Modelo.Estudiante;
import java.awt.Color;
import java.util.List;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JOptionPane;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.sql.Types; // Para el uso de Types.TIMESTAMP
import java.util.ArrayList;
import java.util.Date; // Para el uso de Date()
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JButton;

public class Registro extends javax.swing.JFrame implements Runnable {
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
    
    
    private javax.swing.Timer timer; // Timer para el contador
    private int tiempoRestante = 20;
    private List<ClienteInfo> listaClientes = new ArrayList<>();
    private int clienteId = 0;
    // Lista para manejar las IDs disponibles
    private Set<Integer> idsDisponibles = new HashSet<>();
    private final int MAX_CLIENTES = 2;

    /*conexion con la base de datos*/
    Conectar conect = new Conectar();
    Connection conectar = conect.Conectar();

    // Variable que representa la sala seleccionada. -1 indica que no hay ninguna seleccionada.
    private int salaSeleccionada = -1;

    // Crea una nueva instancia de la clase Cronometro.
    private Cronometro cronoFrame;

    // Array que almacena el estado de cada una de las 11 salas
    private EstadoSala[] estadosSalas = new EstadoSala[11];
    private static JButton[] salasBotones = new JButton[11];
    int conteo = 0;

    public Registro() {
        
        initComponents();
        mPropiedades=new Properties();
        setLocationRelativeTo(null); // Centra la ventana en la pantalla.
        setVisible(true); // Hace visible la ventana inmediatamente después de inicializar los componentes.

        Thread mihilo = new Thread((Runnable) this);
        mihilo.start();
        cronoFrame = new Cronometro(this);
        // Inicializa el estado de todas las salas a LIBRE.
        for (int i = 0; i < 11; i++) {
            estadosSalas[i] = EstadoSala.LIBRE;
        }

        // Array que contiene los botones correspondientes a cada sala.
        salasBotones = new JButton[]{
            jbtnsala1, jbtnsala2, jbtnsala3, jbtnsala4, jbtnsala5,
            jbtnsala6, jbtnsala7, jbtnsala8, jbtnsala9, jbtnsala10, jbtnsala11
        };

        // Recorre cada botón y le asigna un color según el estado de la sala y un ActionListener.
        for (int i = 0; i < salasBotones.length; i++) {
            final int index = i;
            salasBotones[i].setBackground(getColorPorEstado(estadosSalas[i]));

            // Asigna un evento al botón para que cuando se presione:
            salasBotones[i].addActionListener(e -> {
                if (estadosSalas[index] == EstadoSala.LIBRE) {
                    estadosSalas[index] = EstadoSala.TEMPORAL;
                    salasBotones[index].setBackground(getColorPorEstado(estadosSalas[index]));
                    cronoFrame.setMenuSalaTabIndex(index);
                    salaSeleccionada = index + 1;
                } else if (estadosSalas[index] == EstadoSala.TEMPORAL) {
                    estadosSalas[index] = EstadoSala.LIBRE;
                    salasBotones[index].setBackground(getColorPorEstado(estadosSalas[index]));
                    cronoFrame.setMenuSalaTabIndex(index);
                    salaSeleccionada = -1; // Deselecciona la sala.
                }
            });
        }
    }

          private void crearEmail() {

        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
        String FechaActual = formatoFecha.format(new Date());

        emailDestinatario = txtcorreo.getText();
        asunto = "" + txtcodigoalum.getText() + " - Notificación de reserva de sala - " + salaSeleccionada;
        contenido = "<h1>¡Felicidades!</h1>" + "<br>" + "Usted <b>ha realizado una reserva</b> para la sala de biblioteca UCV en la Fecha : " + FechaActual;

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
            JOptionPane.showMessageDialog(null, "Correo enviado");
        } catch (NoSuchProviderException ex) {
            Logger.getLogger(Correo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(Correo.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jbtnVerificar = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtcodigoalum = new javax.swing.JTextField();
        txtnumcel = new javax.swing.JTextField();
        txtdni = new javax.swing.JTextField();
        txtnombre = new javax.swing.JTextField();
        txtapellido = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        cbxintegrantes = new javax.swing.JComboBox<>();
        Sdni = new javax.swing.JSeparator();
        jLabel9 = new javax.swing.JLabel();
        sbiblioteca = new javax.swing.JSeparator();
        Sapellido = new javax.swing.JSeparator();
        SNombre = new javax.swing.JSeparator();
        Scodigo = new javax.swing.JSeparator();
        Snumero = new javax.swing.JSeparator();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtcorreo = new javax.swing.JTextField();
        Sdni1 = new javax.swing.JSeparator();
        jbtnsala1 = new javax.swing.JButton();
        jbtnsala2 = new javax.swing.JButton();
        jbtnsala3 = new javax.swing.JButton();
        jbtnsala4 = new javax.swing.JButton();
        jbtnsala5 = new javax.swing.JButton();
        jbtnsala6 = new javax.swing.JButton();
        jbtnsala7 = new javax.swing.JButton();
        jbtnsala8 = new javax.swing.JButton();
        jbtnsala9 = new javax.swing.JButton();
        jbtnsala10 = new javax.swing.JButton();
        jbtnReservar = new javax.swing.JButton();
        jbtnsala11 = new javax.swing.JButton();
        jbtnconsultar = new javax.swing.JButton();
        jbtObservaciones = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jbtnsalir1 = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Lucida Bright", 1, 48)); // NOI18N
        jLabel1.setText("BIBLIOTECA");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 50, -1, -1));

        jLabel2.setBackground(new java.awt.Color(0, 0, 0));
        jLabel2.setFont(new java.awt.Font("Rockwell", 0, 18)); // NOI18N
        jLabel2.setText("Gestión");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 140, -1, -1));

        jbtnVerificar.setBackground(new java.awt.Color(128, 0, 0));
        jbtnVerificar.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 14)); // NOI18N
        jbtnVerificar.setForeground(new java.awt.Color(255, 255, 255));
        jbtnVerificar.setText("VERIFICAR");
        jbtnVerificar.setBorder(null);
        jbtnVerificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnVerificarActionPerformed(evt);
            }
        });
        jPanel1.add(jbtnVerificar, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 630, 150, 40));

        jLabel7.setFont(new java.awt.Font("Rockwell", 0, 14)); // NOI18N
        jLabel7.setText("D.N.I.");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 440, -1, -1));

        jLabel5.setFont(new java.awt.Font("Rockwell", 0, 14)); // NOI18N
        jLabel5.setText("Número de Celular");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 350, -1, -1));

        jLabel6.setFont(new java.awt.Font("Rockwell", 0, 14)); // NOI18N
        jLabel6.setText("Apellido");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 350, -1, -1));

        txtcodigoalum.setFont(new java.awt.Font("Rockwell", 0, 12)); // NOI18N
        txtcodigoalum.setForeground(new java.awt.Color(102, 102, 102));
        txtcodigoalum.setText("Ingrese el código");
        txtcodigoalum.setBorder(null);
        txtcodigoalum.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                txtcodigoalumMousePressed(evt);
            }
        });
        txtcodigoalum.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtcodigoalumActionPerformed(evt);
            }
        });
        jPanel1.add(txtcodigoalum, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 270, 200, 40));

        txtnumcel.setEditable(false);
        txtnumcel.setBackground(new java.awt.Color(255, 255, 255));
        txtnumcel.setFont(new java.awt.Font("Rockwell", 0, 12)); // NOI18N
        txtnumcel.setForeground(new java.awt.Color(102, 102, 102));
        txtnumcel.setText("*********");
        txtnumcel.setBorder(null);
        txtnumcel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                txtnumcelMousePressed(evt);
            }
        });
        txtnumcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtnumcelActionPerformed(evt);
            }
        });
        jPanel1.add(txtnumcel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 380, 200, 30));

        txtdni.setEditable(false);
        txtdni.setBackground(new java.awt.Color(255, 255, 255));
        txtdni.setForeground(new java.awt.Color(102, 102, 102));
        txtdni.setText("********");
        txtdni.setBorder(null);
        txtdni.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                txtdniMousePressed(evt);
            }
        });
        txtdni.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtdniActionPerformed(evt);
            }
        });
        jPanel1.add(txtdni, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 470, 200, 40));

        txtnombre.setEditable(false);
        txtnombre.setBackground(new java.awt.Color(255, 255, 255));
        txtnombre.setFont(new java.awt.Font("Rockwell", 0, 12)); // NOI18N
        txtnombre.setForeground(new java.awt.Color(102, 102, 102));
        txtnombre.setText("Nombre");
        txtnombre.setBorder(null);
        txtnombre.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                txtnombreMousePressed(evt);
            }
        });
        jPanel1.add(txtnombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 270, 220, 50));

        txtapellido.setEditable(false);
        txtapellido.setBackground(new java.awt.Color(255, 255, 255));
        txtapellido.setFont(new java.awt.Font("Rockwell", 0, 12)); // NOI18N
        txtapellido.setForeground(new java.awt.Color(102, 102, 102));
        txtapellido.setText("Apellido");
        txtapellido.setBorder(null);
        txtapellido.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                txtapellidoMousePressed(evt);
            }
        });
        txtapellido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtapellidoActionPerformed(evt);
            }
        });
        jPanel1.add(txtapellido, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 380, 220, 30));

        jLabel8.setFont(new java.awt.Font("Rockwell", 0, 14)); // NOI18N
        jLabel8.setText("Integrantes");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 440, -1, 20));

        cbxintegrantes.setFont(new java.awt.Font("Rockwell", 0, 12)); // NOI18N
        cbxintegrantes.setForeground(new java.awt.Color(102, 102, 102));
        cbxintegrantes.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--Seleccione--", "3", "4", "5", "6", "7", "8", "9", "10", "11" }));
        cbxintegrantes.setBorder(null);
        cbxintegrantes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxintegrantesActionPerformed(evt);
            }
        });
        jPanel1.add(cbxintegrantes, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 460, 240, 60));

        Sdni.setBackground(new java.awt.Color(0, 0, 0));
        Sdni.setForeground(new java.awt.Color(0, 0, 0));
        jPanel1.add(Sdni, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 590, 200, 10));

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/480px-Isotipo_ucv.png"))); // NOI18N
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 60, 60, 40));

        sbiblioteca.setBackground(new java.awt.Color(0, 0, 0));
        sbiblioteca.setForeground(new java.awt.Color(0, 0, 51));
        sbiblioteca.setAutoscrolls(true);
        sbiblioteca.setDoubleBuffered(true);
        sbiblioteca.setEnabled(false);
        sbiblioteca.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        sbiblioteca.setMaximumSize(new java.awt.Dimension(3276, 3276));
        sbiblioteca.setMinimumSize(new java.awt.Dimension(200, 50));
        sbiblioteca.setName(""); // NOI18N
        sbiblioteca.setOpaque(true);
        sbiblioteca.setPreferredSize(new java.awt.Dimension(200, 50));
        jPanel1.add(sbiblioteca, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 110, 340, 2));

        Sapellido.setBackground(new java.awt.Color(0, 0, 0));
        Sapellido.setForeground(new java.awt.Color(0, 0, 0));
        jPanel1.add(Sapellido, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 420, 200, 10));

        SNombre.setBackground(new java.awt.Color(0, 0, 0));
        SNombre.setForeground(new java.awt.Color(0, 0, 0));
        jPanel1.add(SNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 320, 200, -1));

        Scodigo.setBackground(new java.awt.Color(0, 0, 0));
        Scodigo.setForeground(new java.awt.Color(0, 0, 0));
        Scodigo.setPreferredSize(new java.awt.Dimension(100, 20));
        jPanel1.add(Scodigo, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 320, 200, 10));

        Snumero.setBackground(new java.awt.Color(0, 0, 0));
        Snumero.setForeground(new java.awt.Color(0, 0, 0));
        jPanel1.add(Snumero, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 420, 200, 20));

        jLabel4.setFont(new java.awt.Font("Rockwell", 0, 14)); // NOI18N
        jLabel4.setText("Nombre");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 240, -1, -1));

        jLabel3.setFont(new java.awt.Font("Rockwell", 0, 14)); // NOI18N
        jLabel3.setText("Código de Alumno");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 240, -1, -1));

        jLabel12.setFont(new java.awt.Font("Rockwell", 0, 14)); // NOI18N
        jLabel12.setText("Correo");
        jPanel1.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 530, -1, -1));

        txtcorreo.setEditable(false);
        txtcorreo.setBackground(new java.awt.Color(255, 255, 255));
        txtcorreo.setForeground(new java.awt.Color(102, 102, 102));
        txtcorreo.setText("Exmaple@ucvvirtual.edu.pe");
        txtcorreo.setBorder(null);
        txtcorreo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtcorreoActionPerformed(evt);
            }
        });
        jPanel1.add(txtcorreo, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 550, 200, 40));

        Sdni1.setBackground(new java.awt.Color(0, 0, 0));
        Sdni1.setForeground(new java.awt.Color(0, 0, 0));
        jPanel1.add(Sdni1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 510, 200, 10));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 500, 700));

        jbtnsala1.setFont(new java.awt.Font("Showcard Gothic", 0, 13)); // NOI18N
        jbtnsala1.setText("sala 1");
        jbtnsala1.setBorder(null);
        jbtnsala1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnsala1ActionPerformed(evt);
            }
        });
        getContentPane().add(jbtnsala1, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 120, 140, 60));

        jbtnsala2.setFont(new java.awt.Font("Showcard Gothic", 0, 13)); // NOI18N
        jbtnsala2.setText("Sala 2");
        jbtnsala2.setBorder(null);
        jbtnsala2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnsala2ActionPerformed(evt);
            }
        });
        getContentPane().add(jbtnsala2, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 120, 140, 60));

        jbtnsala3.setFont(new java.awt.Font("Showcard Gothic", 0, 13)); // NOI18N
        jbtnsala3.setText("Sala 3");
        jbtnsala3.setBorder(null);
        getContentPane().add(jbtnsala3, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 120, 140, 60));

        jbtnsala4.setFont(new java.awt.Font("Showcard Gothic", 0, 13)); // NOI18N
        jbtnsala4.setText("Sala 4");
        jbtnsala4.setBorder(null);
        getContentPane().add(jbtnsala4, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 230, 140, 60));

        jbtnsala5.setFont(new java.awt.Font("Showcard Gothic", 0, 13)); // NOI18N
        jbtnsala5.setText("Sala 5");
        jbtnsala5.setBorder(null);
        getContentPane().add(jbtnsala5, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 230, 140, 60));

        jbtnsala6.setFont(new java.awt.Font("Showcard Gothic", 0, 13)); // NOI18N
        jbtnsala6.setText("Sala 6");
        jbtnsala6.setBorder(null);
        jbtnsala6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnsala6ActionPerformed(evt);
            }
        });
        getContentPane().add(jbtnsala6, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 230, 140, 60));

        jbtnsala7.setFont(new java.awt.Font("Showcard Gothic", 0, 13)); // NOI18N
        jbtnsala7.setText("Sala 7");
        jbtnsala7.setBorder(null);
        getContentPane().add(jbtnsala7, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 330, 140, 60));

        jbtnsala8.setFont(new java.awt.Font("Showcard Gothic", 0, 13)); // NOI18N
        jbtnsala8.setText("Sala 8");
        jbtnsala8.setBorder(null);
        getContentPane().add(jbtnsala8, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 330, 140, 60));

        jbtnsala9.setFont(new java.awt.Font("Showcard Gothic", 0, 13)); // NOI18N
        jbtnsala9.setText("Sala 9");
        jbtnsala9.setBorder(null);
        getContentPane().add(jbtnsala9, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 330, 140, 60));

        jbtnsala10.setFont(new java.awt.Font("Showcard Gothic", 0, 13)); // NOI18N
        jbtnsala10.setText("Sala 10");
        jbtnsala10.setBorder(null);
        jbtnsala10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnsala10ActionPerformed(evt);
            }
        });
        getContentPane().add(jbtnsala10, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 430, 140, 50));

        jbtnReservar.setBackground(new java.awt.Color(132, 0, 0));
        jbtnReservar.setFont(new java.awt.Font("Yu Gothic UI", 1, 14)); // NOI18N
        jbtnReservar.setForeground(new java.awt.Color(255, 255, 255));
        jbtnReservar.setText("RESERVAR");
        jbtnReservar.setBorder(null);
        jbtnReservar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnReservarActionPerformed(evt);
            }
        });
        getContentPane().add(jbtnReservar, new org.netbeans.lib.awtextra.AbsoluteConstraints(920, 580, 150, 40));

        jbtnsala11.setFont(new java.awt.Font("Showcard Gothic", 0, 13)); // NOI18N
        jbtnsala11.setText("Sala 11");
        jbtnsala11.setBorder(null);
        getContentPane().add(jbtnsala11, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 430, 140, 50));

        jbtnconsultar.setBackground(new java.awt.Color(132, 0, 0));
        jbtnconsultar.setFont(new java.awt.Font("Yu Gothic UI", 1, 14)); // NOI18N
        jbtnconsultar.setForeground(new java.awt.Color(255, 255, 255));
        jbtnconsultar.setText("CRONÓMETRO");
        jbtnconsultar.setBorder(null);
        jbtnconsultar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnconsultarActionPerformed(evt);
            }
        });
        getContentPane().add(jbtnconsultar, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 580, 150, 40));

        jbtObservaciones.setBackground(new java.awt.Color(132, 0, 0));
        jbtObservaciones.setFont(new java.awt.Font("Yu Gothic UI", 1, 14)); // NOI18N
        jbtObservaciones.setForeground(new java.awt.Color(255, 255, 255));
        jbtObservaciones.setText("OBSERVACIONES");
        jbtObservaciones.setBorder(null);
        jbtObservaciones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtObservacionesActionPerformed(evt);
            }
        });
        getContentPane().add(jbtObservaciones, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 580, 150, 40));

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/transparente.png"))); // NOI18N
        getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 80, 530, 450));

        jbtnsalir1.setBackground(new java.awt.Color(132, 0, 0));
        jbtnsalir1.setFont(new java.awt.Font("Yu Gothic UI", 1, 14)); // NOI18N
        jbtnsalir1.setForeground(new java.awt.Color(255, 255, 255));
        jbtnsalir1.setText("SALIR");
        jbtnsalir1.setBorder(null);
        jbtnsalir1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnsalir1ActionPerformed(evt);
            }
        });
        getContentPane().add(jbtnsalir1, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 640, 150, 40));

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Fondo azul.png"))); // NOI18N
        getContentPane().add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 0, 620, 700));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cbxintegrantesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxintegrantesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxintegrantesActionPerformed

    private void txtapellidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtapellidoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtapellidoActionPerformed

    private void txtapellidoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtapellidoMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtapellidoMousePressed

    private void txtnombreMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtnombreMousePressed
        if(txtnombre.getText().equals("Nombre")){
            txtnombre.setText("");
            txtnombre.setForeground(Color.black);
        }
        if (txtcodigoalum.getText().isEmpty()){
            txtcodigoalum.setText("Ingrese el código");
            txtcodigoalum.setForeground(Color.GRAY);
        }
        if (txtnumcel.getText().isEmpty()){
            txtnumcel.setText("*********");
            txtnumcel.setForeground(Color.GRAY);
        }
        if (txtapellido.getText().isEmpty()){
            txtapellido.setText("Apellido");
            txtapellido.setForeground(Color.GRAY);
        }
        if (txtdni.getText().isEmpty()){
            txtdni.setText("********");
            txtdni.setForeground(Color.GRAY);
        }
    }//GEN-LAST:event_txtnombreMousePressed

    private void txtdniActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtdniActionPerformed

    }//GEN-LAST:event_txtdniActionPerformed

    private void txtdniMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtdniMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtdniMousePressed

    private void txtnumcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtnumcelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtnumcelActionPerformed

    private void txtnumcelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtnumcelMousePressed

    }//GEN-LAST:event_txtnumcelMousePressed

    private void txtcodigoalumMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtcodigoalumMousePressed
        if(txtcodigoalum.getText().equals("Ingrese el código")){
            txtcodigoalum.setText("");
            txtcodigoalum.setForeground(Color.black);
        }
        if (txtnombre.getText().isEmpty()){
            txtnombre.setText("Nombre");
            txtnombre.setForeground(Color.GRAY);
        }
        if (txtnumcel.getText().isEmpty()){
            txtnumcel.setText("*********");
            txtnumcel.setForeground(Color.GRAY);
        }
        if (txtapellido.getText().isEmpty()){
            txtapellido.setText("Apellido");
            txtapellido.setForeground(Color.GRAY);
        }
        if (txtdni.getText().isEmpty()){
            txtdni.setText("********");
            txtdni.setForeground(Color.GRAY);
        }
    }//GEN-LAST:event_txtcodigoalumMousePressed

    private void jbtnVerificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnVerificarActionPerformed
        String codigoAlumno = txtcodigoalum.getText();
        
        if (!codigoAlumno.isEmpty()) {
            Estudiante estudiante = new Estudiante();
            boolean encontrado = estudiante.cargarDatos(conectar, codigoAlumno);

            if (encontrado) {
                txtdni.setText(estudiante.getDni());
                txtnombre.setText(estudiante.getNombre());
                txtapellido.setText(estudiante.getApellido());
                txtcorreo.setText(estudiante.getCorreo()); // Asumiendo que tienes un campo txtcorreo
                txtnumcel.setText(estudiante.getCelular());
                // No hay necesidad de actualizar el estado en la interfaz gráfica, a menos que sea necesario
            } else {
                JOptionPane.showMessageDialog(null, "Alumno no encontrado! o inhabilitado");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Por favor ingrese el código del alumno.");
        }
        
    }//GEN-LAST:event_jbtnVerificarActionPerformed

    /**
 * Método para guardar la información del estudiante en la base de datos.
 */


    private void jbtnsala10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnsala10ActionPerformed

    }//GEN-LAST:event_jbtnsala10ActionPerformed

    private void jbtnsala6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnsala6ActionPerformed

    }//GEN-LAST:event_jbtnsala6ActionPerformed

    private void jbtnsala1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnsala1ActionPerformed

    }//GEN-LAST:event_jbtnsala1ActionPerformed

    private void jbtnsala2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnsala2ActionPerformed

    }//GEN-LAST:event_jbtnsala2ActionPerformed

    private void jbtObservacionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtObservacionesActionPerformed
    Mostrar mostrar = new Mostrar();
    mostrar.setVisible(true);
    }//GEN-LAST:event_jbtObservacionesActionPerformed

    private void jbtnconsultarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnconsultarActionPerformed
        if (cronoFrame.isVisible()) {
            cronoFrame.setVisible(false);
        } else {
            cronoFrame.setVisible(true);
        }
    }//GEN-LAST:event_jbtnconsultarActionPerformed
    
    private void jbtnReservarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnReservarActionPerformed
     if (txtcodigoalum.getText().trim().isEmpty() || txtnombre.getText().trim().isEmpty()
                || txtapellido.getText().trim().isEmpty() || txtnumcel.getText().trim().isEmpty() || txtdni.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos requeridos.", "Campos Incompletos", JOptionPane.WARNING_MESSAGE);
        } else if (salaSeleccionada != -1) {

            String sqlComprobarSala = "SELECT ID_SALA FROM SALA WHERE ID_SALA = ? AND CAPACIDAD <= ? ";
            try {
                
                PreparedStatement ps1 = conectar.prepareStatement(sqlComprobarSala);
                int integrantes = Integer.parseInt(cbxintegrantes.getSelectedItem().toString());
                ps1.setInt(1, salaSeleccionada);
                ps1.setInt(2, integrantes);
                ResultSet rs1 = ps1.executeQuery();

                crearEmail();
                // Enviar mensaje al cliente específico para iniciar el cronómetro

                estadosSalas[salaSeleccionada - 1] = EstadoSala.OCUPADA;
                salasBotones[salaSeleccionada - 1].setBackground(getColorPorEstado(estadosSalas[salaSeleccionada - 1]));
                enviarEmail();
                
                if (listaClientes.size() > salaSeleccionada - 1) {
                    ClienteInfo clienteInfo = listaClientes.get(salaSeleccionada - 1);
                    enviarAMensajeEspecifico(clienteInfo, "iniciar contador");
                }
                cronoFrame.setMenuSalaTabIndex(salaSeleccionada - 1);
                Guardar();
                limpiarCampos();
               
            } catch (SQLException e) {

            }
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione una sala para reservar.", "Sala no seleccionada", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_jbtnReservarActionPerformed

    private void txtcodigoalumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtcodigoalumActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtcodigoalumActionPerformed

    private void txtcorreoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtcorreoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtcorreoActionPerformed

    private void jbtnsalir1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnsalir1ActionPerformed
            System.exit(0);
    }//GEN-LAST:event_jbtnsalir1ActionPerformed

public void cambiarEstadoSala(int indexSala, EstadoSala nuevoEstado) {
    if (indexSala >= 0 && indexSala < estadosSalas.length && indexSala < salasBotones.length) {
        estadosSalas[indexSala] = nuevoEstado;
        if (salasBotones[indexSala] != null) {
            salasBotones[indexSala].setBackground(getColorPorEstado(nuevoEstado));
        }
    }
}

public void Guardar() {
        String codigo = txtcodigoalum.getText();
        String sqlE = "SELECT ID_ESTUDIANTE FROM ESTUDIANTE WHERE CODIGO_ES = ?";
        String sqlA = "SELECT ID_ADMINISTRADOR FROM USUARIO";
        String sql = "INSERT INTO REGISTRO (ID_ESTUDIANTE,ID_SALA,ID_ADMINISTRADOR,INTEGRANTES,TIEMPO_INICIO,TIEMPO_FIN,OBSERVACION) VALUES (?,?,?,?,?,?,?)";

        try {
            PreparedStatement preparedStatement = conectar.prepareStatement(sqlE);
            preparedStatement.setString(1, codigo);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int idEstudiante = resultSet.getInt("ID_ESTUDIANTE");
                PreparedStatement preparedStatement1 = conectar.prepareStatement(sql);
                preparedStatement1.setInt(1, idEstudiante);
                preparedStatement1.setInt(2, salaSeleccionada);
                try {
                    PreparedStatement preparedStatement2 = conectar.prepareStatement(sqlA);
                    ResultSet resultSet1 = preparedStatement2.executeQuery();

                    if (resultSet1.next()) {
                        int id_administrador = resultSet1.getInt("ID_ADMINISTRADOR");
                        preparedStatement1.setInt(3, id_administrador);
                        
                        int integrantes = Integer.parseInt(cbxintegrantes.getSelectedItem().toString());
                        preparedStatement1.setInt(4, integrantes);
                        Date fecha = new Date();
                        long tiempoEnMilisegundos = fecha.getTime();
                        java.sql.Timestamp timestamp = new java.sql.Timestamp(tiempoEnMilisegundos);
                        preparedStatement1.setTimestamp(5, timestamp);
                        preparedStatement1.setNull(6, Types.TIMESTAMP);
                        preparedStatement1.setString(7, "--");
                        preparedStatement1.executeUpdate();
                        cronoFrame.setMenuSalaTabIndex(salaSeleccionada - 1);
                        cronoFrame.startTimer();
                        salaSeleccionada = -1;  // Resetea la sala seleccionada
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error al buscar los datos: " + ex.getMessage());
                }
            } else {
                
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al buscar los datos: " + ex.getMessage());
        }
    }
private void enviarAMensajeEspecifico(ClienteInfo clienteInfo, String mensaje) {
    try {
        DataOutputStream salida = new DataOutputStream(clienteInfo.getSocket().getOutputStream());
        salida.writeUTF(mensaje);
    } catch (IOException e) {
        e.printStackTrace();
    }
}
    private void manejarConexionCliente(Socket socketCliente, ClienteInfo infoCliente) {
        try (DataInputStream entrada = new DataInputStream(socketCliente.getInputStream()); DataOutputStream salida = new DataOutputStream(socketCliente.getOutputStream())) {

            while (!socketCliente.isClosed()) {
                String mensajeRecibido = entrada.readUTF();
            }
        } catch (IOException e) {
            // Manejar la excepción adecuadamente.
        } finally {
            manejarDesconexionCliente(infoCliente.getId());
        }
    }

private void manejarDesconexionCliente(int idCliente) {
    listaClientes.removeIf(clienteInfo -> clienteInfo.getId() == idCliente);
    idsDisponibles.add(idCliente);
}

private String formatotiempo(int TotalSegundos){
        int horas = TotalSegundos / 3600;
        int minutos = (TotalSegundos % 3600) / 60;
        int segundos = TotalSegundos % 60;

        return String.format("%02d:%02d:%02d", horas, minutos, segundos);
    }

public void limpiarCampos() {
    txtdni.setText("");
    txtnombre.setText("");
    txtapellido.setText("");
    txtnumcel.setText("");
    txtcodigoalum.setText("");
    txtcorreo.setText("");
    cbxintegrantes.setSelectedIndex(0); // Esto seleccionará el primer ítem en el JComboBox
}

public enum EstadoSala {
    LIBRE, 
    OCUPADA,
    TEMPORAL;

   
    public EstadoSala siguienteEstado() {
        switch (this) {
            case LIBRE:
                return TEMPORAL;
            case TEMPORAL:
                return OCUPADA;// Si está libre, cambia a ocupada.
            case OCUPADA:
                return LIBRE; // Si está ocupada, cambia a libre.
            default:
                return LIBRE;      // Por defecto, retorna libre (aunque en teoría, nunca debería llegar aquí).

        }
    }
}

public Color getColorPorEstado(EstadoSala estado) {
    switch (estado) {
        case LIBRE:
            return Color.GREEN;    // Si está libre, retorna verde.
        case TEMPORAL:
            return Color.ORANGE;
        case OCUPADA:
            return Color.RED;    // Si está ocupada, retorna rojo.
        default:
            return Color.GREEN;       // Por defecto, retorna verde (aunque en teoría, nunca debería llegar aquí).

    }
}
    public void run() {
        try {
            ServerSocket servidor = new ServerSocket(9999);
            while (true) {
                // Esperar hasta que haya espacio para un nuevo cliente o una ID disponible
                while (listaClientes.size() >= MAX_CLIENTES && idsDisponibles.isEmpty()) {
                    // Opcional: implementar una espera o un log aquí
                    Thread.sleep(100); // Esperar un poco antes de revisar nuevamente
                }

                Socket cliente = servidor.accept();
                int idAsignada;
                if (!idsDisponibles.isEmpty()) {
                    idAsignada = idsDisponibles.iterator().next();
                    idsDisponibles.remove(idAsignada);
                } else {
                    idAsignada = clienteId++;
                }

                ClienteInfo infoCliente = new ClienteInfo(cliente, idAsignada);
                listaClientes.add(infoCliente);

                DataOutputStream salida = new DataOutputStream(cliente.getOutputStream());
                salida.writeInt(idAsignada); // Enviar ID al cliente

                new Thread(() -> manejarConexionCliente(cliente, infoCliente)).start();
            }
        } catch (IOException | InterruptedException e) {
            JOptionPane.showMessageDialog(null, "Error : " + e);
        }
    }

    public static void main(String args[]) {
        // Este Runnable se usa para iniciar la GUI en el Event Dispatch Thread.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    Registro registro = new Registro();
                    registro.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSeparator SNombre;
    private javax.swing.JSeparator Sapellido;
    private javax.swing.JSeparator Scodigo;
    private javax.swing.JSeparator Sdni;
    private javax.swing.JSeparator Sdni1;
    private javax.swing.JSeparator Snumero;
    private javax.swing.JComboBox<String> cbxintegrantes;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton jbtObservaciones;
    private javax.swing.JButton jbtnReservar;
    private javax.swing.JButton jbtnVerificar;
    private javax.swing.JButton jbtnconsultar;
    private javax.swing.JButton jbtnsala1;
    private javax.swing.JButton jbtnsala10;
    private javax.swing.JButton jbtnsala11;
    private javax.swing.JButton jbtnsala2;
    private javax.swing.JButton jbtnsala3;
    private javax.swing.JButton jbtnsala4;
    private javax.swing.JButton jbtnsala5;
    private javax.swing.JButton jbtnsala6;
    private javax.swing.JButton jbtnsala7;
    private javax.swing.JButton jbtnsala8;
    private javax.swing.JButton jbtnsala9;
    private javax.swing.JButton jbtnsalir1;
    private javax.swing.JSeparator sbiblioteca;
    private javax.swing.JTextField txtapellido;
    private javax.swing.JTextField txtcodigoalum;
    private javax.swing.JTextField txtcorreo;
    private javax.swing.JTextField txtdni;
    private javax.swing.JTextField txtnombre;
    private javax.swing.JTextField txtnumcel;
    // End of variables declaration//GEN-END:variables
}

