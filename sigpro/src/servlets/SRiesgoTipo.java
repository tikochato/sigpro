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

import dao.RiesgoTipoDAO;
import dao.RtipoPropiedadDAO;
import pojo.RiesgoPropiedad;
import pojo.RiesgoTipo;
import pojo.RtipoPropiedad;
import pojo.RtipoPropiedadId;
import utilities.Utils;

/**
 * Servlet implementation class SRiesgoTipo
 */
@WebServlet("/SRiesgoTipo")
public class SRiesgoTipo extends HttpServlet {
	private static final long serialVersionUID = 1L;
	class striesgotipo{
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
    public SRiesgoTipo() {
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
		
		if(accion.equals("getRiesgotiposPagina")){
			int pagina = map.get("pagina")!=null  ? Integer.parseInt(map.get("pagina")) : 0;
			int numeroRiesgoTipo = map.get("numeroriesgostipo")!=null  ? Integer.parseInt(map.get("numeroriesgostipo")) : 0;
			List<RiesgoTipo> riesgotipos = RiesgoTipoDAO.getRiesgoTiposPagina(pagina, numeroRiesgoTipo);
			List<striesgotipo> striesgotipos=new ArrayList<striesgotipo>();
			for(RiesgoTipo riesgotipo:riesgotipos){
				striesgotipo temp =new striesgotipo();
				temp.descripcion = riesgotipo.getDescripcion();
				temp.estado = riesgotipo.getEstado();
				temp.fechaActualizacion = Utils.formatDate(riesgotipo.getFechaActualizacion());
				temp.fechaCreacion = Utils.formatDate(riesgotipo.getFechaCreacion());
				temp.id = riesgotipo.getId();
				temp.nombre = riesgotipo.getNombre();
				temp.usuarioActualizo = riesgotipo.getUsuarioActualizo();
				temp.usuarioCreo = riesgotipo.getUsuarioCreo();
				striesgotipos.add(temp);
			}
			
			response_text=new GsonBuilder().serializeNulls().create().toJson(striesgotipos);
	        response_text = String.join("", "\"riesgotipos\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		
		else if(accion.equals("numeroRiesgoTipos")){
			response_text = String.join("","{ \"success\": true, \"totalriesgos\":",RiesgoTipoDAO.getTotalRiesgoTipo().toString()," }");
		}
		else if(accion.equals("guardarRiesgotipo")){
			boolean result = false;
			boolean esnuevo = map.get("esnuevo").equals("true");
			int id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			if(id>0 || esnuevo){
				
				String nombre = map.get("nombre");
				String descripcion = map.get("descripcion");
				RiesgoTipo riesgoTipo;
				
				if(esnuevo){
					riesgoTipo = new RiesgoTipo(nombre, usuario, new DateTime().toDate(), 1);
					riesgoTipo.setDescripcion(descripcion);
				}
				else{
					riesgoTipo = RiesgoTipoDAO.getRiesgoTipoPorId(id);
					riesgoTipo.setNombre(nombre);
					riesgoTipo.setDescripcion(descripcion);
					riesgoTipo.setUsuarioActualizo(usuario);
					riesgoTipo.setFechaActualizacion(new DateTime().toDate());
					Set<RtipoPropiedad> propiedades_temp = riesgoTipo.getRtipoPropiedads();
					riesgoTipo.setRtipoPropiedads(null);
					if (propiedades_temp!=null && propiedades_temp.size()>0){
						for (RtipoPropiedad rtipoPropiedad : propiedades_temp){
							RtipoPropiedadDAO.eliminarTotalRtipoPropiedad(rtipoPropiedad);
						}
					}
				}
				
				result = RiesgoTipoDAO.guardarRiesgoTipo(riesgoTipo);
				
				String[] idsPropiedades =  map.get("propiedades") != null ? map.get("propiedades").toString().split(",") : null;
				if (idsPropiedades !=null && idsPropiedades.length>0){
					for (String idPropiedad : idsPropiedades){
						RtipoPropiedadId rtipoPropiedadId = new RtipoPropiedadId(riesgoTipo.getId(), Integer.parseInt(idPropiedad));
						RiesgoPropiedad riesgoPropiedad = new RiesgoPropiedad();
						riesgoPropiedad.setId(Integer.parseInt(idPropiedad));
						
						RtipoPropiedad rtipoPropiedad = new RtipoPropiedad(
								rtipoPropiedadId, riesgoPropiedad, 
								riesgoTipo, usuario, new DateTime().toDate(),1);
						
						rtipoPropiedad.setRiesgoTipo(riesgoTipo);
						if (riesgoTipo.getRtipoPropiedads() == null){
							riesgoTipo.setRtipoPropiedads(new HashSet<RtipoPropiedad>(0));
						}
						riesgoTipo.getRtipoPropiedads().add(rtipoPropiedad);
					}
				}
				
				result = RiesgoTipoDAO.guardarRiesgoTipo(riesgoTipo);
				response_text = String.join("","{ \"success\": ",(result ? "true" : "false"),", "
						+ "\"id\": " + riesgoTipo.getId() +" }");
			}
			else
				response_text = "{ \"success\": false }";
		}
		else if(accion.equals("borrarRiesgoTipo")){
			int id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			if(id>0){
				RiesgoTipo riesgoTipo = RiesgoTipoDAO.getRiesgoTipoPorId(id);
				response_text = String.join("","{ \"success\": ",(RiesgoTipoDAO.eliminarRiesgoTipo(riesgoTipo) ? "true" : "false")," }");
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
