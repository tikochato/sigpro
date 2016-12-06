package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



import com.google.gson.Gson;

import com.google.gson.reflect.TypeToken;

@WebServlet("/SFormaEjemplo")
public class SFormaEjemplo extends HttpServlet {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	class data {
		String id;
		String type;
		String label;
		String value;

	}

	public SFormaEjemplo() {
		super();

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");

		Gson gson = new Gson();
		Type type = new TypeToken<Map<String, String>>() {
		}.getType();
		StringBuilder sb = new StringBuilder();
		BufferedReader br = request.getReader();
		String str;
		while ((str = br.readLine()) != null) {
			sb.append(str);
		}
		;
		Map<String, String> map = gson.fromJson(sb.toString(), type);
		String action = map.get("action");

		String response_string = "";

		if (action.equals("load")) {

			response_string = "[{ " + "\"id\": \"1\", " + "\"type\": \"int\", " + "\"label\": \"Canidad\", "
					+ "\"value\": \"\"," + "\"placeholder\": \"cantidad\" " + "}, { " + "\"id\": \"2\", "
					+ "\"type\": \"string\", " + "\"label\": \"Descripcion\", " + "\"value\": \"\", "
					+ "\"placeholder\": \"texto\" " + "}, { " + "\"id\": \"3\", " + "\"type\": \"date\", "
					+ "\"label\": \"Fecha Inicio\", " + "\"value\": \"\", " + "\"isOpen\": false, "
					+ "\"dateOptions\": { " + "\"formatYear\": \"yyyy\", " + "\"startingDay\": 1 " + "} " + "}, { "
					+ "\"id\": \"4\", " + "\"type\": \"date\", " + "\"label\": \"Fecha Final\", " + "\"value\": \"\", "
					+ "\"isOpen\": false, " + "\"dateOptions\": { " + "\"formatYear\": \"yyyy\", "
					+ "\"startingDay\": 1 " + "} " + "}, { " + "\"id\": \"5\", " + "\"type\": \"boolean\", "
					+ "\"label\": \"Es nacional\" " + "}, { " + "\"id\": \"6\", " + "\"type\": \"decimal\", "
					+ "\"label\": \"Monto\", " + "\"value\": \"\", " + "\"placeholder\": \"cantidad\" " + "}, { "
					+ "\"id\": \"7\", " + "\"type\": \"string\", " + "\"label\": \"Encargado\", " + "\"value\": \"\", "
					+ "\"placeholder\": \"texto\" " + "}, { " + "\"id\": \"8\", " + "\"type\": \"select\", "
					+ "\"label\": \"Sexo\", " + "\"value\": \"\", " + "\"options\": [{ " + "\"value\": \"1\", "
					+ "\"label\": \"Masculino\" " + "}, { " + "\"value\": \"2\", " + "\"label\": \"Femenino\" "
					+ "}, { " + "\"value\": \"3\", " + "\"label\": \"Indeterminado\" " + " }]" + "}]";

		}
		

		response.setHeader("Content-Encoding", "gzip");
		response.setCharacterEncoding("UTF-8");

		OutputStream output = response.getOutputStream();
		GZIPOutputStream gz = new GZIPOutputStream(output);
		gz.write(response_string.getBytes("UTF-8"));
		gz.close();
		output.close();

	}

}
