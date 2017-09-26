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

import dao.MetaTipoDAO;
import pojo.MetaTipo;
import utilities.Utils;


@WebServlet("/SMetaTipo")
public class SMetaTipo extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
	public class sttipometa{
		Integer id;
		String nombre;
		String descripcion;
		Integer estado;
		String fechaCreacion;
		String fechaActualizacion;
		String usuarioCreo;
		String usuarioActualizo;
	}
	
    public SMetaTipo() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("{ \"success\": false }").append(request.getContextPath());
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		Gson gson = new Gson();
		Type type = new TypeToken<Map<String, String>>(){}.getType();
		HttpSession sesionweb = request.getSession();
		String usuario = sesionweb.getAttribute("usuario")!= null ? sesionweb.getAttribute("usuario").toString() : null;
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
		if(accion.equals("getMetaTiposPagina")){
			int pagina = map.get("pagina")!=null  ? Integer.parseInt(map.get("pagina")) : 0;
			int numeroMetaTipos = map.get("numerometatipo")!=null  ? Integer.parseInt(map.get("numerometatipo")) : 0;
			String filtro_nombre = map.get("filtro_nombre");
			String filtro_usuario_creo = map.get("filtro_usuario_creo");
			String filtro_fecha_creacion = map.get("filtro_fecha_creacion");
			String columna_ordenada = map.get("columna_ordenada");
			String orden_direccion = map.get("orden_direccion");
			List<MetaTipo> MetaTipos = MetaTipoDAO.getMetaTiposPagina(pagina, numeroMetaTipos,filtro_nombre, filtro_usuario_creo, filtro_fecha_creacion, columna_ordenada, orden_direccion);
			List<sttipometa> sttipo = new ArrayList<sttipometa>();
			for(MetaTipo metatipo:MetaTipos){
				sttipometa temp = new sttipometa();
				temp.descripcion = metatipo.getDescripcion();
				temp.estado = metatipo.getEstado();
				temp.fechaActualizacion = Utils.formatDateHour(metatipo.getFechaActualizacion());
				temp.fechaCreacion = Utils.formatDateHour(metatipo.getFechaCreacion());
				temp.id = metatipo.getId();
				temp.nombre = metatipo.getNombre();
				temp.usuarioActualizo = metatipo.getUsuarioActualizo();
				temp.usuarioCreo = metatipo.getUsuarioCreo();
				sttipo.add(temp);
			}
			response_text=new GsonBuilder().serializeNulls().create().toJson(sttipo);
	        response_text = String.join("", "\"MetaTipos\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(accion.equals("getMetaTipos")){
			List<MetaTipo> MetaTipos = MetaTipoDAO.getMetaTipos();
			List<sttipometa> sttipo = new ArrayList<sttipometa>();
			for(MetaTipo metatipo:MetaTipos){
				sttipometa temp = new sttipometa();
				temp.descripcion = metatipo.getDescripcion();
				temp.estado = metatipo.getEstado();
				temp.fechaActualizacion = Utils.formatDateHour(metatipo.getFechaActualizacion());
				temp.fechaCreacion = Utils.formatDateHour(metatipo.getFechaCreacion());
				temp.id = metatipo.getId();
				temp.nombre = metatipo.getNombre();
				temp.usuarioActualizo = metatipo.getUsuarioActualizo();
				temp.usuarioCreo = metatipo.getUsuarioCreo();
				sttipo.add(temp);
			}
			response_text=new GsonBuilder().serializeNulls().create().toJson(sttipo);
	        response_text = String.join("", "\"MetaTipos\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(accion.equals("guardarMetaTipo")){
			boolean result = false;
			boolean esnuevo = map.get("esnueva")!=null ? map.get("esnueva").equals("true") :  false;
			int id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			if(id>0 || esnuevo){
				String nombre = map.get("nombre");
				String descripcion = map.get("descripcion");
				MetaTipo MetaTipo;
				if(esnuevo){
					MetaTipo = new MetaTipo(nombre, descripcion,usuario,null, new DateTime().toDate(), null, 1);
				}
				else{
					MetaTipo = MetaTipoDAO.getMetaTipoPorId(id);
					MetaTipo.setNombre(nombre);
					MetaTipo.setDescripcion(descripcion);
					MetaTipo.setUsuarioActualizo("admin");
					MetaTipo.setFechaActualizacion(new DateTime().toDate());
				}
				result = MetaTipoDAO.guardarMetaTipo(MetaTipo);
				response_text = String.join("","{ \"success\": ",(result ? "true" : "false"),", "
						, "\"id\": " , MetaTipo.getId().toString() , ","
						, "\"usuarioCreo\": \"" , MetaTipo.getUsuarioCreo(),"\","
						, "\"fechaCreacion\":\" " , Utils.formatDateHour(MetaTipo.getFechaCreacion()),"\","
						, "\"usuarioactualizo\": \"" , MetaTipo.getUsuarioActualizo() != null ? MetaTipo.getUsuarioActualizo() : "","\","
						, "\"fechaactualizacion\": \"" , Utils.formatDateHour(MetaTipo.getFechaActualizacion()),"\""
						," }");
			}
			else
				response_text = "{ \"success\": false }";
		}
		else if(accion.equals("borrarMetaTipo")){
			int id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			if(id>0){
				MetaTipo MetaTipo = MetaTipoDAO.getMetaTipoPorId(id);
				MetaTipo.setUsuarioActualizo(usuario);
				MetaTipo.setFechaActualizacion(new DateTime().toDate());
				response_text = String.join("","{ \"success\": ",(MetaTipoDAO.eliminarMetaTipo(MetaTipo) ? "true" : "false")," }");
			}
			else
				response_text = "{ \"success\": false }";
		}
		else if(accion.equals("numeroMetaTipos")){
			String filtro_nombre = map.get("filtro_nombre");
			String filtro_usuario_creo = map.get("filtro_usuario_creo");
			String filtro_fecha_creacion = map.get("filtro_fecha_creacion");
			response_text = String.join("","{ \"success\": true, \"totalMetaTipos\":",MetaTipoDAO.getTotalMetaTipos(filtro_nombre, filtro_usuario_creo, filtro_fecha_creacion).toString()," }");
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
