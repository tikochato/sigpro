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

import dao.FormularioItemTipoDAO;
import pojo.DatoTipo;
import pojo.FormularioItemTipo;
import utilities.Utils;



/**
 * Servlet implementation class SFormularioItemTipo
 */
@WebServlet("/SFormularioItemTipo")
public class SFormularioItemTipo extends HttpServlet {
	private static final long serialVersionUID = 1L;
	class stformularioitemtipo{
		Integer id;
		String nombre;
		String descripcion;
		String usuarioCreo;
		String usuarioActualizacion;
		String fechaCreacion;
		String fechaActualizacion;
		int datotipoid;
		String datotiponombre;
		int estado;
	}
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SFormularioItemTipo() {
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
		
		if(accion.equals("getFormularioItemtiposPagina")){
			int pagina = map.get("pagina")!=null  ? Integer.parseInt(map.get("pagina")) : 0;
			int numeroFormularioItemTipos = map.get("numeroformularioitemtipos")!=null  ? Integer.parseInt(map.get("numeroformularioitemtipos")) : 0;
			String filtro_nombre = map.get("filtro_nombre");
			String filtro_usuario_creo = map.get("filtro_usuario_creo");
			String filtro_fecha_creacion = map.get("filtro_fecha_creacion");
			String columna_ordenada = map.get("columna_ordenada");
			String orden_direccion = map.get("orden_direccion");
			List<FormularioItemTipo> formularioItemTipos = FormularioItemTipoDAO.geFormularioItemTiposPagina(pagina, numeroFormularioItemTipos, 
					filtro_nombre, filtro_usuario_creo, filtro_fecha_creacion,
					columna_ordenada, orden_direccion);
			List<stformularioitemtipo> stdesembolsotipos = new ArrayList<stformularioitemtipo>();
			
			for(FormularioItemTipo formularioItemTipo :formularioItemTipos){
				stformularioitemtipo temp =new stformularioitemtipo();
				temp.id = formularioItemTipo.getId();
				temp.nombre = formularioItemTipo.getNombre();
				temp.descripcion = formularioItemTipo.getDescripcion();
				temp.usuarioCreo = formularioItemTipo.getUsuarioCreo();
				temp.usuarioActualizacion = formularioItemTipo.getUsuarioActualizacion()+"";
				temp.fechaCreacion = Utils.formatDate(formularioItemTipo.getFechaCreacion());
				temp.fechaActualizacion = Utils.formatDate(formularioItemTipo.getFechaActualizacion());
				temp.estado = formularioItemTipo.getEstado();
				temp.datotipoid = formularioItemTipo.getDatoTipo().getId();
				temp.datotiponombre = formularioItemTipo.getDatoTipo().getNombre();
				
				stdesembolsotipos.add(temp);
			}
			
			response_text=new GsonBuilder().serializeNulls().create().toJson(stdesembolsotipos);
	        response_text = String.join("", "\"formularioitemtipos\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(accion.equals("guardarFormularioItemTipo")){
			boolean result = false;
			boolean esnuevo = map.get("esnuevo").equals("true");
			int id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			if(id>0 || esnuevo){
				
				String nombre = map.get("nombre");
				String descripcion = map.get("descripcion");
				int datoTipoId = Integer.parseInt(map.get("datotipoid")!=null ? map.get("datotipoid") : "0");
				
				FormularioItemTipo  formularioItemTipo;
				DatoTipo datoTipo = new DatoTipo();
				datoTipo.setId(datoTipoId);
				
				if(esnuevo){
					formularioItemTipo = new FormularioItemTipo(datoTipo, nombre, descripcion, 1
							, usuario, new DateTime().toDate(), null, null, null);
					
				}
				else{
					formularioItemTipo = FormularioItemTipoDAO.getFormularioItemTipoPorId(id);
					
					formularioItemTipo.setNombre(nombre);
					formularioItemTipo.setDescripcion(descripcion);	
					formularioItemTipo.setFechaActualizacion(new DateTime().toDate());
					formularioItemTipo.setUsuarioActualizacion(usuario);
					formularioItemTipo.setDatoTipo(datoTipo);
				}
				result = FormularioItemTipoDAO.guardarFormularioItemTipo(formularioItemTipo);
				response_text = String.join("","{ \"success\": ",(result ? "true" : "false"),", "
						+ "\"id\": " + formularioItemTipo.getId() +" }");
			}
			else
				response_text = "{ \"success\": false }";
		}
		else if(accion.equals("borrarFormularioItemTipo")){
			int id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			if(id>0){
				FormularioItemTipo formularioItemTipo = FormularioItemTipoDAO.getFormularioItemTipoPorId(id);
				response_text = String.join("","{ \"success\": ",(FormularioItemTipoDAO.eliminarFormularioItemTipo(formularioItemTipo) ? "true" : "false")," }");
			}
			else
				response_text = "{ \"success\": false }";
		}
		else if(accion.equals("numeroFormularioItemTipos")){
			response_text = String.join("","{ \"success\": true, \"totalformularioitemtipos\":",FormularioItemTipoDAO.getTotalFormularioItemTipo().toString()," }");
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
