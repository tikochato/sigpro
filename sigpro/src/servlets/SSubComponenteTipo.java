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

import dao.SctipoPropiedadDAO;
import dao.SubComponenteTipoDAO;
import pojo.SctipoPropiedad;
import pojo.SctipoPropiedadId;
import pojo.SubcomponentePropiedad;
import pojo.SubcomponenteTipo;
import utilities.Utils;

/**
 * Servlet implementation class SSubComponenteTipo
 */
@WebServlet("/SSubComponenteTipo")
public class SSubComponenteTipo extends HttpServlet {
	private static final long serialVersionUID = 1L;
	class stsubcomponentetipo{
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
    public SSubComponenteTipo() {
        super();
        
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String response_text = "{ \"success\": false }";
		
		response.setHeader("Content-Encoding", "gzip");
		response.setCharacterEncoding("UTF-8");
		
        OutputStream output = response.getOutputStream();
		GZIPOutputStream gz = new GZIPOutputStream(output);
        gz.write(response_text.getBytes("UTF-8"));
        gz.close();
        output.close();
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
		if(accion.equals("getSubComponentetiposPagina")){
			int pagina = map.get("pagina")!=null  ? Integer.parseInt(map.get("pagina")) : 0;
			int numeroSubComponenteTipo = map.get("numerosubcomponentetipos")!=null  ? Integer.parseInt(map.get("numerosubcomponentetipos")) : 0;
			String filtro_nombre = map.get("filtro_nombre");
			String filtro_usuario_creo = map.get("filtro_usuario_creo");
			String filtro_fecha_creacion = map.get("filtro_fecha_creacion");
			String columna_ordenada = map.get("columna_ordenada");
			String orden_direccion = map.get("orden_direccion");
			List<SubcomponenteTipo> subcomponentetipos = SubComponenteTipoDAO.getSubComponenteTiposPagina(pagina, numeroSubComponenteTipo
					,filtro_nombre,filtro_usuario_creo,filtro_fecha_creacion,columna_ordenada,orden_direccion);
			List<stsubcomponentetipo> stsubcomponentetipos=new ArrayList<stsubcomponentetipo>();
			for(SubcomponenteTipo subcomponentetipo:subcomponentetipos){
				stsubcomponentetipo temp =new stsubcomponentetipo();
				temp.descripcion = subcomponentetipo.getDescripcion();
				temp.estado = subcomponentetipo.getEstado();
				temp.fechaActualizacion = Utils.formatDateHour(subcomponentetipo.getFechaActualizacion());
				temp.fechaCreacion = Utils.formatDateHour(subcomponentetipo.getFechaCreacion());
				temp.id = subcomponentetipo.getId();
				temp.nombre = subcomponentetipo.getNombre();
				temp.usuarioActualizo = subcomponentetipo.getUsuarioActualizo();
				temp.usuarioCreo = subcomponentetipo.getUsuarioCreo();
				stsubcomponentetipos.add(temp);
			}
			
			response_text=new GsonBuilder().serializeNulls().create().toJson(stsubcomponentetipos);
	        response_text = String.join("", "\"subcomponentetipos\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		
		else if(accion.equals("numeroSubComponenteTipos")){
			String filtro_nombre = map.get("filtro_nombre");
			String filtro_usuario_creo = map.get("filtro_usuario_creo");
			String filtro_fecha_creacion = map.get("filtro_fecha_creacion");
			
			response_text = String.join("","{ \"success\": true, \"totalsubcomponentetipos\":",SubComponenteTipoDAO
					.getTotalSubComponenteTipo(filtro_nombre,filtro_usuario_creo,filtro_fecha_creacion).toString()," }");
		}
		else if(accion.equals("guardarSubComponentetipo")){
			boolean result = false;
			boolean esnuevo = map.get("esnuevo").equals("true");
			int id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			if(id>0 || esnuevo){
				
				String nombre = map.get("nombre");
				String descripcion = map.get("descripcion");
				SubcomponenteTipo subcomponenteTipo;
				
				if(esnuevo){
					subcomponenteTipo = new SubcomponenteTipo(nombre, usuario, new DateTime().toDate(), 1);
				}
				else{
					subcomponenteTipo = SubComponenteTipoDAO.getSubComponenteTipoPorId(id);
					subcomponenteTipo.setNombre(nombre);
					subcomponenteTipo.setDescripcion(descripcion);
					subcomponenteTipo.setUsuarioActualizo(usuario);
					subcomponenteTipo.setFechaActualizacion(new DateTime().toDate());
					Set<SctipoPropiedad> propiedades_temp = subcomponenteTipo.getSctipoPropiedads();
					subcomponenteTipo.setSctipoPropiedads(null);
					if (propiedades_temp!=null){
						for (SctipoPropiedad sctipoPropiedad : propiedades_temp){
							SctipoPropiedadDAO.eliminarTotalSctipoPropiedad(sctipoPropiedad);
						}
					}
				}
				
				result = SubComponenteTipoDAO.guardarSubComponenteTipo(subcomponenteTipo);
				
				String[] idsPropiedades =  map.get("propiedades") != null ? map.get("propiedades").toString().split(",") : null;
				if (idsPropiedades !=null && idsPropiedades.length>0){
					for (String idPropiedad : idsPropiedades){
						SctipoPropiedadId sctipoPropiedadId = new SctipoPropiedadId(subcomponenteTipo.getId(), Integer.parseInt(idPropiedad));
						SubcomponentePropiedad subcomponentePropiedad = new SubcomponentePropiedad();
						subcomponentePropiedad.setId(Integer.parseInt(idPropiedad));
						
						SctipoPropiedad sctipoPropiedad = new SctipoPropiedad(
								sctipoPropiedadId, subcomponentePropiedad, 
								subcomponenteTipo, usuario, new DateTime().toDate());
						
						sctipoPropiedad.setSubcomponenteTipo(subcomponenteTipo);
						if (subcomponenteTipo.getSctipoPropiedads() == null){
							subcomponenteTipo.setSctipoPropiedads(new HashSet<SctipoPropiedad>(0));
						}
						subcomponenteTipo.getSctipoPropiedads().add(sctipoPropiedad);
					}
				}
				
				result = SubComponenteTipoDAO.guardarSubComponenteTipo(subcomponenteTipo);
				response_text = String.join("","{ \"success\": ",(result ? "true" : "false"),", "
						+ "\"id\": " + subcomponenteTipo.getId() , ","
						, "\"usuarioCreo\": \"" , subcomponenteTipo.getUsuarioCreo(),"\","
						, "\"fechaCreacion\":\" " , Utils.formatDateHour(subcomponenteTipo.getFechaCreacion()),"\","
						, "\"usuarioactualizo\": \"" , subcomponenteTipo.getUsuarioActualizo() != null ? subcomponenteTipo.getUsuarioActualizo() : "","\","
						, "\"fechaactualizacion\": \"" , Utils.formatDateHour(subcomponenteTipo.getFechaActualizacion()),"\""+
						" }");
			}
			else
				response_text = "{ \"success\": false }";
		}
		else if(accion.equals("borrarSubComponenteTipo")){
			int id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			if(id>0){
				SubcomponenteTipo subcomponenteTipo = SubComponenteTipoDAO.getSubComponenteTipoPorId(id);
				response_text = String.join("","{ \"success\": ",(SubComponenteTipoDAO.eliminarSubComponenteTipo(subcomponenteTipo) ? "true" : "false")," }");
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
