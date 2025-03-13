/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package coordinador;

import alumno.*;
import conexion.Conexion;
import inicio.Inicio;
import java.awt.Image;
import java.awt.Toolkit;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author ar275
 */
public class Login_coordinador extends javax.swing.JFrame {

    /**
     * Creates new form Login_alumno
     */
    public Login_coordinador() {
        initComponents();
        configuracion_ventana();
        cargar_img();
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
        
        //CARGAR ICONO ALUMNO
        //Alumno
        Image icon_alumno= Toolkit.getDefaultToolkit().getImage(getClass().getResource("/img/icon_coordinador.png"));
        lb_alumno_icon.setIcon(new ImageIcon(icon_alumno.getScaledInstance(lb_alumno_icon.getWidth(), lb_alumno_icon.getHeight(), Image.SCALE_SMOOTH)));
        
        //Icono inscripcioo
         Image icon_inscripcion = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/img/icon_inscripcion.jpg"));
         lb_icon_inscripcion.setIcon(new ImageIcon(icon_inscripcion.getScaledInstance(lb_icon_inscripcion.getWidth(), lb_icon_inscripcion.getHeight(), Image.SCALE_SMOOTH)));
    }
    
    public void iniciar_sesion(String rfc, String password) throws SQLException {
        Conexion cx = new Conexion();//Variable para la conexion a la base de datos
        
        //Creaciond de consulta segura con Prepared
        String query = "SELECT * FROM coordinador WHERE rfc = ? and password = ?";
        //Preparar consulta
        PreparedStatement ps = cx.conectar().prepareStatement(query);
        ps.setString(1, rfc);
        ps.setString(2, password);
        //Executar consulta
        ResultSet rs = ps.executeQuery();
        
        //Si los datos del alumno son correctos redigir al menu de Alumnos
        if(rs.next()){
            MenuCoordinador ventana = new MenuCoordinador(rfc);
            ventana.setVisible(true);
            this.dispose();
        }
        //Si los datos no son correctos
        else{
             JOptionPane.showMessageDialog(null, "RFC o contraseña incorrectos", "Usuario no valido", JOptionPane.ERROR_MESSAGE);
            return;
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
        jPanel2 = new javax.swing.JPanel();
        lb_alumno_icon = new javax.swing.JLabel();
        entrada_rfc = new javax.swing.JTextField();
        password_entrada = new javax.swing.JPasswordField();
        lb_password = new javax.swing.JLabel();
        lb_numControl = new javax.swing.JLabel();
        btn_soporte = new javax.swing.JButton();
        lb_icon_inscripcion = new javax.swing.JLabel();
        btn_iniciarSesion = new javax.swing.JButton();
        mostrar_password = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        btn_regresar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        logo_ita.setText("LOGO ITA");
        jPanel1.add(logo_ita, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 780, 80));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 800, 80));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lb_alumno_icon.setText("jLabel1");
        lb_alumno_icon.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel2.add(lb_alumno_icon, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 30, 160, 160));
        jPanel2.add(entrada_rfc, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 240, 360, 50));
        jPanel2.add(password_entrada, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 340, 360, 50));

        lb_password.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lb_password.setText("Contraseña");
        jPanel2.add(lb_password, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 300, 360, 40));

        lb_numControl.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lb_numControl.setText("RFC");
        jPanel2.add(lb_numControl, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 200, 360, 40));

        btn_soporte.setBackground(new java.awt.Color(153, 153, 153));
        btn_soporte.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn_soporte.setText("?       SOPORTE");
        jPanel2.add(btn_soporte, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 450, 210, 40));

        lb_icon_inscripcion.setText("jLabel1");
        jPanel2.add(lb_icon_inscripcion, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 40, 190, 190));

        btn_iniciarSesion.setBackground(new java.awt.Color(0, 0, 255));
        btn_iniciarSesion.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn_iniciarSesion.setForeground(new java.awt.Color(255, 255, 255));
        btn_iniciarSesion.setText("Iniciar sesión");
        btn_iniciarSesion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_iniciarSesionActionPerformed(evt);
            }
        });
        jPanel2.add(btn_iniciarSesion, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 450, 220, 40));

        mostrar_password.setText("Mostrar contraseña");
        mostrar_password.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mostrar_passwordActionPerformed(evt);
            }
        });
        jPanel2.add(mostrar_password, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 400, -1, -1));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("COORDINADOR");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 20, 210, 40));

        btn_regresar.setText("Regresar");
        btn_regresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_regresarActionPerformed(evt);
            }
        });
        jPanel2.add(btn_regresar, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 100, 30));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 80, 800, 520));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mostrar_passwordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mostrar_passwordActionPerformed
        //boton para mostrar y ocultar contraseña
        if (mostrar_password.isSelected()) {
            // Mostrar la contraseña en texto
            password_entrada.setEchoChar((char) 0);
            password_entrada.requestFocusInWindow();
        } else {
            // Ocultar la contraseña con el carácter por defecto (asterisco)
            password_entrada.setEchoChar('*');
            password_entrada.requestFocusInWindow();
        }
    }//GEN-LAST:event_mostrar_passwordActionPerformed

    private void btn_iniciarSesionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_iniciarSesionActionPerformed
        //Almacena los valores de entrada
        String rfc = entrada_rfc.getText().trim();
        String password = String.valueOf(password_entrada.getPassword());
        
        //Valida que los campos no sean vacios
        if(rfc.isEmpty() || password.isEmpty()){
           JOptionPane.showMessageDialog(null, "Por favor ingrese usuario y contraseña", "Campos Vacios", JOptionPane.ERROR_MESSAGE);
            return;
       }
        
        try { 
            iniciar_sesion(rfc,password);
        } catch (SQLException ex) {
            Logger.getLogger(Login_coordinador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btn_iniciarSesionActionPerformed

    private void btn_regresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_regresarActionPerformed
        Inicio ventana = new Inicio();
        ventana.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btn_regresarActionPerformed

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
            java.util.logging.Logger.getLogger(Login_coordinador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Login_coordinador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Login_coordinador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Login_coordinador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Login_coordinador().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_iniciarSesion;
    private javax.swing.JButton btn_regresar;
    private javax.swing.JButton btn_soporte;
    private javax.swing.JTextField entrada_rfc;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lb_alumno_icon;
    private javax.swing.JLabel lb_icon_inscripcion;
    private javax.swing.JLabel lb_numControl;
    private javax.swing.JLabel lb_password;
    private javax.swing.JLabel logo_ita;
    private javax.swing.JCheckBox mostrar_password;
    private javax.swing.JPasswordField password_entrada;
    // End of variables declaration//GEN-END:variables
}
