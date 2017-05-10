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

import dao.CooperanteDAO;
import pojo.Cooperante;
import utilities.Utils;

/**
 * Servlet implementation class SCooperante
 */
@WebServlet("/SCooperante")
public class SCooperante extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	class stcooperante{
		Integer id;
		Integer codigo;
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
    public SCooperante() {
        super();
        // TODO Auto-generated constructor stub
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
		if(accion.equals("getCooperantesPagina")){
			int pagina = map.get("pagina")!=null  ? Integer.parseInt(map.get("pagina")) : 0;
			int numeroCooperantes = map.get("numerocooperantes")!=null  ? Integer.parseInt(map.get("numerocooperantes")) : 0;
			String filtro_nombre = map.get("filtro_nombre");
			String filtro_codigo = map.get("filtro_codigo");
			String filtro_usuario_creo = map.get("filtro_usuario_creo");
			String filtro_fecha_creacion = map.get("filtro_fecha_creacion");
			String columna_ordenada = map.get("columna_ordenada");
			String orden_direccion = map.get("orden_direccion");
			List<Cooperante> cooperantes = CooperanteDAO.getCooperantesPagina(pagina, numeroCooperantes,
					filtro_codigo,filtro_nombre, filtro_usuario_creo, filtro_fecha_creacion, columna_ordenada, orden_direccion);
			List<stcooperante> stcooperantes=new ArrayList<stcooperante>();
			for(Cooperante cooperante:cooperantes){
				stcooperante temp =new stcooperante();
				temp.codigo = cooperante.getCodigo();
				temp.descripcion = cooperante.getDescripcion();
				temp.estado = cooperante.getEstado();
				temp.fechaActualizacion = Utils.formatDateHour(cooperante.getFechaActualizacion());
				temp.fechaCreacion = Utils.formatDateHour(cooperante.getFechaCreacion());
				temp.id = cooperante.getId();
				temp.nombre = cooperante.getNombre();
				temp.usuarioActualizo = cooperante.getUsuarioActualizo();
				temp.usuarioCreo = cooperante.getUsuarioCreo();
				stcooperantes.add(temp);
			}
			
			response_text=new GsonBuilder().serializeNulls().create().toJson(stcooperantes);
	        response_text = String.join("", "\"cooperantes\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(accion.equals("getCooperantes")){
			List<Cooperante> cooperantes = CooperanteDAO.getCooperantes();
			List<stcooperante> stcooperantes=new ArrayList<stcooperante>();
			for(Cooperante cooperante:cooperantes){
				stcooperante temp =new stcooperante();
				temp.codigo = cooperante.getCodigo();
				temp.descripcion = cooperante.getDescripcion();
				temp.estado = cooperante.getEstado();
				temp.fechaActualizacion = Utils.formatDateHour(cooperante.getFechaActualizacion());
				temp.fechaCreacion = Utils.formatDateHour(cooperante.getFechaCreacion());
				temp.id = cooperante.getId();
				temp.nombre = cooperante.getNombre();
				temp.usuarioActualizo = cooperante.getUsuarioActualizo();
				temp.usuarioCreo = cooperante.getUsuarioCreo();
				stcooperantes.add(temp);
			}
			
			response_text=new GsonBuilder().serializeNulls().create().toJson(stcooperantes);
	        response_text = String.join("", "\"cooperantes\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(accion.equals("guardarCooperante")){
			HttpSession sesionweb = request.getSession();
			boolean result = false;
			boolean esnuevo = map.get("esnuevo").equals("true");
			int id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			if(id>0 || esnuevo){
				Integer codigo = Utils.String2Int(map.get("codigo"));
				String nombre = map.get("nombre");
				String siglas = map.get("siglas");
				String descripcion = map.get("descripcion");
				Cooperante cooperante;
				if(esnuevo){
					cooperante = new Cooperante(codigo, nombre,siglas, descripcion, 
							sesionweb.getAttribute("usuario").toString(), null, new DateTime().toDate(), null, 1, null);
				}
				else{
					cooperante = CooperanteDAO.getCooperantePorId(id);
					cooperante.setCodigo(codigo);
					cooperante.setNombre(nombre);
					cooperante.setDescripcion(descripcion);
					cooperante.setUsuarioActualizo(sesionweb.getAttribute("usuario").toString());
					cooperante.setFechaActualizacion(new DateTime().toDate());
				}
				result = CooperanteDAO.guardarCooperante(cooperante);
				response_text = String.join("","{ \"success\": ",(result ? "true" : "false"),", "
						+ "\"id\": " + cooperante.getId() , ","
						, "\"usuarioCreo\": \"" , cooperante.getUsuarioCreo(),"\","
						, "\"fechaCreacion\":\" " , Utils.formatDateHour(cooperante.getFechaCreacion()),"\","
						, "\"usuarioactualizo\": \"" , cooperante.getUsuarioActualizo() != null ? cooperante.getUsuarioActualizo() : "","\","
						, "\"fechaactualizacion\": \"" , Utils.formatDateHour(cooperante.getFechaActualizacion()),"\""+
						" }");
			}
			else
				response_text = "{ \"success\": false }";
		}
		else if(accion.equals("borrarCooperante")){
			HttpSession sesionweb = request.getSession();
			int id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			if(id>0){
				Cooperante cooperante = CooperanteDAO.getCooperantePorId(id);
				cooperante.setUsuarioActualizo(sesionweb.getAttribute("usuario").toString());
				response_text = String.join("","{ \"success\": ",(CooperanteDAO.eliminarCooperante(cooperante) ? "true" : "false")," }");
			}
			else
				response_text = "{ \"success\": false }";
		}
		else if(accion.equals("numeroCooperantes")){
			String filtro_nombre = map.get("filtro_nombre");
			String filtro_codigo = map.get("filtro_codigo");
			String filtro_usuario_creo = map.get("filtro_usuario_creo");
			String filtro_fecha_creacion = map.get("filtro_fecha_creacion");
			response_text = String.join("","{ \"success\": true, \"totalcooperantes\":",CooperanteDAO.getTotalCooperantes(filtro_codigo,filtro_nombre, filtro_usuario_creo, filtro_fecha_creacion).toString()," }");
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
