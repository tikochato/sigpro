package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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

import org.joda.time.DateTime;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import dao.HitoDAO;
import dao.HitoResultadoDAO;
import pojo.Hito;
import pojo.HitoResultado;
import pojo.HitoTipo;
import pojo.Proyecto;
import utilities.CLogger;
import utilities.Utils;

/**
 * Servlet implementation class SHito
 */
@WebServlet("/SHito")
public class SHito extends HttpServlet {
	private static final long serialVersionUID = 1L;
	class sthito{
		Integer id;
		String nombre;
		String fecha;
		Integer hitotipoid;
		String hitotiponombre;
		String descripcion;
		String usuarioCreo;
		String usuarioActualizo;
		String fechaCreacion;
		String fechaActualizacion;
		String resultado;
		String comentario;
		int datotipoid;
		int estado;
	}
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SHito() {
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
		if(accion.equals("getHitosPagina")){
			int pagina = map.get("pagina")!=null  ? Integer.parseInt(map.get("pagina")) : 0;
			int numeroHitos = map.get("numerohitos")!=null  ? Integer.parseInt(map.get("numerohitos")) : 0;
			List<Hito> hitos = HitoDAO.getHitosPagina(pagina, numeroHitos);
			List<sthito> sthitos=new ArrayList<sthito>();
			for(Hito hito:hitos){
				sthito temp =new sthito();
				temp.descripcion = hito.getDescripcion();
				temp.estado = hito.getEstado();
				temp.fecha = Utils.formatDate(hito.getFecha());
				temp.fechaActualizacion = Utils.formatDate(hito.getFechaActualizacion());
				temp.fechaCreacion = Utils.formatDate(hito.getFechaCreacion());
				temp.id = hito.getId();
				temp.nombre = hito.getNombre();
				temp.usuarioActualizo = hito.getUsuarioActualizo();
				temp.usuarioCreo = hito.getUsuarioCreo();
				temp.hitotipoid = hito.getHitoTipo().getId();
				temp.hitotiponombre = hito.getHitoTipo().getNombre();
				temp.datotipoid = 5;
				
				HitoResultado hitoResultado = HitoResultadoDAO.getHitoResultadoActivoPorHito(hito.getId());
				if (hitoResultado!=null){
					switch(temp.datotipoid){   
						case 1:
							temp.resultado = hitoResultado.getValorString();
							break;
						case 2:
							temp.resultado = hitoResultado.getValorEntero().toString();
							break;
						case 3: 
							temp.resultado = hitoResultado.getValorDecimal().toString();
							break;
						case 4:
							temp.resultado = "false";
							break;
						case 5:
							SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
							temp.resultado = sdf.format(hitoResultado.getValorTiempo());
							break;
						default: temp.resultado = null;	
					}
					temp.comentario = hitoResultado.getComentario();
				}
				sthitos.add(temp);
			}
			
			response_text=new GsonBuilder().serializeNulls().create().toJson(sthitos);
	        response_text = String.join("", "\"hitos\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(accion.equals("getHitos")){
			List<Hito> hitos = HitoDAO.getHitos();
			List<sthito> sthitos=new ArrayList<sthito>();
			for(Hito hito:hitos){
				sthito temp =new sthito();
				temp.descripcion = hito.getDescripcion();
				temp.estado = hito.getEstado();
				temp.fecha = Utils.formatDate(hito.getFecha());
				temp.fechaActualizacion = Utils.formatDate(hito.getFechaActualizacion());
				temp.fechaCreacion = Utils.formatDate(hito.getFechaCreacion());
				temp.id = hito.getId();
				temp.nombre = hito.getNombre();
				temp.usuarioActualizo = hito.getUsuarioActualizo();
				temp.usuarioCreo = hito.getUsuarioCreo();
				temp.hitotipoid = hito.getHitoTipo().getId();				
				temp.hitotiponombre = hito.getHitoTipo().getNombre();
				temp.datotipoid = 5;
				
				HitoResultado hitoResultado = HitoResultadoDAO.getHitoResultadoActivoPorHito(hito.getId());
				if (hitoResultado!=null){
					switch(temp.datotipoid){
						case 1:
							temp.resultado = hitoResultado.getValorString();
							break;
						case 2:
							temp.resultado = hitoResultado.getValorEntero().toString();
							break;
						case 3: 
							temp.resultado = hitoResultado.getValorDecimal().toString();
							break;
						case 4:
							temp.resultado = "false";
							break;
						case 5:
							SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
							temp.resultado = sdf.format(hitoResultado.getValorTiempo());
							break;
						default: temp.resultado = null;	
					}
					temp.comentario = hitoResultado.getComentario();
				}
				sthitos.add(temp);
			}
			 
			response_text=new GsonBuilder().serializeNulls().create().toJson(sthitos);
	        response_text = String.join("", "\"hitos\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(accion.equals("guardarHito")){
			
			boolean result = false;
			boolean esnuevo = map.get("esnuevo").equals("true");
			int id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			if(id>0 || esnuevo){
				try{
				
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				
				String nombre = map.get("nombre");
				String descripcion = map.get("descripcion");
				Date fecha = sdf.parse(map.get("fecha"));
				int datotipoid = Integer.parseInt(map.get("datotipoid"));
				String comentario = map.get("comentario");
				int hitoTipoId = Integer.parseInt(map.get("hitotipoid"));
				int proyectoId = Integer.parseInt(map.get("proyectoid"));
				
				Hito hito;
				HitoTipo hitoTipo = new HitoTipo();
				hitoTipo.setId(hitoTipoId);
				
				Proyecto proyecto = new Proyecto();
				proyecto.setId(proyectoId);
				
				if(esnuevo){
					hito = new Hito(hitoTipo, proyecto, nombre, descripcion, usuario, null 
							,new DateTime().toDate(), null, 1, fecha, null);
				}
				else{
					hito = HitoDAO.getHitoPorId(id);
					hito.setNombre(nombre);
					hito.setDescripcion(descripcion);
					hito.setFecha(fecha);
					hito.setUsuarioActualizo(usuario);					
					hito.setFechaActualizacion(new DateTime().toDate());
					
				}
				result = HitoDAO.guardarHito(hito);
				
				Integer valorEntero = 0;
				String valorString = null;
				Date valorTiempo = null;
				//Boolean valorBool = null;
				BigDecimal valorDecimal = null;
				
				switch(datotipoid){
					case 1: 
						valorEntero =map.get("resultado")!=null && map.get("resultado").length()>0 ? Integer.parseInt(map.get("resultado")) : null;
						break;
					case 2:
						valorString = map.get("resultado");
						break;
					case 3:
						valorDecimal = map.get("resultado")!=null && map.get("resultado").length()>0 ? new BigDecimal(map.get("resultado")) : null;
						break;
					/*case 4:
						if (map.get("resultado").equals("true")){
							valorBool=true;
						}else if (map.get("resultado").equals("true")){
							valorBool = false;
						}*/
					case 5:
						valorTiempo =map.get("resultado")!=null ? sdf.parse(map.get("resultado")) : null;
						break;
				}
				
				HitoResultado hitoResultadoActivo = HitoResultadoDAO.getHitoResultadoActivoPorHito(hito.getId());
				if (hitoResultadoActivo!=null){
					hitoResultadoActivo.setUsuarioActualizo(usuario);
					hitoResultadoActivo.setFechaActualizacion(new DateTime().toDate());
					result = HitoResultadoDAO.eliminarHitoResultado(hitoResultadoActivo);
				}
				
				HitoResultado hitoResultado = new HitoResultado(hito, valorEntero, valorString, valorDecimal, valorTiempo, 
						comentario, usuario, null, new DateTime().toDate(), null, 1);
				
				result = HitoResultadoDAO.guardarHitoResultado(hitoResultado);
				
				
				response_text = String.join("","{ \"success\": ",(result ? "true" : "false"),", "
						+ "\"id\": " + hito.getId() +" }");
				}catch(Throwable e){
					response_text = "{ \"success\": false }";
					CLogger.write("1", SHito.class, e);
				}
			}
			else
				response_text = "{ \"success\": false }";
		}
		else if(accion.equals("borrarHito")){
			boolean result = false;
			int id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			if(id>0){
				Hito hito = HitoDAO.getHitoPorId(id);
				hito.setUsuarioActualizo(usuario);
				result = HitoDAO.eliminarHito(hito);
				
				HitoResultado hitoResultadoActivo = HitoResultadoDAO.getHitoResultadoActivoPorHito(hito.getId());
				if (hitoResultadoActivo!=null){
					hitoResultadoActivo.setUsuarioActualizo(usuario);
					hitoResultadoActivo.setFechaActualizacion(new DateTime().toDate());
					hitoResultadoActivo.setEstado(0);
					result = HitoResultadoDAO.eliminarHitoResultado(hitoResultadoActivo);
				}
					
				response_text = String.join("","{ \"success\": ",(result ? "true" : "false")," }");
			}
			else
				response_text = "{ \"success\": false }";
		}
		else if(accion.equals("numeroHitos")){
			response_text = String.join("","{ \"success\": true, \"totalhitos\":",HitoDAO.getTotalHitos().toString()," }");
		} 
		else if(accion.equals("getHitosPaginaPorProyecto")){
			
			int pagina = map.get("pagina")!=null  ? Integer.parseInt(map.get("pagina")) : 0;
			int numeroHitos = map.get("numerohitos")!=null  ? Integer.parseInt(map.get("numerohitos")) : 0;
			int proyectoId = map.get("proyectoid")!=null  ? Integer.parseInt(map.get("proyectoid")) : 0;
			String filtro_nombre = map.get("filtro_nombre");
			String filtro_usuario_creo = map.get("filtro_usuario_creo");
			String filtro_fecha_creacion = map.get("filtro_fecha_creacion");
			String columna_ordenada = map.get("columna_ordenada");
			String orden_direccion = map.get("orden_direccion");
			
			List<Hito> hitos = HitoDAO.getHitosPaginaPorProyecto(pagina, numeroHitos,proyectoId,
					filtro_nombre,filtro_usuario_creo,filtro_fecha_creacion,columna_ordenada,orden_direccion);
			List<sthito> sthitos=new ArrayList<sthito>();
			for(Hito hito:hitos){
				sthito temp =new sthito();
				temp.descripcion = hito.getDescripcion();
				temp.estado = hito.getEstado();
				temp.fechaActualizacion = Utils.formatDate(hito.getFechaActualizacion());
				temp.fechaCreacion = Utils.formatDate(hito.getFechaCreacion());
				temp.id = hito.getId();
				temp.nombre = hito.getNombre();
				temp.fecha = Utils.formatDate(hito.getFecha());
				temp.usuarioActualizo = hito.getUsuarioActualizo();
				temp.usuarioCreo = hito.getUsuarioCreo();
				temp.hitotipoid = hito.getHitoTipo().getId();
				temp.hitotiponombre = hito.getHitoTipo().getNombre();
				sthitos.add(temp);
			}
			
			response_text=new GsonBuilder().serializeNulls().create().toJson(sthitos);
	        response_text = String.join("", "\"hitos\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(accion.equals("numeroHitosPorProyecto")){
			String filtro_nombre = map.get("filtro_nombre");
			String filtro_usuario_creo = map.get("filtro_usuario_creo");
			String filtro_fecha_creacion = map.get("filtro_fecha_creacion");
			int proyectoId = map.get("proyectoid")!=null  ? Integer.parseInt(map.get("proyectoid")) : 0;
			response_text = String.join("","{ \"success\": true, \"totalhitos\":",HitoDAO.
					getTotalHitosPorProyecto(proyectoId,filtro_nombre,filtro_usuario_creo,filtro_fecha_creacion).toString()," }");
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
