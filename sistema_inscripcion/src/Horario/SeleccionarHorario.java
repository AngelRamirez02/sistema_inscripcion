/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Horario;


import conexion.Conexion;
import java.awt.Image;
import java.awt.Toolkit;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ar275
 */
public class SeleccionarHorario extends javax.swing.JFrame {
    //VARIABLES PARA CONEXION
    Conexion cx = new Conexion();
    
    //VARIABLES PARA BITACORA
    private String numControl;
    private LocalDate fechaInicioSesion;
    private LocalTime horaInicioSesion;
    
    //VARIABLES PARA ALMACENAR LAS MATERIAS SELECCIONADAS
    private ArrayList<String> materias_seleccionadas = new ArrayList<String>();
    private ArrayList<String> horarios_seleccionados = new ArrayList<String>();
   
    public SeleccionarHorario(String numControl,LocalDate fechaInicioSesion, LocalTime horaInicioSesion) throws SQLException {
        //INICIALIZAR VARIABLES PARA LA BITACORAs
        this.numControl = numControl;
        this.fechaInicioSesion = fechaInicioSesion;
        this.horaInicioSesion = horaInicioSesion;
        
        initComponents();
        configuracion_ventana();
        cargar_img();
        cargarDatos_alumno();
        personalizarTabla();
        mostrarHorarios(semestreAlumno(numControl));
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
    }
    
