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

import dao.PrestamoTipoDAO;
import pojo.PrestamoTipo;
import utilities.Utils;

@WebServlet("/SPrestamoTipo")
public class SPrestamoTipo extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	class stprestamotipo{
		Integer id;
		String nombre;
		String descripcion;
		String usuarioCreo;
		String usuarioActualizo;
		String fechaCreacion;
		String fechaActualizacion;
		int estado;
	}
	
    public SPrestamoTipo() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

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
		if(accion.equals("getPrestamoTipoPagina")){
			int pagina = map.get("pagina")!=null  ? Integer.parseInt(map.get("pagina")) : 0;
			int numeroPrestamosTipos = map.get("numeroprestamotipo")!=null  ? Integer.parseInt(map.get("numeroprestamotipo")) : 0;
			String filtro_nombre = map.get("filtro_nombre");
			String filtro_usuario_creo = map.get("filtro_usuario_creo");
			String filtro_fecha_creacion = map.get("filtro_fecha_creacion");
			String columna_ordenada = map.get("columna_ordenada");
			String orden_direccion = map.get("orden_direccion");
			
			
			List<PrestamoTipo> prestamotipos = PrestamoTipoDAO.getPrestamosTipoPagina(pagina, numeroPrestamosTipos, filtro_nombre, filtro_usuario_creo, filtro_fecha_creacion, columna_ordenada, orden_direccion);
			List<stprestamotipo> stcooperantes=new ArrayList<stprestamotipo>();
			for(PrestamoTipo prestamotipo:prestamotipos){
				stprestamotipo temp =new stprestamotipo();
				temp.id = prestamotipo.getId();
				temp.nombre = prestamotipo.getNombre();
				temp.descripcion = prestamotipo.getDescripcion();
				
				temp.estado = prestamotipo.getEstado();
				temp.fechaActualizacion = Utils.formatDateHour(prestamotipo.getFechaActualizacion());
				temp.fechaCreacion = Utils.formatDateHour(prestamotipo.getFechaCreacion());
				temp.usuarioActualizo = prestamotipo.getUsuarioActualizo();
				temp.usuarioCreo = prestamotipo.getUsuarioCreo();
				stcooperantes.add(temp);
			}
			
			response_text=new GsonBuilder().serializeNulls().create().toJson(stcooperantes);
	        response_text = String.join("", "\"poryectotipos\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(accion.equals("numeroPrestamoTipos")){
			String filtro_nombre = map.get("filtro_nombre");
			String filtro_usuario_creo = map.get("filtro_usuario_creo");
			String filtro_fecha_creacion = map.get("filtro_fecha_creacion");
			response_text = String.join("","{ \"success\": true, \"totalprestamotipos\":",PrestamoTipoDAO.getTotalPrestamosTipos(filtro_nombre, filtro_usuario_creo, filtro_fecha_creacion).toString()," }");
		}
		else if(accion.equals("guardarPrestamotipo")){
			boolean result = false;
			boolean esnuevo = map.get("esnuevo").equals("true");
			int id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			if(id>0 || esnuevo){
				
				String nombre = map.get("nombre");
				String descripcion = map.get("descripcion");
				PrestamoTipo prestamoTipo;
				
				if(esnuevo){
					prestamoTipo = new PrestamoTipo(nombre, descripcion, 
							usuario,null,new DateTime().toDate(),null,1,null);
				}
				else{
					prestamoTipo = PrestamoTipoDAO.getPrestamoTipoPorId(id);
					prestamoTipo.setNombre(nombre);
					prestamoTipo.setDescripcion(descripcion);
					prestamoTipo.setUsuarioActualizo(usuario);
					prestamoTipo.setFechaActualizacion(new DateTime().toDate());
				}
				
				result = PrestamoTipoDAO.guardarPrestamoTipo(prestamoTipo);
				
				response_text = String.join("","{ \"success\": ",(result ? "true" : "false"),", "
					+ "\"id\": " + prestamoTipo.getId() ,","
					, "\"usuarioCreo\": \"" , prestamoTipo.getUsuarioCreo(),"\","
					, "\"fechaCreacion\":\" " , Utils.formatDateHour(prestamoTipo.getFechaCreacion()),"\","
					, "\"usuarioactualizo\": \"" , prestamoTipo.getUsuarioActualizo() != null ? prestamoTipo.getUsuarioActualizo() : "","\","
					, "\"fechaactualizacion\": \"" , Utils.formatDateHour(prestamoTipo.getFechaActualizacion()),"\""+
					" }");
			}
			else
				response_text = "{ \"success\": false }";
		}
		else if(accion.equals("borrarPrestamoTipo")){
			int id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			if(id>0){
				PrestamoTipo prestamoTipo = PrestamoTipoDAO.getPrestamoTipoPorId(id);
				response_text = String.join("","{ \"success\": ",(PrestamoTipoDAO.eliminarPrestamoTipo(prestamoTipo) ? "true" : "false")," }");
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
