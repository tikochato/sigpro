package utils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class DecodificadorJson {
	public static String decodificarObjeto(String cadena, String parametro)
    {
		 JsonElement jelement = new JsonParser().parse(cadena);
		 JsonObject  jobject = jelement.getAsJsonObject();
		 String result = jobject.get(parametro).toString(); 
		 return result;
    }
}
