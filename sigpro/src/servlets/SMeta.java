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

import dao.ComponenteDAO;
import dao.DatoTipoDAO;
import dao.MetaDAO;
import dao.MetaTipoDAO;
import dao.MetaUnidadMedidaDAO;
import dao.ProductoDAO;
import dao.ProyectoDAO;
import pojo.Componente;
import pojo.DatoTipo;
import pojo.Meta;
import pojo.MetaTipo;
import pojo.MetaUnidadMedida;
import pojo.Producto;
import pojo.Proyecto;
import utilities.Utils;

/**
 * Servlet implementation class SMeta
 */
@WebServlet("/SMeta")
public class SMeta extends HttpServlet {
	private static final long serialVersionUID = 1L;
     
	public class stmeta{
		Integer id;
		String nombre;
		String descripcion;
		Integer estado;
		Integer proyecto;
		Integer componente;
		Integer producto;
		Integer tipoMetaId;
		String tipoMetaNombre;
		Integer unidadMedidaId;
		String unidadMedidaNombre;
		String usuarioCreo;
		String fechaCreacion;
		String usuarioActualizo;
		String fechaActualizacion;
		Integer objetoId;
		Integer objetoTipo;
	}
	
	public class sttipometa{
		Integer id;
		String nombre;
		String descripcion;
		Integer estado;
		String fechaCreacion;
		String fechaActualizacion;
		String usuarioCreo;
		String usuarioActulizo;
	}
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SMeta() {
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
		if(accion.equals("getMetasPagina")){
			int pagina = map.get("pagina")!=null  ? Integer.parseInt(map.get("pagina")) : 0;
			int numenumeroMetas = map.get("numeroMeta")!=null  ? Integer.parseInt(map.get("numeroMeta")) : 0;
			Integer id =  map.get("id")!=null  ? Integer.parseInt(map.get("id")) : 0;
			Integer tipo =  map.get("tipo")!=null  ? Integer.parseInt(map.get("tipo")) : 0;
			String filtro_nombre = map.get("filtro_nombre");
			String filtro_usuario_creo = map.get("filtro_usuario_creo");
			String filtro_fecha_creacion = map.get("filtro_fecha_creacion");
			String columna_ordenada = map.get("columna_ordenada");
			String orden_direccion = map.get("orden_direccion");
			List<Meta> Metas = MetaDAO.getMetasPagina(pagina, numenumeroMetas, id, tipo,filtro_nombre, filtro_usuario_creo, filtro_fecha_creacion, columna_ordenada, orden_direccion);
			List<stmeta> tmetas = new ArrayList<stmeta>();
			for(Meta meta : Metas){
				stmeta temp = new stmeta();
				temp.id = meta.getId();
				temp.nombre = meta.getNombre();
				temp.descripcion = meta.getDescripcion();
				temp.objetoId = meta.getObjetoId();
				temp.objetoTipo = meta.getObjetoTipo();
				temp.estado = meta.getEstado();
				temp.fechaActualizacion = Utils.formatDate(meta.getFechaActualizacion());
				temp.fechaCreacion = Utils.formatDate(meta.getFechaCreacion());
				temp.tipoMetaId = meta.getMetaTipo().getId();
				temp.tipoMetaNombre = meta.getMetaTipo().getNombre();
				temp.unidadMedidaId = meta.getMetaUnidadMedida().getId();
				temp.unidadMedidaNombre = meta.getMetaUnidadMedida().getNombre();
				temp.usuarioActualizo = meta.getUsuarioActualizo();
				temp.usuarioCreo = meta.getUsuarioCreo();
				tmetas.add(temp);
			}
			response_text=new GsonBuilder().serializeNulls().create().toJson(tmetas);
	        response_text = String.join("", "\"Metas\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(accion.equals("getMetas")){
			List<Meta> Metas = MetaDAO.getMetas();
			response_text=new GsonBuilder().serializeNulls().create().toJson(Metas);
	        response_text = String.join("", "\"Metas\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(accion.equals("guardarMeta")){
			boolean result = false;
			boolean esnuevo = map.get("esnueva")!=null ? map.get("esnueva").equals("true") :  false;
			int id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			if(id>0 || esnuevo){
				String nombre = map.get("nombre");
				Integer idMetaTipo = map.get("tipometaid")!=null ? Integer.parseInt(map.get("tipometaid")) : 0;
				MetaTipo metaTipo = MetaTipoDAO.getMetaTipoPorId(idMetaTipo);
				Integer idUnidadMedida = map.get("unidadmetaid")!=null ? Integer.parseInt(map.get("unidadmetaid")) : 0;
				MetaUnidadMedida metaUnidadMedida = MetaUnidadMedidaDAO.getMetaUnidadMedidaPorId(idUnidadMedida);
				String descripcion = map.get("descripcion");
				Integer datoTipoId = Utils.getParameterInteger(map, "datotipo");
				DatoTipo datoTipo = DatoTipoDAO.getDatoTipo(datoTipoId);
				Integer objetoId = Utils.getParameterInteger(map, "objetoId");
				Integer objetoTipo = Utils.getParameterInteger(map, "objetoTipo");
				Meta Meta;
				if(esnuevo){
					Meta = new Meta(datoTipo, metaTipo, metaUnidadMedida, nombre, descripcion, 
							usuario, null, new DateTime().toDate(), null, 1, objetoId, objetoTipo, null);
				}
				else{
					Meta = MetaDAO.getMetaPorId(id);
					Meta.setNombre(nombre);
					Meta.setDescripcion(descripcion);
					Meta.setUsuarioActualizo(usuario);
					Meta.setFechaActualizacion(new DateTime().toDate());
				}
				result = MetaDAO.guardarMeta(Meta);
				response_text = String.join("","{ \"success\": ",(result ? "true" : "false")," }");
			}
			else
				response_text = "{ \"success\": false }";
		}
		else if(accion.equals("borrarMeta")){
			int id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			if(id>0){
				Meta Meta = MetaDAO.getMetaPorId(id);
				Meta.setUsuarioActualizo(usuario);
				Meta.setFechaActualizacion(new DateTime().toDate());
				response_text = String.join("","{ \"success\": ",(MetaDAO.eliminarMeta(Meta) ? "true" : "false")," }");
			}
			else
				response_text = "{ \"success\": false }";
		}
		else if(accion.equals("numeroMetas")){
			String filtro_nombre = map.get("filtro_nombre");
			String filtro_usuario_creo = map.get("filtro_usuario_creo");
			String filtro_fecha_creacion = map.get("filtro_fecha_creacion");
			response_text = String.join("","{ \"success\": true, \"totalMetas\":",MetaDAO.getTotalMetas(filtro_nombre, filtro_usuario_creo, filtro_fecha_creacion).toString()," }");
		}
		else if(accion.equals("getMetasTipos")){
			List<MetaTipo> MetaTipos = MetaTipoDAO.getMetaTipos();
			List<sttipometa> sttipo = new ArrayList<sttipometa>();
			for(MetaTipo metatipo:MetaTipos){
				sttipometa temp = new sttipometa();
				temp.descripcion = metatipo.getDescripcion();
				temp.estado = metatipo.getEstado();
				temp.fechaActualizacion = Utils.formatDate(metatipo.getFechaActualizacion());
				temp.fechaCreacion = Utils.formatDate(metatipo.getFechaCreacion());
				temp.id = metatipo.getId();
				temp.nombre = metatipo.getNombre();
				temp.usuarioActulizo = metatipo.getUsuarioActualizo();
				temp.usuarioCreo = metatipo.getUsuarioCreo();
				sttipo.add(temp);
			}
			response_text=new GsonBuilder().serializeNulls().create().toJson(sttipo);
	        response_text = String.join("", "\"MetasTipos\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(accion.equals("getMetasUnidadesMedida")){
			List<MetaUnidadMedida> MetaUnidades = MetaUnidadMedidaDAO.getMetaUnidadMedidas();
			List<sttipometa> stunidad = new ArrayList<sttipometa>();
			for(MetaUnidadMedida metaunidad : MetaUnidades){
				sttipometa temp = new sttipometa();
				temp.descripcion = metaunidad.getDescripcion();
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
			response_text = String.join("", "\"MetasUnidades\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(accion.equals("getPcp")){
			String nombre = "";
			Integer tipo = map.get("tipo")!=null ? Integer.parseInt(map.get("tipo")) : 0;
			Integer id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			switch(tipo){
				case 1: Proyecto proyecto = ProyectoDAO.getProyectoPorId(id); nombre = (proyecto!=null) ? proyecto.getNombre() : ""; break;
				case 2: Componente componente = ComponenteDAO.getComponentePorId(id); nombre = (componente!=null) ? componente.getNombre() : ""; break;
				case 3: Producto producto = ProductoDAO.getProductoPorId(id); nombre = (producto!=null) ? producto.getNombre() : ""; break;
			}
	        response_text = String.join("", "\"nombre\":\"",nombre,"\"");
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
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
