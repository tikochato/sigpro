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

import dao.ProgramaTipoDAO;
import dao.ProgtipoPropiedadDAO;
import pojo.ProgramaPropiedad;
import pojo.ProgramaTipo;
import pojo.ProgtipoPropiedad;
import pojo.ProgtipoPropiedadId;
import utilities.Utils;


/**
 * Servlet implementation class SProgramaTipo
 */
@WebServlet("/SProgramaTipo")
public class SProgramaTipo extends HttpServlet {
	private static final long serialVersionUID = 1L;
	class stprogramatipo{
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
    public SProgramaTipo() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("{ \"success\": false }").append(request.getContextPath());
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
		if(accion.equals("getProgramaTipoPagina")){
			int pagina = map.get("pagina")!=null  ? Integer.parseInt(map.get("pagina")) : 0;
			int numeroProgramaTipos = map.get("numeroprogramatipo")!=null  ? Integer.parseInt(map.get("numeroprogramatipo")) : 0;
			String filtro_nombre = map.get("filtro_nombre");
			String filtro_usuario_creo = map.get("filtro_usuario_creo");
			String filtro_fecha_creacion = map.get("filtro_fecha_creacion");
			String columna_ordenada = map.get("columna_ordenada");
			String orden_direccion = map.get("orden_direccion");
			
			
			List<ProgramaTipo> programatipos = ProgramaTipoDAO.getProgramaTipoPagina(pagina, numeroProgramaTipos, filtro_nombre, filtro_usuario_creo, filtro_fecha_creacion, columna_ordenada, orden_direccion);
			List<stprogramatipo> stcooperantes=new ArrayList<stprogramatipo>();
			for(ProgramaTipo programatipo:programatipos){
				stprogramatipo temp =new stprogramatipo();
				temp.id = programatipo.getId();
				temp.nombre = programatipo.getNombre();
				temp.descripcion = programatipo.getDescripcion();
				
				temp.estado = programatipo.getEstado();
				temp.fechaActualizacion = Utils.formatDateHour(programatipo.getFechaActualizacion());
				temp.fechaCreacion = Utils.formatDateHour(programatipo.getFechaCreacion());
				temp.usuarioActualizo = programatipo.getUsuarioActualizo();
				temp.usuarioCreo = programatipo.getUsarioCreo();
				stcooperantes.add(temp);
			}
			
			response_text=new GsonBuilder().serializeNulls().create().toJson(stcooperantes);
	        response_text = String.join("", "\"porgramatipos\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(accion.equals("numeroProgramaTipos")){
			String filtro_nombre = map.get("filtro_nombre");
			String filtro_usuario_creo = map.get("filtro_usuario_creo");
			String filtro_fecha_creacion = map.get("filtro_fecha_creacion");
			response_text = String.join("","{ \"success\": true, \"totalprogramatipos\":",ProgramaTipoDAO.getTotalProgramaTipos(filtro_nombre, filtro_usuario_creo, filtro_fecha_creacion).toString()," }");
		}
		else if(accion.equals("guardarProgramatipo")){
			boolean result = false;
			boolean esnuevo = map.get("esnuevo").equals("true");
			int id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			if(id>0 || esnuevo){
				
				String nombre = map.get("nombre");
				String descripcion = map.get("descripcion");
				ProgramaTipo programaTipo;
				
				if(esnuevo){
					programaTipo = new ProgramaTipo(nombre, descripcion, usuario, null, new DateTime().toDate(), null, 1, null, null);
				}
				else{
					programaTipo = ProgramaTipoDAO.getProgramaTipoPorId(id);
					programaTipo.setNombre(nombre);
					programaTipo.setDescripcion(descripcion);
					programaTipo.setUsuarioActualizo(usuario);
					programaTipo.setFechaActualizacion(new DateTime().toDate());
					Set<ProgtipoPropiedad> propiedades_temp = programaTipo.getProgtipoPropiedads();
					programaTipo.setProgtipoPropiedads(null);
					if (propiedades_temp!=null){
						for (ProgtipoPropiedad progtipoPropiedad : propiedades_temp){
							ProgtipoPropiedadDAO.eliminarTotalProgtipoPropiedad(progtipoPropiedad);
						}
					}
				}
				
				result = ProgramaTipoDAO.guardarProgramaTipo(programaTipo);
				
				String[] idsPropiedades =  map.get("propiedades") != null && map.get("propiedades").length()>0 ? map.get("propiedades").toString().split(",") : null;
				if (idsPropiedades !=null && idsPropiedades.length>0){
					for (String idPropiedad : idsPropiedades){
						ProgtipoPropiedadId progtipoPropiedadId = new ProgtipoPropiedadId(programaTipo.getId(), Integer.parseInt(idPropiedad));
						ProgramaPropiedad programaPropiedad = new ProgramaPropiedad();
						programaPropiedad.setId(Integer.parseInt(idPropiedad));
						
						ProgtipoPropiedad progtipoPropiedad = new ProgtipoPropiedad(
								progtipoPropiedadId,
								programaPropiedad,
								programaTipo, 
								usuario, new DateTime().toDate());
						
						progtipoPropiedad.setProgramaTipo(programaTipo);
						if (programaTipo.getProgtipoPropiedads() == null){
							programaTipo.setProgtipoPropiedads(new HashSet<ProgtipoPropiedad>(0));
						}
						programaTipo.getProgtipoPropiedads().add(progtipoPropiedad);
					}
				}
				
				result = ProgramaTipoDAO.guardarProgramaTipo(programaTipo);
				response_text = String.join("","{ \"success\": ",(result ? "true" : "false"),", "
						+ "\"id\": " + programaTipo.getId() +" }");
			}
			else
				response_text = "{ \"success\": false }";
		}
		else if(accion.equals("borrarProgramaTipo")){
			int id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			if(id>0){
				ProgramaTipo programaTipo = ProgramaTipoDAO.getProgramaTipoPorId(id);
				response_text = String.join("","{ \"success\": ",(ProgramaTipoDAO.eliminarProgramaTipo(programaTipo) ? "true" : "false")," }");
			}
			else
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
