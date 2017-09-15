package servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.GsonBuilder;

import dao.ProdTipoPropiedadDAO;
import dao.ProductoTipoDAO;
import pojo.ProductoTipo;
import utilities.Utils;

/**
 * Servlet implementation class SProductoTipo
 */
@WebServlet("/SProductoTipo")
public class SProductoTipo extends HttpServlet {
	private static final long serialVersionUID = -6537014370076177564L;
	class stproductotipo {
		Integer id;
		String nombre;
		String descripcion;
		String usuarioCreo;
		String usuarioActualizo;
		String fechaCreacion;
		String fechaActualizacion;
		int estado;
	}

	public SProductoTipo() {
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
		Map<String, String> parametro = Utils.getParams(request);
		HttpSession sesionweb = request.getSession();
		String usuario = sesionweb.getAttribute("usuario")!= null ? sesionweb.getAttribute("usuario").toString() : null;
		String accion = parametro.get("accion");
		String response_text="";
		
		if (accion.equals("cargar")) {
			int pagina = Utils.String2Int(parametro.get("pagina"), 1);
			int registros = Utils.String2Int(parametro.get("registros"), 20);
			String filtro_nombre = parametro.get("filtro_nombre");
			String filtro_usuario_creo = parametro.get("filtro_usuario_creo");
			String filtro_fecha_creacion = parametro.get("filtro_fecha_creacion");
			String columna_ordenada = parametro.get("columna_ordenada");
			String orden_direccion = parametro.get("orden_direccion");
			
			
			List<ProductoTipo> productotipos = ProductoTipoDAO.getPagina(pagina, registros,filtro_nombre,filtro_usuario_creo,filtro_fecha_creacion,columna_ordenada,orden_direccion);
			List<stproductotipo> listaProductoTipo = new ArrayList<stproductotipo>();

			for (ProductoTipo productotipo : productotipos) {
				stproductotipo temp = new stproductotipo();
				temp.id = productotipo.getId();
				temp.nombre = productotipo.getNombre();
				temp.descripcion = productotipo.getDescripcion();
				temp.usuarioCreo = productotipo.getUsuarioCreo();
				temp.usuarioActualizo = productotipo.getUsuarioActualizo();
				temp.fechaCreacion = Utils.formatDateHour(productotipo.getFechaCreacion());
				temp.fechaActualizacion = Utils.formatDateHour(productotipo.getFechaActualizacion());
				temp.estado = productotipo.getEstado();

				listaProductoTipo.add(temp);
			}

				response_text=new GsonBuilder().serializeNulls().create().toJson(listaProductoTipo);
		        response_text = String.join("", "\"productoTipos\":",response_text);
		        response_text = String.join("", "{\"success\":true,", response_text,"}");	
			
			
		} else if (accion.equals("crear")) {
			String nombre = parametro.get("nombre");
			String descripcion = parametro.get("descripcion");
			String propiedades = parametro.get("propiedades");
			Integer creado = ProductoTipoDAO.guardar(-1, nombre, descripcion, propiedades, usuario);
			
			ProductoTipo producto = ProductoTipoDAO.getProductoTipo(creado); 
			
			response_text = String.join("","{ \"success\": ",(creado!=null ? "true" : "false"),", "
				, "\"id\": " , creado.toString() , ","
				, "\"usuarioCreo\": \"" , producto.getUsuarioCreo(),"\","
				, "\"fechaCreacion\":\" " , Utils.formatDateHour(producto.getFechaCreacion()),"\","
				, "\"usuarioactualizo\": \"" , producto.getUsuarioActualizo() != null ? producto.getUsuarioActualizo() : "","\","
				, "\"fechaactualizacion\": \"" , Utils.formatDateHour(producto.getFechaActualizacion()),"\""
				," }");
			
		} else if (accion.equals("actualizar")) {
			int codigo = Utils.String2Int(parametro.get("codigo"), -1);
			String nombre = parametro.get("nombre");
			String descripcion = parametro.get("descripcion");
			String propiedades = parametro.get("propiedades");
			boolean actualizado = ProductoTipoDAO.actualizar(codigo, nombre, descripcion, propiedades, usuario);
			
			ProductoTipo producto = ProductoTipoDAO.getProductoTipo(codigo); 
						
			response_text = String.join("","{ \"success\": ",(actualizado ? "true" : "false"),", "
				, "\"id\": " , producto.getId().toString() , ","
				, "\"usuarioCreo\": \"" , producto.getUsuarioCreo(),"\","
				, "\"fechaCreacion\":\" " , Utils.formatDateHour(producto.getFechaCreacion()),"\","
				, "\"usuarioactualizo\": \"" , producto.getUsuarioActualizo() != null ? producto.getUsuarioActualizo() : "","\","
				, "\"fechaactualizacion\": \"" , Utils.formatDateHour(producto.getFechaActualizacion()),"\""
				," }");

		} else if (accion.equals("borrar")) {
			int codigo = Utils.String2Int(parametro.get("codigo"), -1);
			String usuarioElimina = parametro.get("usuario");
			boolean eliminado = ProductoTipoDAO.eliminar(codigo, usuarioElimina);
			if (eliminado) {
				int pagina = Utils.String2Int(parametro.get("pagina"), 1);
				int registros = Utils.String2Int(parametro.get("registros"), 20);
				String filtro_nombre = parametro.get("filtro_nombre");
				String filtro_usuario_creo = parametro.get("filtro_usuario_creo");
				String filtro_fecha_creacion = parametro.get("filtro_fecha_creacion");
				String columna_ordenada = parametro.get("columna_ordenada");
				String orden_direccion = parametro.get("orden_direccion");
				
				List<ProductoTipo> productotipos = ProductoTipoDAO.getPagina(pagina, registros,filtro_nombre,filtro_usuario_creo,filtro_fecha_creacion,columna_ordenada,orden_direccion);
				List<stproductotipo> listaProductoTipo = new ArrayList<stproductotipo>();

				for (ProductoTipo productotipo : productotipos) {
					stproductotipo temp = new stproductotipo();
					temp.id = productotipo.getId();
					temp.nombre = productotipo.getNombre();
					temp.descripcion = productotipo.getDescripcion();
					temp.usuarioCreo = productotipo.getUsuarioCreo();
					temp.usuarioActualizo = productotipo.getUsuarioActualizo();
					temp.fechaCreacion = Utils.formatDateHour(productotipo.getFechaCreacion());
					temp.fechaActualizacion = Utils.formatDateHour(productotipo.getFechaActualizacion());
					temp.estado = productotipo.getEstado();

					listaProductoTipo.add(temp);
				}

					response_text=new GsonBuilder().serializeNulls().create().toJson(listaProductoTipo);
			        response_text = String.join("", "\"productoTipos\":",response_text);
			        response_text = String.join("", "{\"success\":true,", response_text,"}");	
				
			}
		} else if (accion.equals("totalElementos")) {
			String filtro_nombre = parametro.get("filtro_nombre");
			String filtro_usuario_creo = parametro.get("filtro_usuario_creo");
			String filtro_fecha_creacion = parametro.get("filtro_fecha_creacion");
			response_text = String.join("","{ \"success\":true, \"total\":",ProductoTipoDAO.getTotal(filtro_nombre,filtro_usuario_creo,filtro_fecha_creacion).toString()," }");

		} else if (accion.equals("tipoPropiedades")) {
			Integer codigoTipo = Utils.String2Int(parametro.get("codigoTipo"), 0);
			response_text = ProdTipoPropiedadDAO.getJson(codigoTipo);
			if (Utils.isNullOrEmpty(response_text)) {
				response_text = "{\"success\":false}";
			} else {
				response_text = String.join("","{ \"success\":true," , response_text , " }");
			}
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
