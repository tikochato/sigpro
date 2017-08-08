package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
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

import dao.CategoriaAdquisicionDAO;
import pojo.CategoriaAdquisicion;
import utilities.Utils;

@WebServlet("/SCategoriaAdquisicion")
public class SCategoriaAdquisicion extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	class stcategoriaadquisicion{
		Integer id;
		String nombre;
		String descripcion;
		String usuario_creo;
		String usuario_actualizo;
		String fecha_creo;
		String fecha_actualizacion;
		Integer estado;
	}
	
    public SCategoriaAdquisicion() {
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
		Map<String, String> map = gson.fromJson(sb.toString(), type);
		String accion = map.get("accion")!=null ? map.get("accion") : "";
		String response_text = "";
		
		if(accion.equals("getCategoriaAdquisicion")){
			List<CategoriaAdquisicion> categoriaAdquisiciones = CategoriaAdquisicionDAO.getCategoriaAdquisicion();
			
			List<stcategoriaadquisicion> lstCategoriaAdquisicion = new ArrayList<stcategoriaadquisicion>();
			for(CategoriaAdquisicion categoriaAdquisicion : categoriaAdquisiciones){
				stcategoriaadquisicion temp = new stcategoriaadquisicion();
				temp.id = categoriaAdquisicion.getId();
				temp.nombre = categoriaAdquisicion.getNombre();
				temp.descripcion = categoriaAdquisicion.getDescripcion();
				temp.usuario_creo = categoriaAdquisicion.getUsuarioCreo();
				temp.usuario_actualizo = categoriaAdquisicion.getUsuarioActualizo();
				temp.fecha_creo = Utils.formatDate(categoriaAdquisicion.getFechaCreacion());
				temp.fecha_actualizacion = Utils.formatDate(categoriaAdquisicion.getFechaActualizacion());
				temp.estado = categoriaAdquisicion.getEstado();
				lstCategoriaAdquisicion.add(temp);
			}
			
			response_text=new GsonBuilder().serializeNulls().create().toJson(lstCategoriaAdquisicion);
	        response_text = String.join("", "\"categoriaAdquisicion\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text, "}");
		}else if(accion.equals("numeroCategoriaPorObjeto")){
			String filtro_nombre = map.get("filtro_nombre");
			String filtro_usuario_creo = map.get("filtro_usuario_creo");
			String filtro_fecha_creacion = map.get("filtro_fecha_creacion");
			response_text = String.join("","{ \"success\": true, \"totalCategoriaAdquisiciones\":",CategoriaAdquisicionDAO.getTotalCategoriaAdquisicion(filtro_nombre,
					filtro_usuario_creo, filtro_fecha_creacion).toString()," }");

		}else if(accion.equals("getCategoriaAdquisicionPagina")){
			int pagina = map.get("pagina")!=null  ? Integer.parseInt(map.get("pagina")) : 0;
			int numeroCategoriaAdquisicion = map.get("numeroCategoriaAdquisicion")!=null  ? Integer.parseInt(map.get("numeroCategoriaAdquisicion")) : 0;
			String filtro_nombre = map.get("filtro_nombre");
			String filtro_usuario_creo = map.get("filtro_usuario_creo");
			String filtro_fecha_creacion = map.get("filtro_fecha_creacion");
			String columna_ordenada = map.get("columna_ordenada");
			String orden_direccion = map.get("orden_direccion");
			List<CategoriaAdquisicion> categoriaAdquisiciones = CategoriaAdquisicionDAO.getCategoriaAdquisicionPagina(pagina, numeroCategoriaAdquisicion, filtro_nombre, 
					filtro_usuario_creo, filtro_fecha_creacion, columna_ordenada, orden_direccion);
			List<stcategoriaadquisicion> lstCategoriaAdquisicion =new ArrayList<stcategoriaadquisicion>();
			for(CategoriaAdquisicion categoriaAdquisicion:categoriaAdquisiciones){
				stcategoriaadquisicion temp =new stcategoriaadquisicion();
				temp.id = categoriaAdquisicion.getId();
				temp.nombre = categoriaAdquisicion.getNombre();
				temp.descripcion = categoriaAdquisicion.getDescripcion();
				temp.usuario_creo = categoriaAdquisicion.getUsuarioCreo();
				temp.usuario_actualizo = categoriaAdquisicion.getUsuarioActualizo();
				temp.fecha_creo = Utils.formatDate(categoriaAdquisicion.getFechaCreacion());
				temp.fecha_actualizacion = Utils.formatDate(categoriaAdquisicion.getFechaActualizacion());
				temp.estado = categoriaAdquisicion.getEstado();
				lstCategoriaAdquisicion.add(temp);
			}

			response_text=new GsonBuilder().serializeNulls().create().toJson(lstCategoriaAdquisicion);
	        response_text = String.join("", "\"categoriaAdquisiciones\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");

		}else if(accion.equals("guardarCategoria")){
			int id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			boolean result = false;
			boolean esnuevo = map.get("esnuevo").equals("true");
			if(id>0 || esnuevo){
				String nombre = map.get("nombre");
				String descripcion = map.get("descripcion");
				
				CategoriaAdquisicion categoria;
				if(esnuevo){
					categoria = new CategoriaAdquisicion(nombre, descripcion, usuario, null, new Date(),null,1, null);
					
				}else{
					categoria = CategoriaAdquisicionDAO.getCategoriaPorId(id);
					categoria.setNombre(nombre);
					categoria.setDescripcion(descripcion);
					categoria.setUsuarioActualizo(usuario);
					categoria.setFechaActualizacion(new DateTime().toDate());
					
				}
				result = CategoriaAdquisicionDAO.guardarCategoria(categoria);
				
				response_text = String.join("","{ \"success\": ",(result ? "true" : "false"),", "
						+ "\"id\": " + categoria.getId() ,","
						, "\"usuarioCreo\": \"" , categoria.getUsuarioCreo(),"\","
						, "\"fechaCreacion\":\" " , Utils.formatDateHour(categoria.getFechaCreacion()),"\","
						, "\"usuarioactualizo\": \"" , categoria.getUsuarioActualizo() != null ? categoria.getUsuarioActualizo() : "","\","
						, "\"fechaactualizacion\": \"" , Utils.formatDateHour(categoria.getFechaActualizacion()),"\""+
						" }");
			}
		}else if(accion.equals("borrarCategoria")){
			int id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			if(id>0){
				CategoriaAdquisicion categoria = CategoriaAdquisicionDAO.getCategoriaPorId(id);
				categoria.setUsuarioActualizo(usuario);
				response_text = String.join("","{ \"success\": ",(CategoriaAdquisicionDAO.eliminarCategoria(categoria) ? "true" : "false")," }");
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
