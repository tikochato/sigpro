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

import dao.DatoTipoDAO;
import dao.HitoTipoDAO;
import pojo.HitoTipo;
import utilities.Utils;

/**
 * Servlet implementation class SHitoTipo
 */
@WebServlet("/SHitoTipo")
public class SHitoTipo extends HttpServlet {
	private static final long serialVersionUID = 1L;
	class sthitotipo{
		Integer id;
		String codigo;
		String nombre;
		String descripcion;
		String usuarioCreo;
		String usuarioActualizo;
		String fechaCreacion;
		String fechaActualizacion;
		int estado;
		int idTipo;
		String tipo;
		
	}

    /**
     * @see HttpServlet#HttpServlet()
     */
    public SHitoTipo() {
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
		if(accion.equals("getHitoTiposPagina")){
			int pagina = map.get("pagina")!=null  ? Integer.parseInt(map.get("pagina")) : 0;
			int numeroHitoTipos = map.get("numerohitotipos")!=null  ? Integer.parseInt(map.get("numerohitotipos")) : 0;
			String filtro_nombre = map.get("filtro_nombre");
			String filtro_usuario_creo = map.get("filtro_usuario_creo");
			String filtro_fecha_creacion = map.get("filtro_fecha_creacion");
			String columna_ordenada = map.get("columna_ordenada");
			String orden_direccion = map.get("orden_direccion");
			List<HitoTipo> hitotipos = HitoTipoDAO.getHitoTiposPagina(pagina, numeroHitoTipos,filtro_nombre,filtro_usuario_creo,
					filtro_fecha_creacion,columna_ordenada,orden_direccion);
			List<sthitotipo> sthitotipos=new ArrayList<sthitotipo>();
			for(HitoTipo hitotipo:hitotipos){
				sthitotipo temp =new sthitotipo();
				temp.descripcion = hitotipo.getDescripcion();
				temp.id = hitotipo.getId();
				temp.nombre = hitotipo.getNombre();
				temp.idTipo = hitotipo.getDatoTipo().getId();
				temp.tipo = hitotipo.getDatoTipo().getNombre();
				temp.usuarioCreo = hitotipo.getUsuarioCreo();
				temp.usuarioActualizo = hitotipo.getUsuarioActualizo();
				temp.fechaCreacion = Utils.formatDateHour(hitotipo.getFechaCreacion());
				temp.fechaActualizacion = Utils.formatDateHour(hitotipo.getFechaActualizacion());
				temp.estado = hitotipo.getEstado();
				sthitotipos.add(temp);
			}

			response_text=new GsonBuilder().serializeNulls().create().toJson(sthitotipos);
	        response_text = String.join("", "\"hitotipos\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(accion.equals("getHitoTipos")){
			List<HitoTipo> hitotipos = HitoTipoDAO.getHitoTipos();
			List<sthitotipo> sthitotipos=new ArrayList<sthitotipo>();
			for(HitoTipo hitotipo:hitotipos){
				sthitotipo temp =new sthitotipo();
				temp.descripcion = hitotipo.getDescripcion();
				temp.estado = hitotipo.getEstado();
				temp.id = hitotipo.getId();
				temp.nombre = hitotipo.getNombre();
				temp.idTipo = hitotipo.getDatoTipo().getId();
				temp.tipo = hitotipo.getDatoTipo().getNombre();
				temp.usuarioCreo = hitotipo.getUsuarioCreo();
				temp.usuarioActualizo = hitotipo.getUsuarioActualizo();
				temp.fechaCreacion = Utils.formatDateHour(hitotipo.getFechaCreacion());
				temp.fechaActualizacion = Utils.formatDateHour(hitotipo.getFechaActualizacion());
				sthitotipos.add(temp);
			}

			response_text=new GsonBuilder().serializeNulls().create().toJson(sthitotipos);
	        response_text = String.join("", "\"hitotipos\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(accion.equals("guardarHitoTipo")){
			boolean result = false;
			boolean esnuevo = map.get("esnuevo").equals("true");
			int id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			if(id>0 || esnuevo){

				String nombre = map.get("nombre");
				String descripcion = map.get("descripcion");
				Integer dato_tipo = map.get("dato_tipo")!=null ? Integer.parseInt(map.get("dato_tipo")) : 0;
				HitoTipo hitotipo;
				if(esnuevo){
					hitotipo = new  HitoTipo(DatoTipoDAO.getDatoTipo(dato_tipo),nombre, descripcion, 1
							,usuario,null,new DateTime().toDate(),null,null ) ;
				}
				else{
					hitotipo = HitoTipoDAO.getHitoTipoPorId(id);
					hitotipo.setNombre(nombre);
					hitotipo.setDescripcion(descripcion);
					hitotipo.setUsuarioActualizo(usuario);
					hitotipo.setFechaActualizacion(new DateTime().toDate());
				}
				result = HitoTipoDAO.guardarHitoTipo(hitotipo);
				response_text = String.join("","{ \"success\": ",(result ? "true" : "false"),", "
						+ "\"id\": " + hitotipo.getId() +" }");
			}
			else
				response_text = "{ \"success\": false }";
		}
		else if(accion.equals("borrarHitoTipo")){
			int id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			if(id>0){
				HitoTipo cooperante = HitoTipoDAO.getHitoTipoPorId(id);
				response_text = String.join("","{ \"success\": ",(HitoTipoDAO.eliminarHitoTipo(cooperante) ? "true" : "false")," }");
			}
			else
				response_text = "{ \"success\": false }";
		}
		else if(accion.equals("numeroHitoTipos")){
			String filtro_nombre = map.get("filtro_nombre");
			String filtro_usuario_creo = map.get("filtro_usuario_creo");
			String filtro_fecha_creacion = map.get("filtro_fecha_creacion");
			
			response_text = String.join("","{ \"success\": true, \"totalhitotipos\":",HitoTipoDAO.getTotalHitoTipos(
					filtro_nombre,filtro_usuario_creo,filtro_fecha_creacion).toString()," }");
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
