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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import dao.ComponentePropiedadDAO;
import pojo.ComponentePropiedad;
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
        // TODO Auto-generated constructor stub
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
		
		
		
		if(accion.equals("getComponentePropiedadPagina")){
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
				temp.datotiponombre = componentepropiedad.getNombre();
				temp.fechaActualizacion = Utils.formatDate(componentepropiedad.getFechaActualizacion());
				temp.fechaCreacion = Utils.formatDate(componentepropiedad.getFechaCreacion());	
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
				temp.datotiponombre = componentepropiedad.getNombre();
				temp.fechaActualizacion = Utils.formatDate(componentepropiedad.getFechaActualizacion());
				temp.fechaCreacion = Utils.formatDate(componentepropiedad.getFechaCreacion());	
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
		
		response.setHeader("Content-Encoding", "gzip");
		response.setCharacterEncoding("UTF-8");
		
        OutputStream output = response.getOutputStream();
		GZIPOutputStream gz = new GZIPOutputStream(output);
        gz.write(response_text.getBytes("UTF-8"));
        gz.close();
        output.close();
	}
	

}
