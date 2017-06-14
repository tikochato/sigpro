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

import dao.ResponsableRolDAO;
import pojo.ResponsableRol;

import pojo.ResponsableTipo;
import utilities.Utils;


@WebServlet("/SResponsableRol")
public class SResponsableRol extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    
	class stResposableRol {
		int id;
		int responsableTipoId;
		String responsableTipoNombre;
		String nombre;
		String descripcion;
		String usuarioCreo;
		String usuarioActualizo;
		String fechaCreacion;
		String fechaActualizacion;
		int estado;
	}
       
    

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		response.getWriter().append("{ \"success\": false }").append(request.getContextPath());
	}

	
	
	
    public SResponsableRol() {
        super();
    }

	

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String response_text = "";
		
		try{
			request.setCharacterEncoding("UTF-8");
			Gson gson = new Gson();
			Type type = new TypeToken<Map<String, String>>(){}.getType();
			StringBuilder sb = new StringBuilder();
			BufferedReader br = request.getReader();
			String str;
			HttpSession sesionweb = request.getSession();
			String usuario = sesionweb.getAttribute("usuario")!= null ? sesionweb.getAttribute("usuario").toString() : null;
			
			while ((str = br.readLine()) != null) {
				sb.append(str);
			}
			Map<String, String> map = gson.fromJson(sb.toString(), type);
			String accion = map.get("accion")!=null ? map.get("accion") : "";
			
			if (accion.equals("getResponsableRol")){
				int pagina = map.get("pagina")!=null  ? Integer.parseInt(map.get("pagina")) : 0;
				int numeroResponsableRol = map.get("numeroresponsablerol")!=null  ? Integer.parseInt(map.get("numeroresponsablerol")) : 0;
				List<ResponsableRol> responsablesRol = ResponsableRolDAO.getResponsableTiposPagina(pagina, numeroResponsableRol);
				
				List<stResposableRol> stresponsablerol=new ArrayList<stResposableRol>();
				for(ResponsableRol responsableRol:responsablesRol){
					stResposableRol temp =new stResposableRol();
					temp.id = responsableRol.getId();
					temp.nombre = responsableRol.getNombre();
					stresponsablerol.add(temp);
				}
				
				response_text=new GsonBuilder().serializeNulls().create().toJson(stresponsablerol);
		        response_text = String.join("", "\"resposablerol\":",response_text);
		        response_text = String.join("", "{\"success\":true,", response_text,"}");
			}else if (accion.equals("numeroResponsableRol")){
				response_text = String.join("","{ \"success\": true, \"totalresponsablerol\":",ResponsableRolDAO.getTotalResponsableRol().toString()," }");
			}else if(accion.equals("numeroResponsableRolFiltro")){
				String filtro_nombre = map.get("filtro_nombre");
				String filtro_descripcion = map.get("filtro_descripcion");
				String filtro_usuario_creo = map.get("filtro_usuario_creo");
				String filtro_fecha_creacion = map.get("filtro_fecha_creacion");
				response_text = String.join("","{ \"success\": true, \"totalResposnablesRoles\":",ResponsableRolDAO.getTotalResponsablesRoles(filtro_nombre, filtro_descripcion,filtro_usuario_creo, filtro_fecha_creacion).toString()," }");
			}else if(accion.equals("getResponsableRolPaginafiltro")){
				int pagina = map.get("pagina")!=null  ? Integer.parseInt(map.get("pagina")) : 0;
				int numeroresponsabletipo = map.get("numeroresponsabletipo")!=null  ? Integer.parseInt(map.get("numeroresponsabletipo")) : 0;
				String filtro_nombre = map.get("filtro_nombre");
				String filtro_descripcion = map.get("filtro_descripcion");
				String filtro_usuario_creo = map.get("filtro_usuario_creo");
				String filtro_fecha_creacion = map.get("filtro_fecha_creacion");
				String columna_ordenada = map.get("columna_ordenada");
				String orden_direccion = map.get("orden_direccion");
				List<ResponsableRol> responsableRoles = ResponsableRolDAO.getResponsableRolPagina(pagina, numeroresponsabletipo,
						filtro_nombre, filtro_descripcion, filtro_usuario_creo, filtro_fecha_creacion, columna_ordenada, orden_direccion);
				List<stResposableRol> datos_=new ArrayList<stResposableRol>();
				for (ResponsableRol responsableRol : responsableRoles){
					stResposableRol dato = new stResposableRol();
					dato.id = responsableRol.getId();
					dato.responsableTipoId = responsableRol.getResponsableTipo().getId();
					dato.responsableTipoNombre = responsableRol.getResponsableTipo().getNombre();
					dato.nombre = responsableRol.getNombre();
					dato.descripcion = responsableRol.getDescripcion();
					dato.fechaCreacion = Utils.formatDateHour(responsableRol.getFechaCreacion());
					dato.usuarioCreo = responsableRol.getUsuarioCreo();
					dato.fechaActualizacion = Utils.formatDateHour(responsableRol.getFechaActualizacion());
					dato.usuarioActualizo = responsableRol.getUsuarioActualizo();
					dato.estado = responsableRol.getEstado();
					datos_.add(dato);
				}
				response_text=new GsonBuilder().serializeNulls().create().toJson(datos_);
		        response_text = String.join("", "\"responsablesroles\":",response_text);
		        response_text = String.join("", "{\"success\":true,", response_text,"}");
			}else if(accion.equals("guardar")){
				try{
					boolean result = false;
					boolean esnuevo = map.get("esnuevo").equals("true");
					int id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
					ResponsableRol responsableRol;
					if(id>0 || esnuevo){
						String nombre = map.get("nombre");
						String descripcion = map.get("descripcion");
						Integer responsableTipoId = Utils.String2Int(map.get("responsableTipo"));
						
						ResponsableTipo responsableTipo = null;
						if(responsableTipoId != 0)
						{
							responsableTipo = new ResponsableTipo();
							responsableTipo.setId(responsableTipoId);
						}
						
						if(esnuevo){
							responsableRol = new ResponsableRol(responsableTipo, nombre, descripcion, usuario, null, new DateTime().toDate(), null, 1, null);
						}else{
							responsableRol = ResponsableRolDAO.ResponsableRol(id);
							responsableRol.setNombre(nombre);
							responsableRol.setDescripcion(descripcion);
							responsableRol.setUsuarioActualizo(usuario);
							responsableRol.setFechaActualizacion(new DateTime().toDate());
							responsableRol.setResponsableTipo(responsableTipo);
						}
						
						result = ResponsableRolDAO.guardarResponsableRol(responsableRol);
						
						if (result){
							response_text = String.join("","{ \"success\": ",(result ? "true" : "false"),", "
									, "\"id\": " , responsableRol.getId().toString() , ","
									, "\"usuarioCreo\": \"" , responsableRol.getUsuarioCreo(),"\","
									, "\"fechaCreacion\":\" " , Utils.formatDateHour(responsableRol.getFechaCreacion()),"\","
									, "\"usuarioActualizo\": \"" , responsableRol.getUsuarioActualizo() != null ? responsableRol.getUsuarioActualizo() : "","\","
									, "\"fechaActualizacion\": \"" , Utils.formatDateHour(responsableRol.getFechaActualizacion()),"\""
									," }");
						}
						
					}else
						response_text = "{ \"success\": false }";
				}catch (Throwable e) {
					response_text = "{ \"success\": false }";					
				}
			}else if(accion.equals("borrarResponsableRol")){
				int id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
				if(id>0){
					ResponsableRol responsableRol = ResponsableRolDAO.ResponsableRol(id);
					response_text = String.join("","{ \"success\": ",(ResponsableRolDAO.eliminarResponsableRol(responsableRol) ? "true" : "false")," }");
				}
				else
					response_text = "{ \"success\": false }";
			} else if(accion.equals("getResponsableRoles")){
				List<ResponsableRol> responsableRoles = ResponsableRolDAO.getResponsableRol(1);
				List<stResposableRol> stResponsableRoles = new ArrayList<>();
				for (ResponsableRol responsableRol : responsableRoles){
					stResposableRol temp = new stResposableRol();
					temp.id = responsableRol.getId();
					temp.nombre = responsableRol.getNombre();
					temp.descripcion = responsableRol.getDescripcion();
					temp.responsableTipoId = responsableRol.getResponsableTipo().getId();
					temp.responsableTipoNombre = responsableRol.getResponsableTipo().getNombre();
					
					stResponsableRoles.add(temp);
				}
				
				response_text=new GsonBuilder().serializeNulls().create().toJson(stResponsableRoles);
		        response_text = String.join("", "\"responsableroles\":",response_text);
		        response_text = String.join("", "{\"success\":true,", response_text,"}");

			}
				
		}
		catch (Exception ex){
			response_text = String.join("","{ \"success\": false }");
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
