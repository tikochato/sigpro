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

import dao.RecursoPropiedadDAO;
import pojo.RecursoPropiedad;
import pojo.DatoTipo;
import utilities.Utils;

/**
 * Servlet implementation class SRecursoPropiedad
 */
@WebServlet("/SRecursoPropiedad")
public class SRecursoPropiedad extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	class strecursopropiedad{
		int id;
		String nombre;
		String descripcion;
		int datotipoid;
		String datotiponombre;
		String usuarioCreo;
		String usuarioActualizo;
		String fechaCreacion;
		String fechaActualizacion;
	}
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SRecursoPropiedad() {
        super();
        // TODO Auto-generated constructor stub
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
		;
		Map<String, String> map = gson.fromJson(sb.toString(), type);
		String accion = map.get("accion");
		String response_text="";
		
		
		if(accion.equals("getRecursoPropiedadPaginaPorTipo")){
			int pagina = map.get("pagina")!=null  ? Integer.parseInt(map.get("pagina")) : 0;
			int idTipoRecurso = map.get("idRecursoTipo")!=null  ? Integer.parseInt(map.get("idRecursoTipo")) : 0;
			List<RecursoPropiedad> recursopropiedades = RecursoPropiedadDAO.getRecursoPropiedadesPorTipoRecursoPagina(pagina, idTipoRecurso);
			List<strecursopropiedad> strecursopropiedad=new ArrayList<strecursopropiedad>();
			for(RecursoPropiedad recursopropiedad:recursopropiedades){
				strecursopropiedad temp =new strecursopropiedad();
				temp.id = recursopropiedad.getId();
				temp.nombre = recursopropiedad.getNombre();
				temp.descripcion = recursopropiedad.getDescripcion();
				temp.datotipoid = recursopropiedad.getDatoTipo().getId();
				temp.datotiponombre = recursopropiedad.getDatoTipo().getNombre();
				temp.fechaActualizacion = Utils.formatDate(recursopropiedad.getFechaActualizacion());
				temp.fechaCreacion = Utils.formatDate(recursopropiedad.getFechaCreacion());	
				temp.usuarioActualizo = recursopropiedad.getUsuarioActualizo();
				temp.usuarioCreo = recursopropiedad.getUsuarioCreo();
				strecursopropiedad.add(temp);
			}
			response_text=new GsonBuilder().serializeNulls().create().toJson(strecursopropiedad);
	        response_text = String.join("", "\"recursopropiedades\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(accion.equals("getRecursoPropiedadPagina")){
			int pagina = map.get("pagina")!=null  ? Integer.parseInt(map.get("pagina")) : 0;
			int numeroRecursoPropiedad = map.get("numerorecursopropiedad")!=null  ? Integer.parseInt(map.get("numerorecursopropiedad")) : 0;	
			String filtro_nombre = map.get("filtro_nombre");
			String filtro_usuario_creo = map.get("filtro_usuario_creo");
			String filtro_fecha_creacion = map.get("filtro_fecha_creacion");
			String columna_ordenada = map.get("columna_ordenada");
			String orden_direccion = map.get("orden_direccion");
			List<RecursoPropiedad> recursopropiedades = RecursoPropiedadDAO.getRecursoPropiedadesPagina(pagina,numeroRecursoPropiedad,
					filtro_nombre, filtro_usuario_creo, filtro_fecha_creacion, columna_ordenada, orden_direccion);
			List<strecursopropiedad> strecursopropiedad=new ArrayList<strecursopropiedad>();
			for(RecursoPropiedad recursopropiedad:recursopropiedades){
				strecursopropiedad temp =new strecursopropiedad();
				temp.id = recursopropiedad.getId();
				temp.nombre = recursopropiedad.getNombre();
				temp.descripcion = recursopropiedad.getDescripcion();
				temp.datotipoid = recursopropiedad.getDatoTipo().getId();
				temp.datotiponombre = recursopropiedad.getDatoTipo().getNombre();
				temp.fechaActualizacion = Utils.formatDate(recursopropiedad.getFechaActualizacion());
				temp.fechaCreacion = Utils.formatDate(recursopropiedad.getFechaCreacion());	
				temp.usuarioActualizo = recursopropiedad.getUsuarioActualizo();
				temp.usuarioCreo = recursopropiedad.getUsuarioCreo();
				strecursopropiedad.add(temp);
			}
			response_text=new GsonBuilder().serializeNulls().create().toJson(strecursopropiedad);
	        response_text = String.join("", "\"recursopropiedades\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(accion.equals("getRecursoPropiedadesTotalDisponibles")){
			int pagina = map.get("pagina")!=null  ? Integer.parseInt(map.get("pagina")) : 0;
			String idsPropiedades = map.get("idspropiedades")!=null ? map.get("idspropiedades").toString()   : "0";
			int numeroRecursoPropiedad = map.get("numerorecursopropiedad")!=null  ? Integer.parseInt(map.get("numerorecursopropiedad")) : 0;
			List<RecursoPropiedad> recursopropiedades = RecursoPropiedadDAO.getRecursoPropiedadPaginaTotalDisponibles(pagina, numeroRecursoPropiedad,idsPropiedades);
			List<strecursopropiedad> strecursopropiedad=new ArrayList<strecursopropiedad>();
			for(RecursoPropiedad recursopropiedad:recursopropiedades){
				strecursopropiedad temp =new strecursopropiedad();
				temp.id = recursopropiedad.getId();
				temp.nombre = recursopropiedad.getNombre();
				temp.descripcion = recursopropiedad.getDescripcion();
				temp.datotipoid = recursopropiedad.getDatoTipo().getId();
				temp.datotiponombre = recursopropiedad.getNombre();
				temp.fechaActualizacion = Utils.formatDate(recursopropiedad.getFechaActualizacion());
				temp.fechaCreacion = Utils.formatDate(recursopropiedad.getFechaCreacion());	
				temp.usuarioActualizo = recursopropiedad.getUsuarioActualizo();
				temp.usuarioCreo = recursopropiedad.getUsuarioCreo();
				strecursopropiedad.add(temp);
			}
			response_text=new GsonBuilder().serializeNulls().create().toJson(strecursopropiedad);
	        response_text = String.join("", "\"recursopropiedades\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(accion.equals("numeroRecursoPropiedadesDisponibles")){
			response_text = String.join("","{ \"success\": true, \"totalrecursopropiedades\":",RecursoPropiedadDAO.getTotalRecursoPropiedades().toString()," }");
		}
		else if(accion.equals("numeroRecursoPropiedades")){
			String filtro_nombre = map.get("filtro_nombre");
			String filtro_usuario_creo = map.get("filtro_usuario_creo");
			String filtro_fecha_creacion = map.get("filtro_fecha_creacion");
			response_text = String.join("","{ \"success\": true, \"totalrecursopropiedades\":",RecursoPropiedadDAO.getTotalRecursoPropiedad(
					filtro_nombre,filtro_usuario_creo, filtro_fecha_creacion).toString()," }");
		}
		else if(accion.equals("guardarRecursoPropiedad")){
			

			boolean result = false;
			boolean esnuevo = map.get("esnuevo").equals("true");
			int id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			if(id>0 || esnuevo){
				
				String nombre = map.get("nombre");
				String descripcion = map.get("descripcion");
				int datoTipoId = map.get("datoTipoId")!=null ? Integer.parseInt(map.get("datoTipoId")) : 0;
				DatoTipo datoTipo = new DatoTipo();
				datoTipo.setId(datoTipoId);
				
				RecursoPropiedad componentePropiedad;
				if(esnuevo){
					componentePropiedad = new RecursoPropiedad(datoTipo, nombre, descripcion, usuario,null, new DateTime().toDate(),null, 1, null);
				}
				else{
					componentePropiedad = RecursoPropiedadDAO.getRecursoPropiedadPorId(id);
					
					componentePropiedad.setNombre(nombre);
					componentePropiedad.setDescripcion(descripcion);
					componentePropiedad.setUsuarioActualizo(usuario);
					componentePropiedad.setFechaActualizacion(new DateTime().toDate());
				}
				result = RecursoPropiedadDAO.guardarRecursoPropiedad(componentePropiedad);
				response_text = String.join("","{ \"success\": ",(result ? "true" : "false"),", "
						+ "\"id\": " + componentePropiedad.getId() +" }");
			}
			else
				response_text = "{ \"success\": false }";
		}	
		else if(accion.equals("borrarRecursoPropiedad")){
			
			int id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			if(id>0){
				RecursoPropiedad componentePropiedad = RecursoPropiedadDAO.getRecursoPropiedadPorId(id);
				componentePropiedad.setUsuarioActualizo(usuario);
				response_text = String.join("","{ \"success\": ",(RecursoPropiedadDAO.eliminarRecursoPropiedad(componentePropiedad) ? "true" : "false")," }");
			}
			else
				response_text = "{ \"success\": false }";
		}else
			response_text = "{ \"success\": false }";
		
		response.setHeader("Content-Encoding", "gzip");
		response.setCharacterEncoding("UTF-8");
		
        OutputStream output = response.getOutputStream();
		GZIPOutputStream gz = new GZIPOutputStream(output);
        gz.write(response_text.getBytes("UTF-8"));
        gz.close();
        output.close();
	}
	

}
