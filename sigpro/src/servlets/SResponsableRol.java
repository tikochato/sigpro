package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
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

import dao.ResponsableRolDAO;
import pojo.ResponsableRol;
import pojo.ResponsableTipo;

@WebServlet("/SResponsableRol")
public class SResponsableRol extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	class stResposableRol {
		int id;
		ResponsableTipo responsableTipo;
		String nombre;
		String descripcion;
		String usuarioCreo;
		String usuarioActualizo;
		Date fechaCreacion;
		Date fechaActualizacion;
		int estado;
	};
	
    public SResponsableRol() {
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
			Map<String, String> map = gson.fromJson(sb.toString(), type);
			String accion = map.get("accion")!=null ? map.get("accion") : "";
			
			if (accion.equals("getResponsableRol")){
				int pagina = map.get("pagina")!=null  ? Integer.parseInt(map.get("pagina")) : 0;
				int numeroResponsableRol = map.get("numeroresponsablerol")!=null  ? Integer.parseInt(map.get("numeroresponsablerol")) : 0;
				List<ResponsableRol> responsablesRol = ResponsableRolDAO.getResponsableTiposPagina(pagina, numeroResponsableRol);
				
				List<stResposableRol> stresponsablerol=new ArrayList<stResposableRol>();
				for(ResponsableRol responsableRol:responsablesRol){
					stResposableRol temp =new stResposableRol();
					temp.id = responsableRol.getId();
					temp.nombre = responsableRol.getNombre();
					stresponsablerol.add(temp);
				}
				
				response_text=new GsonBuilder().serializeNulls().create().toJson(stresponsablerol);
		        response_text = String.join("", "\"resposablerol\":",response_text);
		        response_text = String.join("", "{\"success\":true,", response_text,"}");
			}else if (accion.equals("numeroResponsableRol")){
				response_text = String.join("","{ \"success\": true, \"totalresponsablerol\":",ResponsableRolDAO.getTotalResponsableRol().toString()," }");
			}
		}
		catch (Exception ex){
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
