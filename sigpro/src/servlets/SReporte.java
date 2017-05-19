package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import dao.ReporteDAO;
import utilities.Utils;

@WebServlet("/SReporte")
public class SReporte extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public SReporte() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		Gson gson = new Gson();
		Type type = new TypeToken<Map<String, String>>(){}.getType();
		StringBuilder sb = new StringBuilder();
		BufferedReader br = request.getReader();
		String str;
		while ((str = br.readLine()) != null) {
			sb.append(str);
		}
		;
		Map<String, String> map = gson.fromJson(sb.toString(), type);
		String accion = map.get("accion")!=null ? map.get("accion") : "";
		String response_text = "";
		
		if(accion.equals("getCargaTrabajoPrestamo")){
			Integer objetoTipo = Utils.String2Int(map.get("objetoTipo"),0);
			Integer idPrestamo = Utils.String2Int(map.get("idPrestamo"),0);
			Integer idComponente = Utils.String2Int(map.get("idComponente"),0);
			Integer idProducto = Utils.String2Int(map.get("idProducto"),0);
			Integer idSubProducto = Utils.String2Int(map.get("idSubProducto"),0);
			
			List<?> actividades_proceso = ReporteDAO.getCargaTrabajo(0,objetoTipo, idPrestamo, idComponente, idProducto, idSubProducto);
			List<?> actividades_atrasadas = ReporteDAO.getCargaTrabajo(1,objetoTipo, idPrestamo, idComponente, idProducto, idSubProducto);
			
			String JsonProceso = Utils.getJSonString("actividadesProceso", actividades_proceso);
			String JsonAtrasadas = Utils.getJSonString("actividadesAtrasadas", actividades_atrasadas);

			response_text = JsonProceso + "," + JsonAtrasadas;
			response_text = String.join("", "{\"success\":true,", response_text, "}");
		}
		
		response.setHeader("Content-Encoding", "gzip");
		response.setCharacterEncoding("UTF-8");

        OutputStream output = response.getOutputStream();
		GZIPOutputStream gz = new GZIPOutputStream(output);
        gz.write(response_text.getBytes("UTF-8"));
        gz.close();
        output.close();
	}
}
