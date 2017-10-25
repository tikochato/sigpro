package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

import dao.RolUnidadEjecutoraDAO;
import pojo.RolUnidadEjecutora;
import utilities.Utils;

@WebServlet("/SRolUnidadEjecutora")
public class SRolUnidadEjecutora extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	public class strolunidadejecutora{
		Integer id;
		String nombre;
		Integer estado;
		Integer rolPredeterminado;
		String fechaCreacion;
		String fechaActualizacion;
		String usuarioCreo;
		String usuarioActualizo;
	}
    
    public SRolUnidadEjecutora() {
        super();
    
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		request.setCharacterEncoding("UTF-8");
		Gson gson = new Gson();
		Type type = new TypeToken<Map<String, String>>(){}.getType();
		StringBuilder sb = new StringBuilder();
		BufferedReader br = request.getReader();
		HttpSession sesionweb = request.getSession();
		String usuario = sesionweb.getAttribute("usuario")!= null ? sesionweb.getAttribute("usuario").toString() : null;
		String str;
		while ((str = br.readLine()) != null) {
			sb.append(str);
		}
		;
		Map<String, String> map = gson.fromJson(sb.toString(), type);
		String accion = map.get("accion");
		String response_text="";
		if(accion.equals("getRolesUnidadEjecutora")){
			int pagina = map.get("pagina")!=null  ? Integer.parseInt(map.get("pagina")) : 0;
			int numeroRolUnidadEjecutora = map.get("numerorolunidadejecutora")!=null  ? Integer.parseInt(map.get("numerorolunidadejecutora")) : 0;
			String filtro_nombre = map.get("filtro_nombre");
			String filtro_usuario_creo = map.get("filtro_usuario_creo");
			String filtro_fecha_creacion = map.get("filtro_fecha_creacion");
			String columna_ordenada = map.get("columna_ordenada");
			String orden_direccion = map.get("orden_direccion");
			List<RolUnidadEjecutora> RolesUnidadEjecutora = RolUnidadEjecutoraDAO.getRolesUnidadEjecutoraPagina(pagina, numeroRolUnidadEjecutora,
					filtro_nombre, filtro_usuario_creo, filtro_fecha_creacion, columna_ordenada, orden_direccion);
			
			List<strolunidadejecutora> stunidad = new ArrayList<strolunidadejecutora>();
			for(RolUnidadEjecutora rol:RolesUnidadEjecutora){
				strolunidadejecutora temp = new strolunidadejecutora();
				temp.estado = rol.getEstado();
				temp.fechaActualizacion = Utils.formatDateHour(rol.getFechaActualizacion());
				temp.fechaCreacion = Utils.formatDateHour(rol.getFechaCreacion());
				temp.id = rol.getId();
				temp.nombre = rol.getNombre();
				temp.usuarioActualizo = rol.getUsuarioActualizo();
				temp.usuarioCreo = rol.getUsuarioCreo();
				temp.rolPredeterminado = rol.getRolPredeterminado();
				stunidad.add(temp);
			}
			response_text=new GsonBuilder().serializeNulls().create().toJson(stunidad);
			response_text = String.join("", "\"rolesUnidadEjecutora\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(accion.equals("guardar")){
			boolean result = false;
			boolean esnuevo = map.get("esnueva")!=null ? map.get("esnueva").equals("true") :  false;
			int id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			int rolPredeterminado = Utils.String2Int(map.get("rolPredeterminado"), 0);
			if(id>0 || esnuevo){
				String nombre = map.get("nombre");
				
				RolUnidadEjecutora rolUnidadEjecutora;
				if(esnuevo){
					rolUnidadEjecutora = new RolUnidadEjecutora(nombre, 1, usuario, null, 
							new Date(), null, rolPredeterminado, null);
				}
				else{
					rolUnidadEjecutora = RolUnidadEjecutoraDAO.getRolUnidadEjecutoraPorId(id);
					rolUnidadEjecutora.setNombre(nombre);
					rolUnidadEjecutora.setUsuarioActualizo(usuario);
					rolUnidadEjecutora.setFechaActualizacion(new Date());
					rolUnidadEjecutora.setRolPredeterminado(rolPredeterminado);
				}
				result = RolUnidadEjecutoraDAO.guardarRolUnidadEjecutora(rolUnidadEjecutora);
				response_text = String.join("","{ \"success\": ",(result ? "true" : "false"),", "
						, "\"id\": " , rolUnidadEjecutora.getId().toString() , ","
						, "\"usuarioCreo\": \"" , rolUnidadEjecutora.getUsuarioCreo(),"\","
						, "\"fechaCreacion\":\" " , Utils.formatDateHour(rolUnidadEjecutora.getFechaCreacion()),"\","
						, "\"usuarioactualizo\": \"" , rolUnidadEjecutora.getUsuarioActualizo() != null ? rolUnidadEjecutora.getUsuarioActualizo() : "","\","
						, "\"fechaactualizacion\": \"" , Utils.formatDateHour(rolUnidadEjecutora.getFechaActualizacion()),"\""
						," }");
			}
			else
				response_text = "{ \"success\": false }";
		}else if(accion.equals("borrar")){
			int id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			if(id>0){
				RolUnidadEjecutora rolUnidadEjecutora = RolUnidadEjecutoraDAO.getRolUnidadEjecutoraPorId(id);
				rolUnidadEjecutora.setUsuarioActualizo(usuario);
				rolUnidadEjecutora.setFechaActualizacion(new Date());
				response_text = String.join("","{ \"success\": ",(RolUnidadEjecutoraDAO.eliminarRolUnidadEjecutora(rolUnidadEjecutora) ? "true" : "false")," }");
			}
			else
				response_text = "{ \"success\": false }";
		}else if(accion.equals("numeroRolesUnidadEjecutora")){
			String filtro_nombre = map.get("filtro_nombre");
			String filtro_usuario_creo = map.get("filtro_usuario_creo");
			String filtro_fecha_creacion = map.get("filtro_fecha_creacion");
			response_text = String.join("","{ \"success\": true, \"totalrolesunidadejecutora\":",RolUnidadEjecutoraDAO.getTotalRolesUnidadEjecutora(filtro_nombre, filtro_usuario_creo, filtro_fecha_creacion).toString()," }");
		}
		else{
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
