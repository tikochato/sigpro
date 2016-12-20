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

import org.joda.time.DateTime;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import dao.ProyectoTipoDAO;
import dao.PtipoPropiedadDAO;
import pojo.ProyectoPropiedad;
import pojo.ProyectoTipo;
import pojo.PtipoPropiedad;
import pojo.PtipoPropiedadId;
import utilities.Utils;


@WebServlet("/SProyectoTipo")
public class SProyectoTipo extends HttpServlet {
	private static final long serialVersionUID = 1L;
	class stproyectotipo{
		Integer id;
		String nombre;
		String descripcion;
		String usuarioCreo;
		String usuarioActualizo;
		String fechaCreacion;
		String fechaActualizacion;
		int estado;
	}
       
    public SProyectoTipo() {
        super();
    }
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	
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
		if(accion.equals("getProyectoTipoPagina")){
			int pagina = map.get("pagina")!=null  ? Integer.parseInt(map.get("pagina")) : 0;
			int numeroProyectoTipos = map.get("numeroproyectotipo")!=null  ? Integer.parseInt(map.get("numeroproyectotipo")) : 0;
			List<ProyectoTipo> proyectotipos = ProyectoTipoDAO.getProyectosTipoPagina(pagina, numeroProyectoTipos);
			List<stproyectotipo> stcooperantes=new ArrayList<stproyectotipo>();
			for(ProyectoTipo proyectotipo:proyectotipos){
				stproyectotipo temp =new stproyectotipo();
				temp.id = proyectotipo.getId();
				temp.nombre = proyectotipo.getNombre();
				temp.descripcion = proyectotipo.getDescripcion();
				
				temp.estado = proyectotipo.getEstado();
				temp.fechaActualizacion = Utils.formatDate(proyectotipo.getFechaActualizacion());
				temp.fechaCreacion = Utils.formatDate(proyectotipo.getFechaCreacion());
				temp.usuarioActualizo = proyectotipo.getUsuarioActualizo();
				temp.usuarioCreo = proyectotipo.getUsarioCreo();
				stcooperantes.add(temp);
			}
			
			response_text=new GsonBuilder().serializeNulls().create().toJson(stcooperantes);
	        response_text = String.join("", "\"poryectotipos\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(accion.equals("numeroProyectoTipos")){
			response_text = String.join("","{ \"success\": true, \"totalproyectotipos\":",ProyectoTipoDAO.getTotalProyectoTipos().toString()," }");
		}
		else if(accion.equals("guardarProyectotipo")){
			boolean result = false;
			boolean esnuevo = map.get("esnuevo").equals("true");
			int id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			if(id>0 || esnuevo){
				
				String nombre = map.get("nombre");
				String descripcion = map.get("descripcion");
				ProyectoTipo proyectoTipo;
				
				if(esnuevo){
					proyectoTipo = new ProyectoTipo(nombre, descripcion, 
							"admin",null,new DateTime().toDate(),null,1,null,new HashSet<PtipoPropiedad>(0));
				}
				else{
					proyectoTipo = ProyectoTipoDAO.getProyectoTipoPorId(id);
					proyectoTipo.setNombre(nombre);
					proyectoTipo.setDescripcion(descripcion);
					proyectoTipo.setUsuarioActualizo("admin");
					proyectoTipo.setFechaActualizacion(new DateTime().toDate());
					Set<PtipoPropiedad> propiedades_temp = proyectoTipo.getPtipoPropiedads();
					proyectoTipo.setPtipoPropiedads(null);
					if (propiedades_temp!=null){
						for (PtipoPropiedad ptipoPropiedad : propiedades_temp){
							PtipoPropiedadDAO.eliminarTotalPtipoPropiedad(ptipoPropiedad);
						}
					}
				}
				
				result = ProyectoTipoDAO.guardarProyectoTipo(proyectoTipo);
				
				String[] idsPropiedades =  map.get("propiedades") != null ? map.get("propiedades").toString().split(",") : null;
				if (idsPropiedades !=null && idsPropiedades.length>0){
					for (String idPropiedad : idsPropiedades){
						PtipoPropiedadId ptipoPropiedadId = new PtipoPropiedadId(proyectoTipo.getId(), Integer.parseInt(idPropiedad));
						ProyectoPropiedad proyectoPropiedad = new ProyectoPropiedad();
						proyectoPropiedad.setId(Integer.parseInt(idPropiedad));
						
						PtipoPropiedad ptipoPropiedad = new PtipoPropiedad(
								ptipoPropiedadId,
								proyectoPropiedad,
								proyectoTipo, 
								"admin", new DateTime().toDate());
						
						ptipoPropiedad.setProyectoTipo(proyectoTipo);
						if (proyectoTipo.getPtipoPropiedads() == null){
							proyectoTipo.setPtipoPropiedads(new HashSet<PtipoPropiedad>(0));
						}
						proyectoTipo.getPtipoPropiedads().add(ptipoPropiedad);
					}
				}
				
				result = ProyectoTipoDAO.guardarProyectoTipo(proyectoTipo);
				response_text = String.join("","{ \"success\": ",(result ? "true" : "false"),", "
						+ "\"id\": " + proyectoTipo.getId() +" }");
			}
			else
				response_text = "{ \"success\": false }";
		}
		else if(accion.equals("borrarProyectoTipo")){
			int id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			if(id>0){
				ProyectoTipo proyectoTipo = ProyectoTipoDAO.getProyectoTipoPorId(id);
				response_text = String.join("","{ \"success\": ",(ProyectoTipoDAO.eliminarProyectoTipo(proyectoTipo) ? "true" : "false")," }");
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
