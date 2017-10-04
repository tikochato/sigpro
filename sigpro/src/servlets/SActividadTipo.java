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


import dao.ActividadTipoDAO;
import dao.AtipoPropiedadDAO;
import pojo.ActividadPropiedad;
import pojo.ActividadTipo;
import pojo.AtipoPropiedad;
import pojo.AtipoPropiedadId;
import utilities.Utils;

/**
 * Servlet implementation class SActividadTipo
 */
@WebServlet("/SActividadTipo")
public class SActividadTipo extends HttpServlet {
	private static final long serialVersionUID = 1L;
	class stactividadtipo{
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
    public SActividadTipo() {
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
		if(accion.equals("getActividadtipos")){
			List<ActividadTipo> actividadtipos = ActividadTipoDAO.getActividadTipos();
			List<stactividadtipo> stactividadtipos=new ArrayList<stactividadtipo>();
			for(ActividadTipo actividadtipo:actividadtipos){
				stactividadtipo temp =new stactividadtipo();
				temp.descripcion = actividadtipo.getDescripcion();
				temp.estado = actividadtipo.getEstado();
				temp.fechaActualizacion = Utils.formatDateHour(actividadtipo.getFechaActualizacion());
				temp.fechaCreacion = Utils.formatDateHour(actividadtipo.getFechaCreacion());
				temp.id = actividadtipo.getId();
				temp.nombre = actividadtipo.getNombre();
				temp.usuarioActualizo = actividadtipo.getUsuarioActualizo();
				temp.usuarioCreo = actividadtipo.getUsuarioCreo();
				stactividadtipos.add(temp);
			}

			response_text=new GsonBuilder().serializeNulls().create().toJson(stactividadtipos);
	        response_text = String.join("", "\"actividadtipos\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(accion.equals("guardarActividadtipo")){
			boolean result = false;
			boolean esnuevo = map.get("esnuevo").equals("true");
			int id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			if(id>0 || esnuevo){

				String nombre = map.get("nombre");
				String descripcion = map.get("descripcion");
				ActividadTipo actividadTipo;

				if(esnuevo){
					actividadTipo = new ActividadTipo(nombre, null,usuario, null, new DateTime().toDate(), null, 1,null, null);
				}
				else{
					actividadTipo = ActividadTipoDAO.getActividadTipoPorId(id);
					actividadTipo.setNombre(nombre);
					actividadTipo.setDescripcion(descripcion);
					actividadTipo.setUsuarioActualizo(usuario);
					actividadTipo.setFechaActualizacion(new DateTime().toDate());
					Set<AtipoPropiedad> propiedades_temp = actividadTipo.getAtipoPropiedads();
					actividadTipo.setAtipoPropiedads(null);
					if (propiedades_temp!=null){
						for (AtipoPropiedad ctipoPropiedad : propiedades_temp){
							AtipoPropiedadDAO.eliminarTotalAtipoPropiedad(ctipoPropiedad);
						}
					}
				}

				result = ActividadTipoDAO.guardarActividadTipo(actividadTipo);

				String[] idsPropiedades =  map.get("propiedades") != null ? map.get("propiedades").toString().split(",") : null;
				if (idsPropiedades !=null && idsPropiedades.length>0){
					for (String idPropiedad : idsPropiedades){
						AtipoPropiedadId atipoPropiedadId = new AtipoPropiedadId(actividadTipo.getId(), Integer.parseInt(idPropiedad));
						ActividadPropiedad actividadPropiedad = new ActividadPropiedad();
						actividadPropiedad.setId(Integer.parseInt(idPropiedad));

						AtipoPropiedad atipoPropiedad = new AtipoPropiedad(
								atipoPropiedadId, actividadPropiedad,
								actividadTipo, usuario, null,new DateTime().toDate(),null);

						atipoPropiedad.setActividadTipo(actividadTipo);
						if (actividadTipo.getAtipoPropiedads() == null){
							actividadTipo.setAtipoPropiedads(new HashSet<AtipoPropiedad>(0));
						}
						actividadTipo.getAtipoPropiedads().add(atipoPropiedad);
					}
				}

				result = ActividadTipoDAO.guardarActividadTipo(actividadTipo);
				response_text = String.join("","{ \"success\": ",(result ? "true" : "false"),", "
						+ "\"id\": " + actividadTipo.getId(), ","
						, "\"usuarioCreo\": \"" , actividadTipo.getUsuarioCreo(),"\","
						, "\"fechaCreacion\":\" " , Utils.formatDateHour(actividadTipo.getFechaCreacion()),"\","
						, "\"usuarioactualizo\": \"" , actividadTipo.getUsuarioActualizo() != null ? actividadTipo.getUsuarioActualizo() : "","\","
						, "\"fechaactualizacion\": \"" , Utils.formatDateHour(actividadTipo.getFechaActualizacion()),"\""
						+" }");
			}
			else
				response_text = "{ \"success\": false }";
		}
		else if(accion.equals("borrarActividadTipo")){
			int id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			if(id>0){
				ActividadTipo actividadTipo = ActividadTipoDAO.getActividadTipoPorId(id);
				response_text = String.join("","{ \"success\": ",(ActividadTipoDAO.eliminarActividadTipo(actividadTipo) ? "true" : "false")," }");
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
