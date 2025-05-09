/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package alumno;

import com.toedter.calendar.JDateChooser;
import conexion.Conexion;
import direccion.ObtenerDireccion;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import validacion.Validacion;

/**
 *
 * @author ar275
 */
public class ModificarDocumento extends javax.swing.JFrame {

    /**
     * Creates new form InscripcionAlumno
     */
    
    Validacion valida = new Validacion();
    ObtenerDireccion direc;//objeto para obtener la direccion ¿
    Conexion cx = new Conexion();
    Alumno alum = new Alumno();
    
    private String numeroControl;
    
    public ModificarDocumento(String numeroControl) throws SQLException{
        initComponents();
        configuracion_ventana();
        cargar_img();   
        cargarDatosAlumnos(numeroControl);
        this.numeroControl=numeroControl;
    }
    
    //Funcion para toda la configuracion de la ventana 
    private void configuracion_ventana() {

        //Centrar ventana
        this.setLocationRelativeTo(null);//La ventana aparece en el centro
        //Funcion para toda la configuracion de la ventana 
    
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
        
        //Imagenes para botones para subir archivos
        Image icon_subir_archivo = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/img/icon_subir_archivo.png"));
        btn_subir_acta.setIcon(new ImageIcon(icon_subir_archivo.getScaledInstance(btn_subir_acta.getWidth(), btn_subir_acta.getHeight(), Image.SCALE_SMOOTH)));
        btn_subir_certificado.setIcon(new ImageIcon(icon_subir_archivo.getScaledInstance(btn_subir_certificado.getWidth(), btn_subir_certificado.getHeight(), Image.SCALE_SMOOTH)));
        btn_subir_curp.setIcon(new ImageIcon(icon_subir_archivo.getScaledInstance(btn_subir_curp.getWidth(), btn_subir_ine.getHeight(), Image.SCALE_SMOOTH)));
        btn_subir_ine.setIcon(new ImageIcon(icon_subir_archivo.getScaledInstance(btn_subir_ine.getWidth(), btn_subir_ine.getHeight(), Image.SCALE_SMOOTH)));
    }
    
