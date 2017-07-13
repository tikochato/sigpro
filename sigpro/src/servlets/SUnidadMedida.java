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
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import dao.UnidadMedidaDAO;
import pojo.UnidadMedida;

@WebServlet("/SUnidadMedida")
public class SUnidadMedida extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	class stUnidadMedida{
		int id;
		String nombre;
		//String descripcion;
		//String usuarioCreo;
		//String usuarioActualizo;
		//String fechaCreo;
		//String fechaActualizacion;
		//Integer estado;
	}
	
	
    public SUnidadMedida() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		HttpSession sesionweb = request.getSession();
		String usuario = sesionweb.getAttribute("usuario")!= null ? sesionweb.getAttribute("usuario").toString() : null;
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
		String response_text = "";
		
		if(accion.equals("getUnidadMedida")){
			List<UnidadMedida> unidadMedida = UnidadMedidaDAO.getUnidadMedida();
			
			List<stUnidadMedida> lsUnidadMedida = new ArrayList<stUnidadMedida>();
			for(UnidadMedida unidad : unidadMedida){
				stUnidadMedida temp = new stUnidadMedida();
				temp.id = unidad.getId();
				temp.nombre = unidad.getNombre();
				lsUnidadMedida.add(temp);
			}
			
			response_text=new GsonBuilder().serializeNulls().create().toJson(lsUnidadMedida);
	        response_text = String.join("", "\"unidadMedida\":",response_text);
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
