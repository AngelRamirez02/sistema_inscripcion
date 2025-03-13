/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package coordinador;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import com.itextpdf.text.Image; // Correcto
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfContentByte;
import java.awt.Desktop;
import java.io.IOException;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author ar275
 */
public class AlumnosPDF {

    public String numControl;
    public String nombres;
    public String apellido_paterno;
    public String apellido_materno;
    public String fecha_nacimiento;
    public String carrera;
    public String Semestre;
    
    public void PdfTodosLosAlumnos(List<AlumnosPDF> listaAlumnos, String ruta) throws FileNotFoundException, DocumentException, BadElementException, IOException{
        //Fecha
        LocalDate fechaActual = LocalDate.now();
        DateTimeFormatter formatoEspanol = DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy", new Locale("es", "ES"));
        String fechaFormateada = fechaActual.format(formatoEspanol);
        
        LocalTime horaActual = LocalTime.now();
         // Formatear la hora en el formato "HH:mm:ss"
        DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("hh:mm:ss a");
        String horaFormateada = horaActual.format(formatoHora);
        
        BaseColor colorCabeceraTabla = new BaseColor(0, 0, 255); // Usando RGB
        com.itextpdf.text.Font fuenteArialBlanca = FontFactory.getFont("Arial", 12, Font.NORMAL, BaseColor.WHITE);
            
        //Ruta para guardar el documento
        String rutaArchivo = ruta+File.separator+"Registro alumnos"+fechaActual.toString()+".pdf";
        //Crar documento
        Document documento = new Document(PageSize.A4.rotate());
        //Nombre del archivo
        FileOutputStream ficheroPdf = new FileOutputStream(rutaArchivo);
        //Instancia del doc
        //PdfWriter.getInstance(documento,ficheroPdf);

        // Crear el escritor de PDF
        PdfWriter writer = PdfWriter.getInstance(documento, ficheroPdf);

        //Abrir documento
        documento.open();
        InputStream is = getClass().getResourceAsStream("/img/logo_ITA.png");
        Image logo = Image.getInstance(is.readAllBytes());
         // Dibujar línea de separación
        PdfContentByte cb = writer.getDirectContent();
        
        PdfPTable encabezado= new PdfPTable(1);
        encabezado.setWidthPercentage(100);
        PdfPCell celdaLogo = new PdfPCell(logo);
            celdaLogo.setBorder(PdfPCell.NO_BORDER);
            celdaLogo.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            encabezado.addCell(celdaLogo);
        documento.add(encabezado);
              
        //Parrafo Titulo
        Paragraph titulo = new Paragraph("Registro de alumnos del Instituto Tecnologico de Acapulco\n\n",FontFactory.getFont("arial",18,Font.BOLD,BaseColor.BLACK));
        titulo.setAlignment(Paragraph.ALIGN_CENTER);
        //Añadir parrafo al doc
        documento.add(titulo);
        
        //salto de linea al doc
        //Crear tabla para alumnos de preescolar y titulos de las columnas
        float[] relacionColumnas= {1f,1f,1f,1f,1f,1f,1f};
        
        //Tabla para alumnos
        PdfPTable tablaAlumnos = new PdfPTable(7);
        tablaAlumnos.setWidthPercentage(100);
        tablaAlumnos.setWidths(relacionColumnas);
        
        //celdas con estilo
        //celda para la CURP
        PdfPCell celda_numControl = new PdfPCell(new Phrase("Numero de control",fuenteArialBlanca));
        celda_numControl.setBackgroundColor(colorCabeceraTabla);
        celda_numControl.setFixedHeight(20f);//Altura de las cabeceras
        //
        PdfPCell celda_apelldo_paterno = new PdfPCell(new Phrase("Apellido paterno",fuenteArialBlanca));
        celda_apelldo_paterno.setBackgroundColor(colorCabeceraTabla);
        celda_apelldo_paterno.setFixedHeight(20f);//Altura de las cabeceras
        //Titulo nombres
        PdfPCell celda_apellido_materno = new PdfPCell(new Phrase("Apellido materno",fuenteArialBlanca));
        celda_apellido_materno.setBackgroundColor(colorCabeceraTabla);
        //Titulo apellidos paterno y materno
        PdfPCell celda_nombres = new PdfPCell(new Phrase("Nombre (s)",fuenteArialBlanca));
        celda_nombres.setBackgroundColor(colorCabeceraTabla);
        //
        PdfPCell celda_fecha_nacimiento = new PdfPCell(new Phrase("Fecha de nacimiento",fuenteArialBlanca));
        celda_fecha_nacimiento.setBackgroundColor(colorCabeceraTabla);
        //Titulo fecha nacimiento
        PdfPCell celda_carrera= new PdfPCell(new Phrase("Carrera",fuenteArialBlanca));
        celda_carrera.setBackgroundColor(colorCabeceraTabla);
        //Titulo nivel escolar
        PdfPCell celda_semestre= new PdfPCell(new Phrase("Semestre",fuenteArialBlanca));
        celda_semestre.setBackgroundColor(colorCabeceraTabla);

        tablaAlumnos.addCell(celda_numControl);
        tablaAlumnos.addCell(celda_apelldo_paterno);
        tablaAlumnos.addCell(celda_apellido_materno);
        tablaAlumnos.addCell(celda_nombres);
        tablaAlumnos.addCell(celda_fecha_nacimiento);
        tablaAlumnos.addCell(celda_carrera);
        tablaAlumnos.addCell(celda_semestre);
        
        //Lista para la tabla secundaria
        for (int i = 0; i < listaAlumnos.size(); i++) {
            tablaAlumnos.addCell(listaAlumnos.get(i).numControl);
            tablaAlumnos.addCell(listaAlumnos.get(i).apellido_paterno);
            tablaAlumnos.addCell(listaAlumnos.get(i).apellido_materno);
            tablaAlumnos.addCell(listaAlumnos.get(i).nombres);
            tablaAlumnos.addCell(listaAlumnos.get(i).fecha_nacimiento);
            tablaAlumnos.addCell(listaAlumnos.get(i).carrera);
            tablaAlumnos.addCell(listaAlumnos.get(i).Semestre);
        }
        
        documento.add(tablaAlumnos);

        //Cerrar doc
        documento.close();       
        //Abre el archivo generado
        File path = new File(rutaArchivo);
        try {
            Desktop.getDesktop().open(path);
        } catch (IOException ex) {
            Logger.getLogger(AlumnosPDF.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

// Método para dibujar una línea de separación
    private void dibujarLineaSeparacion(PdfContentByte cb, Document document) {
        cb.setLineWidth(1f); // Grosor de la línea
        cb.setColorStroke(BaseColor.BLACK); // Color de la línea
        cb.moveTo(document.leftMargin(), document.getPageSize().getHeight() - document.topMargin() - 20);
        cb.lineTo(document.rightMargin(), document.getPageSize().getHeight() - document.topMargin() - 20);
        cb.stroke();
    }

    public AlumnosPDF(String numControl, String apellido_paterno, String apellido_materno, String nombres, String fecha_nacimiento, String carrera, String Semestre) {
        this.numControl = numControl;
        this.nombres = nombres;
        this.apellido_paterno = apellido_paterno;
        this.apellido_materno = apellido_materno;
        this.fecha_nacimiento = fecha_nacimiento;
        this.carrera = carrera;
        this.Semestre = Semestre;
    }
 
    
    public static void main(String[] args) {
        
    }
}
