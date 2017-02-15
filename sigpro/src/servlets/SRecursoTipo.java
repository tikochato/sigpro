package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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


import dao.RecursoTipoDAO;
import dao.RectipoPropiedadDAO;
import pojo.RecursoTipo;
import pojo.RectipoPropiedad;
import pojo.RectipoPropiedadId;
import pojo.RecursoPropiedad;
import utilities.Utils;

/**
 * Servlet implementation class SRecursoTipo
 */
@WebServlet("/SRecursoTipo")
public class SRecursoTipo extends HttpServlet {
	private static final long serialVersionUID = 1L;
	class strecursotipo{
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
    public SRecursoTipo() {
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
		if(accion.equals("getRecursotiposPagina")){
			int pagina = map.get("pagina")!=null  ? Integer.parseInt(map.get("pagina")) : 0;
			int numeroRecursosTipo = map.get("numerorecursostipo")!=null  ? Integer.parseInt(map.get("numerorecursostipo")) : 0;
			String filtro_nombre = map.get("filtro_nombre");
			String filtro_usuario_creo = map.get("filtro_usuario_creo");
			String filtro_fecha_creacion = map.get("filtro_fecha_creacion");
			String columna_ordenada = map.get("columna_ordenada");
			String orden_direccion = map.get("orden_direccion");
			List<RecursoTipo> recursostipos = RecursoTipoDAO.getRecursoTiposPagina(pagina, numeroRecursosTipo,
					filtro_nombre, filtro_usuario_creo, filtro_fecha_creacion, columna_ordenada, orden_direccion);
			List<strecursotipo> strecursotipos=new ArrayList<strecursotipo>();
			for(RecursoTipo recursotipo:recursostipos){
				strecursotipo temp =new strecursotipo();
				temp.descripcion = recursotipo.getDescripcion();
				temp.estado = recursotipo.getEstado();
				temp.fechaActualizacion = Utils.formatDateHour(recursotipo.getFechaActualizacion());
				temp.fechaCreacion = Utils.formatDateHour(recursotipo.getFechaCreacion());
				temp.id = recursotipo.getId();
				temp.nombre = recursotipo.getNombre();
				temp.usuarioActualizo = recursotipo.getUsuarioActualizacion();
				temp.usuarioCreo = recursotipo.getUsuarioCreo();
				strecursotipos.add(temp);
			}
			
			response_text=new GsonBuilder().serializeNulls().create().toJson(strecursotipos);
	        response_text = String.join("", "\"recursotipos\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		
		else if(accion.equals("numeroRecursoTipos")){
			String filtro_nombre = map.get("filtro_nombre");
			String filtro_usuario_creo = map.get("filtro_usuario_creo");
			String filtro_fecha_creacion = map.get("filtro_fecha_creacion");
			response_text = String.join("","{ \"success\": true, \"totalrecursotipos\":",RecursoTipoDAO.getTotalRecursoTipo(filtro_nombre, filtro_usuario_creo, filtro_fecha_creacion).toString()," }");
		}
		else if(accion.equals("guardarRecursotipo")){
			boolean result = false;
			boolean esnuevo = map.get("esnuevo").equals("true");
			int id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			HttpSession sesionweb = request.getSession();
			String usuario = sesionweb.getAttribute("usuario")!= null ? sesionweb.getAttribute("usuario").toString() : null;
			
			if(id>0 || esnuevo){
				
				String nombre = map.get("nombre");
				String descripcion = map.get("descripcion");
				RecursoTipo recursoTipo;
				
				if(esnuevo){
					recursoTipo = new RecursoTipo(nombre, usuario, new DateTime().toDate(), 1);
				}
				else{
					recursoTipo = RecursoTipoDAO.getRecursoTipoPorId(id);
					recursoTipo.setNombre(nombre);
					recursoTipo.setDescripcion(descripcion);
					recursoTipo.setUsuarioActualizacion(usuario);
					recursoTipo.setFechaActualizacion(new DateTime().toDate());
					Set<RectipoPropiedad> propiedades_temp = recursoTipo.getRectipoPropiedads();
					recursoTipo.setRectipoPropiedads(null);
					if (propiedades_temp!=null){
						for (RectipoPropiedad rtipoPropiedad : propiedades_temp){
							RectipoPropiedadDAO.eliminarTotalRtipoPropiedad(rtipoPropiedad);
						}
					}
				}
				
				result = RecursoTipoDAO.guardarRecursoTipo(recursoTipo);
				
				String[] idsPropiedades =  map.get("propiedades") != null ? map.get("propiedades").toString().split(",") : null;
				if (idsPropiedades !=null && idsPropiedades.length>0){
					for (String idPropiedad : idsPropiedades){
						RectipoPropiedadId rtipoPropiedadId = new RectipoPropiedadId(Integer.parseInt(idPropiedad),recursoTipo.getId());
						RecursoPropiedad recursoPropiedad = new RecursoPropiedad();
						recursoPropiedad.setId(Integer.parseInt(idPropiedad));
						
						RectipoPropiedad rtipoPropiedad = new RectipoPropiedad(
								rtipoPropiedadId, recursoPropiedad, 
								recursoTipo, usuario, new DateTime().toDate(),1);
						
						rtipoPropiedad.setRecursoTipo(recursoTipo);
						if (recursoTipo.getRectipoPropiedads() == null){
							recursoTipo.setRectipoPropiedads(new HashSet<RectipoPropiedad>(0));
						}
						recursoTipo.getRectipoPropiedads().add(rtipoPropiedad);
					}
				}
				
				result = RecursoTipoDAO.guardarRecursoTipo(recursoTipo);
				response_text = String.join("","{ \"success\": ",(result ? "true" : "false"),", "
						+ "\"id\": " + recursoTipo.getId() +" }");
			}
			else
				response_text = "{ \"success\": false }";
		}
		else if(accion.equals("borrarRecursoTipo")){
			int id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			if(id>0){
				RecursoTipo recursoTipo = RecursoTipoDAO.getRecursoTipoPorId(id);
				response_text = String.join("","{ \"success\": ",(RecursoTipoDAO.eliminarRecursoTipo(recursoTipo) ? "true" : "false")," }");
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
