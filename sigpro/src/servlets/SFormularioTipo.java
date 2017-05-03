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

import dao.FormularioTipoDAO;
import pojo.FormularioTipo;
import utilities.Utils;


@WebServlet("/SFormularioTipo")
public class SFormularioTipo extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	class stformulariotipo{
		Integer id;
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
    public SFormularioTipo() {
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
		
		if(accion.equals("getFormulariotiposPagina")){
			int pagina = map.get("pagina")!=null  ? Integer.parseInt(map.get("pagina")) : 0;
			int numeroFormularioTipo = map.get("numeroformulariostipo")!=null  ? Integer.parseInt(map.get("numeroformulariostipo")) : 0;
			List<FormularioTipo> formulariotipos = FormularioTipoDAO.getFormularioTiposPagina(pagina, numeroFormularioTipo);
			List<stformulariotipo> striesgotipos=new ArrayList<stformulariotipo>();
			for(FormularioTipo formulariotipo:formulariotipos){
				stformulariotipo temp =new stformulariotipo();
				temp.descripcion = formulariotipo.getDescripcion();
				temp.estado = formulariotipo.getEstado();
				temp.fechaActualizacion = Utils.formatDate(formulariotipo.getFechaActualizacion());
				temp.fechaCreacion = Utils.formatDate(formulariotipo.getFechaCreacion());
				temp.id = formulariotipo.getId();
				temp.nombre = formulariotipo.getNombre();
				temp.usuarioActualizo = formulariotipo.getUsuarioActualizo();
				temp.usuarioCreo = formulariotipo.getUsarioCreo();
				striesgotipos.add(temp);
			}
			
			response_text=new GsonBuilder().serializeNulls().create().toJson(striesgotipos);
	        response_text = String.join("", "\"formulariotipos\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		
		else if(accion.equals("numeroFormularioTipos")){
			response_text = String.join("","{ \"success\": true, \"totalformulariotipos\":",FormularioTipoDAO.getTotalFormularioTipo().toString()," }");
		}
		else if(accion.equals("guardarFormulariotipo")){
			boolean result = false;
			boolean esnuevo = map.get("esnuevo").equals("true");
			int id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			if(id>0 || esnuevo){
				
				String nombre = map.get("nombre");
				String descripcion = map.get("descripcion");
				FormularioTipo formularioTipo;
				
				if(esnuevo){
					formularioTipo = new FormularioTipo(nombre, usuario, new DateTime().toDate(), 1);
					formularioTipo.setDescripcion(descripcion);
				}
				else{
					formularioTipo = FormularioTipoDAO.getFormularioTipoPorId(id);
					formularioTipo.setNombre(nombre);
					formularioTipo.setDescripcion(descripcion);
					formularioTipo.setUsuarioActualizo(usuario);
					formularioTipo.setFechaActualizacion(new DateTime().toDate());
					
				}
				
				
				
				result = FormularioTipoDAO.guardarFormularioTipo(formularioTipo);
				response_text = String.join("","{ \"success\": ",(result ? "true" : "false"),", "
						+ "\"id\": " + formularioTipo.getId(), ","
						, "\"usuarioCreo\": \"" , formularioTipo.getUsarioCreo(),"\","
						, "\"fechaCreacion\":\" " , Utils.formatDateHour(formularioTipo.getFechaCreacion()),"\","
						, "\"usuarioActualizo\": \"" , formularioTipo.getUsuarioActualizo() != null ? formularioTipo.getUsuarioActualizo() : "","\","
						, "\"fechaActualizacion\": \"" , Utils.formatDateHour(formularioTipo.getFechaActualizacion()),"\""
						," }");
			}
			else
				response_text = "{ \"success\": false }";
		}
		else if(accion.equals("borrarFormularioTipo")){
			int id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			if(id>0){
				FormularioTipo formularioTipo = FormularioTipoDAO.getFormularioTipoPorId(id);
				response_text = String.join("","{ \"success\": ",(FormularioTipoDAO.eliminarFormularioTipo(formularioTipo) ? "true" : "false")," }");
			}
			else
				response_text = "{ \"success\": false }";
		}else
			response_text = "{ \"success\": false }";
		
		
		response.setHeader("Content-Encoding", "gzip");
		response.setCharacterEncoding("UTF-8");
		        
        OutputStream output = response.getOutputStream();
		GZIPOutputStream gz = new GZIPOutputStream(output);
        gz.write(response_text.getBytes("UTF-8"));
        gz.close();
        output.close();
		
		
			
	}

}
