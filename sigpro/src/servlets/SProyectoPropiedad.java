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

import dao.ProyectoPropiedadDAO;
import dao.ProyectoPropiedadValorDAO;
import pojo.DatoTipo;
import pojo.ProyectoPropedadValor;
import pojo.ProyectoPropiedad;
import utilities.CFormaDinamica;
import utilities.Utils;


@WebServlet("/SProyectoPropiedad")
public class SProyectoPropiedad extends HttpServlet {
	private static final long serialVersionUID = 1L;
	class stproyectopropiedad{
		int id;
		String nombre;
		String descripcion;
		int datotipoid;
		String datotiponombre;
		String usuarioCreo;
		String usuarioActualizo;
		String fechaCreacion;
		String fechaActualizacion;
		int estado;
	}
    
    public SProyectoPropiedad() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
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
		;
		Map<String, String> map = gson.fromJson(sb.toString(), type);
		String accion = map.get("accion");
		String response_text="";
		
		if(accion.equals("getProyectoPropiedadPagina")){
			int pagina = map.get("pagina")!=null  ? Integer.parseInt(map.get("pagina")) : 0;
			int idProyectoPropiedad = map.get("idProyectoTipo")!=null  ? Integer.parseInt(map.get("idProyectoTipo")) : 0;
			String filtro_nombre = map.get("filtro_nombre");
			String filtro_usuario_creo = map.get("filtro_usuario_creo");
			String filtro_fecha_creacion = map.get("filtro_fecha_creacion");
			String columna_ordenada = map.get("columna_ordenada");
			String orden_direccion = map.get("orden_direccion");
			List<ProyectoPropiedad> proyectopropiedades = ProyectoPropiedadDAO.getProyectoPropiedadesPagina(pagina, idProyectoPropiedad,
					 filtro_nombre, filtro_usuario_creo, filtro_fecha_creacion, columna_ordenada, orden_direccion);
			List<stproyectopropiedad> stproyectopropiedad=new ArrayList<stproyectopropiedad>();
			
			for(ProyectoPropiedad proyectopropiedad:proyectopropiedades){
				stproyectopropiedad temp =new stproyectopropiedad();
				temp.id = proyectopropiedad.getId();
				temp.nombre = proyectopropiedad.getNombre();
				temp.descripcion = proyectopropiedad.getDescripcion();
				temp.datotipoid = proyectopropiedad.getDatoTipo().getId();
				temp.datotiponombre = proyectopropiedad.getDatoTipo().getNombre();
				temp.estado = proyectopropiedad.getEstado();
				temp.fechaActualizacion = Utils.formatDateHour(proyectopropiedad.getFechaActualizacion());
				temp.fechaCreacion = Utils.formatDateHour(proyectopropiedad.getFechaCreacion());	
				temp.usuarioActualizo = proyectopropiedad.getUsuarioActualizo();
				temp.usuarioCreo = proyectopropiedad.getUsuarioCreo();
				stproyectopropiedad.add(temp);
			}
			response_text=new GsonBuilder().serializeNulls().create().toJson(stproyectopropiedad);
	        response_text = String.join("", "\"proyectopropiedades\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(accion.equals("getProyectoPropiedadPaginaPorTipoProy")){
			int pagina = map.get("pagina")!=null  ? Integer.parseInt(map.get("pagina")) : 0;
			int idProyectoPropiedad = map.get("idProyectoTipo")!=null  ? Integer.parseInt(map.get("idProyectoTipo")) : 0;
			
			List<ProyectoPropiedad> proyectopropiedades = ProyectoPropiedadDAO.getProyectoPropiedadesPorTipoProyectoPagina(pagina, idProyectoPropiedad);
			List<stproyectopropiedad> stproyectopropiedad=new ArrayList<stproyectopropiedad>();
			for(ProyectoPropiedad proyectopropiedad:proyectopropiedades){
				stproyectopropiedad temp =new stproyectopropiedad();
				temp.id = proyectopropiedad.getId();
				temp.nombre = proyectopropiedad.getNombre();
				temp.descripcion = proyectopropiedad.getDescripcion();
				temp.datotipoid = proyectopropiedad.getDatoTipo().getId();
				temp.datotiponombre = proyectopropiedad.getDatoTipo().getNombre();
				temp.estado = proyectopropiedad.getEstado();
				temp.fechaActualizacion = Utils.formatDateHour(proyectopropiedad.getFechaActualizacion());
				temp.fechaCreacion = Utils.formatDateHour(proyectopropiedad.getFechaCreacion());	
				temp.usuarioActualizo = proyectopropiedad.getUsuarioActualizo();
				temp.usuarioCreo = proyectopropiedad.getUsuarioCreo();
				stproyectopropiedad.add(temp);
			}
			response_text=new GsonBuilder().serializeNulls().create().toJson(stproyectopropiedad);
	        response_text = String.join("", "\"proyectopropiedades\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(accion.equals("getProyectoPropiedadesTotalDisponibles")){
			int pagina = map.get("pagina")!=null  ? Integer.parseInt(map.get("pagina")) : 0;
			String idsPropiedades = map.get("idspropiedades")!=null ? map.get("idspropiedades").toString()   : "0";
			int numeroProyectoPropiedad = map.get("numeroproyectopropiedad")!=null  ? Integer.parseInt(map.get("numeroproyectopropiedad")) : 0;
			List<ProyectoPropiedad> proyectopropiedades = ProyectoPropiedadDAO.getProyectoPropiedadPaginaTotalDisponibles(pagina, numeroProyectoPropiedad,idsPropiedades);
			List<stproyectopropiedad> stproyectopropiedad=new ArrayList<stproyectopropiedad>();
			for(ProyectoPropiedad proyectopropiedad:proyectopropiedades){
				stproyectopropiedad temp =new stproyectopropiedad();
				temp.id = proyectopropiedad.getId();
				temp.nombre = proyectopropiedad.getNombre();
				temp.descripcion = proyectopropiedad.getDescripcion();
				temp.datotipoid = proyectopropiedad.getDatoTipo().getId();
				temp.datotiponombre = proyectopropiedad.getDatoTipo().getNombre();
				temp.estado = proyectopropiedad.getEstado();
				temp.fechaActualizacion = Utils.formatDateHour(proyectopropiedad.getFechaActualizacion());
				temp.fechaCreacion = Utils.formatDateHour(proyectopropiedad.getFechaCreacion());	
				temp.usuarioActualizo = proyectopropiedad.getUsuarioActualizo();
				temp.usuarioCreo = proyectopropiedad.getUsuarioCreo();
				stproyectopropiedad.add(temp);
			}
			response_text=new GsonBuilder().serializeNulls().create().toJson(stproyectopropiedad);
	        response_text = String.join("", "\"proyectopropiedades\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(accion.equals("getProyectoPropiedadPorTipo")){
			int idProyecto = map.get("idProyecto")!=null  ? Integer.parseInt(map.get("idProyecto")) : 0;
			int idProyectoTipo = map.get("idProyectoTipo")!=null  ? Integer.parseInt(map.get("idProyectoTipo")) : 0;
			List<ProyectoPropiedad> proyectoPropiedades = ProyectoPropiedadDAO.getProyectoPropiedadesPorTipoProyecto(idProyectoTipo);
			
			List<HashMap<String,Object>> campos = new ArrayList<>();
			for(ProyectoPropiedad proyectoPropiedad:proyectoPropiedades){
				HashMap <String,Object> campo = new HashMap<String, Object>();
				campo.put("id", proyectoPropiedad.getId());
				campo.put("nombre", proyectoPropiedad.getNombre());
				campo.put("tipo", proyectoPropiedad.getDatoTipo().getId());
				ProyectoPropedadValor proyectoPropiedadValor = ProyectoPropiedadValorDAO.getValorPorProyectoYPropiedad(proyectoPropiedad.getId(), idProyecto);
				if (proyectoPropiedadValor !=null ){
					switch ((Integer) campo.get("tipo")){
						case 1:
							campo.put("valor", proyectoPropiedadValor.getValorString());
							break;
						case 2:
							campo.put("valor", proyectoPropiedadValor.getValorEntero());
							break;
						case 3:
							campo.put("valor", proyectoPropiedadValor.getValorDecimal());
							break;
						case 4:
							campo.put("valor", proyectoPropiedadValor.getValorEntero()==1 ? true : false);
							break;
						case 5:
							campo.put("valor", Utils.formatDate(proyectoPropiedadValor.getValorTiempo()));
							break;
					}
				}
				else{
					campo.put("valor", "");
				}
				campos.add(campo);
			}
			
			response_text = CFormaDinamica.convertirEstructura(campos);
	        response_text = String.join("", "\"proyectopropiedades\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		
		
		else if(accion.equals("guardarProyectoPropiedad")){
			boolean result = false;
			boolean esnuevo = map.get("esnuevo").equals("true");
			int id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			if(id>0 || esnuevo){
				String nombre = map.get("nombre");
				String descripcion = map.get("descripcion");
				int datoTipoId = map.get("datoTipoId")!=null ? Integer.parseInt(map.get("datoTipoId")) : 0;
				
				DatoTipo datoTipo = new DatoTipo();
				datoTipo.setId(datoTipoId);

				ProyectoPropiedad proyectoPropiedad;
				if(esnuevo){
					proyectoPropiedad = new ProyectoPropiedad(datoTipo,nombre, usuario, new DateTime().toDate(), 1);
					proyectoPropiedad.setDescripcion(descripcion);
				}
				else{
					proyectoPropiedad = ProyectoPropiedadDAO.getProyectoPropiedadPorId(id);
					proyectoPropiedad.setNombre(nombre);
					proyectoPropiedad.setDescripcion(descripcion);
					proyectoPropiedad.setUsuarioActualizo(usuario);
					proyectoPropiedad.setFechaActualizacion(new DateTime().toDate());
					proyectoPropiedad.setDatoTipo(datoTipo);
				}
				result = ProyectoPropiedadDAO.guardarProyectoPropiedad(proyectoPropiedad);
				response_text = String.join("","{ \"success\": ",(result ? "true" : "false"),", "
						+ "\"id\": " + proyectoPropiedad.getId(), ","
						, "\"usuarioCreo\": \"" , proyectoPropiedad.getUsuarioCreo(),"\","
						, "\"fechaCreacion\":\" " , Utils.formatDateHour(proyectoPropiedad.getFechaCreacion()),"\","
						, "\"usuarioactualizo\": \"" , proyectoPropiedad.getUsuarioActualizo() != null ? proyectoPropiedad.getUsuarioActualizo() : "","\","
						, "\"fechaactualizacion\": \"" , Utils.formatDateHour(proyectoPropiedad.getFechaActualizacion()),"\""
						," }");
			}
			else
				response_text = "{ \"success\": false }";
		}
		else if(accion.equals("borrarProyectoPropiedad")){

			int id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			if(id>0){
				ProyectoPropiedad proyectoPropiedad = ProyectoPropiedadDAO.getProyectoPropiedadPorId(id);
				proyectoPropiedad.setUsuarioActualizo(usuario);
				response_text = String.join("","{ \"success\": ",(ProyectoPropiedadDAO.eliminarProyectoPropiedad(proyectoPropiedad) ? "true" : "false")," }");
			}
			else
				response_text = "{ \"success\": false }";
		}
		else if(accion.equals("numeroProyectoPropiedadesDisponibles")){
			String idsPropiedades = map.get("idspropiedades")!=null ? map.get("idspropiedades").toString()   : "0";
			response_text = String.join("","{ \"success\": true, \"totalproyectopropiedades\":",ProyectoPropiedadDAO.getTotalProyectoPropiedadesDisponibles(idsPropiedades).toString()," }");
		}
		else if(accion.equals("numeroProyectoPropiedades")){
			String filtro_nombre = map.get("filtro_nombre");
			String filtro_usuario_creo = map.get("filtro_usuario_creo");
			String filtro_fecha_creacion = map.get("filtro_fecha_creacion");
			response_text = String.join("","{ \"success\": true, \"totalproyectopropiedades\":",ProyectoPropiedadDAO.getTotalProyectoPropiedades(filtro_nombre, filtro_usuario_creo, filtro_fecha_creacion).toString()," }");
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
