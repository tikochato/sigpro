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


import dao.SubComponenteTipoDAO;
import dao.CtipoPropiedadDAO;
import pojo.ComponentePropiedad;
import pojo.ComponenteTipo;
import pojo.CtipoPropiedad;
import pojo.CtipoPropiedadId;
import utilities.Utils;

/**
 * Servlet implementation class SComponenteTipo
 */
@WebServlet("/SSubComponenteTipo")
public class SSubComponenteTipo extends HttpServlet {
	private static final long serialVersionUID = 1L;
	class stcomponentetipo{
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
		if(accion.equals("getComponentetiposPagina")){
			int pagina = map.get("pagina")!=null  ? Integer.parseInt(map.get("pagina")) : 0;
			int numeroComponenteTipo = map.get("numerocomponentetipos")!=null  ? Integer.parseInt(map.get("numerocomponentetipos")) : 0;
			String filtro_nombre = map.get("filtro_nombre");
			String filtro_usuario_creo = map.get("filtro_usuario_creo");
			String filtro_fecha_creacion = map.get("filtro_fecha_creacion");
			String columna_ordenada = map.get("columna_ordenada");
			String orden_direccion = map.get("orden_direccion");
			List<ComponenteTipo> componentetipos = SubComponenteTipoDAO.getComponenteTiposPagina(pagina, numeroComponenteTipo
					,filtro_nombre,filtro_usuario_creo,filtro_fecha_creacion,columna_ordenada,orden_direccion);
			List<stcomponentetipo> stcomponentetipos=new ArrayList<stcomponentetipo>();
			for(ComponenteTipo componentetipo:componentetipos){
				stcomponentetipo temp =new stcomponentetipo();
				temp.descripcion = componentetipo.getDescripcion();
				temp.estado = componentetipo.getEstado();
				temp.fechaActualizacion = Utils.formatDateHour(componentetipo.getFechaActualizacion());
				temp.fechaCreacion = Utils.formatDateHour(componentetipo.getFechaCreacion());
				temp.id = componentetipo.getId();
				temp.nombre = componentetipo.getNombre();
				temp.usuarioActualizo = componentetipo.getUsuarioActualizo();
				temp.usuarioCreo = componentetipo.getUsuarioCreo();
				stcomponentetipos.add(temp);
			}
			
			response_text=new GsonBuilder().serializeNulls().create().toJson(stcomponentetipos);
	        response_text = String.join("", "\"componentetipos\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		
		else if(accion.equals("numeroComponenteTipos")){
			String filtro_nombre = map.get("filtro_nombre");
			String filtro_usuario_creo = map.get("filtro_usuario_creo");
			String filtro_fecha_creacion = map.get("filtro_fecha_creacion");
			
			response_text = String.join("","{ \"success\": true, \"totalcomponentetipos\":",SubComponenteTipoDAO
					.getTotalComponenteTipo(filtro_nombre,filtro_usuario_creo,filtro_fecha_creacion).toString()," }");
		}
		else if(accion.equals("guardarComponentetipo")){
			boolean result = false;
			boolean esnuevo = map.get("esnuevo").equals("true");
			int id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			if(id>0 || esnuevo){
				
				String nombre = map.get("nombre");
				String descripcion = map.get("descripcion");
				ComponenteTipo componenteTipo;
				
				if(esnuevo){
					componenteTipo = new ComponenteTipo(nombre, usuario, new DateTime().toDate(), 1);
				}
				else{
					componenteTipo = SubComponenteTipoDAO.getSubComponenteTipoPorId(id);
					componenteTipo.setNombre(nombre);
					componenteTipo.setDescripcion(descripcion);
					componenteTipo.setUsuarioActualizo(usuario);
					componenteTipo.setFechaActualizacion(new DateTime().toDate());
					Set<CtipoPropiedad> propiedades_temp = componenteTipo.getCtipoPropiedads();
					componenteTipo.setCtipoPropiedads(null);
					if (propiedades_temp!=null){
						for (CtipoPropiedad ctipoPropiedad : propiedades_temp){
							CtipoPropiedadDAO.eliminarTotalCtipoPropiedad(ctipoPropiedad);
						}
					}
				}
				
				result = SubComponenteTipoDAO.guardarSubComponenteTipo(componenteTipo);
				
				String[] idsPropiedades =  map.get("propiedades") != null ? map.get("propiedades").toString().split(",") : null;
				if (idsPropiedades !=null && idsPropiedades.length>0){
					
					for (String idPropiedad : idsPropiedades){
						CtipoPropiedadId ctipoPropiedadId = new CtipoPropiedadId(componenteTipo.getId(), Integer.parseInt(idPropiedad));
						ComponentePropiedad componentePropiedad = new ComponentePropiedad();
						componentePropiedad.setId(Integer.parseInt(idPropiedad));
						
						CtipoPropiedad ctipoPropiedad = new CtipoPropiedad(
								ctipoPropiedadId, componentePropiedad, 
								componenteTipo, usuario, new DateTime().toDate());
						
						ctipoPropiedad.setComponenteTipo(componenteTipo);
						if (componenteTipo.getCtipoPropiedads() == null){
							componenteTipo.setCtipoPropiedads(new HashSet<CtipoPropiedad>(0));
						}
						componenteTipo.getCtipoPropiedads().add(ctipoPropiedad);
					}
				}
				
				result = SubComponenteTipoDAO.guardarSubComponenteTipo(componenteTipo);
				response_text = String.join("","{ \"success\": ",(result ? "true" : "false"),", "
						+ "\"id\": " + componenteTipo.getId() , ","
						, "\"usuarioCreo\": \"" , componenteTipo.getUsuarioCreo(),"\","
						, "\"fechaCreacion\":\" " , Utils.formatDateHour(componenteTipo.getFechaCreacion()),"\","
						, "\"usuarioactualizo\": \"" , componenteTipo.getUsuarioActualizo() != null ? componenteTipo.getUsuarioActualizo() : "","\","
						, "\"fechaactualizacion\": \"" , Utils.formatDateHour(componenteTipo.getFechaActualizacion()),"\""+
						" }");
			}
			else
				response_text = "{ \"success\": false }";
		}
		else if(accion.equals("borrarComponenteTipo")){
			int id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			if(id>0){
				ComponenteTipo componenteTipo = SubComponenteTipoDAO.getSubComponenteTipoPorId(id);
				response_text = String.join("","{ \"success\": ",(SubComponenteTipoDAO.eliminarComponenteTipo(componenteTipo) ? "true" : "false")," }");
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
