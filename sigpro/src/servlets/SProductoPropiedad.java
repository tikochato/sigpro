package servlets;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.ProductoPropiedadDAO;
import utilities.Utils;

@WebServlet("/SProductoPropiedad")
public class SProductoPropiedad extends HttpServlet {
	private static final long serialVersionUID = -6537014370076177564L;

	public SProductoPropiedad() {
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
		} else if (parametro.get("accion").compareTo("getProductoPropiedadPorTipo") == 0) {
			listarProductoPropiedadPorTipo(parametro,response);
		}
	}

	private void listar(Map<String, String> parametro, HttpServletResponse response) throws IOException {
		int pagina = Utils.String2Int(parametro.get("pagina"), 1);
		int registros = Utils.String2Int(parametro.get("registros"), 20);

		String resultadoJson = "";

		resultadoJson = ProductoPropiedadDAO.getJson(pagina, registros);

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
		String usuario = parametro.get("usuario");

		int tipo = Utils.String2Int(parametro.get("tipo"), -1);

		boolean creado = ProductoPropiedadDAO.guardar(-1, nombre, descripcion, usuario, tipo);
		
		if (creado) {
			listar(parametro, response);
		}
	}

	private void actualizar(Map<String, String> parametro, HttpServletResponse response) throws IOException {
		int codigo = Utils.String2Int(parametro.get("codigo"), -1);
		String nombre = parametro.get("nombre");
		String descripcion = parametro.get("descripcion");
		String usuario = parametro.get("usuario");

		int tipo = Utils.String2Int(parametro.get("tipo"), -1);

		boolean actualizado = ProductoPropiedadDAO.actualizar(codigo, nombre, descripcion, usuario, tipo);
		if (actualizado) {
			listar(parametro, response);
		}
	}

	private void eliminar(Map<String, String> parametro, HttpServletResponse response) throws IOException {
		int codigo = Utils.String2Int(parametro.get("codigo"), -1);
		String usuario = parametro.get("usuario");

		boolean eliminado = ProductoPropiedadDAO.eliminar(codigo, usuario);
		if (eliminado) {
			listar(parametro, response);
		}
	}

	private void total(HttpServletResponse response) throws IOException {
		Long total = ProductoPropiedadDAO.getTotal();

		String resultadoJson = "{\"success\":true, \"total\":" + total + "}";

		Utils.writeJSon(response, resultadoJson);
	}
	
	private void listarProductoPropiedadPorTipo (Map<String, String> parametro, HttpServletResponse response) throws IOException {
		int idProducto = Utils.String2Int(parametro.get("idproducto"), -1);
		int idProductoTio = Utils.String2Int(parametro.get("idproductotipo"), -1);
		
		String productoPropiedades = ProductoPropiedadDAO.getJsonPorTipo(idProductoTio,idProducto);
		
		if (Utils.isNullOrEmpty(productoPropiedades)) {
			productoPropiedades = "{\"success\":false}";
		}
		Utils.writeJSon(response, productoPropiedades);
	}

}
