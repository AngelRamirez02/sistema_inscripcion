package direccion;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;

public class ObtenerDireccion {
    
    public String municipio;
    public String estado ;
    public String[] colonias;
   
    public ObtenerDireccion(String codigoPostal) throws Exception {
        // Crear la solicitud HTTP
        //6bae8f3fe1mshaa056e464b2f3bap16a4cfjsnb4fe8f7318df
        //2ad7f5a914msh30a88cad4748696p1d8d13jsnd4801e5f2951
        //3426dd1a6bmsh32dc93140c4cc96p1e4c7ajsn2c35723e3e55
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://mexico-zip-codes.p.rapidapi.com/codigo_postal/" + codigoPostal))
                .header("x-rapidapi-key", "3426dd1a6bmsh32dc93140c4cc96p1e4c7ajsn2c35723e3e55")
                //3426dd1a6bmsh32dc93140c4cc96p1e4c7ajsn2c35723e3e55
                .header("x-rapidapi-host", "mexico-zip-codes.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        // Enviar la solicitud y obtener la respuesta
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        // Crear un StringBuilder para almacenar la respuesta
        StringBuilder respuesta = new StringBuilder();
        respuesta.append(response.body());
        System.out.println(respuesta);
        this.municipio = obtenerMunicipio(respuesta.toString());
        this.estado = obtenerEstado(respuesta.toString());
        this.colonias = obtenerColonias(respuesta.toString());
    }
    
        // Función para obtener el municipio
    public static String obtenerMunicipio(String jsonString) {
        JSONObject jsonObject = new JSONObject(jsonString);
        return jsonObject.getString("municipio");
    }

    // Función para obtener el estado
    public static String obtenerEstado(String jsonString) {
        JSONObject jsonObject = new JSONObject(jsonString);
        return jsonObject.getString("estado");
    }

    // Función para obtener las colonias
    public static String[] obtenerColonias(String jsonString) {
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray coloniasArray = jsonObject.getJSONArray("colonias");
        String[] colonias = new String[coloniasArray.length()];
        for (int i = 0; i < coloniasArray.length(); i++) {
            colonias[i] = coloniasArray.getString(i);
        }
        return colonias;
    }
}
