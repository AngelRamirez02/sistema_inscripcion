/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package validacion;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author ar275
 */
public class Validacion {

    public boolean nombresValidos(String nombre) {
        String regex = "^(De|Del|Los|Las|La)?\\s?[A-ZÁÉÍÓÚÑ][a-záéíóúñ]*(\\s[A-ZÁÉÍÓÚÑ][a-záéíóúñ]*)*$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(nombre);
        return matcher.matches();//retorna el resultado de evaluar el correo con la expresion regular
    }

    public boolean apellidoValido(String apelido) {
        String regex = "^(De|Del|Los|Las|La)?\\s?[A-ZÁÉÍÓÚÑ][a-záéíóúñ]*(\\s[A-ZÁÉÍÓÚÑ][a-záéíóúñ]*)*$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(apelido);
        return matcher.matches();//retorna el resultado de evaluar el correo con la expresion regular
    }

    public boolean cpValido(String cp) {
        String regex = "^\\d{5}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(cp);
        return matcher.matches();//retorna el resultado de evaluar el correo con la expresion regular
    }

    public boolean correo_valido(String correo) {//Valida correo electronicos 
        String regex = "^[a-zA-Z0-9_]+([.][a-zA-Z0-9_]+)*@(gmail|yahoo|hotmail|outlook|acapulco)([.][a-zA-Z0-9_]+)?[.](com|net|org|edu|gov|mx|tecnm)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(correo);
        return matcher.matches();//retorna el resultado de evaluar el correo con la expresion regular
    }

    public boolean numInteriorExteriorValido(String num) {
        String regex = "^[A-Za-z0-9\\s\\-\\/]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(num);
        return matcher.matches();//retorna el resultado de evaluar el correo con la expresion regular
    }
    
    
    public String formatearNombresApellidos(String nombre) {
        if (nombre == null || nombre.isEmpty()) {
            return ""; // Maneja el caso de una cadena nula o vacía
        }

        String nombreFormateado = "";
        String[] palabras = nombre.trim().split("\\s+");
        for (String s : palabras) {
            if (s.length() > 0) {
                String aux = s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
                nombreFormateado += aux + " ";
            }
        }
        return nombreFormateado.trim();//elimina el ultimo espacio     
    }
}
