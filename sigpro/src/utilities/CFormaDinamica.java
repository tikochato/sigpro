package utilities;

import java.util.HashMap;
import java.util.List;

public class CFormaDinamica {
	
	public static String convertirEstructura(List<HashMap<String,Object>> campos) {
		String struct = "[";

		int i = 0;
		for (HashMap<String,Object> c : campos) {
			if (i > 0) {
				struct = struct + ",";
			}
			switch ((Integer)c.get("tipo")) {
			case 1: // String
				struct = struct + tipoTexto(c);
				break;
			case 2: // entero
				struct = struct + tipoEentero(c);
				break;
			case 3: // decimal
				struct = struct + tipoDecimal(c);
				break;

			case 4: // booleano
				struct = struct + tipoBooleano(c);
				break;
			case 5: // tiempo
				struct = struct + tipoFecha(c);
				break;
			}
			i++;
		}
		struct = struct + "]";
		return struct;
	}
	
	private static String tipoTexto(HashMap<String,Object> campo) {
		return String.join("", "{\"id\" :\"", campo.get("id").toString(), "\",\"tipo\" : \"texto\", \"label\" : \"", campo.get("nombre").toString(),
				"\",\"valor\" : \"", campo.get("valor")!=null ? campo.get("valor").toString() : "", "\", \"placeholder\" : \"texto\"}");
	}

	private static String tipoEentero(HashMap<String,Object> campo) {
		return String.join("","{\"id\" :\"",campo.get("id").toString() ,"\",\"tipo\" : \"entero\",\"label\" : \"", campo.get("nombre").toString(),
				"\",\"valor\" : \"", campo.get("valor")!=null ? campo.get("valor").toString() : "", "\",\"placeholder\" : \"cantidad\"}");
	}

	private static String tipoDecimal(HashMap<String,Object> campo) {
		return String.join("","{\"id\" :\"", campo.get("id").toString(), "\"," + "\"tipo\" : \"decimal\",\"label\" : \"",campo.get("nombre").toString(),
				"\",\"valor\" : \"", campo.get("valor")!=null ? campo.get("valor").toString() : "", "\",\"placeholder\" : \"cantidad\"}");
	}

	private static String tipoBooleano(HashMap<String,Object> campo) {
		return String.join("","{\"id\" :\"", campo.get("id").toString(), "\",\"tipo\" : \"booleano\",\"label\" : \"", campo.get("nombre").toString(),"\"}");
	}

	private static String tipoFecha(HashMap<String,Object> campo) {
		return String.join("","{\"id\" :\"", campo.get("id").toString(), "\",\"tipo\" : \"fecha\"," + "\"label\" : \"", campo.get("nombre").toString(),
				"\",\"valor\" : \"",campo.get("valor")!=null ? campo.get("valor").toString() : "", "\" }");
	}

}
