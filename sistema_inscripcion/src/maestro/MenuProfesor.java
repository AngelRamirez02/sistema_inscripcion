/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package maestro;

import alumno.Login_alumno;
import alumno.MenuAlumno;
import conexion.Conexion;
import coordinador.*;
import java.awt.Image;
import java.awt.Toolkit;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author ar275
 */
public class MenuProfesor extends javax.swing.JFrame {

    Conexion cx = new Conexion();

    //VARIABLES PARA BITACORA
    private String rfc;
    private LocalDate fechaInicioSesion;
    private LocalTime horaInicioSesion;

    public MenuProfesor(String rfc,LocalDate fechaInicioSesion, LocalTime horaInicioSesion) throws SQLException {
        //INICIALIZAR VARIABLES PARA LA BITACORAS
        this.rfc = rfc;
        this.fechaInicioSesion = fechaInicioSesion;
        this.horaInicioSesion = horaInicioSesion;

        initComponents();
        configuracion_ventana();
        cargar_img();
        cargarDatos_profesor();
    }

        //Funcion para toda la configuracion de la ventana 
    private void configuracion_ventana(){        
        //Centrar ventana
        this.setLocationRelativeTo(null);//La ventana aparece en el centro
    }
    
        //Funcion para cargar imagenes
    private void cargar_img(){
        //CARGAR EL LOGO PRINCIPAL DEL TEC
        Image logo_ita_img= Toolkit.getDefaultToolkit().getImage(getClass().getResource("/img/logo_ITA.png"));
        logo_ita.setIcon(new ImageIcon(logo_ita_img.getScaledInstance(logo_ita.getWidth(), logo_ita.getHeight(), Image.SCALE_SMOOTH)));
        
        //CARGAR ICONO profesor
        //Profesor
        Image icon_alumno= Toolkit.getDefaultToolkit().getImage(getClass().getResource("/img/icon_maestro_inicio.png"));
        lb_profesor_icon.setIcon(new ImageIcon(icon_alumno.getScaledInstance(lb_profesor_icon.getWidth(), lb_profesor_icon.getHeight(), Image.SCALE_SMOOTH)));
    }

