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

import dao.SubComponentePropiedadDAO;
import dao.SubComponentePropiedadValorDAO;
import pojo.DatoTipo;
import pojo.SubcomponentePropiedad;
import pojo.SubcomponentePropiedadValor;
import utilities.CFormaDinamica;
import utilities.Utils;

/**
 * Servlet implementation class SSubComponentePropiedad
 */
@WebServlet("/SSubComponentePropiedad")
public class SSubComponentePropiedad extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	class stsubcomponentepropiedad{
		int id;
		String nombre;
		String descripcion;
		int datotipoid;
		String datotiponombre;
		String usuarioCreo;
		String usuarioActualizo;
		String fechaCreacion;
		String fechaActualizacion;
		Integer estado;
	}
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SSubComponentePropiedad() {
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
		
		
		if(accion.equals("getSubComponentePropiedadPaginaPorTipo")){
			int pagina = map.get("pagina")!=null  ? Integer.parseInt(map.get("pagina")) : 0;
			int idTipoSubComponente = map.get("idSubComponenteTipo")!=null  ? Integer.parseInt(map.get("idSubComponenteTipo")) : 0;
			List<SubcomponentePropiedad> subcompoentepropiedades = SubComponentePropiedadDAO.getSubComponentePropiedadesPorTipoSubComponentePagina(pagina, idTipoSubComponente);
			List<stsubcomponentepropiedad> stsubcomponentepropiedad=new ArrayList<stsubcomponentepropiedad>();
			for(SubcomponentePropiedad subcomponentepropiedad:subcompoentepropiedades){
				stsubcomponentepropiedad temp =new stsubcomponentepropiedad();
				temp.id = subcomponentepropiedad.getId();
				temp.nombre = subcomponentepropiedad.getNombre();
				temp.descripcion = subcomponentepropiedad.getDescripcion();
				temp.datotipoid = subcomponentepropiedad.getDatoTipo().getId();
				temp.datotiponombre = subcomponentepropiedad.getDatoTipo().getNombre();
				temp.fechaActualizacion = Utils.formatDateHour(subcomponentepropiedad.getFechaActualizacion());
				temp.fechaCreacion = Utils.formatDateHour(subcomponentepropiedad.getFechaCreacion());	
				temp.usuarioActualizo = subcomponentepropiedad.getUsuarioActualizo();
				temp.usuarioCreo = subcomponentepropiedad.getUsuarioCreo();
				temp.estado = subcomponentepropiedad.getEstado();
				stsubcomponentepropiedad.add(temp);
			}
			response_text=new GsonBuilder().serializeNulls().create().toJson(stsubcomponentepropiedad);
	        response_text = String.join("", "\"componentepropiedades\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(accion.equals("getSubComponentePropiedadPagina")){
			int pagina = map.get("pagina")!=null  ? Integer.parseInt(map.get("pagina")) : 0;
			int numeroSubComponentePropiedad = map.get("numerosubcomponentepropiedades")!=null  ? Integer.parseInt(map.get("numerosubcomponentepropiedades")) : 0;
			String filtro_nombre = map.get("filtro_nombre");
			String filtro_usuario_creo = map.get("filtro_usuario_creo");
			String filtro_fecha_creacion = map.get("filtro_fecha_creacion");
			String columna_ordenada = map.get("columna_ordenada");
			String orden_direccion = map.get("orden_direccion");
			List<SubcomponentePropiedad> subcompoentepropiedades = SubComponentePropiedadDAO.getSubComponentePropiedadesPagina(pagina,numeroSubComponentePropiedad,
					filtro_nombre,filtro_usuario_creo,filtro_fecha_creacion,columna_ordenada,orden_direccion);
			List<stsubcomponentepropiedad> stcomponentepropiedad=new ArrayList<stsubcomponentepropiedad>();
			for(SubcomponentePropiedad subcomponentepropiedad:subcompoentepropiedades){
				stsubcomponentepropiedad temp =new stsubcomponentepropiedad();
				temp.id = subcomponentepropiedad.getId();
				temp.nombre = subcomponentepropiedad.getNombre();
				temp.descripcion = subcomponentepropiedad.getDescripcion();
				temp.datotipoid = subcomponentepropiedad.getDatoTipo().getId();
				temp.datotiponombre = subcomponentepropiedad.getDatoTipo().getNombre();
				temp.fechaActualizacion = Utils.formatDateHour(subcomponentepropiedad.getFechaActualizacion());
				temp.fechaCreacion = Utils.formatDateHour(subcomponentepropiedad.getFechaCreacion());	
				temp.usuarioActualizo = subcomponentepropiedad.getUsuarioActualizo();
				temp.usuarioCreo = subcomponentepropiedad.getUsuarioCreo();
				temp.estado = subcomponentepropiedad.getEstado();
				stcomponentepropiedad.add(temp);
			}
			response_text=new GsonBuilder().serializeNulls().create().toJson(stcomponentepropiedad);
	        response_text = String.join("", "\"componentepropiedades\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(accion.equals("getSubComponentePropiedadesTotalDisponibles")){
			int pagina = map.get("pagina")!=null  ? Integer.parseInt(map.get("pagina")) : 0;
			String idsPropiedades = map.get("idspropiedades")!=null ? map.get("idspropiedades").toString()   : "0";
			int numeroSubComponentePropiedad = map.get("numerosubcomponentepropiedad")!=null  ? Integer.parseInt(map.get("numerosubcomponentepropiedad")) : 0;
			List<SubcomponentePropiedad> subcomponentepropiedades = SubComponentePropiedadDAO.getSubComponentePropiedadPaginaTotalDisponibles(pagina, numeroSubComponentePropiedad,idsPropiedades);
			List<stsubcomponentepropiedad> stsubcomponentepropiedad=new ArrayList<stsubcomponentepropiedad>();
			for(SubcomponentePropiedad subcomponentepropiedad:subcomponentepropiedades){
				stsubcomponentepropiedad temp =new stsubcomponentepropiedad();
				temp.id = subcomponentepropiedad.getId();
				temp.nombre = subcomponentepropiedad.getNombre();
				temp.descripcion = subcomponentepropiedad.getDescripcion();
				temp.datotipoid = subcomponentepropiedad.getDatoTipo().getId();
				temp.datotiponombre = subcomponentepropiedad.getDatoTipo().getNombre();
				temp.fechaActualizacion = Utils.formatDateHour(subcomponentepropiedad.getFechaActualizacion());
				temp.fechaCreacion = Utils.formatDateHour(subcomponentepropiedad.getFechaCreacion());	
				temp.usuarioActualizo = subcomponentepropiedad.getUsuarioActualizo();
				temp.usuarioCreo = subcomponentepropiedad.getUsuarioCreo();
				temp.estado = subcomponentepropiedad.getEstado();
				stsubcomponentepropiedad.add(temp);
			}
			response_text=new GsonBuilder().serializeNulls().create().toJson(stsubcomponentepropiedad);
	        response_text = String.join("", "\"componentepropiedades\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(accion.equals("numeroSubComponentePropiedadesDisponibles")){
			response_text = String.join("","{ \"success\": true, \"totalcomponentepropiedades\":",SubComponentePropiedadDAO.getTotalSubComponentePropiedades().toString()," }");
		}
		else if(accion.equals("numeroSubComponentePropiedades")){
			String filtro_nombre = map.get("filtro_nombre");
			String filtro_usuario_creo = map.get("filtro_usuario_creo");
			String filtro_fecha_creacion = map.get("filtro_fecha_creacion");
			response_text = String.join("","{ \"success\": true, \"totalsubcomponentepropiedades\":",SubComponentePropiedadDAO.getTotalSubComponentePropiedad(filtro_nombre, filtro_usuario_creo, filtro_fecha_creacion).toString()," }");
		}
		else if(accion.equals("guardarSubComponentePropiedad")){
			boolean result = false;
			boolean esnuevo = map.get("esnuevo").equals("true");
			int id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			if(id>0 || esnuevo){
				
				String nombre = map.get("nombre");
				String descripcion = map.get("descripcion");
				int datoTipoId = map.get("datoTipoId")!=null ? Integer.parseInt(map.get("datoTipoId")) : 0;
				DatoTipo datoTipo = new DatoTipo();
				datoTipo.setId(datoTipoId);
				
				SubcomponentePropiedad subcomponentePropiedad;
				if(esnuevo){
					subcomponentePropiedad = new SubcomponentePropiedad(datoTipo, nombre, usuario, new DateTime().toDate(), 1);
				}
				else{
					subcomponentePropiedad = SubComponentePropiedadDAO.getSubComponentePropiedadPorId(id);
					
					subcomponentePropiedad.setNombre(nombre);
					subcomponentePropiedad.setDescripcion(descripcion);
					subcomponentePropiedad.setUsuarioActualizo(usuario);
					subcomponentePropiedad.setFechaActualizacion(new DateTime().toDate());
					subcomponentePropiedad.setDatoTipo(datoTipo);
				}
				result = SubComponentePropiedadDAO.guardarSubComponentePropiedad(subcomponentePropiedad);
				response_text = String.join("","{ \"success\": ",(result ? "true" : "false"),", "
						+ "\"id\": " + subcomponentePropiedad.getId() , ","
						, "\"usuarioCreo\": \"" , subcomponentePropiedad.getUsuarioCreo(),"\","
						, "\"fechaCreacion\":\" " , Utils.formatDateHour(subcomponentePropiedad.getFechaCreacion()),"\","
						, "\"usuarioactualizo\": \"" , subcomponentePropiedad.getUsuarioActualizo() != null ? subcomponentePropiedad.getUsuarioActualizo() : "","\","
						, "\"fechaactualizacion\": \"" , Utils.formatDateHour(subcomponentePropiedad.getFechaActualizacion()),"\""+
						" }");
			}
			else
				response_text = "{ \"success\": false }";
		}	
		else if(accion.equals("borrarSubComponentePropiedad")){
			
			int id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			if(id>0){
				SubcomponentePropiedad subcomponentePropiedad = SubComponentePropiedadDAO.getSubComponentePropiedadPorId(id);
				subcomponentePropiedad.setUsuarioActualizo(usuario);
				response_text = String.join("","{ \"success\": ",(SubComponentePropiedadDAO.eliminarSubComponentePropiedad(subcomponentePropiedad) ? "true" : "false")," }");
			}
			else
				response_text = "{ \"success\": false }";
		}
		else if(accion.equals("getSubComponentePropiedadPorTipo")){
			int idSubComponente = map.get("idSubComponente")!=null  ? Integer.parseInt(map.get("idSubComponente")) : 0;
			int idSubComponenteTipo = map.get("idSubComponenteTipo")!=null  ? Integer.parseInt(map.get("idSubComponenteTipo")) : 0;
			List<SubcomponentePropiedad> subcompoentepropiedades = SubComponentePropiedadDAO.getSubComponentePropiedadesPorTipoComponente(idSubComponenteTipo);
			
			List<HashMap<String,Object>> campos = new ArrayList<>();
			for(SubcomponentePropiedad subcomponentepropiedad:subcompoentepropiedades){
				HashMap <String,Object> campo = new HashMap<String, Object>();
				campo.put("id", subcomponentepropiedad.getId());
				campo.put("nombre", subcomponentepropiedad.getNombre());
				campo.put("tipo", subcomponentepropiedad.getDatoTipo().getId());
				SubcomponentePropiedadValor subcoomponentePropiedadValor = SubComponentePropiedadValorDAO.getValorPorSubComponenteYPropiedad(subcomponentepropiedad.getId(), idSubComponente);
				if (subcoomponentePropiedadValor !=null ){
					switch (subcomponentepropiedad.getDatoTipo().getId()){
						case 1:
							campo.put("valor",  subcoomponentePropiedadValor.getValorString());
							break;
						case 2:
							campo.put("valor", subcoomponentePropiedadValor.getValorEntero());
							break;
						case 3:
							campo.put("valor", subcoomponentePropiedadValor.getValorDecimal());
							break;
						case 4:
							campo.put("valor", subcoomponentePropiedadValor.getValorEntero()==1 ? true : false);
							break;	
						case 5:
							campo.put("valor", Utils.formatDate(subcoomponentePropiedadValor.getValorTiempo()));
							break;
					}
				}
				else{
					campo.put("valor", "");
				}
				campos.add(campo);
			}
			
			response_text = CFormaDinamica.convertirEstructura(campos);
	        response_text = String.join("", "\"subcomponentepropiedades\":",response_text);
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
