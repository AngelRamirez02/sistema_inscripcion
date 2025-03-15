/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package alumno;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.DocumentException;
import maestro.*;
import conexion.Conexion;
import coordinador.*;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;

/**
 *
 * @author ar275
 */
public class MenuAlumno extends javax.swing.JFrame {
    Conexion cx = new Conexion();
   private  String numControl;
   
    public MenuAlumno(String numControl) throws SQLException {
        this.numControl = numControl;
        
        initComponents();
        configuracion_ventana();
        cargar_img();
        cargarDatos_alumno();
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

    private void cargarDatos_alumno() throws SQLException {
        // Seleccionar los datos del profesor
        String consulta = "SELECT nombres, apellido_paterno, apellido_materno FROM alumno WHERE num_control = ?";
        PreparedStatement ps = cx.conectar().prepareStatement(consulta);
        ps.setString(1, this.numControl); // Asegúrate de asignar el valor del RFC
        ResultSet rs = ps.executeQuery();

        // Arreglo de datos
        if (rs.next()) {
            String nombreCompleto = rs.getString("nombres") + " " + rs.getString("apellido_paterno") + " " + rs.getString("apellido_materno");
            lb_nombreAlumno.setText("<html> <center>"+nombreCompleto+"</center> </html>");
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
        btn_imprimirHorario = new javax.swing.JLabel();
        panelRound4 = new paneles.PanelRound();
        Calificaciones = new javax.swing.JLabel();
        lb_nombreAlumno = new javax.swing.JLabel();
        btn_cerrar_sesion = new paneles.PanelRound();
        btn_salir = new javax.swing.JLabel();

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

        btn_imprimirHorario.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        btn_imprimirHorario.setForeground(new java.awt.Color(51, 51, 255));
        btn_imprimirHorario.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btn_imprimirHorario.setText("Imprimir horario");
        btn_imprimirHorario.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_imprimirHorario.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_imprimirHorarioMousePressed(evt);
            }
        });
        panelRound1.add(btn_imprimirHorario, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 500, 60));

        jPanel2.add(panelRound1, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 130, 500, 60));

        panelRound4.setBackground(new java.awt.Color(153, 153, 153));
        panelRound4.setRoundBottomLeft(20);
        panelRound4.setRoundBottomRight(20);
        panelRound4.setRoundTopLeft(20);
        panelRound4.setRoundTopRight(20);
        panelRound4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        Calificaciones.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        Calificaciones.setForeground(new java.awt.Color(51, 51, 255));
        Calificaciones.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Calificaciones.setText("Consultar calificaciones");
        Calificaciones.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        panelRound4.add(Calificaciones, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 500, 60));

        jPanel2.add(panelRound4, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 240, 500, 60));

        lb_nombreAlumno.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lb_nombreAlumno.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lb_nombreAlumno.setText("jLabel6");
        jPanel2.add(lb_nombreAlumno, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 320, 340, 80));

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

        btn_salir.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        btn_salir.setForeground(new java.awt.Color(51, 51, 255));
        btn_salir.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btn_salir.setText("Cerrar sesión");
        btn_salir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_salir.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_salirMousePressed(evt);
            }
        });
        btn_cerrar_sesion.add(btn_salir, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 500, 60));

        jPanel2.add(btn_cerrar_sesion, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 340, 500, 60));

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

    private void btn_salirMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_salirMousePressed
        if(SwingUtilities.isLeftMouseButton(evt)){
            Login_alumno ventana = new Login_alumno();
            ventana.setVisible(true);
            this.dispose();
        }
    }//GEN-LAST:event_btn_salirMousePressed

    private void btn_imprimirHorarioMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_imprimirHorarioMousePressed
        //generar pdf de los registros
        if (SwingUtilities.isLeftMouseButton(evt)) {
            //Mostrar interfaz para seleccionar la carpeta
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setPreferredSize(new Dimension(800, 600));//Tamño de la ventana
            fileChooser.setDialogTitle("Seleccionar carpeta");
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); // Solo permitir seleccionar carpetas
            int opcion = fileChooser.showSaveDialog(null); // Mostrar el diálogo de guardar
            //si selecciona una ruta valida
            if (opcion == JFileChooser.APPROVE_OPTION) {
                File directorioSeleccionado = fileChooser.getSelectedFile();
                String rutaCarpeta = directorioSeleccionado.getAbsolutePath();
                try {
                   HorarioPDF x = new HorarioPDF();
                   x.PdfTodosLosAlumnos("25324651", rutaCarpeta);
                    //JOptionPane.showMessageDialog(null,"PDF guardado correctamente", "Reporte Generado",JOptionPane.INFORMATION_MESSAGE);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(SeccionAlumnos.class.getName()).log(Level.SEVERE, null, ex);
                } catch (DocumentException ex) {
                    Logger.getLogger(SeccionAlumnos.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(SeccionAlumnos.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(MenuAlumno.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }//GEN-LAST:event_btn_imprimirHorarioMousePressed

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
            java.util.logging.Logger.getLogger(MenuAlumno.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MenuAlumno.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MenuAlumno.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MenuAlumno.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new MenuAlumno(null).setVisible(true);
                } catch (SQLException ex) {
                    Logger.getLogger(MenuAlumno.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Calificaciones;
    private paneles.PanelRound btn_cerrar_sesion;
    private javax.swing.JLabel btn_imprimirHorario;
    private javax.swing.JLabel btn_salir;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lb_nombreAlumno;
    private javax.swing.JLabel lb_profesor_icon;
    private javax.swing.JLabel logo_ita;
    private paneles.PanelRound panelRound1;
    private paneles.PanelRound panelRound4;
    // End of variables declaration//GEN-END:variables
}
