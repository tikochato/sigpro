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

import dao.ProgramaPropiedadDAO;
import dao.ProgramaPropiedadValorDAO;
import pojo.DatoTipo;
import pojo.ProgramaPropiedad;
import pojo.ProgramaPropiedadValor;
import utilities.CFormaDinamica;
import utilities.Utils;

/**
 * Servlet implementation class SProgramaPropiedad
 */
@WebServlet("/SProgramaPropiedad")
public class SProgramaPropiedad extends HttpServlet {
	private static final long serialVersionUID = 1L;
	class stprogramapropiedad{
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
    
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SProgramaPropiedad() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append( "{ \"success\": false }").append(request.getContextPath());
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
		
		if(accion.equals("getProgramaPropiedadPagina")){
			int pagina = map.get("pagina")!=null  ? Integer.parseInt(map.get("pagina")) : 0;
			int idProgramaPropiedad = map.get("idProgramaTipo")!=null  ? Integer.parseInt(map.get("idProgramaTipo")) : 0;
			String filtro_nombre = map.get("filtro_nombre");
			String filtro_usuario_creo = map.get("filtro_usuario_creo");
			String filtro_fecha_creacion = map.get("filtro_fecha_creacion");
			String columna_ordenada = map.get("columna_ordenada");
			String orden_direccion = map.get("orden_direccion");
			List<ProgramaPropiedad> programaPropiedades = ProgramaPropiedadDAO.getProgramaPropiedadesPagina(pagina, idProgramaPropiedad,
					 filtro_nombre, filtro_usuario_creo, filtro_fecha_creacion, columna_ordenada, orden_direccion);
			List<stprogramapropiedad> stprogramapropiedad=new ArrayList<stprogramapropiedad>();
			
			for(ProgramaPropiedad programaPropiedad:programaPropiedades){
				stprogramapropiedad temp =new stprogramapropiedad();
				temp.id = programaPropiedad.getId();
				temp.nombre = programaPropiedad.getNombre();
				temp.descripcion = programaPropiedad.getDescripcion();
				temp.datotipoid = programaPropiedad.getDatoTipo().getId();
				temp.datotiponombre = programaPropiedad.getDatoTipo().getNombre();
				temp.estado = programaPropiedad.getEstado();
				temp.fechaActualizacion = Utils.formatDateHour(programaPropiedad.getFechaActualizacion());
				temp.fechaCreacion = Utils.formatDateHour(programaPropiedad.getFechaCreacion());	
				temp.usuarioActualizo = programaPropiedad.getUsuarioActualizo();
				temp.usuarioCreo = programaPropiedad.getUsuarioCreo();
				stprogramapropiedad.add(temp);
			}
			response_text=new GsonBuilder().serializeNulls().create().toJson(stprogramapropiedad);
	        response_text = String.join("", "\"programapropiedades\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(accion.equals("getProgramaPropiedadPaginaPorTipoProg")){
			int pagina = map.get("pagina")!=null  ? Integer.parseInt(map.get("pagina")) : 0;
			int idProgramaPropiedad = map.get("idProgramaTipo")!=null  ? Integer.parseInt(map.get("idProgramaTipo")) : 0;
			
			List<ProgramaPropiedad> programaPropiedades = ProgramaPropiedadDAO.getProgramaPropiedadesPorTipoProgramaPagina(pagina, idProgramaPropiedad);
			List<stprogramapropiedad> stprogramapropiedad=new ArrayList<stprogramapropiedad>();
			for(ProgramaPropiedad programapropiedad:programaPropiedades){
				stprogramapropiedad temp =new stprogramapropiedad();
				temp.id = programapropiedad.getId();
				temp.nombre = programapropiedad.getNombre();
				temp.descripcion = programapropiedad.getDescripcion();
				temp.datotipoid = programapropiedad.getDatoTipo().getId();
				temp.datotiponombre = programapropiedad.getDatoTipo().getNombre();
				temp.estado = programapropiedad.getEstado();
				temp.fechaActualizacion = Utils.formatDateHour(programapropiedad.getFechaActualizacion());
				temp.fechaCreacion = Utils.formatDateHour(programapropiedad.getFechaCreacion());	
				temp.usuarioActualizo = programapropiedad.getUsuarioActualizo();
				temp.usuarioCreo = programapropiedad.getUsuarioCreo();
				stprogramapropiedad.add(temp);
			}
			response_text=new GsonBuilder().serializeNulls().create().toJson(stprogramapropiedad);
	        response_text = String.join("", "\"programapropiedades\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(accion.equals("getProgramaPropiedadesTotalDisponibles")){
			int pagina = map.get("pagina")!=null  ? Integer.parseInt(map.get("pagina")) : 0;
			String idsPropiedades = map.get("idspropiedades")!=null ? map.get("idspropiedades").toString()   : "0";
			int numeroProgramaPropiedad = map.get("numeroprogramapropiedad")!=null  ? Integer.parseInt(map.get("numeroprogramapropiedad")) : 0;
			List<ProgramaPropiedad> programapropiedades = ProgramaPropiedadDAO.getProgramaPropiedadPaginaTotalDisponibles(pagina, numeroProgramaPropiedad,idsPropiedades);
			List<stprogramapropiedad> stprogramapropiedad=new ArrayList<stprogramapropiedad>();
			for(ProgramaPropiedad programapropiedad:programapropiedades){
				stprogramapropiedad temp =new stprogramapropiedad();
				temp.id = programapropiedad.getId();
				temp.nombre = programapropiedad.getNombre();
				temp.descripcion = programapropiedad.getDescripcion();
				temp.datotipoid = programapropiedad.getDatoTipo().getId();
				temp.datotiponombre = programapropiedad.getDatoTipo().getNombre();
				temp.estado = programapropiedad.getEstado();
				temp.fechaActualizacion = Utils.formatDateHour(programapropiedad.getFechaActualizacion());
				temp.fechaCreacion = Utils.formatDateHour(programapropiedad.getFechaCreacion());	
				temp.usuarioActualizo = programapropiedad.getUsuarioActualizo();
				temp.usuarioCreo = programapropiedad.getUsuarioCreo();
				stprogramapropiedad.add(temp);
			}
			response_text=new GsonBuilder().serializeNulls().create().toJson(stprogramapropiedad);
	        response_text = String.join("", "\"programapropiedades\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(accion.equals("getProgramaPropiedadPorTipo")){
			int idPrograma = map.get("idPrograma")!=null  ? Integer.parseInt(map.get("idPrograma")) : 0;
			int idProgramaTipo = map.get("idProgramaTipo")!=null  ? Integer.parseInt(map.get("idProgramaTipo")) : 0;
			List<ProgramaPropiedad> programaPropiedades = ProgramaPropiedadDAO.getProgramaPropiedadesPorTipoPrograma(idProgramaTipo);
			
			List<HashMap<String,Object>> campos = new ArrayList<>();
			for(ProgramaPropiedad programaPropiedad:programaPropiedades){
				HashMap <String,Object> campo = new HashMap<String, Object>();
				campo.put("id", programaPropiedad.getId());
				campo.put("nombre", programaPropiedad.getNombre());
				campo.put("tipo", programaPropiedad.getDatoTipo().getId());
				ProgramaPropiedadValor programaPropiedadValor = ProgramaPropiedadValorDAO.getValorPorProgramaYPropiedad(programaPropiedad.getId(), idPrograma);
				if (programaPropiedadValor !=null ){
					switch ((Integer) campo.get("tipo")){
						case 1:
							campo.put("valor", programaPropiedadValor.getValorString());
							break;
						case 2:
							campo.put("valor", programaPropiedadValor.getValorEntero());
							break;
						case 3:
							campo.put("valor", programaPropiedadValor.getValorDecimal());
							break;
						case 4:
							campo.put("valor", programaPropiedadValor.getValorEntero()==1 ? true : false);
							break;
						case 5:
							campo.put("valor", Utils.formatDate(programaPropiedadValor.getValorTiempo()));
							break;
					}
				}
				else{
					campo.put("valor", "");
				}
				campos.add(campo);
			}
			
			response_text = CFormaDinamica.convertirEstructura(campos);
	        response_text = String.join("", "\"programapropiedades\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(accion.equals("guardarProgramaPropiedad")){
			boolean result = false;
			boolean esnuevo = map.get("esnuevo").equals("true");
			int id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			if(id>0 || esnuevo){
				String nombre = map.get("nombre");
				String descripcion = map.get("descripcion");
				int datoTipoId = map.get("datoTipoId")!=null ? Integer.parseInt(map.get("datoTipoId")) : 0;
				
				DatoTipo datoTipo = new DatoTipo();
				datoTipo.setId(datoTipoId);

				ProgramaPropiedad programaPropiedad;
				if(esnuevo){
					programaPropiedad = new ProgramaPropiedad(datoTipo,nombre, usuario, new DateTime().toDate(), 1);
					programaPropiedad.setDescripcion(descripcion);
				}
				else{
					programaPropiedad = ProgramaPropiedadDAO.getProgramaPropiedadPorId(id);
					programaPropiedad.setNombre(nombre);
					programaPropiedad.setDescripcion(descripcion);
					programaPropiedad.setUsuarioActualizo(usuario);
					programaPropiedad.setFechaActualizacion(new DateTime().toDate());
					programaPropiedad.setDatoTipo(datoTipo);
				}
				result = ProgramaPropiedadDAO.guardarProgramaPropiedad(programaPropiedad);
				response_text = String.join("","{ \"success\": ",(result ? "true" : "false"),", "
						+ "\"id\": " + programaPropiedad.getId() +" }");
			}
			else
				response_text = "{ \"success\": false }";
		}
		else if(accion.equals("borrarProgramaPropiedad")){

			int id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			if(id>0){
				ProgramaPropiedad programaPropiedad = ProgramaPropiedadDAO.getProgramaPropiedadPorId(id);
				programaPropiedad.setUsuarioActualizo(usuario);
				response_text = String.join("","{ \"success\": ",(ProgramaPropiedadDAO.eliminarProgramaPropiedad(programaPropiedad) ? "true" : "false")," }");
			}
			else
				response_text = "{ \"success\": false }";
		}
		else if(accion.equals("numeroProgramaPropiedadesDisponibles")){
			String idsPropiedades = map.get("idspropiedades")!=null ? map.get("idspropiedades").toString()   : "0";
			response_text = String.join("","{ \"success\": true, \"totalprogramapropiedades\":",ProgramaPropiedadDAO.getTotalProgramaPropiedadesDisponibles(idsPropiedades).toString()," }");
		}
		else if(accion.equals("numeroProgramaPropiedades")){
			String filtro_nombre = map.get("filtro_nombre");
			String filtro_usuario_creo = map.get("filtro_usuario_creo");
			String filtro_fecha_creacion = map.get("filtro_fecha_creacion");
			response_text = String.join("","{ \"success\": true, \"totalprogramapropiedades\":",ProgramaPropiedadDAO.getTotalProgramaPropiedades(filtro_nombre, filtro_usuario_creo, filtro_fecha_creacion).toString()," }");
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
