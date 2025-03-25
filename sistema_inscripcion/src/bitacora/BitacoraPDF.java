/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bitacora;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import coordinador.AlumnosPDF;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ar275
 */
public class BitacoraPDF {
    private String id_sesion;
    private String usuario;
    private String tipo_usuario;
    private String fecha_entrada;
    private String hora_entrada;
    private String fecha_salida;
    private String hora_salida;

    public BitacoraPDF(String id_sesion, String usuario, String tipo_usuario, String fecha_entrada, String hora_entrada, String fecha_salida, String hora_salida) {
        this.id_sesion = id_sesion;
        this.usuario = usuario;
        this.tipo_usuario = tipo_usuario;
        this.fecha_entrada = fecha_entrada;
        this.hora_entrada = hora_entrada;
        this.fecha_salida = fecha_salida;
        this.hora_salida = hora_salida;
    }
    
    public void generarBitacora(List<BitacoraPDF> bitacora, String ruta) throws DocumentException, IOException{
        //Fecha actual para el nombre
        LocalDate fechaActual = LocalDate.now();
        
        BaseColor colorCabeceraTabla = new BaseColor(0, 0, 255); // Usando RGB
        com.itextpdf.text.Font fuenteArialBlanca = FontFactory.getFont("Arial", 12, Font.NORMAL, BaseColor.WHITE);
            
        //Ruta para guardar el documento
        String rutaArchivo = ruta+File.separator+"Bitacora"+fechaActual.toString()+".pdf";
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
        Paragraph titulo = new Paragraph("Bitacora del sistema de inscripción\n\n",FontFactory.getFont("arial",18,Font.BOLD,BaseColor.BLACK));
        titulo.setAlignment(Paragraph.ALIGN_CENTER);
        //Añadir parrafo al doc
        documento.add(titulo);
        
        //salto de linea al doc
        //Crear tabla para alumnos de preescolar y titulos de las columnas
        float[] relacionColumnas= {1f,1f,1f,1f,1f,1f,1f};
        
        //Tabla para alumnos
        PdfPTable tablaBitacora = new PdfPTable(7);
        tablaBitacora.setWidthPercentage(100);
        tablaBitacora.setWidths(relacionColumnas);
        
        //celdas con estilo
        //celda para la CURP
        PdfPCell celda_idSesion = new PdfPCell(new Phrase("ID sesión",fuenteArialBlanca));
        celda_idSesion.setBackgroundColor(colorCabeceraTabla);
        celda_idSesion.setFixedHeight(20f);//Altura de las cabeceras
        //
        PdfPCell celda_usuario = new PdfPCell(new Phrase("Usuario",fuenteArialBlanca));
        celda_usuario.setBackgroundColor(colorCabeceraTabla);
        celda_usuario.setFixedHeight(20f);//Altura de las cabeceras
        //Titulo nombres
        PdfPCell celda_tipoUsuario = new PdfPCell(new Phrase("Tipo usuario",fuenteArialBlanca));
        celda_tipoUsuario.setBackgroundColor(colorCabeceraTabla);
        //Titulo apellidos paterno y materno
        PdfPCell celda_fechaEntrada = new PdfPCell(new Phrase("Fecha entrada",fuenteArialBlanca));
        celda_fechaEntrada.setBackgroundColor(colorCabeceraTabla);
        //
        PdfPCell celda_horaEntrada = new PdfPCell(new Phrase("Hora entrada",fuenteArialBlanca));
        celda_horaEntrada.setBackgroundColor(colorCabeceraTabla);
        //Titulo fecha nacimiento
        PdfPCell celda_fechaSalida= new PdfPCell(new Phrase("Fecha salida",fuenteArialBlanca));
        celda_fechaSalida.setBackgroundColor(colorCabeceraTabla);
        //Titulo nivel escolar
        PdfPCell celda_horaSalida = new PdfPCell(new Phrase("Hora salida",fuenteArialBlanca));
        celda_horaSalida.setBackgroundColor(colorCabeceraTabla);

        tablaBitacora.addCell(celda_idSesion);
        tablaBitacora.addCell(celda_usuario);
        tablaBitacora.addCell(celda_tipoUsuario);
        tablaBitacora.addCell(celda_fechaEntrada);
        tablaBitacora.addCell(celda_horaSalida);
        tablaBitacora.addCell(celda_fechaSalida);
        tablaBitacora.addCell(celda_horaSalida);
        
        //Lista para la tabla de bitcora
        for (int i = 0; i < bitacora.size(); i++) {
            tablaBitacora.addCell(bitacora.get(i).id_sesion);
            tablaBitacora.addCell(bitacora.get(i).usuario);
            tablaBitacora.addCell(bitacora.get(i).tipo_usuario);
            tablaBitacora.addCell(bitacora.get(i).fecha_entrada);
            tablaBitacora.addCell(bitacora.get(i).hora_entrada);
            tablaBitacora.addCell(bitacora.get(i).fecha_salida);
            tablaBitacora.addCell(bitacora.get(i).hora_salida);
        }
        documento.add(tablaBitacora);

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
}
