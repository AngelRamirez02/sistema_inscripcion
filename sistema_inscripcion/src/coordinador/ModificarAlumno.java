/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package coordinador;

import alumno.*;
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
public class ModificarAlumno extends javax.swing.JFrame {

    /**
     * Creates new form InscripcionAlumno
     */
    
    private String rfc_coordinador;
    private String numControl_original;
    private String correo_original;
    
    Validacion valida = new Validacion();
    ObtenerDireccion direc;//objeto para obtener la direccion ¿
    Conexion cx = new Conexion();
    Alumno alum = new Alumno();
    
    public ModificarAlumno(String rfc) {
        this.rfc_coordinador=rfc;
        initComponents();
        configuracion_ventana();
        cargar_img();
        //Centrar ventana
        this.setLocationRelativeTo(null);//La ventana aparece en el centro
        //
        panel_doc.setVisible(false);
    }
    
     //Funcion para toda la configuracion de la ventana 
    private void configuracion_ventana(){
        //Añadir el listener para detectar cuando la ventana es redimensionada
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int panelWidth = panel_contenido.getWidth();
                int panel_heigth = panel_contenido.getHeight() - panel_logo.getHeight();
                int newX = (fondo.getWidth() - panelWidth) / 2; // Calcular la nueva posición en 
                int newY = (fondo.getHeight() - panel_heigth)/2;
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

    private String seleccionar_pdf() {
        //Mostrar interfaz para seleccionar la carpeta
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setPreferredSize(new Dimension(800, 600));//Tamño de la ventana
        fileChooser.setDialogTitle("Seleccionar archivo");

        // Crear un filtro para archivos PDF
        FileNameExtensionFilter filtroPDF = new FileNameExtensionFilter("Archivos PDF (*.pdf)", "pdf");
        fileChooser.setFileFilter(filtroPDF); // Solo permitir seleccionar carpetas

        // Abrir el cuadro de diálogo
        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            // Validar que el archivo termine en ".pdf"
            if (selectedFile.getName().toLowerCase().endsWith(".pdf")) {
                return selectedFile.getAbsolutePath();
            } else {
                JOptionPane.showMessageDialog(null, "El archivo seleccionado no es un PDF válido.", "Error", JOptionPane.ERROR_MESSAGE);
                return "";
            }
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


    private void actualizar_alumno() throws SQLException {
        //Obtener todos los datos de entrada
        Date fecha_nacimiento = entrada_fechaNacimiento.getDate();
        java.sql.Date fecha_sql = new java.sql.Date(fecha_nacimiento.getTime());
        
        

        //if (!rutaCarpetaAlumno.isEmpty()) {
            /*String acta = guardar_pdf(ruta_acta.getText(), rutaCarpetaAlumno, "acta_nacimiento");
            String certificado = guardar_pdf(ruta_certificado.getText(), rutaCarpetaAlumno, "certificado_bachillerato");
            String curp = guardar_pdf(ruta_curp.getText(), rutaCarpetaAlumno, "curp");
            String ine = guardar_pdf(ruta_ine.getText(), rutaCarpetaAlumno, "ine");*/

           
            String sql = "UPDATE `alumno` SET `carrera` = ?, `semestre` = ?, `nombres` = ?, `apellido_paterno` = ?, "
           + "`apellido_materno` = ?, `genero` = ?, `fecha_nacimiento` = ?, `password` = ?, `telefono` = ?, "
           + "`correo` = ?, `codigo_postal` = ?, `estado` = ?, `municipio` = ?, `colonia` = ?, "
           + "`calle` = ?, `num_interior` = ?, `num_exterior` = ? "
           + "WHERE `num_control` = ?";

            PreparedStatement pstmt = cx.conectar().prepareStatement(sql);
            // Establecer los valores para cada parámetro
            pstmt.setString(1, entrada_carrera.getSelectedItem().toString());
            pstmt.setString(2, entrada_semestre.getSelectedItem().toString());
            pstmt.setString(3, valida.formatearNombresApellidos(entrada_nombres.getText()));
            pstmt.setString(4, valida.formatearNombresApellidos(entrada_apellidoPaterno.getText()));
            pstmt.setString(5, valida.formatearNombresApellidos(entrada_apellidoMaterno.getText()));
            pstmt.setString(6, entrada_genero.getSelectedItem().toString());
            pstmt.setDate(7, fecha_sql);
            pstmt.setString(8, "999");
            pstmt.setString(9, entrada_telefono.getText());
            pstmt.setString(10, entrada_correo.getText());
            pstmt.setString(11, entrada_cp.getText());
            pstmt.setString(12, entrada_estado.getSelectedItem().toString());
            pstmt.setString(13, entrada_municipio.getSelectedItem().toString());
            pstmt.setString(14, entrada_colonia.getSelectedItem().toString());
            pstmt.setString(15, entrada_calle.getText());
            pstmt.setString(16, entrada_numInterior.getText());
            pstmt.setString(17, entrada_numExterior.getText());
            
            //Num de control a modificar
            pstmt.setString(18, this.numControl_original);
            
             //Verifica que se realizó el registro
            int filas_insertadas = pstmt.executeUpdate();
            if(filas_insertadas >0){
                //alta_documentos(numControl, acta, certificado, curp, ine);
                JOptionPane.showMessageDialog(null,"Datos actualizados exitosamente" ,"Registro existoso", JOptionPane.INFORMATION_MESSAGE);
                SeccionAlumnos ventana = new SeccionAlumnos(this.rfc_coordinador);
                ventana.setVisible(true);
                this.dispose();
                
            }else{
                 JOptionPane.showMessageDialog(null,"Hubo un error al registrar los datos, intente otra vez", "Error en el registro", JOptionPane.WARNING_MESSAGE);
                 return;
            }
       /* }
        else{
             JOptionPane.showMessageDialog(null,"Hubo un error al registrar los datos, intente otra vez", "Error en el registro", JOptionPane.WARNING_MESSAGE);
                 return;
        }*/

    }

    private void alta_documentos(String numControl, String acta, String certificado, String curp, String ine) throws SQLException {
        String sql = "INSERT INTO `documentos` (`id_documentos`, `num_control`, `"
                + "acta_nacimiento`, `certificado_bachillerato`, `curp`, `ine`) "
                + "VALUES (NULL, ?, ?, ?, ?, ?)";

        PreparedStatement pstmt = cx.conectar().prepareStatement(sql);

        pstmt.setString(1, numControl);
        pstmt.setString(2, acta);
        pstmt.setString(3, certificado);
        pstmt.setString(4, curp);
        pstmt.setString(5, ine);

        //Verifica que se realizó el registro
        int filas_insertadas = pstmt.executeUpdate();
        if (filas_insertadas > 0) {
            JOptionPane.showMessageDialog(null, "Documentos cargados con exito", "Registro existoso", JOptionPane.INFORMATION_MESSAGE);

        } else {
            JOptionPane.showMessageDialog(null, "Hubo un error al registrar los docuemntos, intente otra vez", "Error en el registro", JOptionPane.WARNING_MESSAGE);
            return;
        }
    }
    
    public void buscar_alumno(String numControl) throws SQLException {
        String sql = "SELECT * FROM alumno WHERE num_control = ?";
        PreparedStatement ps = cx.conectar().prepareStatement(sql);
        ps.setString(1, numControl);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            this.numControl_original=rs.getString("num_control");
            this.correo_original=rs.getString("correo");
            //datos del alumno
            entrada_numControl.setText(rs.getString("num_control"));
            entrada_nombres.setText(rs.getString("nombres"));
            entrada_apellidoPaterno.setText(rs.getString("apellido_paterno"));
            entrada_apellidoMaterno.setText(rs.getString("apellido_materno"));
            entrada_semestre.setSelectedItem(rs.getString("semestre"));
            entrada_carrera.setSelectedItem(rs.getString("carrera"));
            entrada_telefono.setText(rs.getString("telefono"));
            entrada_correo.setText(rs.getString("correo"));
            entrada_fechaNacimiento.setDate(rs.getDate("fecha_nacimiento"));
            entrada_cp.setText(rs.getString("codigo_postal"));
            obtenerDireccion(entrada_cp.getText());
            entrada_calle.setText(rs.getString("calle"));
            entrada_numInterior.setText(rs.getString("num_interior"));
            entrada_numExterior.setText(rs.getString("num_exterior"));

        } else {
            JOptionPane.showMessageDialog(null, "No se encontó a ningun alumno con el numero de control ingresado", "Registro no encontrado", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    public boolean correoRepetido(){
        if(entrada_correo.getText().equals(this.correo_original)){
            return false;
        }
        try {
            //Prepara la consulta para verificar si existe el correo
            String consulta_correo = "SELECT * FROM alumno WHERE correo = ?";
            PreparedStatement ps = cx.conectar().prepareStatement(consulta_correo);
            ps.setString(1, entrada_correo.getText());
            ResultSet rs = ps.executeQuery();
            if(rs.next()){//si encuentra un fila con el correo quiere decir que ya existe
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ModificarAlumno.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;//Retorna falso si no encuentra el correo
    }
    
    /*
    Ingenieria en sistemas computacionales
    Ingenieria bioquimica
    Ingenieria electromecanica
    Arquitectura
    */
    private String codigoCarrera(String carrera) {
        switch (carrera) {
            case "Arquitectura" ->{
                return "31";
            }
            case "Ingenieria en sistemas computacionales" -> {
                return "32";
            }
            
            case "Ingenieria bioquimica" ->{
                return "33";
            }
            
            case "Ingenieria electromecanica" ->{
                return "34";
            }
        }
        return "";
    }

    public void obtenerDireccion(String colonia) {
        try {
            direc = new ObtenerDireccion(entrada_cp.getText());
            entrada_estado.removeAllItems();
            entrada_municipio.removeAllItems();
            entrada_colonia.removeAllItems();
            //si el estado no esta vacio quiere decir que el codigo pertenece a mexico
            entrada_estado.addItem(direc.estado);
            entrada_municipio.addItem(direc.municipio);
            entrada_colonia.setModel(new DefaultComboBoxModel<>(direc.colonias));
            entrada_colonia.setSelectedItem(colonia);
        } catch (Exception ex) {
            Logger.getLogger(ModificarAlumno.class.getName()).log(Level.SEVERE, null, ex);
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
        jLabel4 = new javax.swing.JLabel();
        entrada_numControl = new javax.swing.JTextField();
        entrada_apellidoPaterno = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        entrada_carrera = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
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
        btn_buscarCodigoPostal = new javax.swing.JButton();
        entrada_estado = new javax.swing.JComboBox<>();
        entrada_municipio = new javax.swing.JComboBox<>();
        entrada_colonia = new javax.swing.JComboBox<>();
        entrada_nombres = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        entrada_semestre = new javax.swing.JComboBox<>();
        entrada_fechaNacimiento = new com.toedter.calendar.JDateChooser();
        jLabel21 = new javax.swing.JLabel();
        btn_registrar = new javax.swing.JButton();
        btn_cancelar = new javax.swing.JButton();
        btn_regresar = new javax.swing.JButton();
        panel_doc = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        ruta_acta = new javax.swing.JTextField();
        lb_curp1 = new javax.swing.JLabel();
        ruta_certificado = new javax.swing.JTextField();
        lb_curp2 = new javax.swing.JLabel();
        ruta_curp = new javax.swing.JTextField();
        lb_curp = new javax.swing.JLabel();
        ruta_ine = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        btn_subir_ine = new javax.swing.JLabel();
        btn_subir_acta = new javax.swing.JLabel();
        btn_subir_certificado = new javax.swing.JLabel();
        btn_subir_curp = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1100, 700));
        getContentPane().setLayout(new java.awt.CardLayout());

        fondo.setBackground(new java.awt.Color(255, 255, 255));
        fondo.setLayout(null);

        panel_logo.setBackground(new java.awt.Color(255, 255, 255));
        panel_logo.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        logo_ita.setText("LOGO ITA");
        panel_logo.add(logo_ita, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 1020, 110));

        fondo.add(panel_logo);
        panel_logo.setBounds(0, 0, 1050, 110);

        panel_contenido.setBorder(null);
        panel_contenido.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setMinimumSize(new java.awt.Dimension(1020, 1200));
        jPanel2.setPreferredSize(new java.awt.Dimension(1020, 1500));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("DOMICILIO");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 1020, 370, 30));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel4.setText("Numero telefonico");
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 770, -1, -1));
        jPanel2.add(entrada_numControl, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 110, 370, 50));
        jPanel2.add(entrada_apellidoPaterno, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 290, 370, 50));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel6.setText("Numero de control");
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 80, -1, -1));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel7.setText("Apellido paterno");
        jPanel2.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 260, -1, -1));

        entrada_carrera.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        entrada_carrera.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Ingenieria en sistemas computacionales" }));
        jPanel2.add(entrada_carrera, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 690, 370, 50));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel8.setText("Apellido materno");
        jPanel2.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 360, -1, -1));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel9.setText("Fecha de Nacimiento");
        jPanel2.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 560, -1, -1));
        jPanel2.add(entrada_apellidoMaterno, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 390, 370, 50));
        jPanel2.add(entrada_telefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 800, 370, 50));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel10.setText("Correo electronico");
        jPanel2.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 880, -1, -1));

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("DATOS PERSONALES");
        jPanel2.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, 370, 30));
        jPanel2.add(entrada_correo, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 920, 370, 50));

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel12.setText("Código postal");
        jPanel2.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 1070, -1, 40));
        jPanel2.add(entrada_cp, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 1110, 180, 40));

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel13.setText("Estado");
        jPanel2.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 1070, -1, 40));
        jPanel2.add(entrada_numInterior, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 1220, 150, 40));

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel15.setText("Colonia");
        jPanel2.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 1180, -1, 40));

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel16.setText("N° Interior");
        jPanel2.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 1180, -1, 40));
        jPanel2.add(entrada_calle, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 1220, 270, 40));

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel17.setText("Municipio");
        jPanel2.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 1070, -1, 40));
        jPanel2.add(entrada_numExterior, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 1220, 150, 40));

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel18.setText("Calle");
        jPanel2.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 1180, -1, 40));

        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel19.setText("N° Exterior");
        jPanel2.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 1180, -1, 40));

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel14.setText("Carrrera");
        jPanel2.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 660, -1, -1));

        entrada_genero.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        entrada_genero.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Masculino", "Femenino", "Otro" }));
        jPanel2.add(entrada_genero, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 490, 370, 50));

        btn_buscarCodigoPostal.setBackground(new java.awt.Color(204, 204, 204));
        btn_buscarCodigoPostal.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn_buscarCodigoPostal.setText("Buscar");
        btn_buscarCodigoPostal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_buscarCodigoPostalActionPerformed(evt);
            }
        });
        jPanel2.add(btn_buscarCodigoPostal, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 1110, 110, 40));

        entrada_estado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "<seleccionar>" }));
        entrada_estado.setEnabled(false);
        jPanel2.add(entrada_estado, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 1110, 230, 40));

        entrada_municipio.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "<seleccionar>" }));
        entrada_municipio.setEnabled(false);
        jPanel2.add(entrada_municipio, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 1110, 210, 40));

        entrada_colonia.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "<seleccionar>" }));
        jPanel2.add(entrada_colonia, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 1220, 240, 50));
        jPanel2.add(entrada_nombres, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 190, 370, 50));

        jLabel20.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel20.setText("Nombre(s)");
        jPanel2.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 160, -1, -1));

        entrada_semestre.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Primero", "Segundo", "Tercero" }));
        entrada_semestre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                entrada_semestreActionPerformed(evt);
            }
        });
        jPanel2.add(entrada_semestre, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 690, 180, 50));
        jPanel2.add(entrada_fechaNacimiento, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 592, 380, 50));

        jLabel21.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel21.setText("Genero");
        jPanel2.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 460, -1, -1));

        btn_registrar.setBackground(new java.awt.Color(0, 0, 255));
        btn_registrar.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btn_registrar.setForeground(new java.awt.Color(255, 255, 255));
        btn_registrar.setText("Actualizar");
        btn_registrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_registrarActionPerformed(evt);
            }
        });
        jPanel2.add(btn_registrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 1370, 180, 40));

        btn_cancelar.setBackground(new java.awt.Color(102, 102, 102));
        btn_cancelar.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btn_cancelar.setForeground(new java.awt.Color(255, 255, 255));
        btn_cancelar.setText("Cancelar");
        btn_cancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cancelarActionPerformed(evt);
            }
        });
        jPanel2.add(btn_cancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 1370, 180, 40));

        btn_regresar.setText("Regresar");
        btn_regresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_regresarActionPerformed(evt);
            }
        });
        jPanel2.add(btn_regresar, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 30, 100, 30));

        panel_doc.setBackground(new java.awt.Color(255, 255, 255));
        panel_doc.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("ACTUALIZAR ARCHIVOS");
        panel_doc.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 10, 360, 40));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel5.setText("Acta de nacimiento");
        panel_doc.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 60, -1, -1));

        ruta_acta.setEditable(false);
        ruta_acta.setBackground(new java.awt.Color(204, 204, 204));
        ruta_acta.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        ruta_acta.setBorder(null);
        ruta_acta.setFocusable(false);
        panel_doc.add(ruta_acta, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 90, 340, 40));

        lb_curp1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lb_curp1.setText("Certificado de bachillerato");
        panel_doc.add(lb_curp1, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 160, -1, -1));

        ruta_certificado.setEditable(false);
        ruta_certificado.setBackground(new java.awt.Color(204, 204, 204));
        ruta_certificado.setBorder(null);
        ruta_certificado.setFocusable(false);
        panel_doc.add(ruta_certificado, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 200, 340, 40));

        lb_curp2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lb_curp2.setText("Curp");
        panel_doc.add(lb_curp2, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 270, -1, -1));

        ruta_curp.setEditable(false);
        ruta_curp.setBackground(new java.awt.Color(204, 204, 204));
        ruta_curp.setBorder(null);
        ruta_curp.setFocusable(false);
        panel_doc.add(ruta_curp, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 300, 340, 40));

        lb_curp.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lb_curp.setText("INE");
        panel_doc.add(lb_curp, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 390, -1, -1));

        ruta_ine.setEditable(false);
        ruta_ine.setBackground(new java.awt.Color(204, 204, 204));
        ruta_ine.setBorder(null);
        ruta_ine.setFocusable(false);
        panel_doc.add(ruta_ine, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 430, 340, 40));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 0, 0));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("* Nota: debe digitalizar cuando menos tres para poder inscribrse");
        panel_doc.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 490, 380, 30));

        btn_subir_ine.setText("jLabel2");
        btn_subir_ine.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_subir_ine.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_subir_ineMousePressed(evt);
            }
        });
        panel_doc.add(btn_subir_ine, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 400, 80, 80));

        btn_subir_acta.setText("jLabel2");
        btn_subir_acta.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_subir_acta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_subir_actaMousePressed(evt);
            }
        });
        panel_doc.add(btn_subir_acta, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 70, 80, 80));

        btn_subir_certificado.setText("jLabel2");
        btn_subir_certificado.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_subir_certificado.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_subir_certificadoMousePressed(evt);
            }
        });
        panel_doc.add(btn_subir_certificado, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 180, 80, 80));

        btn_subir_curp.setText("jLabel2");
        btn_subir_curp.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_subir_curp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_subir_curpMousePressed(evt);
            }
        });
        panel_doc.add(btn_subir_curp, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 290, 80, 80));

        jPanel2.add(panel_doc, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 20, 520, 550));

        panel_contenido.setViewportView(jPanel2);

        fondo.add(panel_contenido);
        panel_contenido.setBounds(0, 120, 1050, 565);

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

    private void btn_buscarCodigoPostalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_buscarCodigoPostalActionPerformed
        //busca las coincidencias con el codigo postal
        if (valida.cpValido(entrada_cp.getText())) {
            //obtener los datos del codigo postal si es valido
            try {
                direc = new ObtenerDireccion(entrada_cp.getText());
                if (!direc.estado.isEmpty()) {
                    entrada_estado.removeAllItems();
                    entrada_municipio.removeAllItems();
                    entrada_colonia.removeAllItems();
                    //si el estado no esta vacio quiere decir que el codigo pertenece a mexico
                    entrada_estado.addItem(direc.estado);
                    entrada_municipio.addItem(direc.municipio);
                    entrada_colonia.setModel(new DefaultComboBoxModel<>(direc.colonias));

                } else {
                    JOptionPane.showMessageDialog(null, "Ingrese un codigo postal de Mexico", "Codigo postal no valido", JOptionPane.WARNING_MESSAGE);
                    entrada_cp.requestFocusInWindow();    // Borde al tener foco;
                    return;
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Hubo un error en la consulta de codigos postales\n"
                        + "Verifique su conexión a internet "
                        + "\nSi el problema persiste contacte al soporte del sistema", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Ingrese un codigo postal valido", "Codigo postal no valido", JOptionPane.WARNING_MESSAGE);
            entrada_cp.requestFocusInWindow();    // Borde al tener foco;
            return;
        }
    }//GEN-LAST:event_btn_buscarCodigoPostalActionPerformed

    private void entrada_semestreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_entrada_semestreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_entrada_semestreActionPerformed

    private void btn_registrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_registrarActionPerformed
        //Validaciones
        if (!valida.nombresValidos(valida.formatearNombresApellidos(entrada_nombres.getText()))) {
            JOptionPane.showMessageDialog(null, "Ingrese un nombre valido", "Nombre no valido", JOptionPane.WARNING_MESSAGE);
            entrada_nombres.requestFocusInWindow();
            return;
        }
        if (!valida.apellidoValido(valida.formatearNombresApellidos(entrada_apellidoPaterno.getText()))) {
            JOptionPane.showMessageDialog(null, "Ingrese un apellido paterno valido", "Apellido no valido", JOptionPane.WARNING_MESSAGE);
            entrada_apellidoPaterno.requestFocusInWindow();
            return;
        }
        if (!valida.apellidoValido(valida.formatearNombresApellidos(entrada_apellidoMaterno.getText()))) {
            JOptionPane.showMessageDialog(null, "Ingrese un apellido materno valido", "Apellido no valido", JOptionPane.WARNING_MESSAGE);
            entrada_apellidoMaterno.requestFocusInWindow();
            return;
        }
        if (!valida.correo_valido(entrada_correo.getText())) {
            JOptionPane.showMessageDialog(null, "Ingrese un correo electronico valido", "Correo no valido", JOptionPane.WARNING_MESSAGE);
            entrada_correo.requestFocusInWindow();
            return;
        }
        //VALIDA FECHA DE NACIMIENTO
        if (entrada_fechaNacimiento.getDate() == null) {
            JOptionPane.showMessageDialog(null, "Ingrese una fecha de nacimiento valida", "Fecha no valido", JOptionPane.WARNING_MESSAGE);
            entrada_fechaNacimiento.requestFocusInWindow();
            return;
        }
        //Valida que la fecha de nacimiento se encuentre en el rango
        Date minDate = ((JDateChooser) entrada_fechaNacimiento).getMinSelectableDate();
        Date maxDate = ((JDateChooser) entrada_fechaNacimiento).getMaxSelectableDate();

        if (entrada_fechaNacimiento.getDate().before(minDate) || entrada_fechaNacimiento.getDate().after(maxDate)) {
            JOptionPane.showMessageDialog(null, "Ingrese una fecha de nacimiento valida", "Fecha no valido", JOptionPane.WARNING_MESSAGE);
            entrada_fechaNacimiento.requestFocusInWindow();
            return;
        }
        
        //CORREO
        if (correoRepetido()) {
            JOptionPane.showMessageDialog(null, "El correo ya se encuentra registrado\nPor favor ingrese otro", "Correo repetido", JOptionPane.WARNING_MESSAGE);
            entrada_correo.requestFocusInWindow();
            return;
        }
        if(!valida.validarNumeroTelefono(entrada_telefono.getText())){
            JOptionPane.showMessageDialog(null, "Ingrese un numero de telefono valido", "Numero de telefono no valido", JOptionPane.WARNING_MESSAGE);
            entrada_telefono.requestFocusInWindow();
            return;
        }
        if (!valida.cpValido(entrada_cp.getText())) {
            JOptionPane.showMessageDialog(null, "Ingrese un codigo postal valido", "Codigo postal no valido", JOptionPane.WARNING_MESSAGE);
            entrada_cp.requestFocusInWindow();    // Borde al tener foco;
            return;
        }
        if (entrada_colonia.getSelectedItem().toString().equals("<seleccionar>")) {//sino selecciona una colonia
            JOptionPane.showMessageDialog(null, "Seleccione una colonia", "Colonia no seleccionada", JOptionPane.WARNING_MESSAGE);
            entrada_colonia.requestFocusInWindow();    // Borde al tener foco;
            return;
        }
        if (!valida.numInteriorExteriorValido(entrada_numExterior.getText())) {
            JOptionPane.showMessageDialog(null, "Ingrese un nímero exterior valido", "Número exterior no valido", JOptionPane.WARNING_MESSAGE);
            entrada_numExterior.requestFocusInWindow();    // Borde al tener foco;
            return;
        }
        if (!valida.numInteriorExteriorValido(entrada_numInterior.getText())) {
            JOptionPane.showMessageDialog(null, "Ingrese un numero exterior valido", "Numero interior no valido", JOptionPane.WARNING_MESSAGE);
            entrada_numInterior.requestFocusInWindow();    // Borde al tener foco;
            return;
        }
        /*if (ruta_acta.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe cargar su acta de nacimiento en formato pdf para continuar", "No ha cargado documento", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (ruta_certificado.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe cargar su certificado de preparatoria en formato pdf para continuar", "No ha cargado documento", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if(ruta_curp.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "Debe cargar su curp en formato pdf para continuar", "No ha cargado documento", JOptionPane.WARNING_MESSAGE);
            return;
        }*/
        try {
            actualizar_alumno();
        } catch (SQLException ex) {
            Logger.getLogger(InscripcionAlumno.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btn_registrarActionPerformed

    private void btn_cancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancelarActionPerformed
        try {
            SeccionAlumnos ventana = new SeccionAlumnos(this.rfc_coordinador);
            ventana.setVisible(true);
            this.dispose();
        } catch (SQLException ex) {
            Logger.getLogger(ModificarAlumno.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btn_cancelarActionPerformed

    private void btn_regresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_regresarActionPerformed
        try {
            SeccionAlumnos ventana = new SeccionAlumnos(this.rfc_coordinador);
            ventana.setVisible(true);
            this.dispose();
        } catch (SQLException ex) {
            Logger.getLogger(ModificarAlumno.class.getName()).log(Level.SEVERE, null, ex);
        }
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
            java.util.logging.Logger.getLogger(ModificarAlumno.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ModificarAlumno.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ModificarAlumno.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ModificarAlumno.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ModificarAlumno(null).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_buscarCodigoPostal;
    private javax.swing.JButton btn_cancelar;
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
    private javax.swing.JTextField entrada_numControl;
    private javax.swing.JTextField entrada_numExterior;
    private javax.swing.JTextField entrada_numInterior;
    private javax.swing.JComboBox<String> entrada_semestre;
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
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
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
    private javax.swing.JPanel panel_doc;
    private javax.swing.JPanel panel_logo;
    private javax.swing.JTextField ruta_acta;
    private javax.swing.JTextField ruta_certificado;
    private javax.swing.JTextField ruta_curp;
    private javax.swing.JTextField ruta_ine;
    // End of variables declaration//GEN-END:variables
}
