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

import dao.RiesgoPropiedadDAO;
import pojo.RiesgoPropiedad;
import pojo.DatoTipo;
import utilities.Utils;

/**
 * Servlet implementation class SRiesgoPropiedad
 */
@WebServlet("/SRiesgoPropiedad")
public class SRiesgoPropiedad extends HttpServlet {
	private static final long serialVersionUID = 1L;
	class striesgopropiedad{
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
    public SRiesgoPropiedad() {
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
		
		
		if(accion.equals("getRiesgoPropiedadPaginaPorTipo")){
			int pagina = map.get("pagina")!=null  ? Integer.parseInt(map.get("pagina")) : 0;
			int idRiesgoTipo = map.get("idRiesgoTipo")!=null  ? Integer.parseInt(map.get("idRiesgoTipo")) : 0;
			List<RiesgoPropiedad> riesgopropiedades = RiesgoPropiedadDAO.getRiesgoPropiedadesPorTipoRiesgoPagina(pagina, idRiesgoTipo);
			List<striesgopropiedad> striesgopropiedad=new ArrayList<striesgopropiedad>();
			for(RiesgoPropiedad riesgopropiedad:riesgopropiedades){
				striesgopropiedad temp =new striesgopropiedad();
				temp.id = riesgopropiedad.getId();
				temp.nombre = riesgopropiedad.getNombre();
				temp.descripcion = riesgopropiedad.getDescripcion();
				
				temp.fechaActualizacion = Utils.formatDate(riesgopropiedad.getFechaActualizacion());
				temp.fechaCreacion = Utils.formatDate(riesgopropiedad.getFechaCreacion());	
				temp.usuarioActualizo = riesgopropiedad.getUsuarioActualizo();
				temp.usuarioCreo = riesgopropiedad.getUsuarioCreo();
				striesgopropiedad.add(temp);
			}
			response_text=new GsonBuilder().serializeNulls().create().toJson(striesgopropiedad);
	        response_text = String.join("", "\"riesgopropiedades\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(accion.equals("getRiesgoPropiedadPagina")){
			int pagina = map.get("pagina")!=null  ? Integer.parseInt(map.get("pagina")) : 0;
			int numeroRiesgoPropiedad = map.get("numeroriesgopropiedad")!=null  ? Integer.parseInt(map.get("numeroriesgopropiedad")) : 0;			
			List<RiesgoPropiedad> riesgopropiedades = RiesgoPropiedadDAO.getRiesgoPropiedadesPagina(pagina,numeroRiesgoPropiedad);
			List<striesgopropiedad> striesgopropiedad=new ArrayList<striesgopropiedad>();
			for(RiesgoPropiedad riesgopropiedad:riesgopropiedades){
				striesgopropiedad temp =new striesgopropiedad();
				temp.id = riesgopropiedad.getId();
				temp.nombre = riesgopropiedad.getNombre();
				temp.descripcion = riesgopropiedad.getDescripcion();
				//temp.datotipoid = riesgopropiedad.getDatoTipo().getId();
				//temp.datotiponombre = riesgopropiedad.getDatoTipo().getNombre();
				temp.fechaActualizacion = Utils.formatDate(riesgopropiedad.getFechaActualizacion());
				temp.fechaCreacion = Utils.formatDate(riesgopropiedad.getFechaCreacion());	
				temp.usuarioActualizo = riesgopropiedad.getUsuarioActualizo();
				temp.usuarioCreo = riesgopropiedad.getUsuarioCreo();
				striesgopropiedad.add(temp);
			}
			response_text=new GsonBuilder().serializeNulls().create().toJson(striesgopropiedad);
	        response_text = String.join("", "\"riesgopropiedades\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(accion.equals("getRiesgoPropiedadesTotalDisponibles")){
			int pagina = map.get("pagina")!=null  ? Integer.parseInt(map.get("pagina")) : 0;
			String idsPropiedades = map.get("idspropiedades")!=null ? map.get("idspropiedades").toString()   : "0";
			int numeroRiesgoPropiedad = map.get("numerocomponentepropiedad")!=null  ? Integer.parseInt(map.get("numerocomponentepropiedad")) : 0;
			List<RiesgoPropiedad> riesgopropiedades = RiesgoPropiedadDAO.getRiesgoPropiedadPaginaTotalDisponibles(pagina, numeroRiesgoPropiedad,idsPropiedades);
			List<striesgopropiedad> streisgopropiedad=new ArrayList<striesgopropiedad>();
			for(RiesgoPropiedad riesgopropiedad:riesgopropiedades){
				striesgopropiedad temp =new striesgopropiedad();
				temp.id = riesgopropiedad.getId();
				temp.nombre = riesgopropiedad.getNombre();
				temp.descripcion = riesgopropiedad.getDescripcion();
				//temp.datotipoid = riesgopropiedad.getDatoTipo().getId();
				//temp.datotiponombre = riesgopropiedad.getDatoTipo.getNombre();
				temp.fechaActualizacion = Utils.formatDate(riesgopropiedad.getFechaActualizacion());
				temp.fechaCreacion = Utils.formatDate(riesgopropiedad.getFechaCreacion());	
				temp.usuarioActualizo = riesgopropiedad.getUsuarioActualizo();
				temp.usuarioCreo = riesgopropiedad.getUsuarioCreo();
				streisgopropiedad.add(temp);
			}
			response_text=new GsonBuilder().serializeNulls().create().toJson(streisgopropiedad);
	        response_text = String.join("", "\"riesgopropiedades\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(accion.equals("numeroRiesgoPropiedadesDisponibles")){
			response_text = String.join("","{ \"success\": true, \"totalriesgopropiedades\":",RiesgoPropiedadDAO.getTotalRiesgoPropiedades().toString()," }");
		}
		else if(accion.equals("numeroRiesgoPropiedades")){
			response_text = String.join("","{ \"success\": true, \"totalriesgopropiedades\":",RiesgoPropiedadDAO.getTotalRiesgoPropiedad().toString()," }");
		}
		else if(accion.equals("guardarRiesgoPropiedad")){
			boolean result = false;
			boolean esnuevo = map.get("esnuevo").equals("true");
			int id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			if(id>0 || esnuevo){
				String nombre = map.get("nombre");
				String descripcion = map.get("descripcion");
				int datoTipoId = map.get("datoTipoId")!=null ? Integer.parseInt(map.get("datoTipoId")) : 0;
				DatoTipo datoTipo = new DatoTipo();
				datoTipo.setId(datoTipoId);
				
				RiesgoPropiedad riesgoPropiedad;
				if(esnuevo){
					riesgoPropiedad = new RiesgoPropiedad(nombre, usuario, new DateTime().toDate(), 1);
				}
				else{
					riesgoPropiedad = RiesgoPropiedadDAO.getRiesgoPropiedadPorId(id);
					riesgoPropiedad.setNombre(nombre);
					riesgoPropiedad.setDescripcion(descripcion);
					riesgoPropiedad.setUsuarioActualizo(usuario);
					riesgoPropiedad.setFechaActualizacion(new DateTime().toDate());
				}
				result = RiesgoPropiedadDAO.guardarRiesgoPropiedad(riesgoPropiedad);
				response_text = String.join("","{ \"success\": ",(result ? "true" : "false"),", "
						+ "\"id\": " + riesgoPropiedad.getId() +" }");
			}
			else
				response_text = "{ \"success\": false }";
		}	
		else if(accion.equals("borrarComponentePropiedad")){
			
			int id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			if(id>0){
				RiesgoPropiedad riesgoPropiedad = RiesgoPropiedadDAO.getRiesgoPropiedadPorId(id);
				riesgoPropiedad.setUsuarioActualizo(usuario);
				response_text = String.join("","{ \"success\": ",(RiesgoPropiedadDAO.eliminarRiesgoPropiedad(riesgoPropiedad) ? "true" : "false")," }");
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
