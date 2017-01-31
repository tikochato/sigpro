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

import org.joda.time.DateTime;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import dao.MetaUnidadMedidaDAO;
import pojo.MetaUnidadMedida;
import utilities.Utils;

/**
 * Servlet implementation class SMetaUnidadMedida
 */
@WebServlet("/SMetaUnidadMedida")
public class SMetaUnidadMedida extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
	
	public class stunidadmeta{
		Integer id;
		String nombre;
		String descripcion;
		String simbolo;
		Integer estado;
		String fechaCreacion;
		String fechaActualizacion;
		String usuarioCreo;
		String usuarioActulizo;
	}
	
    public SMetaUnidadMedida() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
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
		if(accion.equals("getMetaUnidadMedidasPagina")){
			int pagina = map.get("pagina")!=null  ? Integer.parseInt(map.get("pagina")) : 0;
			int numenumeroMetaUnidadMedidas = map.get("numerometaunidadmedidas")!=null  ? Integer.parseInt(map.get("numerometaunidadmedidas")) : 0;
			String filtro_nombre = map.get("filtro_nombre");
			String filtro_usuario_creo = map.get("filtro_usuario_creo");
			String filtro_fecha_creacion = map.get("filtro_fecha_creacion");
			String columna_ordenada = map.get("columna_ordenada");
			String orden_direccion = map.get("orden_direccion");
			List<MetaUnidadMedida> MetaUnidadMedidas = MetaUnidadMedidaDAO.getMetaUnidadMedidasPagina(pagina, numenumeroMetaUnidadMedidas,
					filtro_nombre, filtro_usuario_creo, filtro_fecha_creacion, columna_ordenada, orden_direccion);
			
			List<stunidadmeta> stunidad = new ArrayList<stunidadmeta>();
			for(MetaUnidadMedida metaunidad:MetaUnidadMedidas){
				stunidadmeta temp = new stunidadmeta();
				temp.descripcion = metaunidad.getDescripcion();
				temp.simbolo = metaunidad.getSimbolo();
				temp.estado = metaunidad.getEstado();
				temp.fechaActualizacion = Utils.formatDate(metaunidad.getFechaActualizacion());
				temp.fechaCreacion = Utils.formatDate(metaunidad.getFechaCreacion());
				temp.id = metaunidad.getId();
				temp.nombre = metaunidad.getNombre();
				temp.usuarioActulizo = metaunidad.getUsuarioActualizo();
				temp.usuarioCreo = metaunidad.getUsuarioCreo();
				stunidad.add(temp);
			}
			response_text=new GsonBuilder().serializeNulls().create().toJson(stunidad);
			response_text = String.join("", "\"MetaUnidadMedidas\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(accion.equals("getMetaUnidadMedidas")){
			List<MetaUnidadMedida> MetaUnidadMedidas = MetaUnidadMedidaDAO.getMetaUnidadMedidas();
			List<stunidadmeta> stunidad = new ArrayList<stunidadmeta>();
			for(MetaUnidadMedida metaunidad:MetaUnidadMedidas){
				stunidadmeta temp = new stunidadmeta();
				temp.descripcion = metaunidad.getDescripcion();
				temp.simbolo = metaunidad.getSimbolo();
				temp.estado = metaunidad.getEstado();
				temp.fechaActualizacion = Utils.formatDate(metaunidad.getFechaActualizacion());
				temp.fechaCreacion = Utils.formatDate(metaunidad.getFechaCreacion());
				temp.id = metaunidad.getId();
				temp.nombre = metaunidad.getNombre();
				temp.usuarioActulizo = metaunidad.getUsuarioActualizo();
				temp.usuarioCreo = metaunidad.getUsuarioCreo();
				stunidad.add(temp);
			}
			response_text=new GsonBuilder().serializeNulls().create().toJson(stunidad);
			response_text = String.join("", "\"MetaUnidadMedidas\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(accion.equals("guardarMetaUnidadMedida")){
			boolean result = false;
			boolean esnuevo = map.get("esnueva")!=null ? map.get("esnueva").equals("true") :  false;
			int id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			if(id>0 || esnuevo){
				String nombre = map.get("nombre");
				String descripcion = map.get("descripcion");
				String simbolo = map.get("simbolo");
				MetaUnidadMedida MetaUnidadMedida;
				if(esnuevo){
					MetaUnidadMedida = new MetaUnidadMedida(nombre, descripcion,simbolo,"admin",null, new DateTime().toDate(), null, 1, null );
				}
				else{
					MetaUnidadMedida = MetaUnidadMedidaDAO.getMetaUnidadMedidaPorId(id);
					MetaUnidadMedida.setNombre(nombre);
					MetaUnidadMedida.setDescripcion(descripcion);
					MetaUnidadMedida.setSimbolo(simbolo);
					MetaUnidadMedida.setUsuarioActualizo("admin");
					MetaUnidadMedida.setFechaActualizacion(new DateTime().toDate());
				}
				result = MetaUnidadMedidaDAO.guardarMetaUnidadMedida(MetaUnidadMedida);
				response_text = String.join("","{ \"success\": ",(result ? "true" : "false")," }");
			}
			else
				response_text = "{ \"success\": false }";
		}
		else if(accion.equals("borrarMetaUnidadMedida")){
			int id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			if(id>0){
				MetaUnidadMedida MetaUnidadMedida = MetaUnidadMedidaDAO.getMetaUnidadMedidaPorId(id);
				MetaUnidadMedida.setUsuarioActualizo("admin");
				MetaUnidadMedida.setFechaActualizacion(new DateTime().toDate());
				response_text = String.join("","{ \"success\": ",(MetaUnidadMedidaDAO.eliminarMetaUnidadMedida(MetaUnidadMedida) ? "true" : "false")," }");
			}
			else
				response_text = "{ \"success\": false }";
		}
		else if(accion.equals("numeroMetaUnidadMedidas")){
			String filtro_nombre = map.get("filtro_nombre");
			String filtro_usuario_creo = map.get("filtro_usuario_creo");
			String filtro_fecha_creacion = map.get("filtro_fecha_creacion");
			response_text = String.join("","{ \"success\": true, \"totalMetaUnidadMedidas\":",MetaUnidadMedidaDAO.getTotalMetaUnidadMedidas(filtro_nombre, filtro_usuario_creo, filtro_fecha_creacion).toString()," }");
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
