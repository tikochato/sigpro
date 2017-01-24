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

import org.joda.time.DateTime;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import dao.RecursoUnidadMedidaDAO;
import pojo.RecursoUnidadMedida;
import utilities.Utils;

/**
 * Servlet implementation class SRecursoUnidadMedida
 */
@WebServlet("/SRecursoUnidadMedida")
public class SRecursoUnidadMedida extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
	
	public class stunidadrecurso{
		Integer id;
		String nombre;
		String descripcion;
		String simbolo;
		Integer estado;
		String fechaCreacion;
		String fechaActualizacion;
		String usuarioCreo;
		String usuarioActulizo;
	}
	
    public SRecursoUnidadMedida() {
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
		if(accion.equals("getRecursoUnidadMedidasPagina")){
			int pagina = map.get("pagina")!=null  ? Integer.parseInt(map.get("pagina")) : 0;
			int numenumeroRecursoUnidadMedidas = map.get("numerorecursounidadmedidas")!=null  ? Integer.parseInt(map.get("numerorecursounidadmedidas")) : 0;
			List<RecursoUnidadMedida> RecursoUnidadMedidas = RecursoUnidadMedidaDAO.getRecursoUnidadMedidasPagina(pagina, numenumeroRecursoUnidadMedidas);
			
			List<stunidadrecurso> stunidad = new ArrayList<stunidadrecurso>();
			for(RecursoUnidadMedida recursounidad:RecursoUnidadMedidas){
				stunidadrecurso temp = new stunidadrecurso();
				temp.descripcion = recursounidad.getDescripcion();
				temp.simbolo = recursounidad.getSimbolo();
				temp.estado = recursounidad.getEstado();
				temp.fechaActualizacion = Utils.formatDate(recursounidad.getFechaActualizacion());
				temp.fechaCreacion = Utils.formatDate(recursounidad.getFechaCreacion());
				temp.id = recursounidad.getId();
				temp.nombre = recursounidad.getNombre();
				temp.usuarioActulizo = recursounidad.getUsuarioActualizo();
				temp.usuarioCreo = recursounidad.getUsuarioCreo();
				stunidad.add(temp);
			}
			response_text=new GsonBuilder().serializeNulls().create().toJson(stunidad);
			response_text = String.join("", "\"RecursoUnidadMedidas\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(accion.equals("getRecursoUnidadMedidas")){
			List<RecursoUnidadMedida> RecursoUnidadMedidas = RecursoUnidadMedidaDAO.getRecursoUnidadMedidas();
			List<stunidadrecurso> stunidad = new ArrayList<stunidadrecurso>();
			for(RecursoUnidadMedida recursounidad:RecursoUnidadMedidas){
				stunidadrecurso temp = new stunidadrecurso();
				temp.descripcion = recursounidad.getDescripcion();
				temp.simbolo = recursounidad.getSimbolo();
				temp.estado = recursounidad.getEstado();
				temp.fechaActualizacion = Utils.formatDate(recursounidad.getFechaActualizacion());
				temp.fechaCreacion = Utils.formatDate(recursounidad.getFechaCreacion());
				temp.id = recursounidad.getId();
				temp.nombre = recursounidad.getNombre();
				temp.usuarioActulizo = recursounidad.getUsuarioActualizo();
				temp.usuarioCreo = recursounidad.getUsuarioCreo();
				stunidad.add(temp);
			}
			response_text=new GsonBuilder().serializeNulls().create().toJson(stunidad);
			response_text = String.join("", "\"RecursoUnidadMedidas\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(accion.equals("guardarRecursoUnidadMedida")){
			boolean result = false;
			boolean esnuevo = map.get("esnueva")!=null ? map.get("esnueva").equals("true") :  false;
			int id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			HttpSession sesionweb = request.getSession();
			String usuario = sesionweb.getAttribute("usuario")!= null ? sesionweb.getAttribute("usuario").toString() : null;
			if(id>0 || esnuevo){
				String nombre = map.get("nombre");
				String descripcion = map.get("descripcion");
				String simbolo = map.get("simbolo");
				RecursoUnidadMedida RecursoUnidadMedida;
				if(esnuevo){
					RecursoUnidadMedida = new RecursoUnidadMedida(nombre, descripcion,simbolo,1,usuario,null, new DateTime().toDate(), null,null);
				}
				else{
					RecursoUnidadMedida = RecursoUnidadMedidaDAO.getRecursoUnidadMedidaPorId(id);
					RecursoUnidadMedida.setNombre(nombre);
					RecursoUnidadMedida.setDescripcion(descripcion);
					RecursoUnidadMedida.setSimbolo(simbolo);
					RecursoUnidadMedida.setUsuarioActualizo(usuario);
					RecursoUnidadMedida.setFechaActualizacion(new DateTime().toDate());
				}
				result = RecursoUnidadMedidaDAO.guardarRecursoUnidadMedida(RecursoUnidadMedida);
				response_text = String.join("","{ \"success\": ",(result ? "true" : "false")," }");
			}
			else
				response_text = "{ \"success\": false }";
		}
		else if(accion.equals("borrarRecursoUnidadMedida")){
			int id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			if(id>0){
				RecursoUnidadMedida RecursoUnidadMedida = RecursoUnidadMedidaDAO.getRecursoUnidadMedidaPorId(id);
				RecursoUnidadMedida.setUsuarioActualizo("admin");
				RecursoUnidadMedida.setFechaActualizacion(new DateTime().toDate());
				response_text = String.join("","{ \"success\": ",(RecursoUnidadMedidaDAO.eliminarRecursoUnidadMedida(RecursoUnidadMedida) ? "true" : "false")," }");
			}
			else
				response_text = "{ \"success\": false }";
		}
		else if(accion.equals("numeroRecursoUnidadMedidas")){
			response_text = String.join("","{ \"success\": true, \"totalRecursoUnidadMedidas\":",RecursoUnidadMedidaDAO.getTotalRecursoUnidadMedidas().toString()," }");
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
