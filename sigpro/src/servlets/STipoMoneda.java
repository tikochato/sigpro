package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import dao.TipoMonedaDAO;
import pojo.TipoMoneda;

@WebServlet("/STipoMoneda")
public class STipoMoneda extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	class stprograma {
		int id;
		String nombre;
		String descripcion;
		int programatipoid;
		String programatipo;
		String fechaCreacion;
		String usuarioCreo;
		String fechaactualizacion;
		String usuarioactualizo;
	};
	
    public STipoMoneda() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String response_text = "";
		try{
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

			if (accion.equals("getTipoMnedaPagina")) {
				int pagina = map.get("pagina")!=null  ? Integer.parseInt(map.get("pagina")) : 0;
				int numeroTipoMoneda = map.get("numerotipomoneda")!=null  ? Integer.parseInt(map.get("numerotipomoneda")) : 0;
				List<TipoMoneda> tipoMonedas = TipoMonedaDAO.getAutorizacionTiposPagina(pagina, numeroTipoMoneda);
				
				List<stprograma> sttipomoneda=new ArrayList<stprograma>();
				for(TipoMoneda tipoMoneda:tipoMonedas){
					stprograma temp =new stprograma();
					temp.id = tipoMoneda.getId();
					temp.nombre = tipoMoneda.getNombre();
					sttipomoneda.add(temp);
				}
				
				response_text=new GsonBuilder().serializeNulls().create().toJson(sttipomoneda);
		        response_text = String.join("", "\"tipoMonedas\":",response_text);
		        response_text = String.join("", "{\"success\":true,", response_text,"}");
			} else if (accion.equals("numeroTipoMonedas")){
				response_text = String.join("","{ \"success\": true, \"totalactividadtipos\":",TipoMonedaDAO.getTotalAuotirzacionTipo().toString()," }");
			}
		}catch (Exception e) {
			response_text = String.join("","{ \"success\": false }");
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
