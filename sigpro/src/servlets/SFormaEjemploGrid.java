package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.GsonBuilder;

@WebServlet("/SFormaEjemploGrid")
public class SFormaEjemploGrid extends HttpServlet {
	private static final long serialVersionUID = 1L;

	class stentidad {
		Integer id;

		String nombre;
		String descripcion;
		String gender;

	}

	public SFormaEjemploGrid() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");

		List<HashMap<String, Object>> data = new ArrayList<>();

		for (int i = 1; i <= 10; i++) {
			HashMap<String, Object> obj = new HashMap<String, Object>();
			obj.put("id", i);
			obj.put("nombre", "nombre_" + i);
			obj.put("descripcion", "descripcion");
			data.add(obj);
		}

		StringBuilder sb = new StringBuilder();
		BufferedReader br = request.getReader();
		String str;
		while ((str = br.readLine()) != null) {
			sb.append(str);
		}
		
		ArrayList<stentidad> stentidades = new ArrayList<stentidad>();

		if (data != null && data.size() > 0) {
			for (HashMap<String, Object> centidad : data) {
				stentidad sttemp = new stentidad();
				sttemp.id = (Integer) centidad.get("id");

				sttemp.nombre = (String) centidad.get("nombre");
				sttemp.descripcion = (String) centidad.get("descripcion");

				stentidades.add(sttemp);
			}

			response.setHeader("Content-Encoding", "gzip");
			response.setCharacterEncoding("UTF-8");
			String response_text = new GsonBuilder().serializeNulls().create().toJson(stentidades);
			response_text = String.join("", "\"entidades\":", response_text);

			response_text = String.join("", "{\"success\":true,", response_text, "}");

			// String response_text="";

			OutputStream output = response.getOutputStream();
			GZIPOutputStream gz = new GZIPOutputStream(output);
			gz.write(response_text.getBytes("UTF-8"));
			gz.close();
			output.close();

		}
	}
}
