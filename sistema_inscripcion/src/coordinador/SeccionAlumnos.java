/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package coordinador;

import com.itextpdf.text.DocumentException;
import conexion.Conexion;
import java.awt.Desktop;
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
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import javax.swing.SwingUtilities;

/**
 *
 * @author ar275
 */
    

public class SeccionAlumnos extends javax.swing.JFrame {

    public List<AlumnosPDF> listAlumnos = new ArrayList<AlumnosPDF>();
    Conexion cx = new Conexion();
    private String rfc_coordinador;
    AlumnosPDF x;

    public SeccionAlumnos(String rfc) throws SQLException {
        this.rfc_coordinador = rfc;

        initComponents();
        configuracion_ventana();
        cargar_img();
        //Centrar ventana
        this.setLocationRelativeTo(null);//La ventana aparece en el centro
         //No mostrar el panel de datos al inicio
        panel_datos.setVisible(false);      
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
        
        Image img_buscar = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/img/icon_buscar.png"));
        icon_buscar.setIcon(new ImageIcon(img_buscar.getScaledInstance(icon_buscar.getWidth(), icon_buscar.getHeight(), Image.SCALE_SMOOTH))); 
        
        Image img_descarga = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/img/descarga_icono.png"));
        btn_descargarPDF.setIcon(new ImageIcon(img_descarga.getScaledInstance(btn_descargarPDF.getWidth(), btn_descargarPDF.getHeight(), Image.SCALE_SMOOTH)));
    }
    
