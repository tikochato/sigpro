package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
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

import dao.ComponentePropiedadDAO;
import dao.ComponentePropiedadValorDAO;
import pojo.ComponentePropiedad;
import pojo.ComponentePropiedadValor;
import pojo.DatoTipo;
import utilities.CFormaDinamica;
import utilities.Utils;

/**
 * Servlet implementation class SComponentePropiedad
 */
@WebServlet("/SComponentePropiedad")
public class SComponentePropiedad extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	class stcomponentepropiedad{
		int id;
		String nombre;
		String descripcion;
		int datotipoid;
		String datotiponombre;
		String usuarioCreo;
		String usuarioActualizo;
		String fechaCreacion;
		String fechaActualizacion;
	}
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SComponentePropiedad() {
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
		
		
		if(accion.equals("getComponentePropiedadPaginaPorTipo")){
			int pagina = map.get("pagina")!=null  ? Integer.parseInt(map.get("pagina")) : 0;
			int idProyectoPropiedad = map.get("idComponenteTipo")!=null  ? Integer.parseInt(map.get("idComponenteTipo")) : 0;
			List<ComponentePropiedad> compoentepropiedades = ComponentePropiedadDAO.getComponentePropiedadesPorTipoComponentePagina(pagina, idProyectoPropiedad);
			List<stcomponentepropiedad> stcomponentepropiedad=new ArrayList<stcomponentepropiedad>();
			for(ComponentePropiedad componentepropiedad:compoentepropiedades){
				stcomponentepropiedad temp =new stcomponentepropiedad();
				temp.id = componentepropiedad.getId();
				temp.nombre = componentepropiedad.getNombre();
				temp.descripcion = componentepropiedad.getDescripcion();
				temp.datotipoid = componentepropiedad.getDatoTipo().getId();
				temp.datotiponombre = componentepropiedad.getDatoTipo().getNombre();
				temp.fechaActualizacion = Utils.formatDateHour(componentepropiedad.getFechaActualizacion());
				temp.fechaCreacion = Utils.formatDateHour(componentepropiedad.getFechaCreacion());	
				temp.usuarioActualizo = componentepropiedad.getUsuarioActualizo();
				temp.usuarioCreo = componentepropiedad.getUsuarioCreo();
				stcomponentepropiedad.add(temp);
			}
			response_text=new GsonBuilder().serializeNulls().create().toJson(stcomponentepropiedad);
	        response_text = String.join("", "\"componentepropiedades\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(accion.equals("getComponentePropiedadPagina")){
			int pagina = map.get("pagina")!=null  ? Integer.parseInt(map.get("pagina")) : 0;
			int numeroComponentePropiedad = map.get("numerocomponentepropiedades")!=null  ? Integer.parseInt(map.get("numerocomponentepropiedades")) : 0;
			String filtro_nombre = map.get("filtro_nombre");
			String filtro_usuario_creo = map.get("filtro_usuario_creo");
			String filtro_fecha_creacion = map.get("filtro_fecha_creacion");
			String columna_ordenada = map.get("columna_ordenada");
			String orden_direccion = map.get("orden_direccion");
			List<ComponentePropiedad> compoentepropiedades = ComponentePropiedadDAO.getComponentePropiedadesPagina(pagina,numeroComponentePropiedad,
					filtro_nombre,filtro_usuario_creo,filtro_fecha_creacion,columna_ordenada,orden_direccion);
			List<stcomponentepropiedad> stcomponentepropiedad=new ArrayList<stcomponentepropiedad>();
			for(ComponentePropiedad componentepropiedad:compoentepropiedades){
				stcomponentepropiedad temp =new stcomponentepropiedad();
				temp.id = componentepropiedad.getId();
				temp.nombre = componentepropiedad.getNombre();
				temp.descripcion = componentepropiedad.getDescripcion();
				temp.datotipoid = componentepropiedad.getDatoTipo().getId();
				temp.datotiponombre = componentepropiedad.getDatoTipo().getNombre();
				temp.fechaActualizacion = Utils.formatDateHour(componentepropiedad.getFechaActualizacion());
				temp.fechaCreacion = Utils.formatDateHour(componentepropiedad.getFechaCreacion());	
				temp.usuarioActualizo = componentepropiedad.getUsuarioActualizo();
				temp.usuarioCreo = componentepropiedad.getUsuarioCreo();
				stcomponentepropiedad.add(temp);
			}
			response_text=new GsonBuilder().serializeNulls().create().toJson(stcomponentepropiedad);
	        response_text = String.join("", "\"componentepropiedades\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(accion.equals("getComponentePropiedadesTotalDisponibles")){
			int pagina = map.get("pagina")!=null  ? Integer.parseInt(map.get("pagina")) : 0;
			String idsPropiedades = map.get("idspropiedades")!=null ? map.get("idspropiedades").toString()   : "0";
			int numeroComponentePropiedad = map.get("numerocomponentepropiedad")!=null  ? Integer.parseInt(map.get("numerocomponentepropiedad")) : 0;
			List<ComponentePropiedad> componentepropiedades = ComponentePropiedadDAO.getComponentePropiedadPaginaTotalDisponibles(pagina, numeroComponentePropiedad,idsPropiedades);
			List<stcomponentepropiedad> stcomponentepropiedad=new ArrayList<stcomponentepropiedad>();
			for(ComponentePropiedad componentepropiedad:componentepropiedades){
				stcomponentepropiedad temp =new stcomponentepropiedad();
				temp.id = componentepropiedad.getId();
				temp.nombre = componentepropiedad.getNombre();
				temp.descripcion = componentepropiedad.getDescripcion();
				temp.datotipoid = componentepropiedad.getDatoTipo().getId();
				temp.datotiponombre = componentepropiedad.getDatoTipo().getNombre();
				temp.fechaActualizacion = Utils.formatDateHour(componentepropiedad.getFechaActualizacion());
				temp.fechaCreacion = Utils.formatDateHour(componentepropiedad.getFechaCreacion());	
				temp.usuarioActualizo = componentepropiedad.getUsuarioActualizo();
				temp.usuarioCreo = componentepropiedad.getUsuarioCreo();
				stcomponentepropiedad.add(temp);
			}
			response_text=new GsonBuilder().serializeNulls().create().toJson(stcomponentepropiedad);
	        response_text = String.join("", "\"componentepropiedades\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(accion.equals("numeroComponentePropiedadesDisponibles")){
			response_text = String.join("","{ \"success\": true, \"totalcomponentepropiedades\":",ComponentePropiedadDAO.getTotalComponentePropiedades().toString()," }");
		}
		else if(accion.equals("numeroComponentePropiedades")){
			String filtro_nombre = map.get("filtro_nombre");
			String filtro_usuario_creo = map.get("filtro_usuario_creo");
			String filtro_fecha_creacion = map.get("filtro_fecha_creacion");
			response_text = String.join("","{ \"success\": true, \"totalcomponentepropiedades\":",ComponentePropiedadDAO.getTotalComponentePropiedad(filtro_nombre, filtro_usuario_creo, filtro_fecha_creacion).toString()," }");
		}
		else if(accion.equals("guardarComponentePropiedad")){
			boolean result = false;
			boolean esnuevo = map.get("esnuevo").equals("true");
			int id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			if(id>0 || esnuevo){
				
				String nombre = map.get("nombre");
				String descripcion = map.get("descripcion");
				int datoTipoId = map.get("datoTipoId")!=null ? Integer.parseInt(map.get("datoTipoId")) : 0;
				DatoTipo datoTipo = new DatoTipo();
				datoTipo.setId(datoTipoId);
				
				ComponentePropiedad componentePropiedad;
				if(esnuevo){
					componentePropiedad = new ComponentePropiedad(datoTipo, nombre, usuario, new DateTime().toDate(), 1);
				}
				else{
					componentePropiedad = ComponentePropiedadDAO.getComponentePropiedadPorId(id);
					
					componentePropiedad.setNombre(nombre);
					componentePropiedad.setDescripcion(descripcion);
					componentePropiedad.setUsuarioActualizo(usuario);
					componentePropiedad.setFechaActualizacion(new DateTime().toDate());
					componentePropiedad.setDatoTipo(datoTipo);
				}
				result = ComponentePropiedadDAO.guardarComponentePropiedad(componentePropiedad);
				response_text = String.join("","{ \"success\": ",(result ? "true" : "false"),", "
						+ "\"id\": " + componentePropiedad.getId() , ","
						, "\"usuarioCreo\": \"" , componentePropiedad.getUsuarioCreo(),"\","
						, "\"fechaCreacion\":\" " , Utils.formatDateHour(componentePropiedad.getFechaCreacion()),"\","
						, "\"usuarioactualizo\": \"" , componentePropiedad.getUsuarioActualizo() != null ? componentePropiedad.getUsuarioActualizo() : "","\","
						, "\"fechaactualizacion\": \"" , Utils.formatDateHour(componentePropiedad.getFechaActualizacion()),"\""+
						" }");
			}
			else
				response_text = "{ \"success\": false }";
		}	
		else if(accion.equals("borrarComponentePropiedad")){
			
			int id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			if(id>0){
				ComponentePropiedad componentePropiedad = ComponentePropiedadDAO.getComponentePropiedadPorId(id);
				componentePropiedad.setUsuarioActualizo(usuario);
				response_text = String.join("","{ \"success\": ",(ComponentePropiedadDAO.eliminarComponentePropiedad(componentePropiedad) ? "true" : "false")," }");
			}
			else
				response_text = "{ \"success\": false }";
		}
		else if(accion.equals("getComponentePropiedadPorTipo")){
			int idComponente = map.get("idComponente")!=null  ? Integer.parseInt(map.get("idComponente")) : 0;
			int idComponenteTipo = map.get("idComponenteTipo")!=null  ? Integer.parseInt(map.get("idComponenteTipo")) : 0;
			List<ComponentePropiedad> compoentepropiedades = ComponentePropiedadDAO.getComponentePropiedadesPorTipoComponente(idComponenteTipo);
			
			List<HashMap<String,Object>> campos = new ArrayList<>();
			for(ComponentePropiedad componentepropiedad:compoentepropiedades){
				HashMap <String,Object> campo = new HashMap<String, Object>();
				campo.put("id", componentepropiedad.getId());
				campo.put("nombre", componentepropiedad.getNombre());
				campo.put("tipo", componentepropiedad.getDatoTipo().getId());
				ComponentePropiedadValor coomponentePropiedadValor = ComponentePropiedadValorDAO.getValorPorComponenteYPropiedad(componentepropiedad.getId(), idComponente);
				if (coomponentePropiedadValor !=null ){
					switch (componentepropiedad.getDatoTipo().getId()){
						case 1:
							campo.put("valor",  coomponentePropiedadValor.getValorString());
							break;
						case 2:
							campo.put("valor", coomponentePropiedadValor.getValorEntero());
							break;
						case 3:
							campo.put("valor", coomponentePropiedadValor.getValorDecimal());
							break;
						case 4:
							campo.put("valor", coomponentePropiedadValor.getValorEntero()==1 ? true : false);
							break;	
						case 5:
							campo.put("valor", Utils.formatDate(coomponentePropiedadValor.getValorTiempo()));
							break;
					}
				}
				else{
					campo.put("valor", "");
				}
				campos.add(campo);
			}
			
			response_text = CFormaDinamica.convertirEstructura(campos);
	        response_text = String.join("", "\"componentepropiedades\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else
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
