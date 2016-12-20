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

import org.joda.time.DateTime;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import dao.MetaUnidadMedidaDAO;
import pojo.MetaUnidadMedida;

/**
 * Servlet implementation class SMetaUnidadMedida
 */
@WebServlet("/SMetaUnidadMedida")
public class SMetaUnidadMedida extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SMetaUnidadMedida() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
		if(accion.equals("getMetaUnidadMedidasPagina")){
			int pagina = map.get("pagina")!=null  ? Integer.parseInt(map.get("pagina")) : 0;
			int numenumeroMetaUnidadMedidas = map.get("numerometaunidadmedidas")!=null  ? Integer.parseInt(map.get("numerometaunidadmedidas")) : 0;
			List<MetaUnidadMedida> MetaUnidadMedidas = MetaUnidadMedidaDAO.getMetaUnidadMedidasPagina(pagina, numenumeroMetaUnidadMedidas);
			
			
			response_text=new GsonBuilder().serializeNulls().create().toJson(MetaUnidadMedidas);
	        response_text = String.join("", "\"MetaUnidadMedidas\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(accion.equals("getMetaUnidadMedidas")){
			List<MetaUnidadMedida> MetaUnidadMedidas = MetaUnidadMedidaDAO.getMetaUnidadMedidas();
			response_text=new GsonBuilder().serializeNulls().create().toJson(MetaUnidadMedidas);
	        response_text = String.join("", "\"MetaUnidadMedidas\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(accion.equals("guardarMetaUnidadMedida")){
			boolean result = false;
			boolean esnuevo = map.get("esnueva")!=null ? map.get("esnueva").equals("true") :  false;
			int id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			if(id>0 || esnuevo){
				String nombre = map.get("nombre");
				String descripcion = map.get("descripcion");
				MetaUnidadMedida MetaUnidadMedida;
				if(esnuevo){
					MetaUnidadMedida = new MetaUnidadMedida(nombre, descripcion,"admin",null, new DateTime().toDate(), null, 1, null );
				}
				else{
					MetaUnidadMedida = MetaUnidadMedidaDAO.getMetaUnidadMedidaPorId(id);
					MetaUnidadMedida.setNombre(nombre);
					MetaUnidadMedida.setDescripcion(descripcion);
					MetaUnidadMedida.setUsuarioActualizo("admin");
					MetaUnidadMedida.setFechaActualizacion(new DateTime().toDate());
				}
				result = MetaUnidadMedidaDAO.guardarMetaUnidadMedida(MetaUnidadMedida);
				response_text = String.join("","{ \"success\": ",(result ? "true" : "false")," }");
			}
			else
				response_text = "{ \"success\": false }";
		}
		else if(accion.equals("borrarMetaUnidadMedida")){
			int id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			if(id>0){
				MetaUnidadMedida MetaUnidadMedida = MetaUnidadMedidaDAO.getMetaUnidadMedidaPorId(id);
				MetaUnidadMedida.setUsuarioActualizo("admin");
				MetaUnidadMedida.setFechaActualizacion(new DateTime().toDate());
				response_text = String.join("","{ \"success\": ",(MetaUnidadMedidaDAO.eliminarMetaUnidadMedida(MetaUnidadMedida) ? "true" : "false")," }");
			}
			else
				response_text = "{ \"success\": false }";
		}
		else if(accion.equals("numeroMetaUnidadMedidas")){
			response_text = String.join("","{ \"success\": true, \"totalMetaUnidadMedidas\":",MetaUnidadMedidaDAO.getTotalMetaUnidadMedidas().toString()," }");
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
