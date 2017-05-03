package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import dao.ProductoPropiedadDAO;
import pojo.ProductoPropiedad;
import utilities.Utils;

@WebServlet("/SProductoPropiedad")
public class SProductoPropiedad extends HttpServlet {
	private static final long serialVersionUID = -6537014370076177564L;
	
	static class productoPropiedad {
		Integer id;
		String nombre;
		String descripcion;
		Integer idTipo;
		String tipo;
		String usuarioCreo;
		String usuarioActualizo;
		String fechaCreacion;
		String fechaActualizacion;
	}
	
	public SProductoPropiedad() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String response_text = "{ \"success\": false }";

		response.setHeader("Content-Encoding", "gzip");
		response.setCharacterEncoding("UTF-8");

        OutputStream output = response.getOutputStream();
		GZIPOutputStream gz = new GZIPOutputStream(output);
        gz.write(response_text.getBytes("UTF-8"));
        gz.close();
        output.close();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
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
		Map<String, String> parametro = gson.fromJson(sb.toString(), type);
		String accion = parametro.get("accion")!=null ? parametro.get("accion") : "";
		String response_text = "";

		if (accion.equals("cargar")) {
			int pagina = Utils.String2Int(parametro.get("pagina"), 1);
			int registros = Utils.String2Int(parametro.get("registros"), 20);
			String filtro_nombre = parametro.get("filtro_nombre");
			String filtro_usuario_creo = parametro.get("filtro_usuario_creo");
			String filtro_fecha_creacion = parametro.get("filtro_fecha_creacion");
			String columna_ordenada = parametro.get("columna_ordenada");
			String orden_direccion = parametro.get("orden_direccion");

			List<ProductoPropiedad> pojos = ProductoPropiedadDAO.getPagina(pagina, registros, filtro_nombre,filtro_usuario_creo,filtro_fecha_creacion,columna_ordenada,orden_direccion);

			List<productoPropiedad> listaEstructuraPojos = new ArrayList<productoPropiedad>();

			for (ProductoPropiedad pojo : pojos) {
				productoPropiedad estructuraPojo = new productoPropiedad();
				estructuraPojo.id = pojo.getId();
				estructuraPojo.nombre = pojo.getNombre();
				estructuraPojo.descripcion = pojo.getDescripcion();
				estructuraPojo.idTipo = pojo.getDatoTipo().getId();
				estructuraPojo.tipo = pojo.getDatoTipo().getNombre();
				estructuraPojo.usuarioCreo = pojo.getUsuarioCreo();
				estructuraPojo.usuarioActualizo = pojo.getUsuarioActualizo();
				estructuraPojo.fechaCreacion = Utils.formatDateHour(pojo.getFechaCreacion());
				estructuraPojo.fechaActualizacion =Utils.formatDateHour(pojo.getFechaActualizacion());

				listaEstructuraPojos.add(estructuraPojo);
			}

			response_text=new GsonBuilder().serializeNulls().create().toJson(listaEstructuraPojos);
	        response_text = String.join("", "\"productoPropiedades\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
			

		} else if (accion.equals("crear")) {
			String nombre = parametro.get("nombre");
			String descripcion = parametro.get("descripcion");
			
			int tipo = Utils.String2Int(parametro.get("tipo"), -1);

			Integer creado = ProductoPropiedadDAO.guardar(-1, nombre, descripcion, usuario , tipo);
			ProductoPropiedad productoPropiedad = ProductoPropiedadDAO.getProductoPropiedadPorId(creado);
			response_text = String.join("","{ \"success\": ",(creado!=null ? "true" : "false"),", "
					, "\"id\": " , creado.toString() , ","
					, "\"usuarioCreo\": \"" , productoPropiedad.getUsuarioCreo(),"\","
					, "\"fechaCreacion\":\" " , Utils.formatDateHour(productoPropiedad.getFechaCreacion()),"\","
					, "\"usuarioactualizo\": \"" , productoPropiedad.getUsuarioActualizo() != null ? productoPropiedad.getUsuarioActualizo() : "","\","
					, "\"fechaactualizacion\": \"" , Utils.formatDateHour(productoPropiedad.getFechaActualizacion()),"\""
					," }");
			
		} else if (accion.equals("actualizar")) {
			int codigo = Utils.String2Int(parametro.get("codigo"), -1);
			String nombre = parametro.get("nombre");
			String descripcion = parametro.get("descripcion");
			int tipo = Utils.String2Int(parametro.get("tipo"), -1);
			boolean actualizado = ProductoPropiedadDAO.actualizar(codigo, nombre, descripcion, usuario, tipo);
			if (actualizado) {
				int pagina = Utils.String2Int(parametro.get("pagina"), 1);
				int registros = Utils.String2Int(parametro.get("registros"), 20);
				String filtro_nombre = parametro.get("filtro_nombre");
				String filtro_usuario_creo = parametro.get("filtro_usuario_creo");
				String filtro_fecha_creacion = parametro.get("filtro_fecha_creacion");
				String columna_ordenada = parametro.get("columna_ordenada");
				String orden_direccion = parametro.get("orden_direccion");

				List<ProductoPropiedad> pojos = ProductoPropiedadDAO.getPagina(pagina, registros, filtro_nombre,filtro_usuario_creo,filtro_fecha_creacion,columna_ordenada,orden_direccion);

				List<productoPropiedad> listaEstructuraPojos = new ArrayList<productoPropiedad>();

				for (ProductoPropiedad pojo : pojos) {
					productoPropiedad estructuraPojo = new productoPropiedad();
					estructuraPojo.id = pojo.getId();
					estructuraPojo.nombre = pojo.getNombre();
					estructuraPojo.descripcion = pojo.getDescripcion();
					estructuraPojo.idTipo = pojo.getDatoTipo().getId();
					estructuraPojo.tipo = pojo.getDatoTipo().getNombre();
					estructuraPojo.usuarioCreo = pojo.getUsuarioCreo();
					estructuraPojo.usuarioActualizo = pojo.getUsuarioActualizo();
					estructuraPojo.fechaCreacion = Utils.formatDateHour(pojo.getFechaCreacion());
					estructuraPojo.fechaActualizacion =Utils.formatDateHour(pojo.getFechaActualizacion());

					listaEstructuraPojos.add(estructuraPojo);
				}
				ProductoPropiedad productoPropiedad = ProductoPropiedadDAO.getProductoPropiedad(codigo);
				
				response_text = String.join("","{ \"success\": ",(actualizado ? "true" : "false"),", "
						, "\"id\": " , productoPropiedad.getId().toString() , ","
						, "\"usuarioCreo\": \"" , productoPropiedad.getUsuarioCreo(),"\","
						, "\"fechaCreacion\":\" " , Utils.formatDateHour(productoPropiedad.getFechaCreacion()),"\","
						, "\"usuarioactualizo\": \"" , productoPropiedad.getUsuarioActualizo() != null ? productoPropiedad.getUsuarioActualizo() : "","\","
						, "\"fechaactualizacion\": \"" , Utils.formatDateHour(productoPropiedad.getFechaActualizacion()),"\""
						," }");
				
			}
		} else if (accion.equals("borrar")) {
			int codigo = Utils.String2Int(parametro.get("codigo"), -1);
			String usuarioc = parametro.get("usuario");

			boolean eliminado = ProductoPropiedadDAO.eliminar(codigo, usuarioc);
			if (eliminado) {
				int pagina = Utils.String2Int(parametro.get("pagina"), 1);
				int registros = Utils.String2Int(parametro.get("registros"), 20);
				String filtro_nombre = parametro.get("filtro_nombre");
				String filtro_usuario_creo = parametro.get("filtro_usuario_creo");
				String filtro_fecha_creacion = parametro.get("filtro_fecha_creacion");
				String columna_ordenada = parametro.get("columna_ordenada");
				String orden_direccion = parametro.get("orden_direccion");

				List<ProductoPropiedad> pojos = ProductoPropiedadDAO.getPagina(pagina, registros, filtro_nombre,filtro_usuario_creo,filtro_fecha_creacion,columna_ordenada,orden_direccion);

				List<productoPropiedad> listaEstructuraPojos = new ArrayList<productoPropiedad>();

				for (ProductoPropiedad pojo : pojos) {
					productoPropiedad estructuraPojo = new productoPropiedad();
					estructuraPojo.id = pojo.getId();
					estructuraPojo.nombre = pojo.getNombre();
					estructuraPojo.descripcion = pojo.getDescripcion();
					estructuraPojo.idTipo = pojo.getDatoTipo().getId();
					estructuraPojo.tipo = pojo.getDatoTipo().getNombre();
					estructuraPojo.usuarioCreo = pojo.getUsuarioCreo();
					estructuraPojo.usuarioActualizo = pojo.getUsuarioActualizo();
					estructuraPojo.fechaCreacion = Utils.formatDateHour(pojo.getFechaCreacion());
					estructuraPojo.fechaActualizacion =Utils.formatDateHour(pojo.getFechaActualizacion());

					listaEstructuraPojos.add(estructuraPojo);
				}

				response_text=new GsonBuilder().serializeNulls().create().toJson(listaEstructuraPojos);
		        response_text = String.join("", "\"productoPropiedades\":",response_text);
		        response_text = String.join("", "{\"success\":true,", response_text,"}");
				
			}
		} else if (accion.equals("totalElementos")) {
			String filtro_nombre = parametro.get("filtro_nombre");
			String filtro_usuario_creo = parametro.get("filtro_usuario_creo");
			String filtro_fecha_creacion = parametro.get("filtro_fecha_creacion");
			Long total = ProductoPropiedadDAO.getTotal(filtro_nombre,filtro_usuario_creo,filtro_fecha_creacion);

			response_text = "{\"success\":true, \"total\":" + total + "}";
		} else if (accion.equals("getProductoPropiedadPorTipo")) {
			int idProducto = Utils.String2Int(parametro.get("idproducto"), -1);
			int idProductoTio = Utils.String2Int(parametro.get("idproductotipo"), -1);
			
			response_text = ProductoPropiedadDAO.getJsonPorTipo(idProductoTio,idProducto);
			
			if (Utils.isNullOrEmpty(response_text)) {
				response_text = "{\"success\":false}";
			}
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
