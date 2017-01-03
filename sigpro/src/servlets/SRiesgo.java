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

import dao.RiesgoDAO;
import pojo.Proyecto;
import pojo.Riesgo;
import pojo.RiesgoTipo;
import utilities.Utils;

/**
 * Servlet implementation class SRiesgo
 */
@WebServlet("/SRiesgo")
public class SRiesgo extends HttpServlet {
	private static final long serialVersionUID = 1L;
	class striesgo{
		Integer id;
		String nombre;
		String descripcion;
		String usuarioCreo;
		String usuarioActualizo;
		String fechaCreacion;
		String fechaActualizacion;
		Integer riesgotipoid;
		String riesgotiponombre;
		int estado;
	}
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SRiesgo() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
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
		if(accion.equals("getRiesgosPagina")){
			int pagina = map.get("pagina")!=null  ? Integer.parseInt(map.get("pagina")) : 0;
			int numeroRiesgos = map.get("numerocomponentes")!=null  ? Integer.parseInt(map.get("numerocomponentes")) : 0;
			List<Riesgo> riesgos = RiesgoDAO.getRiesgosPagina(pagina, numeroRiesgos);
			List<striesgo> striesgos=new ArrayList<striesgo>();
			for(Riesgo riesgo:riesgos){
				striesgo temp =new striesgo();
				temp.descripcion = riesgo.getDescripcion();
				temp.estado = riesgo.getEstado();
				temp.fechaActualizacion = Utils.formatDate(riesgo.getFechaActualizacion());
				temp.fechaCreacion = Utils.formatDate(riesgo.getFechaCreacion());
				temp.id = riesgo.getId();
				temp.nombre = riesgo.getNombre();
				temp.usuarioActualizo = riesgo.getUsuarioActualizo();
				temp.usuarioCreo = riesgo.getUsuarioCreo();
				temp.riesgotipoid = riesgo.getRiesgoTipo().getId();
				temp.riesgotiponombre = riesgo.getRiesgoTipo().getNombre();
				striesgos.add(temp);
			}
			
			response_text=new GsonBuilder().serializeNulls().create().toJson(striesgos);
	        response_text = String.join("", "\"riesgos\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(accion.equals("getRiesgos")){
			List<Riesgo> riesgos = RiesgoDAO.getRiesgos();
			List<striesgo> striesgos=new ArrayList<striesgo>();
			for(Riesgo riesgo:riesgos){
				striesgo temp =new striesgo();
				temp.descripcion = riesgo.getDescripcion();
				temp.estado = riesgo.getEstado();
				temp.fechaActualizacion = Utils.formatDate(riesgo.getFechaActualizacion());
				temp.fechaCreacion = Utils.formatDate(riesgo.getFechaCreacion());
				temp.id = riesgo.getId();
				temp.nombre = riesgo.getNombre();
				temp.usuarioActualizo = riesgo.getUsuarioActualizo();
				temp.usuarioCreo = riesgo.getUsuarioCreo();
				temp.riesgotipoid = riesgo.getRiesgoTipo().getId();
				temp.riesgotiponombre = riesgo.getRiesgoTipo().getNombre();
				striesgos.add(temp);
			}
			
			response_text=new GsonBuilder().serializeNulls().create().toJson(striesgos);
	        response_text = String.join("", "\"riesgos\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(accion.equals("guardarRiesgo")){
			boolean result = false;
			boolean esnuevo = map.get("esnuevo").equals("true");
			int id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			if(id>0 || esnuevo){
				String nombre = map.get("nombre");
				String descripcion = map.get("descripcion");
				int riesgotipoid = Integer.parseInt(map.get("riesgotipoid"));
				int proyectoid= Integer.parseInt(map.get("proyectoid"));
				
				RiesgoTipo riesgoTipo= new RiesgoTipo();
				riesgoTipo.setId(riesgotipoid);
				
				Proyecto proyecto = new Proyecto();
				proyecto .setId(proyectoid);
				
				Riesgo riesgo;
				if(esnuevo){
					riesgo = new Riesgo(null, null, proyecto
							,riesgoTipo, nombre, usuario, new DateTime().toDate(), 1);
				}
				else{
					riesgo = RiesgoDAO.getRiesgoPorId(id);
					riesgo.setNombre(nombre);
					riesgo.setDescripcion(descripcion);
					riesgo.setUsuarioActualizo(usuario);
					riesgo.setFechaActualizacion(new DateTime().toDate());
				}
				result = RiesgoDAO.guardarRiesgo(riesgo);
				
				response_text = String.join("","{ \"success\": ",(result ? "true" : "false"),", "
						+ "\"id\": " + riesgo.getId() +" }");
			}
			else
				response_text = "{ \"success\": false }";
		}
		else if(accion.equals("borrarRiesgo")){
			int id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			if(id>0){
				Riesgo riesgo = RiesgoDAO.getRiesgoPorId(id);
				riesgo.setUsuarioActualizo(usuario);
				response_text = String.join("","{ \"success\": ",(RiesgoDAO.eliminarRiesgo(riesgo) ? "true" : "false")," }");
			}
			else
				response_text = "{ \"success\": false }";
		}
		else if(accion.equals("numeroRiesgos")){
			response_text = String.join("","{ \"success\": true, \"totalriesgos\":",RiesgoDAO.getTotalRiesgos().toString()," }");
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