    private void personalizarTabla() {
        tabla_horarios.getColumnModel().getColumn(0).setMaxWidth(55); 
        tabla_horarios.getColumnModel().getColumn(1).setMaxWidth(50); 
        tabla_horarios.getColumnModel().getColumn(2).setMaxWidth(50);
        tabla_horarios.getColumnModel().getColumn(5).setMaxWidth(80); 
        tabla_horarios.getColumnModel().getColumn(6).setMaxWidth(80); 
        
        tabla_horarios.setRowHeight(30); // Establece altura de 30 píxeles

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
    
    private String semestreAlumno(String num_control) throws SQLException {
        String sql = "SELECT semestre FROM alumno WHERE num_control = ?";
        PreparedStatement ps = cx.conectar().prepareStatement(sql);
        ps.setString(1, num_control);
        ResultSet rs = ps.executeQuery();
        
        if (rs.next()) {
            return rs.getString("semestre");
        }
        return "";
    }
    
    private void mostrarHorarios(String semestre) throws SQLException {
        String sql = "SELECT \n"
                + "	g.id_grupo,\n"
                + "    g.nombre_grupo AS grupo,\n"
                + "    g.salon,\n"
                + "    g.ciclo_escolar,\n"
                + "    m.nombre_materia,\n"
                + "    CONCAT(d.nombres, ' ', d.apellido_paterno, ' ', d.apellido_materno) AS profesor,\n"
                + "    h.hora_inicio,\n"
                + "    h.hora_fin,\n"
                + "    GROUP_CONCAT(hd.dia ORDER BY FIELD(hd.dia, 'Lunes','Martes','Miercoles','Jueves','Viernes') SEPARATOR ', ') AS dias\n"
                + "FROM grupo g\n"
                + "JOIN horario h ON g.id_grupo = h.id_grupo\n"
                + "JOIN docente d ON h.rfc = d.rfc\n"
                + "JOIN horario_dias hd ON h.id_horario = hd.id_horario\n"
                + "JOIN materia m ON h.id_materia = m.id_materia\n"
                + "WHERE m.semestre = ?\n"
                + "   AND g.ciclo_escolar = '2024-2025'  -- Cambia esto al ciclo que desees\n"
                + "GROUP BY g.salon, m.nombre_materia, profesor, h.hora_inicio, h.hora_fin";
        
        //R
        PreparedStatement ps = cx.conectar().prepareStatement(sql);
        ps.setString(1, semestre);
        ResultSet rs = ps.executeQuery();
        
        //Arreglo de datos
        Object[] historial = new Object[8];
        DefaultTableModel modelo;//modelo para la tabla
        modelo = (DefaultTableModel) tabla_horarios.getModel();

        while (rs.next()) {
            //se obtienen los datos de la tabla
            historial[0] = rs.getInt("id_grupo");
            historial[1] = rs.getString("grupo");
            historial[2] = rs.getString("salon");
            historial[3] = rs.getString("nombre_materia");
            historial[4] = rs.getString("profesor");
            historial[5] = rs.getTime("hora_inicio");
            historial[6] = rs.getTime("hora_fin");
            historial[7] = rs.getString("dias");
            
            modelo.addRow(historial);
        }
        tabla_horarios.setModel(modelo);
    }
    
    private void registrarMateria(int id_grupo) throws SQLException{
        String sql = "INSERT INTO `alumno_grupo` (`num_control`, `id_grupo`) VALUES (?, ?)";
        
        PreparedStatement psm = cx.conectar().prepareStatement(sql);
        psm.setString(1, this.numControl);
        psm.setInt(2, id_grupo);
        
        int filas_insertadas = psm.executeUpdate();
        //Si se logró hacer el registro
        if(filas_insertadas > 0){
            JOptionPane.showMessageDialog(null, "Materia cargada con exito", "Materia cargada", JOptionPane.WARNING_MESSAGE);
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
        titulo = new javax.swing.JLabel();
        lb_nombreAlumno = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabla_horarios = new javax.swing.JTable();
        btn_seleccionarMateria = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setResizable(false);
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
        jPanel2.setPreferredSize(new java.awt.Dimension(998, 900));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        titulo.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        titulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titulo.setText("Selección de materias");
        jPanel2.add(titulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 10, 580, 50));

        lb_nombreAlumno.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lb_nombreAlumno.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lb_nombreAlumno.setText("jLabel6");
        jPanel2.add(lb_nombreAlumno, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 60, 970, 50));

        tabla_horarios.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tabla_horarios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id grupo", "Grupo", "Salon", "Materia", "Profesor", "Hora inicio", "Hora fin", "Dias"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabla_horarios.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tabla_horarios.getTableHeader().setReorderingAllowed(false);
        tabla_horarios.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tabla_horariosFocusGained(evt);
            }
        });
        jScrollPane2.setViewportView(tabla_horarios);

        jPanel2.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, 1150, 260));

        btn_seleccionarMateria.setText("Agregar grupo");
        btn_seleccionarMateria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_seleccionarMateriaActionPerformed(evt);
            }
        });
        jPanel2.add(btn_seleccionarMateria, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 410, 170, 40));

        jScrollPane1.setViewportView(jPanel2);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 90, 1200, 560));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        Object[] opciones = {"Aceptar", "Cancelar"};
        //dialogo que pregunta si desea confirmar salir
        int opcionSeleccionada = JOptionPane.showOptionDialog(null,
                "¿Cerrar sesión y salir?", "Confirmación de salida", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE,
                null, opciones, opciones[1]); // Por defecto, la opción seleccionada es "Cancelar"
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
                ps.setString(1, this.numControl);
                ps.setString(2, "Alumno");
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
                Logger.getLogger(SeleccionarHorario.class.getName()).log(Level.SEVERE, null, ex);
            }
            // Cerrar la aplicación
            this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
        } else {
            this.setDefaultCloseOperation(this.DO_NOTHING_ON_CLOSE);
        }
    }//GEN-LAST:event_formWindowClosing

    private void btn_seleccionarMateriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_seleccionarMateriaActionPerformed
        int fila = tabla_horarios.getSelectedRow();
        if (fila != -1) {
            String materia_nombre = tabla_horarios.getValueAt(fila, 3).toString(); // nombre de la materia
            Object id_grupo = tabla_horarios.getValueAt(fila, 0);
            String hora_materia = tabla_horarios.getValueAt(fila, 5).toString();
            
            if (materias_seleccionadas.contains(materia_nombre)) { //si la  materia ya esta seleccionada
                JOptionPane.showMessageDialog(null, "Ya tiene seleccionada esta materia", "Materia no cargada", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if(horarios_seleccionados.contains(hora_materia)){ // si la materia tiene la misma hora de inicio
                JOptionPane.showMessageDialog(null, "Ya tiene seleccionada esta materia", "Materia no cargada", JOptionPane.WARNING_MESSAGE);
                return;
            }
            //Agregar las materias a las listas            materias_seleccionadas.add(materia_nombre);
            horarios_seleccionados.add(hora_materia);
            System.out.println("Materia seleccionada: " + materia_nombre);
            System.out.println("Hora elegida: " + hora_materia);
            
            try {
                //Registrar a alumno en la BD
                registrarMateria((int) id_grupo);
            } catch (SQLException ex) {
                Logger.getLogger(SeleccionarHorario.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } else {
            JOptionPane.showMessageDialog(null, "Debe seleccionar un grupo", "Materia no cargada", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btn_seleccionarMateriaActionPerformed

    private void tabla_horariosFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tabla_horariosFocusGained
        //Validar que no haya seleccionado esa materia
        
    }//GEN-LAST:event_tabla_horariosFocusGained

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
            java.util.logging.Logger.getLogger(SeleccionarHorario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SeleccionarHorario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SeleccionarHorario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SeleccionarHorario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                    new SeleccionarHorario(null, null,null).setVisible(true);
                } catch (SQLException ex) {
                    Logger.getLogger(SeleccionarHorario.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_seleccionarMateria;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lb_nombreAlumno;
    private javax.swing.JLabel logo_ita;
    private javax.swing.JTable tabla_horarios;
    private javax.swing.JLabel titulo;
    // End of variables declaration//GEN-END:variables
}
