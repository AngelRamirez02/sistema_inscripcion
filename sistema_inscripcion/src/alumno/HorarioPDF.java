/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package alumno;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import conexion.Conexion;
import coordinador.AlumnosPDF;
import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.time.LocalTime;

/**
 *
 * @author ar275
 */
public class HorarioPDF {

    public void PdfTodosLosAlumnos(String numControl, String ruta) throws FileNotFoundException, DocumentException, BadElementException, IOException, SQLException {
        BaseColor colorCabeceraTabla = new BaseColor(0, 0, 255); // Color Azul
        com.itextpdf.text.Font fuenteArialBlanca = FontFactory.getFont("Arial", 12, Font.NORMAL, BaseColor.WHITE);

        // Ruta para guardar el documento
        String rutaArchivo = ruta + File.separator + "Horario_Alumno.pdf";

        // Crear documento
        Document documento = new Document(PageSize.A4.rotate());
        FileOutputStream ficheroPdf = new FileOutputStream(rutaArchivo);
        PdfWriter.getInstance(documento, ficheroPdf);

        // Abrir documento
        documento.open();
        InputStream is = getClass().getResourceAsStream("/img/logo_ITA.png");
        Image logo = Image.getInstance(is.readAllBytes());

        PdfPTable encabezado = new PdfPTable(1);
        encabezado.setWidthPercentage(100);
        PdfPCell celdaLogo = new PdfPCell(logo);
        celdaLogo.setBorder(PdfPCell.NO_BORDER);
        celdaLogo.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        encabezado.addCell(celdaLogo);
        documento.add(encabezado);

        // Título
        Paragraph titulo = new Paragraph("Horario del alumno " + numControl + "\n\n",
                FontFactory.getFont("arial", 18, Font.BOLD, BaseColor.BLACK));
        titulo.setAlignment(Paragraph.ALIGN_CENTER);
        documento.add(titulo);

        // Consulta SQL para obtener el horario del alumno
        String consulta = """
        SELECT h.hora_inicio, h.hora_fin, hd.dia, m.nombre_materia, 
        CONCAT(ma.nombres, ' ', ma.apellido_paterno, ' ', ma.apellido_materno) AS docente
        FROM horario h
        JOIN horario_dias hd ON h.id_horario = hd.id_horario
        JOIN materia m ON h.id_materia = m.id_materia
        JOIN docente ma ON h.rfc = ma.rfc
        JOIN alumno_grupo ag ON h.id_grupo = ag.id_grupo
        WHERE ag.num_control = ?
        ORDER BY h.hora_inicio, FIELD(hd.dia, 'Lunes', 'Martes', 'Miercoles', 'Jueves', 'Viernes');
    """;

        Conexion cx = new Conexion();
        PreparedStatement ps = cx.conectar().prepareStatement(consulta);
        ps.setString(1, numControl);
        ResultSet rs = ps.executeQuery();

        // Definir la estructura del horario
        String[] dias = {"Lunes", "Martes", "Miercoles", "Jueves", "Viernes"};
        String[][] horario = new String[12][6]; // 12 horas x 6 columnas (Hora + 5 días)
        String[] docentes = new String[12]; // Para almacenar el docente por hora

        // Inicializar la tabla con valores vacíos
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 6; j++) {
                horario[i][j] = " ";
            }
            docentes[i] = " ";
        }

        // Llenar tabla con datos de la consulta
        while (rs.next()) {
            String dia = rs.getString("dia");
            LocalTime horaInicio = rs.getTime("hora_inicio").toLocalTime();
            String materia = rs.getString("nombre_materia");
            String docente = rs.getString("docente");

            int indiceDia = java.util.Arrays.asList(dias).indexOf(dia);
            int indiceHora = horaInicio.getHour() - 7; // Ajuste para horarios desde las 7 AM

            if (indiceHora >= 0 && indiceHora < 12 && indiceDia >= 0) {
                horario[indiceHora][indiceDia + 1] = materia; // Se coloca en la columna del día
                docentes[indiceHora] = docente; // Se asigna el docente
            }
        }

        // Crear tabla PDF
        PdfPTable tabla = new PdfPTable(7); // 7 columnas (Hora + 5 días + Docente)
        tabla.setWidthPercentage(100);
        tabla.setWidths(new float[]{1f, 1f, 1f, 1f, 1f, 1f, 1f});

        // Encabezados
        String[] encabezados = {"Hora", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Docente"};
        for (String encabeza : encabezados) {
            PdfPCell celda = new PdfPCell(new Phrase(encabeza, fuenteArialBlanca));
            celda.setBackgroundColor(colorCabeceraTabla);
            celda.setFixedHeight(20f);
            tabla.addCell(celda);
        }

        // Llenar la tabla con el horario
        for (int i = 0; i < 12; i++) {
            tabla.addCell((7 + i) + ":00 - " + (8 + i) + ":00"); // Hora en la primera columna
            for (int j = 1; j < 6; j++) {
                tabla.addCell(horario[i][j]); // Materias en los días
            }
            tabla.addCell(docentes[i]); // Docente en la última columna
        }

        // Agregar tabla al documento
        documento.add(tabla);

        // Cerrar documento
        documento.close();

        // Abrir el archivo generado
        File path = new File(rutaArchivo);
        try {
            Desktop.getDesktop().open(path);
        } catch (IOException ex) {
            Logger.getLogger(AlumnosPDF.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
