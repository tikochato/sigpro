package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import dao.RolDAO;
import pojo.Rol;
import pojo.RolPermiso;

/**
 * Servlet implementation class SRol
 */
@WebServlet("/SRol")
public class SRol extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	class strol{
		Integer id;
		String nombre;
		String descripcion;
	}
	class stpermiso{
		Integer id;
		String nombre;
		String descripcion;
	}
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SRol() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
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
		String accion = "";
		accion = map.get("accion");
		if(accion!=null){
			if(accion.compareTo("getRoles")==0){
				List <Rol> roles = RolDAO.getRoles();
				List <strol> stroles= new ArrayList <strol>();
				for(Rol role :roles ){
					strol stRol = new strol();
					stRol.id= role.getId();
					stRol.nombre=role.getNombre();
					stRol.descripcion=role.getDescripcion();
					stroles.add(stRol);
				}
				String respuesta = new GsonBuilder().serializeNulls().create().toJson(stroles);
				response_text = String.join("", "\"roles\": ",respuesta);
				response_text = String.join("", "{\"success\":true,", response_text,"}");
			}else if(accion.compareTo("getPermisosPorRol")==0){
				int rolid= Integer.parseInt(map.get("id"));
				List<RolPermiso> rolpermisos= RolDAO.getPermisosPorRol(rolid);
				List<stpermiso> stpermisos= new ArrayList <stpermiso>();
				for(RolPermiso rolpermiso:rolpermisos){
					stpermiso stPermiso = new stpermiso();
					stPermiso.id = rolpermiso.getId().getPermisoid();
					stPermiso.nombre=rolpermiso.getPermiso().getNombre();
					stPermiso.descripcion=rolpermiso.getPermiso().getDescripcion();
					stpermisos.add(stPermiso);
				}
				String respuesta = new GsonBuilder().serializeNulls().create().toJson(stpermisos);
				response_text = String.join("", "\"permisos\": ",respuesta);
				response_text = String.join("", "{\"success\":true,", response_text,"}");
				
			}
		}
		gz.write(response_text.getBytes("UTF-8"));
	    gz.close();
	    output.close();
		
	}

}