    private void cargarDatos_profesor() throws SQLException {
        // Seleccionar los datos del profesor
        String consulta = "SELECT nombres, apellido_paterno, apellido_materno FROM docente WHERE rfc = ?";
        PreparedStatement ps = cx.conectar().prepareStatement(consulta);
        ps.setString(1, rfc); // Asegúrate de asignar el valor del RFC
        ResultSet rs = ps.executeQuery();

        // Arreglo de datos
        if (rs.next()) {
            String nombreCompleto = rs.getString("nombres") + " " + rs.getString("apellido_paterno") + " " + rs.getString("apellido_materno");
            lb_nombreProfesor.setText("<html> <center>"+nombreCompleto+"</center> </html>");
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        logo_ita = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        lb_profesor_icon = new javax.swing.JLabel();
        panelRound1 = new paneles.PanelRound();
        jLabel1 = new javax.swing.JLabel();
        panelRound3 = new paneles.PanelRound();
        jLabel3 = new javax.swing.JLabel();
        panelRound4 = new paneles.PanelRound();
        jLabel4 = new javax.swing.JLabel();
        panelRound5 = new paneles.PanelRound();
        jLabel5 = new javax.swing.JLabel();
        btn_cerrar_sesion = new paneles.PanelRound();
        jLabel2 = new javax.swing.JLabel();
        lb_nombreProfesor = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        logo_ita.setText("LOGO ITA");
        jPanel1.add(logo_ita, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 1180, 80));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1200, 90));

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setPreferredSize(new java.awt.Dimension(998, 500));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lb_profesor_icon.setText("jLabel1");
        lb_profesor_icon.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel2.add(lb_profesor_icon, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 100, 260, 230));

        panelRound1.setBackground(new java.awt.Color(153, 153, 153));
        panelRound1.setRoundBottomLeft(20);
        panelRound1.setRoundBottomRight(20);
        panelRound1.setRoundTopLeft(20);
        panelRound1.setRoundTopRight(20);
        panelRound1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(51, 51, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Visualizar grupos");
        jLabel1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        panelRound1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 500, 60));

        jPanel2.add(panelRound1, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 30, 500, 60));

        panelRound3.setBackground(new java.awt.Color(153, 153, 153));
        panelRound3.setRoundBottomLeft(20);
        panelRound3.setRoundBottomRight(20);
        panelRound3.setRoundTopLeft(20);
        panelRound3.setRoundTopRight(20);
        panelRound3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(51, 51, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Visualizar horario");
        jLabel3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        panelRound3.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 500, 60));

        jPanel2.add(panelRound3, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 250, 500, 60));

        panelRound4.setBackground(new java.awt.Color(153, 153, 153));
        panelRound4.setRoundBottomLeft(20);
        panelRound4.setRoundBottomRight(20);
        panelRound4.setRoundTopLeft(20);
        panelRound4.setRoundTopRight(20);
        panelRound4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(51, 51, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Capturar calificaciones");
        jLabel4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        panelRound4.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 500, 60));

        jPanel2.add(panelRound4, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 140, 500, 60));

        panelRound5.setBackground(new java.awt.Color(153, 153, 153));
        panelRound5.setRoundBottomLeft(20);
        panelRound5.setRoundBottomRight(20);
        panelRound5.setRoundTopLeft(20);
        panelRound5.setRoundTopRight(20);
        panelRound5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(51, 51, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Imprimir horario");
        jLabel5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        panelRound5.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 500, 60));

        jPanel2.add(panelRound5, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 360, 500, 60));

        btn_cerrar_sesion.setBackground(new java.awt.Color(153, 153, 153));
        btn_cerrar_sesion.setRoundBottomLeft(20);
        btn_cerrar_sesion.setRoundBottomRight(20);
        btn_cerrar_sesion.setRoundTopLeft(20);
        btn_cerrar_sesion.setRoundTopRight(20);
        btn_cerrar_sesion.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_cerrar_sesionMousePressed(evt);
            }
        });
        btn_cerrar_sesion.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(51, 51, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Cerrar sesión");
        jLabel2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_cerrar_sesion.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 500, 60));

        jPanel2.add(btn_cerrar_sesion, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 470, 500, 60));

        lb_nombreProfesor.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lb_nombreProfesor.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lb_nombreProfesor.setText("jLabel6");
        jPanel2.add(lb_nombreProfesor, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 320, 340, 80));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("DOCENTE");
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 30, 300, 50));

        jScrollPane1.setViewportView(jPanel2);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 90, 1200, 560));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_cerrar_sesionMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_cerrar_sesionMousePressed
        if (SwingUtilities.isLeftMouseButton(evt)) {
            Object[] opciones = {"Aceptar", "Cancelar"};
            // Si existe información que no ha sido guardada
            // Mostrar diálogo que pregunta si desea confirmar la salida
            int opcionSeleccionada = JOptionPane.showOptionDialog(
                    null,
                    "¿Cerrar sesión?",
                    "Confirmación de salida",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE,
                    null,
                    opciones,
                    opciones[1]); // Por defecto, la opción seleccionada es "Cancelar"

            // Manejar las opciones seleccionadas
            if (opcionSeleccionada == JOptionPane.YES_OPTION) {
                //Creacion de consulta para el historial de sesione
                LocalTime horaFinSesion = LocalTime.now();//Hora de salida
                LocalDate fecha_salida = LocalDate.now();//Fecha de salida
                String sql = "INSERT INTO historial_sesiones"
                    + "(usuario,tipo_usuario,fecha_entrada, hora_entrada, fecha_salida, hora_salida)"
                    + "values (?,?,?,?,?,?)";
            try {
                PreparedStatement ps = cx.conectar().prepareStatement(sql);//Creacion de la consulta
                ps.setString(1, this.rfc);
                ps.setString(2, "Docente");
                ps.setObject(3, this.fechaInicioSesion);
                ps.setObject(4, this.horaInicioSesion);
                ps.setObject(5, fecha_salida);
                ps.setObject(6, horaFinSesion);
                    // Paso 4: Ejecutar la consulta
                    int rowsInserted = ps.executeUpdate();
                    if (rowsInserted > 0) {
                        System.out.println("Historial guardado");
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(MenuAlumno.class.getName()).log(Level.SEVERE, null, ex);
                }
                //Cerrar la sesión y volver al login del maestro
                Login_maestro ventana = new Login_maestro();
                ventana.setVisible(true);
                this.dispose();
            }
        }
    }//GEN-LAST:event_btn_cerrar_sesionMousePressed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        Object[] opciones = {"Aceptar", "Cancelar"};
        // Si existe información que no ha sido guardada
        // Mostrar diálogo que pregunta si desea confirmar la salida
        int opcionSeleccionada = JOptionPane.showOptionDialog(
                null,
                "¿Cerrar sesión?",
                "Confirmación de salida",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null,
                opciones,
                opciones[1]); // Por defecto, la opción seleccionada es "Cancelar"

        // Manejar las opciones seleccionadas
        if (opcionSeleccionada == JOptionPane.YES_OPTION) {
            //Creacion de consulta para el historial de sesione
            LocalTime horaFinSesion = LocalTime.now();//Hora de salida
            LocalDate fecha_salida = LocalDate.now();//Fecha de salida
            String sql = "INSERT INTO historial_sesiones"
                    + "(usuario,tipo_usuario,fecha_entrada, hora_entrada, fecha_salida, hora_salida)"
                    + "values (?,?,?,?,?,?)";
            try {
                PreparedStatement ps = cx.conectar().prepareStatement(sql);//Creacion de la consulta
                ps.setString(1, this.rfc);
                ps.setString(2, "Docente");
                ps.setObject(3, this.fechaInicioSesion);
                ps.setObject(4, this.horaInicioSesion);
                ps.setObject(5, fecha_salida);
                ps.setObject(6, horaFinSesion);
                // Paso 4: Ejecutar la consulta
                int rowsInserted = ps.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Historial guardado");
                }
            } catch (SQLException ex) {
                Logger.getLogger(MenuAlumno.class.getName()).log(Level.SEVERE, null, ex);
            }
            // Cerrar la aplicación
            this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
        }
        this.setDefaultCloseOperation(this.DO_NOTHING_ON_CLOSE);
    }//GEN-LAST:event_formWindowClosing

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
            java.util.logging.Logger.getLogger(MenuProfesor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MenuProfesor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MenuProfesor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MenuProfesor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new MenuProfesor(null,null,null).setVisible(true);
                } catch (SQLException ex) {
                    Logger.getLogger(MenuProfesor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private paneles.PanelRound btn_cerrar_sesion;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lb_nombreProfesor;
    private javax.swing.JLabel lb_profesor_icon;
    private javax.swing.JLabel logo_ita;
    private paneles.PanelRound panelRound1;
    private paneles.PanelRound panelRound3;
    private paneles.PanelRound panelRound4;
    private paneles.PanelRound panelRound5;
    // End of variables declaration//GEN-END:variables
}
