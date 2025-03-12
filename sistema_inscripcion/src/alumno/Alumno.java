/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package alumno;

import conexion.Conexion;
import java.util.Random;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Year;

/**
 *
 * @author ar275
 */
public class Alumno {

    public String generarNumeroControl(String codigoCarrera) {
        // Obtener el año actual completo
        int añoActual = Year.now().getValue();

        // O como string formateado
        String ultimosDosDigitosYear = String.format("%02d", añoActual % 100);
        // Generar y verificar hasta encontrar un número único
        String numeroControl;
        boolean esUnico = false;

        do {
            // Generar 4 dígitos aleatorios
            String ultimosDigitos = generarUltimosDigitos();

            // Construir el número de control completo
            numeroControl = ultimosDosDigitosYear + codigoCarrera + ultimosDigitos;

            // Verificar si ya existe en la base de datos
            esUnico = !existeNumeroControl(numeroControl);

        } while (!esUnico);

        return numeroControl;
    }
    
    private String generarUltimosDigitos() {
        Random random = new Random();
        // Generar un número entre 0 y 9999
        int numero = random.nextInt(10000);
        // Formatear para que siempre tenga 4 dígitos (con ceros a la izquierda si es necesario)
        return String.format("%04d", numero);
    }
    
    private boolean existeNumeroControl(String numeroControl) {
        Conexion conexion = new Conexion();//Variable para la conexion a la base de datos
        try {
            String query = "SELECT COUNT(*) FROM alumno WHERE num_control = ?";
            PreparedStatement statement = conexion.conectar().prepareStatement(query);
            statement.setString(1, numeroControl);
            
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false; // En caso de error, asumimos que no existe
    }
    public static void main(String[] args) {
        Alumno a = new Alumno();
        System.out.println(a.generarNumeroControl("32"));
    }
}
