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

import dao.ResponsableTipoDAO;
import pojo.ResponsableTipo;
import utilities.Utils;

@WebServlet("/SResponsableTipo")
public class SResponsableTipo extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    class stResponsableTipo{
    	int id;
    	String nombre;
    	String descripcion;
    	String usuarioCreo;
    	String usuarioActualizo;
    	String fechaCreacion;
    	String fechaActualizacion;
    	int estado;
    }
	
	public SResponsableTipo() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String response_text = "";
		HttpSession sesionweb = request.getSession();
		String usuario = sesionweb.getAttribute("usuario")!= null ? sesionweb.getAttribute("usuario").toString() : null;

		try{
			request.setCharacterEncoding("UTF-8");
			Gson gson = new Gson();
			Type type = new TypeToken<Map<String, String>>(){}.getType();
			StringBuilder sb = new StringBuilder();
			BufferedReader br = request.getReader();
			String str;
			while ((str = br.readLine()) != null) {
				sb.append(str);
			}
			Map<String, String> map = gson.fromJson(sb.toString(), type);
			String accion = map.get("accion")!=null ? map.get("accion") : "";
			
			if (accion.equals("getResponsableTipoPagina")) {
				int pagina = map.get("pagina")!=null  ? Integer.parseInt(map.get("pagina")) : 0;
				int numeroResponsableTipo = map.get("numeroResponsableTipo")!=null  ? Integer.parseInt(map.get("numeroResponsableTipo")) : 0;
				List<ResponsableTipo> responsabletipo = ResponsableTipoDAO.getResponsableTiposPagina(pagina, numeroResponsableTipo);
				
				List<stResponsableTipo> stresponsabletipo=new ArrayList<stResponsableTipo>();
				for(ResponsableTipo responsableTipo:responsabletipo){
					stResponsableTipo temp =new stResponsableTipo();
					temp.id = responsableTipo.getId();
					temp.nombre = responsableTipo.getNombre();
					stresponsabletipo.add(temp);
				}
				
				response_text=new GsonBuilder().serializeNulls().create().toJson(stresponsabletipo);
		        response_text = String.join("", "\"responsableTipo\":",response_text);
		        response_text = String.join("", "{\"success\":true,", response_text,"}");
			}else if (accion.equals("numeroResponsableTipo")){
				response_text = String.join("","{ \"success\": true, \"totalactividadtipos\":",ResponsableTipoDAO.getTotalResponsableTipo().toString()," }");
			}else if(accion.equals("numeroResponsableTipoFiltro")){
				String filtro_nombre = map.get("filtro_nombre");
				String filtro_descripcion = map.get("filtro_descripcion");
				String filtro_usuario_creo = map.get("filtro_usuario_creo");
				String filtro_fecha_creacion = map.get("filtro_fecha_creacion");
				response_text = String.join("","{ \"success\": true, \"totalResposnablesTipos\":",ResponsableTipoDAO.getTotalResponsablesTipos(filtro_nombre, filtro_descripcion,filtro_usuario_creo, filtro_fecha_creacion).toString()," }");
			}else if(accion.equals("getResponsableTipoPaginafiltro")){
				int pagina = map.get("pagina")!=null  ? Integer.parseInt(map.get("pagina")) : 0;
				int numeroresponsabletipo = map.get("numeroresponsabletipo")!=null  ? Integer.parseInt(map.get("numeroresponsabletipo")) : 0;
				String filtro_nombre = map.get("filtro_nombre");
				String filtro_descripcion = map.get("filtro_descripcion");
				String filtro_usuario_creo = map.get("filtro_usuario_creo");
				String filtro_fecha_creacion = map.get("filtro_fecha_creacion");
				String columna_ordenada = map.get("columna_ordenada");
				String orden_direccion = map.get("orden_direccion");
				List<ResponsableTipo> responsableTipos = ResponsableTipoDAO.getResponsableTipoPagina(pagina, numeroresponsabletipo,
						filtro_nombre, filtro_descripcion, filtro_usuario_creo, filtro_fecha_creacion, columna_ordenada, orden_direccion);
				List<stResponsableTipo> datos_=new ArrayList<stResponsableTipo>();
				for (ResponsableTipo responsableTipo : responsableTipos){
					stResponsableTipo dato = new stResponsableTipo();
					dato.id = responsableTipo.getId();
					dato.nombre = responsableTipo.getNombre();
					dato.descripcion = responsableTipo.getDescripcion();
					dato.fechaCreacion = Utils.formatDateHour(responsableTipo.getFechaCreacion());
					dato.usuarioCreo = responsableTipo.getUsuarioCreo();
					dato.fechaActualizacion = Utils.formatDateHour(responsableTipo.getFechaActualizacion());
					dato.usuarioActualizo = responsableTipo.getUsuarioActualizo();
					dato.estado = responsableTipo.getEstado();
					datos_.add(dato);
				}
				response_text=new GsonBuilder().serializeNulls().create().toJson(datos_);
		        response_text = String.join("", "\"responsablestipos\":",response_text);
		        response_text = String.join("", "{\"success\":true,", response_text,"}");
			}else if(accion.equals("guardar")){
				try{
					boolean result = false;
					boolean esnuevo = map.get("esnuevo").equals("true");
					int id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
					ResponsableTipo responsableTipo;
					if(id>0 || esnuevo){
						String nombre = map.get("nombre");
						String descripcion = map.get("descripcion");
						
						if(esnuevo){
							responsableTipo = new ResponsableTipo(nombre,descripcion,usuario
									,null, new DateTime().toDate(),null,1,null);
						}else{
							responsableTipo = ResponsableTipoDAO.ResponsableTipo(id);
							responsableTipo.setNombre(nombre);
							responsableTipo.setDescripcion(descripcion);
							responsableTipo.setUsuarioActualizo(usuario);
							responsableTipo.setFechaActualizacion(new DateTime().toDate());
						}
						
						result = ResponsableTipoDAO.guardarResponsableTipo(responsableTipo);
						
						if (result){
							response_text = String.join("","{ \"success\": ",(result ? "true" : "false"),", "
									, "\"id\": " , responsableTipo.getId().toString() , ","
									, "\"usuarioCreo\": \"" , responsableTipo.getUsuarioCreo(),"\","
									, "\"fechaCreacion\":\" " , Utils.formatDateHour(responsableTipo.getFechaCreacion()),"\","
									, "\"usuarioActualizo\": \"" , responsableTipo.getUsuarioActualizo() != null ? responsableTipo.getUsuarioActualizo() : "","\","
									, "\"fechaActualizacion\": \"" , Utils.formatDateHour(responsableTipo.getFechaActualizacion()),"\""
									," }");
						}
						
					}else
						response_text = "{ \"success\": false }";
				}catch (Throwable e) {
					response_text = "{ \"success\": false }";					
				}
			}else if(accion.equals("borrarResponsableTipo")){
				int id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
				if(id>0){
					ResponsableTipo responsableTipo = ResponsableTipoDAO.ResponsableTipo(id);
					response_text = String.join("","{ \"success\": ",(ResponsableTipoDAO.eliminarResponsableTipo(responsableTipo) ? "true" : "false")," }");
				}
				else
					response_text = "{ \"success\": false }";
			}
		}
		catch(Exception e) {
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
