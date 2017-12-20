package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
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

import dao.ActividadPropiedadDAO;
import dao.ActividadPropiedadValorDAO;
import pojo.ActividadPropiedad;
import pojo.ActividadPropiedadValor;
import pojo.DatoTipo;
import utilities.CFormaDinamica;
import utilities.Utils;

/**
 * Servlet implementation class SActividadPropiedad
 */
@WebServlet("/SActividadPropiedad")
public class SActividadPropiedad extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	class stactividadpropiedad{
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
    public SActividadPropiedad() {
        super();
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
		
		
		if(accion.equals("getActividadPropiedadPaginaPorTipo")){
			int pagina = map.get("pagina")!=null  ? Integer.parseInt(map.get("pagina")) : 0;
			int idActividadTipo = map.get("idActividadTipo")!=null  ? Integer.parseInt(map.get("idActividadTipo")) : 0;
			List<ActividadPropiedad> actividadpropiedades = ActividadPropiedadDAO.getActividadPropiedadesPorTipoActividadPagina(pagina, idActividadTipo);
			List<stactividadpropiedad> stactividadpropiedad=new ArrayList<stactividadpropiedad>();
			for(ActividadPropiedad actividadpropiedad:actividadpropiedades){
				stactividadpropiedad temp =new stactividadpropiedad();
				temp.id = actividadpropiedad.getId();
				temp.nombre = actividadpropiedad.getNombre();
				temp.descripcion = actividadpropiedad.getDescripcion();
				temp.datotipoid = actividadpropiedad.getDatoTipo().getId();
				temp.datotiponombre = actividadpropiedad.getDatoTipo().getNombre();
				temp.fechaActualizacion = Utils.formatDateHour(actividadpropiedad.getFechaActualizacion());
				temp.fechaCreacion = Utils.formatDateHour(actividadpropiedad.getFechaCreacion());	
				temp.usuarioActualizo = actividadpropiedad.getUsuarioActualizo();
				temp.usuarioCreo = actividadpropiedad.getUsuarioCreo();
				stactividadpropiedad.add(temp);
			}
			response_text=new GsonBuilder().serializeNulls().create().toJson(stactividadpropiedad);
	        response_text = String.join("", "\"actividadpropiedades\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(accion.equals("getActividadPropiedadPagina")){
			int pagina = map.get("pagina")!=null  ? Integer.parseInt(map.get("pagina")) : 0;
			int numeroActividadPropiedad = map.get("numeroactividadpropiedad")!=null  ? Integer.parseInt(map.get("numeroactividadpropiedad")) : 0;	
			String filtro_nombre = map.get("filtro_nombre");
			String filtro_usuario_creo = map.get("filtro_usuario_creo");
			String filtro_fecha_creacion = map.get("filtro_fecha_creacion");
			String columna_ordenada = map.get("columna_ordenada");
			String orden_direccion = map.get("orden_direccion");
			List<ActividadPropiedad> actividadpropiedades = ActividadPropiedadDAO.getActividadPropiedadesPagina(pagina,numeroActividadPropiedad,
					filtro_nombre, filtro_usuario_creo, filtro_fecha_creacion, columna_ordenada, orden_direccion);
			List<stactividadpropiedad> stactividadpropiedad=new ArrayList<stactividadpropiedad>();
			for(ActividadPropiedad actividadpropiedad:actividadpropiedades){
				stactividadpropiedad temp =new stactividadpropiedad();
				temp.id = actividadpropiedad.getId();
				temp.nombre = actividadpropiedad.getNombre();
				temp.descripcion = actividadpropiedad.getDescripcion();
				temp.datotipoid = actividadpropiedad.getDatoTipo().getId();
				temp.datotiponombre = actividadpropiedad.getDatoTipo().getNombre();
				temp.fechaActualizacion = Utils.formatDateHour(actividadpropiedad.getFechaActualizacion());
				temp.fechaCreacion = Utils.formatDateHour(actividadpropiedad.getFechaCreacion());	
				temp.usuarioActualizo = actividadpropiedad.getUsuarioActualizo();
				temp.usuarioCreo = actividadpropiedad.getUsuarioCreo();
				stactividadpropiedad.add(temp);
			}
			response_text=new GsonBuilder().serializeNulls().create().toJson(stactividadpropiedad);
	        response_text = String.join("", "\"actividadpropiedades\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(accion.equals("getActividadPropiedadesTotalDisponibles")){
			int pagina = map.get("pagina")!=null  ? Integer.parseInt(map.get("pagina")) : 0;
			String idsPropiedades = map.get("idspropiedades")!=null ? map.get("idspropiedades").toString()   : "0";
			int numeroActividadPropiedad = map.get("numeroactividadpropiedad")!=null  ? Integer.parseInt(map.get("numeroactividadpropiedad")) : 0;
			List<ActividadPropiedad> actividadpropiedades = ActividadPropiedadDAO.getActividadPropiedadPaginaTotalDisponibles(pagina, numeroActividadPropiedad,idsPropiedades);
			List<stactividadpropiedad> stactividadpropiedad=new ArrayList<stactividadpropiedad>();
			for(ActividadPropiedad actividadpropiedad:actividadpropiedades){
				stactividadpropiedad temp =new stactividadpropiedad();
				temp.id = actividadpropiedad.getId();
				temp.nombre = actividadpropiedad.getNombre();
				temp.descripcion = actividadpropiedad.getDescripcion();
				temp.datotipoid = actividadpropiedad.getDatoTipo().getId();
				temp.datotiponombre = actividadpropiedad.getDatoTipo().getNombre();
				temp.fechaActualizacion = Utils.formatDateHour(actividadpropiedad.getFechaActualizacion());
				temp.fechaCreacion = Utils.formatDateHour(actividadpropiedad.getFechaCreacion());	
				temp.usuarioActualizo = actividadpropiedad.getUsuarioActualizo();
				temp.usuarioCreo = actividadpropiedad.getUsuarioCreo();
				stactividadpropiedad.add(temp);
			}
			response_text=new GsonBuilder().serializeNulls().create().toJson(stactividadpropiedad);
	        response_text = String.join("", "\"actividadpropiedades\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(accion.equals("numeroActividadPropiedadesDisponibles")){
			response_text = String.join("","{ \"success\": true, \"totalactividadpropiedades\":",ActividadPropiedadDAO.getTotalActividadPropiedades().toString()," }");
		}
		else if(accion.equals("numeroActividadPropiedades")){
			String filtro_nombre = map.get("filtro_nombre");
			String filtro_usuario_creo = map.get("filtro_usuario_creo");
			String filtro_fecha_creacion = map.get("filtro_fecha_creacion");
			response_text = String.join("","{ \"success\": true, \"totalactividadpropiedades\":",ActividadPropiedadDAO.getTotalActividadPropiedad(filtro_nombre, filtro_usuario_creo, filtro_fecha_creacion).toString()," }");
		}
		else if(accion.equals("guardarActividadPropiedad")){
			boolean result = false;
			boolean esnuevo = map.get("esnuevo").equals("true");
			int id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			if(id>0 || esnuevo){
				
				String nombre = map.get("nombre");
				String descripcion = map.get("descripcion");
				int datoTipoId = map.get("datoTipoId")!=null ? Integer.parseInt(map.get("datoTipoId")) : 0;
				DatoTipo datoTipo = new DatoTipo();
				datoTipo.setId(datoTipoId);
				
				ActividadPropiedad actividadPropiedad;
				if(esnuevo){
					actividadPropiedad = new ActividadPropiedad(datoTipo, nombre, descripcion, usuario, null, new DateTime().toDate(), null, 1, null, null);
				}
				else{
					actividadPropiedad = ActividadPropiedadDAO.getActividadPropiedadPorId(id);
					
					actividadPropiedad.setNombre(nombre);
					actividadPropiedad.setDescripcion(descripcion);
					actividadPropiedad.setUsuarioActualizo(usuario);
					actividadPropiedad.setFechaActualizacion(new DateTime().toDate());
					actividadPropiedad.setDatoTipo(datoTipo);
				}
				result = ActividadPropiedadDAO.guardarActividadPropiedad(actividadPropiedad);
				response_text = String.join("","{ \"success\": ",(result ? "true" : "false"),", "
						+ "\"id\": " + actividadPropiedad.getId() ,","
						, "\"usuarioCreo\": \"" , actividadPropiedad.getUsuarioCreo(),"\","
						, "\"fechaCreacion\":\" " , Utils.formatDateHour(actividadPropiedad.getFechaCreacion()),"\","
						, "\"usuarioactualizo\": \"" , actividadPropiedad.getUsuarioActualizo() != null ? actividadPropiedad.getUsuarioActualizo() : "","\","
						, "\"fechaactualizacion\": \"" , Utils.formatDateHour(actividadPropiedad.getFechaActualizacion()),"\""+
						" }");
			}
			else
				response_text = "{ \"success\": false }";
		}	
		else if(accion.equals("borrarActividadPropiedad")){
			
			int id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			if(id>0){
				ActividadPropiedad actividadPropiedad = ActividadPropiedadDAO.getActividadPropiedadPorId(id);
				actividadPropiedad.setUsuarioActualizo(usuario);
				response_text = String.join("","{ \"success\": ",(ActividadPropiedadDAO.eliminarActividadPropiedad(actividadPropiedad) ? "true" : "false")," }");
			}
			else
				response_text = "{ \"success\": false }";
		}
		else if(accion.equals("getActividadPropiedadPorTipo")){
			int idActividad = map.get("idActividad")!=null  ? Integer.parseInt(map.get("idActividad")) : 0;
			int idActividadTipo = map.get("idActividadTipo")!=null || !map.get("idActividadTipo").isEmpty() ? Integer.parseInt(map.get("idActividadTipo")) : 0;
			List<ActividadPropiedad> actividadpropiedades = ActividadPropiedadDAO.getActividadPropiedadesPorTipoActividad(idActividadTipo);
			
			List<HashMap<String,Object>> campos = new ArrayList<>();
			for(ActividadPropiedad actividadpropiedad:actividadpropiedades){
				HashMap <String,Object> campo = new HashMap<String, Object>();
				campo.put("id", actividadpropiedad.getId());
				campo.put("nombre", actividadpropiedad.getNombre());
				campo.put("tipo", actividadpropiedad.getDatoTipo().getId());
				ActividadPropiedadValor coomponentePropiedadValor = ActividadPropiedadValorDAO.getValorPorActividadYPropiedad(actividadpropiedad.getId(), idActividad);
				if (coomponentePropiedadValor !=null ){
					switch (actividadpropiedad.getDatoTipo().getId()){
						case 1:
							campo.put("valor", coomponentePropiedadValor.getValorString());
							break;
						case 2:
							campo.put("valor", coomponentePropiedadValor.getValorEntero());
							break;
						case 3:
							campo.put("valor", coomponentePropiedadValor.getValorDecimal());
							break;
						case 4:
							campo.put("valor", coomponentePropiedadValor.getValorEntero()==1 ? true : false);
							break;	
						case 5:
							campo.put("valor", Utils.formatDate(coomponentePropiedadValor.getValorTiempo()));
							break;
					}
				}
				else{
					campo.put("valor", "");
				}
				campos.add(campo);
			}
			
			response_text = CFormaDinamica.convertirEstructura(campos);
	        response_text = String.join("", "\"actividadpropiedades\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else
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
