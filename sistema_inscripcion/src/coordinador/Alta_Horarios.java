/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package coordinador;

import bitacora.BitacoraPDF;
import com.itextpdf.text.DocumentException;
import com.mysql.cj.xdevapi.Statement;
import maestro.*;
import conexion.Conexion;
import coordinador.*;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

/**
 *
 * @author ar275
 */
public class Alta_Horarios extends javax.swing.JFrame {
   //VARIABLE DE CONEXION A DB
    Conexion cx = new Conexion();
   
   //
   private  String rfc;
   
   //VARIABLES PARA LA BITACORA
   public List<BitacoraPDF> listaBitacora = new ArrayList<BitacoraPDF>();
   BitacoraPDF registros;
   
    public Alta_Horarios(String rfc) throws SQLException {
        this.rfc = rfc;
        
        initComponents();
        configuracion_ventana();
        cargar_img();
        cargarDatos_coordinador();
        
        //Cargar materias de primer semestre
        cargar_materias(entrada_semestre.getSelectedItem().toString());
        //Cargar docentes
        cargar_docentes();
        
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
       
    private void cargar_docentes() throws SQLException{
        String sql = "SELECT rfc FROM docente";
        PreparedStatement psmt = cx.conectar().prepareStatement(sql);
        
        ResultSet rs = psmt.executeQuery();
        
        while(rs.next()){
            entrada_rfcDocente.addItem(rs.getString("rfc"));
        }
        AutoCompleteDecorator.decorate(entrada_rfcDocente);
    }
    
    private void mostrarDatosDocente(String toString) throws SQLException {
        String sql = "SELECT nombres, apellido_paterno, apellido_materno FROM docente WHERE rfc = ?";
        
        PreparedStatement psmt = cx.conectar().prepareStatement(sql);
        psmt.setString(1, entrada_rfcDocente.getSelectedItem().toString());
        
        ResultSet rs = psmt.executeQuery();
        
        //Si existe un docente con ese RFC mostrar sus datos
        if(rs.next()){
            nombre_docente.setText(rs.getString("nombres"));
            apellidoPaterno_docente.setText(rs.getString("apellido_paterno"));
            apellidoMaterno_docente.setText(rs.getString("apellido_materno"));
        }
    }
    
    private boolean altaGrupo() throws SQLException {
        String sql = "INSERT INTO `grupo` (`nombre_grupo`, `carrera`, `ciclo_escolar`, `salon`) VALUES (?, ?, ?, ?)";

        PreparedStatement psmt = cx.conectar().prepareStatement(sql);

        psmt.setString(1, entrada_nombreGrupo.getText());
        psmt.setString(2, "Ingenieria en sistemas computacionales");
        psmt.setString(3, entrada_cicloEscolar.getText());
        psmt.setString(4, entrada_salon.getText());

        //Verifica que se realizó el registro
        int filas_insertadas = psmt.executeUpdate();
        
        //Si se creo el grupo retorna verdadero
        return filas_insertadas > 0;
    }
    
    private void cargar_materias(String semestre) throws SQLException { //Carga las materias del semestre
        String sql = "SELECT nombre_materia FROM materia WHERE semestre = ?";
        PreparedStatement psmt = cx.conectar().prepareStatement(sql);
        
        psmt.setString(1, semestre);
        
        ResultSet rs = psmt.executeQuery();
        while (rs.next()) {
            entrada_materia.addItem(rs.getString("nombre_materia"));
        }
    }
    
    public static Time stringToSqlTime(String timeString) {
        try {
            // Formato de hora: HH:mm:ss
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            long ms = sdf.parse(timeString).getTime();
            return new Time(ms);
        } catch (ParseException e) {
            System.out.println("Error al convertir la cadena a Time: " + e.getMessage());
            return null;
        }
    }
    
    private void altaHorario(Time horaInicio, Time horaFin) throws SQLException {
        String sql = "INSERT INTO `horario` (`id_grupo`, `rfc`, `hora_inicio`, `hora_fin`, `id_materia`) VALUES (?, ?, ?, ?, ?)";

        PreparedStatement psmt = cx.conectar().prepareStatement(sql);
        psmt.setInt(1, ObtenerIdGrupo());
        psmt.setString(2, entrada_rfcDocente.getSelectedItem().toString());
        psmt.setTime(3, horaInicio);
        psmt.setTime(4, horaFin);
        psmt.setString(5, obtenerIdMateria(entrada_materia.getSelectedItem().toString()));

        int filas_insertadas = psmt.executeUpdate();

        //Si se crea el horario
        if (filas_insertadas > 0) {
            //Si la materia es de 5 creditos
            if (obtenerCreditosMateria() == 5) {
                //si el horario se carga correctamente
                if (altaHorarioCincoDias()) {
                    JOptionPane.showMessageDialog(null, "Grupo creado con exito", "Grupo creado", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "No se ha logrado crear el grupo", "Error a crear el grupo", JOptionPane.WARNING_MESSAGE);
                }
            }
            //Si la materia es de 4 creditos
            if (obtenerCreditosMateria() == 4) {
                //si el horario de 4 dia se crea correctamente
                if (altaHorarioCuatroDias()) {
                    JOptionPane.showMessageDialog(null, "Grupo creado con exito", "Grupo creado", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "No se ha logrado crear el grupo", "Error a crear el grupo", JOptionPane.WARNING_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "No se ha logrado crear el grupo", "Error a crear el grupo", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private boolean altaHorarioCuatroDias() throws SQLException {
        String sql = "";
        //Si la materia es de 4 creditos insertar solo 4 dias a la semana
        sql = "INSERT INTO `horario_dias` (`id_horario`, `dia`) VALUES (?, ?), (?, ?),"
                + "(?,?), (?,?)";

        PreparedStatement pstm = cx.conectar().prepareStatement(sql);
        pstm.setInt(1, ObtenerIdHorario());
        pstm.setString(2, "Lunes");
        pstm.setInt(3, ObtenerIdHorario());
        pstm.setString(4, "Martes");
        pstm.setInt(5, ObtenerIdHorario());
        pstm.setString(6, "Miercoles");
        pstm.setInt(7, ObtenerIdHorario());
        pstm.setString(8, "Jueves");
        
        int filas_insertadas = pstm.executeUpdate();
        
        if(filas_insertadas>0){
            return true;
        }
        
        return false;
    }
    
    private boolean altaHorarioCincoDias() throws SQLException {
        String sql = "INSERT INTO `horario_dias` (`id_horario`, `dia`) VALUES (?, ?), (?, ?), (?, ?), (?, ?), (?, ?)";

        PreparedStatement pstm = cx.conectar().prepareStatement(sql);
        int idHorario = ObtenerIdHorario();  // Mejor obtenerlo una vez si no cambia

        pstm.setInt(1, idHorario);
        pstm.setString(2, "Lunes");

        pstm.setInt(3, idHorario);
        pstm.setString(4, "Martes");

        pstm.setInt(5, idHorario);
        pstm.setString(6, "Miércoles");

        pstm.setInt(7, idHorario);
        pstm.setString(8, "Jueves");

        pstm.setInt(9, idHorario);
        pstm.setString(10, "Viernes");

        int filas_insertadas = pstm.executeUpdate();

        return filas_insertadas > 0;
    }

    
    //busca el id del grupo que se creo
    private int ObtenerIdGrupo() throws SQLException {
        String sql = "SELECT id_grupo FROM grupo ORDER BY id_grupo DESC LIMIT 1";
        
        PreparedStatement psmt = cx.conectar().prepareStatement(sql);
        ResultSet rs = psmt.executeQuery();
        
        if(rs.next()){
            return rs.getInt("id_grupo");
        }
        
        return -1;
    }
   
    //Busca el id de la materia seleccionada
    private String obtenerIdMateria(String nombreMateria) throws SQLException{
        String sql = "SELECT id_materia FROM materia WHERE nombre_materia = ?";
        PreparedStatement psmt = cx.conectar().prepareStatement(sql);
        psmt.setString(1, nombreMateria);
        ResultSet rs = psmt.executeQuery();
             
        if(rs.next()){
            return rs.getString("id_materia");
        }
        
        return null;
    }
    
    private int ObtenerIdHorario() throws SQLException{
        String sql = "SELECT id_horario FROM horario ORDER BY id_horario DESC LIMIT 1";
        
        PreparedStatement psmt = cx.conectar().prepareStatement(sql);
        ResultSet rs = psmt.executeQuery();
        
        if(rs.next()){
            return rs.getInt("id_horario");
        }
        
        return -1;
    }
    
    //Buscar el numero de creditos de la materia
    private int obtenerCreditosMateria() throws SQLException{
        int creditos=0;
        String sql = "SELECT creditos FROM materia WHERE id_materia = ?";
        
        PreparedStatement psmt = cx.conectar().prepareStatement(sql);
        psmt.setString(1, obtenerIdMateria(entrada_materia.getSelectedItem().toString()));
        
        ResultSet rs = psmt.executeQuery();
        
        if(rs.next()){
           creditos = rs.getInt("creditos");
        }
        
        return creditos;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dateUtil1 = new com.toedter.calendar.DateUtil();
        fondo = new javax.swing.JPanel();
        panel_logo = new javax.swing.JPanel();
        logo_ita = new javax.swing.JLabel();
        panel_contenido = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        lb_nombreCoordinador = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        entrada_nombreGrupo = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        entrada_cicloEscolar = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        entrada_semestre = new javax.swing.JComboBox<>();
        entrada_rfcDocente = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        entrada_salon = new javax.swing.JTextField();
        entrada_materia = new javax.swing.JComboBox<>();
        nombre_docente = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        apellidoPaterno_docente = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        apellidoMaterno_docente = new javax.swing.JTextField();
        entrada_horaInicio = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        entrada_horaFin = new javax.swing.JTextField();
        btn_guardarHorario = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();

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
        jPanel2.add(lb_nombreCoordinador, new org.netbeans.lib.awtextra.AbsoluteConstraints(980, 50, 180, 40));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("ALTA DE HORARIOS");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 10, 300, 50));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setText("Hora de inicio:");
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 520, 130, 40));

        entrada_nombreGrupo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                entrada_nombreGrupoActionPerformed(evt);
            }
        });
        jPanel2.add(entrada_nombreGrupo, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 140, 180, 40));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setText("Materia");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 270, 130, 40));
        jPanel2.add(entrada_cicloEscolar, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 140, 160, 40));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel4.setText("Docente:");
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 270, 130, 40));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel5.setText("Nombre grupo: ");
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, 150, 40));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel6.setText(" Ciclo escolar: ");
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 140, 130, 40));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel7.setText("Materia");
        jPanel2.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 270, 130, 40));

        entrada_semestre.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Primero", "Segundo", "Tercero" }));
        entrada_semestre.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                entrada_semestreItemStateChanged(evt);
            }
        });
        jPanel2.add(entrada_semestre, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 270, 180, 40));

        entrada_rfcDocente.setEditable(true);
        entrada_rfcDocente.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                entrada_rfcDocenteItemStateChanged(evt);
            }
        });
        jPanel2.add(entrada_rfcDocente, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 270, 200, 40));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel8.setText("Salón:");
        jPanel2.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 140, 60, 40));
        jPanel2.add(entrada_salon, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 140, 170, 40));

        jPanel2.add(entrada_materia, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 270, 240, 40));
        jPanel2.add(nombre_docente, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 410, 220, 40));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel9.setText("Semestre");
        jPanel2.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 270, 150, 40));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel10.setText("Apellido paterno:");
        jPanel2.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 410, 160, 40));
        jPanel2.add(apellidoPaterno_docente, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 410, 210, 40));

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel11.setText("Apellido Materno:");
        jPanel2.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 410, 160, 40));
        jPanel2.add(apellidoMaterno_docente, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 410, 220, 40));
        jPanel2.add(entrada_horaInicio, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 520, 120, 40));

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel12.setText("Nombre(s):");
        jPanel2.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 410, 130, 40));

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel13.setText("Hora fin:");
        jPanel2.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 520, 80, 40));
        jPanel2.add(entrada_horaFin, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 520, 120, 40));

        btn_guardarHorario.setBackground(new java.awt.Color(0, 0, 255));
        btn_guardarHorario.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn_guardarHorario.setForeground(new java.awt.Color(255, 255, 255));
        btn_guardarHorario.setText("Guardar horario");
        btn_guardarHorario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_guardarHorarioActionPerformed(evt);
            }
        });
        jPanel2.add(btn_guardarHorario, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 640, 310, 50));

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("Coordinador: ");
        jPanel2.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(980, 20, 180, 30));

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel15.setText("DOCENTE: ");
        jPanel2.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 350, 150, 30));

        panel_contenido.setViewportView(jPanel2);

        fondo.add(panel_contenido);
        panel_contenido.setBounds(0, 100, 1200, 605);

        getContentPane().add(fondo, "card2");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void entrada_nombreGrupoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_entrada_nombreGrupoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_entrada_nombreGrupoActionPerformed

    private void btn_guardarHorarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_guardarHorarioActionPerformed
        try {
            if (!altaGrupo()) {
                JOptionPane.showMessageDialog(null, "No se ha logrado crear el grupo", "Error a crear el grupo", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Alta_Horarios.class.getName()).log(Level.SEVERE, null, ex);
        }

        Time horaInicio = stringToSqlTime(entrada_horaInicio.getText());
        Time horaFin = stringToSqlTime(entrada_horaFin.getText());

        if (horaInicio == null || horaFin == null) {
            JOptionPane.showMessageDialog(this, "Formato de hora inválido. Usa HH:mm:ss");
            return;
        }

        // Límites permitidos
        Time limiteInicioMin = stringToSqlTime("07:00:00");
        Time limiteInicioMax = stringToSqlTime("18:00:00");
        Time limiteFinMin = stringToSqlTime("08:00:00");
        Time limiteFinMax = stringToSqlTime("19:00:00");

        if (horaInicio.before(limiteInicioMin) || horaInicio.after(limiteInicioMax)) {
            JOptionPane.showMessageDialog(this, "La hora de entrada debe ser entre 07:00 y 18:00.");
            return;
        }

        if (horaFin.before(limiteFinMin) || horaFin.after(limiteFinMax)) {
            JOptionPane.showMessageDialog(this, "La hora de salida debe ser entre 08:00 y 19:00.");
            return;
        }

        // Validar diferencia máxima de 2 horas
        long diferenciaMs = horaFin.getTime() - horaInicio.getTime();
        long dosHorasMs = 2 * 60 * 60 * 1000;

        if (diferenciaMs <= 0) {
            JOptionPane.showMessageDialog(this, "La hora de salida debe ser posterior a la de entrada.");
            return;
        }

        if (diferenciaMs > dosHorasMs) {
            JOptionPane.showMessageDialog(this, "La diferencia entre entrada y salida no debe superar las 2 horas.");
            return;
        }
        
        //Si se cumplen todas las validaciones del horario crear el horario
        try {
            altaHorario(horaInicio, horaFin);
        } catch (SQLException ex) {
            Logger.getLogger(Alta_Horarios.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btn_guardarHorarioActionPerformed

    private void entrada_semestreItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_entrada_semestreItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            try {
                entrada_materia.removeAllItems();
                //si selecciona un item verificar
                cargar_materias(entrada_semestre.getSelectedItem().toString());
            } catch (SQLException ex) {
                Logger.getLogger(Alta_Horarios.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_entrada_semestreItemStateChanged

    private void entrada_rfcDocenteItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_entrada_rfcDocenteItemStateChanged
        //Si se ha seleccionado un rfc
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            try {
                mostrarDatosDocente(entrada_rfcDocente.getSelectedItem().toString());
            } catch (SQLException ex) {
                Logger.getLogger(Alta_Horarios.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_entrada_rfcDocenteItemStateChanged

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
            java.util.logging.Logger.getLogger(Alta_Horarios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Alta_Horarios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Alta_Horarios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Alta_Horarios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                    new Alta_Horarios(null).setVisible(true);
                } catch (SQLException ex) {
                    Logger.getLogger(Alta_Horarios.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField apellidoMaterno_docente;
    private javax.swing.JTextField apellidoPaterno_docente;
    private javax.swing.JButton btn_guardarHorario;
    private com.toedter.calendar.DateUtil dateUtil1;
    private javax.swing.JTextField entrada_cicloEscolar;
    private javax.swing.JTextField entrada_horaFin;
    private javax.swing.JTextField entrada_horaInicio;
    private javax.swing.JComboBox<String> entrada_materia;
    private javax.swing.JTextField entrada_nombreGrupo;
    private javax.swing.JComboBox<String> entrada_rfcDocente;
    private javax.swing.JTextField entrada_salon;
    private javax.swing.JComboBox<String> entrada_semestre;
    private javax.swing.JPanel fondo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lb_nombreCoordinador;
    private javax.swing.JLabel logo_ita;
    private javax.swing.JTextField nombre_docente;
    private javax.swing.JScrollPane panel_contenido;
    private javax.swing.JPanel panel_logo;
    // End of variables declaration//GEN-END:variables

}
