/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package coordinador;

import Horario.ListasGrupos;
import bitacora.BitacoraPDF;
import com.itextpdf.text.DocumentException;
import conexion.Conexion;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
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
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ar275
 */
public class GruposCoordinador extends javax.swing.JFrame {
   //VARIABLE DE CONEXION A DB
    Conexion cx = new Conexion();
   
   //
   private  String rfc;
   
   //VARIABLES PARA LA BITACORA
   public List<ListasGrupos> listaAlumnos = new ArrayList<ListasGrupos>();
   ListasGrupos alumno;
   
    public GruposCoordinador(String rfc) throws SQLException {
        this.rfc = rfc;
        
        initComponents();
        configuracion_ventana();
        cargar_img();
        cargarDatos_coordinador();
        personalizarTabla();
        mostrarHorarios();
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
    
     private void personalizarTabla() {
        tabla_horarios.getColumnModel().getColumn(0).setMaxWidth(55); 
        tabla_horarios.getColumnModel().getColumn(1).setMaxWidth(50); 
        tabla_horarios.getColumnModel().getColumn(2).setMaxWidth(50);
        tabla_horarios.getColumnModel().getColumn(5).setMaxWidth(80); 
        tabla_horarios.getColumnModel().getColumn(6).setMaxWidth(80); 
        
        tabla_horarios.setRowHeight(30); // Establece altura de 30 píxeles

    }
    
    private void mostrarHorarios() throws SQLException {
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
                + "GROUP BY g.salon, m.nombre_materia, profesor, h.hora_inicio, h.hora_fin";
        
        //R
        PreparedStatement ps = cx.conectar().prepareStatement(sql);
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
    
    private void cargarListaAlumnos(int id_grupo) throws SQLException {
        listaAlumnos.clear();//limpiar la lista
        String sql = "SELECT \n"
                + "    a.num_control,\n"
                + "    a.nombres,\n"
                + "    a.apellido_paterno,\n"
                + "    a.apellido_materno\n"
                + "FROM alumno_grupo ag\n"
                + "JOIN alumno a ON ag.num_control = a.num_control\n"
                + "WHERE ag.id_grupo = ? \n"
                + "ORDER BY a.apellido_paterno ASC";
        
        PreparedStatement pstm = cx.conectar().prepareStatement(sql);
        pstm.setInt(1, id_grupo);
        
        ResultSet rs = pstm.executeQuery();
        
        while (rs.next()){
            alumno = new ListasGrupos(rs.getString("num_control"), rs.getString("apellido_paterno"), rs.getString("apellido_materno"),
                    rs.getString("nombres"));
            
            listaAlumnos.add(alumno);
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
        lb_nombreCoordinador = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabla_horarios = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        btn_seleccionarMateria = new javax.swing.JButton();

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

        lb_nombreCoordinador.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lb_nombreCoordinador.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lb_nombreCoordinador.setText("jLabel6");
        jPanel2.add(lb_nombreCoordinador, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 0, 500, 80));

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

        jPanel2.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 1150, 320));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("HORARIOS");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 30, 440, 40));

        btn_seleccionarMateria.setBackground(new java.awt.Color(0, 51, 255));
        btn_seleccionarMateria.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn_seleccionarMateria.setForeground(new java.awt.Color(255, 255, 255));
        btn_seleccionarMateria.setText("Descargar lista");
        btn_seleccionarMateria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_seleccionarMateriaActionPerformed(evt);
            }
        });
        jPanel2.add(btn_seleccionarMateria, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 500, 220, 50));

        panel_contenido.setViewportView(jPanel2);

        fondo.add(panel_contenido);
        panel_contenido.setBounds(10, 100, 1200, 605);

        getContentPane().add(fondo, "card2");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tabla_horariosFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tabla_horariosFocusGained
        //Validar que no haya seleccionado esa materia

    }//GEN-LAST:event_tabla_horariosFocusGained

    private void btn_seleccionarMateriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_seleccionarMateriaActionPerformed
        int fila = tabla_horarios.getSelectedRow();
        if (fila != -1) {
            int id_grupo = (int) tabla_horarios.getValueAt(fila, 0);
            String docente = tabla_horarios.getValueAt(fila, 4).toString();
            String grupo =tabla_horarios.getValueAt(fila, 1).toString();
            String materia = tabla_horarios.getValueAt(fila, 3).toString();
            String salon = tabla_horarios.getValueAt(fila, 2).toString();
            String hora_inicio = tabla_horarios.getValueAt(fila, 5).toString();
            String hora_fin = tabla_horarios.getValueAt(fila, 6).toString();
            try {
                cargarListaAlumnos(id_grupo);
            } catch (SQLException ex) {
                Logger.getLogger(GruposCoordinador.class.getName()).log(Level.SEVERE, null, ex);
            }
            
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
                    alumno.generarListaGrupo(listaAlumnos, grupo, docente, materia, salon, hora_inicio, hora_fin, rutaCarpeta);
                } catch (DocumentException ex) {
                    Logger.getLogger(GruposCoordinador.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(GruposCoordinador.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        } else {
            JOptionPane.showMessageDialog(null, "Debe seleccionar un grupo", "Grupo no seleccionado", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btn_seleccionarMateriaActionPerformed

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
            java.util.logging.Logger.getLogger(GruposCoordinador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GruposCoordinador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GruposCoordinador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GruposCoordinador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
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
                    new GruposCoordinador(null).setVisible(true);
                } catch (SQLException ex) {
                    Logger.getLogger(GruposCoordinador.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_seleccionarMateria;
    private javax.swing.JPanel fondo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lb_nombreCoordinador;
    private javax.swing.JLabel logo_ita;
    private javax.swing.JScrollPane panel_contenido;
    private javax.swing.JPanel panel_logo;
    private javax.swing.JTable tabla_horarios;
    // End of variables declaration//GEN-END:variables
}
