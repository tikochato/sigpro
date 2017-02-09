package servlets;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.joda.time.DateTime;

import dao.ComponenteDAO;
import dao.ProductoDAO;
import pojo.Componente;
import pojo.Producto;
import pojo.ProductoTipo;
import pojo.UnidadEjecutora;
import utilities.Utils;

@WebServlet("/SProducto")
public class SProducto extends HttpServlet {
	
	private static final long serialVersionUID = 1457438583225714402L;
	String usuario ="";
	public SProducto() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession sesionweb = request.getSession();
		usuario = sesionweb.getAttribute("usuario")!= null ? sesionweb.getAttribute("usuario").toString() : null;

		Map<String, String> parametro = Utils.getParams(request);

		if (parametro.get("accion").compareTo("cargar") == 0) {
			listar(parametro, response);
		} else if (parametro.get("accion").compareTo("guardar") == 0) {
			guardar(parametro, response,request);
		} else if (parametro.get("accion").compareTo("borrar") == 0) {
			eliminar(parametro, response);
		} else if (parametro.get("accion").compareTo("totalElementos") == 0) {
			total(parametro,response);
		} else if (parametro.get("accion").compareTo("listarTipos") == 0) {
			listarTipos(parametro, response);
		} else if (parametro.get("accion").compareTo("listarProductos") == 0) {
			listarProductos(parametro, response);
		} else if (parametro.get("accion").compareTo("listarComponentes") == 0) {
			listarComponentes(parametro, response);
		}
	}

	private void listar(Map<String, String> parametro, HttpServletResponse response) throws IOException {
		int componenteid = Utils.String2Int(parametro.get("componenteid"), 0);
		int pagina = Utils.String2Int(parametro.get("pagina"), 1);
		int registros = Utils.String2Int(parametro.get("registros"), 20);
		String filtro_nombre = parametro.get("filtro_nombre");
		String filtro_usuario_creo = parametro.get("filtro_usuario_creo");
		String filtro_fecha_creacion = parametro.get("filtro_fecha_creacion");
		String columna_ordenada = parametro.get("columna_ordenada");
		String orden_direccion = parametro.get("orden_direccion");

		String resultadoJson = "";

		resultadoJson = ProductoDAO.getJson(pagina, registros,componenteid,usuario,filtro_nombre,filtro_usuario_creo
				,filtro_fecha_creacion,columna_ordenada,orden_direccion);

		if (Utils.isNullOrEmpty(resultadoJson)) {
			resultadoJson = "{\"success\":false}";
		} else {
			resultadoJson = "{\"success\":true," + resultadoJson + "}";
		}

		Utils.writeJSon(response, resultadoJson);
	}
	
	private void guardar(Map<String, String> map, HttpServletResponse response, HttpServletRequest request) throws IOException {
		
		
		boolean esnuevo = map.get("esnuevo").equals("true");
		int id = Utils.String2Int(map.get("id"));
		boolean ret = false;
		
		if (id>0 || esnuevo){
			String nombre = map.get("nombre");
			String descripcion = map.get("descripcion");

			Integer componenteId = Utils.String2Int(map.get("componente"));
			Integer productoPadreId = Utils.String2Int(map.get("productoPadre"));
			Integer tipoproductoId = Utils.String2Int(map.get("tipoproductoid")); 
			Integer unidadEjecutoraId = Utils.String2Int(map.get("unidadEjecutora"));
			
			Long snip = Utils.String2Long(map.get("snip"), null);
			Integer programa = Utils.String2Int(map.get("programa"), null);
			Integer subPrograma = Utils.String2Int(map.get("subprograma"), null);
			Integer proyecto_ = Utils.String2Int(map.get("proyecto_"), null);
			Integer obra = Utils.String2Int(map.get("obra"), null);
			Integer fuente = Utils.String2Int(map.get("fuente"), null);
			
			Componente componente = new Componente();
			componente.setId(componenteId);
			Producto productoPadre = new Producto();
			productoPadre.setId(productoPadreId);
			ProductoTipo productoTipo = new ProductoTipo();
			productoTipo.setId(tipoproductoId);
			UnidadEjecutora unidadEjecutora = new UnidadEjecutora();
			unidadEjecutora.setUnidadEjecutora(unidadEjecutoraId);
			
			Producto producto;
			
			if (esnuevo){
				producto = new Producto(componente, productoTipo, unidadEjecutora, nombre, descripcion
						, productoPadreId, usuario, null, new DateTime().toDate(),null, 1
						, snip, programa, subPrograma, proyecto_, obra, fuente, null, null);
			}else{
				producto = ProductoDAO.getProductoPorId(id);
				producto.setComponente(componente);
				producto.setProductoTipo(productoTipo);
				producto.setUnidadEjecutora(unidadEjecutora);
				producto.setNombre(nombre);
				producto.setDescripcion(descripcion);
				producto.setProductoid(productoPadreId);
				producto.setSnip(snip);
				producto.setPrograma(programa);
				producto.setSubprograma(subPrograma);
				producto.setProyecto(proyecto_);
				producto.setActividadObra(obra);
				producto.setFuente(fuente);
				producto.setUsuarioActualizo(usuario);
				producto.setFechaActualizacion(new DateTime().toDate());
			}
			
			ret = ProductoDAO.guardarProducto(producto);
			
		}
		if (ret) {
			listar(map, response);
		}
	}

	

	

	private void eliminar(Map<String, String> parametro, HttpServletResponse response) throws IOException {
		int codigo = Utils.String2Int(parametro.get("codigo"), -1);
		String usuario = parametro.get("usuario");

		boolean eliminado = ProductoDAO.eliminar(codigo, usuario);
		if (eliminado) {
			listar(parametro, response);
		}
	}

	private void total(Map<String, String> parametro,HttpServletResponse response) throws IOException {
		int componenteid = Utils.String2Int(parametro.get("componenteid"), 0);
		Long total = ProductoDAO.getTotalProductos(componenteid,usuario);

		String resultadoJson = "{\"success\":true, \"total\":" + total + "}";

		Utils.writeJSon(response, resultadoJson);
	}

	private void listarTipos(Map<String, String> parametro, HttpServletResponse response) throws IOException {
		int pagina = Utils.String2Int(parametro.get("pagina"), 1);
		int registros = Utils.String2Int(parametro.get("registros"), 20);
		int componenteid = Utils.String2Int(parametro.get("componenteid"), 0);
		
		String filtro_nombre = parametro.get("filtro_nombre");
		String filtro_usuario_creo = parametro.get("filtro_usuario_creo");
		String filtro_fecha_creacion = parametro.get("filtro_fecha_creacion");
		String columna_ordenada = parametro.get("columna_ordenada");
		String orden_direccion = parametro.get("orden_direccion");

		

		String resultadoJson = "";

		resultadoJson = ProductoDAO.getJson(pagina, registros,componenteid,usuario,
				filtro_nombre,filtro_usuario_creo
				,filtro_fecha_creacion,columna_ordenada,orden_direccion);

		if (Utils.isNullOrEmpty(resultadoJson)) {
			resultadoJson = "{\"success\":false}";
		} else {
			resultadoJson = "{\"success\":true," + resultadoJson + "}";
		}

		Utils.writeJSon(response, resultadoJson);
	}

	private void listarProductos(Map<String, String> parametro, HttpServletResponse response) throws IOException {
		int pagina = Utils.String2Int(parametro.get("pagina"), 1);
		int registros = Utils.String2Int(parametro.get("registros"), 20);
		int componenteid = Utils.String2Int(parametro.get("componenteid"), 0);
		String filtro_nombre = parametro.get("filtro_nombre");
		String filtro_usuario_creo = parametro.get("filtro_usuario_creo");
		String filtro_fecha_creacion = parametro.get("filtro_fecha_creacion");
		String columna_ordenada = parametro.get("columna_ordenada");
		String orden_direccion = parametro.get("orden_direccion");


		String resultadoJson = "";

		resultadoJson = Utils.getJSonString("productos", ProductoDAO.getProductosPagina(pagina, registros,componenteid,usuario,
				filtro_nombre,filtro_usuario_creo
				,filtro_fecha_creacion,columna_ordenada,orden_direccion));

		if (Utils.isNullOrEmpty(resultadoJson)) {
			resultadoJson = "{\"success\":false}";
		} else {
			resultadoJson = "{\"success\":true," + resultadoJson + "}";
		}

		Utils.writeJSon(response, resultadoJson);
	}

	private void listarComponentes(Map<String, String> parametro, HttpServletResponse response) throws IOException {
		int pagina = Utils.String2Int(parametro.get("pagina"), 1);
		int registros = Utils.String2Int(parametro.get("registros"), 20);

		String resultadoJson = "";

		resultadoJson = Utils.getJSonString("productos", ComponenteDAO.getComponentesPagina(pagina, registros,usuario));

		if (Utils.isNullOrEmpty(resultadoJson)) {
			resultadoJson = "{\"success\":false}";
		} else {
			resultadoJson = "{\"success\":true," + resultadoJson + "}";
		}

		Utils.writeJSon(response, resultadoJson);
	}
}
