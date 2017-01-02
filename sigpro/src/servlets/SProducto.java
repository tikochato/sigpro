package servlets;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.ComponenteDAO;
import dao.ProductoDAO;
import utilities.Utils;

@WebServlet("/SProducto")
public class SProducto extends HttpServlet {
	
	private static final long serialVersionUID = 1457438583225714402L;

	public SProducto() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Map<String, String> parametro = Utils.getParams(request);

		if (parametro.get("accion").compareTo("cargar") == 0) {
			listar(parametro, response);
		} else if (parametro.get("accion").compareTo("crear") == 0) {
			crear(parametro, response);
		} else if (parametro.get("accion").compareTo("actualizar") == 0) {
			actualizar(parametro, response);
		} else if (parametro.get("accion").compareTo("borrar") == 0) {
			eliminar(parametro, response);
		} else if (parametro.get("accion").compareTo("totalElementos") == 0) {
			total(response);
		} else if (parametro.get("accion").compareTo("listarTipos") == 0) {
			listarTipos(parametro, response);
		} else if (parametro.get("accion").compareTo("listarProductos") == 0) {
			listarProductos(parametro, response);
		} else if (parametro.get("accion").compareTo("listarComponentes") == 0) {
			listarComponentes(parametro, response);
		}
	}

	private void listar(Map<String, String> parametro, HttpServletResponse response) throws IOException {
		int pagina = Utils.String2Int(parametro.get("pagina"), 1);
		int registros = Utils.String2Int(parametro.get("registros"), 20);

		String resultadoJson = "";

		resultadoJson = ProductoDAO.getJson(pagina, registros);

		if (Utils.isNullOrEmpty(resultadoJson)) {
			resultadoJson = "{\"success\":false}";
		} else {
			resultadoJson = "{\"success\":true," + resultadoJson + "}";
		}

		Utils.writeJSon(response, resultadoJson);
	}

	private void crear(Map<String, String> parametro, HttpServletResponse response) throws IOException {

		String nombre = parametro.get("nombre");
		String descripcion = parametro.get("descripcion");

		Integer componente = Utils.String2Int(parametro.get("componente"));
		Integer productoPadre = Utils.String2Int(parametro.get("productoPadre"));
		Integer tipo = Utils.String2Int(parametro.get("tipo"));

		// todos estos pueden o deben ser JSON con la EstructuraPojo de su
		// respectivo DAO
		String propiedades = parametro.get("propiedades");
		String actividades = parametro.get("actividades");
		String usuario = parametro.get("usuario");

		boolean creado = ProductoDAO.guardar(nombre, descripcion, componente, productoPadre, tipo, propiedades,
				actividades, usuario);

		if (creado) {
			listar(parametro, response);
		}
	}

	private void actualizar(Map<String, String> parametro, HttpServletResponse response) throws IOException {

		int productoId = Utils.String2Int(parametro.get("codigo"), -1);
		String nombre = parametro.get("nombre");
		String descripcion = parametro.get("descripcion");

		Integer componente = Utils.String2Int(parametro.get("componente"));
		Integer productoPadre = Utils.String2Int(parametro.get("productoPadre"));
		Integer tipo = Utils.String2Int(parametro.get("tipo"));

		// todos estos pueden o deben ser JSON con la EstructuraPojo de su
		// respectivo DAO
		String propiedades = parametro.get("propiedades");
		String actividades = parametro.get("actividades");
		String usuario = parametro.get("usuario");

		boolean actualizado = ProductoDAO.actualizar(productoId, nombre, descripcion, componente, productoPadre, tipo,
				propiedades, actividades, usuario);
		if (actualizado) {
			listar(parametro, response);
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

	private void total(HttpServletResponse response) throws IOException {
		Long total = ProductoDAO.getTotalProductos();

		String resultadoJson = "{\"success\":true, \"total\":" + total + "}";

		Utils.writeJSon(response, resultadoJson);
	}

	private void listarTipos(Map<String, String> parametro, HttpServletResponse response) throws IOException {
		int pagina = Utils.String2Int(parametro.get("pagina"), 1);
		int registros = Utils.String2Int(parametro.get("registros"), 20);

		String resultadoJson = "";

		resultadoJson = ProductoDAO.getJson(pagina, registros);

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

		String resultadoJson = "";

		resultadoJson = Utils.getJSonString("productos", ProductoDAO.getProductosPagina(pagina, registros));

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

		resultadoJson = Utils.getJSonString("productos", ComponenteDAO.getComponentesPagina(pagina, registros));

		if (Utils.isNullOrEmpty(resultadoJson)) {
			resultadoJson = "{\"success\":false}";
		} else {
			resultadoJson = "{\"success\":true," + resultadoJson + "}";
		}

		Utils.writeJSon(response, resultadoJson);
	}
}
