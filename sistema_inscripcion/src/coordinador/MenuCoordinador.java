/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package coordinador;

import bitacora.BitacoraPDF;
import com.itextpdf.text.DocumentException;
import maestro.*;
import conexion.Conexion;
import coordinador.*;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;

/**
 *
 * @author ar275
 */
public class MenuCoordinador extends javax.swing.JFrame {
   //VARIABLE DE CONEXION A DB
    Conexion cx = new Conexion();
   
   //
   private  String rfc;
   
   //VARIABLES PARA LA BITACORA
   public List<BitacoraPDF> listaBitacora = new ArrayList<BitacoraPDF>();
   BitacoraPDF registros;
   
    public MenuCoordinador(String rfc) throws SQLException {
        this.rfc = rfc;
        
        initComponents();
        configuracion_ventana();
        cargar_img();
        cargarDatos_coordinador();
        //Centrar ventana
        this.setLocationRelativeTo(null);//La ventana aparece en el centro
    }

    //Funcion para toda la configuracion de la ventana 
    private void configuracion_ventana() {
        //Añadir el listener para detectar cuando la ventana es redimensionada
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int panelWidth = panel_contenido.getWidth();
                int panel_heigth = panel_contenido.getHeight() - panel_logo.getHeight();
                int newX = (fondo.getWidth() - panelWidth) / 2; // Calcular la nueva posición en 
                int newY = (fondo.getHeight() - panel_heigth) / 2;
                panel_logo.setSize(fondo.getWidth(), panel_logo.getHeight());
                panel_contenido.setLocation(newX, newY);
                logo_ita.setLocation(newX, logo_ita.getY());
            }
        });
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
    
    private void cargarDatosBitacora() throws SQLException {
        String sql = "SELECT *FROM historial_sesiones";//Consulta para traer los datos de las sesiones
        Object[] datos = new Object[7];//Arreglo para almacenar cada dato
        //Ejecución de consulta
        PreparedStatement ps = cx.conectar().prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            //se obtienen los datos de la tabla
            datos[0] = rs.getInt("id_sesion");
            datos[1] = rs.getString("usuario");
            datos[2] = rs.getString("tipo_usuario");
            datos[3] = rs.getDate("fecha_entrada");
            datos[4] = rs.getTime("hora_entrada");
            datos[5] = rs.getDate("fecha_salida");
            datos[6] = rs.getTime("hora_salida");
            
            //Crear un nuevo registro para la bitacora
            registros = new BitacoraPDF(datos[0].toString(),datos[1].toString(),datos[2].toString(),
                    datos[3].toString(),datos[4].toString(),datos[5].toString(),datos[6].toString());
            
            //Agregar el registro a la lista
            listaBitacora.add(registros);
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

        fondo = new javax.swing.JPanel();
        panel_logo = new javax.swing.JPanel();
        logo_ita = new javax.swing.JLabel();
        panel_contenido = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        lb_profesor_icon = new javax.swing.JLabel();
        panelRound1 = new paneles.PanelRound();
        btn_alumnos = new javax.swing.JLabel();
        panelRound3 = new paneles.PanelRound();
        jLabel3 = new javax.swing.JLabel();
        panelRound4 = new paneles.PanelRound();
        btn_grupos = new javax.swing.JLabel();
        panelRound5 = new paneles.PanelRound();
        btn_horarios = new javax.swing.JLabel();
        btn_cerrar_sesion = new paneles.PanelRound();
        jLabel2 = new javax.swing.JLabel();
        lb_nombreCoordinador = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        btn_bitacora = new paneles.PanelRound();
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setMinimumSize(new java.awt.Dimension(1230, 690));
        getContentPane().setLayout(new java.awt.CardLayout());

        fondo.setBackground(new java.awt.Color(255, 255, 255));
        fondo.setLayout(null);

        panel_logo.setBackground(new java.awt.Color(255, 255, 255));
        panel_logo.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        logo_ita.setText("LOGO ITA");
        panel_logo.add(logo_ita, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 1180, 80));

        fondo.add(panel_logo);
        panel_logo.setBounds(0, 0, 1200, 90);

        panel_contenido.setBorder(null);
        panel_contenido.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        panel_contenido.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setPreferredSize(new java.awt.Dimension(998, 700));
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
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_alumnosMousePressed(evt);
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

        jPanel2.add(panelRound3, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 230, 500, 60));

        panelRound4.setBackground(new java.awt.Color(153, 153, 153));
        panelRound4.setRoundBottomLeft(20);
        panelRound4.setRoundBottomRight(20);
        panelRound4.setRoundTopLeft(20);
        panelRound4.setRoundTopRight(20);
        panelRound4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btn_grupos.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        btn_grupos.setForeground(new java.awt.Color(51, 51, 255));
        btn_grupos.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btn_grupos.setText("Grupos");
        btn_grupos.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_grupos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_gruposMousePressed(evt);
            }
        });
        panelRound4.add(btn_grupos, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 500, 60));

        jPanel2.add(panelRound4, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 120, 500, 60));

        panelRound5.setBackground(new java.awt.Color(153, 153, 153));
        panelRound5.setRoundBottomLeft(20);
        panelRound5.setRoundBottomRight(20);
        panelRound5.setRoundTopLeft(20);
        panelRound5.setRoundTopRight(20);
        panelRound5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btn_horarios.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        btn_horarios.setForeground(new java.awt.Color(51, 51, 255));
        btn_horarios.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btn_horarios.setText("Cargar horarios");
        btn_horarios.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_horarios.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_horariosMousePressed(evt);
            }
        });
        panelRound5.add(btn_horarios, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 500, 60));

        jPanel2.add(panelRound5, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 330, 500, 60));

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

        jPanel2.add(btn_cerrar_sesion, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 530, 500, 60));

        lb_nombreCoordinador.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lb_nombreCoordinador.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lb_nombreCoordinador.setText("jLabel6");
        jPanel2.add(lb_nombreCoordinador, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 320, 340, 80));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("COORDINADOR");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 30, 300, 50));

        btn_bitacora.setBackground(new java.awt.Color(153, 153, 153));
        btn_bitacora.setRoundBottomLeft(20);
        btn_bitacora.setRoundBottomRight(20);
        btn_bitacora.setRoundTopLeft(20);
        btn_bitacora.setRoundTopRight(20);
        btn_bitacora.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_bitacoraMousePressed(evt);
            }
        });
        btn_bitacora.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(51, 51, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Imprimir bitácora");
        jLabel6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_bitacora.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 500, 60));

        jPanel2.add(btn_bitacora, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 430, 500, 60));

        panel_contenido.setViewportView(jPanel2);

        fondo.add(panel_contenido);
        panel_contenido.setBounds(0, 100, 1200, 605);

        getContentPane().add(fondo, "card2");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_cerrar_sesionMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_cerrar_sesionMousePressed
        if(SwingUtilities.isLeftMouseButton(evt)){
            Login_coordinador ventana = new Login_coordinador();
            ventana.setVisible(true);
            this.dispose();
        }
    }//GEN-LAST:event_btn_cerrar_sesionMousePressed

    private void btn_bitacoraMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_bitacoraMousePressed
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
                try {
                    // Obtener la carpeta seleccionada por el usuario
                    cargarDatosBitacora();
                } catch (SQLException ex) {
                    Logger.getLogger(SeccionAlumnos.class.getName()).log(Level.SEVERE, null, ex);
                }
                File directorioSeleccionado = fileChooser.getSelectedFile();
                String rutaCarpeta = directorioSeleccionado.getAbsolutePath();
                try {
                    registros.generarBitacora(listaBitacora, rutaCarpeta);
                    //JOptionPane.showMessageDialog(null,"PDF guardado correctamente", "Reporte Generado",JOptionPane.INFORMATION_MESSAGE);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(SeccionAlumnos.class.getName()).log(Level.SEVERE, null, ex);
                } catch (DocumentException ex) {
                    Logger.getLogger(SeccionAlumnos.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(SeccionAlumnos.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }//GEN-LAST:event_btn_bitacoraMousePressed

    private void btn_horariosMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_horariosMousePressed
        if (SwingUtilities.isLeftMouseButton(evt)){//si se da click sobre el boton de horario
            try {
                //Crear nueva ventana 
                Alta_Horarios ventana = new Alta_Horarios(rfc);
                ventana.setVisible(true);
                this.dispose();
            } catch (SQLException ex) {
                Logger.getLogger(MenuCoordinador.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }//GEN-LAST:event_btn_horariosMousePressed

    private void btn_gruposMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_gruposMousePressed
        if(SwingUtilities.isLeftMouseButton(evt)){ //si da click izquierdo
            try {
                GruposCoordinador ventana = new GruposCoordinador(this.rfc);
                ventana.setVisible(true);
                this.dispose();
            } catch (SQLException ex) {
                Logger.getLogger(MenuCoordinador.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_btn_gruposMousePressed

    private void btn_alumnosMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_alumnosMousePressed
        if(SwingUtilities.isLeftMouseButton(evt)){
            try {
                SeccionAlumnos ventana = new SeccionAlumnos(this.rfc);
                ventana.setVisible(true);
                this.dispose();
            } catch (SQLException ex) {
                Logger.getLogger(MenuCoordinador.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_btn_alumnosMousePressed

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
    private paneles.PanelRound btn_bitacora;
    private paneles.PanelRound btn_cerrar_sesion;
    private javax.swing.JLabel btn_grupos;
    private javax.swing.JLabel btn_horarios;
    private javax.swing.JPanel fondo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lb_nombreCoordinador;
    private javax.swing.JLabel lb_profesor_icon;
    private javax.swing.JLabel logo_ita;
    private paneles.PanelRound panelRound1;
    private paneles.PanelRound panelRound3;
    private paneles.PanelRound panelRound4;
    private paneles.PanelRound panelRound5;
    private javax.swing.JScrollPane panel_contenido;
    private javax.swing.JPanel panel_logo;
    // End of variables declaration//GEN-END:variables
}
