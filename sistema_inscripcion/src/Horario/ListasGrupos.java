/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Horario;

import alumno.Alumno;
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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ListasGrupos {

    public ListasGrupos(String no_control, String apellido_paterno, String apellido_materno,String nombres) {
        this.no_control = no_control;
        this.apellido_paterno = apellido_paterno;
        this.apellido_materno = apellido_materno;
        this.nombres = nombres;
    }
    private String no_control;
    private String nombres;
    private String apellido_paterno;
    private String apellido_materno;


    public void generarListaGrupo(List<ListasGrupos> lista, String grupo,String nombre_docente, String materia,String salon, String hora_inicio, 
         String hora_salida,String ruta) throws DocumentException, IOException{
        
        BaseColor colorCabeceraTabla = new BaseColor(0, 0, 255); // Usando RGB
        com.itextpdf.text.Font fuenteArialBlanca = FontFactory.getFont("Arial", 12, Font.NORMAL, BaseColor.WHITE);
            
        //Ruta para guardar el documento
        String rutaArchivo = ruta+File.separator+"Lista Grupo "+grupo+".pdf";
        //Crar documento
        Document documento = new Document(PageSize.A4);
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
        
        PdfPTable encabezado = new PdfPTable(1);
        encabezado.setWidthPercentage(100);
        PdfPCell celdaLogo = new PdfPCell(logo);
        celdaLogo.setBorder(PdfPCell.NO_BORDER);
        celdaLogo.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        encabezado.addCell(celdaLogo);
        documento.add(encabezado);
              
        //Parrafo Titulo
        Paragraph titulo = new Paragraph("Lista de alumnos del grupo: " + grupo + "\n\n", FontFactory.getFont("arial", 18, Font.BOLD, BaseColor.BLACK));
        titulo.setAlignment(Paragraph.ALIGN_CENTER);
        //Añadir parrafo al doc
        documento.add(titulo);

        //Titulo para la materia
        Paragraph materia_titulo = new Paragraph("Materia: " + materia + "\n",
                FontFactory.getFont("arial", 14, Font.NORMAL, BaseColor.BLACK));
        materia_titulo.setAlignment(Paragraph.ALIGN_LEFT);
        documento.add(materia_titulo);
        //Profesor
                Paragraph titulo_profesor = new Paragraph("Docente: " + nombre_docente + "\n",
                FontFactory.getFont("arial", 14, Font.NORMAL, BaseColor.BLACK));
        materia_titulo.setAlignment(Paragraph.ALIGN_LEFT);
        documento.add(titulo_profesor);      
        //SALON
        Paragraph titulo_salon = new Paragraph("Aula: " + salon + "\n",
                FontFactory.getFont("arial", 14, Font.NORMAL, BaseColor.BLACK));
        titulo_salon.setAlignment(Paragraph.ALIGN_LEFT);
        documento.add(titulo_salon);
        //  Hora entrada
        Paragraph horario = new Paragraph("Horario: " + hora_inicio+ " - "+hora_salida+"\n\n",
                FontFactory.getFont("arial", 14, Font.NORMAL, BaseColor.BLACK));
        horario.setAlignment(Paragraph.ALIGN_LEFT);
        documento.add(horario);

        //salto de linea al doc
        //Crear tabla para alumnos de preescolar y titulos de las columnas
        float[] relacionColumnas= {1f,2f,3f,3f,3f};
        
        //Tabla para alumnos
        PdfPTable tablaBitacora = new PdfPTable(5);
        tablaBitacora.setWidthPercentage(100);
        tablaBitacora.setWidths(relacionColumnas);
        
        //celdas con estilo
        //
        PdfPCell celda_noLista = new PdfPCell(new Phrase("No lista",fuenteArialBlanca));
        celda_noLista.setBackgroundColor(colorCabeceraTabla);
        //celda para la CURP
        PdfPCell celda_noControl = new PdfPCell(new Phrase("No control",fuenteArialBlanca));
        celda_noControl.setBackgroundColor(colorCabeceraTabla);
        celda_noControl.setFixedHeight(20f);//Altura de las cabeceras
        //
        PdfPCell celda_apellidoPaterno = new PdfPCell(new Phrase("Apellido paterno",fuenteArialBlanca));
        celda_apellidoPaterno.setBackgroundColor(colorCabeceraTabla);
        celda_apellidoPaterno.setFixedHeight(20f);//Altura de las cabeceras
        //
        PdfPCell celda_apellidoMaterno = new PdfPCell(new Phrase("Apellido Materno",fuenteArialBlanca));
        celda_apellidoMaterno.setBackgroundColor(colorCabeceraTabla);
        //
        PdfPCell celda_nombres = new PdfPCell(new Phrase("Nombre(s):",fuenteArialBlanca));
        celda_nombres.setBackgroundColor(colorCabeceraTabla);


        tablaBitacora.addCell(celda_noLista);
        tablaBitacora.addCell(celda_noControl);
        tablaBitacora.addCell(celda_apellidoPaterno);
        tablaBitacora.addCell(celda_apellidoPaterno);
        tablaBitacora.addCell(celda_nombres);
        
        //Lista para la tabla de bitcora
        for (int i = 0; i <lista.size(); i++) {
            tablaBitacora.addCell(""+i+1);
            tablaBitacora.addCell(lista.get(i).no_control);
            tablaBitacora.addCell(lista.get(i).apellido_paterno);
            tablaBitacora.addCell(lista.get(i).apellido_materno);
            tablaBitacora.addCell(lista.get(i).nombres);
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