    private void buscar_alumno(String numControl) throws SQLException {
        String sql = "SELECT * FROM alumno WHERE num_control = ?";
        PreparedStatement ps = cx.conectar().prepareStatement(sql);
        ps.setString(1, numControl);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            //mostrar el panel con los datos
            panel_datos.setVisible(true);
            //datos del alumno
            nombres.setText(rs.getString("nombres"));
            apellido_paterno.setText(rs.getString("apellido_paterno"));
            apellido_materno.setText(rs.getString("apellido_materno"));
            this.numControl.setText(numControl);
            semestre.setText(rs.getString("semestre"));
            carrera.setText(rs.getString("carrera"));
            num_telefono.setText(rs.getString("telefono"));
            correo.setText(rs.getString("correo"));
            fecha_nacimiento.setText(rs.getDate("fecha_nacimiento").toString());
            codigo_postal.setText(rs.getString("codigo_postal"));
            estado.setText(rs.getString("estado"));
            municipio.setText(rs.getString("municipio"));
            colonia.setText(rs.getString("colonia"));
            calle.setText(rs.getString("calle"));
            num_Interior.setText(rs.getString("num_interior"));
            num_exterior.setText(rs.getString("num_exterior"));
            CargarDocumentos(numControl);
        } else {
            JOptionPane.showMessageDialog(null, "No se encontó a ningun alumno con el numero de control ingresado", "Registro no encontrado", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void CargarDocumentos(String numeroControl) throws SQLException{
         String sql = "SELECT * FROM documentos WHERE num_control = ?";
        PreparedStatement ps = cx.conectar().prepareStatement(sql);
        ps.setString(1, numeroControl);
        ResultSet rs = ps.executeQuery();
        
        if(rs.next()){
            ruta_acta.setText(rs.getString("acta_nacimiento"));
            ruta_certificado.setText(rs.getString("certificado_bachillerato"));
            ruta_curp.setText(rs.getString("curp"));
            if(rs.getString("ine")!=null){
                ruta_ine.setText(rs.getString("ine"));
            }else{
                ruta_ine.setText("SN");
            }
        }
    }
    
    private void abrirDocumento(String rutaArchivo){
                //Abre el archivo generado
        File path = new File(rutaArchivo);
        try {
            Desktop.getDesktop().open(path);
        } catch (IOException ex) {
            Logger.getLogger(AlumnosPDF.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
     private boolean eliminarCarpeta(File folder) {
        if (!folder.exists()) {
            return false; // La carpeta no existe
        }
        
        File[] files = folder.listFiles();
        if (files != null) { // Eliminar archivos y subcarpetas
            for (File file : files) {
                if (file.isDirectory()) {
                    eliminarCarpeta(file); // Llamada recursiva
                } else {
                    file.delete(); // Eliminar archivo
                }
            }
        }
        return folder.delete(); // Eliminar la carpeta vacía
    }
    
    private void eliminar_alumno(String numControl) {
        try {
            //consulta para eliminar
            String sql = "DELETE FROM alumno WHERE num_control = ?";
            PreparedStatement ps = cx.conectar().prepareStatement(sql);
            ps.setString(1, numControl);
            //ejecutar consulta
            int filas_eliminadas = ps.executeUpdate();
            //verificar si se elimaron los datos
            if (filas_eliminadas > 0) {
                String folderPath = "documentos_alumnos/"+numControl; // Reemplazar con la ruta real
                File folder = new File(folderPath);
                eliminarCarpeta(folder);
                JOptionPane.showMessageDialog(null, "Alumno eliminado exitosamente", "Eliminación exitosa", JOptionPane.INFORMATION_MESSAGE);
                numControl_busqueda.setText("");
                panel_datos.setVisible(false);
                //Regresar al comienzo de la pantalla
                JScrollBar verticalBar = panel_contenido.getVerticalScrollBar();
                verticalBar.setValue(verticalBar.getMinimum());
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró el registro con la CURP especificada", "Error en la eliminación", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    private void cargarDatoAlumnos() throws SQLException {
        String sql = "SELECT num_control,nombres, apellido_paterno, apellido_materno, fecha_nacimiento,carrera, semestre FROM alumno";
        PreparedStatement ps = cx.conectar().prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        Object[] alumno = new Object[7];
        while (rs.next()) {
            //se obtienen los datos de la tabla
            alumno[0] = rs.getString("num_control");
            alumno[1] = rs.getString("apellido_paterno");
            alumno[2] = rs.getString("apellido_materno");
            alumno[3] = rs.getString("nombres");
            alumno[4] = rs.getDate("fecha_nacimiento").toString();
            alumno[5] = rs.getString("carrera");
            alumno[6] = rs.getString("semestre");
            x = new AlumnosPDF(alumno[0].toString(), alumno[1].toString(), alumno[2].toString(), alumno[3].toString(), alumno[4].toString(), alumno[5].toString(), alumno[6].toString());
            listAlumnos.add(x);
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
        numControl_busqueda = new javax.swing.JTextField();
        lb_inicial = new javax.swing.JLabel();
        icon_buscar = new javax.swing.JLabel();
        panel_datos = new javax.swing.JPanel();
        titulo_numControl = new javax.swing.JTextField();
        numControl = new javax.swing.JTextField();
        titulo_apellidoPaterno = new javax.swing.JTextField();
        titulo_apellidoMaterno = new javax.swing.JTextField();
        apellido_materno = new javax.swing.JTextField();
        titulo_nombres = new javax.swing.JTextField();
        nombres = new javax.swing.JTextField();
        apellido_paterno = new javax.swing.JTextField();
        lb_datosAlumno = new javax.swing.JLabel();
        titulo_correo = new javax.swing.JTextField();
        correo = new javax.swing.JTextField();
        titulo_carrera = new javax.swing.JTextField();
        titulo_semestre = new javax.swing.JTextField();
        semestre = new javax.swing.JTextField();
        titulo_telefono = new javax.swing.JTextField();
        num_telefono = new javax.swing.JTextField();
        carrera = new javax.swing.JTextField();
        titulo_colonia = new javax.swing.JTextField();
        colonia = new javax.swing.JTextField();
        titulo_estado = new javax.swing.JTextField();
        titulo_municipio = new javax.swing.JTextField();
        municipio = new javax.swing.JTextField();
        estado = new javax.swing.JTextField();
        titulo_calle = new javax.swing.JTextField();
        calle = new javax.swing.JTextField();
        titulo_numInterior = new javax.swing.JTextField();
        titulo_numExterior = new javax.swing.JTextField();
        num_exterior = new javax.swing.JTextField();
        num_Interior = new javax.swing.JTextField();
        btn_eliminar = new javax.swing.JButton();
        btn_editar = new javax.swing.JButton();
        lb_datosAlumno3 = new javax.swing.JLabel();
        titulo_calle1 = new javax.swing.JTextField();
        codigo_postal = new javax.swing.JTextField();
        titulo_fechaNacimiento = new javax.swing.JTextField();
        fecha_nacimiento = new javax.swing.JTextField();
        ruta_ine = new javax.swing.JLabel();
        ruta_acta = new javax.swing.JLabel();
        ruta_curp = new javax.swing.JLabel();
        ruta_certificado = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        btn_descargarPDF = new javax.swing.JLabel();
        btn_regresar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setMinimumSize(new java.awt.Dimension(1220, 650));
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

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setMinimumSize(new java.awt.Dimension(1190, 1000));
        jPanel2.setPreferredSize(new java.awt.Dimension(998, 1200));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        numControl_busqueda.setColumns(1);
        numControl_busqueda.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        numControl_busqueda.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        numControl_busqueda.setActionCommand("<Not Set>");
        numControl_busqueda.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        numControl_busqueda.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        jPanel2.add(numControl_busqueda, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 140, 710, 50));

        lb_inicial.setFont(new java.awt.Font("Roboto", 1, 18)); // NOI18N
        lb_inicial.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lb_inicial.setText("INGRESE EL NUMERO DE CONTROL DEL ALUMNO A CONSULTAR");
        jPanel2.add(lb_inicial, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 110, 710, 30));

        icon_buscar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        icon_buscar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                icon_buscarMouseClicked(evt);
            }
        });
        jPanel2.add(icon_buscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 130, 70, 70));

        panel_datos.setBackground(new java.awt.Color(255, 255, 255));
        panel_datos.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        titulo_numControl.setEditable(false);
        titulo_numControl.setBackground(new java.awt.Color(102, 102, 255));
        titulo_numControl.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        titulo_numControl.setForeground(new java.awt.Color(255, 255, 255));
        titulo_numControl.setText("Numero de control");
        titulo_numControl.setBorder(null);
        titulo_numControl.setFocusable(false);
        panel_datos.add(titulo_numControl, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 100, 230, 40));

        numControl.setEditable(false);
        numControl.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        numControl.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        numControl.setText("jTextField1");
        numControl.setBorder(null);
        numControl.setFocusable(false);
        panel_datos.add(numControl, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 140, 230, 40));

        titulo_apellidoPaterno.setEditable(false);
        titulo_apellidoPaterno.setBackground(new java.awt.Color(102, 102, 255));
        titulo_apellidoPaterno.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        titulo_apellidoPaterno.setForeground(new java.awt.Color(255, 255, 255));
        titulo_apellidoPaterno.setText("   Apellido Paterno");
        titulo_apellidoPaterno.setBorder(null);
        titulo_apellidoPaterno.setFocusable(false);
        panel_datos.add(titulo_apellidoPaterno, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 100, 250, 40));

