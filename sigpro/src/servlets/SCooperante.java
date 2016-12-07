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
		String codigo;
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
		if(accion.equals("getCooperantes")){
			List<Cooperante> cooperantes = CooperanteDAO.getCooperantes();
			List<stcooperante> stcooperantes=new ArrayList<stcooperante>();
			for(Cooperante cooperante:cooperantes){
				stcooperante temp =new stcooperante();
				temp.codigo = cooperante.getCodigo();
				temp.descripcion = cooperante.getDescripcion();
				temp.estado = cooperante.getEstado();
				temp.fechaActualizacion = Utils.formatDate(cooperante.getFechaActualizacion());
				temp.fechaCreacion = Utils.formatDate(cooperante.getFechaCreacion());
				temp.id = cooperante.getId();
				temp.nombre = cooperante.getNombre();
				temp.usuarioActualizo = cooperante.getUsuarioActualizo();
				temp.usuarioCreo = cooperante.getUsarioCreo();
				stcooperantes.add(temp);
			}
			
			response_text=new GsonBuilder().serializeNulls().create().toJson(stcooperantes);
	        response_text = String.join("", "\"cooperantes\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(accion.equals("guardarCooperante")){
			boolean result = false;
			boolean esnuevo = map.get("esnuevo").equals("true");
			int id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			if(id>0 || esnuevo){
				String codigo = map.get("codigo");
				String nombre = map.get("nombre");
				String descripcion = map.get("descripcion");
				Cooperante cooperante;
				if(esnuevo){
					cooperante = new Cooperante(codigo, nombre, descripcion, 
							"admin", null, new DateTime().toDate(), null, 1, null);
				}
				else{
					cooperante = CooperanteDAO.getCooperantePorId(id);
					cooperante.setCodigo(codigo);
					cooperante.setNombre(nombre);
					cooperante.setDescripcion(descripcion);
					cooperante.setUsuarioActualizo("admin");
					cooperante.setFechaActualizacion(new DateTime().toDate());
				}
				result = CooperanteDAO.guardarCooperante(cooperante);
				response_text = String.join("","{ \"success\": ",(result ? "true" : "false")," }");
			}
			else
				response_text = "{ \"success\": false }";
		}
		else if(accion.equals("borrarCooperante")){
			int id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			if(id>0){
				Cooperante cooperante = CooperanteDAO.getCooperantePorId(id);
				response_text = String.join("","{ \"success\": ",(CooperanteDAO.eliminarCooperante(cooperante) ? "true" : "false")," }");
			}
			else
				response_text = "{ \"success\": false }";
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
