/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package coordinador;

import maestro.*;
import conexion.Conexion;
import coordinador.*;
import java.awt.Image;
import java.awt.Toolkit;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;

/**
 *
 * @author ar275
 */
public class MenuCoordinador extends javax.swing.JFrame {
    Conexion cx = new Conexion();
   private  String rfc;
   
    public MenuCoordinador(String rfc) throws SQLException {
        this.rfc = rfc;
        
        initComponents();
        configuracion_ventana();
        cargar_img();
        cargarDatos_coordinador();
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

    private void cargarDatos_coordinador() throws SQLException {
        // Seleccionar los datos del coordinador
        String consulta = "SELECT nombres, apellido_paterno, apellido_materno FROM coordinador WHERE rfc = ?";
        PreparedStatement ps = cx.conectar().prepareStatement(consulta);
        ps.setString(1, this.rfc); // Asegúrate de asignar el valor del RFC
        ResultSet rs = ps.executeQuery();

        // Arreglo de datos
        if (rs.next()) {
            String nombreCompleto = rs.getString("nombres") + " " + rs.getString("apellido_paterno") + " " + rs.getString("apellido_materno");
            lb_nombreCoordinador.setText("<html> <center>"+nombreCompleto+"</center> </html>");
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
        btn_alumnos = new javax.swing.JLabel();
        panelRound3 = new paneles.PanelRound();
        jLabel3 = new javax.swing.JLabel();
        panelRound4 = new paneles.PanelRound();
        jLabel4 = new javax.swing.JLabel();
        panelRound5 = new paneles.PanelRound();
        jLabel5 = new javax.swing.JLabel();
        btn_cerrar_sesion = new paneles.PanelRound();
        jLabel2 = new javax.swing.JLabel();
        lb_nombreCoordinador = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
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

        btn_alumnos.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        btn_alumnos.setForeground(new java.awt.Color(51, 51, 255));
        btn_alumnos.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btn_alumnos.setText("Alumnos");
        btn_alumnos.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_alumnos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_alumnosMouseClicked(evt);
            }
        });
        panelRound1.add(btn_alumnos, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 500, 60));

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
        jLabel3.setText("Estadisticas");
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
        jLabel4.setText("Profesores");
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
        jLabel5.setText("Materias");
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

        lb_nombreCoordinador.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lb_nombreCoordinador.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lb_nombreCoordinador.setText("jLabel6");
        jPanel2.add(lb_nombreCoordinador, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 320, 340, 80));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("COORDINADOR");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 30, 300, 50));

        jScrollPane1.setViewportView(jPanel2);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 90, 1200, 560));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_cerrar_sesionMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_cerrar_sesionMousePressed
        if(SwingUtilities.isLeftMouseButton(evt)){
            Login_coordinador ventana = new Login_coordinador();
            ventana.setVisible(true);
            this.dispose();
        }
    }//GEN-LAST:event_btn_cerrar_sesionMousePressed

    private void btn_alumnosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_alumnosMouseClicked
        if(SwingUtilities.isLeftMouseButton(evt)){
            try {
                SeccionAlumnos ventana = new SeccionAlumnos(this.rfc);
                ventana.setVisible(true);
                this.dispose();
            } catch (SQLException ex) {
                Logger.getLogger(MenuCoordinador.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_btn_alumnosMouseClicked

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
            java.util.logging.Logger.getLogger(MenuCoordinador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MenuCoordinador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MenuCoordinador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MenuCoordinador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new MenuCoordinador(null).setVisible(true);
                } catch (SQLException ex) {
                    Logger.getLogger(MenuCoordinador.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel btn_alumnos;
    private paneles.PanelRound btn_cerrar_sesion;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lb_nombreCoordinador;
    private javax.swing.JLabel lb_profesor_icon;
    private javax.swing.JLabel logo_ita;
    private paneles.PanelRound panelRound1;
    private paneles.PanelRound panelRound3;
    private paneles.PanelRound panelRound4;
    private paneles.PanelRound panelRound5;
    // End of variables declaration//GEN-END:variables
}
