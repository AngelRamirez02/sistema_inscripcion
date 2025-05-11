/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package maestro;

import alumno.MenuAlumno;
import conexion.Conexion;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ItemEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import validacion.Validacion;

/**
 *
 * @author ar275
 */
public class CargarCalificacion extends javax.swing.JFrame {

    Conexion cx = new Conexion();

    //VARIABLES PARA BITACORA
    private String rfc;
    private LocalDate fechaInicioSesion;
    private LocalTime horaInicioSesion;
    private int id_grupo;
    private String id_materia;

    public CargarCalificacion(String rfc,LocalDate fechaInicioSesion, LocalTime horaInicioSesion, int id_grupo, String id_materia) throws SQLException {
        //INICIALIZAR VARIABLES PARA LA BITACORAS
        this.rfc = rfc;
        this.fechaInicioSesion = fechaInicioSesion;
        this.horaInicioSesion = horaInicioSesion;
        this.id_grupo=id_grupo;
        this.id_materia = id_materia;

        initComponents();
        configuracion_ventana();
        cargar_img();
        cargarDatos_profesor();
        cargarListaAlumnos(id_grupo);
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
    
    private void limpiarCampos(){
        entrada_califUnidad1.setText("");
        entrada_califUnidad2.setText("");
        entrada_califUnidad3.setText("");
    }
    
    private void cargarListaAlumnos(int id_grupo) throws SQLException {
        String sql = "SELECT \n"
                + "    a.num_control\n"
                + "FROM alumno_grupo ag\n"
                + "JOIN alumno a ON ag.num_control = a.num_control\n"
                + "WHERE ag.id_grupo = ? \n";
        
        PreparedStatement pstm = cx.conectar().prepareStatement(sql);
        pstm.setInt(1, id_grupo);
        
        ResultSet rs = pstm.executeQuery();
        
        while (rs.next()){
            num_control.addItem(rs.getString("num_control"));
        }
        AutoCompleteDecorator.decorate(num_control);
    }
    
    private void mostrarDatosAlumno() throws SQLException{
        String sql = "SELECT nombres, apellido_paterno, apellido_materno FROM alumno WHERE num_control = ?";
        
        PreparedStatement pstm = cx.conectar().prepareStatement(sql);
        pstm.setString(1, num_control.getSelectedItem().toString());
        
        ResultSet rs = pstm.executeQuery();
        
        if(rs.next()){
            apellido_paterno.setText(rs.getString("apellido_paterno"));
            apellidoMaterno.setText(rs.getString("apellido_materno"));
            nombres.setText(rs.getString("nombres"));
        }else{
            apellido_paterno.setText("");
            apellidoMaterno.setText("");
            nombres.setText("");
        }     
    }
    
    private void cargarCalificacion() throws SQLException {
        String sql = "INSERT INTO `calificacion` (`num_control`, `id_grupo`, `rfc`, "
                + "`calif_unidad1`, `calif_unidad2`, `calif_unidad3`, `calificacion_final`, `id_materia`) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement pstm = cx.conectar().prepareStatement(sql);

        pstm.setString(1, num_control.getSelectedItem().toString());
        pstm.setInt(2, this.id_grupo);
        pstm.setString(3, this.rfc);
        pstm.setInt(4, Integer.parseInt(entrada_califUnidad1.getText()));
        pstm.setInt(5, Integer.parseInt(entrada_califUnidad2.getText()));
        pstm.setInt(6, Integer.parseInt(entrada_califUnidad3.getText()));
        pstm.setInt(7, Integer.parseInt(prom_final.getText()));
        pstm.setString(8, this.id_materia);

        int filas_insertadas = pstm.executeUpdate();

        if (filas_insertadas > 0) {
            JOptionPane.showMessageDialog(null, "Calificación insertada con exito", "Calificación registrada", JOptionPane.INFORMATION_MESSAGE);
            limpiarCampos();
        } else {
            JOptionPane.showMessageDialog(null, "Hubo error al registrar la calificación", "Calificación no registrada", JOptionPane.WARNING_MESSAGE);
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
        lb_nombreProfesor = new javax.swing.JLabel();
        num_control = new javax.swing.JComboBox<>();
        nombres = new javax.swing.JTextField();
        apellido_paterno = new javax.swing.JTextField();
        apellidoMaterno = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        btn_subirCalif = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        entrada_califUnidad1 = new javax.swing.JTextField();
        btn_regresar = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        entrada_califUnidad2 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        entrada_califUnidad3 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        prom_final = new javax.swing.JTextField();

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

        lb_nombreProfesor.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lb_nombreProfesor.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lb_nombreProfesor.setText("jLabel6");
        jPanel2.add(lb_nombreProfesor, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 10, 340, 50));

        num_control.setEditable(true);
        num_control.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                num_controlItemStateChanged(evt);
            }
        });
        jPanel2.add(num_control, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 220, 190, 40));

        nombres.setEditable(false);
        jPanel2.add(nombres, new org.netbeans.lib.awtextra.AbsoluteConstraints(920, 220, 220, 40));

        apellido_paterno.setEditable(false);
        jPanel2.add(apellido_paterno, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 220, 250, 40));

        apellidoMaterno.setEditable(false);
        jPanel2.add(apellidoMaterno, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 220, 230, 40));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setText("Nombre(s)");
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(920, 170, 180, 40));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setText("No Control:");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 170, 180, 40));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setText("Apellido paterno");
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 170, 180, 40));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setText("Apellido materno");
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 170, 180, 40));

        btn_subirCalif.setBackground(new java.awt.Color(51, 51, 255));
        btn_subirCalif.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn_subirCalif.setForeground(new java.awt.Color(255, 255, 255));
        btn_subirCalif.setText("Cargar calificación");
        btn_subirCalif.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_subirCalifActionPerformed(evt);
            }
        });
        jPanel2.add(btn_subirCalif, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 490, 210, 50));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("Calificación unidad 1: ");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 330, 180, 30));

        entrada_califUnidad1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jPanel2.add(entrada_califUnidad1, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 360, 180, 40));

        btn_regresar.setText("Regresar");
        btn_regresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_regresarActionPerformed(evt);
            }
        });
        jPanel2.add(btn_regresar, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 190, 40));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setText("Calificación unidad 2: ");
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 330, 180, 30));

        entrada_califUnidad2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jPanel2.add(entrada_califUnidad2, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 360, 180, 40));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel7.setText("Calificación unidad 3: ");
        jPanel2.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 330, 180, 30));

        entrada_califUnidad3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jPanel2.add(entrada_califUnidad3, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 360, 180, 40));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel8.setText("Promedio final");
        jPanel2.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 330, 180, 30));

        prom_final.setEditable(false);
        prom_final.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jPanel2.add(prom_final, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 360, 180, 40));

        jScrollPane1.setViewportView(jPanel2);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 90, 1200, 560));

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
                    this.dispose();
                }
            } catch (SQLException ex) {
                Logger.getLogger(MenuAlumno.class.getName()).log(Level.SEVERE, null, ex);
            }
            // Cerrar la aplicación
            this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
        }
        this.setDefaultCloseOperation(this.DO_NOTHING_ON_CLOSE);
    }//GEN-LAST:event_formWindowClosing

    private void num_controlItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_num_controlItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            try {
                mostrarDatosAlumno();
            } catch (SQLException ex) {
                Logger.getLogger(CargarCalificacion.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_num_controlItemStateChanged

    private void btn_regresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_regresarActionPerformed
        try {
            MenuProfesor ventana = new MenuProfesor(rfc, this.fechaInicioSesion, this.horaInicioSesion);
            ventana.setVisible(true);
            this.dispose();
        } catch (SQLException ex) {
            Logger.getLogger(CargarCalificacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btn_regresarActionPerformed

    private void btn_subirCalifActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_subirCalifActionPerformed
        Validacion valida = new Validacion();
        
        //VALIDA QUE LAS CALIFICACIONES TENGAN UN FORMATO VALIDO
        if(!valida.validarCalif(entrada_califUnidad1.getText())){
            JOptionPane.showMessageDialog(null, "Ingrese una calificación valida para la unidad 1", "Calificación no valida", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if(!valida.validarCalif(entrada_califUnidad2.getText())){
            JOptionPane.showMessageDialog(null, "Ingrese una calificación valida para la unidad 2", "Calificación no valida", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if(!valida.validarCalif(entrada_califUnidad3.getText())){
            JOptionPane.showMessageDialog(null, "Ingrese una calificación valida para la unidad 3", "Calificación no valida", JOptionPane.WARNING_MESSAGE);
            return;
        }
        //Calcular prom final
        int u1 = Integer.parseInt(entrada_califUnidad1.getText());
        int u2 = Integer.parseInt(entrada_califUnidad2.getText());
        int u3 = Integer.parseInt(entrada_califUnidad3.getText());
        int finalProm = (u1+u2+u3)/3;
        
        //Mostrar el promedio final
        prom_final.setText(""+finalProm);
        
        try {
            cargarCalificacion();
        } catch (SQLException ex) {
            Logger.getLogger(CargarCalificacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btn_subirCalifActionPerformed

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
            java.util.logging.Logger.getLogger(CargarCalificacion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CargarCalificacion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CargarCalificacion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CargarCalificacion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new CargarCalificacion(null,null,null,2,"A").setVisible(true);
                } catch (SQLException ex) {
                    Logger.getLogger(CargarCalificacion.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField apellidoMaterno;
    private javax.swing.JTextField apellido_paterno;
    private javax.swing.JButton btn_regresar;
    private javax.swing.JButton btn_subirCalif;
    private javax.swing.JTextField entrada_califUnidad1;
    private javax.swing.JTextField entrada_califUnidad2;
    private javax.swing.JTextField entrada_califUnidad3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lb_nombreProfesor;
    private javax.swing.JLabel logo_ita;
    private javax.swing.JTextField nombres;
    private javax.swing.JComboBox<String> num_control;
    private javax.swing.JTextField prom_final;
    // End of variables declaration//GEN-END:variables
}
