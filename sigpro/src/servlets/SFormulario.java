package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

import dao.FormularioDAO;
import dao.FormularioItemDAO;
import pojo.Formulario;
import pojo.FormularioItem;
import pojo.FormularioItemTipo;
import pojo.FormularioTipo;
import utilities.Utils;


/**
 * Servlet implementation class SFormulario
 */
@WebServlet("/SFormulario")
public class SFormulario extends HttpServlet {
	private static final long serialVersionUID = 1L;
	class stformulario{
		Integer id;
		String codigo;
		String descripcion;
		String usuarioCreo;
		String usuarioActualizo;
		String fechaCreacion;
		String fechaActualizacion;
		int estado;
		int formulariotipoid;
		String formulariotiponombre;
	}
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SFormulario() {
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
		if(accion.equals("getFormularioPagina")){
			int pagina = map.get("pagina")!=null  ? Integer.parseInt(map.get("pagina")) : 0;
			int numeroFormularios = map.get("numeroformularios")!=null  ? Integer.parseInt(map.get("numeroformularios")) : 0;
			List<Formulario> formularios = FormularioDAO.getFormularioPagina(pagina, numeroFormularios);
			List<stformulario> stfromularios=new ArrayList<stformulario>();
			for(Formulario formulario:formularios){
				stformulario temp =new stformulario();
				temp.id = formulario.getId();
				temp.codigo = formulario.getCodigo();
				temp.descripcion = formulario.getDescripcion();
				temp.estado = formulario.getEstado();
				temp.fechaActualizacion = Utils.formatDate(formulario.getFechaActualizacion());
				temp.fechaCreacion = Utils.formatDate(formulario.getFechaCreacion());
				temp.usuarioActualizo = formulario.getUsuarioActualizo();
				temp.usuarioCreo = formulario.getUsuarioCreo();
				temp.formulariotipoid = formulario.getFormularioTipo().getId();
				temp.formulariotiponombre = formulario.getFormularioTipo().getNombre();
				stfromularios.add(temp);
			}
			
			response_text=new GsonBuilder().serializeNulls().create().toJson(stfromularios);
	        response_text = String.join("", "\"formularios\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(accion.equals("numeroFormularios")){
			response_text = String.join("","{ \"success\": true, \"totalformularios\":",FormularioDAO.getTotalFormularios().toString()," }");
		}
		else if(accion.equals("guardarFormulario")){
			boolean result = false;
			boolean esnuevo = map.get("esnuevo").equals("true");
			int id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			if(id>0 || esnuevo){
				
				String codigo = map.get("codigo");
				String descripcion = map.get("descripcion");
				
				int formulariotipoid = Integer.parseInt(map.get("formulariotipoid"));
				FormularioTipo formularioTipo = new FormularioTipo();
				formularioTipo.setId(formulariotipoid);
				
				Formulario formulario;
				
				if(esnuevo){
					formulario = new Formulario(formularioTipo, codigo, descripcion, usuario, new DateTime().toDate(), 1);
					
				}
				else{
					formulario = FormularioDAO.getFormularioPorId(id);
					formulario.setCodigo(codigo);
					formulario.setDescripcion(descripcion);
					formulario.setUsuarioActualizo("admin");
					formulario.setFechaActualizacion(new DateTime().toDate());
					Set<FormularioItem> items_temp = formulario.getFormularioItems();
					formulario.setFormularioItems(null);
					if (items_temp!=null){
						for (FormularioItem formularioItem : items_temp){
							FormularioItemDAO.eliminarFormularioItem(formularioItem);
						}
					}
				}
				
				result = FormularioDAO.guardarFormulario(formulario);
				
				String[] idsItems =  map.get("items") != null ? map.get("items").toString().split(",") : null;
				if (idsItems !=null && idsItems.length>0){
					for (String idPropiedad : idsItems){
				
						FormularioItemTipo formularioItemTipo = new FormularioItemTipo();
						formularioItemTipo.setId(Integer.parseInt(idPropiedad));
						
						FormularioItem formularioItem = new FormularioItem(formulario, formularioItemTipo, "", 1
								,usuario,new DateTime().toDate(), 1);
						
						
						if (formulario.getFormularioItems() == null){
							formulario.setFormularioItems(new HashSet<FormularioItem>(0));
						}
						formulario.getFormularioItems().add(formularioItem);
					}
				}
				
				result = FormularioDAO.guardarFormulario(formulario);
				response_text = String.join("","{ \"success\": ",(result ? "true" : "false"),", "
						+ "\"id\": " + formulario.getId() +" }");
			}
			else
				response_text = "{ \"success\": false }";
		}
		else if(accion.equals("borrarFormulario")){
			int id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			if(id>0){
				Formulario formulario = FormularioDAO.getFormularioPorId(id);
				response_text = String.join("","{ \"success\": ",(FormularioDAO.eliminarFormulario(formulario) ? "true" : "false")," }");
			}
			else
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
