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

import dao.ComponenteTipoDAO;
import pojo.ComponenteTipo;
import utilities.Utils;

/**
 * Servlet implementation class SComponenteTipo
 */
@WebServlet("/SComponenteTipo")
public class SComponenteTipo extends HttpServlet {
	private static final long serialVersionUID = 1L;
	class stcomponentetipo{
		Integer id;
		String nombre;
		String descripcion;
		String usuarioCreo;
		String usuarioActualizo;
		String fechaCreacion;
		String fechaActualizacion;
		int estado;
	}
	
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SComponenteTipo() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
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
		if(accion.equals("getComponentetiposPagina")){
			int pagina = map.get("pagina")!=null  ? Integer.parseInt(map.get("pagina")) : 0;
			int numeroCooperantesTipo = map.get("numerocomponentestipo")!=null  ? Integer.parseInt(map.get("numerocomponentestipo")) : 0;
			List<ComponenteTipo> componentetipos = ComponenteTipoDAO.getComponenteTiposPagina(pagina, numeroCooperantesTipo);
			List<stcomponentetipo> stcomponentetipos=new ArrayList<stcomponentetipo>();
			for(ComponenteTipo componentetipo:componentetipos){
				stcomponentetipo temp =new stcomponentetipo();
				temp.descripcion = componentetipo.getDescripcion();
				temp.estado = componentetipo.getEstado();
				temp.fechaActualizacion = Utils.formatDate(componentetipo.getFechaActualizacion());
				temp.fechaCreacion = Utils.formatDate(componentetipo.getFechaCreacion());
				temp.id = componentetipo.getId();
				temp.nombre = componentetipo.getNombre();
				temp.usuarioActualizo = componentetipo.getUsuarioActualizo();
				temp.usuarioCreo = componentetipo.getUsuarioCreo();
				stcomponentetipos.add(temp);
			}
			
			response_text=new GsonBuilder().serializeNulls().create().toJson(stcomponentetipos);
	        response_text = String.join("", "\"componentetipos\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		
		else if(accion.equals("numeroComponenteTipos")){
			response_text = String.join("","{ \"success\": true, \"totalcooperantes\":",ComponenteTipoDAO.getTotalComponenteTipo().toString()," }");
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