    private void cargarDatosAlumnos(String num_control) throws SQLException{
        String sql = "SELECT * FROM alumno WHERE num_control = ?";
        PreparedStatement ps = cx.conectar().prepareStatement(sql);
        ps.setString(1, num_control);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            //datos del alumno
            entrada_nombres.setText(rs.getString("nombres"));
            entrada_apellidoPaterno.setText(rs.getString("apellido_paterno"));
            entrada_apellidoMaterno.setText(rs.getString("apellido_materno"));
            entrada_carrera.setSelectedItem(rs.getString("carrera"));
            entrada_telefono.setText(rs.getString("telefono"));
            entrada_correo.setText(rs.getString("correo"));
            entrada_fechaNacimiento.setDate(rs.getDate("fecha_nacimiento"));
            entrada_cp.setText(rs.getString("codigo_postal"));
            entrada_estado.addItem(rs.getString("estado"));
            entrada_municipio.addItem(rs.getString("municipio"));
            entrada_colonia.addItem(rs.getString("colonia"));
            entrada_calle.setText(rs.getString("calle"));
            entrada_numInterior.setText(rs.getString("num_interior"));
            entrada_numExterior.setText(rs.getString("num_exterior"));
            
            cargarDocumentosAlumno(num_control);

        }
    }
    
    private void cargarDocumentosAlumno(String num_control) throws SQLException{
        String sql = "SELECT * FROM documentos WHERE num_control = ?";
        
        PreparedStatement pstm = cx.conectar().prepareCall(sql);
        pstm.setString(1, num_control);
        
        ResultSet rs = pstm.executeQuery();
        
        if(rs.next()){
            ruta_acta.setText(rs.getString("acta_nacimiento"));
            ruta_certificado.setText(rs.getString("certificado_bachillerato"));
            ruta_curp.setText(rs.getString("curp"));
            ruta_ine.setText(rs.getString("ine"));
        }
        
        //Si ya tienes sus archivos cargados se deshabilitan los botones
        if(!ruta_acta.getText().isEmpty()){
            btn_subir_acta.setVisible(false);
        }
        if(!ruta_certificado.getText().isEmpty()){
            btn_subir_certificado.setVisible(false);
        }
        if(!ruta_curp.getText().isEmpty()){
            btn_subir_curp.setVisible(false);
        }
        if(!ruta_ine.getText().isEmpty()){
            System.out.println(ruta_ine.getText());
            btn_subir_ine.setVisible(false);
        }
        
    }

    private String seleccionar_pdf() {
        // Mostrar interfaz para seleccionar el archivo
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setPreferredSize(new Dimension(800, 600)); // Tamaño de la ventana
        fileChooser.setDialogTitle("Seleccionar archivo");

        // Crear un filtro para archivos PDF
        FileNameExtensionFilter filtroPDF = new FileNameExtensionFilter("Archivos PDF (*.pdf)", "pdf");
        fileChooser.setFileFilter(filtroPDF);

        // Abrir el cuadro de diálogo
        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            // Validar extensión PDF
            if (!selectedFile.getName().toLowerCase().endsWith(".pdf")) {
                JOptionPane.showMessageDialog(null,
                        "El archivo seleccionado no es un PDF válido.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return "";
            }

            // Validar tamaño máximo (500 KB)
            long fileSizeInKB = selectedFile.length() / 1024; // Convertir bytes a KB
            final long MAX_SIZE_KB = 500;

            if (fileSizeInKB > MAX_SIZE_KB) {
                JOptionPane.showMessageDialog(null,
                        String.format("El archivo excede el tamaño máximo permitido (%.2f MB). Tamaño actual: %.2f MB",
                                MAX_SIZE_KB / 1024.0,
                                fileSizeInKB / 1024.0),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return "";
            }

            return selectedFile.getAbsolutePath();
        }
        return "";
    }

    private String crear_carpetaAlumno(String nombreCarpeta) {
        // Ruta base donde se crearán las nuevas carpetas
        String basePath = "documentos_alumnos";
        String folderPath = basePath + File.separator + nombreCarpeta;

        // Crear la carpeta
        File folder = new File(folderPath);
        if (!folder.exists()) {
            if (folder.mkdir()) {
                return folderPath;
            }
        }
        
        return "";
    }
    
    private String guardar_pdf(String rutaOrigen, String rutaDestino, String nombreArchivo) {
        // Ruuta desde donde se copia el archivo
        String sourceFilePath = rutaOrigen;
        
        // Ruta del archivo de destino dentro de la nueva carpeta
        String destinationFilePath = rutaDestino + File.separator + nombreArchivo + ".pdf";
        
        // Copiar el archivo
        Path sourcePath = Paths.get(sourceFilePath);
        Path destinationPath = Paths.get(destinationFilePath);

        try {
            Files.copy(sourcePath, destinationPath);
            //System.out.println("Archivo copiado a: " + destinationFilePath);
            //JOptionPane.showMessageDialog(null, "Ejecutable generado con exito", "Archivo generado", JOptionPane.INFORMATION_MESSAGE);
            return destinationFilePath;
        } catch (IOException e) {
            System.out.println("Error al copiar el archivo: " + e.getMessage());
        }
        
        return "";
    }

    private void actualizarDocumentos() throws SQLException {

        String rutaCarpetaAlumno = "documentos_alumnos/" + this.numeroControl + "/";

//        String acta = guardar_pdf(ruta_acta.getText(), rutaCarpetaAlumno, "acta_nacimiento");
//        String certificado = guardar_pdf(ruta_certificado.getText(), rutaCarpetaAlumno, "certificado_bachillerato");
//        String curp = guardar_pdf(ruta_curp.getText(), rutaCarpetaAlumno, "curp");
        String ine = guardar_pdf(ruta_ine.getText(), rutaCarpetaAlumno, "ine");

        // Preparar la consulta SQL
        String sql = "UPDATE `documentos` SET `ine` = ? WHERE num_control = ?";
        //Preparar consulta
        PreparedStatement psmt = cx.conectar().prepareStatement(sql);
        psmt.setString(1, ine);
        psmt.setString(2, this.numeroControl);
        
        int filas_actualizadas = psmt.executeUpdate();
        
        if(filas_actualizadas>0){
            JOptionPane.showMessageDialog(null,"Datos actualizados exitosamente\n"
                    + "Ya puede seleccionar materias" ,"Registro existoso", JOptionPane.INFORMATION_MESSAGE);
            MenuAlumno ventana = new MenuAlumno(numeroControl, LocalDate.now(), LocalTime.now());
            ventana.setVisible(true);
            this.dispose();
        }else{
            JOptionPane.showMessageDialog(null,"Los datos no se actualizaron correctamente" ,"Error en el registrp", JOptionPane.INFORMATION_MESSAGE);
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
        jLabel1 = new javax.swing.JLabel();
        ruta_acta = new javax.swing.JTextField();
        btn_subir_acta = new javax.swing.JLabel();
        ruta_certificado = new javax.swing.JTextField();
        btn_subir_certificado = new javax.swing.JLabel();
        ruta_curp = new javax.swing.JTextField();
        btn_subir_curp = new javax.swing.JLabel();
        ruta_ine = new javax.swing.JTextField();
        btn_subir_ine = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lb_curp = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lb_curp1 = new javax.swing.JLabel();
        lb_curp2 = new javax.swing.JLabel();
        entrada_nombres = new javax.swing.JTextField();
        entrada_apellidoPaterno = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        entrada_carrera = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        btn_registrar = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        entrada_apellidoMaterno = new javax.swing.JTextField();
        entrada_telefono = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        entrada_correo = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        entrada_cp = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        entrada_numInterior = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        entrada_calle = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        entrada_numExterior = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        entrada_genero = new javax.swing.JComboBox<>();
        entrada_estado = new javax.swing.JComboBox<>();
        entrada_municipio = new javax.swing.JComboBox<>();
        entrada_colonia = new javax.swing.JComboBox<>();
        entrada_fechaNacimiento = new com.toedter.calendar.JDateChooser();
        jLabel20 = new javax.swing.JLabel();
        btn_regresar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setMinimumSize(new java.awt.Dimension(1100, 750));
        getContentPane().setLayout(new java.awt.CardLayout());

        fondo.setBackground(new java.awt.Color(255, 255, 255));
        fondo.setLayout(null);

        panel_logo.setBackground(new java.awt.Color(255, 255, 255));
        panel_logo.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        logo_ita.setText("LOGO ITA");
        panel_logo.add(logo_ita, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 1020, 110));

        fondo.add(panel_logo);
        panel_logo.setBounds(0, 5, 1050, 110);

        panel_contenido.setBorder(null);
        panel_contenido.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        panel_contenido.setPreferredSize(new java.awt.Dimension(1022, 700));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setPreferredSize(new java.awt.Dimension(1020, 1300));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("DOMICILIO");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 950, 370, 30));

        ruta_acta.setEditable(false);
        ruta_acta.setBackground(new java.awt.Color(204, 204, 204));
        ruta_acta.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        ruta_acta.setBorder(null);
        ruta_acta.setFocusable(false);
        jPanel2.add(ruta_acta, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 110, 340, 40));

        btn_subir_acta.setText("jLabel2");
        btn_subir_acta.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_subir_acta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_subir_actaMousePressed(evt);
            }
        });
        jPanel2.add(btn_subir_acta, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 90, 80, 80));

        ruta_certificado.setEditable(false);
        ruta_certificado.setBackground(new java.awt.Color(204, 204, 204));
        ruta_certificado.setBorder(null);
        ruta_certificado.setFocusable(false);
        jPanel2.add(ruta_certificado, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 220, 340, 40));

        btn_subir_certificado.setText("jLabel2");
        btn_subir_certificado.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_subir_certificado.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_subir_certificadoMousePressed(evt);
            }
        });
        jPanel2.add(btn_subir_certificado, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 200, 80, 80));

        ruta_curp.setEditable(false);
        ruta_curp.setBackground(new java.awt.Color(204, 204, 204));
        ruta_curp.setBorder(null);
        ruta_curp.setFocusable(false);
        jPanel2.add(ruta_curp, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 330, 340, 40));

        btn_subir_curp.setText("jLabel2");
        btn_subir_curp.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_subir_curp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_subir_curpMousePressed(evt);
            }
        });
        jPanel2.add(btn_subir_curp, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 310, 80, 80));

        ruta_ine.setEditable(false);
        ruta_ine.setBackground(new java.awt.Color(204, 204, 204));
        ruta_ine.setBorder(null);
        ruta_ine.setFocusable(false);
        jPanel2.add(ruta_ine, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 440, 340, 40));

        btn_subir_ine.setText("jLabel2");
        btn_subir_ine.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_subir_ine.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_subir_ineMousePressed(evt);
            }
        });
        jPanel2.add(btn_subir_ine, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 420, 80, 80));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("SUBIR ARCHIVOS");
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 50, 360, 20));

        lb_curp.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lb_curp.setText("INE");
        jPanel2.add(lb_curp, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 410, -1, -1));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel4.setText("Numero telefonico");
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 700, -1, -1));

        lb_curp1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lb_curp1.setText("Certificado de bachillerato");
        jPanel2.add(lb_curp1, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 190, -1, -1));

        lb_curp2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lb_curp2.setText("Curp");
        jPanel2.add(lb_curp2, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 300, -1, -1));

        entrada_nombres.setEditable(false);
        entrada_nombres.setEnabled(false);
        entrada_nombres.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                entrada_nombresFocusLost(evt);
            }
        });
        entrada_nombres.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                entrada_nombresKeyTyped(evt);
            }
        });
        jPanel2.add(entrada_nombres, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 110, 370, 50));

        entrada_apellidoPaterno.setEditable(false);
        entrada_apellidoPaterno.setEnabled(false);
        entrada_apellidoPaterno.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                entrada_apellidoPaternoFocusLost(evt);
            }
        });
        entrada_apellidoPaterno.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                entrada_apellidoPaternoKeyTyped(evt);
            }
        });
        jPanel2.add(entrada_apellidoPaterno, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 220, 370, 50));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel5.setText("Acta de nacimiento");
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 80, -1, -1));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel6.setText("Nombre(s)");
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 80, -1, -1));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel7.setText("Apellido paterno");
        jPanel2.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 190, -1, -1));

        entrada_carrera.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        entrada_carrera.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Ingenieria en sistemas computacionales" }));
        entrada_carrera.setEnabled(false);
        jPanel2.add(entrada_carrera, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 620, 370, 50));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel8.setText("Fecha de nacimiento");
        jPanel2.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 390, -1, -1));

        btn_registrar.setBackground(new java.awt.Color(0, 0, 255));
        btn_registrar.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btn_registrar.setForeground(new java.awt.Color(255, 255, 255));
        btn_registrar.setText("Cargar archivo");
        btn_registrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_registrarActionPerformed(evt);
            }
        });
        jPanel2.add(btn_registrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 560, 180, 40));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel9.setText("Genero");
        jPanel2.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 480, -1, -1));

        entrada_apellidoMaterno.setEditable(false);
        entrada_apellidoMaterno.setEnabled(false);
        entrada_apellidoMaterno.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                entrada_apellidoMaternoFocusLost(evt);
            }
        });
        entrada_apellidoMaterno.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                entrada_apellidoMaternoKeyTyped(evt);
            }
        });
        jPanel2.add(entrada_apellidoMaterno, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 320, 370, 50));

        entrada_telefono.setEnabled(false);
        entrada_telefono.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                entrada_telefonoKeyTyped(evt);
            }
        });
        jPanel2.add(entrada_telefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 730, 370, 50));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel10.setText("Correo electronico");
        jPanel2.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 810, -1, -1));

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("DATOS PERSONALES");
        jPanel2.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 40, 370, 30));

        entrada_correo.setEnabled(false);
        entrada_correo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                entrada_correoKeyTyped(evt);
            }
        });
        jPanel2.add(entrada_correo, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 850, 370, 50));

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel12.setText("Código postal");
        jPanel2.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 980, -1, 40));

        entrada_cp.setEnabled(false);
        entrada_cp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                entrada_cpKeyTyped(evt);
            }
        });
        jPanel2.add(entrada_cp, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 1020, 200, 40));

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel13.setText("Estado");
        jPanel2.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 980, -1, 40));

        entrada_numInterior.setEnabled(false);
        jPanel2.add(entrada_numInterior, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 1130, 150, 40));

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel15.setText("Colonia");
        jPanel2.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 1090, -1, 40));

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel16.setText("N° Interior");
        jPanel2.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 1090, -1, 40));

        entrada_calle.setEnabled(false);
        jPanel2.add(entrada_calle, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 1130, 270, 40));

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel17.setText("Municipio");
        jPanel2.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 980, -1, 40));

        entrada_numExterior.setEnabled(false);
        jPanel2.add(entrada_numExterior, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 1130, 150, 40));

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel18.setText("Calle");
        jPanel2.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 1090, -1, 40));

        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel19.setText("N° Exterior");
        jPanel2.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 1090, -1, 40));

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel14.setText("Carrrera");
        jPanel2.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 590, -1, -1));

        entrada_genero.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        entrada_genero.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Masculino", "Femenino", "Otro" }));
        entrada_genero.setEnabled(false);
        jPanel2.add(entrada_genero, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 510, 370, 50));

        entrada_estado.setEnabled(false);
        jPanel2.add(entrada_estado, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 1020, 230, 40));

        entrada_municipio.setEnabled(false);
        jPanel2.add(entrada_municipio, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 1020, 210, 40));

        entrada_colonia.setEnabled(false);
        jPanel2.add(entrada_colonia, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 1130, 240, 50));

        entrada_fechaNacimiento.setEnabled(false);
        entrada_fechaNacimiento.setMaxSelectableDate(new java.util.Date(1167634886000L));
        entrada_fechaNacimiento.setMinSelectableDate(new java.util.Date(1072940486000L));
        jPanel2.add(entrada_fechaNacimiento, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 420, 370, 50));

        jLabel20.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel20.setText("Apellido materno");
        jPanel2.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 290, -1, -1));

        btn_regresar.setText("Regresar");
        btn_regresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_regresarActionPerformed(evt);
            }
        });
        jPanel2.add(btn_regresar, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 100, 30));

        panel_contenido.setViewportView(jPanel2);

        fondo.add(panel_contenido);
        panel_contenido.setBounds(-6, 120, 1060, 580);

        getContentPane().add(fondo, "card2");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_subir_actaMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_subir_actaMousePressed
        if(SwingUtilities.isLeftMouseButton(evt)){
            String archivo_acta = seleccionar_pdf();
            if(!archivo_acta.isEmpty()){
                ruta_acta.setText(archivo_acta);
            }
        }
    }//GEN-LAST:event_btn_subir_actaMousePressed

    private void btn_subir_certificadoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_subir_certificadoMousePressed
         if(SwingUtilities.isLeftMouseButton(evt)){
            String archivo_certificado = seleccionar_pdf();
            if(!archivo_certificado.isEmpty()){
                ruta_certificado.setText(archivo_certificado);
            }
        }
    }//GEN-LAST:event_btn_subir_certificadoMousePressed

    private void btn_subir_curpMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_subir_curpMousePressed
         if(SwingUtilities.isLeftMouseButton(evt)){
            String archivo_curp = seleccionar_pdf();
            if(!archivo_curp.isEmpty()){
                ruta_curp.setText(archivo_curp);
            }
        }
    }//GEN-LAST:event_btn_subir_curpMousePressed

    private void btn_subir_ineMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_subir_ineMousePressed
         if(SwingUtilities.isLeftMouseButton(evt)){
            String archivo_ine = seleccionar_pdf();
            if(!archivo_ine.isEmpty()){
                ruta_ine.setText(archivo_ine);
            }
        }
    }//GEN-LAST:event_btn_subir_ineMousePressed

    private void btn_registrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_registrarActionPerformed
        try {
            actualizarDocumentos();
        } catch (SQLException ex) {
            Logger.getLogger(ModificarDocumento.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btn_registrarActionPerformed

    //------BOTON PARA REGRESAR AL LOGIN
    private void btn_regresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_regresarActionPerformed
        Login_alumno ventana = new Login_alumno();
        ventana.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btn_regresarActionPerformed

    //------------------VALIDA LA LONGITUD DE ENTRADA DE LOS CAMPOS-------------------
    private void entrada_nombresKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_entrada_nombresKeyTyped
        if (entrada_nombres.getText().length() >= 59) {//si la longitud es mayor a 59 no permite seguir escribiendo
            JOptionPane.showMessageDialog(null, "El numero maximo de caracteres es de 60", "Número maximo de cáracteres alcanzados", JOptionPane.WARNING_MESSAGE);
            evt.consume();
        }
    }//GEN-LAST:event_entrada_nombresKeyTyped

    private void entrada_apellidoPaternoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_entrada_apellidoPaternoKeyTyped
        if (entrada_apellidoPaterno.getText().length() >= 59) {//si la longitud es mayor a 59 no permite seguir escribiendo
            JOptionPane.showMessageDialog(null, "El numero maximo de caracteres es de 60", "Número maximo de cáracteres alcanzados", JOptionPane.WARNING_MESSAGE);
            evt.consume();
        }
    }//GEN-LAST:event_entrada_apellidoPaternoKeyTyped

    private void entrada_apellidoMaternoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_entrada_apellidoMaternoKeyTyped
        if (entrada_apellidoMaterno.getText().length() >= 59) {//si la longitud es mayor a 59 no permite seguir escribiendo
            JOptionPane.showMessageDialog(null, "El numero maximo de caracteres es de 60", "Número maximo de cáracteres alcanzados", JOptionPane.WARNING_MESSAGE);
            evt.consume();
        }
    }//GEN-LAST:event_entrada_apellidoMaternoKeyTyped

    private void entrada_telefonoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_entrada_telefonoKeyTyped
        if (entrada_telefono.getText().length() >= 14) {//si la longitud es mayor a 59 no permite seguir escribiendo
            JOptionPane.showMessageDialog(null, "El numero maximo de caracteres es de 15", "Número maximo de cáracteres alcanzados", JOptionPane.WARNING_MESSAGE);
            evt.consume();
        }
    }//GEN-LAST:event_entrada_telefonoKeyTyped

    private void entrada_correoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_entrada_correoKeyTyped
       if (entrada_correo.getText().length() >= 69) {//si la longitud es mayor a 69 no permite seguir escribiendo
            JOptionPane.showMessageDialog(null, "El numero maximo de caracteres es de 70", "Número maximo de cáracteres alcanzados", JOptionPane.WARNING_MESSAGE);
            evt.consume();
        }
    }//GEN-LAST:event_entrada_correoKeyTyped

    private void entrada_cpKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_entrada_cpKeyTyped
        if (entrada_cp.getText().length() >= 8) {//si la longitud es mayor a 8 no permite seguir escribiendo
            JOptionPane.showMessageDialog(null, "El numero maximo de caracteres es de 8", "Número maximo de cáracteres alcanzados", JOptionPane.WARNING_MESSAGE);
            evt.consume();
        }
    }//GEN-LAST:event_entrada_cpKeyTyped

    private void entrada_nombresFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_entrada_nombresFocusLost
        entrada_nombres.setText(valida.formatearNombresApellidos(entrada_nombres.getText()));
    }//GEN-LAST:event_entrada_nombresFocusLost

    private void entrada_apellidoPaternoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_entrada_apellidoPaternoFocusLost
       entrada_apellidoPaterno.setText(valida.formatearNombresApellidos(entrada_apellidoPaterno.getText()));
    }//GEN-LAST:event_entrada_apellidoPaternoFocusLost

    private void entrada_apellidoMaternoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_entrada_apellidoMaternoFocusLost
        entrada_apellidoMaterno.setText(valida.formatearNombresApellidos(entrada_apellidoMaterno.getText()));
    }//GEN-LAST:event_entrada_apellidoMaternoFocusLost

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
            java.util.logging.Logger.getLogger(ModificarDocumento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ModificarDocumento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ModificarDocumento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ModificarDocumento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new ModificarDocumento("25320001").setVisible(true);
                } catch (SQLException ex) {
                    Logger.getLogger(ModificarDocumento.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_registrar;
    private javax.swing.JButton btn_regresar;
    private javax.swing.JLabel btn_subir_acta;
    private javax.swing.JLabel btn_subir_certificado;
    private javax.swing.JLabel btn_subir_curp;
    private javax.swing.JLabel btn_subir_ine;
    private javax.swing.JTextField entrada_apellidoMaterno;
    private javax.swing.JTextField entrada_apellidoPaterno;
    private javax.swing.JTextField entrada_calle;
    private javax.swing.JComboBox<String> entrada_carrera;
    private javax.swing.JComboBox<String> entrada_colonia;
    private javax.swing.JTextField entrada_correo;
    private javax.swing.JTextField entrada_cp;
    private javax.swing.JComboBox<String> entrada_estado;
    private com.toedter.calendar.JDateChooser entrada_fechaNacimiento;
    private javax.swing.JComboBox<String> entrada_genero;
    private javax.swing.JComboBox<String> entrada_municipio;
    private javax.swing.JTextField entrada_nombres;
    private javax.swing.JTextField entrada_numExterior;
    private javax.swing.JTextField entrada_numInterior;
    private javax.swing.JTextField entrada_telefono;
    private javax.swing.JPanel fondo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lb_curp;
    private javax.swing.JLabel lb_curp1;
    private javax.swing.JLabel lb_curp2;
    private javax.swing.JLabel logo_ita;
    private javax.swing.JScrollPane panel_contenido;
    private javax.swing.JPanel panel_logo;
    private javax.swing.JTextField ruta_acta;
    private javax.swing.JTextField ruta_certificado;
    private javax.swing.JTextField ruta_curp;
    private javax.swing.JTextField ruta_ine;
    // End of variables declaration//GEN-END:variables
}
