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
import com.google.gson.reflect.TypeToken;

import dao.PermisoDAO;
import pojo.Permiso;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Date;
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
		Date fechaCreacion;
		Date fechaActualizacion;
		int estado;
	}
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SPermiso() {
        super();
        // TODO Auto-generated constructor stub
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
		action = map.get("action");
		if(action!=null){
			if(action.compareTo("guardarPermiso")==0){
				String nombrePermiso = map.get("nombre");
				String descripcionPermiso = map.get("descripcion");
				if(nombrePermiso!=null && descripcionPermiso!=null){
					HttpSession sesionweb = request.getSession();
					Permiso permiso = new Permiso(nombrePermiso,descripcionPermiso,sesionweb.getAttribute("usuario").toString(),new DateTime().toDate(),1);
					response_text = String.join("","{ \"success\": ",(PermisoDAO.guardarPermiso(permiso) ? "true" : "false")," }");
				}else{
					response_text = String.join("", "{\"success\":false, \"error\":\"parametro vacio\" }");
				}
			}else if(action.compareTo("editarPermiso")==0){
				String nombrePermiso = map.get("nombre");
				String descripcionPermiso = map.get("descripcion");
				Integer idPermiso = Integer.parseInt(map.get("id").toString());
				if(nombrePermiso!=null && descripcionPermiso!=null && idPermiso !=null ){
					HttpSession sesionweb = request.getSession();
					Permiso permiso = PermisoDAO.getPermiso(nombrePermiso);
					permiso.setDescripcion(descripcionPermiso);
					permiso.setUsuarioActualizo(sesionweb.getAttribute("usuario").toString());
					permiso.setFechaActualizacion(new DateTime().toDate());
					response_text = String.join("","{ \"success\": ",(PermisoDAO.guardarPermiso(permiso) ? "true" : "false")," }");
				}else{
					response_text = String.join("", "{\"success\":false, \"error\":\"parametro vacio\" }");
				}
				
			}
			else if(action.compareTo("eliminarPermiso")==0){
				String idPermiso = map.get("id").toString();
				if(idPermiso!=null){
					Permiso permiso = PermisoDAO.getPermisoById(Integer.parseInt(idPermiso));
					response_text = String.join("","{ \"success\": ",(PermisoDAO.eliminarPermiso(permiso) ? "true" : "false")," }");
				}else{
					response_text = String.join("", "{\"success\":false, \"error\":\"parametro vacio\" }");
				}
				
			}else if(action.compareTo("getPermisos")==0){
				List <Permiso> permisos = PermisoDAO.getPermisos();
				List <stpermiso> stpermisos = new <stpermiso> ArrayList();
				
			}else if(action.compareTo("getPermiso")==0){
				
			}
		}else{
			response_text = String.join("", "{\"success\":false, \"error\":\"falta parametro de accion\" }");
		}
		
		gz.write(response_text.getBytes("UTF-8"));
        gz.close();
        output.close();
		
	}

}