        titulo_apellidoMaterno.setEditable(false);
        titulo_apellidoMaterno.setBackground(new java.awt.Color(102, 102, 255));
        titulo_apellidoMaterno.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        titulo_apellidoMaterno.setForeground(new java.awt.Color(255, 255, 255));
        titulo_apellidoMaterno.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        titulo_apellidoMaterno.setText("    Apellido Materno");
        titulo_apellidoMaterno.setBorder(null);
        titulo_apellidoMaterno.setFocusable(false);
        titulo_apellidoMaterno.setMargin(new java.awt.Insets(10, 6, 2, 6));
        panel_datos.add(titulo_apellidoMaterno, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 100, 230, 40));

        apellido_materno.setEditable(false);
        apellido_materno.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        apellido_materno.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        apellido_materno.setText("jTextField1");
        apellido_materno.setBorder(null);
        apellido_materno.setFocusable(false);
        panel_datos.add(apellido_materno, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 140, 230, 40));

        titulo_nombres.setEditable(false);
        titulo_nombres.setBackground(new java.awt.Color(102, 102, 255));
        titulo_nombres.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        titulo_nombres.setForeground(new java.awt.Color(255, 255, 255));
        titulo_nombres.setText("  Nombre(s)");
        titulo_nombres.setBorder(null);
        titulo_nombres.setFocusable(false);
        panel_datos.add(titulo_nombres, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 100, 280, 40));

        nombres.setEditable(false);
        nombres.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        nombres.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        nombres.setText("jTextField1");
        nombres.setBorder(null);
        nombres.setFocusable(false);
        panel_datos.add(nombres, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 140, 280, 40));

        apellido_paterno.setEditable(false);
        apellido_paterno.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        apellido_paterno.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        apellido_paterno.setText("jTextField1");
        apellido_paterno.setBorder(null);
        apellido_paterno.setFocusable(false);
        panel_datos.add(apellido_paterno, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 140, 250, 40));

        lb_datosAlumno.setFont(new java.awt.Font("Roboto", 1, 24)); // NOI18N
        lb_datosAlumno.setText("Datos personales");
        panel_datos.add(lb_datosAlumno, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 60, 350, 40));

        titulo_correo.setEditable(false);
        titulo_correo.setBackground(new java.awt.Color(102, 102, 255));
        titulo_correo.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        titulo_correo.setForeground(new java.awt.Color(255, 255, 255));
        titulo_correo.setText("Correo electronico");
        titulo_correo.setBorder(null);
        titulo_correo.setFocusable(false);
        panel_datos.add(titulo_correo, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 200, 240, 40));

        correo.setEditable(false);
        correo.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        correo.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        correo.setText("jTextField1");
        correo.setBorder(null);
        correo.setFocusable(false);
        panel_datos.add(correo, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 240, 240, 40));

        titulo_carrera.setEditable(false);
        titulo_carrera.setBackground(new java.awt.Color(102, 102, 255));
        titulo_carrera.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        titulo_carrera.setForeground(new java.awt.Color(255, 255, 255));
        titulo_carrera.setText("Carrera");
        titulo_carrera.setBorder(null);
        titulo_carrera.setFocusable(false);
        panel_datos.add(titulo_carrera, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 200, 250, 40));

        titulo_semestre.setEditable(false);
        titulo_semestre.setBackground(new java.awt.Color(102, 102, 255));
        titulo_semestre.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        titulo_semestre.setForeground(new java.awt.Color(255, 255, 255));
        titulo_semestre.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        titulo_semestre.setText("Semestre");
        titulo_semestre.setBorder(null);
        titulo_semestre.setFocusable(false);
        titulo_semestre.setMargin(new java.awt.Insets(10, 6, 2, 6));
        panel_datos.add(titulo_semestre, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 200, 230, 40));

        semestre.setEditable(false);
        semestre.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        semestre.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        semestre.setText("jTextField1");
        semestre.setBorder(null);
        semestre.setFocusable(false);
        panel_datos.add(semestre, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 240, 230, 40));

        titulo_telefono.setEditable(false);
        titulo_telefono.setBackground(new java.awt.Color(102, 102, 255));
        titulo_telefono.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        titulo_telefono.setForeground(new java.awt.Color(255, 255, 255));
        titulo_telefono.setText("Numero de telefono");
        titulo_telefono.setBorder(null);
        titulo_telefono.setFocusable(false);
        panel_datos.add(titulo_telefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 200, 270, 40));

        num_telefono.setEditable(false);
        num_telefono.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        num_telefono.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        num_telefono.setText("jTextField1");
        num_telefono.setBorder(null);
        num_telefono.setFocusable(false);
        panel_datos.add(num_telefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 240, 270, 40));

        carrera.setEditable(false);
        carrera.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        carrera.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        carrera.setText("jTextField1");
        carrera.setBorder(null);
        carrera.setFocusable(false);
        panel_datos.add(carrera, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 240, 250, 40));

        titulo_colonia.setEditable(false);
        titulo_colonia.setBackground(new java.awt.Color(102, 102, 255));
        titulo_colonia.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        titulo_colonia.setForeground(new java.awt.Color(255, 255, 255));
        titulo_colonia.setText("Colonia");
        titulo_colonia.setBorder(null);
        titulo_colonia.setFocusable(false);
        panel_datos.add(titulo_colonia, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 420, 230, 40));

        colonia.setEditable(false);
        colonia.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        colonia.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        colonia.setText("jTextField1");
        colonia.setBorder(null);
        colonia.setFocusable(false);
        panel_datos.add(colonia, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 460, 230, 40));

        titulo_estado.setEditable(false);
        titulo_estado.setBackground(new java.awt.Color(102, 102, 255));
        titulo_estado.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        titulo_estado.setForeground(new java.awt.Color(255, 255, 255));
        titulo_estado.setText("Estado");
        titulo_estado.setBorder(null);
        titulo_estado.setFocusable(false);
        panel_datos.add(titulo_estado, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 420, 250, 40));

        titulo_municipio.setEditable(false);
        titulo_municipio.setBackground(new java.awt.Color(102, 102, 255));
        titulo_municipio.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        titulo_municipio.setForeground(new java.awt.Color(255, 255, 255));
        titulo_municipio.setText("Municipio");
        titulo_municipio.setBorder(null);
        titulo_municipio.setFocusable(false);
        panel_datos.add(titulo_municipio, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 420, 280, 40));

        municipio.setEditable(false);
        municipio.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        municipio.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        municipio.setText("jTextField1");
        municipio.setBorder(null);
        municipio.setFocusable(false);
        panel_datos.add(municipio, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 460, 280, 40));

        estado.setEditable(false);
        estado.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        estado.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        estado.setText("jTextField1");
        estado.setBorder(null);
        estado.setFocusable(false);
        panel_datos.add(estado, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 460, 250, 40));

        titulo_calle.setEditable(false);
        titulo_calle.setBackground(new java.awt.Color(102, 102, 255));
        titulo_calle.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        titulo_calle.setForeground(new java.awt.Color(255, 255, 255));
        titulo_calle.setText("Calle");
        titulo_calle.setBorder(null);
        titulo_calle.setFocusable(false);
        panel_datos.add(titulo_calle, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 520, 230, 40));

        calle.setEditable(false);
        calle.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        calle.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        calle.setText("jTextField1");
        calle.setBorder(null);
        calle.setFocusable(false);
        panel_datos.add(calle, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 560, 230, 40));

        titulo_numInterior.setEditable(false);
        titulo_numInterior.setBackground(new java.awt.Color(102, 102, 255));
        titulo_numInterior.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        titulo_numInterior.setForeground(new java.awt.Color(255, 255, 255));
        titulo_numInterior.setText("N° Interior");
        titulo_numInterior.setBorder(null);
        titulo_numInterior.setFocusable(false);
        panel_datos.add(titulo_numInterior, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 520, 250, 40));

        titulo_numExterior.setEditable(false);
        titulo_numExterior.setBackground(new java.awt.Color(102, 102, 255));
        titulo_numExterior.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        titulo_numExterior.setForeground(new java.awt.Color(255, 255, 255));
        titulo_numExterior.setText("N° Exterior");
        titulo_numExterior.setBorder(null);
        titulo_numExterior.setFocusable(false);
        panel_datos.add(titulo_numExterior, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 520, 280, 40));

        num_exterior.setEditable(false);
        num_exterior.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        num_exterior.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        num_exterior.setText("jTextField1");
        num_exterior.setBorder(null);
        num_exterior.setFocusable(false);
        panel_datos.add(num_exterior, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 560, 280, 40));

        num_Interior.setEditable(false);
        num_Interior.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        num_Interior.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        num_Interior.setText("jTextField1");
        num_Interior.setBorder(null);
        num_Interior.setFocusable(false);
        panel_datos.add(num_Interior, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 560, 250, 40));

        btn_eliminar.setBackground(new java.awt.Color(255, 0, 0));
        btn_eliminar.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btn_eliminar.setForeground(new java.awt.Color(255, 255, 255));
        btn_eliminar.setText("Eliminar");
        btn_eliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_eliminarActionPerformed(evt);
            }
        });
        panel_datos.add(btn_eliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 810, 160, 50));

        btn_editar.setBackground(new java.awt.Color(51, 51, 255));
        btn_editar.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btn_editar.setForeground(new java.awt.Color(255, 255, 255));
        btn_editar.setText("Modificar");
        btn_editar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_editarActionPerformed(evt);
            }
        });
        panel_datos.add(btn_editar, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 810, 160, 50));

        lb_datosAlumno3.setFont(new java.awt.Font("Roboto", 1, 24)); // NOI18N
        lb_datosAlumno3.setText("Domicilio");
        panel_datos.add(lb_datosAlumno3, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 380, 350, 40));

        titulo_calle1.setEditable(false);
        titulo_calle1.setBackground(new java.awt.Color(102, 102, 255));
        titulo_calle1.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        titulo_calle1.setForeground(new java.awt.Color(255, 255, 255));
        titulo_calle1.setText("Codigo postal");
        titulo_calle1.setBorder(null);
        titulo_calle1.setFocusable(false);
        panel_datos.add(titulo_calle1, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 420, 230, 40));

        codigo_postal.setEditable(false);
        codigo_postal.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        codigo_postal.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        codigo_postal.setText("jTextField1");
        codigo_postal.setBorder(null);
        codigo_postal.setFocusable(false);
        panel_datos.add(codigo_postal, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 460, 230, 40));

        titulo_fechaNacimiento.setEditable(false);
        titulo_fechaNacimiento.setBackground(new java.awt.Color(102, 102, 255));
        titulo_fechaNacimiento.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        titulo_fechaNacimiento.setForeground(new java.awt.Color(255, 255, 255));
        titulo_fechaNacimiento.setText("Fecha de nacimiento");
        titulo_fechaNacimiento.setBorder(null);
        titulo_fechaNacimiento.setFocusable(false);
        panel_datos.add(titulo_fechaNacimiento, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 290, 230, 40));

        fecha_nacimiento.setEditable(false);
        fecha_nacimiento.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        fecha_nacimiento.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        fecha_nacimiento.setText("jTextField1");
        fecha_nacimiento.setBorder(null);
        fecha_nacimiento.setFocusable(false);
        panel_datos.add(fecha_nacimiento, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 330, 230, 40));

        ruta_ine.setText("jLabel1");
        ruta_ine.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ruta_ine.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                ruta_ineMousePressed(evt);
            }
        });
        panel_datos.add(ruta_ine, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 690, 210, 30));

        ruta_acta.setText("jLabel1");
        ruta_acta.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ruta_acta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                ruta_actaMousePressed(evt);
            }
        });
        panel_datos.add(ruta_acta, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 690, 210, 30));

        ruta_curp.setText("jLabel1");
        ruta_curp.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ruta_curp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                ruta_curpMousePressed(evt);
            }
        });
        panel_datos.add(ruta_curp, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 690, 210, 30));

        ruta_certificado.setText("jLabel1");
        ruta_certificado.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ruta_certificado.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                ruta_certificadoMousePressed(evt);
            }
        });
        panel_datos.add(ruta_certificado, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 690, 210, 30));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("INE");
        panel_datos.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 660, 200, 30));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setText("Acta de nacimiento");
        panel_datos.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 660, 180, 30));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setText("CURP");
        panel_datos.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 660, 180, 30));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setText("Certificado de preparatoria");
        panel_datos.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 660, 200, 30));

        jPanel2.add(panel_datos, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 240, 1190, 910));

        btn_descargarPDF.setText("jLabel5");
        btn_descargarPDF.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_descargarPDF.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_descargarPDFMousePressed(evt);
            }
        });
        jPanel2.add(btn_descargarPDF, new org.netbeans.lib.awtextra.AbsoluteConstraints(1030, 50, 100, 100));

        btn_regresar.setText("Regresar");
        btn_regresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_regresarActionPerformed(evt);
            }
        });
        jPanel2.add(btn_regresar, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 100, 30));

        panel_contenido.setViewportView(jPanel2);

        fondo.add(panel_contenido);
        panel_contenido.setBounds(0, 93, 1200, 550);

        getContentPane().add(fondo, "card2");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void icon_buscarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_icon_buscarMouseClicked
        if (SwingUtilities.isLeftMouseButton(evt)) {
            if (numControl_busqueda.getText().isEmpty()) {//Si no hay texto en el campo de busqueda
                JOptionPane.showMessageDialog(null, "Por favor ingrese un numero de control para consultar", "Numero de control vacio", JOptionPane.WARNING_MESSAGE);
                return;                    
            }
            try {
                buscar_alumno(numControl_busqueda.getText());
            } catch (SQLException ex) {
                Logger.getLogger(SeccionAlumnos.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_icon_buscarMouseClicked

    private void btn_eliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_eliminarActionPerformed
        Object[] opciones = {"Aceptar", "Cancelar"};
        // Si existe información que no ha sido guardada
        // Mostrar diálogo que pregunta si desea confirmar la salida
        int opcionSeleccionada = JOptionPane.showOptionDialog(
                null,
                "Se perderán los datos, ¿Desea eliminar al alumno?",
                "Eliminacion de alumno",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null,
                opciones,
                opciones[1]); // Por defecto, la opción seleccionada es "Cancelar"

        // Manejar las opciones seleccionadas
        if (opcionSeleccionada == JOptionPane.YES_OPTION) {
            eliminar_alumno(numControl.getText());
        } else {
            return;
        }
    }//GEN-LAST:event_btn_eliminarActionPerformed

    private void btn_editarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_editarActionPerformed
        ModificarAlumno ventana = new ModificarAlumno(this.rfc_coordinador);
        try {
            ventana.buscar_alumno(numControl.getText());
        } catch (SQLException ex) {
            Logger.getLogger(SeccionAlumnos.class.getName()).log(Level.SEVERE, null, ex);
        }
        ventana.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btn_editarActionPerformed

    private void btn_descargarPDFMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_descargarPDFMousePressed
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
                    cargarDatoAlumnos();
                } catch (SQLException ex) {
                    Logger.getLogger(SeccionAlumnos.class.getName()).log(Level.SEVERE, null, ex);
                }
                File directorioSeleccionado = fileChooser.getSelectedFile();
                String rutaCarpeta = directorioSeleccionado.getAbsolutePath();
                try {
                    x.PdfTodosLosAlumnos(listAlumnos, rutaCarpeta);
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
    }//GEN-LAST:event_btn_descargarPDFMousePressed

    private void btn_regresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_regresarActionPerformed
        try {
            MenuCoordinador ventana = new MenuCoordinador(this.rfc_coordinador);
            ventana.setVisible(true);
            this.dispose();
        } catch (SQLException ex) {
            Logger.getLogger(ModificarAlumno.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btn_regresarActionPerformed

    private void ruta_actaMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ruta_actaMousePressed
        if (SwingUtilities.isLeftMouseButton(evt)) {
            abrirDocumento(ruta_acta.getText());
        }
    }//GEN-LAST:event_ruta_actaMousePressed

    private void ruta_curpMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ruta_curpMousePressed
        if (SwingUtilities.isLeftMouseButton(evt)) {
            abrirDocumento(ruta_curp.getText());
        }
    }//GEN-LAST:event_ruta_curpMousePressed

    private void ruta_certificadoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ruta_certificadoMousePressed
        if(SwingUtilities.isLeftMouseButton(evt)){
            abrirDocumento(ruta_certificado.getText());
        }
    }//GEN-LAST:event_ruta_certificadoMousePressed

    private void ruta_ineMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ruta_ineMousePressed
        if (ruta_ine.getText().toString() == "SN") {
            System.out.println("xdd");
            return;
        }
        if (SwingUtilities.isLeftMouseButton(evt)) {
            abrirDocumento(ruta_ine.getText());
        }
    }//GEN-LAST:event_ruta_ineMousePressed

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
            java.util.logging.Logger.getLogger(SeccionAlumnos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SeccionAlumnos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SeccionAlumnos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SeccionAlumnos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                    new SeccionAlumnos(null).setVisible(true);
                } catch (SQLException ex) {
                    Logger.getLogger(SeccionAlumnos.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField apellido_materno;
    private javax.swing.JTextField apellido_paterno;
    private javax.swing.JLabel btn_descargarPDF;
    private javax.swing.JButton btn_editar;
    private javax.swing.JButton btn_eliminar;
    private javax.swing.JButton btn_regresar;
    private javax.swing.JTextField calle;
    private javax.swing.JTextField carrera;
    private javax.swing.JTextField codigo_postal;
    private javax.swing.JTextField colonia;
    private javax.swing.JTextField correo;
    private javax.swing.JTextField estado;
    private javax.swing.JTextField fecha_nacimiento;
    private javax.swing.JPanel fondo;
    private javax.swing.JLabel icon_buscar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lb_datosAlumno;
    private javax.swing.JLabel lb_datosAlumno3;
    private javax.swing.JLabel lb_inicial;
    private javax.swing.JLabel logo_ita;
    private javax.swing.JTextField municipio;
    private javax.swing.JTextField nombres;
    private javax.swing.JTextField numControl;
    private javax.swing.JTextField numControl_busqueda;
    private javax.swing.JTextField num_Interior;
    private javax.swing.JTextField num_exterior;
    private javax.swing.JTextField num_telefono;
    private javax.swing.JScrollPane panel_contenido;
    private javax.swing.JPanel panel_datos;
    private javax.swing.JPanel panel_logo;
    private javax.swing.JLabel ruta_acta;
    private javax.swing.JLabel ruta_certificado;
    private javax.swing.JLabel ruta_curp;
    private javax.swing.JLabel ruta_ine;
    private javax.swing.JTextField semestre;
    private javax.swing.JTextField titulo_apellidoMaterno;
    private javax.swing.JTextField titulo_apellidoPaterno;
    private javax.swing.JTextField titulo_calle;
    private javax.swing.JTextField titulo_calle1;
    private javax.swing.JTextField titulo_carrera;
    private javax.swing.JTextField titulo_colonia;
    private javax.swing.JTextField titulo_correo;
    private javax.swing.JTextField titulo_estado;
    private javax.swing.JTextField titulo_fechaNacimiento;
    private javax.swing.JTextField titulo_municipio;
    private javax.swing.JTextField titulo_nombres;
    private javax.swing.JTextField titulo_numControl;
    private javax.swing.JTextField titulo_numExterior;
    private javax.swing.JTextField titulo_numInterior;
    private javax.swing.JTextField titulo_semestre;
    private javax.swing.JTextField titulo_telefono;
    // End of variables declaration//GEN-END:variables
}
