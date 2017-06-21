package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import dao.PermisoDAO;
import pojo.Permiso;
import utilities.Utils;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;
/**
 * Servlet implementation class SPermiso
 */
@WebServlet("/SPermiso")
public class SPermiso extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	class stpermiso{
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
    public SPermiso() {
        super();
    }

	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setHeader("Content-Encoding", "gzip");
		response.setCharacterEncoding("UTF-8");
		OutputStream output = response.getOutputStream();
		GZIPOutputStream gz = new GZIPOutputStream(output);
		String response_text = "";
		Gson gson = new Gson();
		Type type = new TypeToken<Map<String, String>>(){}.getType();
		StringBuilder sb = new StringBuilder();
	    BufferedReader br = request.getReader();
	    String str;
	    while( (str = br.readLine()) != null ){
	        sb.append(str);
	    };
	    Map<String, String> map = gson.fromJson(sb.toString(), type);
		String action = "";
		action = map.get("accion");
		if(action!=null){
			if(action.compareTo("guardarPermiso")==0){
				String nombrePermiso = map.get("nombre");
				String descripcionPermiso = map.get("descripcion");
				boolean esnuevo = map.get("esnuevo").equals("true");
				Integer idPermiso = Integer.parseInt(map.get("id").toString());
				if(nombrePermiso!=null && descripcionPermiso!=null && esnuevo){
					HttpSession sesionweb = request.getSession();
					Permiso permiso = new Permiso();
					permiso.setId(idPermiso);
					permiso.setNombre(nombrePermiso);
					permiso.setDescripcion(descripcionPermiso);
					permiso.setUsuarioCreo(sesionweb.getAttribute("usuario").toString());
					permiso.setFechaCreacion(new DateTime().toDate());
					permiso.setEstado(1);
					response_text = String.join("","{ \"success\": ",(PermisoDAO.guardarPermiso(permiso) ? "true" : "false"),", "
							, "\"data\":",permiso.getId() + "", ","
							, "\"id\": " , permiso.getId()+ "" , ","
							, "\"usuarioCreo\": \"" , permiso.getUsuarioCreo(),"\","
							, "\"fechaCreacion\":\" " , Utils.formatDateHour(permiso.getFechaCreacion()),"\","
							, "\"usuarioactualizo\": \"" , permiso.getUsuarioActualizo() != null ? permiso.getUsuarioActualizo() : "","\","
							, "\"fechaactualizacion\": \"" , Utils.formatDateHour(permiso.getFechaActualizacion()),"\""
							," }");
				}else{
					if(nombrePermiso!=null && descripcionPermiso!=null && idPermiso !=null ){
						HttpSession sesionweb = request.getSession();
						Permiso permiso = PermisoDAO.getPermisoById(idPermiso);
						permiso.setDescripcion(descripcionPermiso);
						permiso.setNombre(nombrePermiso);
						permiso.setUsuarioActualizo(sesionweb.getAttribute("usuario").toString());
						permiso.setFechaActualizacion(new DateTime().toDate());
						response_text = String.join("","{ \"success\": ",(PermisoDAO.guardarPermiso(permiso) ? "true" : "false"),", "
							, "\"data\":",permiso.getId() + "", ","
							, "\"id\": " , permiso.getId() + "" , ","
							, "\"usuarioCreo\": \"" , permiso.getUsuarioCreo(),"\","
							, "\"fechaCreacion\":\" " , Utils.formatDateHour(permiso.getFechaCreacion()),"\","
							, "\"usuarioactualizo\": \"" , permiso.getUsuarioActualizo() != null ? permiso.getUsuarioActualizo() : "","\","
							, "\"fechaactualizacion\": \"" , Utils.formatDateHour(permiso.getFechaActualizacion()),"\""
							," }");
					}else{
						response_text = String.join("", "{\"success\":false, \"error\":\"parametro vacio\" }");
					}
				}
			}
			else if(action.compareTo("eliminarPermiso")==0){
				String idPermiso = map.get("id").toString();
				if(idPermiso!=null){
					HttpSession sesionweb = request.getSession();
					Permiso permiso = PermisoDAO.getPermisoById(Integer.parseInt(idPermiso));
					permiso.setUsuarioActualizo(sesionweb.getAttribute("usuario").toString());
					permiso.setFechaActualizacion(new DateTime().toDate());
					response_text = String.join("","{ \"success\": ",(PermisoDAO.eliminarPermiso(permiso) ? "true" : "false")," }");
				}else{
					response_text = String.join("", "{\"success\":false, \"error\":\"parametro vacio\" }");
				}
				
			}else if(action.compareTo("getPermisos")==0){
				List <Permiso> permisos = PermisoDAO.getPermisos();
				List <stpermiso> stpermisos = new  ArrayList <stpermiso>();
				for(Permiso permiso:permisos)
				{
					stpermiso tmp = new stpermiso();
					tmp.id = permiso.getId();
					tmp.nombre = permiso.getNombre();
					tmp.descripcion= permiso.getDescripcion();
					tmp.usuarioCreo=permiso.getUsuarioCreo();
					tmp.fechaCreacion= Utils.formatDateHour(permiso.getFechaCreacion());
					tmp.usuarioActualizo=permiso.getUsuarioActualizo();
					tmp.fechaActualizacion=Utils.formatDateHour( permiso.getFechaActualizacion());
					tmp.estado = permiso.getEstado();
					stpermisos.add(tmp);
				}
				response_text=new GsonBuilder().serializeNulls().create().toJson(stpermisos);
		        response_text = String.join("", "\"permisos\":",response_text);
		        response_text = String.join("", "{\"success\":true,", response_text,"}");
				
			}else if(action.compareTo("getPermisosPagina")==0){
				int pagina = map.get("pagina")!=null  ? Integer.parseInt(map.get("pagina")) : 0;
				int numeroPermisos = map.get("numeroPermisos")!=null  ? Integer.parseInt(map.get("numeroPermisos")) : 0;
				String filtro_id = map.get("filtro_id");
				String filtro_usuario_creo = map.get("filtro_usuario_creo");
				String filtro_fecha_creacion = map.get("filtro_fecha_creacion");
				String filtro_nombre= map.get("filtro_nombre");
				List <Permiso> permisos = PermisoDAO.getPermisosPagina(pagina, numeroPermisos, filtro_id,filtro_nombre,filtro_usuario_creo,filtro_fecha_creacion);
				List <stpermiso> stpermisos = new ArrayList <stpermiso>();
				for(Permiso permiso:permisos)
				{
					stpermiso tmp = new stpermiso();
					tmp.id = permiso.getId();
					tmp.nombre = permiso.getNombre();
					tmp.descripcion= permiso.getDescripcion();
					tmp.usuarioCreo=permiso.getUsuarioCreo();
					tmp.fechaCreacion=Utils.formatDateHour(permiso.getFechaCreacion());
					tmp.usuarioActualizo=permiso.getUsuarioActualizo();
					tmp.fechaActualizacion= Utils.formatDateHour(permiso.getFechaActualizacion());
					tmp.estado = permiso.getEstado();
					stpermisos.add(tmp);
				}
				response_text=new GsonBuilder().serializeNulls().create().toJson(stpermisos);
		        response_text = String.join("", "\"permisos\":",response_text);
		        response_text = String.join("", "{\"success\":true,", response_text,"}");
				
			}
			else if(action.compareTo("getTotalPermisos")==0){
				String filtro_id = map.get("filtro_id");
				String filtro_usuario_creo = map.get("filtro_usuario_creo");
				String filtro_fecha_creacion = map.get("filtro_fecha_creacion");
				String filtro_nombre= map.get("filtro_nombre");
				response_text = String.join("","{ \"success\": true, \"totalPermisos\":",PermisoDAO.getTotalPermisos(filtro_id,filtro_nombre,filtro_usuario_creo,filtro_fecha_creacion).toString()," }");
			}
		}else{
			response_text = String.join("", "{\"success\":false, \"error\":\"falta parametro de accion\" }");
		}
		
		gz.write(response_text.getBytes("UTF-8"));
        gz.close();
        output.close();
		
	}

}
