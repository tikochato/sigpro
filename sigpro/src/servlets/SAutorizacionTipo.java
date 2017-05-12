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

import dao.AutorizacionTipoDAO;
import pojo.AutorizacionTipo;



@WebServlet("/SAutorizacionTipo")
public class SAutorizacionTipo extends HttpServlet {
	private static final long serialVersionUID = 1L;
	class stautorizacionTipo{
		int id;
		String nombre;
		String descripcion;
	}
       
   
    public SAutorizacionTipo() {
        super();
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		response.getWriter().append("{ \"success\": false }").append(request.getContextPath());
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
		String accion = map.get("accion");
		String response_text="";
		if(accion.equals("numeroAutorizacionTipo")){	
			response_text = String.join("","{ \"success\": true, \"totalactividades\":",AutorizacionTipoDAO.getTotalAuotirzacionTipo().toString()," }");
		}else if(accion.equals("getAutorizacionTipoPagin")){
			int pagina = map.get("pagina")!=null  ? Integer.parseInt(map.get("pagina")) : 0;
			int numeroAutorizacionTipo = map.get("numeroautorizaciontipo")!=null  ? Integer.parseInt(map.get("numeroautorizaciontipo")) : 0;
			List<AutorizacionTipo> autorizacionTipos = AutorizacionTipoDAO.getAutorizacionTiposPagina(pagina, numeroAutorizacionTipo);
			
			List<stautorizacionTipo> stautorizaciontipos=new ArrayList<stautorizacionTipo>();
			for(AutorizacionTipo autorizacionTipo:autorizacionTipos){
				stautorizacionTipo temp =new stautorizacionTipo();
				temp.id = autorizacionTipo.getId();
				temp.nombre = autorizacionTipo.getNombre();
				temp.descripcion = autorizacionTipo.getDescripcion();
				stautorizaciontipos.add(temp);
			}
			
			response_text=new GsonBuilder().serializeNulls().create().toJson(stautorizaciontipos);
	        response_text = String.join("", "\"autorizacionTipos\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		
		}
		else{
			response_text = "{ \"success\": false }";
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
